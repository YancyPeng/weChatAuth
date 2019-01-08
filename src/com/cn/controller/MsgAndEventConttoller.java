package com.cn.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.controller.weChat.util.HttpClientUtil;
import com.cn.controller.weChat.util.IPUtil;
import com.cn.controller.weChat.util.SignUtil;
import com.cn.controller.weChat.util.XmlUtil;
import com.cn.model.weChat.ReplyInfo;
import com.cn.service.IAccessTokenInfoSV;
import com.cn.service.IKeyWordSV;
import com.cn.service.IReplyInfoSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YancyPeng on 2018/10/24.
 * 消息和事件控制类，用于接受公众号消息和事件推送
 * 对应于第三方平台申请时设置的公众号消息与事件接受URL
 */
@RestController
@RequestMapping("/msgAndEvent")
//@Api(value = "/msgAndEvent", description = "消息和事件控制类，用于接受公众号消息和事件推送对应于第三方平台申请时设置的公众号消息与事件接受URL")
public class MsgAndEventConttoller {

    //    private static final Logger LOGGER = LoggerFactory.getLogger(MsgAndEventConttoller.class);
//
    @Autowired
    private IReplyInfoSV iReplyInfoSV;

    @Autowired
    private IKeyWordSV iKeyWordSV;

    @Autowired
    private IAccessTokenInfoSV iAccessTokenInfoSV;

    private String componentAppid = "wx3faff19fa0b9cee8";

    /**
     * 事件和消息回调函数
     *
     * @param authorizerAppid 接受到的公众号appid
     * @param postData        接收到的消息或事件的xml
     * @return
     */
    @RequestMapping(value = "/{authorizerAppid}/callback", method = RequestMethod.POST)
    public void callback(@PathVariable String authorizerAppid, @RequestBody String postData,
                         HttpServletResponse response, HttpServletRequest request) {

        System.out.println("消息与事件中发起请求的客户端地址为：------------------------" + IPUtil.getLocalIp(request));
        System.out.println("执行消息与事件接受方法  <callback> 的入参为：--------------------------" + postData);
        String resultData = SignUtil.decryptMsg(request, postData); // 解密后的xml
        try {
            Map<String, String> dataMap = XmlUtil.xmlToMap(resultData); // xml转换后的map
            System.out.println("解密后的公众号消息与事件的map为-------------------------" + dataMap);
            if (dataMap.get("MsgType").equals("event")) { // 如果接受到的是事件
                this.replyEvent(dataMap, authorizerAppid, response);
            } else { // 如果接受到的是消息
                this.replyMsg(dataMap, authorizerAppid, response);
            }
        } catch (Exception e) {
            System.out.println("执行消息与事件接受方法 <callback> 时回复消息失败");
            e.printStackTrace();
            this.output(response, "success");
        }

    }

    /**
     * 回复事件
     *
     * @param map 事件xml转换成的map
     * @return 返回对事件的回复（加密的xml）
     */
    private void replyEvent(Map<String, String> map, String authorizerAppid, HttpServletResponse response) {
        String event = map.get("Event");
        switch (event) {
            case "subscribe":  // 关注事件
                this.replySubscribeEvent(map, authorizerAppid, response);
                break;
            case "unsubscribe": // 取消关注事件
                this.output(response, "success");
                break;
            case "CLICK": // 点击事件
                this.replyClickEvent(map, response);
                break;
            case "VIEW": // 菜单链接事件
                this.output(response, "success");
                break;
            default:
                this.output(response, "success");
                break;
        }

    }

    /**
     * 回复订阅事件
     *
     * @param map 事件xml转换成的map
     * @return 对订阅事件的回复（加密的xml）
     */
    private void replySubscribeEvent(Map<String, String> map, String authorizerAppid, HttpServletResponse response) {
        System.out.println("接收到了关注事件------------------------------");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("keyWord", "订阅");
        jsonObject.put("authorizerAppid", authorizerAppid);
        String replyId = iKeyWordSV.selectByKeyWord(jsonObject);
        if (replyId != null) {
            ReplyInfo replyInfo = iReplyInfoSV.selectByReplyId(replyId);
            Map<String, Object> sendMap = this.setPubMap(map, replyInfo);
            System.out.println("发送给用户的订阅回复消息为：--------------------------" + sendMap);
            String sendXml = XmlUtil.mapToXml(sendMap);
            String encryptXml = SignUtil.encryptMsg(sendXml);
            this.output(response, encryptXml);
        } else {
            this.output(response, "success");
        }
    }

    /**
     * 回复点击菜单拉取消息事件
     *
     * @param map 事件xml转换成的map
     * @return 对点击事件的回复(加密的xml)
     */
    private void replyClickEvent(Map<String, String> map, HttpServletResponse response) {
        System.out.println("接受到了用户的点击菜单事件-----------------------");
        String eventKey = map.get("EventKey"); // 这个key对应于回复表中的replyId
        ReplyInfo replyInfo = iReplyInfoSV.selectByReplyId(eventKey);
        Map<String, Object> sendMap = this.setPubMap(map, replyInfo);
        String sendXml = XmlUtil.mapToXml(sendMap);
        String encryptXml = SignUtil.encryptMsg(sendXml);
        this.output(response, encryptXml);
    }


    /**
     * 回复消息
     *
     * @param map             事件xml转换成的map
     * @param authorizerAppid 公众号appid
     * @return
     */
    private void replyMsg(Map<String, String> map, String authorizerAppid, HttpServletResponse response) {
        String msgType = map.get("MsgType");
        switch (msgType) {
            case "text":   // 文本消息
                this.replyTextMsg(map, authorizerAppid, response);
                break;
            case "image":   // 图片消息
                this.output(response, "success");
                break;
            case "voice":   // 语音消息
                this.output(response, "success");
                break;
            case "video":   // 视频消息
                this.output(response, "success");
                break;
            case "shortvideo":  // 小视频消息
                this.output(response, "success");
                break;
            case "location":    // 地理位置消息
                this.output(response, "success");
                break;
            case "link":    // 链接消息
                this.output(response, "success");
                break;
            default:
                this.output(response, "success");
                break;
        }
    }

    /**
     * 回复文本消息
     *
     * @param map
     * @param authorizerAppid
     * @return
     */
    private void replyTextMsg(Map<String, String> map, String authorizerAppid, HttpServletResponse response) {
        String content = map.get("Content");
        System.out.println("接受到的用户发送的文本消息为：---------------" + content);
        Map<String, Object> sendMap = new HashMap<>();
        ReplyInfo replyInfo = null;
        if (content.equals("TESTCOMPONENT_MSG_TYPE_TEXT")) {// 全网发布检测模拟粉丝发送文本消息给专用测试公众号，直接回复固定content
            sendMap = this.setPubMap(map, replyInfo);
            sendMap.put("Content", "TESTCOMPONENT_MSG_TYPE_TEXT_callback");
            String sendXml = XmlUtil.mapToXml(sendMap);
            String encryptXml = SignUtil.encryptMsg(sendXml);
            System.out.println("全网发布检测模拟粉丝发送文本消息给专用测试公众号，准备发送的加密xml为：" + encryptXml);
            this.output(response, encryptXml);
        } else if (content.startsWith("QUERY_AUTH_CODE")) {   // 全网发布检测模拟粉丝发送文本消息给专用测试公众号，第三方平台方需在5秒内返回空串表明暂时不回复，然后再立即使用客服消息接口发送消息回复粉丝
            System.out.println("接收到粉丝发送的消息为：--------------------------" + content);
            // 首先回复空串
            sendMap.put("Content", "");
            String sendXml = XmlUtil.mapToXml(sendMap);
            String encryptXml = SignUtil.encryptMsg(sendXml);
            this.output(response, encryptXml);

            // 拿到$query_auth_code$的值调用 使用授权码换取公众号的授权信息API，用来获得接口调用凭据accessToken
            String query_auth_code = content.substring(16);
            System.out.println("得到的query_auth_code值为：--------------------" + query_auth_code);
            String componentAccessToken = iAccessTokenInfoSV.selectActInfo().getComponentAccessToken();
            JSONObject params = new JSONObject();
            params.put("component_appid", componentAppid);
            params.put("authorization_code", query_auth_code);
            String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=" + componentAccessToken,
                    params.toJSONString());

            // 在结果中拿到accessToken
            JSONObject resultJson = JSONObject.parseObject(result);
            String accessToken = resultJson.getJSONObject("authorization_info").getString("authorizer_access_token");
            System.out.println("得到的AccessToken为：-------------------" + accessToken);

            // 调用客服消息接口发送消息回复粉丝
            String touser = map.get("FromUserName");
            JSONObject text = new JSONObject();
            text.put("content", query_auth_code + "_from_api");
            JSONObject params1 = new JSONObject();
            params1.put("touser", touser);
            params1.put("msgtype", "text");
            params1.put("text", text);

            HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken, params1.toJSONString());

        } else {// 对应于正式消息，先到关键词表中查询replyId，根据replyId查询回复

            JSONObject params = new JSONObject();
            params.put("keyWord", content);
            params.put("authorizerAppid", authorizerAppid);
            String replyId = iKeyWordSV.selectByKeyWord(params);
            if (replyId != null) {
                replyInfo = iReplyInfoSV.selectByReplyId(replyId);
                System.out.println("回复给用户的replyInfo 为： --------------" + replyInfo);
                sendMap = this.setPubMap(map, replyInfo);
                System.out.println("回复给用户的消息为：-------------------- sendMap = " + sendMap);
            } else {
                sendMap = this.setPubMap(map, replyInfo);
            }
            System.out.println("回复给用户的消息为--------------------sendMap = " + sendMap);
            String sendXml = XmlUtil.mapToXml(sendMap);
            String encryptXml = SignUtil.encryptMsg(sendXml);
            this.output(response, encryptXml);
        }
    }

    /**
     * 设置公共返回map
     *
     * @param map
     * @param replyInfo
     * @return
     */
    private Map<String, Object> setPubMap(Map<String, String> map, ReplyInfo replyInfo) {
        String toUserName = map.get("FromUserName");
        String fromUserName = map.get("ToUserName");
        int createTime = (int) System.currentTimeMillis();
        Map<String, Object> sendMap = new HashMap<>();
        sendMap.put("ToUserName", toUserName);
        sendMap.put("FromUserName", fromUserName);
        sendMap.put("CreateTime", createTime + "");
        if (replyInfo != null) {
            if (replyInfo.getReplyType().equals("text")) {
                sendMap.put("MsgType", "text");
                sendMap.put("Content", replyInfo.getText());
            } else {
                sendMap.put("MsgType", "image");
                sendMap.put("MediaId", replyInfo.getImageMediaId());
            }
        } else {
            sendMap.put("MsgType", "text");
            sendMap.put("Content", "输入关键词有误，请重新输入。");
        }

        return sendMap;
    }

    private void output(HttpServletResponse response, String returnValue) {
        try {
            ServletOutputStream output = response.getOutputStream();
            output.write(returnValue.getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

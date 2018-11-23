package com.cn.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.controller.weChat.util.HttpClientUtil;
import com.cn.controller.weChat.util.RandomUtils;
import com.cn.controller.weChat.util.ResponseCode;
import com.cn.controller.weChat.util.RtnValue;
import com.cn.model.weChat.AccessTokenInfo;
import com.cn.model.weChat.ReplyInfo;
import com.cn.model.weChat.WeChatInfo;
import com.cn.service.IAccessTokenInfoSV;
import com.cn.service.IKeyWordSV;
import com.cn.service.IReplyInfoSV;
import com.cn.service.IWeChatInfoSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by YancyPeng on 2018/10/23.
 * 订阅回复控制类
 */
@RestController
@RequestMapping("/subscribe")
//@Api(value = "/subscribe", description = "订阅回复控制类")
public class SubscribeController {

//    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeController.class);

    @Value("${ComponentAppId}")
    private String ComponentAppId; // 第三方平台appid

    @Autowired
    private IKeyWordSV iKeyWordSV;

    @Autowired
    private IReplyInfoSV iReplyInfoSV;

    @Autowired
    private IWeChatInfoSV iWeChatInfoSV;

    @Autowired
    private IAccessTokenInfoSV iAccessTokenInfoSV;

    /**
     * 新增、修改订阅回复
     *
     * @param
     * @param jsonObject
     * @return 返回新增订阅回复消息的结果集
     */
    @RequestMapping(method = RequestMethod.POST)
//    @ApiOperation(value = "设置订阅回复", notes = "返回新增订阅回复结果",
//            consumes = "application/json", produces = "application/json", httpMethod = "POST")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> subscribe(@RequestBody JSONObject jsonObject) {
        System.out.println("执行设置订阅回复方法 <subscribe> 的入参为：--------------------" + jsonObject.toJSONString());
        if (StringUtils.isEmpty(jsonObject.getString("messageType"))
                || StringUtils.isEmpty(jsonObject.getString("authorizerAppid"))) {
            return RtnValue.getRtnMap(jsonObject, ResponseCode.FAIL.code(), "缺少必要参数messageType或authorizerAppid");
        }
        String authorizerAppid = jsonObject.getString("authorizerAppid");
        String replyId = "";
        int insert = 0;
        JSONObject params1 = new JSONObject();
        params1.put("ruleName", "订阅");
        params1.put("authorizerAppid", authorizerAppid);
        if (!(replyId = iKeyWordSV.selectByRuleName(params1)).equals("") || replyId == null) { // 检查关键词表中是否存在订阅关键词，若存在，取replyId

        } else { // 如果不存在则新建关键词
            replyId = RandomUtils.generateUuidStr(); // 根据replyId、关键字"订阅"，在关键词表中添加关键字
            JSONObject params = new JSONObject();
            params.put("replyId", replyId);
            params.put("authorizerAppid", authorizerAppid);
            params.put("keyword", "订阅");
            params.put("ruleName", "订阅");
            insert = iKeyWordSV.saveKeyWord(params);
        }
        jsonObject.put("replyId", replyId);
        if (iReplyInfoSV.selectByReplyId(replyId) != null) { // 如果回复表中已经存在，直接更新
            insert += iReplyInfoSV.updateReplyInfo(jsonObject);
            return insert > 1 ? RtnValue.getRtnMap(insert, ResponseCode.SUCCESS.code(), "新增订阅回复成功")
                    : RtnValue.getRtnMap(insert, ResponseCode.FAIL.code(), "新增订阅回复失败");
        } // 如果不存在就新增
        insert += iReplyInfoSV.saveReplyInfo(jsonObject);
        return insert > 1 ? RtnValue.getRtnMap(insert, ResponseCode.SUCCESS.code(), "新增订阅回复成功")
                : RtnValue.getRtnMap(insert, ResponseCode.FAIL.code(), "新增订阅回复失败");
    }

//
//    /**
//     * 修改订阅回复
//     *
//     * @param jsonObject
//     * @return 返回修改的结果集
//     */
//    @RequestMapping(method = RequestMethod.PUT)
//    @ApiOperation(value = "修改订阅回复", notes = "返回修改订阅回复结果",
//            consumes = "application/json", produces = "application/json", httpMethod = "PUT")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
//    public Map<String, Object> modifySubscribe(@RequestBody JSONObject jsonObject) {
//
//        LOGGER.info("执行修改订阅回复方法  <modifySubscribe> 的入参为：--------------------" + jsonObject.toJSONString());
//        if (StringUtils.isEmpty(jsonObject.getString("messageType"))
//                || StringUtils.isEmpty(jsonObject.getString("authorizerAppid"))) {
//            return RtnValue.getRtnMap(jsonObject, ResponseCode.FAIL.code(), "缺少必要参数messageType或authorizerAppid");
//        }
////        String replyType = jsonObject.getString("replyType");
////        String replyId = jsonObject.getString("replyId");
////        int update = 0;
////        if (replyType.equals("text")) { // 如果类型是文本
////            String text = jsonObject.getString("text");
////            JSONObject params = new JSONObject();
////            params.put("replyId", replyId);
////            params.put("replyType", "text");
////            params.put("text", text);
////            update = iReplyInfoSV.updateReplyInfo(params);
////        } else { // 是图片
////            String mediaId = jsonObject.getString("mediaId");
////            String imageUrl = jsonObject.getString("imageUrl");
////            JSONObject params = new JSONObject();
////            params.put("replyId", replyId);
////            params.put("mediaId", mediaId);
////            params.put("imageUrl", imageUrl);
////            update = iReplyInfoSV.updateReplyInfo(params);
////        }
//        int update = iReplyInfoSV.saveReplyInfo(jsonObject);
//        return update > 0 ? RtnValue.getRtnMap(update, ResponseCode.SUCCESS.code(), "修改订阅回复成功")
//                : RtnValue.getRtnMap(update, ResponseCode.FAIL.code(), "修改订阅回复失败");
//
//    }

    /**
     * 查看订阅回复
     *
     * @param authorizerAppid 微信公众号appid
     * @return 返回订阅回复的结果集和replyId
     */
    @RequestMapping(method = RequestMethod.GET)
//    @ApiOperation(value = "查看订阅回复", notes = "返回带有订阅回复内容的结果集",
//            consumes = "application/json", produces = "application/json", httpMethod = "GET")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> getSubscribe(@RequestParam String authorizerAppid) {
        System.out.println("执行查看订阅回复的方法 <getSubscribe> 的入参为：-----------------------" + authorizerAppid);
        JSONObject params = new JSONObject();
        JSONObject result = new JSONObject();
        params.put("authorizerAppid", authorizerAppid);
        params.put("ruleName", "订阅");
        String replyId = iKeyWordSV.selectByRuleName(params);// 根据authorizerAppid 和 关键词 "订阅" 在关键词表中查找replyId
        ReplyInfo replyInfo = iReplyInfoSV.selectByReplyId(replyId);// 根据replyId去回复表中查回复
        if (replyInfo != null) {
            String replyType = replyInfo.getReplyType();
            result.put("replyType", replyType);
            if (replyType.equals("text")) {
                result.put("text", replyInfo.getText());
                return RtnValue.getRtnMap(result, ResponseCode.SUCCESS.code(), "查询订阅回复成功");
            } else { // 为图片
                result.put("imageUrl", replyInfo.getImageUrl());
                return RtnValue.getRtnMap(result, ResponseCode.SUCCESS.code(), "查询订阅回复成功");
            }
        }
        return RtnValue.getRtnMap("", ResponseCode.FAIL.code(), "暂无订阅回复");
    }

    /**
     * 删除订阅回复
     *
     * @param jsonObject 回复id和公众号appdi
     * @return 返回删除的结果
     */
    @RequestMapping(method = RequestMethod.DELETE)
//    @ApiOperation(value = "删除订阅回复", notes = "删除订阅回复",
//            consumes = "application/json", produces = "application/json", httpMethod = "DELETE")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> deleteSubscribe(@RequestBody JSONObject jsonObject) {
        System.out.println("执行删除订阅回复方法 <deleteSubscribe> 的入参为：------------------" + jsonObject.toJSONString());
        if (StringUtils.isEmpty(jsonObject.getString("replyId"))
                || StringUtils.isEmpty(jsonObject.getString("authorizerAppid"))) {
            return RtnValue.getRtnMap(jsonObject, ResponseCode.FAIL.code(), "authorizerAppid");
        }
        String replyId = jsonObject.getString("replyId");

        int delete = iReplyInfoSV.deleteByReplyId(replyId);

        return delete == 1 ? RtnValue.getRtnMap(delete, ResponseCode.SUCCESS.code(), "删除订阅回复成功")
                : RtnValue.getRtnMap(delete, ResponseCode.FAIL.code(), "删除订阅回复失败");
    }

//    /**
//     * 上传图片接口
//     * @param file
//     * @param authorizerAppid
//     * @return
//     */
//    @RequestMapping(value = "/image/{authorizerAppid}", method = RequestMethod.POST)
////    @ApiOperation(value = "上传订阅图片到ONEST和微信服务器", notes = "上传订阅图片到ONEST和微信服务器",
////            consumes = "application/json", produces = "application/json", httpMethod = "POST")
////    @ApiImplicitParams({
////            @ApiImplicitParam(name = "file", value = "入参json", required = true, paramType = "query", dataType = "MultipartFile"),
////            @ApiImplicitParam(name = "type", value = "入参json", required = true, paramType = "query", dataType = "String")
////    })
////    @ApiResponses({
////            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
////            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
////    })
//    public Map<String, Object> uploadPic(@RequestParam MultipartFile file, @PathVariable String authorizerAppid) {
//        String tenantId = "hykf123";
//        String authorizationAccessToken = this.checkUpdateTime(authorizerAppid, tenantId);
//        if (authorizationAccessToken.equals("")) {
//            return RtnValue.getRtnMap("", ResponseCode.FAIL.code(), "该微信公众号需要重新授权");
//        }
//        System.out.println("上传订阅图片到ONEST和微信服务器的方法 <uploadPic>的参数为：authorizationAccessToken = " + authorizationAccessToken + "tenantId = " + tenantId);
//        JSONObject result = UploadPicUtil.addMaterialEver(file, authorizationAccessToken, "image"); // 上传到微信服务器
//        String mediaId = result.getString("media_id"); // 上传到微信服务器返回的mediaId
//        try {
//            String imageUrl = UploadPicUtil.uploadPicToLocal(file, authorizerAppid); // 上传图片到本地
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("imageMediaId", mediaId);
//            jsonObject.put("imageUrl", imageUrl);
//            return RtnValue.getRtnMap(jsonObject, ResponseCode.SUCCESS.code(), "上传订阅图片到ONEST和微信服务器成功");
//        } catch (GeneralException e) {
//            e.printStackTrace();
//        }
//        return RtnValue.getRtnMap("", ResponseCode.FAIL.code(), "上传订阅图片到ONEST和微信服务器失败");
//    }

    /**
     * 校验接口调用凭据和刷新令牌是否过期
     *
     * @param authorizerAppid
     * @return
     */
    private String checkUpdateTime(String authorizerAppid) {
        WeChatInfo weChatInfo = iWeChatInfoSV.selectWeChatInfoByAuthAppid(authorizerAppid);
        Long accessUpdateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(weChatInfo.getAccessUpdateTime(), new ParsePosition(0)).getTime();
        Long refreshUpdateTIme = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(weChatInfo.getRefreshUpdateTime(), new ParsePosition(0)).getTime();
        Long currentTime = System.currentTimeMillis();
        if ((currentTime - accessUpdateTime) / 1000 >= 60 * 60 * 2) { // 先判断接口调用凭据是否过期
            if ((currentTime - refreshUpdateTIme) / 1000 >= 60 * 60 * 24 * 30) { // 如果刷新令牌的更新时间超过30天，需要进行重新授权
                return "";
            } else {
                AccessTokenInfo accessTokenInfo = iAccessTokenInfoSV.selectActInfo();
                String componentAccessToken = accessTokenInfo.getComponentAccessToken();
                String authorizerRefreshToken = weChatInfo.getAuthorizerRefreshToken();
                JSONObject params = new JSONObject();
                params.put("component_appid", ComponentAppId);
                params.put("authorizer_appid", authorizerAppid);
                params.put("authorizer_refresh_token", authorizerRefreshToken);
                String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=" + componentAccessToken, params.toJSONString());
                System.out.println("在订阅controller中通过刷新令牌获取新的接口调用凭据的结果为---------------------+" + result);
                JSONObject resultObject = JSONObject.parseObject(result);
                String authorizerAccessToken = resultObject.getString("authorizer_access_token");

                // 拼接参数更新本地数据库
                JSONObject params1 = new JSONObject();
                params1.put("authorizerAppid", authorizerAppid);
                params1.put("authorizerAccessToken", authorizerAccessToken);
                iWeChatInfoSV.updateByAuthAppid(params1);
                return authorizerAccessToken;
            }

        } else { // 接口调用凭据在有效期内
            return weChatInfo.getAuthorizerAccessToken();
        }
    }
}

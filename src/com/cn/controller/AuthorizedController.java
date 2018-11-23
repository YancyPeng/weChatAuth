package com.cn.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.controller.weChat.util.*;
import com.cn.model.weChat.AccessTokenInfo;
import com.cn.model.weChat.WeChatInfo;
import com.cn.service.IAccessTokenInfoSV;
import com.cn.service.IWeChatInfoSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YancyPeng on 2018/10/17.
 * 授权服务控制类
 */
@RestController
@RequestMapping("/authorized")
//@Api(value = "/authorized", description = "授权服务控制类")
public class AuthorizedController {

//    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizedController.class);

    private static Map<String, String> authorizedMap = new HashMap<>();

    private String ComponentAppId = "wx77c6c812c7b5a32d"; // 第三方平台appid

    private String ComponentAppSecret = "fe52b59c143166926aa4e51b3d070f06"; // 第三方平台appsecret

    @Autowired
    private IWeChatInfoSV iWeChatInfoSV;

    @Autowired
    private IAccessTokenInfoSV iAccessTokenInfoSV;

//    @Resource
//    private IKeyWordSV iKeyWordSV;
//
//    @Resource
//    private IReplyInfoSV iReplyInfoSV;

    /**
     * @param postdata 微信发送过来的加密的xml格式数据，通过在创建第三方平台是填写的授权事件URL关联
     *                 除了接受授权事件（成功授权、取消授权以及授权更新）外，在接受ticket及授权后回调URI也会用到该方法
     * @return 根据微信开放平台规定，接收到授权事件后只需要直接返回success
     */
    @RequestMapping(value = "/event", method = RequestMethod.POST)
//    @ApiOperation(value = "接受授权事件通知和ticket", notes = "返回sucess",
//            consumes = "application/json", produces = "application/json", httpMethod = "POST")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public String receiveAuthorizedEvent(@RequestBody(required = false) String postdata, HttpServletRequest request) {
        System.out.println("调用接受授权事件通知的方法 <getAuthorizedEvent> 的入参为：-----------------------" + postdata);
////        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//                .getRequest();
        String decryptXml = SignUtil.decryptMsg(request, postdata); // 获得解密后的xml文件
        String infoType; // 事件类型
        try {
            authorizedMap = XmlUtil.xmlToMap(decryptXml); // 获得xml文件对应的map
            System.out.println("解密后的xml文件为：------" + authorizedMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((infoType = authorizedMap.get("InfoType")).equals("component_verify_ticket")) { //如果是接受ticket
            System.out.println("接受到微信发送的ticket，ticket = " + authorizedMap.get("ComponentVerifyTicket"));
            this.setPublicAuthorizedCode(authorizedMap.get("ComponentVerifyTicket")); // 根据ticket去刷新公共授权码
        } else if (infoType.equals("unauthorized")) { // 接受的是取消授权事件，将微信授权状态设为3
            String authorizerAppid = authorizedMap.get("AuthorizerAppid");
            JSONObject params = new JSONObject();
            params.put("authorizerAppid", authorizerAppid);
            params.put("authorizerState", "3");
            int update = iWeChatInfoSV.updateByAuthAppid(params);
            System.out.println("微信端取消授权 【0：失败，1：成功】 update = " + update);

        } // 如果是授权成功和更新授权事件，则什么都不做，在authorizedSuccess中进行处理
        return "success";
    }

    /**
     * 接受授权成功事件
     */
    @RequestMapping(value = "/success/info", method = RequestMethod.GET)
    public void authorizedSuccess(HttpServletRequest request) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String tenantId = "hykf123";
        String authorizationCode = request.getParameter("auth_code");
        System.out.println("授权成功获得的授权码为：-----------------------authorizationCode = " + authorizationCode);
        String componentAccessToken = iAccessTokenInfoSV.selectActInfo().getComponentAccessToken(); //通过tenantId在数据库中查出

        String result = this.getAuthorizedAppid(authorizationCode, componentAccessToken); //获得authorizer_appid和接口调用凭据
        JSONObject resultObject = JSONObject.parseObject(result).getJSONObject("authorization_info");
        System.out.println("授权成功后获取authorizer_appid和接口调用凭据的结果为：-------------" + resultObject.toJSONString());


        JSONObject weixinInfo = new JSONObject();
        weixinInfo.put("authorizationCode", authorizationCode);
        weixinInfo.put("authorizerAppid", resultObject.getString("authorizer_appid"));
        weixinInfo.put("authorizerAccessToken", resultObject.getString("authorizer_access_token"));
        weixinInfo.put("authorizerRefreshToken", resultObject.getString("authorizer_refresh_token"));
        weixinInfo.put("tenantId", tenantId);
        this.getWeiXinInfo(weixinInfo, componentAccessToken);// 获取公众号的基本信息并入库

        // 首先尝试更新，若本地不存在，再添加
        int update = iWeChatInfoSV.updateByAuthAppid(weixinInfo);
        if (0 == update) {
            int insert = iWeChatInfoSV.saveWeChatInfo(weixinInfo);
            if (insert > 0) {
                System.out.println("执行添加微信公众号信息成功！" + insert);
            } else {
                System.out.println("执行添加微信公众号信息失败！！！" + insert);
            }
        } else {
            System.out.println("本地已存在该微信公众号信息，已更新!");
        }
        try {
            response.sendRedirect("http://hykf.natapp1.cc");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增授权，由于预授权码pre_auth_code 10分钟更新一次，所以每次请求需要判断当前时间和数据库中的updateTime是否超过600秒
     * 当请求新增授权并且本地数据库中的预授权码过期了才去请求微信接口 获取新的预授权码 保存进本地数据库
     */
    @RequestMapping(method = RequestMethod.GET)
//    @ApiOperation(value = "新增授权", notes = "重定向到微信授权二维码页面",
//            consumes = "application/json", produces = "application/json", httpMethod = "GET")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public void authorize() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String redirectUrl = "http://hykf.natapp1.cc/authorized/success/info";
        AccessTokenInfo accessTokenInfo = iAccessTokenInfoSV.selectActInfo(); // 获取公共授权码对象

        Long codeUpdateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(accessTokenInfo.getCodeUpdateTime(), new ParsePosition(0)).getTime();
        Long currentTime = System.currentTimeMillis();
        String preAuthCode = "";
        if ((currentTime - codeUpdateTime) / 1000 >= 600) { // 如果大于10分钟，就去调用微信接口更新
            String componentAccessToken = accessTokenInfo.getComponentAccessToken();
            // 接下来根据component_access_token来获取预授权码 pre_auth_code
            JSONObject params = new JSONObject();
            params.put("component_appid", ComponentAppId);
            String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=" + componentAccessToken, params.toJSONString());
            preAuthCode = JSONObject.parseObject(result).getString("pre_auth_code");
            System.out.println("获取的pre_auth_code为：------------------------" + preAuthCode);
            if (!(StringUtils.isEmpty(preAuthCode))) { // 如果获取到预授权码才更新
                JSONObject params1 = new JSONObject();
                params1.put("preAuthCode", preAuthCode);
                params1.put("codeUpdateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));
                int update = iAccessTokenInfoSV.updateAccessToken(params1); // 更新本地数据库
                System.out.println("更新预授权码 【0：失败，1：成功】 update = " + update);
            } else {
                System.out.println("component_access_token 的值过期了！！！！！！！");
            }
        } else { // 如果小于10分钟 直接取
            preAuthCode = accessTokenInfo.getPreAuthCode();
        }
        try {

//            request.getRequestDispatcher("https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=" + ComponentAppId
//                    + "&pre_auth_code=" + preAuthCode + "&redirect_uri=" + redirectUrl).forward(request,response);
            response.sendRedirect("https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=" + ComponentAppId
                    + "&pre_auth_code=" + preAuthCode + "&redirect_uri=" + redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三方平台主动取消授权
     *
     * @param authorizerAppid 公众号id
     * @return 授权结果
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
//    @ApiOperation(value = "第三方平台主动取消授权", notes = "返回取消授权结果",
//            consumes = "application/json", produces = "application/json", httpMethod = "POST")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> cancelAuthorized(@RequestParam String authorizerAppid) {
        // 通过公众号id去修改数据库中的公众号状态
        int delete = iWeChatInfoSV.deleteByAuthAppid(authorizerAppid);

        return delete > 0 ?
                RtnValue.getRtnMap(delete, ResponseCode.SUCCESS.code(), "取消授权成功") :
                RtnValue.getRtnMap(delete, ResponseCode.FAIL.code(), "取消授权失败");

    }

    /**
     * 获取当前租户下的所有微信
     *
     * @param tenantId 租户id
     * @return 微信列表及列表中第一个微信的关键词和自定义菜单信息
     */
    @RequestMapping(value = "/weixin/{tenantId}", method = RequestMethod.GET)
//    @ApiOperation(value = "获得当前租户下的所有微信", notes = "微信列表",
//            consumes = "application/json", produces = "application/json", httpMethod = "POST")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> getWeiXinList(@PathVariable String tenantId) {
        List<WeChatInfo> weChatList = this.checkUpdateTime(tenantId); // 查找数据中的所有公众号信息，组成一个list
        System.out.println("获得的微信列表为：---------------------------------" + weChatList);
        if (weChatList.isEmpty()) {
            return RtnValue.getRtnMap("", ResponseCode.FAIL.code(), "微信列表为空");
        } else {
            JSONArray wechatArray = JSONArray.parseArray(JSON.toJSONString(weChatList));
            JSONObject result = new JSONObject();
            result.put("wechatArray", wechatArray);

//            // 默认获取第一个公众号的所有信息
//            WeChatInfo weChatInfo = weChatList.get(0);
//            String authorizerAppid = weChatInfo.getAuthorizerAppid();
//
//            JSONArray replyArray = new JSONArray();
            // 先用authorizerAppid 查询回复表，得到回复的List，从list中获取所有的replyId
//            List<ReplyInfo> replyInfoList = iReplyInfoSV.selectReplyInfoList(authorizerAppid);
//            for (ReplyInfo replyInfo : replyInfoList) {
//                String replyId = replyInfo.getReplyId();
//                List<KeyWord> keyWordList = iKeyWordSV.selectKeyWordList(replyId);
//                List<String> keywords = new ArrayList<>();
//                for (KeyWord keyWord : keyWordList) {
//                    keywords.add(keyWord.getKeyWord());
//                }
//
//                JSONObject reply = new JSONObject();
//                reply.put("ruleName", keyWordList.get(0).getRuleName()); // 由于同一个replyId的规则都相同，直接取第一个
//                reply.put("keyword", keywords);
//
//                String replyType = replyInfo.getReplyType();
//                reply.put("replyType", replyType);
//                if (replyType.equals("text")) {
//                    reply.put("text", replyInfo.getText());
//                } else {
//                    reply.put("imageUrl", replyInfo.getImageUrl());
//                }
//                replyArray.add(reply);
//            }
//            result.put("replyArray", replyArray);
//
//            JSONObject params = new JSONObject();
//            JSONObject subscribe = new JSONObject();
//            params.put("authorizerAppid", authorizerAppid);
//            params.put("keyword", "订阅");
//            String replyId = iKeyWordSV.selectByKeyWord(params);// 根据authorizerAppid 和 关键词 "订阅" 在关键词表中查找replyId
//            ReplyInfo replyInfo = iReplyInfoSV.selectByReplyId(replyId);// 根据replyId去回复表中查回复
//            if (replyInfo != null) {
//                String replyType = replyInfo.getReplyType();
//                subscribe.put("replyType", replyType);
//                if (replyType.equals("text")) {
//                    subscribe.put("text", replyInfo.getText());
//                } else { // 为图片
//                    subscribe.put("imageUrl", replyInfo.getImageUrl());
//                }
//            }
//            result.put("subscribe", subscribe);

            return RtnValue.getRtnMap(result, ResponseCode.SUCCESS.code(), "查询所有微信信息成功！");
        }

    }

    /**
     * 刷新公共授权码，由于component_access_token需要2个小时刷新一次，所以需要判断本地表中存在的第三方接口调用凭据updateTime和当前时间的差值
     * 如果超过1小时50分就调用微信接口更新，否则不做任何操作
     *
     * @param componentVerifyTicket 根据最近可用的component_verify_ticket来获得componentAccessToken和preAuthCode
     */
    private void setPublicAuthorizedCode(String componentVerifyTicket) {
        // 根据tenantId查出 当前公共授权码表中的 ComponentVerifyTicket
        System.out.println("执行controller层 刷新公共授权码的方法  <setPublicAuthorizedCode> 的入参为： componentVerifyTicket = " + componentVerifyTicket);
        AccessTokenInfo accessTokenInfo = iAccessTokenInfoSV.selectActInfo();
        if (null != accessTokenInfo) { // 如果不是首次接受ticket
            Long tokenUpdateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(accessTokenInfo.getTokenUpdateTime(),
                    new ParsePosition(0)).getTime();
            Long currentTime = System.currentTimeMillis();
            if ((currentTime - tokenUpdateTime) / 1000 >= 6600) { // 如果大于等于1小时50分
                // 获取 component_access_token
                JSONObject params = new JSONObject();
                params.put("component_verify_ticket", componentVerifyTicket);
                params.put("component_appsecret", ComponentAppSecret);
                params.put("component_appid", ComponentAppId);
                String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_component_token", params.toJSONString());
                System.out.println("获取component_access_token的结果为：---------------------" + result);
                String componentAccessToken = JSONObject.parseObject(result).getString("component_access_token");

                // 拼装参数，添加到本地数据库
                JSONObject params1 = new JSONObject();
                params1.put("componentVerifyTicket", componentVerifyTicket);
                params1.put("componentAccessToken", componentAccessToken);
                params1.put("tokenUpdateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));
                int update = iAccessTokenInfoSV.updateAccessToken(params1);
                System.out.println("更新第三方接口调用凭据component_access_token 【0：失败，1：成功】 update = " + update);
            } // 如果小于则不需要更新

        } else { //首次接收ticket，需要走一遍整个流程，获取component_access_token和pre_auth_code，添加进本地数据库

            // 首先获取component_access_token
            JSONObject params = new JSONObject();
            params.put("component_verify_ticket", componentVerifyTicket);
            params.put("component_appsecret", ComponentAppSecret);
            params.put("component_appid", ComponentAppId);
            String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_component_token", params.toJSONString());
            System.out.println("首次获取component_access_token的结果为：---------------------" + result);
            String componentAccessToken = JSONObject.parseObject(result).getString("component_access_token");

            // 获取pre_auth_code
            JSONObject params1 = new JSONObject();
            params1.put("component_appid", ComponentAppId);
            result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=" + componentAccessToken, params1.toJSONString());
            System.out.println("首次获取的pre_auth_code为：------------------------" + result);
            String preAuthCode = JSONObject.parseObject(result).getString("pre_auth_code");

            // 封装参数，添加进本地数据库
            JSONObject params2 = new JSONObject();
            params2.put("componentVerifyTicket", componentVerifyTicket);
            params2.put("componentAccessToken", componentAccessToken);
            params2.put("preAuthCode", preAuthCode);
            int insert = iAccessTokenInfoSV.insertSelective(params2);
            System.out.println("首次添加公共授权码进本地数据库  【0：失败，1：成功】 insert = " + insert);
        }

    }

    /**
     * 获取公众号authorizer_appid和公众号接口调用凭据
     *
     * @param authorizationCode 授权码
     * @return 公众号id和公众号接口调用凭据
     */
    private String getAuthorizedAppid(String authorizationCode, String componentAccessToken) {
        JSONObject params = new JSONObject();
        params.put("authorization_code", authorizationCode);
        params.put("component_appid", ComponentAppId);
        String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=" + componentAccessToken, params.toJSONString());
        System.out.println("根据授权码获得公众号authorizer_appid和公众号接口调用凭据等的结果为：-----------" + result);
        return result;

    }

    /**
     * 获取微信基本消息并入库
     *
     * @param weixinInfo           微信基本信息Json对象
     * @param componentAccessToken 第三方平台调用凭据
     */
    private void getWeiXinInfo(JSONObject weixinInfo, String componentAccessToken) {
        JSONObject params = new JSONObject();
        params.put("component_appid", ComponentAppId);
        params.put("authorizer_appid", weixinInfo.getString("authorizerAppid"));

        String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=" + componentAccessToken, params.toJSONString());
        System.out.println("获得的微信基本信息如下：----------------------------" + result);
        JSONObject authorizerInfo = JSONObject.parseObject(result).getJSONObject("authorizer_info");

        weixinInfo.put("nickName", authorizerInfo.getString("nick_name"));// 获取授权方昵称
        weixinInfo.put("headImg", authorizerInfo.getString("head_img"));// 授权方头像
        weixinInfo.put("serviceTypeInfo", authorizerInfo.getJSONObject("service_type_info").getString("id")); //授权方公众号类型
        weixinInfo.put("verifyTypeInfo", authorizerInfo.getJSONObject("verify_type_info").getString("id"));// 授权方认证类型
        weixinInfo.put("userName", authorizerInfo.getString("user_name"));// 授权方公众号原始id
        weixinInfo.put("principalName", authorizerInfo.getString("principal_name")); // 公众号的主体名称
        weixinInfo.put("qrcodeUrl", authorizerInfo.getString("qrcode_url"));// 二维码图片的URL
        weixinInfo.put("authorizerState", "1");
    }

    /**
     * @param tenantId
     * @return
     */
    private List<WeChatInfo> checkUpdateTime(String tenantId) {
        List<WeChatInfo> weChatInfoList = iWeChatInfoSV.selectWeChatInfoList(tenantId);
        if (weChatInfoList.size() != 0) {
            Long currentTime = System.currentTimeMillis();
            for (WeChatInfo weChatInfo : weChatInfoList) {
                Long refreshUpdateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(weChatInfo.getRefreshUpdateTime(),
                        new ParsePosition(0)).getTime();
                if ((currentTime - refreshUpdateTime) / 1000 >= 60 * 60 * 24 * 30) { // 是否大于刷新令牌的失效时间30天
                    weChatInfo.setAuthorizerState("2");

                    JSONObject params = new JSONObject();
                    params.put("authorizerAppid", weChatInfo.getAuthorizerAppid());
                    params.put("authorizerState", "2"); // 设置公众号的状态为2（授权过期）
                    iWeChatInfoSV.updateByAuthAppid(params);
                }
            }
        }
        return weChatInfoList;
    }


//    /**
//     * 更新授权码
//     *
//     * @param authorizerAppid 当前公众号appid
//     * @return 公众号更新后的调用凭据
//     */
//    public Map<String, Object> refreshAuthorizedAccessToken(String authorizerAppid) {
//        Map<String, Object> result = new HashMap<>();
//        //1。 根据tenantId查出componentAccessToken
//        //2。 根据tenantId查出微信List
//        //3。 遍历list取出各authorizerAppid和authorizerRefreshToken
//        //4。 调用刷新接口，获取新的authorizerRefreshToken和authorizerAccessToken，更新本地数据库
//        //5.  组装返回的数据
//
//        return result;
//    }

}

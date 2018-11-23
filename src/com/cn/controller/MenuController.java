package com.cn.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.controller.weChat.util.HttpClientUtil;
import com.cn.controller.weChat.util.RandomUtils;
import com.cn.controller.weChat.util.ResponseCode;
import com.cn.controller.weChat.util.RtnValue;
import com.cn.model.weChat.AccessTokenInfo;
import com.cn.model.weChat.ReplyInfo;
import com.cn.model.weChat.WeChatInfo;
import com.cn.service.IAccessTokenInfoSV;
import com.cn.service.IReplyInfoSV;
import com.cn.service.IWeChatInfoSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by YancyPeng on 2018/10/22.
 * 自定义菜单控制类
 */
@RestController
@RequestMapping("/menu")
//@Api(value = "/menu", description = "自定义菜单控制类")
public class MenuController {

//    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

//    @Autowired
//    private FileStorage storage;

    @Autowired
    private IWeChatInfoSV iWeChatInfoSV;

    @Autowired
    private IAccessTokenInfoSV iAccessTokenInfoSV;

    @Autowired
    private IReplyInfoSV iReplyInfoSV;

    private String ComponentAppId = "wx77c6c812c7b5a32d"; // 第三方平台appid

    /**
     * 删除菜单
     *
     * @param authorizerAppid 微信公众号appid
     * @return 返回删除自定义菜单的结果集
     */
    @RequestMapping(value = "/{authorizerAppid}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteMenu(@PathVariable String authorizerAppid) {
        System.out.println("调用删除菜单接口 <deleteMenu> 的入参为：authorizerAppid = " + authorizerAppid);
        String authorizationAccessToken = this.checkUpdateTime(authorizerAppid);
        String result = HttpClientUtil.httpGet("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + authorizationAccessToken);
        JSONObject params = JSONObject.parseObject(result);
        String errCode = params.getString("errCode");
        return errCode.equals("0") ? RtnValue.getRtnMap("", ResponseCode.SUCCESS.code(), "删除自定义菜单成功")
                : RtnValue.getRtnMap("", ResponseCode.FAIL.code(), "删除自定义菜单失败");
    }

    /**
     * 查询菜单
     *
     * @param authorizerAppid 微信公众号appid
     * @return 返回自定义菜单结果集
     */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> getMenu(@RequestParam String authorizerAppid) {
//                {
//                    "menu": {
        //                    "button": [
        //                    {
        //                            "type": "click",
        //                            "name": "今日歌曲",
        //                            "key": "V1001_TODAY_MUSIC",
        //                            "sub_button": [ ]
        //                    },
        //                    {
        //                        "type": "click",
        //                            "name": "歌手简介",
        //                            "key": "V1001_TODAY_SINGER",
        //                            "sub_button": [ ]
        //                    },
//                         {
//                            "name": "菜单",
//                            "sub_button": [
        //                        {
        //                            "type": "view",
        //                                "name": "搜索",
        //                                "url": "http://www.soso.com/",
        //                                "sub_button": [ ]
        //                        },
        //                        {
        //                            "type": "view",
        //                                "name": "视频",
        //                                "url": "http://v.qq.com/",
        //                                "sub_button": [ ]
        //                        },
        //                        {
        //                            "type": "click",
        //                                "name": "赞一下我们",
        //                                "key": "V1001_GOOD",
        //                                "sub_button": [ ]
        //                        }
//                        ]
//                    }
//                ]
//            }
//        }
        System.out.println("调用微信接口查询自定菜单的入参为：authorizerAppid = " + authorizerAppid);
        String authorizationAccessToken = this.checkUpdateTime(authorizerAppid);
        String result = HttpClientUtil.httpGet("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + authorizationAccessToken);
        System.out.println("调用微信接口查询自定菜单的出参为：--------------------" + result);
        JSONObject params = JSONObject.parseObject(result);
        JSONArray array = params.getJSONObject("menu").getJSONArray("button");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                if (jsonObject.getString("sub_button").isEmpty()) { //如果子菜单为空
                    this.setReplyParam(jsonObject);
                } else { // 如果存在子菜单，需要继续遍历子菜单
                    JSONArray subButton = jsonObject.getJSONArray("sub_button"); // 得到子菜单json数组
                    for (int j = 0; j < subButton.size(); j++) {
                        JSONObject jsonObject1 = subButton.getJSONObject(j);
                        this.setReplyParam(jsonObject1);
                    }
                }
            }
        }
        return RtnValue.getRtnMap(params, ResponseCode.SUCCESS.code(), "查询成功");
    }


    /**
     * 新增菜单和修改菜单
     *
     * @param jsonObject
     * @return 返回新增自定义菜单结果
     * 由于微信没有提供修改接口，所以修改和新增使用同一个封装的接口
     */
    @RequestMapping(method = RequestMethod.POST)
//    @ApiOperation(value = "调用微信接口新增自定义菜单，包含新增和修改自定义菜单", notes = "返回一个结果集",
//            consumes = "application/json", produces = "application/json", httpMethod = "POST")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> addMenu(@RequestBody JSONObject jsonObject) {
        if (StringUtils.isEmpty(jsonObject.getString("authorizerAppid"))) {
            return RtnValue.getRtnMap(jsonObject, ResponseCode.FAIL.code(), "缺少必要参数authorizerAppid");
        }
        System.out.println("调用新增自定义菜单的方法 <addMenu> 的入参为：---------------------" + jsonObject.toJSONString());
        jsonObject = this.setClickEvent(jsonObject);
        System.out.println("调用设置点击事件的方法 <setClickEvent> 后的结果为：---------------------" + jsonObject.toJSONString());
        String authorizerAppid = jsonObject.getString("authorizerAppid");
        jsonObject.remove("authorizerAppid");
        String authorizationAccessToken = this.checkUpdateTime(authorizerAppid);
        String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + authorizationAccessToken, jsonObject.toJSONString());
        System.out.println("调用新增自定义菜单方法 <addMenu> 的出参为： ---------------------" + result);
        JSONObject params = JSONObject.parseObject(result);
        String errCode = params.getString("errcode");
        return errCode.equals("0") ? RtnValue.getRtnMap("", ResponseCode.SUCCESS.code(), "新增自定义菜单成功")
                : RtnValue.getRtnMap("", ResponseCode.FAIL.code(), "新增自定义菜单失败");
    }

    /**
     * @param jsonObject
     * @return 返回带有一个事件key的结果集
     * 由于调用微信新增自定义菜单接口时，所有的点击事件都只传一个key
     * 本方法是将回复表中的replyId作为这个key，拼接好后调用微信接口
     */
    public JSONObject setClickEvent(JSONObject jsonObject) {

//        {
//            "authorizerAppid" : "",
//            "button":[
//            {
//                "type":"click",
//                "name":"今日歌曲",
//                "replyType":”text”
//                "text" : "你好"
//            },
//            {
//                "name":"菜单",
//                    "sub_button":[
//                {
//                    "type":"view",
//                        "name":"搜索",
//                        "url":"http://www.soso.com/"
//                },
//                {
//                    "type":"click",
//                    "name":"赞一下我们",
//                    "replyType":”image”,
//		              "imageUrl" : ""
//                    "mediaId" : ""
//                }]
//            }]
//        }
        JSONObject params = JSONObject.parseObject(jsonObject.toJSONString());
        System.out.println("开始前jsonObject的值为：-------------------------" + params.toJSONString());
        String authorizerAppid = params.getString("authorizerAppid");
        JSONArray button = new JSONArray();
        JSONObject arrayObject = new JSONObject();
        JSONObject subObject = new JSONObject();
        if (!(button = params.getJSONArray("button")).isEmpty()) {
            for (int i = 0; i < button.size(); i++) { // 遍历jsonArray
                arrayObject = button.getJSONObject(i);
                if (arrayObject.getJSONArray("sub_button") != null) { // 如果存在二级菜单，继续遍历二级菜单
                    JSONArray subButton = arrayObject.getJSONArray("sub_button");
                    for (int j = 0; j < subButton.size(); j++) {
                        subObject = subButton.getJSONObject(j);
                        setSendParam(subObject, authorizerAppid);
                    }
                } else {// 如果不存在二级菜单，查看点击类型
                    setSendParam(arrayObject, authorizerAppid);
                }
            }
        }
        System.out.println("结束后 jsonObject的值为：-------------------------" + params.toJSONString());
        return params;
    }

    /**
     * 设置调用微信接口的参数
     *
     * @param jsonObject
     * @param authorizerAppid
     */
    public void setSendParam(JSONObject jsonObject, String authorizerAppid) {
        if (jsonObject.getString("type").equals("click")) {// 如果为点击click事件，查看消息类型
            String replyId = RandomUtils.generateUuidStr();
            String replyType = jsonObject.getString("replyType");
            if (replyType.equals("text")) { // 如果为text类型
                String text = jsonObject.getString("text");
                JSONObject params = new JSONObject();
                params.put("authorizerAppid", authorizerAppid);
                params.put("replyId", replyId);
                params.put("text", text);
                params.put("replyType", replyType);
                iReplyInfoSV.saveReplyInfo(params);

                // 删除请求体的相关内容，拼接成调用微信接口需要的参数
                jsonObject.remove("replyType");
                jsonObject.remove("text");
                jsonObject.put("key", replyId);

            } else {  // 是图片
                String mediaId = jsonObject.getString("mediaId");
                String imageUrl = jsonObject.getString("imageUrl");
                JSONObject params = new JSONObject();
                params.put("authorizerAppid", authorizerAppid);
                params.put("replyId", replyId);
                params.put("imageUrl", imageUrl);
                params.put("mediaId", mediaId);
                params.put("replyType", replyType);
                iReplyInfoSV.saveReplyInfo(params);

                jsonObject.remove("replyType");
                jsonObject.remove("imageUrl");
                jsonObject.remove("mediaId");
                jsonObject.put("key", replyId);
            }
        } // 如果是view事件，不做任何处理
    }

    /**
     * 对微信接口返回的数据进行拼接，返回给前端展示
     *
     * @param jsonObject
     */
    public void setReplyParam(JSONObject jsonObject) {
        if (jsonObject.getString("type").equals("click")) {
            String key = jsonObject.getString("key");
            ReplyInfo replyInfo = iReplyInfoSV.selectByReplyId(key);
            String replyType = replyInfo.getReplyType();
            if (replyType.equals("image")) {
                jsonObject.remove("key");
                jsonObject.put("replyType", replyType);
                jsonObject.put("imageUrl", replyInfo.getImageUrl());
            } else {
                jsonObject.remove("key");
                jsonObject.put("replyType", replyType);
                jsonObject.put("text", replyInfo.getText());
            }
        }
    }

    /**
     * 校验接口调用凭据和刷新令牌
     * 每次调用微信接口前都需要进行验证
     *
     * @param authorizerAppid
     * @return
     */
    public String checkUpdateTime(String authorizerAppid) {
        WeChatInfo weChatInfo = iWeChatInfoSV.selectWeChatInfoByAuthAppid(authorizerAppid);
        Long accessUpdateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(weChatInfo.getAccessUpdateTime(),
                new ParsePosition(0)).getTime();
        Long refreshUpdateTIme = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(weChatInfo.getRefreshUpdateTime(),
                new ParsePosition(0)).getTime();
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
                String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token" + componentAccessToken, params.toJSONString());
                System.out.println("在自定义菜单controller中通过刷新令牌获取新的接口调用凭据的结果为---------------------+" + result);
                JSONObject resultObject = JSONObject.parseObject(result);
                String authorizerAccessToken = "";
                if (!(authorizerAccessToken = resultObject.getString("authorizer_access_token")).isEmpty()) {

                    // 拼接参数更新本地数据库
                    JSONObject params1 = new JSONObject();
                    params1.put("authorizerAppid", authorizerAppid);
                    params1.put("authorizerAccessToken", authorizerAccessToken);
                    iWeChatInfoSV.updateByAuthAppid(params1);
                }

                return authorizerAccessToken;
            }

        } else { // 接口调用凭据在有效期内
            return weChatInfo.getAuthorizerAccessToken();
        }
    }


}


package com.cn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.controller.weChat.util.RandomUtils;
import com.cn.controller.weChat.util.ResponseCode;
import com.cn.controller.weChat.util.RtnValue;
import com.cn.model.weChat.KeyWord;
import com.cn.model.weChat.ReplyInfo;
import com.cn.service.IKeyWordSV;
import com.cn.service.IReplyInfoSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by YancyPeng on 2018/10/23.
 * 关键词控制类
 */
@RestController
@RequestMapping("/keyword")
//@Api(value = "/keyword", description = "关键词控制类")
public class keywordController {

//    private static final Logger LOGGER = LoggerFactory.getLogger(keywordController.class);

    @Value("${ComponentAppId}")
    private String ComponentAppId; // 第三方平台appid

    @Autowired
    private IKeyWordSV iKeyWordSV;

    @Autowired
    private IReplyInfoSV iReplyInfoSV;

    /**
     * 新增关键词回复
     *
     * @param jsonObject
     * @return 返回新增关键词回复的结果集
     */
    @RequestMapping(method = RequestMethod.POST)
//    @ApiOperation(value = "新增关键词回复", notes = "返回新增关键词回复的结果集",
//            consumes = "application/json", produces = "application/json", httpMethod = "POST")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> saveKeywords(@RequestBody JSONObject jsonObject) {
        System.out.println("执行新增关键词回复方法 <addKeywords> 的入参为：-------------------------" + jsonObject.toJSONString());
        if (StringUtils.isEmpty(jsonObject.getString("aauthorizerAppid"))) {
            return RtnValue.getRtnMap(jsonObject, ResponseCode.FAIL.code(), "缺少必要参数authorizerAppid");
        }
        String replyId = RandomUtils.generateUuidStr();
        jsonObject.put("replyId", replyId);
        int insertKeyWord = iKeyWordSV.saveKeyWord(jsonObject);
        int insertReply = iReplyInfoSV.saveReplyInfo(jsonObject);
        int insert = insertKeyWord + insertReply;
        return insert > 1 ? RtnValue.getRtnMap(insert, ResponseCode.SUCCESS.code(), "新增关键词成功")
                : RtnValue.getRtnMap(insert, ResponseCode.FAIL.code(), "新增关键词失败");
    }

    /**
     * 修改关键词回复
     *
     * @param jsonObject
     * @return 返回修改关键词回复的结果
     */
    @RequestMapping(method = RequestMethod.PUT)
//    @ApiOperation(value = "修改关键词回复", notes = "返回修改关键词回复的结果集",
//            consumes = "application/json", produces = "application/json", httpMethod = "PUT")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> modifyKeywords(@RequestBody JSONObject jsonObject) {
        System.out.println("执行修改关键词回复方法  <modifyKeywords> 的入参为：--------------------" + jsonObject.toJSONString());
        if (StringUtils.isEmpty(jsonObject.getString("authorizerAppid"))) {
            return RtnValue.getRtnMap(jsonObject, ResponseCode.FAIL.code(), "缺少必要参数authorizerAppid");
        }

        String replyId = jsonObject.getString("replyId");

        // 根据replyId 将关键词表中的关键词全部删除
        int delete = iKeyWordSV.deleteKeyWord(replyId);
        System.out.println("执行修改关键词的方法 <modifyKeywords> 的第一步删除关键词的结果为 【0：失败，>=1：成功】 delete = " + delete);

        // 根据keywords数组和replyId组装成一个list 添加关键词表
        int update = iKeyWordSV.saveKeyWord(jsonObject);

        // 更新回复表
        update += iReplyInfoSV.updateReplyInfo(jsonObject);
        return update > 1 ? RtnValue.getRtnMap(update, ResponseCode.SUCCESS.code(), "修改关键词成功")
                : RtnValue.getRtnMap(update, ResponseCode.FAIL.code(), "修改关键词失败");
    }


    /**
     * 查看单个关键词回复
     *
     * @param replyId
     * @return 返回单个关键词回复的结果集
     */
    @RequestMapping(method = RequestMethod.GET)
//    @ApiOperation(value = "查看单个关键词回复", notes = "返回单个关键词回复的结果集",
//            consumes = "application/json", produces = "application/json", httpMethod = "GET")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> getKeywords(@RequestParam String replyId) {
        System.out.println("执行查询单个关键词回复方法  <getKeywords> 的入参为：-------------------- replyId = " + replyId);

        JSONObject result = this.getList(replyId);
        System.out.println("执行查询单个关键词回复的方法 <getKeywords> 的结果为： -------------------------------" + result.toJSONString());
        return RtnValue.getRtnMap(result, ResponseCode.SUCCESS.code(), "查询单个关键词回复成功");
    }


    /**
     * 查询关键词回复列表
     *
     * @param authorizerAppid 公众号appid
     * @return 返回该公众号下的所有关键词回复的结果集
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    @ApiOperation(value = "查看关键词回复列表", notes = "返回关键词回复列表的结果集",
//            consumes = "application/json", produces = "application/json", httpMethod = "GET")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> getKeywordsList(@RequestParam String authorizerAppid) {
        System.out.println("执行查询关键词回复列表的方法 <getKeywordsList> 的入参为：----------------------" + authorizerAppid);
        JSONArray jsonArray = new JSONArray();
        // 先用authorizerAppid 查询回复表，得到回复的List，从list中获取所有的replyId
        List<ReplyInfo> replyInfoList = iReplyInfoSV.selectReplyInfoList(authorizerAppid);
        for (ReplyInfo replyInfo : replyInfoList){
            String replyId = replyInfo.getReplyId();
            JSONObject result = this.getList(replyId);
            jsonArray.add(result);
        }
        // 根据replyId 查询所有的关键词 组装成结果集返回
        System.out.println("调用查询公众号下的关键词列表方法 <getKeywordsList> 的结果为 -----------------" + jsonArray.toJSONString());

        return RtnValue.getRtnMap(jsonArray, ResponseCode.SUCCESS.code(), "查询关键词回复列表成功");
    }

    /**
     * 删除关键词回复
     *
     * @param replyId
     * @return 返回删除关键词回复的结果
     */
    @RequestMapping(value = "/{replyId}",method = RequestMethod.DELETE)
//    @ApiOperation(value = "删除关键词回复", notes = "返回删除关键词回复的结果集",
//            consumes = "application/json", produces = "application/json", httpMethod = "DELETE")
//    @ApiImplicitParams({})
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功", response = JSONObject.class),
//            @ApiResponse(code = 500, message = "失败", response = JSONObject.class)
//    })
    public Map<String, Object> deleteKeywords(@PathVariable String replyId) {
        int delete = iKeyWordSV.deleteKeyWord(replyId);
        delete += iReplyInfoSV.deleteByReplyId(replyId);
        System.out.println("执行controller层 删除关键词回复方法 <deleteKeywords> 的结果为：【<1：失败，>1：成功】 delete = " +delete);
        return delete > 1 ? RtnValue.getRtnMap(delete, ResponseCode.SUCCESS.code(), "删除关键词回复成功")
                : RtnValue.getRtnMap(delete, ResponseCode.FAIL.code(), "删除关键词回复失败");
    }

    /**
     * 生成关键词回复列表
     * @param replyId
     * @return
     */
    private JSONObject getList(String replyId){
        // 根据 replyId 查询关键词List列表
        List<KeyWord> keyWordList = iKeyWordSV.selectKeyWordList(replyId);
        List<String> keywords = new ArrayList<>();
        for(KeyWord keyWord : keyWordList){
            keywords.add(keyWord.getKeyWord());
        }
        // 根据 replyId 查询回复
        ReplyInfo replyInfo = iReplyInfoSV.selectByReplyId(replyId);

        // 根据回复类型进行回复
        JSONObject result = new JSONObject();
        result.put("ruleName", keyWordList.get(0).getRuleName()); // 由于同一个replyId的规则都相同，直接取第一个
        result.put("keyword", keywords);

        String replyType = replyInfo.getReplyType();
        result.put("replyType", replyType);
        if (replyType.equals("text")){
            result.put("text", replyInfo.getText());
        }else {
            result.put("imageUrl", replyInfo.getImageUrl());
        }
        return result;
    }

//    /**
//     * 校验接口调用凭据和刷新令牌是否过期
//     *
//     * @param authorizationAppid
//     * @param tenantId
//     * @return
//     */
//    public String checkUpdateTime(String authorizationAppid, String tenantId) {
//        WeChatInfo weChatInfo = iWeChatInfoSV.selectWeChatInfoByAuthAppid(authorizationAppid);
//        Long accessUpdateTime = Long.parseLong(weChatInfo.getAccessUpdateTime());
//        Long refreshUpdateTIme = Long.parseLong(weChatInfo.getRefreshUpdateTime());
//        Long currentTime = System.currentTimeMillis();
//        if ((currentTime - accessUpdateTime) / 1000 >= 60 * 60 * 2) { // 先判断接口调用凭据是否过期
//            if ((currentTime - refreshUpdateTIme) / 1000 >= 60 * 60 * 24 * 30) { // 如果刷新令牌的更新时间超过30天，需要进行重新授权
//                return "";
//            } else {
//                AccessTokenInfo accessTokenInfo = iAccessTokenInfoSV.selectActInfoByTenantId(tenantId);
//                String componentAccessToken = accessTokenInfo.getComponentAccessToken();
//                String authorizerRefreshToken = weChatInfo.getAuthorizerRefreshToken();
//                JSONObject params = new JSONObject();
//                params.put("component_appid", ComponentAppId);
//                params.put("authorizer_appid", authorizationAppid);
//                params.put("authorizer_refresh_token", authorizerRefreshToken);
//                String result = HttpClientUtil.httpPost("https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=" + componentAccessToken, params.toJSONString());
//                LOGGER.info("在订阅controller中通过刷新令牌获取新的接口调用凭据的结果为---------------------+" + result);
//                JSONObject resultObject = JSONObject.parseObject(result);
//                String authorizerAccessToken = resultObject.getString("authorizer_access_token");
//
//                // 拼接参数更新本地数据库
//                JSONObject params1 = new JSONObject();
//                params1.put("tenantId", tenantId);
//                params1.put("authorizerAccessToken", authorizerAccessToken);
//                iAccessTokenInfoSV.updateByTenantId(params1);
//                return authorizerAccessToken;
//            }
//
//        } else { // 接口调用凭据在有效期内
//            return weChatInfo.getAuthorizerAccessToken();
//        }
//    }
}

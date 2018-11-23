package com.cn.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.model.weChat.KeyWord;

import java.util.List;

/**
 * Created by YancyPeng on 2018/10/27.
 * 关键词service层
 */
public interface IKeyWordSV {
    /**
     * 根据关键词列表新增关键词
     * @param jsonObject
     * @return
     */
    int saveKeyWord(JSONObject jsonObject);

    /**
     * 根据replyId删除所有关键词
     * @param replyId
     * @return
     */
    int deleteKeyWord(String replyId);

    /**
     * 根据回复Id和公众号appid查找所有关键词（获取单个关键词回复）
     * @param replyId
     * @return
     */
    List<KeyWord> selectKeyWordList(String replyId);

//    /**
//     * 根据公众号appid查找所有关键词（获取当前公众号下所有的关键词回复）
//     * @param authorizerAppid
//     * @return
//     */
//    List<KeyWord> selectByAuthAppid(String authorizerAppid);

    /**
     * 根据规则名称和公众号appid查询replyId
     *
     * @param jsonObject
     * @return
     */
    String selectByRuleName(JSONObject jsonObject);

    /**
     * 根据关键词和公众号appid查询replyId，用于根据用户发送的关键词查询
     * @param jsonObject
     * @return
     */
    String selectByKeyWord(JSONObject jsonObject);
}

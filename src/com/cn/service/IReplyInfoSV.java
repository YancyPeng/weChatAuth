package com.cn.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.model.weChat.ReplyInfo;

import java.util.List;

/**
 * Created by YancyPeng on 2018/10/27.
 * 回复service层
 */
public interface IReplyInfoSV {
    /**
     * 根据replyI查询回复
     * @param replyId
     * @return
     */
    ReplyInfo selectByReplyId(String replyId);

    /**
     * 根据replyId删除回复
     * @param replyId
     * @return
     */
    int deleteByReplyId(String replyId);

    /**
     * 根据公众号appid查询所有的回复
     * @param authorizerAppid
     * @return
     */
    List<ReplyInfo> selectReplyInfoList(String authorizerAppid);

    /**
     * 更新回复信息
     * @param jsonObject
     * @return
     */
    int updateReplyInfo(JSONObject jsonObject);

    /**
     * 新增回复
     * @param jsonObject
     * @return
     */
    int saveReplyInfo(JSONObject jsonObject);
}

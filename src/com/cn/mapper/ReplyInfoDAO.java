package com.cn.mapper;


import com.cn.model.weChat.ReplyInfo;

import java.util.List;

/**
 * Created by YancyPeng on 2018/10/26.
 * 回复信息DAO层
 */
public interface ReplyInfoDAO {

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
     * @param replyInfo
     * @return
     */
    int updateReplyInfo(ReplyInfo replyInfo);

    /**
     * 新增回复
     * @param replyInfo
     * @return
     */
    int saveReplyInfo(ReplyInfo replyInfo);
}

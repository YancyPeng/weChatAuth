package com.cn.mapper;

import com.cn.model.weChat.KeyWord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by YancyPeng on 2018/10/26.
 * 关键词信息DAO层
 */
public interface KeyWordDAO {

    /**
     * 根据关键词列表新增关键词
     *
     * @param keyWords
     * @return
     */
    int saveKeyWord(List<KeyWord> keyWords);

    /**
     * 根据replyId删除所有关键词
     *
     * @param replyId
     * @return
     */
    int deleteKeyWord(String replyId);

    /**
     * 根据回复Id和公众号appid查找所有关键词（获取单个关键词回复）
     *
     * @param replyId
     * @return
     */
    List<KeyWord> selectKeyWordList(String replyId);

    /**
     * 根据公众号appid查找所有关键词（获取当前公众号下所有的关键词回复）
     *
     * @param authorizerAppid
     * @return
     */
    List<KeyWord> selectByAuthAppid(String authorizerAppid);

    /**
     * 根据关键词和公众号appid查询replyId
     *
     * @param ruleName
     * @return
     */
    String selectByRuleName(@Param("ruleName") String ruleName, @Param("authorizerAppid") String authorizerAppid);

    /**
     * 根据关键词和公众号appid查询replyId
     *
     * @param keyWord
     * @param authorizerAppid
     * @return
     */
    String selectByKeyWord(@Param("keyWord") String keyWord, @Param("authorizerAppid") String authorizerAppid);
}

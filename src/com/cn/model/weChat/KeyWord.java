package com.cn.model.weChat;

import java.io.Serializable;

/**
 * Created by YancyPeng on 2018/10/26.
 * 关键词信息实体类
 */
public class KeyWord implements Serializable{

    private static final long serialVersionUID = -2903430464424013818L;

    // 主键Id
    private Long pkId;

    // 授权方appid
    private String authorizerAppid;

    // 规则名称
    private String ruleName;

    // 关键词
    private String keyWord;

    // 回复Id（对应于回复表中的replyId）
    private String replyId;

    // 创建时间
    private String createTime;

    // 创建用户id
    private String createUserId;

    // 更新时间
    private String updateTime;

    // 更新用户id
    private String updateUserId;



    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public KeyWord() {
        super();
    }

    @Override
    public String toString() {
        return "KeyWord{" +
                "pkId=" + pkId +
                ", authorizerAppid='" + authorizerAppid + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", replyId='" + replyId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", updateUserId='" + updateUserId + '\'' +
                '}';
    }
}

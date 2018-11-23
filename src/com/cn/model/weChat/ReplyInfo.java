package com.cn.model.weChat;

import java.io.Serializable;

/**
 * Created by YancyPeng on 2018/10/26.
 * 回复信息表
 */
public class ReplyInfo implements Serializable{

    private static final long serialVersionUID = 744994463022815435L;

    // 主键Id
    private Long pkId;

    // 回复Id
    private String replyId;

    // 授权公众号appId
    private String authorizerAppid;

    // 回复类型
    private String replyType;

    // 回复文本
    private String text;

    // 回复图片mediaId（回复给关注用户时使用）
    private String imageMediaId;

    // 回复图片的url（在本地查看图片时使用）
    private String imageUrl;

    // 创建时间
    private String createTime;

    // 创建用户id
    private String createUserId;

    // 更新时间
    private String updateTime;

    // 更新用户id
    private String updateUserId;


    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageMediaId() {
        return imageMediaId;
    }

    public void setImageMediaId(String imageMediaId) {
        this.imageMediaId = imageMediaId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public ReplyInfo() {
        super();
    }

    @Override
    public String toString() {
        return "ReplyInfo{" +
                "pkId=" + pkId +
                ", replyId='" + replyId + '\'' +
                ", authorizerAppid='" + authorizerAppid + '\'' +
                ", replyType='" + replyType + '\'' +
                ", text='" + text + '\'' +
                ", imageMediaId='" + imageMediaId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", updateUserId='" + updateUserId + '\'' +
                '}';
    }
}

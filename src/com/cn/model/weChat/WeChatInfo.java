package com.cn.model.weChat;

import java.io.Serializable;

/**
 * Created by YancyPeng on 2018/10/26.
 * 微信公众号信息实体类
 */
public class WeChatInfo implements Serializable{

    private static final long serialVersionUID = -9060117717011994408L;
    // 主键ID
    private Long pkId;

    // 公众号appid
    private String authorizerAppid;

    // 租户Id
    private String tenantId;

    // 公众号原始ID
    private String userName;

    // 授权方公众号昵称
    private String nickName;

    // 授权方头像url
    private String headImg;

    // 授权方公众号类型
    private String serviceTypeInfo;

    // 授权方公众号认证类型
    private String verifyTypeInfo;

    // 公众号的主体名称
    private String principalName;

    // 授权方二维码图片的URl
    private String qrcodeUrl;

    // 创建时间
    private String createTime;

    // 创建用户id
    private String createUserId;

    // 接口调用凭据更新时间
    private String accessUpdateTime;

    // 刷新令牌更新时间
    private String refreshUpdateTime;

    // 授权码，只用来获取公众号的接口调用凭据和刷新令牌
    private String authorizationCode;

    // 公众号接口调用凭据
    private String authorizerAccessToken;

    // 公众号刷新令牌
    private String authorizerRefreshToken;

    // 公众号授权状态，0未授权，1已授权，2授权过期,3状态异常（只在微信端主动取消授权后设置为此状态）
    private String authorizerState;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(String serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    public String getVerifyTypeInfo() {
        return verifyTypeInfo;
    }

    public void setVerifyTypeInfo(String verifyTypeInfo) {
        this.verifyTypeInfo = verifyTypeInfo;
    }

    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAuthorizerAccessToken() {
        return authorizerAccessToken;
    }

    public void setAuthorizerAccessToken(String authorizerAccessToken) {
        this.authorizerAccessToken = authorizerAccessToken;
    }

    public String getAuthorizerRefreshToken() {
        return authorizerRefreshToken;
    }

    public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
        this.authorizerRefreshToken = authorizerRefreshToken;
    }

    public String getAuthorizerState() {
        return authorizerState;
    }

    public void setAuthorizerState(String authorizerState) {
        this.authorizerState = authorizerState;
    }

    public String getAccessUpdateTime() {
        return accessUpdateTime;
    }

    public void setAccessUpdateTime(String accessUpdateTime) {
        this.accessUpdateTime = accessUpdateTime;
    }

    public String getRefreshUpdateTime() {
        return refreshUpdateTime;
    }

    public void setRefreshUpdateTime(String refreshUpdateTime) {
        this.refreshUpdateTime = refreshUpdateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }


    public WeChatInfo(){
        super();
    }

    @Override
    public String toString() {
        return "WeChatInfo{" +
                "pkId=" + pkId +
                ", authorizerAppid='" + authorizerAppid + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", serviceTypeInfo='" + serviceTypeInfo + '\'' +
                ", verifyTypeInfo='" + verifyTypeInfo + '\'' +
                ", principalName='" + principalName + '\'' +
                ", qrcodeUrl='" + qrcodeUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", accessUpdateTime='" + accessUpdateTime + '\'' +
                ", refreshUpdateTime='" + refreshUpdateTime + '\'' +
                ", authorizationCode='" + authorizationCode + '\'' +
                ", authorizerAccessToken='" + authorizerAccessToken + '\'' +
                ", authorizerRefreshToken='" + authorizerRefreshToken + '\'' +
                ", authorizerState='" + authorizerState + '\'' +
                '}';
    }
}

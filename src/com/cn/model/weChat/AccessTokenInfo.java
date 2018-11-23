package com.cn.model.weChat;

import java.io.Serializable;

/**
 * Created by YancyPeng on 2018/10/26.
 * 授权码公共信息实体类
 */
//@ApiModel(value = "AccessTokenInfo", description = "授权码公共信息实体类")
public class AccessTokenInfo implements Serializable{

    private static final long serialVersionUID = 7552743614811735919L;

    // 主键Id
    private Long pkId;

    // 租户id
    private String tenantId;

    // 第三方核实Key
    private String componentVerifyTicket;

    // 第三方接口调用凭据
    private String componentAccessToken;

    // 预授权码
    private String preAuthCode;

    // 创建时间
    private String createTime;

    // 第三方接口调用凭据更新时间
    private String tokenUpdateTime;

    // 预授权码更新时间
    private String codeUpdateTime;

    public String getComponentVerifyTicket() {
        return componentVerifyTicket;
    }

    public void setComponentVerifyTicket(String componentVerifyTicket) {
        this.componentVerifyTicket = componentVerifyTicket;
    }

    public String getComponentAccessToken() {
        return componentAccessToken;
    }

    public void setComponentAccessToken(String componentAccessToken) {
        this.componentAccessToken = componentAccessToken;
    }

    public String getPreAuthCode() {
        return preAuthCode;
    }

    public void setPreAuthCode(String preAuthCode) {
        this.preAuthCode = preAuthCode;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCodeUpdateTime() {
        return codeUpdateTime;
    }

    public void setCodeUpdateTime(String codeUpdateTime) {
        this.codeUpdateTime = codeUpdateTime;
    }

    public String getTokenUpdateTime() {
        return tokenUpdateTime;
    }

    public void setTokenUpdateTime(String tokenUpdateTime) {
        this.tokenUpdateTime = tokenUpdateTime;
    }

    public AccessTokenInfo() {
        super();
    }

    @Override
    public String toString() {
        return "AccessTokenInfo{" +
                "pkId=" + pkId +
                ", tenantId='" + tenantId + '\'' +
                ", componentVerifyTicket='" + componentVerifyTicket + '\'' +
                ", componentAccessToken='" + componentAccessToken + '\'' +
                ", preAuthCode='" + preAuthCode + '\'' +
                ", createTime='" + createTime + '\'' +
                ", tokenUpdateTime='" + tokenUpdateTime + '\'' +
                ", codeUpdateTime='" + codeUpdateTime + '\'' +
                '}';
    }
}

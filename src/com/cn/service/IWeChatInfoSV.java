package com.cn.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.model.weChat.WeChatInfo;

import java.util.List;


/**
 * Created by YancyPeng on 2018/10/27.
 * 微信公众号信息service层
 */
public interface IWeChatInfoSV {

    /**
     * 添加微信公众号信息
     * @param jsonObject
     * @return
     */
    int saveWeChatInfo(JSONObject jsonObject);

    /**
     * 根据公众号id查询公众号信息
     * @param authorizerAppid
     * @return
     */
    WeChatInfo selectWeChatInfoByAuthAppid(String authorizerAppid);

    /**
     * 根据租户id查询所有公众号
     * @param tenantId
     * @return
     */
    List<WeChatInfo> selectWeChatInfoList(String tenantId);

    /**
     * 根据appid删除公众号
     * @param authorizerAppid
     * @return
     */
    int deleteByAuthAppid(String authorizerAppid);

    /**
     * 根据appid进行更新
     * @param jsonObject
     * @return
     */
    int updateByAuthAppid(JSONObject jsonObject);

    /**
     * 按需添加微信公众号信息
     * @param jsonObject
     * @return
     */
    int insertSelective(JSONObject jsonObject);
}

package com.cn.mapper;


import com.cn.model.weChat.WeChatInfo;

import java.util.List;

/**
 * Created by YancyPeng on 2018/10/26.
 * 微信公众号信息 DAO层
 */
public interface WeChatInfoDAO {

    /**
     * 添加微信公众号信息
     * @param weChatInfo
     * @return
     */
    int saveWeChatInfo(WeChatInfo weChatInfo);

    /**
     * 根据公众号appid查询公众号信息
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
     * 根据公众号appid取消授权
     * @param authorizerAppid
     * @return
     */
    int deleteByAuthAppid(String authorizerAppid);

    /**
     * 根据公众号appid进行更新
     * @param weChatInfo
     * @return
     */
    int updateByAuthAppid(WeChatInfo weChatInfo);

    /**
     * 按需添加微信公众号信息
     * @param weChatInfo
     * @return
     */
    int insertSelective(WeChatInfo weChatInfo);
}

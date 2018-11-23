package com.cn.mapper;


import com.cn.model.weChat.AccessTokenInfo;

/**
 * Created by YancyPeng on 2018/10/26.
 * 授权码公共信息 DAO层
 */
public interface AccessTokenInfoDAO {

    /**
     * 根据租户id查询授权码公共信息
     * @return
     */
    AccessTokenInfo selectActInfo();

    /**
     * 根据租户id更改授权码公共信息
     * @param accessTokenInfo
     * @return
     */
    int updateAccessToken(AccessTokenInfo accessTokenInfo);

    /**
     * 添加授权码公共信息
     * @param accessTokenInfo
     * @return
     */
    int saveActInfo(AccessTokenInfo accessTokenInfo);

    /**
     * 按需添加微信公众号信息
     * @param accessTokenInfo
     * @return
     */
    int insertSelective(AccessTokenInfo accessTokenInfo);
}

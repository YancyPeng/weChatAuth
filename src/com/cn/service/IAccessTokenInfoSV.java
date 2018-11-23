package com.cn.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.model.weChat.AccessTokenInfo;

/**
 * Created by YancyPeng on 2018/10/27.
 * 公共授权码service层
 */
public interface IAccessTokenInfoSV {
    /**
     * 根据租户id查询授权码公共信息
     * @return
     */
    AccessTokenInfo selectActInfo();

    /**
     * 根据租户id更改授权码公共信息
     * @param jsonObject
     * @return
     */
    int updateAccessToken(JSONObject jsonObject);

    /**
     * 添加授权码公共信息
     * @param jsonObject
     * @return
     */
    int saveActInfo(JSONObject jsonObject);

    /**
     * 按需添加公共授权码
     * @param jsonObject
     * @return
     */
    int insertSelective(JSONObject jsonObject);

}

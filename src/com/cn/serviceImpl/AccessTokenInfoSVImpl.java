package com.cn.serviceImpl;


import com.alibaba.fastjson.JSONObject;
import com.cn.mapper.AccessTokenInfoDAO;
import com.cn.model.weChat.AccessTokenInfo;
import com.cn.service.IAccessTokenInfoSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * Created by YancyPeng on 2018/10/30.
 * 公共授权码实现类
 */
@Service("iAccessTokenInfoSV")
public class AccessTokenInfoSVImpl implements IAccessTokenInfoSV {

//    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenInfoSVImpl.class);

    @Autowired
    private AccessTokenInfoDAO accessTokenInfoDAO;

    /**
     * 根据租户id查询公共授权码
     *
     * @return
     */
    @Override
    public AccessTokenInfo selectActInfo() {
        System.out.println("执行service层 查询公共授权码的方法  <selectActInfo> ");
        AccessTokenInfo accessTokenInfo = accessTokenInfoDAO.selectActInfo();
        System.out.println("执行service层 根据租户id查询公共授权码的方法  <selectActInfo> 的结果为：------------------" + accessTokenInfo);
        return accessTokenInfo;
    }

    /**
     * 更新公共授权码
     *
     * @param jsonObject
     * @return
     */
    @Override
    public int updateAccessToken(JSONObject jsonObject) {
        System.out.println("执行service层 根据租户id更新公共授权码的方法 <updateAccessToken> 的入参为：------------------" + jsonObject.toJSONString());
        AccessTokenInfo accessTokenInfo = JSONObject.parseObject(jsonObject.toJSONString(), AccessTokenInfo.class);
        System.out.println("执行service层 根据租户id更新公共授权码的方法 <updateAccessToken> 拼装成的 AccessTokenInfo 对象为：--------------" + accessTokenInfo );
        int update = accessTokenInfoDAO.updateAccessToken(accessTokenInfo);
        System.out.println("更新授权码 【0：失败，1：成功】 update = "+ update);
        return update;
    }

    /**
     * 添加公共授权码
     *
     * @param jsonObject
     * @return
     */
    @Override
    public int saveActInfo(JSONObject jsonObject) {
        System.out.println("执行service层 添加公共授权码（首次调用）的方法 <saveActInfo> 的入参为：--------------------" + jsonObject.toJSONString());
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        jsonObject.put("createTime", currentTime);
        jsonObject.put("tokenUpdateTime", currentTime);
        jsonObject.put("codeUpdateTime", currentTime);
        AccessTokenInfo accessTokenInfo = JSONObject.parseObject(jsonObject.toJSONString(), AccessTokenInfo.class);
        int insert = accessTokenInfoDAO.saveActInfo(accessTokenInfo);
        System.out.println("添加公共授权码 【0：失败，1：成功】 insert = " + insert);
        return insert;
    }

    /**
     * 按需添加
     *
     * @param jsonObject
     * @return
     */
    @Override
    public int insertSelective(JSONObject jsonObject) {
        System.out.println("执行service层 按需添加公共授权码的方法 <insertSelective> 的入参为：-------------------"+ jsonObject.toJSONString());
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        jsonObject.put("createTime", currentTime);
        jsonObject.put("tokenUpdateTime", currentTime);
        jsonObject.put("codeUpdateTime", currentTime);
        AccessTokenInfo accessTokenInfo = JSONObject.parseObject(jsonObject.toJSONString(), AccessTokenInfo.class);
        System.out.println("执行service层 按需添加公共授权码的方法 <insertSelective> 拼装成的 accessTokenInfo 为：-------------" + accessTokenInfo);
        int insert = accessTokenInfoDAO.saveActInfo(accessTokenInfo);
        System.out.println("按需添加公共授权码 【0：失败，1：成功】 insert = " + insert);
        return insert;
    }
}

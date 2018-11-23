package com.cn.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.cn.mapper.WeChatInfoDAO;
import com.cn.model.weChat.WeChatInfo;
import com.cn.service.IWeChatInfoSV;
import freemarker.template.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by YancyPeng on 2018/10/27.
 * 微信基本信息实现类
 */
@Service
public class WeChatInfoSVImpl implements IWeChatInfoSV {

//    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatInfoSVImpl.class);

    @Autowired
    private WeChatInfoDAO weChatInfoDAO;

    /**
     * 添加微信基本信息
     *
     * @param jsonObject
     * @return
     */
    @Override
    public int saveWeChatInfo(JSONObject jsonObject) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        jsonObject.put("createTime",currentTime);
        jsonObject.put("accessUpdateTime",currentTime);
        jsonObject.put("refreshUpdateTime",currentTime);
        System.out.println("执行service层 添加微信公众号信息方法  <saveWeChatInfo> 的入参为：---------------------" + jsonObject.toJSONString());
        WeChatInfo weChatInfo = JSONObject.parseObject(jsonObject.toJSONString(), WeChatInfo.class);
        int insert = weChatInfoDAO.saveWeChatInfo(weChatInfo);
        return insert;
    }

    /**
     * 根据公众号appid查询公众号的详细信息
     *
     * @param authorizerAppid
     * @return
     */
    @Override
    public WeChatInfo selectWeChatInfoByAuthAppid(String authorizerAppid) {
        System.out.println("执行service层 根据公众号appid查询公众号详细信息的方法 <selectWeChatInfoByAuthAppid> 的入参为：--------------" + authorizerAppid);
        WeChatInfo weChatInfo = weChatInfoDAO.selectWeChatInfoByAuthAppid(authorizerAppid);
        return weChatInfo;
    }

    /**
     * 根据当前租户id查询当前租户下的所有公众号
     *
     * @param tenantId
     * @return
     */
    @Override
    public List<WeChatInfo> selectWeChatInfoList(String tenantId) {
        System.out.println("执行service层 查询当前公众号下公众号列表的方法 <selectWeChatInfoList> 的入参为：----------------" + tenantId);
        List<WeChatInfo> weChatInfoList = weChatInfoDAO.selectWeChatInfoList(tenantId);
        return weChatInfoList;
    }

    /**
     * 根据公众号appid 取消授权，修改状态
     *
     * @param authorizerAppid
     * @return
     */
    @Override
    public int deleteByAuthAppid(String authorizerAppid) {
        System.out.println("执行service层 根据公众号appid删除公众号信息的方法 <deleteByAuthAppid> 的入参为：------------------" + authorizerAppid);
        int delete = weChatInfoDAO.deleteByAuthAppid(authorizerAppid);
        System.out.println("删除公众号信息 【0失败，1成功】，delete =" + delete);
        return delete;
    }

    /**
     * 根据公众号appid修改公众号信息
     *
     * @param jsonObject
     * @return
     */
    @Override
    public int updateByAuthAppid(JSONObject jsonObject) {
        System.out.println("执行service层 根据appid修改公众号信息的方法 <updateByAuthAppid> 的入参为：-------------------" + jsonObject.toString());
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        if (!(StringUtils.isEmpty(jsonObject.getString("authorizerAccessToken")))){
            jsonObject.put("accessUpdateTime",currentTime);
        }
        if (!(StringUtils.isEmpty(jsonObject.getString("authorizerRefreshToken")))){
            jsonObject.put("refreshUpdateTime",currentTime);
        }
        WeChatInfo weChatInfo = JSONObject.parseObject(jsonObject.toJSONString(),WeChatInfo.class);
        int update = weChatInfoDAO.updateByAuthAppid(weChatInfo);
        System.out.println("修改公众号信息 【0失败，1成功】,update=" + update);
        return update;
    }

    /**
     * 按需添加
     *
     * @param jsonObject
     * @return
     */
    @Override
    public int insertSelective(JSONObject jsonObject) {
        System.out.println("执行service层 按需添加公众号信息的方法 <insertSelective> 的入参为：-------------------" + jsonObject.toString());
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        jsonObject.put("createTime",currentTime);
        jsonObject.put("accessUpdateTime",currentTime);
        jsonObject.put("refreshUpdateTime",currentTime);
        WeChatInfo weChatInfo = JSONObject.parseObject(jsonObject.toJSONString(),WeChatInfo.class);
        int insert = weChatInfoDAO.insertSelective(weChatInfo);
        System.out.println("按需添加公众号信息 【0：失败，1：成功】， insert = " + insert);
        return insert;
    }
}

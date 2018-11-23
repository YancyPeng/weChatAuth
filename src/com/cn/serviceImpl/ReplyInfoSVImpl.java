package com.cn.serviceImpl;


import com.alibaba.fastjson.JSONObject;
import com.cn.mapper.ReplyInfoDAO;
import com.cn.model.weChat.ReplyInfo;
import com.cn.service.IReplyInfoSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by YancyPeng on 2018/11/1.
 */
@Service
public class ReplyInfoSVImpl implements IReplyInfoSV {

//    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyInfoSVImpl.class);

    @Autowired
    private ReplyInfoDAO replyInfoDAO;

    /**
     * 根据replyId 查看单个回复详情
     * @param replyId
     * @return
     */
    @Override
    public ReplyInfo selectByReplyId(String replyId) {
        System.out.println("执行service层 查询单个回复方法 <selectByReplyId> 的入参为：replyId = " + replyId);
        ReplyInfo replyInfo = replyInfoDAO.selectByReplyId(replyId);
        System.out.println("执行service层 查询单个回复方法 <selectByReplyId> 的结果为 --------------" + replyInfo);
        return replyInfo;
    }

    /**
     * 根据replyId 删除回复
     * @param replyId
     * @return
     */
    @Override
    public int deleteByReplyId(String replyId) {
        System.out.println("执行service层 删除回复方法 <deleteByReplyId> 的入参为： replyId = "+ replyId);
        int delete = replyInfoDAO.deleteByReplyId(replyId);
        System.out.println("执行service层 删除回复方法 <deleteByReplyId> 的结果为：【0：失败，1：成功】 delete = " + delete);
        return delete;
    }

    /**
     * 根据微信appid查询回复列表
     * @param authorizerAppid
     * @return
     */
    @Override
    public List<ReplyInfo> selectReplyInfoList(String authorizerAppid) {
        System.out.println("执行service层 查询回复列表方法 <selectReplyInfoList> 的入参为：authorizerAppid = " + authorizerAppid);
        List<ReplyInfo> replyInfoList = replyInfoDAO.selectReplyInfoList(authorizerAppid);
        return replyInfoList;
    }

    /**
     * 根据replyId修改回复
     * @param jsonObject
     * @return
     */
    @Override
    public int updateReplyInfo(JSONObject jsonObject) {
        System.out.println("执行service层 修改回复方法 <updateReplyInfo> 的入参为：-----------------" + jsonObject.toJSONString());
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        jsonObject.put("updateTime", currentTime);
//            String replyType = jsonObject.getString("replyType");

//        int update = 0;
//        if (replyType.equals("text")) { // 如果类型是文本
//            String text = jsonObject.getString("text");
//            JSONObject params = new JSONObject();
//            params.put("replyType", "text");
//            params.put("text", text);
//            update = iReplyInfoSV.updateReplyInfo(params);
//        } else { // 是图片
//            String mediaId = jsonObject.getString("mediaId");
//            String imageUrl = jsonObject.getString("imageUrl");
//            JSONObject params = new JSONObject();
//            params.put("replyId", replyId);
//            params.put("mediaId", mediaId);
//            params.put("imageUrl", imageUrl);
//            update = iReplyInfoSV.updateReplyInfo(params);
//        }
        ReplyInfo replyInfo = JSONObject.parseObject(jsonObject.toJSONString(), ReplyInfo.class);
        int update = replyInfoDAO.updateReplyInfo(replyInfo);
        System.out.println("执行service层，修改回复方法 <updateReplyInfo> 的结果【0：失败，1：成功】 insert = "+ update);
        return update;
    }

    /**
     * 新增回复
     *
     * @param jsonObject
     * @return
     */
    @Override
    public int saveReplyInfo(JSONObject jsonObject) {
        System.out.println("执行service层 新增回复方法 <saveReplyInfo> 的入参为：-----------------" + jsonObject.toJSONString());
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        jsonObject.put("currentTime",currentTime);
        jsonObject.put("updateTime", currentTime);
//        String replyType = jsonObject.getString("replyType");
//        ReplyInfo replyInfo = new ReplyInfo();
//        replyInfo.setAuthorizerAppid(authorizationAppid);
//        replyInfo.setReplyId(replyId);
//        replyInfo.setCreateTime(String.valueOf(currentTime));
//        replyInfo.setUpdateTime(String.valueOf(currentTime));
//        if (replyType.equals("text")) {
//            String text = jsonObject.getString("text");
//            replyInfo.setReplyType("text");
//            replyInfo.setText(text);
//        } else { // 是图片
//            String imageMediaId = jsonObject.getString("imageMediaId");
//            String imageUrl = jsonObject.getString("imageUrl");
//            replyInfo.setReplyType("image");
//            replyInfo.setImageMediaId(imageMediaId);
//            replyInfo.setImageUrl(imageUrl);
//        }
        ReplyInfo replyInfo = JSONObject.parseObject(jsonObject.toJSONString(), ReplyInfo.class);
        int insertReply = replyInfoDAO.saveReplyInfo(replyInfo);
        System.out.println("执行service层，新增关键词回复方法 <saveReplyInfo> 的结果为：【0：失败，1：成功】 insertReply = " + insertReply);
        return insertReply;
    }
}

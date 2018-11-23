package com.cn.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.cn.mapper.KeyWordDAO;
import com.cn.mapper.ReplyInfoDAO;
import com.cn.model.weChat.KeyWord;
import com.cn.service.IKeyWordSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YancyPeng on 2018/11/1.
 * 关键词service实现层
 */
@Service
public class KeyWordSVImpl implements IKeyWordSV {

//    private static final Logger LOGGER = LoggerFactory.getLogger(KeyWordSVImpl.class);

    @Autowired
    private KeyWordDAO keyWordDAO;

    @Autowired
    private ReplyInfoDAO replyInfoDAO;

    /**
     * 保存关键词
     *
     * @param jsonObject
     * @return
     */
    @Override
    public int saveKeyWord(JSONObject jsonObject) {
        String authorizationAppid = jsonObject.getString("authorizationAppid");
        String[] keywords = jsonObject.getString("keyword").split(",");
        String replyId = jsonObject.getString("replyId");
        Long currentTime = System.currentTimeMillis();
        String ruleName = jsonObject.getString("ruleName");
        List<KeyWord> keyWordList = new ArrayList<>();
        for (String keyword : keywords) {
            KeyWord keyWord1 = new KeyWord();
            keyWord1.setKeyWord(keyword);
            keyWord1.setAuthorizerAppid(authorizationAppid);
            keyWord1.setRuleName(ruleName);
            keyWord1.setReplyId(replyId);
            keyWord1.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));
            keyWord1.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));
            keyWordList.add(keyWord1);
        }
        int insertKeyWord = keyWordDAO.saveKeyWord(keyWordList);
        System.out.println("执行service层，新增关键词方法 <saveKeyWord> 的结果为：【0：失败，>1 成功】 insertKeyWord = " + insertKeyWord);

        return insertKeyWord;
    }

    @Override
    public int deleteKeyWord(String replyId) {
        System.out.println("执行service层，删除关键词方法 <deleteKeyWord> 的入参为：replyId = " + replyId);
        int delete = keyWordDAO.deleteKeyWord(replyId);
        System.out.println("执行service层，删除关键词方法 <deleteKeyWord> 的结果为：【0：失败，>1 成功】 delete = " + delete);
        return delete;
    }

    /**
     * 查询单个关键词回复
     *
     * @param replyId
     * @return
     */
    @Override
    public List<KeyWord> selectKeyWordList(String replyId) {
        System.out.println("执行service层，查看单个关键词回复方法 <selectKeyWordList> 的入参为：replyId = " + replyId);
        List<KeyWord> keyWordList = keyWordDAO.selectKeyWordList(replyId);
        System.out.println("执行service层，查看单个关键词回复方法 <selectKeyWordList> 的结果为： " + keyWordList);
        return keyWordList;
    }

    /**
     * 根据公众号appid和关键词keyword查询replyId
     *
     * @param jsonObject
     * @return
     */
    @Override
    public String selectByRuleName(JSONObject jsonObject) {
        System.out.println("执行service层，根据公众号appid和关键词keyword查询replyId的方法 <selectByKeyWord> 的入参为：--------------------------" + jsonObject.toJSONString());
        String authorizerAppid = jsonObject.getString("authorizerAppid");
        String ruleName = jsonObject.getString("ruleName");
        String replyId = keyWordDAO.selectByRuleName(ruleName, authorizerAppid);
        System.out.println("执行service层，根据公众号appid和关键词keyword查询replyId的方法 <selectByKeyWord> 的结果为： replyId = " + replyId);
        return replyId;
    }

    /**
     * 根据公众号appid和关键词keyword查询replyId
     * @param jsonObject
     * @return
     */
    @Override
    public String selectByKeyWord(JSONObject jsonObject) {
        System.out.println("执行service层，根据公众号appid和关键词keyword查询replyId的方法 <selectByKeyWord> 的入参为：--------------------------" + jsonObject.toJSONString());
        String authorizerAppid = jsonObject.getString("authorizerAppid");
        String keyWord = jsonObject.getString("keyWord");
        String replyId = keyWordDAO.selectByKeyWord(keyWord, authorizerAppid);
        System.out.println("执行service层，根据公众号appid和关键词keyword查询replyId的方法 <selectByKeyWord> 的结果为： replyId = " + replyId);
        return replyId;
    }
}

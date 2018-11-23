package com.cn.controller.weChat.util;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by YancyPeng on 2018/10/16.
 * 微信消息加解密工具
 */
public class SignUtil {

    private static WXBizMsgCrypt pc;

    //在第三方平台填写的token

    private static String token = "hykf123456";

    //在第三方平台填写的加解密key

    private static String encodingAesKey = "8nfdaf83n3ygn3um3ianmkli2239an0184nhalocner";

    //公众号第三方平台的appid

    private static String appId = "wx77c6c812c7b5a32d";

    //微信加密签名
    private static String msg_signature;
    //时间戳
    private static String timestamp;
    //随机数
    private static String nonce;

    static {
        try {
            pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        } catch (AesException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param encryptMsg 加密的消息
     * @return 返回解密后xml格式字符串消息
     */
    public static String decryptMsg(HttpServletRequest request, String encryptMsg) {

        String result = "";

        //获取微信加密签名
        msg_signature = request.getParameter("msg_signature");

        //时间戳
        timestamp = request.getParameter("timestamp");

        //随机数
        nonce = request.getParameter("nonce");


        System.out.println("微信加密签名为：-----------------" +msg_signature);

        try {
            result = pc.decryptMsg(msg_signature, timestamp, nonce, encryptMsg);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param replyMsg 需要加密的xml格式字符串
     * @return 加密过后的xml格式字符串
     */
    public static String encryptMsg(String replyMsg) {

        try {
            replyMsg = pc.encryptMsg(replyMsg, timestamp, nonce);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return replyMsg;
    }

    private SignUtil() {

    }
}

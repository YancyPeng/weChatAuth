package com.cn.controller.weChat.util;

/**
 * describe: 统一全局响应。
 *
 * @author : 王校兵
 * @version : v1.0
 * @time : 2017-9-8
 */
public enum ResponseCode {

    SUCCESS("0000", "success"), //success为调用方法成功
    FAIL("9999", "call failed"), //fail为调用方法失败
    INVALIDVALIDATIONCODE("0001","Invalid validation code"),//验证码无效
    SMSOVERQUOTA("0002","The messages have been oversubscribed today"),//本日短信已经超额
    NOTUSERCE("0003","No user audit information"), // 没有用户审核信息
    USERNAMEALREADYEXISTS("0004","User name already exists"),//用户名已经存在

    TELALREADYEXISTS("0005","Telephone number already exists"),//手机号已经存在

    CUSTOMINFOCONFIGATEALREADYEXISTS("0006","CustomInfoConfig info already exists"),//该企业客户界面信息设置已经存在
    DIALOGTEMPLATEALREADYEXISTS("0007","DialogTemplate info already exists"),//该企业会话模板已经存在
    DELETEFILEFAILED("0008","The file delete failed "),//文件删除失败
    SAVEFILEUPLOADFAILED("0009","Save fileUpload failed "),//文件上传下载表保存失败
    DELETEFILEUPLOADFAILED("0010","Delete fileUpload failed "),//文件上传下载表删除失败

    NOTAGENTCVS("0011","No Agent Conversation information"), //没有该坐席的会话信息
    NOTONLINE("0012","Agent not online or busyness"), //坐席离线或忙碌
    SESSIONUSERISNUL("0013","session中用户信息为空"), //坐席离线或忙碌
    NOCHANGES("0014","您没有做任何修改");
    private String code;
    private String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }

}

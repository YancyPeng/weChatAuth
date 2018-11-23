package com.cn.controller.weChat.util;

import java.util.HashMap;
import java.util.Map;

/**
 * describe: 统一map封装返回值
 *
 * @author : XuWeiXing
 * @version : v1.0
 * @time : Created in 10:01 2018/2/12
 */
public class RtnValue {

    private static Map<String, Object> map = new HashMap<>();

    public static Map<String, Object> getRtnMap (Object rtnObj, String rtnCode, String rtnMsg) {
        map.put("rtnObj", rtnObj);
        map.put("rtnCode", rtnCode);
        map.put("rtnMsg", rtnMsg);
        return map;
    }
}

package com.cn.controller.weChat.util;

import java.io.File;

/**
 * describe: 文件操作工具类包括以下的功能：
 * 1、文件操作：改名
 * 2、文件名：主文件名，扩展名的获取
 * 3、
 *
 * @author : 王校兵
 * @version : v1.0
 * @time : 2017/8/29  20:49
 */
public class FileUtil {

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';
    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';


    /**
     * 判断当前环境是否为Windows环境
     *
     * @return 是否为Windows环境, 如果为windows环境返回ture，否则返回false
     */
    public static boolean isWindows() {
        return '\\' == File.separatorChar;
    }

    /**
     * 获得文件的扩展名，扩展名不带"."
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String fileSuffixName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        } else {
            String ext = fileName.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return (ext.contains(String.valueOf(UNIX_SEPARATOR)) || ext.contains(String.valueOf(WINDOWS_SEPARATOR))) ? "" : ext;
        }
    }

}

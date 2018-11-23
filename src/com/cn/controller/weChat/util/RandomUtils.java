package com.cn.controller.weChat.util;

import java.util.Random;
import java.util.UUID;

/**
 * describe: 生成随机字符串、数字等工具类
 *
 * @author : 王校兵
 * @version : v1.0
 * @time : 2017/8/28  20:53
 */
public class RandomUtils {

    public final static String _DATA_STREAM_NUM_PREFIX = "S";

    /**
     * get random seed
     *
     * @return randomSeed
     */
    private static synchronized long getRandomSeed() {

        // 通过Math的random()函数返回一个double类型随机数，范围[0.0, 1.0)
        final double random = Math.random();

        // 通过d获取一个[0, 100000)之间的整数
        final int number = (int) (random * 100000);
        return number;
    }

    /**
     * 从给定的字符串中随机取出指定长度的字符串
     *
     * @param seedStr 源字符串
     * @param length  长度
     * @return 随机生成的字符串
     */
    private static String getRandomStr(String seedStr, int length) {
        StringBuffer resultRandStr = new StringBuffer();
        int randNum = 0;
        Random rand = new Random(getRandomSeed());
        int randStrLen = seedStr.length();
        for (int i = 0; i < length; i++) {
            randNum = rand.nextInt(randStrLen);
            resultRandStr.append(seedStr.substring(randNum, randNum + 1));
        }
        return resultRandStr.toString();
    }

    /**
     * 从0-9中随机生成指定长度的字符串
     *
     * @param length 字符串的长度
     * @return 随机生成的字符串
     */
    public static String generateRandomNumberStr(int length) {
        String seedStr = "0123456789";
        return getRandomStr(seedStr, length);
    }

    /**
     * 从大小写字母、数字中随机生成指定长度的字符串
     *
     * @param length 字符串的长度
     * @return 随机生成的字符串
     */
    public static String generateRandomStr(int length) {
        String seedStr = "ABCDEFGHIJKLMNOPQRSTUVWSYZabcdefghijklmnopqrstuvwsyz0123456789";
        return getRandomStr(seedStr, length);
    }

    /**
     * 随机生成字符串，格式为:前缀随机字符+时间戳+后缀随机字符，时间格式为：yyyyMMddHHmmssSSS
     *
     * @param randomLenBefore 前缀随机字符串的长度
     * @param randomLenAfter  后缀随机字符串的长度
     * @return
     */
    public static String generateTimeStampRandomStr(int randomLenBefore, int randomLenAfter, String timeFormat) {
        StringBuffer result = new StringBuffer();
        //生成前缀随机字符串
        result.append(generateRandomStr(randomLenBefore));

        //生成时间戳
        result.append(DateUtils.getCurrentTimeString(timeFormat));

        //生成后缀随机字符串
        result.append(generateRandomStr(randomLenAfter));
        return result.toString();
    }

    /**
     * 生成一个23位的随机字符串,格式为：2位前缀字符串+"yyyyMMddHHmmssSSS"格式的时间戳+4位后缀字符
     *
     * @return 随机生成的字符串
//     */
    public final static String getStreamNo23() {
        return generateTimeStampRandomStr(2, 4);
    }

    /**
     * 生成一个32位的随机字符串,格式为：9位前缀字符串+"yyyyMMddHHmmssSSS"格式的时间戳+6位后缀字符
     *
     * @return 随机生成的字符串
     */
    public static String getCorrelator32() {
        return generateTimeStampRandomStr(9, 6);
    }


    /**
     * 生成一个大于17位的时间戳随机字符串,格式为:前缀字符串+"yyyyMMddHHmmssSSS"格式的时间戳+后缀字符
     *
     * @param randomLenBefore 前缀字符串的位数
     * @param randomLenAfter  后缀字符串的位数
     * @return 随机生成的字符串
     */
    public static String generateTimeStampRandomStr(int randomLenBefore, int randomLenAfter) {
        return generateTimeStampRandomStr(randomLenBefore, randomLenAfter, "yyyyMMddHHmmssSSS");
    }

    /**
     * 生成一个uuid字符串
     *
     * @return uuid字符串
     */
    public static String generateUuidStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}

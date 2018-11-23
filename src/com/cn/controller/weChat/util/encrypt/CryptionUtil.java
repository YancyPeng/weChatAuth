package com.cn.controller.weChat.util.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

public class CryptionUtil {
    private final static String encoding = "utf-8";
    private final static String key = "cnmobile";
    private final static String secretKey = "cmcc_cnmobile_asiainfo_ocs";
    //private static final Logger log =LoggerFactory.getActionLog(DesUtil.class);
//    private static final Logger log = LoggerFactory.getActionLog(CryptionUtil.class);
    /**、
     * 加密
     *
     * @param plainText
     * @return
     */
    public static String encode(String plainText) {

        Key deskey = null;
        byte[] encryptData = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            encryptData = cipher.doFinal(plainText.getBytes(encoding));
            BASE64Encoder base64Encoder = new BASE64Encoder();
            return str2Hex(base64Encoder.encode(encryptData));
        } catch (Exception e) {
//            log.info("加密异常===="+e);
        }
        return "";
    }

    /**
     * 解密
     *
     * @param data
     * @return
     */
    public static String decode(String data) {

        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            Key deskey = keyfactory.generateSecret(spec);
            Cipher deCipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(key.getBytes());
            deCipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] pasByte  = deCipher.doFinal(base64Decoder.decodeBuffer(hex2Str(data)));
            return new String(pasByte, "UTF-8");
        } catch (Exception e) {
//            log.info("解密异常===="+e);
        }
        return "";
    }

    public static String hex2Str(String theHex) {

        char[] chars = theHex.toCharArray();
        int len = chars.length / 2;
        byte[] theByte = new byte[len];
        for (int i = 0; i < len; i++) {
            theByte[i] = Integer.decode("0X" + chars[i * 2] + chars[i * 2 + 1]).byteValue();
        }
        return new String(theByte);
    }

    public static String str2Hex(String theStr) {

        int tmp;
        String tmpStr;
        byte[] bytes = theStr.getBytes();
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            tmp = bytes[i];
            if (tmp < 0) {
                tmp += 256;
            }
            tmpStr = Integer.toHexString(tmp);
            if (tmpStr.length() == 1) {
                result.append('0');
            }
            result.append(tmpStr);
        }
        return result.toString();
    }
	   /* public static void main(String[] args){
            String pwd = "admin123";
			String decodePwd = encode(pwd);
			System.out.println("==decodePwd=="+decodePwd);
		}*/
}

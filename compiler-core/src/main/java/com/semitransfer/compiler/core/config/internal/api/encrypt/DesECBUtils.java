package com.semitransfer.compiler.core.config.internal.api.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/*** 
  * DES ECB PKCS5Padding 对称加密 解密 
 *
 * @program: semitransfer
 * @author: Mr.Yang
 * @date: 2018-07-07 10:31
 * @version:2.0
 **/
public class DesECBUtils {
    /**
     *       加密数据 
     *
     * @param encryptString   需要加密明文
     * @param encryptKey    加密key
     * @return  返回密文
     * @throws Exception  
     * @author Mr.Yang
     * @date 2018/12/6 0006
     */
    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(encryptKey), "DES"));
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     *      key  不足8位补位
     *
     * @param keyRule key  信息
     * @author Mr.Yang
     * @date 2018/12/6 0006
     */
    private static byte[] getKey(String keyRule) {
        Key key;
        byte[] keyByte = keyRule.getBytes();
        // 创建一个空的八位数组,默认情况下为0  
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组  
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        key = new SecretKeySpec(byteTemp, "DES");
        return key.getEncoded();
    }

    /**
     *  * 解密数据 
     *
     * @param decryptString  密文
     * @param decryptKey     密钥
     * @return 返回明文  
     * @throws Exception  
     * @author Mr.Yang
     * @date 2018/12/6 0006
     */
    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        byte[] sourceBytes = Base64.decodeBase64(decryptString);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(decryptKey), "DES"));
        byte[] decoded = cipher.doFinal(sourceBytes);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}
package com.semitransfer.compiler.core.config.internal.api.encrypt;
 
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.semitransfer.compiler.core.config.internal.api.Constants.AES_CBC_PKCS5;


/**
 * <p>
 * AES加密
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
@Component
@Data
@ConfigurationProperties(prefix = AesUtils.AES_PREFIX)
public class AesUtils {

    private static Logger logger = LoggerFactory.getLogger(AesUtils.class);

    public static final String AES_PREFIX = "aes";

    /**
     * 密钥
     */
    private String key;
    /**
     * iv
     */
    private String iv;

    /**
     * 静态密钥
     */
    private static String STATIC_KEYS;
    private static String STATIC_IV;

    /**
     * 初始化
     *
     * @author Mr.Yang
     * @date 2018/7/7
     */
    @PostConstruct
    public void init() {
        STATIC_KEYS = key;
        STATIC_IV = iv;
    }


    /**
     * aes加密
     *
     * @param content 内容
     * @return byte[] byte数组
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static byte[] aesEncrypt(String content) {
        try {
            SecretKeySpec key = new SecretKeySpec(STATIC_KEYS.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(STATIC_IV.getBytes()));
            return cipher.doFinal(content.getBytes());
        } catch (Exception e) {
            logger.error("exception:" + e.toString());
            return null;
        }
    }

    /**
     * aes解密
     *
     * @param content 内容
     * @return byte[] byte数组
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static byte[] aesDecrypt(String content) {
        try {
            SecretKeySpec key = new SecretKeySpec(STATIC_KEYS.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(STATIC_IV.getBytes()));
            return cipher.doFinal(decryptBASE64(content));
        } catch (Exception e) {
            logger.error("exception:" + e.toString());
            return null;
        }
    }

    /**
     * 字符串装换成base64
     *
     * @param key 秘钥转换为byte
     * @return byte[] byte数组
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key.getBytes());
    }

    /**
     * 二进制装换成base64
     *
     * @param key 秘钥转换为byte
     * @return byte[] byte数组
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String encryptBASE64(byte[] key) {
        return new String(Base64.encodeBase64(key));
    }

    /**
     * byte转普通字符串
     *
     * @param bytes 数组
     * @return 返回字符串
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String byteToStr(byte[] bytes) {
        return new String(bytes);
    }
}

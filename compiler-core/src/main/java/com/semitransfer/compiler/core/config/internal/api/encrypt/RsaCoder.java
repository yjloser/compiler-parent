package com.semitransfer.compiler.core.config.internal.api.encrypt;

import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 生成公钥私钥
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class RsaCoder {

    /**
     * 密码算法
     */
    public static final String KEY_ALGORITHM = "RSA";
    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 获得公钥
     *
     * @param keyMap 公私密钥map
     * @return 返回公钥
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        // 获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        // 编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 获得私钥
     *
     * @param keyMap 公私密钥map
     * @return 返回私钥
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        // 获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        // 编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 编码返回字符串
     *
     * @param key byte类型
     * @return 返回字符串
     * @author Mr.Yang
     * @date 2018/7/7
     */
    @SuppressWarnings("restriction")
    public static String encryptBASE64(byte[] key) {
        return Base64.encodeBase64String(key);
    }

    /**
     * map对象中存放公私钥
     *
     * @return 公私密钥map
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static Map<String, Object> initKey() throws Exception {
        // 获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        // 通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }
}

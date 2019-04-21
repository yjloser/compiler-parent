package com.semitransfer.compiler.core.config.internal.api.encrypt;


import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

import static com.semitransfer.compiler.core.config.internal.api.Constants.NUM_ZERO;


/**
 * <p>
 *rsa工具
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
@Component
@ConfigurationProperties(prefix = RsaUtils.RSA_PREFIX)
@Data
public class RsaUtils {

    private static Logger logger = LoggerFactory.getLogger(RsaUtils.class);

    public static final String RSA_PREFIX = "rsa";
    /**
     * 公钥
     */
    private String publick;
    /**
     * 私钥
     */
    private String privatek;

    /**
     * 公 密钥的静态
     */
    private static String PUBLICKEY;
    /**
     * 私密钥的静态
     */
    private static String PRIVATEKEY;

    /**
     * 初始化静态变量
     */
    @PostConstruct
    public void init() {
        PUBLICKEY = publick;
        PRIVATEKEY = privatek;
    }

    /**
     * 指定加密算法为DESede MD5withRSA///RSA/ECB/PKCS1Padding
     */
    private static String ALGORITHM = "RSA/ECB/PKCS1Padding";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    /**
     * 公钥模量
     */
    public static String publicModulus = null;
    /**
     * 公钥指数
     */
    public static String publicExponent = null;
    /**
     * 私钥模量
     */
    public static String privateModulus = null;
    /**
     * 私钥指数
     */
    public static String privateExponent = null;
    private static KeyFactory keyFactory = null;

    static {
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * 生成密钥对字符串
     *
     * @param keySize key数量
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/7/7
     */
    @SuppressWarnings("unused")
    private void generateKeyPairString(int keySize) throws Exception {
        // RSA算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        // 利用上面的随机数据源初始化这个KeyPairGenerator对象
        kpg.initialize(keySize, sr);
        // 生成密匙对
        KeyPair kp = kpg.generateKeyPair();
        // 得到公钥
        Key publicKey = kp.getPublic();
        // 得到私钥
        Key privateKey = kp.getPrivate();
        // 用字符串将生成的密钥写入文件
        String algorithm = publicKey.getAlgorithm();
        // 获取算法
        KeyFactory keyFact = KeyFactory.getInstance(algorithm);
        BigInteger prime;
        BigInteger exponent;
        RSAPublicKeySpec keySpec = keyFact.getKeySpec(publicKey, RSAPublicKeySpec.class);
        prime = keySpec.getModulus();
        exponent = keySpec.getPublicExponent();
        RsaUtils.publicModulus = HexUtils.bytes2Hex(prime.toByteArray());
        RsaUtils.publicExponent = HexUtils.bytes2Hex(exponent.toByteArray());
        RSAPrivateCrtKeySpec privateKeySpec = keyFact.getKeySpec(privateKey, RSAPrivateCrtKeySpec.class);
        BigInteger privateModulus = privateKeySpec.getModulus();
        BigInteger privateExponent = privateKeySpec.getPrivateExponent();
        RsaUtils.privateModulus = HexUtils.bytes2Hex(privateModulus.toByteArray());
        RsaUtils.privateExponent = HexUtils.bytes2Hex(privateExponent.toByteArray());
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象。
     *
     * @param hexModulus        系数。
     * @param hexPublicExponent 专用指数。
     * @return RSA专用公钥对象。
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static RSAPublicKey getRSAPublicKey(String hexModulus, String hexPublicExponent) {
        if (isBlank(hexModulus) || isBlank(hexPublicExponent)) {
            logger.error("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
            return null;
        }
        byte[] modulus = null;
        byte[] publicExponent = null;
        try {
            modulus = HexUtils.hex2Bytes(hexModulus);
            publicExponent = HexUtils.hex2Bytes(hexPublicExponent);
        } catch (Exception ex) {
            logger.error("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
            ex.printStackTrace();
        }
        if (modulus != null && publicExponent != null) {
            return generateRSAPublicKey(modulus, publicExponent);
        }
        return null;
    }

    /**
     * 根据给定的系数和专用指数构造一个RSA专用的公钥对象。
     *
     * @param modulus        系数。
     * @param publicExponent 专用指数。
     * @return RSA专用公钥对象。
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException ex) {
            logger.error("RSAPublicKeySpec is unavailable.");
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            logger.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的私钥对象。
     *
     * @param hexModulus         系数。
     * @param hexPrivateExponent 专用指数。
     * @return RSA专用私钥对象。
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
        if (isBlank(hexModulus) || isBlank(hexPrivateExponent)) {
            logger.error("hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
            return null;
        }
        byte[] modulus = null;
        byte[] privateExponent = null;
        try {
            modulus = HexUtils.hex2Bytes(hexModulus);
            privateExponent = HexUtils.hex2Bytes(hexPrivateExponent);
        } catch (Exception ex) {
            logger.error("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
            ex.printStackTrace();
        }
        if (modulus != null && privateExponent != null) {
            return generateRSAPrivateKey(modulus, privateExponent);
        }
        return null;
    }

    /**
     * 根据给定的系数和专用指数构造一个RSA专用的私钥对象。
     *
     * @param modulus         系数。
     * @param privateExponent 专用指数。
     * @return RSA专用私钥对象。
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),
                new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException ex) {
            logger.error("RSAPrivateKeySpec is unavailable.");
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            logger.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 使用给定的公钥加密给定的字符串。
     *
     * @param key       给定的公钥。
     * @param plaintext 字符串。
     * @return 给定字符串的密文。
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String encryptString(Key key, String plaintext) {
        if (key == null || plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        try {
            byte[] enData = encrypt(key, data);
            return Base64.encodeBase64String(enData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 使用指定的公钥加密数据。
     *
     * @param key  给定的公钥。
     * @param data 要加密的数据。
     * @return 加密后的数据。
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static byte[] encrypt(Key key, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHM);
        ci.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = NUM_ZERO;
        byte[] cache;
        int i = NUM_ZERO;
        // 对数据分段加密
        while (inputLen - offSet > NUM_ZERO) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = ci.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = ci.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, NUM_ZERO, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 使用给定的公钥解密给定的字符串。
     *
     * @param key         给定的公钥
     * @param encrypttext 密文
     * @return 原文字符串。
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String decryptString(Key key, String encrypttext) {
        if (key == null || isBlank(encrypttext)) {
            return null;
        }
        try {
            byte[] enData = Base64.decodeBase64(encrypttext);
            byte[] data = decrypt(key, enData);
            return new String(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("{} Decryption failed. Cause: {}", encrypttext, ex.getCause().getMessage());
        }
        return null;
    }

    /**
     * 使用指定的公钥解密数据。
     *
     * @param key  指定的公钥
     * @param data 要解密的数据
     * @return 原数据
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static byte[] decrypt(Key key, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHM);
        ci.init(Cipher.DECRYPT_MODE, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = NUM_ZERO;
        byte[] cache;
        int i = NUM_ZERO;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = ci.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = ci.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, NUM_ZERO, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 判断非空字符串
     *
     * @param cs 待判断的CharSequence序列
     * @return 是否非空
     * @author Mr.Yang
     * @date 2018/7/7
     */
    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == NUM_ZERO) {
            return true;
        }
        for (int i = NUM_ZERO; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取公钥包装类
     *
     * @return PublicKey 公钥包装类
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/7/7
     */
    @SuppressWarnings("restriction")
    public static PublicKey getPublicKey() throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(PUBLICKEY);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥包装类
     *
     * @return PublicKey 私钥包装类
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(PRIVATEKEY);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


    /**
     * 根据公钥字符串加载公钥
     *
     * @param publicKeyStr 公钥字符串
     * @return 返回公钥对象
     * @throws Exception
     */
    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = javax.xml.bind.DatatypeConverter.parseBase64Binary(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法", e);
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法", e);
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空", e);
        }
    }

    /**
     * 根据私钥字符串加载私钥
     *
     * @param privateKeyStr 私钥字符串
     * @return 返回私钥对象
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = javax.xml.bind.DatatypeConverter.parseBase64Binary(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法", e);
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法", e);
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空", e);
        }
    }
}
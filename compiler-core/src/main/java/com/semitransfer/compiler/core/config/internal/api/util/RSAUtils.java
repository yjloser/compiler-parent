package com.semitransfer.compiler.core.config.internal.api.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author xms
 * @description RSA加密
 */
@Component
public class RSAUtils {

    private static final String CODE = "RSA";

    private static final String IOS_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGOJ8XSE9vjpoRMY8ekbTNbK5ISKDRvnFA/6JLoqAFA2EhcoSMx5R/X1jsRs1O1TxD5FspCS1Qh3swNB4A3cJwLcmwlsGmcK+GID30lXbkm1insPJD/vfe2C9hUsdaXlrhwAG8hhH2CEKS0lzc11JWlKn9OQ4s9u8tITTVtZhO/wIDAQAB";

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY_PATH = "/config/rsa_public.key";

    /**
     * 私钥
     */
    private static final String PRIVATE_KEY_PATH = "/config/rsa_private.key";

    /**
     * 私钥Key实例
     */
    private static Key privateKey = null;

    /**
     * 公钥Key实例
     */
    private static Key publicKey = null;

    /**
     * 默认编码
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    @PostConstruct
    private void init() {
        try {
            // 客户端调用时初始化
            clientKeyInit();

            // 服务端调用时初始化改方法
            serverKeyInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @return
     */
    public static String encode(String data) {
        return encode(data, DEFAULT_CHARSET);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @return
     */
    public static String decode(String data) {
        return decode(data, DEFAULT_CHARSET);
    }

    /**
     * 加密
     *
     * @param data    待加密数据
     * @param charset 编码
     * @return
     */
    public static String encode(String data, String charset) {
        byte[] datas;
        try {
            datas = data.getBytes(charset);
            byte[] res = encryptByPublicKey(datas);
            return byteArr2HexStr(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param data    待解密数据
     * @param charset 编码
     * @return
     */
    public static String decode(String data, String charset) {
        byte[] datas;
        try {
            datas = hexStr2ByteArr(data, charset);
            byte[] res = decryptByPrivateKey(datas);
            return new String(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用方公钥初始化
     *
     * @throws Exception
     * @throws FileNotFoundException
     */
    public static void clientKeyInit() throws Exception {
        // 1.公钥加密初始化
        ObjectInputStream ois = new ObjectInputStream(RSAUtils.class.getResourceAsStream(PUBLIC_KEY_PATH));
        publicKey = (Key) ois.readObject();
    }

    /**
     * 被调用方私钥初始化
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void serverKeyInit() throws Exception {
        // 2.私钥解密初始化
        ObjectInputStream ois = new ObjectInputStream(RSAUtils.class.getResourceAsStream(PRIVATE_KEY_PATH));
        privateKey = (Key) ois.readObject();
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data) {
        // 对数据加密
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CODE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedData = null;
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data) {
        // 对数据解密
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CODE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedData = null;
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[] hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] bytes) {
        int iLen = bytes.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = bytes[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB) 互为可逆的转换过程
     *
     * @param text 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     * @author
     */
    public static byte[] hexStr2ByteArr(String text, String charset) throws Exception {
        byte[] arrB = text.getBytes(charset);
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 生成公私密钥
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genPublicPrivateKey() throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(CODE);
        keyPairGen.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 公钥
        Key publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        Key privateKey = (RSAPrivateKey) keyPair.getPrivate();

        writeToFile(publicKey, PUBLIC_KEY_PATH);
        writeToFile(privateKey, PRIVATE_KEY_PATH);
    }

    /**
     * 将Key写入到文件
     *
     * @param key
     * @param path
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void writeToFile(Key key, String path) throws FileNotFoundException, IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File(path)));
        outputStream.writeObject(key);
        outputStream.flush();
        outputStream.close();
    }
}

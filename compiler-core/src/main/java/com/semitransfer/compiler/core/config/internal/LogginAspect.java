package com.semitransfer.compiler.core.config.internal;

import com.auth0.jwt.algorithms.Algorithm;
import com.semitransfer.compiler.core.config.internal.annotation.LoginManage;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.boot.redis.core.SpringJedisStandAloneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static com.semitransfer.compiler.core.config.internal.api.encrypt.RsaUtils.loadPrivateKey;
import static com.semitransfer.compiler.core.config.internal.api.encrypt.RsaUtils.loadPublicKey;

/**
 * <p>
 * 拦截登录
 * <p>
 *
 * @author : Mr.Yang
 * @since : 2018-12-09 09:47
 **/
@Aspect
@Component
@Order(2)
public class LogginAspect {

    private static Logger logger = LoggerFactory.getLogger(LogginAspect.class);

    /**
     * 字符串缓存
     */
    @Autowired
    private SpringJedisStandAloneService redisTempalte;

    /**
     * 公钥
     */
    private RSAPublicKey publicKey;
    /**
     * 私钥
     */
    private RSAPrivateKey privateKey;

    /**
     * 环境
     */
    @Autowired
    private Environment environment;

    /**
     * 初始化加密
     */
    public static Algorithm algorithm;

    {
        try {
            //私钥
            privateKey = loadPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALY49wZPNXST0ZEi\n" +
                    "G60/FrjDD8do8OJGIjSPSIKvhX5AbW6uz8mL0wYrqOm4L2jsgmlMaGQ3uPyC7RxN\n" +
                    "qui3CGktSSFOoklFp7qFdHOUH3dbBPRoIV2H4ALCJHT22JD1ZZJVf4v1o/goF5TP\n" +
                    "yaNTE7VieJFTBgxxOEWXqc7f6fwlAgMBAAECgYEArAP2XQRk59mqmSDjk5Xcsymg\n" +
                    "OZP84P1nyMoBnyxmDhpvs25eRFo0KL0KRSdTye6J5TD10rUvcV9+yZsf3XL7AgQY\n" +
                    "WpAuR9TYYxzIl0hIOiTLeBp/qHiDWwc1CaYPbnPgdHNHUHLaWCA+h/OOrtsUP40C\n" +
                    "hh6SJHg7hs2XOOkvUnkCQQDxxJxlFrJsxpiNKIeOGz6SM32e25eevY0VI9CTMIjk\n" +
                    "SaMEquFXQilVgr9CuGkRLEz1pYNzY5qONab6/AB8y4Z/AkEAwPMEV9oYSWPcG5Yh\n" +
                    "1IMEEhhYBDCmGwGW6OD1ULVP8kLKACZCnTY9PwS5Z3eO2M+z9nhBCgw3KtZUp0v/\n" +
                    "ugpTWwJANg8eYUQn9UaaycVsOgxBe3Nj/WdgibAcocN2WdMaaOFGQD7tUBONJn+r\n" +
                    "wIF3jM15D9xIfj6hSncYtTov6beghQJAUSYj/nrYWg1opiWHRuRvUtjwM5ruUU9i\n" +
                    "08DBC9elrwMOB/APdiU4rwdinrR23JLGYnODDyHCFf8cjVv2Sp1LHwJAM1CLXPRn\n" +
                    "MTiEEadFXAg6SB7D0jNdCre3g42bSR0DrSPHvLNWd+i7NXUrLvO/hy9iVwNYQwJh\n" +
                    "JXiLcRLndttb8g==");
            //公钥
            publicKey = loadPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2OPcGTzV0k9GRIhutPxa4ww/H\n" +
                    "aPDiRiI0j0iCr4V+QG1urs/Ji9MGK6jpuC9o7IJpTGhkN7j8gu0cTarotwhpLUkh\n" +
                    "TqJJRae6hXRzlB93WwT0aCFdh+ACwiR09tiQ9WWSVX+L9aP4KBeUz8mjUxO1YniR\n" +
                    "UwYMcThFl6nO3+n8JQIDAQAB");
            algorithm = Algorithm.RSA256(publicKey, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前置登录处理
     *
     * @param loginManag 拦截注解
     * @author Mr.Yang
     * @date 2018/12/10
     */
    @Before("@annotation(loginManag)")
    public void beforeLogin(LoginManage loginManag) {

    }
}
package com.semitransfer.compiler.core.config.internal.api;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Serializable;

import static com.semitransfer.compiler.core.config.internal.api.encrypt.AesUtils.aesEncrypt;
import static com.semitransfer.compiler.core.config.internal.api.encrypt.RsaCoder.encryptBASE64;

/**
 * <p>
 *API基础响应信息。
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
@Data
public abstract class AbstractResponse implements Serializable {


    private static final long serialVersionUID = -1491122687805350590L;

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(AbstractResponse.class);

    public AbstractResponse() {

    }

    public AbstractResponse(String code, String msg) {
        this(code, msg, null);
    }

    public AbstractResponse(String code, String msg, String body) {
        this(code, msg, body, null, null);
    }

    public AbstractResponse(String code, String msg, String body, String subCode, String subMsg) {
        this.code = code;
        this.msg = msg;
        this.subCode = subCode;
        this.subMsg = subMsg;
        this.body = body;
    }

    /**
     * 网关返回码
     */
    private String code;
    /**
     * 网关返回码描述
     */
    private String msg;
    /**
     * 业务返回码
     */
    private String subCode;
    /**
     * 业务返回码描述
     */
    private String subMsg;
    /**
     * 网页html或json
     */
    private String body;


    /**
     * 无加密输出
     *
     * @param result   结果信息
     * @param response 响应
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static void write(Object result, HttpServletResponse response) {
        write(JSONObject.parseObject(JSONObject.toJSONString(result)), response);
    }

    /**
     * 无加密输出
     *
     * @param result   结果信息
     * @param response 响应
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static void write(JSONObject result, HttpServletResponse response) {
        write(result.toJSONString(), response);
    }

    /**
     * 无加密输出
     *
     * @param result   结果信息
     * @param response 响应
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static void write(String result, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.write(result);
            pw.flush();
        } catch (Exception e) {
            logger.error("响应请求方失败", e);
        }
    }

    /**
     * 普通加密
     *
     * @param outcome 返回信息
     * @return 返回加密后的字符串
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String jsonEncrypt(JSONObject outcome) {
        // 使用AES加密
        byte[] encrypted = aesEncrypt(outcome.toJSONString());
        // 转换返回类型
        String result = "";
        try {
            result =  encryptBASE64(encrypted);
        } catch (Exception e) {
            logger.error("对数据进行加密错误,请检查参数是否配置正确.", e);
        }
        return result;
    }


}

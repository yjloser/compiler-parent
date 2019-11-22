package com.semitransfer.compiler.core.config.internal.response;

import com.alibaba.fastjson.JSONObject;
import com.semitransfer.compiler.core.config.internal.api.AbstractResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

import static com.semitransfer.compiler.core.config.internal.api.Constants.*;
import static com.semitransfer.compiler.core.config.internal.api.encrypt.AnalyzeUtils.getCodeValue;
import static com.semitransfer.compiler.core.config.internal.api.util.RSAUtils.encode;


/**
 * <p>
 * 移动端响应
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class MobileEndResponse extends AbstractResponse {

    private static final long serialVersionUID = 6635153930520851477L;

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(MobileEndResponse.class);

    /**
     * 默认返回成功
     *
     * @return JSONObject json类型
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static JSONObject responseMessage() {
        return responseMessage(NUM_ZERO);
    }

    /**
     * 返回平台全局码
     *
     * @param code 返回码
     * @return JSONObject json类型
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static JSONObject responseMessage(Integer code) {
        return responseMessage(code, false);
    }


    /**
     * 返回平台全局码
     *
     * @param code   返回码
     * @param change 是否使用默认成功返回码0
     * @return JSONObject json类型
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static JSONObject responseMessage(Integer code, boolean change) {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, code);
        // 如果为true，则需要替换一下返回码
        if (change) {
            result.put(FIELD_CODE, NUM_ZERO);
        }
        //获取返回码对应的信息
        result.put(FIELD_MSG, getCodeValue(String.valueOf(code)));
        return result;
    }

    /**
     * 默认成功
     *
     * @param response 响应对象
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static void responseMessage(HttpServletResponse response) {
        responseMessage(null, response);
    }

    /**
     * 无加密输出 业务成功
     *
     * @param body     业务提示
     * @param response 响应对象
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static void responseMessage(String body, HttpServletResponse response) {
        responseMessage(body, false, response);
    }

    /**
     * 加密输出 业务成功
     *
     * @param body      业务提示
     * @param isEncrypt 是否加密
     * @param response  响应对象
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static void responseMessage(String body, boolean isEncrypt, HttpServletResponse response) {
        //是否加密输出
        if (isEncrypt) {
            // 转换返回类型
            try {
                body = encode(body);
            } catch (Exception e) {
                logger.error("对返回数据进行加密错误,请检查参数是否配置正确.", e);
                //失败直接写出
                write(new MobileEndResponse("40003", "加密异常", null), response);
                return;
            }
        }
        //直接写出
        write(new MobileEndResponse(STR_ZERO, getCodeValue(STR_ZERO), body), response);
    }

    /**
     * 业务出现错误返回方法
     *
     * @param code     业务码
     * @param response 响应对象
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static void responseMessage(Object code, HttpServletResponse response) {
        responseMessage(String.valueOf(code), getCodeValue(String.valueOf(code)), response);
    }


    /**
     * 业务出现错误返回方法
     *
     * @param response 响应对象
     * @author Mr.Yang
     * @date 2018/12/2
     */

    public static void responseMessage(String code, String msg, HttpServletResponse response) {
        //获取信息
        JSONObject outcome = new JSONObject();
        outcome.put(FIELD_CODE, code);
        //获取返回码对应的信息
        outcome.put(FIELD_MSG, msg);
        //直接响应
        write(outcome, response);
    }


    public MobileEndResponse() {
    }

    public MobileEndResponse(String code, String msg) {
        this(code, msg, null);
    }

    public MobileEndResponse(String code, String msg, String body) {
        this(code, msg, body, null, null);
    }

    public MobileEndResponse(String code, String msg, String body, String subCode, String subMsg) {
        super(code, msg, body, subCode, subMsg);
    }

}

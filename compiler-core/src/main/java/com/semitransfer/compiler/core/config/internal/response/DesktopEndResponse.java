package com.semitransfer.compiler.core.config.internal.response;

import com.alibaba.fastjson.JSONObject;
import com.semitransfer.compiler.core.config.internal.api.AbstractResponse;

import javax.servlet.http.HttpServletResponse;

import static com.semitransfer.compiler.core.config.internal.api.Constants.*;
import static com.semitransfer.compiler.core.config.internal.api.encrypt.AnalyzeUtils.getCodeValue;

/**
 * <p>
 * 桌面端响应
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class DesktopEndResponse extends AbstractResponse {

    private static final long serialVersionUID = 3700838367361208237L;


    /**
     * 新增失败
     *
     * @return JSONObject json类型
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static JSONObject responseMessageSaveFail() {
        return responseMessage(40011, "新增失败");
    }

    /**
     * 更新失败
     *
     * @return JSONObject json类型
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static JSONObject responseMessageUpdateFail() {
        return responseMessage(40012, "更新失败");
    }

    /**
     * 删除失败
     *
     * @return JSONObject json类型
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static JSONObject responseMessageRemoveFail() {
        return responseMessage(40013, "删除失败");
    }


    /**
     * 组装信息返回
     *
     * @param code 返回码
     * @param msg  返回信息
     * @return JSONObject json类型
     * @author Mr.Yang
     * @date 2018/12/2
     */
    private static JSONObject responseMessage(Integer code, String msg) {
        JSONObject result = new JSONObject();
        //结果信息
        result.put(FIELD_CODE, code);
        //获取返回码对应的信息
        result.put(FIELD_MSG, msg);
        return result;
    }


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
        responseMessage(NUM_ZERO, response);
    }

    /**
     * 直接返回平台信息，适用于增删改操作
     *
     * @param code     返回码
     * @param response 响应对象
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static void responseMessage(Integer code, HttpServletResponse response) {
        //获取信息
        JSONObject outcome = new JSONObject();
        outcome.put(FIELD_CODE, code);
        //获取返回码对应的信息
        outcome.put(FIELD_MSG, getCodeValue(String.valueOf(code)));
        //直接响应
        write(outcome, response);
    }


    /**
     * 返回响应信息，适用于复杂返回信息
     *
     * @param outcome  响应信息
     * @param response 响应对象
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static void responseMessage(JSONObject outcome, HttpServletResponse response) {
        //响应前台
        write(outcome, response);
    }
}

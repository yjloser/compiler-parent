package com.semitransfer.compiler.core.config.internal.request;

import com.alibaba.fastjson.JSONObject;
import com.semitransfer.compiler.core.config.internal.api.AbstractRequest;
import com.semitransfer.compiler.core.config.internal.response.DesktopEndResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.semitransfer.compiler.core.config.internal.api.Constants.*;
import static com.semitransfer.compiler.core.config.internal.api.util.StringUtils.*;

/**
 * <p>
 * 桌面端请求
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class DesktopEndRequest extends AbstractRequest<DesktopEndResponse> {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(DesktopEndRequest.class);

    /**
     * 从request中获取请求信息
     *
     * @param request 解析信息
     * @return 返回处理的信息
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static JSONObject requestMessage(HttpServletRequest request) {
        return requestMessage(request, null);
    }


    /**
     * 从request中获取请求信息
     *
     * @param request 解析信息
     * @param fields  必填字段
     * @return 返回处理的信息
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static JSONObject requestMessage(HttpServletRequest request, String... fields) {
        //获取requestParameter信息
        String requestParameter = request.getParameter(FIELD_PARAMS);
        //获取requestAttribute信息
        String requestAttribute = String.valueOf(request.getAttribute(FIELD_PARAMS));
        //从请求中获取数据
        if (notEmptyEnhance(requestParameter)
                || notEmptyEnhance(requestAttribute)) {
            //获取参数
            String requestParams = notEmptyEnhance(requestParameter) ? requestParameter : requestAttribute;
            try {
                JSONObject params = JSONObject.parseObject(requestParams);
                //转换json格式
//                JSONObject params = new JSONObject();
//                temp.forEach((k, v) -> {
//                    if (notEmptyEnhance(v)) {
//                        params.put(k, v);
//                    }
//                });
                //获取operator
                //压入用户公司、操作ip、操作时间
                params.put(COMPANY_ID, notEmptyEnhance(request.getAttribute(COMPANY_ID)) ? request.getAttribute(COMPANY_ID) : null);
                params.put(COMPANY_IDS, notEmptyEnhance(request.getAttribute(COMPANY_IDS)) ? request.getAttribute(COMPANY_IDS) : null);
                params.put(PROJECT_IDS, notEmptyEnhance(request.getAttribute(PROJECT_IDS)) ? request.getAttribute(PROJECT_IDS) : null);
                params.put(MANAGE_FLAG, notEmptyEnhance(request.getAttribute(MANAGE_FLAG)) ? request.getAttribute(MANAGE_FLAG) : null);
                params.put(OPERATOR, notEmptyEnhance(request.getAttribute(FIELD_OPERATOR_NAME)) ? request.getAttribute(FIELD_OPERATOR_NAME) : null);
                params.put(OPERATOR_IP, getIpAddr(request));
                params.put(OPERATOR_TIME, LocalDateTime.now());
                //获取头部loginkey
                params.put(FIELD_LOGIN_KEY,
                        isEmptyEnhance(request.getHeader(FIELD_X_TOKEN)) ? null : request.getHeader(FIELD_X_TOKEN));
                if (fields != null) {
                    // 校验请求参数
                    for (String key : fields) {
                        // 判断是否存在该字段、字段是否为空
                        if (!params.containsKey(key) || isEmpty(params.getString(key))) {
                            // 请求参数缺失
                            params.put(FIELD_CODE, 40001);
                            params.put(FIELD_MSG, "请求缺少必选参数");
                            params.put(FIELD_CHECK_STATUS, false);
                            return params;
                        }
                    }
                }
                //处理分页页面
                checkPage(params);
                return params;
            } catch (Exception e) {
                logger.error("请求信息转换为JSON格式报错", e);
                e.printStackTrace();
                return null;
            }
        }
        //转换json格式
        JSONObject params = new JSONObject();
        //处理分页页面
        checkPage(params);
        //获取头部loginkey
        params.put(FIELD_LOGIN_KEY,
                isEmptyEnhance(request.getHeader(FIELD_X_TOKEN)) ? null : request.getHeader(FIELD_X_TOKEN));
        params.put(COMPANY_ID, notEmptyEnhance(request.getAttribute(COMPANY_ID)) ? request.getAttribute(COMPANY_ID) : null);
        params.put(COMPANY_IDS, notEmptyEnhance(request.getAttribute(COMPANY_IDS)) ? request.getAttribute(COMPANY_IDS) : null);
        params.put(PROJECT_IDS, notEmptyEnhance(request.getAttribute(PROJECT_IDS)) ? request.getAttribute(PROJECT_IDS) : null);
        return params;
    }
}

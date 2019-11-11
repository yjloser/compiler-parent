package com.semitransfer.compiler.core.config.internal.request;

import com.alibaba.fastjson.JSONObject;
import com.semitransfer.compiler.core.config.internal.api.AbstractRequest;
import com.semitransfer.compiler.core.config.internal.response.MobileEndResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.semitransfer.compiler.core.config.internal.api.Constants.*;
import static com.semitransfer.compiler.core.config.internal.api.util.RSAUtils.decode;
import static com.semitransfer.compiler.core.config.internal.api.util.StringUtils.*;


/**
 * <p>
 * 移动端请求
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class MobileEndRequest extends AbstractRequest<MobileEndResponse> {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(MobileEndRequest.class);

    /**
     * 解析请求数据
     *
     * @param request 请求类型
     * @return 返回解析得到的字符串
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static JSONObject requestMessage(HttpServletRequest request) {
        return requestMessage(request, null);
    }


    /**
     * 解析请求数据
     *
     * @param request 请求类型
     * @param fields  字符串数组-->校验请求参数
     * @return 返回解析得到的字符串
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static JSONObject requestMessage(HttpServletRequest request, String... fields) {
        JSONObject params = new JSONObject();
        //设置必要参数 设置返回成功
        params.put(FIELD_CHECK_STATUS, true);
        //获取头部loginkey
        params.put(FIELD_LOGIN_KEY,
                isEmptyEnhance(request.getHeader(FIELD_LOGIN_KEY)) ? null : request.getHeader(FIELD_LOGIN_KEY));
        //压入用户公司、操作ip、操作时间
        params.put(COMPANY_ID, notEmptyEnhance(request.getAttribute(COMPANY_ID)) ? request.getAttribute(COMPANY_ID) : null);
        params.put(OPERATOR, notEmptyEnhance(request.getAttribute(FIELD_CONTACTS_NAME)) ? request.getAttribute(FIELD_CONTACTS_NAME) : null);
        params.put(OPERATOR_IP, getIpAddr(request));
        params.put(OPERATOR_TIME, LocalDateTime.now());
        params.put(COMPANY_IDS, notEmptyEnhance(request.getAttribute(COMPANY_IDS)) ? request.getAttribute(COMPANY_IDS) : null);
        params.put(PROJECT_IDS, notEmptyEnhance(request.getAttribute(PROJECT_IDS)) ? request.getAttribute(PROJECT_IDS) : null);
        //获取requestParameter信息
        String requestParameter = request.getParameter(FIELD_PARAMS);
        //获取requestAttribute信息
        String requestAttribute = String.valueOf(request.getAttribute(FIELD_PARAMS));
        //从请求中获取数据
        if (notEmptyEnhance(requestParameter)
                || notEmptyEnhance(requestAttribute)) {
            try {
                // 将请求数据转为json字符串
                String tempParams = notEmptyEnhance(requestParameter)
                        ? requestParameter : requestAttribute;
                // 解析密钥
                params = JSONObject.parseObject(decode(tempParams));
            } catch (Exception e) {
                e.printStackTrace();
                // 解析失败
                params.put(FIELD_CODE, 40002);
                params.put(FIELD_MSG, "密钥不一致，解密失败");
                params.put(FIELD_CHECK_STATUS, false);
                return params;
            }
            if (fields != null) {
                // 校验请求参数
                for (String key : fields) {
                    // 判断是否存在该字段、字段是否为空
                    if (!params.containsKey(key) || isEmpty(params.getString(key))) {
                        // 请求参数缺失
                        params.put(FIELD_CHECK_STATUS, false);
                        params.put(FIELD_CODE, 40001);
                        params.put(FIELD_MSG, "请求缺少必选参数");
                        return params;
                    }
                }
            }
            // 分页操作
            checkPage(params);
            logger.info("{}|{}|{}|{}", 10000, getIpAddr(request), request.getRequestURI(), params);
            return params;
        }
        //处理分页页面
        checkPage(params);
        return params;
    }
}

package com.semitransfer.compiler.core.config.internal.request;

import com.alibaba.fastjson.JSONObject;
import com.semitransfer.compiler.core.config.internal.api.AbstractRequest;
import com.semitransfer.compiler.core.config.internal.response.MobileEndResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.semitransfer.compiler.core.config.internal.api.Constants.*;
import static com.semitransfer.compiler.core.config.internal.api.encrypt.AesUtils.aesDecrypt;
import static com.semitransfer.compiler.core.config.internal.api.encrypt.AesUtils.byteToStr;
import static com.semitransfer.compiler.core.config.internal.api.encrypt.AnalyzeUtils.STATIC_KEYS;
import static com.semitransfer.compiler.core.config.internal.api.encrypt.RsaUtils.decryptString;
import static com.semitransfer.compiler.core.config.internal.api.encrypt.RsaUtils.getPrivateKey;
import static com.semitransfer.compiler.core.config.internal.api.util.StringUtils.isEmpty;
import static com.semitransfer.compiler.core.config.internal.api.util.StringUtils.notEmptyEnhance;


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
        return requestMessage(request, null, null);
    }


    /**
     * 解析请求数据
     *
     * @param request      请求类型
     * @param fields       字符串数组-->校验请求参数
     * @param databaseType 数据库类型
     * @return 返回解析得到的字符串
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static JSONObject requestMessage(HttpServletRequest request, String databaseType, String... fields) {
        JSONObject params = new JSONObject();
        //获取requestParameter信息
        String requestParameter = request.getParameter(FIELD_PARA);
        //获取requestAttribute信息
        String requestAttribute = String.valueOf(request.getAttribute(FIELD_PARA));
        //从请求中获取数据
        if (notEmptyEnhance(requestParameter)
                || notEmptyEnhance(requestAttribute)) {
            // 将请求数据转为json字符串
            JSONObject requestParams = JSONObject.parseObject(notEmptyEnhance(requestParameter)
                    ? requestParameter : requestAttribute);
            // 解析密钥部分
            try {
                // 获取密钥信息
                String openKey = decryptString(getPrivateKey(), requestParams.getString(FIELD_KEY));
                // 如果不一致直接报错，让外层捕捉一下
                assert openKey != null;
                if (!openKey.equals(STATIC_KEYS)) {
                    throw new RuntimeException("密钥不相等");
                }
            } catch (Exception e) {
                logger.error("私钥解析AES密钥错误，检查密钥与配置的是否一致", e);
                // 解析失败
                params.put(FIELD_CODE, 40002);
                params.put(FIELD_MSG, "密钥不一致，解密失败");
                params.put(FIELD_CHECK_STATUS, false);
                return params;
            }
            // 解析参数部分
            try {
                // 解密
                byte[] decrypted = aesDecrypt(requestParams.getString(FIELD_PARAMS));
                // 获取参数结果
                params = JSONObject.parseObject(byteToStr(decrypted));
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
                // 设置返回成功
                params.put(FIELD_CHECK_STATUS, true);
                //获取operator
                //压入用户公司、操作ip、操作时间
                params.put(COMPANY_ID, notEmptyEnhance(request.getAttribute(COMPANY_ID)) ? request.getAttribute(COMPANY_ID) : null);
                params.put(OPERATOR, notEmptyEnhance(request.getAttribute(FIELD_CONTACTS_NAME)) ? request.getAttribute(FIELD_CONTACTS_NAME) : null);
                params.put(OPERATOR_IP, getIpAddr(request));
                params.put(OPERATOR_TIME, LocalDateTime.now());
                // 判断是否存在分页问题
                if (!isEmpty(databaseType)) {
                    // 分页操作
                    checkPage(params, databaseType);
                }
                logger.info("{}|{}|{}|{}", 10000, getIpAddr(request), request.getRequestURI(), params.toString());
                return params;
            } catch (Exception e) {
                e.printStackTrace();
                // 解析失败
                params.put(FIELD_CODE, 40002);
                params.put(FIELD_MSG, "密钥不一致，解密失败");
                params.put(FIELD_CHECK_STATUS, false);
                return params;
            }
        }
        return null;
    }
}

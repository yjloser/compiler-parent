package com.semitransfer.compiler.core.config.internal.api;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import static com.semitransfer.compiler.core.config.internal.api.Constants.*;
import static com.semitransfer.compiler.plugin.config.internal.ConfigEnum.MYSQL_JDBC;


/**
 * <p>
 * 基础请求类
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public abstract class AbstractRequest<T extends AbstractResponse> {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(AbstractRequest.class);

    /**
     * 获取登录用户IP地址
     *
     * @param request 请求包装类
     * @return 返回用户真正的端口
     * @author Mr.Yang
     * @date 2018/7/4 0004
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (null == ip || ip.length() == NUM_ZERO || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (null == ip || ip.length() == NUM_ZERO || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");

        }
        if (null == ip || ip.length() == NUM_ZERO || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (LOCALHOST.equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 数据库分页操作
     *
     * @param outcome 请求参数
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static void checkPage(JSONObject outcome) {
        checkPage(outcome, outcome.containsKey(FIELD_DATABASE_TYPE) ?
                outcome.getString(FIELD_DATABASE_TYPE) : MYSQL_JDBC.getKey());
    }

    /**
     * 数据库分页操作
     *
     * @param outcome      请求参数
     * @param databaseType 数据库类型
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static void checkPage(JSONObject outcome, String databaseType) {
        // 起始页
        int currentPage;
        // 每条数
        int pageSize;
        try {
            currentPage = outcome.containsKey(FIELD_CURRENT) ? outcome.getIntValue(FIELD_CURRENT) : NUM_ONE;
            pageSize = outcome.containsKey(FIELD_SIZE) ?
                    outcome.getIntValue(FIELD_SIZE) : NUM_THIRTH;
        } catch (Exception e) {
            logger.error("解析数据库分页时,请求参数未解析到分页字段.", e);
            // 解析失败
            outcome.put(FIELD_CHECK_STATUS, false);
            return;
        }
        // 实体类分页
        outcome.put(FIELD_BIGENPAGE, (((currentPage - NUM_ONE) * pageSize)));
        outcome.put(FIELD_SIZE, pageSize);
        // 数据库类型
        if (FIELD_ORACLE.equals(databaseType)) {
            outcome.put(FIELD_ENDPAGE, pageSize * currentPage);
        }
    }


}

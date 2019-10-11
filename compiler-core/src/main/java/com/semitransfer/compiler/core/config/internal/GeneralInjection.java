package com.semitransfer.compiler.core.config.internal;

import com.alibaba.fastjson.JSONArray;
import com.semitransfer.compiler.core.config.internal.annotation.AclManage;
import org.boot.redis.core.SpringJedisStandAloneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.semitransfer.compiler.core.config.internal.api.Constants.FIELD_ADD;
import static com.semitransfer.compiler.plugin.config.internal.ConfigConstants.*;


/**
 * <p>
 * 通用注入
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
@Configuration
public class GeneralInjection {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(GeneralInjection.class);

    @Autowired
    WebApplicationContext applicationContext;

    /**
     * 环境
     */
    @Autowired
    Environment environment;

    /**
     * jedis
     */
    @Autowired
    SpringJedisStandAloneService redisTempalte;

    /**
     * 初始化检索所有requestMapping
     *
     * @author Mr.Yang
     * @date 2018/12/7 0007
     */
    @PostConstruct
    @Order(NUM_EIGHT)
    public void retrievalRequestMapping() throws InterruptedException {
        //新增免过滤路径
        String interceptroList = StringUtils.isEmpty(this.environment.getProperty(FIELD_ACL_INTERCEPTOR)) ?
                ACL_INTERCEPTOR_LIST.substring(NUM_ONE) :
                this.environment.getProperty(FIELD_ACL_INTERCEPTOR).concat(ACL_INTERCEPTOR_LIST);
        //获取使用RequestMapping注解方法
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        //获取所有url链接
        List<Map<String, String>> listUrl = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            boolean flag = true;
            Map<String, String> methodMap = new HashMap<>(NUM_SIX);
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            // 方法名
            String methodName = method.getMethod().getName();
            //过滤不展示处理的方法
            String[] excludeUlr = {"tableToEntity", "error"};
            for (String url : excludeUlr) {
                //如果开头以过滤的，则默认不展示
                if (methodName.startsWith(url)) {
                    flag = false;
                    break;
                }
            }
            //不是过滤数组则可以加入展示
            if (flag) {
                PatternsRequestCondition p = info.getPatternsCondition();
                methodMap.put(FIELD_METHOD, methodName);
                // 一个方法可能对应多个url
                methodMap.put(FIELD_URL, JSONArray.toJSONString(p.getPatterns()));
                // 类名
                methodMap.put(FIELD_CLASS_NAME, method.getMethod().getDeclaringClass().getName());
                //反射获取自定义注解
                AclManage loginManage = method.getMethodAnnotation(AclManage.class);
                //获取名称
                if (loginManage != null) {
                    methodMap.put(FIELD_ACL_NAME, loginManage.value());
                }
                RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
                for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                    methodMap.put(FIELD_TEPY, requestMethod.toString());
                }
                //权限码
                String aclCode = String.valueOf(System.currentTimeMillis());
                TimeUnit.MILLISECONDS.sleep(50);
                //开头1新增 2删除 3更新 4列表/检索 5导出 6导入 7跳转 8详情 9预新增
                switch (methodName) {
                    case FIELD_ADD:
                        aclCode = String.valueOf(NUM_ONE).concat(aclCode);
                        break;
                    case FIELD_REMOVE:
                        aclCode = String.valueOf(NUM_TWO).concat(aclCode);
                        break;
                    case FIELD_UPDATE:
                        aclCode = String.valueOf(NUM_THREE).concat(aclCode);
                        break;
                    case FIELD_LIST:
                        aclCode = String.valueOf(NUM_FOUR).concat(aclCode);
                        break;
                    case FIELD_EXPORT:
                        aclCode = String.valueOf(NUM_FIVE).concat(aclCode);
                        break;
                    case FIELD_IMPORT:
                        aclCode = String.valueOf(NUM_SIX).concat(aclCode);
                        break;
                    case FIELD_JUMP:
                        aclCode = String.valueOf(NUM_SEVEN).concat(aclCode);
                        break;
                    case FIELD_GET:
                        aclCode = String.valueOf(NUM_EIGHT).concat(aclCode);
                        break;
                    case FIELD_PRE_ADDED:
                        aclCode = String.valueOf(NUM_NINE).concat(aclCode);
                        break;
                    default:
                        aclCode = String.valueOf(NUM_ZERO).concat(aclCode);
                        break;
                }
                methodMap.put(FIELD_ACL_CODE, aclCode);
                listUrl.add(methodMap);
            }
        }
        try {
            redisTempalte.set(URL_LIST, JSONArray.toJSONString(listUrl));
            //压入免过滤信息
            this.redisTempalte.set(FIELD_ACL_INTERCEPTOR, interceptroList);
        } catch (Exception e) {
            logger.warn("Found Redis dependency, configuration information does not exist. Continue execution");
        }

    }
}

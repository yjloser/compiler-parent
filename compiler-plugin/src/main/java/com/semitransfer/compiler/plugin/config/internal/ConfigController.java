package com.semitransfer.compiler.plugin.config.internal;

import com.alibaba.fastjson.JSONObject;
import com.sun.management.OperatingSystemMXBean;
import org.boot.redis.core.SpringJedisStandAloneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.semitransfer.compiler.plugin.config.internal.ConfigConstants.CONFIG_INDEX;
import static com.semitransfer.compiler.plugin.config.internal.ConfigConstants.FIELD_SYS_JVM;
import static com.semitransfer.compiler.plugin.config.internal.ConfigEnum.CONVERT_ERROR;
import static com.semitransfer.compiler.plugin.config.internal.ConfigEnum.PROCESS_SUCCESS;
import static com.semitransfer.compiler.plugin.config.internal.util.ConvertHandler.convertTableToEntity;


/**
 * <p>
 * 配置信息控制中心
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
@RestController
@RequestMapping(CONFIG_INDEX)
public class ConfigController {

    @Autowired
    SpringJedisStandAloneService redisTempalte;

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);

    /**
     * 从数据库表转换为实体等（service、entity、controller、mapper）
     *
     * @param convert 请求的参数 json格式
     * @return 返回是否创建成功
     * @author Mr.Yang
     * @date 2018/11/29 0029
     */
    @RequestMapping(ConfigConstants.TABLE_TO_ENTITY)
    public String tableToEntity(@RequestParam(required = false) String convert) {
        //如果传入的字段不为空，则表示不是通过接口传值
        if (!StringUtils.isEmpty(convert)) {
            //处理自动转换工具
            return convertTableToEntity(convert) ? PROCESS_SUCCESS.getKey()
                    : CONVERT_ERROR.getKey();
        }
        return null;
    }


    @PostConstruct
    @Scheduled(fixedRate = 5000)
    public void init() throws UnknownHostException {
        getMemery();
    }


    /**
     * 获取内存使用率
     *
     * @author Mr.Yang
     * @date 2019/4/20
     */
    private void getMemery() throws UnknownHostException {
        OperatingSystemMXBean osmxb = (com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        JSONObject sysInfo = new JSONObject();
        long totalvirtualMemory = osmxb.getTotalSwapSpaceSize();
        sysInfo.put("sysTotal", Math.round(totalvirtualMemory / 1024.0 / 1024.0) + "MB");
        // 物理内存（内存条）
        long physicalMemorySize = osmxb.getTotalPhysicalMemorySize();
        sysInfo.put("sysPhysical", Math.round(physicalMemorySize / 1024.0 / 1024.0) + "MB");
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        //剩余的物理内存
        sysInfo.put("sysFree", Math.round(freePhysicalMemorySize / 1024.0 / 1024.0) + "MB");
        //使用的内存
        sysInfo.put("sysUsed", Math.round((physicalMemorySize - freePhysicalMemorySize) / 1024.0 / 1024.0) + "MB");
        //内存使用率
        Double compare = (1 - freePhysicalMemorySize * 1.0 / physicalMemorySize) * 100;
        sysInfo.put("utilizationRate", compare.intValue() + "%");
        InetAddress addr;
        addr = InetAddress.getLocalHost();
        sysInfo.put("ip", addr.getHostAddress());
        Runtime r = Runtime.getRuntime();
        //jvm使用内存总量
        sysInfo.put("jvmTotal", Math.round(r.totalMemory() / 1024.0 / 1024.0) + "MB");
        //jvm剩余内存总量
        sysInfo.put("jvmFree", Math.round(r.freeMemory() / 1024.0 / 1024.0) + "MB");
        sysInfo.put("processors", r.availableProcessors());
        //插入缓存
        this.redisTempalte.hset(FIELD_SYS_JVM, addr.getHostAddress(), sysInfo.toJSONString());
    }


}

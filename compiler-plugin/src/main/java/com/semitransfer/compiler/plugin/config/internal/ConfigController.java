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
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.semitransfer.compiler.plugin.config.internal.ConfigConstants.CONFIG_INDEX;
import static com.semitransfer.compiler.plugin.config.internal.ConfigConstants.FIELD_SYS_JVM;
import static com.semitransfer.compiler.plugin.config.internal.ConfigEnum.CONVERT_ERROR;
import static com.semitransfer.compiler.plugin.config.internal.ConfigEnum.PROCESS_SUCCESS;
import static com.semitransfer.compiler.plugin.config.internal.util.ConvertHandler.convertTableToEntity;
import static com.semitransfer.compiler.plugin.config.internal.util.JvmInfo.getCpuInfo;
import static com.semitransfer.compiler.plugin.config.internal.util.JvmInfo.getSystemInfo;


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
        // 物理内存（内存条）
        long physicalMemorySize = osmxb.getTotalPhysicalMemorySize();
        sysInfo.put("memoryTotal", Math.round(physicalMemorySize / 1024.0 / 1024.0 / 1024.0) + "GB");
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        //剩余的物理内存
        sysInfo.put("memoryFree", Math.round(freePhysicalMemorySize / 1024.0 / 1024.0 / 1024.0) + "GB");
        //使用的物理内存
        sysInfo.put("memoryUsed", Math.round((physicalMemorySize - freePhysicalMemorySize) / 1024.0 / 1024.0 / 1024.0) + "GB");
        //物理内存使用率
        Double compare = (1 - freePhysicalMemorySize * 1.0 / physicalMemorySize) * 100;
        sysInfo.put("memoryPercentage", compare.intValue() + "%");
        InetAddress addr;
        addr = InetAddress.getLocalHost();
        sysInfo.put("ip", addr.getHostAddress());
        Runtime r = Runtime.getRuntime();
        //jvm使用内存总量
        sysInfo.put("jvmTotal", Math.round(r.totalMemory() / 1024.0 / 1024.0 / 1024.0) + "GB");
        //jvm剩余内存总量
        sysInfo.put("jvmFree", Math.round(r.freeMemory() / 1024.0 / 1024.0 / 1024.0) + "GB");
        sysInfo.put("processors", r.availableProcessors());
        File[] file = File.listRoots();
        long diskTotal = 0L;
        long diskFree = 0L;
        for (File file2 : file) {
            diskTotal = diskTotal + (file2.getTotalSpace() / 1024 / 1024 / 1024);
            diskFree = diskFree + (file2.getFreeSpace() / 1024 / 1024 / 1024);
        }
        //硬盘使用率
        sysInfo.put("diskPercentage", ((1 - diskFree * 1.0 / diskTotal) * 100) + "%");
        java.lang.management.OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();
        if (system.getName().equalsIgnoreCase("Linux")) {
            //cpu使用率
            sysInfo.put("cpuPercentage", (int) getCpuInfo() + "%");
        }
        sysInfo.put("content", getSystemInfo());
        //插入缓存
        this.redisTempalte.hset(FIELD_SYS_JVM, addr.getHostAddress(), sysInfo.toJSONString());
    }
}

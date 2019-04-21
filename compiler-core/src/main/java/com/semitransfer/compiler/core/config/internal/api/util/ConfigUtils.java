package com.semitransfer.compiler.core.config.internal.api.util;

import java.util.Objects;

import static com.semitransfer.compiler.plugin.config.internal.ConfigConstants.*;


/**
 * <p>
 * 配置工具
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class ConfigUtils {


    /**
     * 获取文件路径
     *
     * @return 返回文件路径
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static String getFilePath() {
        //获取项目路径
        String projectPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        //判断操作系统
        if (System.getProperty(OS_NAME).toLowerCase().contains(WINDOWS)) {
            //处理前置路径
            String preposePath = projectPath.substring(1);
            //处理
            projectPath = preposePath.replaceAll("/", "\\\\");
        } else {
            //如果存在file开头，截取掉
            if (projectPath.startsWith(FIELD_FILE)) {
                projectPath = projectPath.substring(5);
            }
        }
        return projectPath;
    }
}

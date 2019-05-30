package com.semitransfer.compiler.plugin.config.internal.util;

import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.management.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class JvmInfo {

    static final long MB = 1024 * 1024;

    /**
     * 获取当前服务器信息
     *
     * @return 返回系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    public static String getSystemInfo() {
        StringBuilder sysBuilder = new StringBuilder();
        //打印系统信息
        sysBuilder.append("==========系统信息=========\r\n");
        printOperatingSystemInfo(sysBuilder);
        //打印编译信息
        sysBuilder.append("\r\n=========编译信息========\r\n");
        printCompilationInfo(sysBuilder);
        //打印类加载信息
        sysBuilder.append("\r\n========类加载信息========\r\n");
        printClassLoadingInfo(sysBuilder);
        //打印运行时信息
        sysBuilder.append("\r\n=========运行时信息=========\r\n");
        printRuntimeInfo(sysBuilder);
        //打印内存管理器信息
        sysBuilder.append("\r\n=======内存管理器信息========\r\n");
        printMemoryManagerInfo(sysBuilder);
        //打印垃圾回收信息
        sysBuilder.append("\r\n========垃圾回收信息========\r\n");
        printGarbageCollectorInfo(sysBuilder);
        //打印vm内存
        sysBuilder.append("\r\n=========vm内存信息=========\r\n");
        printMemoryInfo(sysBuilder);
        //打印vm各内存区信息
        sysBuilder.append("\r\n========vm各内存区信息========\r\n");
        printMemoryPoolInfo(sysBuilder);
        //打印线程信息
        sysBuilder.append("\r\n===========JVM线程==========\r\n");
        printThreadInfo(sysBuilder);
        sysBuilder.append("\r\n===========系统硬盘==========\r\n");
        fileData(sysBuilder);
        return sysBuilder.toString();
    }


    /**
     * 系统信息
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void printOperatingSystemInfo(StringBuilder sysBuilder) {
        OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();
        //相当于System.getProperty("os.name").
        sysBuilder.append("系统名称:").append(system.getName()).append("\r\n");
        //相当于System.getProperty("os.version").
        sysBuilder.append("系统版本:").append(system.getVersion()).append("\r\n");
        //相当于System.getProperty("os.arch").
        sysBuilder.append("操作系统的架构:").append(system.getArch()).append("\r\n");
        //相当于 Runtime.availableProcessors()
        sysBuilder.append("可用的内核数:").append(system.getAvailableProcessors()).append("\r\n");
        if (isSunOsMBean(system)) {
            sysBuilder.append("\r\n==========内存信息=========\r\n");
            long totalPhysicalMemory = getLongFromOperatingSystem(system, "getTotalPhysicalMemorySize");
            long freePhysicalMemory = getLongFromOperatingSystem(system, "getFreePhysicalMemorySize");
            long usedPhysicalMemorySize = totalPhysicalMemory - freePhysicalMemory;
            sysBuilder.append("总物理内存(M):").append(totalPhysicalMemory / MB).append("\r\n");
            sysBuilder.append("已用物理内存(M):").append(usedPhysicalMemorySize / MB).append("\r\n");
            sysBuilder.append("剩余物理内存(M):").append(freePhysicalMemory / MB).append("\r\n");
            long totalSwapSpaceSize = getLongFromOperatingSystem(system, "getTotalSwapSpaceSize");
            long freeSwapSpaceSize = getLongFromOperatingSystem(system, "getFreeSwapSpaceSize");
            long usedSwapSpaceSize = totalSwapSpaceSize - freeSwapSpaceSize;
            sysBuilder.append("总交换空间(M):").append(totalSwapSpaceSize / MB).append("\r\n");
            sysBuilder.append("已用交换空间(M):").append(usedSwapSpaceSize / MB).append("\r\n");
            sysBuilder.append("剩余交换空间(M):").append(freeSwapSpaceSize / MB).append("\r\n");
        }
    }

    /**
     * 处理指定内存信息
     *
     * @param operatingSystem 系统bean
     * @param methodName      模块信息
     * @return 获取内存容量
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static long getLongFromOperatingSystem(OperatingSystemMXBean operatingSystem, String methodName) {
        try {
            final Method method = operatingSystem.getClass().getMethod(methodName,
                    (Class<?>[]) null);
            method.setAccessible(true);
            return (Long) method.invoke(operatingSystem, (Object[]) null);
        } catch (final InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            } else if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new IllegalStateException(e.getCause());
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 编译信息
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void printCompilationInfo(StringBuilder sysBuilder) {
        CompilationMXBean compilation = ManagementFactory.getCompilationMXBean();
        sysBuilder.append("JIT编译器名称：").append(compilation.getName()).append("\r\n");
        //判断jvm是否支持编译时间的监控
        if (compilation.isCompilationTimeMonitoringSupported()) {
            sysBuilder.append("总编译时间：").append(compilation.getTotalCompilationTime()).append("秒").append("\r\n");
        }
    }

    /**
     * jvm加载类
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void printClassLoadingInfo(StringBuilder sysBuilder) {
        ClassLoadingMXBean classLoad = ManagementFactory.getClassLoadingMXBean();
        sysBuilder.append("已加载类总数：").append(classLoad.getTotalLoadedClassCount()).append("\r\n");
        sysBuilder.append("已加载当前类：").append(classLoad.getLoadedClassCount()).append("\r\n");
        sysBuilder.append("已卸载类总数：").append(classLoad.getUnloadedClassCount()).append("\r\n");
    }

    /**
     * jvm信息
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void printRuntimeInfo(StringBuilder sysBuilder) {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        sysBuilder.append("进程PID=").append(runtime.getName().split("@")[0]).append("\r\n");
        sysBuilder.append("jvm规范名称:").append(runtime.getSpecName()).append("\r\n");
        sysBuilder.append("jvm规范运营商:").append(runtime.getSpecVendor()).append("\r\n");
        sysBuilder.append("jvm规范版本:").append(runtime.getSpecVersion()).append("\r\n");
        //返回虚拟机在毫秒内的开始时间。该方法返回了虚拟机启动时的近似时间
        sysBuilder.append("jvm启动时间（毫秒）:").append(runtime.getStartTime()).append("\r\n");
        //相当于System.getProperties
        sysBuilder.append("获取System.properties:").append(runtime.getSystemProperties()).append("\r\n");
        sysBuilder.append("jvm正常运行时间（毫秒）:").append(runtime.getUptime()).append("\r\n");
        //相当于System.getProperty("java.vm.name").
        sysBuilder.append("jvm名称:").append(runtime.getVmName()).append("\r\n");
        //相当于System.getProperty("java.vm.vendor").
        sysBuilder.append("jvm运营商:").append(runtime.getVmVendor()).append("\r\n");
        //相当于System.getProperty("java.vm.version").
        sysBuilder.append("jvm实现版本:").append(runtime.getVmVersion()).append("\r\n");
        List<String> args = runtime.getInputArguments();
        if (args != null && !args.isEmpty()) {
            sysBuilder.append("vm参数:").append("\r\n");
            for (String arg : args) {
                sysBuilder.append(arg).append("\r\n");
            }
        }
        sysBuilder.append("类路径:").append(runtime.getClassPath()).append("\r\n");
        sysBuilder.append("引导类路径:").append(runtime.getBootClassPath()).append("\r\n");
        sysBuilder.append("库路径:").append(runtime.getLibraryPath()).append("\r\n");
    }

    /**
     * vm内存管理器
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void printMemoryManagerInfo(StringBuilder sysBuilder) {
        List<MemoryManagerMXBean> managers = ManagementFactory.getMemoryManagerMXBeans();
        if (!managers.isEmpty()) {
            for (MemoryManagerMXBean manager : managers) {
                sysBuilder.append("vm内存管理器：名称=").append(manager.getName()).append(",管理的内存区=").append(Arrays.deepToString(manager.getMemoryPoolNames())).append(",ObjectName=").append(manager.getObjectName()).append("\r\n");
            }
        }
    }

    /**
     * gc垃圾回收
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void printGarbageCollectorInfo(StringBuilder sysBuilder) {
        List<GarbageCollectorMXBean> garbages = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbage : garbages) {
            sysBuilder.append("垃圾收集器：名称=").append(garbage.getName()).append(",收集=").append(garbage.getCollectionCount()).append(",总花费时间=").append(garbage.getCollectionTime()).append(",内存区名称=").append(Arrays.deepToString(garbage.getMemoryPoolNames())).append("\r\n");
        }
    }

    /**
     * gc垃圾回收
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void printMemoryInfo(StringBuilder sysBuilder) {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        MemoryUsage headMemory = memory.getHeapMemoryUsage();
        sysBuilder.append("head堆:").append("\r\n");
        sysBuilder.append("\t初始(M):").append(headMemory.getInit() / MB).append("\r\n");
        sysBuilder.append("\t最大(上限)(M):").append(headMemory.getMax() / MB).append("\r\n");
        sysBuilder.append("\t当前(已使用)(M):").append(headMemory.getUsed() / MB).append("\r\n");
        sysBuilder.append("\t提交的内存(已申请)(M):").append(headMemory.getCommitted() / MB).append("\r\n");
        sysBuilder.append("\t使用率:").append(headMemory.getUsed() * 100 / headMemory.getCommitted()).append("%").append("\r\n");
        sysBuilder.append("non-head非堆:").append("\r\n");
        MemoryUsage nonheadMemory = memory.getNonHeapMemoryUsage();
        sysBuilder.append("\t初始(M):").append(nonheadMemory.getInit() / MB).append("\r\n");
        sysBuilder.append("\t最大(上限)(M):").append(nonheadMemory.getMax() / MB).append("\r\n");
        sysBuilder.append("\t当前(已使用)(M):").append(nonheadMemory.getUsed() / MB).append("\r\n");
        sysBuilder.append("\t提交的内存(已申请)(M):").append(nonheadMemory.getCommitted() / MB).append("\r\n");
        sysBuilder.append("\t使用率:").append(nonheadMemory.getUsed() * 100 / nonheadMemory.getCommitted()).append("%").append("\r\n");
    }

    /**
     * vm内存区
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void printMemoryPoolInfo(StringBuilder sysBuilder) {
        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        if (!pools.isEmpty()) {
            for (MemoryPoolMXBean pool : pools) {
                //只打印一些各个内存区都有的属性，一些区的特殊属性，可看文档或百度
                //最大值，初始值，如果没有定义的话，返回-1，所以真正使用时，要注意
                sysBuilder.append("vm内存区:\r\n\t名称=").append(pool.getName()).append("\r\n\t所属内存管理者=").append(Arrays.deepToString(pool.getMemoryManagerNames())).append("\r\n\t ObjectName=").append(pool.getObjectName()).append("\r\n\t初始大小(M)=").append(pool.getUsage().getInit() / MB).append("\r\n\t最大(上限)(M)=").append(pool.getUsage().getMax() / MB).append("\r\n\t已用大小(M)=").append(pool.getUsage().getUsed() / MB).append("\r\n\t已提交(已申请)(M)=").append(pool.getUsage().getCommitted() / MB).append("\r\n\t使用率=").append(pool.getUsage().getUsed() * 100 / pool.getUsage().getCommitted()).append("%").append("\r\n");

            }
        }
    }

    /**
     * 线程总数
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void printThreadInfo(StringBuilder sysBuilder) {
        ThreadMXBean thread = ManagementFactory.getThreadMXBean();
        sysBuilder.append("ObjectName=").append(thread.getObjectName()).append("\r\n");
        sysBuilder.append("仍活动的线程总数=").append(thread.getThreadCount()).append("\r\n");
        sysBuilder.append("峰值=").append(thread.getPeakThreadCount()).append("\r\n");
        sysBuilder.append("线程总数（被创建并执行过的线程总数）=").append(thread.getTotalStartedThreadCount()).append("\r\n");
        sysBuilder.append("当初仍活动的守护线程（daemonThread）总数=").append(thread.getDaemonThreadCount()).append("\r\n");
        //检查是否有死锁的线程存在
        long[] deadlockedIds = thread.findDeadlockedThreads();
        if (deadlockedIds != null && deadlockedIds.length > 0) {
            ThreadInfo[] deadlockInfos = thread.getThreadInfo(deadlockedIds);
            sysBuilder.append("死锁线程信息:").append("\r\n");
            sysBuilder.append("\t\t线程名称\t\t状态\t\t").append("\r\n");
            for (ThreadInfo deadlockInfo : deadlockInfos) {
                sysBuilder.append("\t\t").append(deadlockInfo.getThreadName()).append("\t\t").append(deadlockInfo.getThreadState()).append("\t\t").append(deadlockInfo.getBlockedTime()).append("\t\t").append(deadlockInfo.getWaitedTime()).append("\t\t").append(Arrays.toString(deadlockInfo.getStackTrace())).append("\r\n");
            }
        }
        long[] threadIds = thread.getAllThreadIds();
        if (threadIds != null && threadIds.length > 0) {
            ThreadInfo[] threadInfos = thread.getThreadInfo(threadIds);
            sysBuilder.append("所有线程信息:").append("\r\n");
            sysBuilder.append("\t\t线程名称\t\t\t\t状态\t\t\t\t线程id").append("\r\n");
            for (ThreadInfo threadInfo : threadInfos) {
                sysBuilder.append("\t\t").append(threadInfo.getThreadName()).append("\t\t\t\t").append(threadInfo.getThreadState()).append("\t\t\t\t").append(threadInfo.getThreadId()).append("\r\n");
            }
        }
    }

    /**
     * 硬盘信息
     *
     * @param sysBuilder 系统信息
     * @author Mr.Yang
     * @date 2019/5/15 0015
     */
    private static void fileData(StringBuilder sysBuilder) {
        File[] file = File.listRoots();
        for (File file2 : file) {
            String path = "";
            if (!StringUtils.isEmpty(file2.getPath())) {
                path = file2.getPath();
            }
            sysBuilder.append("硬盘总空间：").append(path).append(" ").append(file2.getTotalSpace() / 1024 / 1024 / 1024).append("GB").append("\r\n");
            sysBuilder.append("硬盘已用空间：").append(path).append(" ").append((file2.getTotalSpace() - file2.getFreeSpace()) / 1024 / 1024 / 1024).append("GB").append("\r\n");
            sysBuilder.append("硬盘可用空间：").append(path).append(" ").append(file2.getFreeSpace() / 1024 / 1024 / 1024).append("GB").append("\r\n");
            sysBuilder.append("------------------------------------------------------------------").append("\r\n");
        }
    }

    private static boolean isSunOsMBean(OperatingSystemMXBean operatingSystem) {
        final String className = operatingSystem.getClass().getName();
        return "com.sun.management.OperatingSystem".equals(className)
                || "sun.management.OperatingSystemImpl".equals(className)
                || "com.sun.management.UnixOperatingSystem".equals(className);
    }

    public static float getCpuInfo() {
        File file = new File("/proc/stat");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            StringTokenizer token = new StringTokenizer(br.readLine());
            token.nextToken();
            long user1 = Long.parseLong(token.nextToken());
            long nice1 = Long.parseLong(token.nextToken());
            long sys1 = Long.parseLong(token.nextToken());
            long idle1 = Long.parseLong(token.nextToken());
            Thread.sleep(1000);
            br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));
            token = new StringTokenizer(br.readLine());
            token.nextToken();
            long user2 = Long.parseLong(token.nextToken());
            long nice2 = Long.parseLong(token.nextToken());
            long sys2 = Long.parseLong(token.nextToken());
            long idle2 = Long.parseLong(token.nextToken());
            return (float) ((user2 + sys2 + nice2) - (user1 + sys1 + nice1))
                    / (float) ((user2 + nice2 + sys2 + idle2) - (user1 + nice1
                    + sys1 + idle1));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0f;
    }
}
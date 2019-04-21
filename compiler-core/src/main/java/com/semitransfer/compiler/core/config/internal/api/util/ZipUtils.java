package com.semitransfer.compiler.core.config.internal.api.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * <p>
 * 文件压缩、解压工具类。文件压缩格式为zip
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class ZipUtils {

    private static final int KB = 1024;

    private ZipUtils() {
    }


    /**
     * 批量压缩文件
     *
     * @param resFiles    待压缩文件集合
     * @param zipFilePath 压缩文件路径及文件名
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean zipFiles(final Collection<File> resFiles, final String zipFilePath)
            throws IOException {
        return zipFiles(resFiles, zipFilePath, null);
    }

    /**
     * 批量压缩文件
     *
     * @param resFiles    待压缩文件集合
     * @param zipFilePath 压缩文件路径
     * @param comment     压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean zipFiles(final Collection<File> resFiles, final String zipFilePath, final String comment)
            throws IOException {
        return zipFiles(resFiles, new File(zipFilePath), comment);
    }

    /**
     * 批量压缩文件
     *
     * @param resFiles 待压缩文件集合
     * @param zipFile  压缩文件
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean zipFiles(final Collection<File> resFiles, final File zipFile)
            throws IOException {
        return zipFiles(resFiles, zipFile, null);
    }

    /**
     * 批量压缩文件
     *
     * @param resFiles 待压缩文件集合
     * @param zipFile  压缩文件
     * @param comment  压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean zipFiles(final Collection<File> resFiles, final File zipFile, final String comment)
            throws IOException {
        if (null == resFiles || null == zipFile) {
            return false;
        }
        //创建压缩流
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File resFile : resFiles) {
                if (!zipFile(resFile, "", zos, comment)) {
                    return false;
                }
            }
            return true;
        }
    }


    /**
     * 压缩文件
     *
     * @param resFilePath 待压缩文件路径
     * @param zipFilePath 压缩文件路径
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean zipFile(final String resFilePath, final String zipFilePath)
            throws IOException {
        return zipFile(resFilePath, zipFilePath, null);
    }


    /**
     * 压缩文件
     *
     * @param resFilePath 待压缩文件路径
     * @param zipFilePath 压缩文件路径
     * @param comment     压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean zipFile(final String resFilePath, final String zipFilePath, final String comment)
            throws IOException {
        return zipFile(new File(resFilePath), new File(zipFilePath), comment);
    }


    /**
     * 压缩文件
     *
     * @param resFile 待压缩文件
     * @param zipFile 压缩文件
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean zipFile(final File resFile, final File zipFile)
            throws IOException {
        return zipFile(resFile, zipFile, null);
    }

    /**
     * 压缩文件
     *
     * @param resFile 待压缩文件
     * @param zipFile 压缩文件
     * @param comment 压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean zipFile(final File resFile, final File zipFile, final String comment)
            throws IOException {
        if (null == resFile || null == zipFile) {
            return false;
        }
        //创建压缩流
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            return zipFile(resFile, "", zos, comment);
        }
    }

    /**
     * 压缩文件
     *
     * @param resFile  待压缩文件
     * @param rootPath 相对于压缩文件的路径
     * @param zos      压缩文件输出流
     * @param comment  压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    private static boolean zipFile(final File resFile, String rootPath, final ZipOutputStream zos, final String comment)
            throws IOException {
        rootPath = rootPath + (isSpace(rootPath) ? "" : File.separator) + resFile.getName();
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            // 如果是空文件夹那么创建它，我把'/'换为File.separator测试就不成功，eggPain
            if (null == fileList || fileList.length <= 0) {
                ZipEntry entry = new ZipEntry(rootPath + '/');
                if (!StringUtils.isEmptyEnhance(comment)) {
                    entry.setComment(comment);
                }
                zos.putNextEntry(entry);
                zos.closeEntry();
            } else {
                for (File file : fileList) {
                    // 如果递归返回false则返回false
                    if (!zipFile(file, rootPath, zos, comment)) {
                        return false;
                    }
                }
            }
        } else {
            try (InputStream is = new BufferedInputStream(new FileInputStream(resFile))) {
                ZipEntry entry = new ZipEntry(rootPath);
                if (!StringUtils.isEmptyEnhance(comment)) {
                    entry.setComment(comment);
                }
                zos.putNextEntry(entry);
                byte[] buffer = new byte[KB];
                int len;
                while ((len = is.read(buffer, 0, KB)) != -1) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            }
        }
        return true;
    }

    /**
     * 批量解压文件
     *
     * @param zipFiles    压缩文件集合（文件路径及文件名）
     * @param destDirPath 目标目录路径
     * @return {@code true}: 解压成功<br>{@code false}: 解压失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean unzipFiles(final Collection<File> zipFiles, final String destDirPath)
            throws IOException {
        return unzipFiles(zipFiles, new File(destDirPath));
    }

    /**
     * 批量解压文件
     *
     * @param zipFiles 压缩文件集合（文件路径及文件名）
     * @param destDir  目标目录
     * @return {@code true}: 解压成功<br>{@code false}: 解压失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean unzipFiles(final Collection<File> zipFiles, final File destDir)
            throws IOException {
        if (null == zipFiles || null == destDir) {
            return false;
        }
        for (File zipFile : zipFiles) {
            if (!unzipFile(zipFile, destDir)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 解压文件
     *
     * @param zipFilePath 待解压文件路径 （文件路径及文件名）
     * @param destDirPath 目标目录路径
     * @return {@code true}: 解压成功<br>{@code false}: 解压失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean unzipFile(final String zipFilePath, final String destDirPath)
            throws IOException {
        return unzipFile(new File(zipFilePath), new File(destDirPath));
    }

    /**
     * 解压文件
     *
     * @param zipFile 待解压文件
     * @param destDir 目标目录
     * @return {@code true}: 解压成功<br>{@code false}: 解压失败
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean unzipFile(final File zipFile, final File destDir)
            throws IOException {
        return unzipFileByKeyword(zipFile, destDir, null) != null;
    }

    /**
     * 解压带有关键字的文件
     *
     * @param zipFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @param keyword     关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<File> unzipFileByKeyword(final String zipFilePath, final String destDirPath, final String keyword)
            throws IOException {
        return unzipFileByKeyword(new File(zipFilePath),
                new File(destDirPath), keyword);
    }

    /**
     * 解压带有关键字的文件
     *
     * @param zipFile 待解压文件
     * @param destDir 目标目录
     * @param keyword 关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<File> unzipFileByKeyword(final File zipFile, final File destDir, final String keyword)
            throws IOException {
        if (null == zipFile || null == destDir) {
            return null;
        }
        List<File> files = new ArrayList<>();
        ZipFile zf = new ZipFile(zipFile);
        Enumeration<?> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            String entryName = entry.getName();
            if (StringUtils.isEmptyEnhance(keyword) || FileUtils.getFileName(entryName).toLowerCase().contains(keyword.toLowerCase())) {
                String filePath = destDir + File.separator + entryName;
                File file = new File(filePath);
                files.add(file);
                if (entry.isDirectory()) {
                    if (!FileUtils.checkDirAndMakeDirs(file)) {
                        return null;
                    }
                } else {
                    if (!FileUtils.checkFileAndMakeDirs(file)) {
                        return null;
                    }
                    try (InputStream in = new BufferedInputStream(zf.getInputStream(entry));
                         OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
                        byte[] buffer = new byte[KB];
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    }
                }
            }
        }
        return files;
    }


    /**
     * 获取压缩文件中的文件路径链表
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的文件路径链表
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<String> getFilesPath(final String zipFilePath)
            throws IOException {
        return getFilesPath(new File(zipFilePath));
    }

    /**
     * 获取压缩文件中的文件路径链表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的文件路径链表
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<String> getFilesPath(final File zipFile)
            throws IOException {
        if (null == zipFile) {
            return null;
        }
        List<String> paths = new ArrayList<>();
        Enumeration<?> entries = getEntries(zipFile);
        while (entries.hasMoreElements()) {
            paths.add(((ZipEntry) entries.nextElement()).getName());
        }
        return paths;
    }


    /**
     * 获取压缩文件中的注释链表
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的注释链表
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<String> getComments(final String zipFilePath)
            throws IOException {
        return getComments(new File(zipFilePath));
    }

    /**
     * 获取压缩文件中的注释链表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的注释链表
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<String> getComments(final File zipFile)
            throws IOException {
        if (null == zipFile) {
            return null;
        }
        List<String> comments = new ArrayList<>();
        Enumeration<?> entries = getEntries(zipFile);
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            comments.add(entry.getComment());
        }
        return comments;
    }

    /**
     * 获取压缩文件中的文件对象
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的文件对象
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static Enumeration<?> getEntries(final String zipFilePath)
            throws IOException {
        return getEntries(new File(zipFilePath));
    }

    /**
     * 获取压缩文件中的文件对象
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的文件对象
     * @throws IOException IO错误时抛出
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static Enumeration<?> getEntries(final File zipFile)
            throws IOException {
        if (null == zipFile) {
            return null;
        }
        return new ZipFile(zipFile).entries();
    }


    private static boolean isSpace(final String s) {
        if (null == s) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

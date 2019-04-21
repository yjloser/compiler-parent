package com.semitransfer.compiler.core.config.internal.api.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.*;
import java.util.stream.IntStream;

import static com.semitransfer.compiler.core.config.internal.api.Constants.*;

/**
 * <p>
 * 文件工具类
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class FileUtils {

    /**
     * 创建图片验证码
     *
     * @param localPath 本地路径
     * @return 返回路径加文件名
     * @author Mr.Yang
     * @date 2018/12/9
     */
    public static JSONObject createCode(String localPath) {
        return createCode(localPath, NUM_FOUR);
    }

    /**
     * 创建图片验证码
     *
     * @param localPath 本地路径
     * @param size      验证码个数
     * @return 返回路径加文件名
     * @author Mr.Yang
     * @date 2018/12/9
     */
    public static JSONObject createCode(String localPath, int size) {
        int width = NUM_EIGHTY, height = NUM_FORTH;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(NUM_ZERO, NUM_ZERO, width, height);
        // 设置字体
        g.setFont(new Font(FONT, Font.BOLD, NUM_TWENTY));
        // 随机数字
        StringBuilder builder = new StringBuilder();
        Random r = new Random(System.currentTimeMillis());
        IntStream.range(NUM_ZERO, size).forEach(t -> {
            //验证码
            int a = r.nextInt(NUM_TEN);
            // 15~30范围内的一个整数，作为y坐标
            int y = 15 + r.nextInt(NUM_TWENTY);
            builder.append(a);
            Color c = new Color(r.nextInt(NUM_MAX_SIZE), r.nextInt(NUM_MAX_SIZE), r.nextInt(NUM_MAX_SIZE));
            g.setColor(c);
            g.drawString(String.valueOf(a), NUM_FIVE + t * width / size, y);
        });
        //干扰线
        IntStream.range(NUM_ZERO, NUM_TEN).forEach(t -> {
            Color c = new Color(r.nextInt(NUM_MAX_SIZE), r.nextInt(NUM_MAX_SIZE), r.nextInt(NUM_MAX_SIZE));
            g.setColor(c);
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
        });
        try {
            // 类似于流中的close()带动flush()---把数据刷到img对象当中
            g.dispose();
            //本地路径及文件名
            String fileName = String.valueOf(System.currentTimeMillis()).concat(".jpg");
            //如果为空则创建
            File file = new File(localPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String localPathFileName = localPath + File.separator + fileName;
            //写入到本地路径
            ImageIO.write(img, "png", new File(localPathFileName));
            //本地路径
            JSONObject outcome = new JSONObject();
            //验证码
            outcome.put(FIELD_CODE, builder.toString());
            //路径
            outcome.put(FIELD_LOCAL_PATH, localPathFileName);
            //文件名
            outcome.put(FIELD_FILE_NAME, fileName);
            return outcome;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //=====================操作文件开始=======================//

    /**
     * 文件上传
     *
     * @param savePath 保存的路径
     * @param fileName 文件名
     * @param file     MultipartFile流
     * @return 是否写入到本地 true为是
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static boolean saveFile(@NotNull final String savePath,
                                   @NotNull final String fileName,
                                   @NotNull final MultipartFile file)
            throws Exception {
        byte[] data = readInputStream(file.getInputStream());
        //new一个文件对象用来保存图片
        File uploadFile = new File(savePath + fileName);
        //判断文件夹是否存在，不存在就创建一个
        File fileDirectory = new File(savePath);
        if (!fileDirectory.exists() && !fileDirectory.mkdir()) {
            throw new Exception("文件夹创建失败！路径为：" + savePath);
        }
        //创建输出流
        try (FileOutputStream outStream = new FileOutputStream(uploadFile)) {
            outStream.write(data);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return uploadFile.exists();
    }

    /**
     * 读取输入流返回byte数组
     *
     * @param inStream 输入流
     * @return 返回byte数组
     * @author Mr.Yang
     * @date 2018/12/2
     */
    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }


    /**
     * 多种类型文件下载
     *
     * @param request     请求对象
     * @param response    响应对象
     * @param contentType （http://tool.oschina.net/commons） 内容协调器
     * @param filePath    文件路径
     * @param fileName    文件名
     * @throws IOException
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static void download(HttpServletRequest request,
                                HttpServletResponse response,
                                String contentType,
                                String filePath,
                                String fileName) throws IOException {
        request.setCharacterEncoding("UTF-8");
        long fileLength = new File(filePath).length();
        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment;" + UserAgentUtils.encodeFileName(request, fileName));
        response.setHeader("Content-Length", String.valueOf(fileLength));
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
             BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断指定路径是否存在，如果不存在，根据参数决定是否新建
     *
     * @param filePath 指定的文件路径
     * @param isNew    true：新建、false：不新建
     * @return 存在返回true，否则false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isExist(String filePath, boolean isNew) {
        File file = new File(filePath);
        if (!file.exists() && isNew) {
            //新建文件路径
            return file.mkdirs();
        }
        return false;
    }

    /**
     * 获取指定文件的大小
     *
     * @param file 文件路径
     * @return 返回文件大小
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return size;
    }

    /**
     * 删除所有文件，包括文件夹
     *
     * @param filePath 删除路径
     * @return 返回删除结果 true为删除成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean deleteAll(String filePath) {
        File path = new File(filePath);
        try {
            if (!path.exists()) {
                // 目录不存在退出
                return false;
            }
            // 如果是文件删除
            if (path.isFile()) {
                return path.delete();
            }
            // 如果目录中有文件递归删除文件
            File[] files = path.listFiles();
            assert files != null;
            IntStream.range(0, files.length).
                    forEach(t -> deleteAll(files[t].getAbsolutePath()));
            return path.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 复制文件或者文件夹
     *
     * @param inputFile   源文件
     * @param outputFile  目的文件
     * @param isOverWrite 是否覆盖文件
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static void copy(File inputFile, File outputFile, boolean isOverWrite) {
        if (!inputFile.exists()) {
            throw new RuntimeException(inputFile.getPath() + " source directory does not exist");
        }
        copyPri(inputFile, outputFile, isOverWrite);
    }

    /**
     * 复制文件或者文件夹
     *
     * @param inputFile   源文件
     * @param outputFile  目的文件
     * @param isOverWrite 是否覆盖文件
     * @author Mr.Yang
     * @date 2018/12/1
     */
    private static void copyPri(File inputFile, File outputFile, boolean isOverWrite) {
        //文件
        if (inputFile.isFile()) {
            copySimpleFile(inputFile, outputFile, isOverWrite);
        } else {
            //文件夹
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            }
            // 循环子文件夹
            Arrays.asList(Objects.requireNonNull(inputFile.listFiles())).forEach(file -> {
                //复制
                copy(file, new File(outputFile.getPath() + File.separator + file.getName()), isOverWrite);
            });
        }
    }

    /**
     * 复制单个文件
     *
     * @param inputFile   源文件
     * @param outputFile  目的文件
     * @param isOverWrite 是否覆盖
     * @author Mr.Yang
     * @date 2018/12/1
     */
    private static void copySimpleFile(File inputFile, File outputFile, boolean isOverWrite) {
        if (outputFile.exists()) {
            //可以覆盖
            if (isOverWrite) {
                if (!outputFile.delete()) {
                    throw new RuntimeException(outputFile.getPath() + " unable to cover");
                }
            } else {
                // 不允许覆盖
                return;
            }
        }
        //文件处理
        try (InputStream in = new FileInputStream(inputFile);
             OutputStream out = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件的MD5
     *
     * @param file 文件
     * @return 返回文件md5编码
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getFileMD5(String file) {
        return getFileMD5(new File(file));
    }

    /**
     * 获取文件的MD5
     *
     * @param file 文件
     * @return 返回文件md5编码
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte[] bytes = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(bytes, 0, 1024)) != -1) {
                digest.update(bytes, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 文件重命名
     *
     * @param oldPath 老文件
     * @param newPath 新文件
     * @return 返回true标识，重命名成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean renameDir(String oldPath, String newPath) {
        // 文件或目录
        File oldFile = new File(oldPath);
        // 文件或目录
        File newFile = new File(newPath);
        // 重命名
        return oldFile.renameTo(newFile);
    }

    /**
     * 获取文件名, 带扩展名
     *
     * @param file 文件
     * @return 文件名
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getFileName(File file) {
        if (null == file) {
            return null;
        }
        return getFileName(file.getPath());
    }

    /**
     * 获取文件名, 带扩展名
     *
     * @param filePath 文件
     * @return 文件名
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmptyEnhance(filePath)) {
            return filePath;
        }
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    /**
     * 获取文件名, 不带扩展名
     *
     * @param file 文件
     * @return 文件名
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getFileNameWithoutExtension(File file) {
        if (null == file) {
            return null;
        }
        return getFileNameWithoutExtension(file.getPath());
    }

    /**
     * 获取文件名, 不带扩展名
     *
     * @param filePath 文件路径
     * @return 文件名
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmptyEnhance(filePath)) {
            return filePath;
        }
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return (lastPoi == -1 ? filePath : filePath.substring(0, lastPoi));
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPoi);
    }

    /**
     * 获取文件扩展名
     *
     * @param file 文件
     * @return 文件扩展名
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getFileExtension(File file) {
        if (null == file) {
            return null;
        }
        return getFileExtension(file.getPath());
    }

    /**
     * 获取文件扩展名
     *
     * @param filePath 文件路径
     * @return 文件扩展名
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isEmptyEnhance(filePath)) {
            return filePath;
        }
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) {
            return "";
        }
        return filePath.substring(lastPoi + 1);
    }
    //=====================读取文件开始=======================//


    /**
     * 读取文件, 默认编码 UTF-8
     *
     * @param filePath 文件路径
     * @return 文本内容, 读取失败返回 null
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String readAsString(String filePath) {
        StringBuilder builder = readAsStringBuilder(filePath);
        return builder != null ? builder.toString() : null;
    }

    /**
     * 读取文件, 默认编码 UTF-8
     *
     * @param file 文件对象
     * @return 文本内容, 读取失败返回 null
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String readAsString(File file) {
        StringBuilder builder = readAsStringBuilder(file);
        return builder != null ? builder.toString() : null;
    }

    /**
     * 读取文件
     *
     * @param file        文件对象
     * @param charsetName 编码名称
     * @return 文本内容, 读取失败返回 null
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String readAsString(File file, String charsetName) {
        StringBuilder builder = readAsStringBuilder(file, charsetName);
        return builder != null ? builder.toString() : null;
    }

    /**
     * 读取文件, 默认编码 UTF-8
     *
     * @param file 文件对象
     * @return 文本内容, 读取失败返回 null
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static StringBuilder readAsStringBuilder(File file) {
        return readAsStringBuilder(file, StandardCharsets.UTF_8.toString());
    }

    /**
     * 读取文件, 默认编码 UTF-8
     *
     * @param filePath 文件路径
     * @return 文本内容, 读取失败返回 null
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static StringBuilder readAsStringBuilder(String filePath) {
        if (StringUtils.isEmptyEnhance(filePath)) {
            return null;
        }
        return readAsStringBuilder(new File(filePath), StandardCharsets.UTF_8.toString());
    }

    /**
     * 读取文件
     *
     * @param file        文件对象
     * @param charsetName 编码名称
     * @return 文本内容, 读取失败返回 null
     * @author Mr.Yang
     * @date 2018/12/1
     */
    private static StringBuilder readAsStringBuilder(File file, String charsetName) {
        if (!file.isFile()) {
            return null;
        }
        StringBuilder fileContent = new StringBuilder();
        try (InputStreamReader is = new InputStreamReader(
                new FileInputStream(file), charsetName);
             BufferedReader reader = new BufferedReader(is)) {
            //开始处理
            String line;
            while ((line = reader.readLine()) != null) {
                if (fileContent.length() != 0) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }

    /**
     * 以集合形式读取文件, 每一行为一个元素, 默认编码 UTF-8
     *
     * @param file 文件对象
     * @return 文本集合, 每一个元素代表一行
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<String> readLines(File file) {
        return readLines(file, StandardCharsets.UTF_8.toString());
    }

    /**
     * 以集合形式读取文件, 每一行为一个元素
     *
     * @param file        文件对象
     * @param charsetName 编码名称
     * @return 文本集合, 每一个元素代表一行
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<String> readLines(File file, String charsetName) {
        if (!file.isFile()) {
            return null;
        }
        List<String> contents = new ArrayList<>();
        try (InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
             BufferedReader reader = new BufferedReader(is)) {
            String line;
            while ((line = reader.readLine()) != null) {
                contents.add(line);
            }
            return contents;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }

    /**
     * 文件不存在, 如果父文件夹已存在, 返回 true;
     * 如果父文件夹不存在将自动创建, 创建成功返回 true, 失败返回 false
     * 文件存在, 对象为文件对象返回 true, 为文件夹对象返回 false
     *
     * @param file 自动创建父文件夹
     * @return true 检查通过, false 为不通过
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean checkFileAndMakeDirs(File file) {
        if (!file.exists()) {
            File dir = file.getParentFile();
            return dir != null && (dir.exists() ? dir.isDirectory() : dir.mkdirs());
        }
        return !file.isDirectory();
    }

    //=====================写入文件开始=======================//

    /**
     * 将字符串写入文件
     *
     * @param file    文件
     * @param content 文本内容
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeString(File file, String content) {
        return writeString(file, content, false);
    }

    /**
     * 将字符串写入文件
     *
     * @param file    文件
     * @param content 文本内容
     * @param append  是否为追加 (true 在文本末尾写入, false 清除原有文本重新写入)
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeString(File file, String content, boolean append) {
        return writeString(file, content, append, false);
    }

    /**
     * 将字符串写入文件
     *
     * @param file           文件
     * @param content        文本内容
     * @param append         是否为追加 (true 在文本末尾写入, false 清除原有文本重新写入)
     * @param endWithNewLine 是否在末尾添加换行
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeString(File file, String content, boolean append, boolean endWithNewLine) {
        if (StringUtils.isEmptyEnhance(content) || !checkFileAndMakeDirs(file)) {
            return false;
        }
        //写入操作
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            writer.append(content);
            if (endWithNewLine) {
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }

    /**
     * 将字符串集合写入文件, 集合中每个元素占一行
     *
     * @param file     文件
     * @param contents 文本内容集合
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeLines(File file, List<String> contents) {
        return writeLines(file, contents, false);
    }

    /**
     * 将字符串数组写入文件, 数组中每个元素占一行
     *
     * @param file     文件
     * @param contents 文本内容数组
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeLines(File file, String... contents) {
        return writeLines(file, Arrays.asList(contents), false);
    }

    /**
     * 将字符串集合写入文件, 集合中每个元素占一行
     *
     * @param file     文件
     * @param contents 文本内容集合
     * @param append   是否为追加 (true 在文本末尾写入, false 清除原有文本重新写入)
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeLines(File file, List<String> contents, boolean append) {
        if (null == contents || !checkFileAndMakeDirs(file)) {
            return false;
        }
        //写入操作
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            for (int i = 0; i < contents.size(); i++) {
                if (i > 0) {
                    writer.newLine();
                }
                writer.append(contents.get(i));
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }


    /**
     * 将输入流写入文件
     *
     * @param file 文件
     * @param is   输入流
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeStream(File file, InputStream is) {
        return writeStream(file, is, false);
    }

    /**
     * 将输入流写入文件
     *
     * @param file   文件
     * @param is     输入流
     * @param append 是否为追加 (true 在文本末尾写入, false 清除原有文本重新写入)
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeStream(File file, InputStream is, boolean append) {
        if (null == is || !checkFileAndMakeDirs(file)) {
            return false;
        }
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file, append))) {
            int sBufferSize = 8192;
            byte[] data = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将字节数组写入文件
     *
     * @param file  文件
     * @param bytes 字节数组
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeBytes(File file, byte[] bytes) {
        return writeBytes(file, bytes, false);
    }

    /**
     * 将字节数组写入文件
     *
     * @param file   文件
     * @param bytes  字节数组
     * @param append 是否为追加 (true 在文本末尾写入, false 清除原有文本重新写入)
     * @return 是否写入成功
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean writeBytes(File file, byte[] bytes, boolean append) {
        if (null == bytes || !checkFileAndMakeDirs(file)) {
            return false;
        }
        //文件写入操作
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, append))) {
            bos.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 自动创建该文件夹及其父文件夹
     *
     * @param file 指定文件夹
     * @return true 检查通过, false 为不通过
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean checkDirAndMakeDirs(File file) {
        if (file.exists()) {
            return file.isDirectory();
        }
        return file.mkdirs();
    }
}

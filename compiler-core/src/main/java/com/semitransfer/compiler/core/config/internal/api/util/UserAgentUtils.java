package com.semitransfer.compiler.core.config.internal.api.util;


import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * <p>
 * 浏览器版本识别
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class UserAgentUtils {

    /**
     * 获取客户端浏览器类型、编码下载文件名
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 返回不同浏览器下载文件名
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static String encodeFileName(HttpServletRequest request, String fileName) {
        String userAgent = request.getHeader("User-Agent");
        String rtn;
        try {
            String newFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            rtn = "filename=\"" + newFilename + "\"";
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.contains("msie")) {
                    rtn = "filename=\"" + newFilename + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.contains("opera")) {
                    rtn = "filename*=UTF-8''" + newFilename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.contains("safari")) {
                    rtn = "filename=\"" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"";
                }
                // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
                else if (userAgent.contains("applewebkit")) {
                    newFilename = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
                    rtn = "filename=\"" + newFilename + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.contains("mozilla")) {
                    rtn = "filename*=UTF-8''" + newFilename;
                }
            }
            return rtn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
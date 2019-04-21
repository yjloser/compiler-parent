package com.semitransfer.compiler.core.config.internal.api.util;


import org.springframework.core.env.Environment;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

import static com.semitransfer.compiler.core.config.internal.api.Constants.*;

/**
 * <p>
 * 邮箱工具
 * <p>
 *
 * @author: Mr.Yang
 * @since: 2018-12-09 15:36
 **/
public class MailUtils {


    /**
     * 发送邮件
     *
     * @param content     邮箱内容
     * @param receiver    接收者 收件人邮箱 收件人  多个时参数形式  ：  "xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
     * @param environment 环境变量
     * @author Mr.Yang
     * @date 2018/12/9
     */
    public static void sendTextMail(Environment environment, String content, String receiver) {
        try {
            //发送邮箱
            sendTextMail(environment.getProperty(FIELD_MAIL_PROTOCOL)
                    , environment.getProperty(FIELD_MAIL_HOST), environment.getProperty(FIELD_MAIL_SENDER),
                    environment.getProperty(FIELD_MAIL_SENDER_NAME), environment.getProperty(FIELD_MAIL_AUTH),
                    environment.getProperty(FIELD_MAIL_TITLE), content,
                    receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送邮件
     *
     * @param receiver    接收者 收件人邮箱 收件人  多个时参数形式  ：  "xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
     * @param environment 环境变量
     * @author Mr.Yang
     * @date 2018/12/9
     */
    public static void sendTextMail(Environment environment, String receiver) {
        try {
            //发送邮箱
            sendTextMail(environment.getProperty(FIELD_MAIL_PROTOCOL)
                    , environment.getProperty(FIELD_MAIL_HOST), environment.getProperty(FIELD_MAIL_SENDER),
                    environment.getProperty(FIELD_MAIL_SENDER_NAME), environment.getProperty(FIELD_MAIL_AUTH),
                    environment.getProperty(FIELD_MAIL_TITLE), environment.getProperty(FIELD_MAIL_CONTENT),
                    receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送邮件
     *
     * @param protocol    邮箱协议
     * @param host        发送方代理
     * @param sendMail    发送者邮箱
     * @param sendName    发送者名称
     * @param authCode    发送者邮箱授权码
     * @param receiveMail 接收者邮箱 收件人邮箱 收件人  多个时参数形式  ：  "xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
     * @param mailTitle   邮箱标题
     * @param content     邮箱内容
     * @author Mr.Yang
     * @date 2018/12/4
     */
    public static void sendTextMail(String protocol, String host, String sendMail, String sendName, String authCode
            , String mailTitle, String content, String receiveMail) throws Exception {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();
        // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.transport.protocol", protocol);
        // 发件人的邮箱的
        props.setProperty("mail.host", host);
        // SMTP
        // 服务器地址
        // 请求认证，参数名称与具体实现有关
        props.setProperty("mail.smtp.auth", "true");
        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        // 设置为debug模式, 可以查看详细的发送 log
        session.setDebug(false);
        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, sendMail, sendName, mailTitle,
                content, receiveMail);
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        // 5. 使用 邮箱账号 和 密码 连接邮件服务器
        // 这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
        transport.connect(sendMail, authCode);
        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人,
        // 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        // 7. 关闭连接
        transport.close();
    }

    /**
     * 创建邮件信息
     *
     * @param session     和服务器交互的会话
     * @param sendMail    发件人邮箱
     * @param sendName    发件人名称
     * @param receiveMail 收件人邮箱 收件人  多个时参数形式  ：  "xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
     * @param mailTitle   邮箱标题
     * @param content     文本信息
     * @author Mr.Yang
     * @date 2018/12/9
     */
    private static MimeMessage createMimeMessage(Session session, String sendMail, String sendName, String mailTitle,
                                                 String content, String receiveMail) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, sendName, StandardCharsets.UTF_8.toString()));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        if (StringUtils.notEmptyEnhance(receiveMail)) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(sendMail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiveMail));
        }

        // 4. Subject: 邮件主题
        message.setSubject(mailTitle, StandardCharsets.UTF_8.toString());

        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }

    /**
     * 创建随即验证码
     *
     * @return 返回常见的验证码
     * @author Mr.Yang
     * @date 2018/12/4
     */
    public static String createRandom() {
        return createRandom(NUM_SIX);
    }

    /**
     * 创建随即验证码
     *
     * @param length 验证码长度
     * @return 返回常见的验证码
     * @author Mr.Yang
     * @date 2018/12/4
     */
    public static String createRandom(int length) {
        return createRandom(true, length);
    }

    /**
     * 创建随即验证码
     *
     * @param numberFlag true为数字 fales为字符串
     * @param length     验证码长度
     * @return 返回常见的验证码
     * @author Mr.Yang
     * @date 2018/12/4
     */
    public static String createRandom(boolean numberFlag, int length) {
        StringBuilder retStr;
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = new StringBuilder();
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr.append(strTable.charAt(intR));
            }
            if (count >= NUM_TWO) {
                bDone = false;
            }
        } while (bDone);

        return retStr.toString();
    }
}

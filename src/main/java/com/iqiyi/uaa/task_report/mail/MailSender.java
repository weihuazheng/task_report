package com.iqiyi.uaa.task_report.mail;

import java.util.Date;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.iqiyi.uaa.task_report.config.Configuration;
import com.iqiyi.uaa.task_report.dao.MailDAO;

/*
 * Author : zhengweihua@qiyi.com
 * Date : 2015-12-09 11:00:00
 * */
public class MailSender {
    private static Logger logger = Logger.getLogger(MailSender.class.getName());
    private static final String EMAIL_SUFFIX = "qiyi.com";

    public static String toHtmlFormat(String content) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>")
          .append(content.replace("\\t", "    ").replace("\\n", "<br>"))
          .append("<br></html>");
        return sb.toString();
    }

    public static boolean isValidEmail(String email) {
        if (email.length() > 0 && email.endsWith(EMAIL_SUFFIX)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean sendMail(MailDAO mailDAO) {
        MailAuthenticator authenticator = new MailAuthenticator(Configuration.MAIL_USER_NAME, Configuration.MAIL_PASSWORD);
        Session sendMailSession = Session.getInstance(MailDAO.getProperties(), authenticator);
        System.out.println(mailDAO.getString());

        try {
            Message mailMessage = new MimeMessage(sendMailSession);
            Address from = new InternetAddress(Configuration.MAIL_FROM_ADDRESS);

            mailMessage.setFrom(from);
            String[] receivers = mailDAO.getToAddress();
            if (receivers != null && receivers.length > 0) {
                Address[] to_list = new InternetAddress[receivers.length];
                for (int index = 0; index < receivers.length; ++index) {
                    String email = receivers[index].trim();
                    if (isValidEmail(email)) {
                        to_list[index] = new InternetAddress(email);
                    } else {
                        logger.error("--> error email : " + email);
                    }
                }
                mailMessage.setRecipients(Message.RecipientType.TO, to_list);
            } else {
                logger.error("--> receivers is empty : " + mailDAO.getToAddress());
                return false;
            }

            String[] ccs = mailDAO.getCcs();
            if (ccs != null && ccs.length > 0) {
                Address[] cc_list = new InternetAddress[ccs.length];
                for (int index = 0; index < ccs.length; ++index) {
                    String email = ccs[index].trim();
                    if (isValidEmail(email)) {
                        cc_list[index] = new InternetAddress(email);
                    } else {
                        logger.error("--> error email : " + email);
                    }
                }
                mailMessage.setRecipients(Message.RecipientType.CC, cc_list);
            }

            mailMessage.setSubject(mailDAO.getSubject());
            mailMessage.setSentDate(new Date());
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent(mailDAO.getContent(), "text/html; charset=UTF-8");
            mainPart.addBodyPart(html);
            mailMessage.setContent(mainPart);
            Transport.send(mailMessage);
            logger.info("--> send mail OK");
            return true;
        } catch (Exception e) {
            logger.error("--> sendMail Exception : " + e.getMessage());
        }
        return false;
    }

    public static void test() {
        // 创建邮件信息
        MailDAO mailInfo = new MailDAO();
        mailInfo.setToAddress("zhengweihua@qiyi.com");
        mailInfo.setSubject("Mail Test");
        mailInfo.setContent("there\tis\na\tmsg");
        MailSender.sendMail(mailInfo);
    }

    public static void main(String[] args) {
        //test();
    	//String content = "there\tis\na\tmsg";
        //System.out.println(toHtmlFormat(content));
    }
}
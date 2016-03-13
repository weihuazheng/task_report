package com.iqiyi.uaa.task_report.dao;

import java.util.Properties;

import com.iqiyi.uaa.task_report.config.Configuration;

/**
 * Author : zhengweihua@qiyi.com
 * Date : 2015-12-09 11:00:00
 * */
public class MailDAO {
    private static final String EMAIL_ADDRESS_SEP = ";";
    // 是否需要身份验证
    private static final boolean validate = true;
    // 邮件的接收者，可以有多个，逗号分号分割
    private String toAddresses;
    // 邮件的抄送者，可以有多个，逗号分号分割
    private String ccs;
    // 邮件主题
    private String subject;
    // 邮件的文本内容
    private String content;
    // 邮件附件的文件名
    private String[] attachFileNames;

    public static Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", Configuration.MAIL_SERVER_HOST);
        p.put("mail.smtp.port", Configuration.MAIL_SERVER_PORT);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }

    public String[] getToAddress() {
        return toAddresses == null ? null : toAddresses.split(EMAIL_ADDRESS_SEP);
    }

    public String[] getCcs() {
        return ccs == null ? null : ccs.split(EMAIL_ADDRESS_SEP);
    }

    public void setCcs(String ccs) {
        this.ccs = ccs;
    }

    public void setToAddress(String toAddress) {
        this.toAddresses = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }

    public String[] getAttachFileNames() {
        return attachFileNames;
    }

    public void setAttachFileNames(String[] fileNames) {
        this.attachFileNames = fileNames;
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        sb.append("to:").append(toAddresses).append(",")
          .append("cc:").append(ccs).append(",")
          .append("subject:").append(subject).append(",")
          .append("content:").append(content);
        return sb.toString();
    }
}

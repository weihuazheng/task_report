package com.iqiyi.uaa.task_report.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.iqiyi.uaa.task_report.dao.MailDAO;
import com.iqiyi.uaa.task_report.mail.MailSender;
import com.iqiyi.uaa.task_report.utils.ReturnObj;

@WebServlet("/send/msg")
public class SendlMsg extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String SEP = ",";
    private static final int PARAMETER_NUMBER = 4;
    private static Logger logger = Logger.getLogger(SendlMsg.class);

    public SendlMsg() {
        super();
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
        return ;
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        ReturnObj retObj = new ReturnObj();

        String param = request.getParameter("param");
        // suggestion by shaohuimeng
        if (param != null && param.split(SEP, PARAMETER_NUMBER).length == PARAMETER_NUMBER) {
            logger.info("--> SendMsg Input : " + param);
        } else {
            retObj.setCode(1);
            retObj.setMsg("input: param=to_list,cc_list,subject,msg_content");
            response.getWriter().println(retObj.toString());
            return ;
        }

        String[] args = param.split(SEP, PARAMETER_NUMBER);
        MailDAO mailInfo = new MailDAO();
        int error = 0;
        if (args[0].length() > 0) {
            mailInfo.setToAddress(args[0]);
        } else {
            ++error;
        }
        if (args[1].length() > 0) {
            mailInfo.setCcs(args[1]);
        }
        if (args[2].length() > 0) {
            mailInfo.setSubject(args[2]);
        } else {
            ++error;
        }
        if (args[3].length() > 0) {
            mailInfo.setContent(MailSender.toHtmlFormat(args[3]));
        } else {
            ++error;
        }
        if (error == 0 && MailSender.sendMail(mailInfo)) {
            retObj.setCode(0);
            retObj.setMsg("send OK");
            response.getWriter().println(retObj.toString());
        } else {
            retObj.setCode(1);
            retObj.setMsg("send failed, please check the input");
            response.getWriter().println(retObj.toString());
        }
        return ;
    }
}

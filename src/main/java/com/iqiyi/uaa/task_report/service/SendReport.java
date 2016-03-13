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
import com.iqiyi.uaa.task_report.mysql.MysqlClient;
import com.iqiyi.uaa.task_report.utils.HtmlFormat;
import com.iqiyi.uaa.task_report.utils.ReturnObj;

@WebServlet("/send/report")
public class SendReport extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String NAME_COLUMN = "name";
    private static final String EMAIL_COLUMN = "email";
    private static Logger logger = Logger.getLogger(SendReport.class);

    public SendReport() {
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
        if (param != null) {
            logger.info("--> SendMsg Input : " + param);
        } else {
            retObj.setCode(1);
            retObj.setMsg("input: param=id");
            response.getWriter().println(retObj.toString());
            return ;
        }

        int id = 0;
        String name = null;
        String email = null;

        try {
            id = Integer.parseInt(param);
            name = MysqlClient.selectInfo(id, NAME_COLUMN);
            email = MysqlClient.selectInfo(id, EMAIL_COLUMN);
            if (name == null || email == null) {
                retObj.setCode(1);
                retObj.setMsg("invalid id");
                response.getWriter().println(retObj.toString());
                return ;
            }
        } catch (Exception e) {
            retObj.setCode(1);
            retObj.setMsg("invalid id");
            response.getWriter().println(retObj.toString());
            return ;
        }
        

        MailDAO mailInfo = new MailDAO();
        mailInfo.setToAddress(email);
        mailInfo.setSubject(String.format("Task Report for %s", name));
        mailInfo.setContent(HtmlFormat.convertToHtml(name, HtmlFormat.getTaskReporList(id)));
        if (MailSender.sendMail(mailInfo)) {
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

package com.iqiyi.uaa.task_report.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.iqiyi.uaa.task_report.dao.UserInfoDAO;
import com.iqiyi.uaa.task_report.mysql.MysqlClient;
import com.iqiyi.uaa.task_report.utils.ReturnObj;

@WebServlet("/select/user")
public class SelectUser extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(SelectUser.class);

    public SelectUser() {
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
        logger.info("--> selectUser");

        List<UserInfoDAO> userList = MysqlClient.selectUser();
        if (userList == null) {
            ReturnObj returnObj = new ReturnObj();
            returnObj.setCode(1);
            returnObj.setMsg("output is null");
            response.getWriter().println(returnObj.toString());
        } else {
            for (UserInfoDAO userInfoDAO : userList) {
                response.getWriter().println(userInfoDAO.toString());
            }
        }
        return ;
    }
}

package com.iqiyi.uaa.task_report.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.iqiyi.uaa.task_report.dao.TaskDataDAO;
import com.iqiyi.uaa.task_report.mysql.MysqlClient;
import com.iqiyi.uaa.task_report.utils.ReturnObj;

@WebServlet("/insert/data")
public class InsertTaskData extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String NAME_COLUMN = "name";
    private static Logger logger = Logger.getLogger(InsertTaskData.class);

    public InsertTaskData() {
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
        if (param == null) {
            retObj.setCode(1);
            retObj.setMsg("input: param=id,task_name,key,value");
            response.getWriter().println(retObj.toString());
            return ;
        } else {
            logger.info("--> InsertTaskData Input : " + param);
        }

        TaskDataDAO taskDataDAO = new TaskDataDAO();
        retObj = taskDataDAO.parseFromStr(param);
        if (retObj.getCode() == 0) {
            if (MysqlClient.selectInfo(taskDataDAO.getId(), NAME_COLUMN) != null) {
                retObj = MysqlClient.insertTaskData(taskDataDAO);
                response.getWriter().println(retObj.toString());
            } else {
                retObj.setCode(1);
                retObj.setMsg(String.format("id:%d is invalid", taskDataDAO.getId()));
                response.getWriter().println(retObj.toString());
            }
        } else {
            logger.error("taskDataDAO parse failed : " + param);
            response.getWriter().println(retObj.toString());
        }
        return ;
    }
}

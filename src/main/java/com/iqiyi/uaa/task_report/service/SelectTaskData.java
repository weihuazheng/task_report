package com.iqiyi.uaa.task_report.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.iqiyi.uaa.task_report.dao.TaskDataDAO;
import com.iqiyi.uaa.task_report.mysql.MysqlClient;
import com.iqiyi.uaa.task_report.utils.DateFormatCheck;
import com.iqiyi.uaa.task_report.utils.ReturnObj;

@WebServlet("/select/data")
public class SelectTaskData extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String NAME_COLUMN = "name";
    private static Logger logger = Logger.getLogger(SelectTaskData.class);

    public SelectTaskData() {
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
        if (param != null && param.split(",").length == 2) {
            logger.info("--> SelectTaskData Input : " + param);
        } else {
            retObj.setCode(1);
            retObj.setMsg("input: param=id,dt(%F)");
            response.getWriter().println(retObj.toString());
            return ;
        }

        int id = 0;
        String dt = null;

        try {
            String[] args = param.split(",");
            id = Integer.parseInt(args[0]);
            dt = args[1];
            logger.info(String.format("--> SelectTaskData : %d %s",id, dt));
            if (MysqlClient.selectInfo(id, NAME_COLUMN) != null && DateFormatCheck.isDataFormatOK(dt)) {
                List<TaskDataDAO> dataList = MysqlClient.selectTaskData(id, dt);
                if (dataList != null) {
                    for (TaskDataDAO taskDataDAO : dataList) {
                        response.getWriter().println(taskDataDAO.toString());
                    }
                } else {
                    retObj.setCode(1);
                    retObj.setMsg("no task data");
                    response.getWriter().println(retObj.toString());
                }
            } else {
                retObj.setCode(1);
                retObj.setMsg(String.format("invalid id:%d or dt:%s", id, dt));
                response.getWriter().println(retObj.toString());
            }
        } catch (Exception e) {
            logger.error("--> SelectTaskData Exception : " + e.getMessage());
            retObj.setCode(1);
            retObj.setMsg(e.getMessage());
            response.getWriter().println(retObj.toString());
        }
        return ;
    }
}

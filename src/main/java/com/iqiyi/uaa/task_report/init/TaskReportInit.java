package com.iqiyi.uaa.task_report.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import com.iqiyi.uaa.task_report.mysql.MysqlClient;

public class TaskReportInit implements ServletContextListener {
    private static Logger logger = Logger.getLogger(TaskReportInit.class);

    public void contextInitialized(ServletContextEvent event) {
        MysqlClient.init();
        logger.info("--> TaskReport Initialize() : OK");
        return ;
    }

    public void contextDestroyed(ServletContextEvent event) {
        MysqlClient.free();
        logger.info("--> TaskReport Destroyed() : OK");
        org.apache.log4j.LogManager.shutdown();
        return ;
    }
}

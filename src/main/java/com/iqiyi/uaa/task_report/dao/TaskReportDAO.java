package com.iqiyi.uaa.task_report.dao;

import java.util.HashMap;

/**
 * Author : zhengweihua@qiyi.com
 * Date   : 2015-12-01 10:00:00
 * */
public class TaskReportDAO {
    private String task_name;
    private String var_name;
    private HashMap<String, Long> kvMap;

    public TaskReportDAO() {
        this.task_name = null;
        this.var_name = null;
        this.kvMap = null;
    }

    public String getTaskName() {
        return task_name;
    }

    public void setTaskName(String task_name) {
        this.task_name = task_name;
    }

    public String getVarName() {
        return var_name;
    }

    public void setVarName(String var_name) {
        this.var_name = var_name;
    }

    public HashMap<String, Long> getKvMap() {
        return kvMap;
    }

    public void setKvMap(HashMap<String, Long> kvMap) {
        this.kvMap = kvMap;
    }
}

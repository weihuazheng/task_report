package com.iqiyi.uaa.task_report.dao;

import java.util.Comparator;

public class TaskReportCmp implements Comparator<TaskReportDAO> {
    public int compare(TaskReportDAO obj_1, TaskReportDAO obj_2) {
        String task_var_1 = String.format("%s_%s", obj_1.getTaskName(), obj_1.getVarName());
        String task_var_2 = String.format("%s_%s", obj_2.getTaskName(), obj_2.getVarName());
        return task_var_1.compareTo(task_var_2);
    }
}

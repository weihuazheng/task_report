package com.iqiyi.uaa.task_report.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iqiyi.uaa.task_report.dao.TaskDataDAO;
import com.iqiyi.uaa.task_report.dao.TaskReportCmp;
import com.iqiyi.uaa.task_report.dao.TaskReportDAO;
import com.iqiyi.uaa.task_report.mysql.MysqlClient;

public class HtmlFormat {
    private static final String HTML_START = "<html>";
    private static final String HTML_END = "</html>";
    private static final String TABLE_START = "<table border=\"2\">";
    private static final String TABLE_END = "</table>";
    private static final String TITLE_START = "<h3>";
    private static final String TITLE_END = "</h3>";
    private static final String TR_START = "<tr>";
    private static final String TR_END = "</tr>";
    // E7E757 yellow
    private static final String TD_COLUMN_START = "<td bgcolor=\"E7E757\">";
    private static final String TD_END = "</td>";
    private static final String TD_RED = "<td bgcolor=\"FFCCCC\">";
    private static final String TD_GREEN = "<td bgcolor=\"669933\">";
    private static final String TD_DEFAULT = "<td>";

    private static List<String> getDateList() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateList = new ArrayList<String>();
        String dt = sdf.format(new Date(System.currentTimeMillis()));
        dateList.add(dt);
        dateList.add(getTheDay(dt, -1));
        dateList.add(getTheDay(dt, -2));
        dateList.add(getTheDay(dt, -7));
        return dateList;
    }

    public static List<TaskReportDAO> getTaskReporList(int id) {
        HashMap<String, TaskReportDAO> taskVarMap = new HashMap<String, TaskReportDAO>();
        List<String> dateList = getDateList();

        for (String dt : dateList) {
            List<TaskDataDAO> taskDataList = MysqlClient.selectTaskData(id, dt);
            if (taskDataList == null) {
                System.out.println(String.format("--> no data for : %d %s",id, dt));
                continue;
            }
            for (TaskDataDAO taskData : taskDataList) {
                String taskVarName = String.format("%s:%s", taskData.getTaskName(), taskData.getVarName());
                if (taskVarMap.containsKey(taskVarName)) {
                    TaskReportDAO taskReport = taskVarMap.get(taskVarName);
                    if (taskReport.getKvMap().containsKey(taskData.getDate().toString())) {
                        System.out.println("--> error : " + taskData.toString());
                    } else {
                        taskReport.getKvMap().put(taskData.getDate().toString(), taskData.getVarValue());
                    }
                } else {
                    TaskReportDAO taskReport = new TaskReportDAO();
                    taskReport.setTaskName(taskData.getTaskName());
                    taskReport.setVarName(taskData.getVarName());
                    HashMap<String, Long> kvList = new HashMap<String, Long>();
                    kvList.put(taskData.getDate().toString(), taskData.getVarValue());
                    taskReport.setKvMap(kvList);
                    taskVarMap.put(taskVarName, taskReport);
                }
            }
        }

        List<TaskReportDAO> reportList = new ArrayList<TaskReportDAO>();
        for (Map.Entry<String, TaskReportDAO> entry : taskVarMap.entrySet()) {
            reportList.add(entry.getValue());
        }
        Collections.sort(reportList, new TaskReportCmp());
        return reportList;
    }

    /*
     * */
    public static String convertToHtml(String name, List<TaskReportDAO> taskReportList) {
        StringBuilder htmlResult = new StringBuilder();

        List<String> dateList = getDateList();
        htmlResult.append(HTML_START + "\n");
        // 打印标题
        htmlResult.append(TITLE_START + String.format("%s Task Report For %s", dateList.get(0), name) + TITLE_END + "\n");
        // 打印table
        htmlResult.append(TABLE_START + "\n");
        // 打印列名
        htmlResult.append(TR_START + "\n")
                  .append(TD_COLUMN_START + "TaskName" + TD_END + "\n")
                  .append(TD_COLUMN_START + "VarName" + TD_END + "\n");
        for (String dt : dateList) {
            htmlResult.append(TD_COLUMN_START + dt + TD_END + "\n");
        }
        htmlResult.append(TR_END + "\n");

        String lastTaskName = "";
        String td_start = TD_DEFAULT;
        for (TaskReportDAO taskReportDAO : taskReportList) {
            if (lastTaskName.length() > 0 && taskReportDAO.getTaskName().compareTo(lastTaskName) != 0) {
                if (td_start.equals(TD_DEFAULT)) {
                    td_start = TD_GREEN;
                } else {
                    td_start = TD_DEFAULT;
                }
            }
            htmlResult.append(TR_START + "\n")
                      .append(td_start + taskReportDAO.getTaskName() + TD_END + "\n")
                      .append(td_start + taskReportDAO.getVarName() + TD_END + "\n");
            for (String dt : dateList) {
                if (taskReportDAO.getKvMap().containsKey(dt)) {
                    htmlResult.append(td_start + taskReportDAO.getKvMap().get(dt) + TD_END + "\n");
                } else {
                    htmlResult.append(TD_RED + "Null" + TD_END + "\n");
                }
            }
            htmlResult.append(TR_END + "\n");
            lastTaskName = taskReportDAO.getTaskName();
        }
         
        htmlResult.append(TABLE_END + "\n");
        htmlResult.append(HTML_END + "\n");
        return htmlResult.toString();
    }

    private static String getTheDay(String today, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt = sdf.parse(today);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DAY_OF_YEAR, days);
            Date nextDate = rightNow.getTime();
            return sdf.format(nextDate);
        } catch (Exception e) {
            return "";
        }
    }
}

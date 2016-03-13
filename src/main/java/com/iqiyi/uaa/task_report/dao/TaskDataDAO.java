package com.iqiyi.uaa.task_report.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.iqiyi.uaa.task_report.utils.ReturnObj;
import com.iqiyi.uaa.task_report.utils.CommonVar;

/**
 * Author : zhengweihua@qiyi.com
 * Date : 2015-11-26 17:25:00
 * */
public class TaskDataDAO {
    private int id;
    private Date date;
    private String task_name;
    private String var_name;
    private long var_value;
    private Timestamp insert_time;

    public TaskDataDAO() {
        this.id = 0;
        this.date = null;
        this.task_name = null;
        this.var_name = null;
        this.var_value = 0L;
        this.insert_time = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public long getVarValue() {
        return var_value;
    }

    public void setVarValue(long var_value) {
        this.var_value = var_value;
    }

    public Timestamp getInsertTime() {
        return insert_time;
    }

    public void setInsertTime(Timestamp insert_time) {
        this.insert_time = insert_time;
    }

    public ReturnObj parseFromStr(String parameter) {
        String[] part = parameter.split(CommonVar.INPUT_VALUE_SEP);
        // id, task_name, key, value
        if (part.length != 4) {
            return new ReturnObj(1, "input: param=id, task_name, key, value");
        } else if (part[0].isEmpty()) {
            return new ReturnObj(1, "id is empty");
        } else if (part[1].isEmpty()) {
            return new ReturnObj(1, "task_name is empty");
        } else if (part[2].isEmpty()) {
            return new ReturnObj(1, "var_name is empty");
        } else if (part[3].isEmpty()) {
            return new ReturnObj(1, "var_value is empty");
        }

        try {
            setId(Integer.parseInt(part[0]));
            setDate(new Date(System.currentTimeMillis()));
            setTaskName(part[1]);
            setVarName(part[2]);
            setVarValue(Long.parseLong(part[3]));
            setInsertTime(new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            return new ReturnObj(1, e.getMessage());
        }
        return new ReturnObj(0, "OK");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sb.append(this.id).append(CommonVar.OUTPUT_VALUE_SEP)
          .append(sdf.format(this.date)).append(CommonVar.OUTPUT_VALUE_SEP)
          .append(this.task_name).append(CommonVar.OUTPUT_VALUE_SEP)
          .append(this.var_name).append(CommonVar.OUTPUT_VALUE_SEP)
          .append(this.var_value).append(CommonVar.OUTPUT_VALUE_SEP)
          .append(this.insert_time.toString().substring(0, 19));
        return sb.toString();
    }
}

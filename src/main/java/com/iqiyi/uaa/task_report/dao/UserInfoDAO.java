package com.iqiyi.uaa.task_report.dao;

import com.iqiyi.uaa.task_report.utils.ReturnObj;
import com.iqiyi.uaa.task_report.utils.CommonVar;

/**
 * Author : zhengweihua@qiyi.com
 * Date   : 2015-11-26 17:25:00
 * */
public class UserInfoDAO {
    private static final String QIYI_COM = "@qiyi.com";
    private static final String DEV_QIYI_COM = "@dev.qiyi.com";

    private int id;
    private String name;
    private String email;

    public UserInfoDAO() {
        this.id = 0;
        this.name = null;
        this.email = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ReturnObj parseFromStr(String parameter) {
        String[] part = parameter.split(CommonVar.INPUT_VALUE_SEP);
        // id, name, email
        if (part.length != 3) {
            return new ReturnObj(1, "input: param=id,name,email");
        } else if (part[0].isEmpty()) {
            return new ReturnObj(1, "id is empty");
        } else if (part[1].isEmpty()) {
            return new ReturnObj(1, "name is empty");
        } else if (part[2].isEmpty()) {
            return new ReturnObj(1, "email is empty");
        }

        try {
            if (part[0].length() <= CommonVar.MAX_ID_LENGTH) {
                setId(Integer.parseInt(part[0]));
            } else {
                return new ReturnObj(1, "id is too large");
            }
            setName(part[1]);
            if (part[2].endsWith(QIYI_COM) || part[2].endsWith(DEV_QIYI_COM)) {
                setEmail(part[2]);
            } else {
                return new ReturnObj(1, "email: name@[dev.]qiyi.com");
            }
            
        } catch (Exception e) {
            return new ReturnObj(1, e.getMessage());
        }
        return new ReturnObj(0, "OK");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id).append(CommonVar.OUTPUT_VALUE_SEP)
          .append(this.name).append(CommonVar.OUTPUT_VALUE_SEP)
          .append(this.email);
        return sb.toString();
    }
}

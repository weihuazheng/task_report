package com.iqiyi.uaa.task_report.utils;

/*
 * Author : zhengweihua@qiyi.com
 * Date   : 2015-11-26 17:25:00
 * */
public class ReturnObj {
    private int code;
    private String msg;

    public ReturnObj() {
        this.code = -1;
        this.msg = null;
    }

    public ReturnObj(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return String.format("{\"code\":%d,\"msg\":\"%s\"}", code, msg);
    }

    public boolean equals(ReturnObj returnObj) {
        if (this.code == returnObj.getCode()) {
            return true;
        } else {
            return false;
        }
    }
}

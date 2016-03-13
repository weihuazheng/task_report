package com.iqiyi.uaa.task_report.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;

import com.iqiyi.uaa.task_report.config.Configuration;

public class TaskReportHttp {
    private static final String SEND_MSG_URL_SUFFIX = "/send/msg";
    private static final String INSERT_DATA_URL_SUFFIX = "/insert/data";

    public static boolean sendMsg(String to, String cc, String subject, String msg) {
        // if cc is empty, please set cc = "" not NULL
        if (to.isEmpty() || subject.isEmpty() || msg.isEmpty()) {
            System.err.println("--> Error : to_email or title or msg is empty");
            return false;
        } else if (cc == null) {
            cc = "";
        }

        try {
            StringBuffer request = new StringBuffer();
            request.append(URLEncoder.encode("param", "UTF-8")).append("=")
                   .append(URLEncoder.encode(String.format("%s,%s,%s,%s", to, cc, subject, msg), "UTF-8"));
            // send the request
            String service_url= Configuration.SERVICE_URL + SEND_MSG_URL_SUFFIX;
            URL url = new URL(service_url);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(request.toString());
            out.flush();
            // get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            System.err.println("--> Exception : " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean insertTaskData(int id, String task_name, String var_name, long var_value) {
        if (task_name.isEmpty() || var_name.isEmpty()) {
            System.err.println("--> Error : task_name or var_name is empty");
            return false;
        }

        try {
            StringBuffer request = new StringBuffer();
            request.append(URLEncoder.encode("param", "UTF-8")).append("=")
                   .append(URLEncoder.encode(String.format("%d,%s,%s,%d", id, task_name, var_name, var_value), "UTF-8"));
            // send the request
            String service_url= Configuration.SERVICE_URL + INSERT_DATA_URL_SUFFIX;
            URL url = new URL(service_url);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(request.toString());
            out.flush();
            // get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            System.err.println("--> Exception : " + e.getMessage());
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        boolean ret = insertTaskData(2892, "test_task", "var_test", 1001L);
        if (ret) {
            System.out.println("--> OK");
        } else {
            System.out.println("--> failed");
        }
    }
}

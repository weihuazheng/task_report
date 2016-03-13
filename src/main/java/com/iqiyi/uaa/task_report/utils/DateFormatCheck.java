package com.iqiyi.uaa.task_report.utils;

import org.apache.log4j.Logger;

public class DateFormatCheck {
    private static Logger logger = Logger.getLogger(DateFormatCheck.class);
    // YYYY-MM-DD
    public static boolean isDataFormatOK(String dt) {
        if (dt.length() != 10) {
            return false;
        }

        String[] parts = dt.split("-");
        if (parts.length != 3) {
            logger.error("--> DateFormatCheck Error data : " + dt);
            return false;
        }

        if (parts[0].length() == 4 && parts[1].length() == 2 && parts[2].length() == 2) {
            return true;
        } else {
            logger.error("--> DateFormatCheck Error data : " + dt);
            return false;
        }
    }
}

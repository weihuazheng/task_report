package com.iqiyi.uaa.task_report.mysql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.iqiyi.uaa.task_report.utils.ReturnObj;
import com.iqiyi.uaa.task_report.config.Configuration;
import com.iqiyi.uaa.task_report.dao.TaskDataDAO;
import com.iqiyi.uaa.task_report.dao.UserInfoDAO;

/*
 * Author : zhengweihua@qiyi.com
 * Date : 2015-12-10 11:00:00
 * */
public class MysqlClient {
    private static Logger logger = Logger.getLogger(MysqlClient.class);

    private static final String SELECT_SINGLE_COLUMN = "SELECT ? FROM tr_user_info WHERE id = ?";
    private static final String INSERT_USER = "INSERT INTO tr_user_info (id, name, email) VALUES(?, ?, ?)";
    private static final String INSERT_DATA = "REPLACE INTO tr_data_info"
            + " (dt, id, task_name, var_name, var_value, insert_time) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SELECT_DATA = "SELECT task_name, var_name, var_value, insert_time FROM tr_data_info WHERE dt = ? AND id = ?";
    private static final String SELECT_USER = "SELECT id, name, email FROM tr_user_info";
    private static DataSource datasource = null;

    public static void init() {
        PoolProperties p = new PoolProperties();

        p.setUrl("jdbc:mysql://" + Configuration.MYSQL_SERVER_ADDRESS);
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername(Configuration.MYSQL_USER_NAME);
        p.setPassword(Configuration.MYSQL_PASSWORD);

        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);

        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(5);
        p.setMaxIdle(5);
        p.setInitialSize(5);
        p.setMinIdle(5);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);

        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

        datasource = new DataSource();
        datasource.setPoolProperties(p);
        logger.info("--> MySQL init OK");
        return ;
    }

    /**
     * get all users' info
     * */
    public static List<UserInfoDAO> selectUser() {
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            PreparedStatement sql = null;
            sql = conn.prepareStatement(SELECT_USER);

            List<UserInfoDAO> userList = new ArrayList<UserInfoDAO>();
            ResultSet rs = sql.executeQuery();
            while (rs.next()) {
                UserInfoDAO userInfoDAO = new UserInfoDAO();
                userInfoDAO.setId((rs.getInt("id")));
                userInfoDAO.setName(rs.getString("name"));
                userInfoDAO.setEmail(rs.getString("email"));
                userList.add(userInfoDAO);
            }
            rs.close();
            sql.close();
            conn.close();
            if (userList.size() > 0) {
                return userList;
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("--> selectUser Exception : " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ignore, as we can't do anything about it here
                }
            }
        }
    }

    /**
     * get the column info of id
     * */
    public static String selectInfo(int id, String column) {
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            PreparedStatement sql = null;
            sql = conn.prepareStatement(SELECT_SINGLE_COLUMN);

            logger.info("--> select : " + column);
            sql.setString(1, column);
            sql.setInt(2, id);
            String info = null;
            ResultSet rs = sql.executeQuery();
            while (rs.next()) {
                info = rs.getString(column);
                break;
            }
            rs.close();
            sql.close();
            conn.close();
            if (info != null && info.length() > 0) {
                return info;
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("--> selectInfo Exception : " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ignore, as we can't do anything about it here
                }
            }
        }
    }

    /**
     * register
     * */
    public static ReturnObj insertUserInfo(UserInfoDAO userInfoDAO) {
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            PreparedStatement sql = conn.prepareStatement(INSERT_USER);
            sql.setInt(1, userInfoDAO.getId());
            sql.setString(2, userInfoDAO.getName());
            sql.setString(3, userInfoDAO.getEmail());
            int ret = sql.executeUpdate();
            sql.close();
            conn.close();
            if (ret == 1) {
                return new ReturnObj(0, "OK");
            } else {
                return new ReturnObj(0, "insert user failed");
            }
        } catch (SQLException e) {
            logger.error("--> insertUserInfo Exception : " + e.getMessage());
            e.printStackTrace();
            return new ReturnObj(1, "insert user failed");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ignore, as we can't do anything about it here
                }
            }
        }
    }

    /*
     * */
    public static ReturnObj insertTaskData(TaskDataDAO taskDataDAO) {
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            PreparedStatement sql = conn.prepareStatement(INSERT_DATA);
            sql.setDate(1, taskDataDAO.getDate());
            sql.setInt(2, taskDataDAO.getId());
            sql.setString(3, taskDataDAO.getTaskName());
            sql.setString(4, taskDataDAO.getVarName());
            sql.setLong(5, taskDataDAO.getVarValue());
            sql.setTimestamp(6, taskDataDAO.getInsertTime());
            int ret = sql.executeUpdate();
            sql.close();
            conn.close();
            if (ret != 0) {
                return new ReturnObj(0, "OK");
            } else {
                return new ReturnObj(0, "insert data failed");
            }
        } catch (SQLException e) {
            logger.error("--> insertTaskData Exception : " + e.getMessage());
            e.printStackTrace();
            return new ReturnObj(1, "insert data failed");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ignore, as we can't do anything about it here
                }
            }
        }
    }

    /*
     * */
    public static List<TaskDataDAO> selectTaskData(int id, String dt) {
        Connection conn = null;
        List<TaskDataDAO> dataList = new ArrayList<TaskDataDAO>();
        try {
            conn = datasource.getConnection();
            PreparedStatement sql = conn.prepareStatement(SELECT_DATA);
            sql.setDate(1, Date.valueOf(dt));
            sql.setInt(2, id);

            ResultSet rs = sql.executeQuery();
            while (rs.next()) {
                TaskDataDAO taskDataDAO = new TaskDataDAO();
                taskDataDAO.setDate(Date.valueOf(dt));
                taskDataDAO.setId(id);
                taskDataDAO.setTaskName(rs.getString("task_name"));
                taskDataDAO.setVarName(rs.getString("var_name"));
                taskDataDAO.setVarValue(rs.getLong("var_value"));
                taskDataDAO.setInsertTime(rs.getTimestamp("insert_time"));
                dataList.add(taskDataDAO);
            }
            rs.close();
            sql.close();
            conn.close();
            if (dataList.size() > 0) {
                logger.info(String.format("--> %d records selected for %d %s", dataList.size(), id, dt));
                return dataList;
            } else {
                logger.error(String.format("--> 0 records selected for %d %s", id, dt));
                return null;
            }
        } catch (SQLException e) {
            logger.error("--> selectTaskDataInfo Exception : " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ignore, as we can't do anything about it here
                }
            }
        }
    }

    public static void free() {
        datasource.close();
        datasource = null;
        logger.info("--> MySQL ConnectionPool Closed");
    }
}

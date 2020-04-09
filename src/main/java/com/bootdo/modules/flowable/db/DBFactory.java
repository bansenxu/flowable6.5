package com.bootdo.modules.flowable.db;

import com.bootdo.modules.flowable.domain.ExtDatasourceDO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

public class DBFactory {

    public Connection getConnection(ExtDatasourceDO ds) {
        Connection conn = null;
        try {
            Class.forName(ds.getDriverclassname()).newInstance(); //MYSQL驱动
            conn = DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getUserpwd()); //链接本地MYSQL
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public String build_SQL(String sql, Map<String, Object> param) {
        for (String key : param.keySet()) {
            try {
                if (-1 < sql.indexOf("${" + key + "}")) {
                    Object obj = param.get(key);
                    String tmp = "";
                    if (null != obj) {
                        tmp += obj;
                        sql = sql.replace("${" + key + "}", tmp);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

        }
        System.out.println("sql===" + sql);
        return sql;
    }

    public boolean execSql(ExtDatasourceDO ds, String sql, Map<String, Object> param) {
        boolean bl = false;
        try {
            Connection conn = getConnection(ds);
            sql = build_SQL(sql, param);
            Statement st = conn.createStatement();
            st.execute(sql);
            bl = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bl;
    }

}

package Data;

/**
  * @ProjectName:    Escape
  * @ClassName:      LoginIn
  * @Description:    登录类
  * @Author:         Han Chubo
  */


import UI.LoginMenu;
import UI.MultiPersonMode;
import UI.OnePersonMode;

import javax.swing.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.*;

public class LoginIn {
    private String account;
    private String password;

    Connection c = null;
    Statement stmt = null;

    public LoginIn(String accountName, String passwordOfAccount ,int mode) throws Exception {
        account = accountName;
        password = passwordOfAccount;
        databaseInit();
        if (succeedLoginIn()) {
            System.out.println("登录成功！");
            JOptionPane.showMessageDialog(null, "登录成功！欢迎你，" + account + "，即将进入游戏！");
            stmt.close();
            c.close();
            LoginMenu.needClose = true;
            if (mode == 1)
                new OnePersonMode(account, false);
            if (mode == 2)
                new MultiPersonMode(account);
        }
        else {
            System.out.println("登录失败，检查账号或密码。");
            JOptionPane.showMessageDialog(null, "登录失败，检查账号或密码。");
            stmt.close();
            c.close();

        }

    }

    // 数据库的连接
    private void databaseInit() throws Exception {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:src/data/users.db");
        // name String(80), password String(80)
        c.setAutoCommit(true);
        System.out.println("Database Connected Successfully");
        stmt = c.createStatement();
    }


    // 判断账号密码是否正确
    private boolean succeedLoginIn() throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT * FROM id");
        while (rs.next()) {
            if (rs.getString("name").equals(account)) {
                if (stringToMD5(password).equals(rs.getString("password")))
                    return true;
            }
        }
        return false;
    }

    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(" md5 转换失败");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }



}

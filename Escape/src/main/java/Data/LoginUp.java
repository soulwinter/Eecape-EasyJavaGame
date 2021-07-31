package Data;

/**
  * @ProjectName:    Escape
  * @ClassName:      LoginUp
  * @Description:    注册类
  * @Author:         Han Chubo
  */


import Game.RoleAchievement;
import UI.LoginMenu;
import UI.MultiPersonMode;
import UI.OnePersonMode;

import javax.swing.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.*;
import java.util.ArrayList;

public class LoginUp {
    private String account;
    private String password;

    Connection c = null;
    Statement stmt = null;
    public LoginUp(String accountName, String passwordOfAccount, int mode) throws Exception {
        account = accountName;
        password = passwordOfAccount;
        databaseInit();
        // recreateDatabase();

        if (account.length()==0 || accountName.length()>=79 || password.length()==0 || password.length()>79){
            System.out.println("注册失败");
            JOptionPane.showMessageDialog(null, "注册失败，请输入正确的用户名或密码。");
            stmt.close();
            c.close();
        }

        // 如果账号重复
        if (accountExists(account))
        {
            System.out.println("注册失败");
            JOptionPane.showMessageDialog(null, "注册失败，账号已存在。");
            stmt.close();
            c.close();

        }

        // 如果账号未被注册
        if (!accountExists(account)) {
            insertAccount();
            System.out.println("注册成功！");
            LoginMenu.needClose = true;
            ArrayList<Double> temp = new ArrayList<>();
            new RoleAchievement(temp, account).write();
            JOptionPane.showMessageDialog(null, "注册成功！即将进入游戏！");
            stmt.close();
            c.close();
            if (mode == 1)
                new OnePersonMode(account, true);
            if (mode == 2)
                new MultiPersonMode(account);

            // 注册成功页面
            // 进入游戏页面
        }


    }
    private void databaseInit() throws Exception {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:src/data/users.db");
        // name String(80), password String(80)
        c.setAutoCommit(true);
        System.out.println("Database Connected Successfully");
        stmt = c.createStatement();
    }

    private void recreateDatabase() throws Exception {
        stmt.executeUpdate("DROP table IF EXISTS id");
        String sql = "CREATE TABLE id(name varchar(80), password varchar(80))"; // TEXT 是否也是一种数据格式？
        stmt.executeUpdate(sql);
    }

    private void insertAccount() {
        String sql = "INSERT INTO id VALUES('" + account + "','" + stringToMD5(password) + "');";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "注册可能发生错误，正在重试中……");
        }
    }

    private boolean accountExists(String accountName) throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT * FROM ID");
        while (rs.next()) {
            if (rs.getString("name").equals(accountName))
                return true;
        }
        return false;
    }

    public static String stringToMD5(String plainText) { // MD5 加密算法
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

}

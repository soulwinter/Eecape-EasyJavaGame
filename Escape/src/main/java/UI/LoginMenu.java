package UI;

/**
  * @ProjectName:    Escape
  * @ClassName:      LoginMenu
  * @Description:    登录页面
  * @Author:         Han Chubo
  */


import Data.LoginIn;
import Data.LoginUp;
import Music.Music;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginMenu extends JFrame {

    public static boolean needClose = false;
    static Thread closeThread;

    public LoginMenu(int mode) {
        setTitle("Escape! 登录");
        // setSize(Constant.loginWIDTH, Constant.loginHEIGHT);
        setBounds(300,300,Constant.loginWIDTH, Constant.loginHEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        DrawPanel loginMenuPanel = new DrawPanel();
        setContentPane(loginMenuPanel);
        loginMenuPanel.setLayout(null);

        loginMenuPanel.setBackground(new Color(0xF6DCD1));

        // 绘制标题（暂时），TODO: 将标题用贴图替换
        JLabel title = new JLabel("使用 Escape! 账号登录...",JLabel.CENTER);
        title.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,30));
        title.setForeground(new Color(0x61473F));
        title.setBounds(30,50,Constant.loginWIDTH-30*2,100);
        loginMenuPanel.add(title);

        // 绘制 输入框
        MyTextField account = new MyTextField("用户名...",11);
        // account.setEditable(true);
        account.setBounds(60,200,Constant.loginWIDTH-60*2,40);
        account.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,15));
        loginMenuPanel.add(account);

        JPasswordField password = new JPasswordField();
        password.setBounds(60,260,Constant.loginWIDTH-60*2,40);
        loginMenuPanel.add(password);

        // 注册按钮
        JButton loginUpButton = new JButton("注册");
        loginUpButton.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,20));
        loginUpButton.setForeground(new Color(0xE68968));
        loginUpButton.setBounds(150,320,Constant.loginWIDTH-150*2,50);
        // 给按钮设置监听事件
        loginUpButton.addActionListener(e -> {
//            new OnePersonMode();
            try {
//                System.out.println("账号：" + account.getText());
//                System.out.println("密码：" + String.valueOf(password.getPassword()));
                new Music(false).play("src/sources/buttonClick.mp3");
                new LoginUp(account.getText(),String.valueOf(password.getPassword()),mode);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
//            dispose();
        });
        loginMenuPanel.add(loginUpButton);

        JButton LoginInButton = new JButton("登录");
        LoginInButton.setBounds(150,390,Constant.loginWIDTH-150*2,50);
        LoginInButton.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,20));
        LoginInButton.setForeground(new Color(0xE68968));
        // 给按钮设置监听事件
        LoginInButton.addActionListener(e -> {
//            new OnePersonMode();
            try {
//                System.out.println("账号：" + account.getText());
//                System.out.println("密码：" + String.valueOf(password.getPassword()));
                new Music(false).play("src/sources/buttonClick.mp3");
                new LoginIn(account.getText(),String.valueOf(password.getPassword()),mode);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
//            dispose();
        });

        loginMenuPanel.add(LoginInButton);


        closeThread = new Thread(() -> {
            while (true) {
                if (needClose)
                    dispose();
                try {
                    Thread.sleep(15);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
       closeThread.start();

        setVisible(true);
        LoginInButton.setFocusable(true);
        LoginInButton.requestFocus(); // .requestFocus() 需要放到 setVisible() 之后
    }


    private class DrawPanel extends JPanel {

    }
}



package UI;

/**
  * @ProjectName:    Escape
  * @ClassName:      Failed
  * @Description:    失败页面的绘图
  * @Author:         Han Chubo
  */


import Game.RoleAchievement;
import HandlePDF.CreatePDF;
import Music.Music;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.DocumentException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Failed extends JFrame {
    double livedTime;
    public Failed(double liveTime, String name) {
        livedTime = liveTime;

//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<Enemy>>(){}.getType();
//        EnemyReceived = gson.fromJson(getData(), type);


        // 读取战绩并写入新战绩
        RoleAchievement temp1 = null;
        ArrayList<Double> temp2;
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<RoleAchievement>(){}.getType();
        try {
            temp1 = gson.fromJson(new RoleAchievement(name).read(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        temp2 = temp1.getScore();
        temp2.add(livedTime);
        try {
            new RoleAchievement(temp2,name).write();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        setTitle("Escape!");
        setBounds(200,100,Constant.gWIDTH, Constant.gHEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawPanel failedMenu = new DrawPanel();
        setContentPane(failedMenu);

        failedMenu.setLayout(null);

        failedMenu.setBackground(new Color(0xF6DCD1));

        // 绘制标题（暂时），TODO: 将标题用贴图替换
        JLabel title = new JLabel(Double.toString(livedTime),JLabel.CENTER);
        title.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,150));
        title.setForeground(new Color(0x61473F));
        title.setBounds(0,100,Constant.gWIDTH,150);
        failedMenu.add(title);

        // 绘制按钮
        JButton againButton = new JButton("再来一次");
        againButton.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,20));
        againButton.setForeground(new Color(0xE68968));
        againButton.setBounds(Constant.gWIDTH/2-100,Constant.gHEIGHT/2-50,200,60);
        // 给按钮设置监听事件
        againButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Music(false).play("src/sources/buttonClick.mp3");
                new OnePersonMode(name, false);
                dispose();
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            }
        });

        failedMenu.add(againButton);


        JButton scoreChartButton = new JButton("战绩");
        scoreChartButton.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,20));
        scoreChartButton.setForeground(new Color(0xE68968));
        scoreChartButton.setBounds(Constant.gWIDTH/2-100,Constant.gHEIGHT/2+60,200,60);
        scoreChartButton.addActionListener(e -> {
            new Music(false).play("src/sources/buttonClick.mp3");
            new ScoreChart(name);
        });
        failedMenu.add(scoreChartButton);

        JButton backToMenuButton = new JButton("退出登录");
        backToMenuButton.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,20));
        backToMenuButton.setForeground(new Color(0xE68968));
        backToMenuButton.setBounds(Constant.gWIDTH/2-100,Constant.gHEIGHT/2 + 60 + 110,200,60);
        backToMenuButton.addActionListener(e -> {
            new Music(false).play("src/sources/buttonClick.mp3");
            StartMenu.bgm.stop();
            LoginMenu.needClose = false;
            new StartMenu();
            dispose();
        });
        failedMenu.add(backToMenuButton);


        if (livedTime >= 50) {
            JButton getCert = new JButton("获取游戏证书");
            getCert.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,20));
            getCert.setForeground(new Color(0xE68968));
            getCert.setBounds(Constant.gWIDTH / 2 - 100, Constant.gHEIGHT / 2 + 60 + 110 + 110, 200, 60);
            getCert.addActionListener(e -> {
                try {
                    new Music(false).play("src/sources/buttonClick.mp3");
                    new CreatePDF(livedTime, name);
                    File file = new File("Cert.pdf");
                    Desktop.getDesktop().open(file);
                } catch (DocumentException | IOException ioException) {
                    ioException.printStackTrace();
                }

            });
            failedMenu.add(getCert);
        }



        setVisible(true);
    }

    private class DrawPanel extends JPanel {

    }
}

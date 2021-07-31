package UI;

/**
  * @ProjectName:    Escape
  * @ClassName:      StartMenu
  * @Description:    开始页面的绘制
  * @Author:         Han Chubo
  */


import Game.Enemy;
import Music.Music;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class StartMenu extends JFrame{

    ArrayList<Enemy> enemies = new ArrayList<>();
    Rectangle bound;
    static Thread gameThread;
    private int timeRound = 0;
    private Random rand = new Random();
    private int enemyIndex = -1;
    static Music bgm = new Music(true);

    public StartMenu() {
        bgm.play("src/sources/Andrew Applepie - Trip To The Moon.mp3"); // 设置背景音乐
        // 绘制标题
        setTitle("Escape!");
        setSize(Constant.gWIDTH, Constant.gHEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawPanel startMenuPanel = new DrawPanel();
        setContentPane(startMenuPanel);
        startMenuPanel.setLayout(null);

        startMenuPanel.setBackground(new Color(0xF6DCD1));

        // 绘制标题
        JLabel title = new JLabel("Escape! ",JLabel.CENTER);
        title.setForeground(new Color(0x61473F));
        title.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,150));
        title.setBounds(0,100,Constant.gWIDTH,150);
        startMenuPanel.add(title);

        // 绘制各类按钮及增加监听事件
        JButton onePersonModeButton = new JButton("单人游戏");
        onePersonModeButton.setForeground(new Color(0xE68968));
        onePersonModeButton.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,20));
        onePersonModeButton.setBounds(Constant.gWIDTH/2-100,Constant.gHEIGHT/2-50,200,60);
        onePersonModeButton.addActionListener(e -> {
            new Music(false).play("src/sources/buttonClick.mp3"); // 给按钮添加音效
            new LoginMenu(1);
            dispose();
        });
        startMenuPanel.add(onePersonModeButton);

        JButton multiPersonModeButton = new JButton("多人游戏");
        multiPersonModeButton.setForeground(new Color(0xE68968));
        multiPersonModeButton.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,20));
        multiPersonModeButton.setBounds(Constant.gWIDTH/2-100,Constant.gHEIGHT/2+60,200,60);
        multiPersonModeButton.addActionListener(e -> {
            new Music(false).play("src/sources/buttonClick.mp3");
            new LoginMenu(2);
            dispose();
        });
        startMenuPanel.add(multiPersonModeButton);

        setVisible(true);


        // 设置主页面装饰
        Insets windowHeight = getInsets();

        gameThread = new Thread(() -> {
            while (true) {
                timeRound++;
                bound = getBounds(); // 获取窗口边界 确定鼠标位置
                Point p = MouseInfo.getPointerInfo().getLocation();
                double mouseX = p.getX() - bound.x;
                double mouseY = p.getY() - bound.y - windowHeight.top;


                // 添加 Enemy 类
                if (timeRound == 1) {
                    for (int i = 0; i < 5; i++) {
                        switch (rand.nextInt(4) + 1) {
                            case 1 -> enemies.add(new Enemy(Constant.gWIDTH + 50, rand.nextInt(Constant.gHEIGHT), rand.nextInt(4) + 1));
                            case 2 -> enemies.add(new Enemy(-50, rand.nextInt(700), rand.nextInt(4) + 1));
                            case 3 -> enemies.add(new Enemy(rand.nextInt(Constant.gWIDTH), Constant.gHEIGHT + 50, rand.nextInt(4) + 1));
                            case 4 -> enemies.add(new Enemy(rand.nextInt(Constant.gWIDTH), -50, rand.nextInt(4) + 1));
                        }

                    }
                }

                if (timeRound % 200 == 0) {
                    for (int i = 0; i < rand.nextInt(timeRound / 100) + 1; i++) {
                        switch (rand.nextInt(4) + 1) {
                            case 1 -> enemies.add(new Enemy(Constant.gWIDTH + 50, rand.nextInt(Constant.gHEIGHT), rand.nextInt(4) + 1));
                            case 2 -> enemies.add(new Enemy(-50, rand.nextInt(700), rand.nextInt(4) + 1));
                            case 3 -> enemies.add(new Enemy(rand.nextInt(Constant.gWIDTH), Constant.gHEIGHT + 50, rand.nextInt(4) + 1));
                            case 4 -> enemies.add(new Enemy(rand.nextInt(Constant.gWIDTH), -50, rand.nextInt(4) + 1));
                        }

                    }
                }

                // 若 Enemy 触碰在一起触发效果
                for (Enemy i : enemies) {
                    i.move(mouseX, mouseY);
                    for (Enemy j : enemies) {
                        if (i != j && i.contact(j)) {
                            enemyIndex = enemies.indexOf(j);
                            i.speed = (i.speed + j.speed) / 2 + 1;
                            break;
                        }
                    }
                }
                if (enemyIndex != -1) {
                    enemies.remove(enemyIndex);
                    enemyIndex = -1;
                }


                repaint();
                try {
                    Thread.sleep(15);
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }

        });
        gameThread.start();



    }




    private class DrawPanel extends JPanel {
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;


            // 绘制敌人
            for (Enemy i: enemies) {
                i.draw(g);
            }
        }
    }

}

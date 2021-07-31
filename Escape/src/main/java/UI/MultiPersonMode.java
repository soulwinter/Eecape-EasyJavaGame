package UI;

/**
  * @ProjectName:    Escape
  * @ClassName:      MultiPersonMode
  * @Description:    多人模式的页面及状态刷新
  * @Author:         Han Chubo
  */


import Game.Enemy;
import Game.Role;
import Music.Music;
import Server.CloseUtil;
import Server.Receive;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class MultiPersonMode extends JFrame {


    public class Send implements Runnable{
        private DataOutputStream dos;
        private boolean flag = true; // 判断线程是否正常运行

        public Send(Socket client) {
            try {
                dos = new DataOutputStream(client.getOutputStream());
            } catch (IOException e) {
                flag = false;
                CloseUtil.closeAll(dos, client);
            }
        }


        private void send(String str) { // 发送字符串
            try {
                dos.writeUTF(str);
                dos.flush();
            } catch (IOException e) {
                flag = false;
                CloseUtil.closeAll(dos);
            }
        }

        @Override
        public void run() // 发送线程
        {
            while (flag) {
                Gson gson = new Gson();
                String dataToSend = gson.toJson(player);
                this.send(dataToSend);
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    Insets windowHeight;

    static Thread gameThread;
    Rectangle bound;


    private int deathTime = 0;
    private Date currentTime = new Date();
    private double immuneTimer = currentTime.getTime();
    private double immuneStatus = 0;

    private Random rand = new Random();

    double mouseX,mouseY;

    public static ArrayList<Enemy> enemies = new ArrayList<>();
    private int enemyNum = 0;
    public Role player;

    public MultiPersonMode(String nameOfRole) throws IOException {
        setVisible(true);
        windowHeight = getInsets();

        setTitle("Escape! " + nameOfRole);
        setSize(Constant.gWIDTH, Constant.gHEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawPanel multiPersonModePanel = new DrawPanel();
        setResizable(false);
        setContentPane(multiPersonModePanel);

        player = new Role(rand.nextInt(Constant.gWIDTH), rand.nextInt(Constant.gHEIGHT), 7,nameOfRole);

        Socket client = new Socket("localhost",9999);

        Receive receive = new Receive(client);
        Send send = new Send(client);

        new Thread(receive).start();
        new Thread(send).start();

        gameThread = new Thread(() -> {
                while (true) {
                    bound = getBounds(); // 获取窗口边界 确定鼠标位置
                    Point p = MouseInfo.getPointerInfo().getLocation();
                    mouseX = p.getX() - bound.x;
                    mouseY = p.getY() - bound.y - windowHeight.top;
                    player.move(mouseX, mouseY); // 角色向鼠标移动

                    enemies = receive.getEnemyReceived();
                    if (enemyNum > enemies.size())
                        new Music(false).play("src/sources/enemyContact.mp3"); // 若监测到合并，则播放音乐
                    enemyNum = enemies.size();


                    for (Enemy i:enemies) {
                        if (player.contactEnemy(i) && new Date().getTime() > immuneTimer + 5000) { // 如果不在无敌时间且接触到敌人则死亡数+1
                            new Music(false).play("src/sources/failed.mp3");
                            deathTime++;
                            currentTime = new Date();
                            immuneTimer = currentTime.getTime();
                        }
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

            setBackground(new Color(0xF6DCD1));

            for (Enemy i: enemies) {
                i.draw(g);
            }

            player.draw(g);

            immuneStatus = ( 5000 - new Date().getTime() + immuneTimer) / 1000;
            if (immuneStatus > 0) {
                g2.setColor(new Color(0x9CB0B0B1, true));
                g2.setFont(new Font("FZBiaoZhiS-R-GB", Font.BOLD, 300));
                g2.drawString(String.format("%.1f", ((5000 - new Date().getTime() + immuneTimer) / 1000)), 50, Constant.gHEIGHT - 100);
                g2.setFont(new Font("FZBiaoZhiS-R-GB", Font.BOLD, 50));
                g2.drawString("复活无敌时间！", 50, Constant.gHEIGHT - 350);
            }

            g2.setColor(new Color(0x7A000000, true));
            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,100));
            g2.drawString(Integer.toString(deathTime),50,130);

            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,30));
            g2.drawString("死亡数",50,35);

        }
    }

}

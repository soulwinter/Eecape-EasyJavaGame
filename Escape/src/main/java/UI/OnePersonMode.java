package UI;

/**
  * @ProjectName:    Escape
  * @ClassName:      OnePersonMode
  * @Description:    单人模式的绘制、刷新
  * @Author:         Han Chubo
  */


import Game.Enemy;
import Game.Role;
import Game.Skill;
import Music.Music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Random;

import java.util.ArrayList;


public class OnePersonMode extends JFrame{

    double mouseX,mouseY; // 用于获取鼠标的位置
    Insets windowHeight; // 用于获取窗口状态栏高度

    static Thread gameThread;
    Rectangle bound; // 用于获取窗口状态栏高度

    int enemyIndex = -1; // 用于在碰撞时删除 Enemy 的 Index；防止异常
    int skillIndex = -1;
    private int timeRound = 0;
    private int timeSkillRound = 500;
    private int temp = 0;
    private boolean firstPlayer;

    Date startTime = new Date();
    Date currentTime;

    private Random rand = new Random();

    Role player;
    ArrayList<Enemy> enemies = new ArrayList<>(); // 定义敌人 Enemy ArrayList
    ArrayList<Skill> skills = new ArrayList<>();


    boolean failed = false;

    public OnePersonMode(String nameOfRole, boolean firstOpen) {

        player = new Role((double) Constant.gWIDTH/2, (double) Constant.gHEIGHT/2, 7,nameOfRole);
        firstPlayer = firstOpen;

        setVisible(true);
        windowHeight = getInsets(); // 获取窗口标题栏高度

        setTitle("Escape!");
        setSize(Constant.gWIDTH, Constant.gHEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawPanel onePersonModePanel = new DrawPanel();
        setResizable(false);
        setContentPane(onePersonModePanel);

        if (firstOpen) {
            player.getSkill(-2);
            timeSkillRound = 2400;
            startTime.setTime(new Date().getTime() + 45000);
        }




        // 获取鼠标位置
        // 刷新
        gameThread = new Thread(() -> {
            while (!failed) {
                bound = getBounds(); // 获取窗口边界 确定鼠标位置
                Point p = MouseInfo.getPointerInfo().getLocation();
                mouseX = p.getX() - bound.x;
                mouseY = p.getY() - bound.y - windowHeight.top;
                player.move(mouseX, mouseY);
                timeRound += 1;
                // 时间
                currentTime = new Date();

                // 技能
                for (Skill i: skills) {
                    if (player.contactSkill(i)) { // 如果接收到技能则获得技能
                        player.getSkill(i.skillKind);
                        skillIndex = skills.indexOf(i);
                    }
                }
                if (skillIndex != -1) {
                    skills.remove(skillIndex); // 接收到技能，技能消失
                    skillIndex = -1;
                }

                player.useSkill();

                // 生成敌人
                if (timeRound % 200 == 0) {
                    for (int i=0; i<rand.nextInt(timeRound/200)+1; i++) {
                        switch (rand.nextInt(4)+1) { // 从四面随机生成敌人
                            case 1 -> enemies.add(new Enemy(Constant.gWIDTH+50, rand.nextInt(Constant.gHEIGHT), rand.nextInt(4) + 1));
                            case 2 -> enemies.add(new Enemy(-50, rand.nextInt(700), rand.nextInt(4) + 1));
                            case 3 -> enemies.add(new Enemy(rand.nextInt(Constant.gWIDTH), Constant.gHEIGHT+50, rand.nextInt(4) + 1));
                            case 4 -> enemies.add(new Enemy(rand.nextInt(Constant.gWIDTH), -50, rand.nextInt(4) + 1));
                        }

                    }
                }

                // 随机生成敌人
                if (timeRound > timeSkillRound) {
                    new Music(false).play("src/sources/skillHappen.mp3");
                    skills.add(new Skill(rand.nextInt(2)+1,rand.nextInt(Constant.gWIDTH),rand.nextInt(Constant.gHEIGHT)));
                    timeSkillRound = timeRound + rand.nextInt(500);
                }



                // 遇到两个重叠则删除一个，并加速另外一个
                for (Enemy i:enemies) {
                    i.chase(player);
                    for (Enemy j:enemies) {
                        if (i != j && i.contact(j)) {
                            enemyIndex = enemies.indexOf(j);
                            i.speed = (i.speed + j.speed)/2 + 1;
                            break;
                        }
                    }
                }
                if (enemyIndex != -1)
                {
                    new Music(false).play("src/sources/enemyContact.mp3");
                    enemies.remove(enemyIndex);
                    enemyIndex = -1;
                }

                // 死亡判定
                for (Enemy i:enemies) {
                    if (player.contactEnemy(i) && !player.immune) {
                        new Music(false).play("src/sources/failed.mp3");
                        player.time.stop();
                        new Failed(((double)(currentTime.getTime()-startTime.getTime())/1000),nameOfRole);
                        dispose();
                        failed = true;
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

        KeyListenerTest();

    }




    private class DrawPanel extends JPanel {
        public void paint(Graphics g)
        {
            setBackground(new Color(0xF6DCD1));
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;

            // 绘制玩家
            player.draw(g);
            // 绘制敌人
            for (Enemy i: enemies) {
                i.draw(g);
            }
            // 绘制技能
            for (Skill i: skills) {
                i.draw(g);
            }

            g2.setColor(new Color(0x7A000000, true));
            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,100));
            g2.drawString(Double.toString(((double)(currentTime.getTime()-startTime.getTime())/1000)),50,100);

            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,25));
            g2.drawString("可用无敌技能次数",Constant.gWIDTH-220,Constant.gHEIGHT-160);
            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,80));
            g2.drawString(Integer.toString(player.immuneTime),Constant.gWIDTH-80,Constant.gHEIGHT-70);

            g2.setColor(new Color(0x99000000, true));
            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,30));
            if (timeRound < Constant.forFirstPlayers.size()*260 && firstPlayer)
                g2.drawString(Constant.forFirstPlayers.get(timeRound/260),60,150);



        }
    }

    public void KeyListenerTest() {
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_SPACE -> {
                        if (player.immuneTime > 0) {
                            new Music(false).play("src/sources/getImmune.mp3");
                            player.getSkill(0);
                            player.immuneTime--;
                        }
                    }
                }
                repaint();
            }
        });
    }



}

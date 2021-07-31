package Game;

/**
  * @ProjectName:    Escape
  * @ClassName:      Role
  * @Description:    游戏界面，用于生成玩家
  * @Author:         Han Chubo
  */


import Music.Music;
import UI.Constant;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Date;

public class Role {
    public double posX,posY;
    public transient double speed;
    private transient double skillTimer = -1;
    private transient int gotSkillKind = -1;
    private transient double preSpeed;
    private transient int size = 35;
    public transient int immuneTime = 2;
    public transient boolean immune = false;

    public transient Music time = new Music(false);

    public String name;

    public Role(double x, double y,double previousSpeed, String nameOfRole) {
        posX = x;
        posY = y;
        speed = previousSpeed;
        preSpeed = previousSpeed;
        name = nameOfRole;
    }

    public void move(double x, double y) // 移动到目标位置
    {
        double totalDistance = Math.sqrt(Math.pow(x-posX, 2) + Math.pow(y-posY,2));
        double needTime = totalDistance / speed;
        posX -= (posX-x)/needTime;
        posY -= (posY-y)/needTime;
    }

    public boolean contactEnemy(Enemy theOtherEnemy) // 返回是否与 Enemy 接触
    {
        Shape drawPlayer = new Ellipse2D.Double(posX, posY, size,size);
        if (drawPlayer.intersects(theOtherEnemy.posX, theOtherEnemy.posY, 20, 20))
            return true;
        else
            return false;

    }

    public boolean contactSkill(Skill touchedSkill) // 返回是否与 Skill 接触
    {
        Shape drawPlayer = new Ellipse2D.Double(posX, posY, size,size);
        if (drawPlayer.intersects(touchedSkill.posX, touchedSkill.posY, 30, 30))
            return true;
        else
            return false;
    }

    public void getSkill(int kind) { // 获得技能后对 Role 属性改变

        initRole();
        gotSkillKind = kind;
        skillTimer = new Date().getTime() + 5*1000;
        if (kind == -2)
            skillTimer = new Date().getTime() + 45*1000;
        new Music(false).play("src/sources/getSkill.mp3");
        time.play("src/sources/timer.mp3");
    }

    public void useSkill() { // 获得技能后对 Role 属性改变
        switch (gotSkillKind) {
            // -2 新手无敌
            // -1 无状态
            // 0 无敌状态
            // 1 加速技能
            // 2 缩小技能
            case -2, 0 -> {
                if (new Date().getTime() - skillTimer < 0 && skillTimer != -1) {
                    immune = true;
                }
                if (new Date().getTime() - skillTimer > 0) {
                    skillTimer = -1;
                    gotSkillKind = -1;
                    initRole();
                }
            }

            case 1 -> {
                if (new Date().getTime() - skillTimer < 0 && skillTimer != -1) {
                    speed = 15;
                }
                if (new Date().getTime() - skillTimer > 0) {
                    skillTimer = -1;
                    gotSkillKind = -1;
                    initRole();
                }
            }
            case 2 -> {
                if (new Date().getTime() - skillTimer < 0 && skillTimer != -1) {
                    size = 20;
                }
                if (new Date().getTime() - skillTimer > 0) {
                    skillTimer = -1;
                    gotSkillKind = -1;
                    initRole();
                }
            }


        }
    }

    public void initRole() { // 初始化 player，用于对每次技能使用完毕返回原始值
        immune = false;
        size = 35;
        speed = preSpeed;
        time.stop();
    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0x2D2525));
        Shape drawPlayer = new Ellipse2D.Double(posX, posY, size,size);
        g2.fill(drawPlayer);

        if (gotSkillKind == 1) {
            g2.setColor(new Color(0x9CB0B0B1, true));
            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,300));
            g2.drawString(String.format("%.1f",((skillTimer - new Date().getTime())/1000)), 50,Constant.gHEIGHT-100);
            g2.setFont(new Font("FZBiaoZhiS-R-GB", Font.BOLD, 50));
            g2.drawString("获得加速技能！", 50, Constant.gHEIGHT - 350);
        }

        if (gotSkillKind == 2) {
            g2.setColor(new Color(0x9CB0B0B1, true));
            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,300));
            g2.drawString(String.format("%.1f",((skillTimer - new Date().getTime())/1000)), 50,Constant.gHEIGHT-100);
            g2.setFont(new Font("FZBiaoZhiS-R-GB", Font.BOLD, 50));
            g2.drawString("获得缩小技能！", 50, Constant.gHEIGHT - 350);
        }

        if (gotSkillKind == 0 && ((skillTimer - new Date().getTime())/1000)>0) {
            g2.setColor(new Color(0x9CB0B0B1, true));
            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,300));
            g2.drawString(String.format("%.1f",((skillTimer - new Date().getTime())/1000)), 50,Constant.gHEIGHT-100);
            g2.setFont(new Font("FZBiaoZhiS-R-GB", Font.BOLD, 50));
            g2.drawString("启动无敌技能！", 50, Constant.gHEIGHT - 350);
        }

        if (gotSkillKind == -2) {
            g2.setColor(new Color(0x9CB0B0B1, true));
            g2.setFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,300));
            g2.drawString(String.format("%.1f",((skillTimer - new Date().getTime())/1000)), 50,Constant.gHEIGHT-100);
            g2.setFont(new Font("FZBiaoZhiS-R-GB", Font.BOLD, 50));
            g2.drawString("新手无敌保护！", 50, Constant.gHEIGHT - 350);
        }

    }

}

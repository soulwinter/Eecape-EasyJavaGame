package Game;

/**
  * @ProjectName:    Escape
  * @ClassName:      Enemy
  * @Description:    游戏界面，Enemy 类，用于生成敌人
  * @Author:         Han Chubo
  */


import java.awt.*;
import java.awt.geom.Ellipse2D;

import java.util.Random;

public class Enemy{


    public double posX,posY;
    public transient double speed;
    transient Random  rand = new Random();

    public Enemy(double x, double y, int previousSpeed) {
        posX = x;
        posY = y;
        speed = previousSpeed;

    }

    public void chase(Role player) {
        move(player.posX, player.posY);
    }

    public void move(double x, double y) // 移动到目标位置
    {
        double totalDistance = Math.sqrt(Math.pow(x-posX, 2) + Math.pow(y-posY,2));
        double needTime = totalDistance / speed;
        posX -= (posX-x)/needTime;
        posY -= (posY-y)/needTime;
    }


    public boolean contact(Enemy theOtherEnemy) // 返回是否与与其他 Enemy 接触
    {
        Shape drawEnemy = new Ellipse2D.Double(posX, posY, 20,20);
        if (drawEnemy.intersects(theOtherEnemy.posX, theOtherEnemy.posY, 20, 20))
            return true;
        else
            return false;

    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(210, 0, 67));
        Shape drawEnemy = new Ellipse2D.Double(posX, posY, 20,20);
        g2.fill(drawEnemy);

    }

}

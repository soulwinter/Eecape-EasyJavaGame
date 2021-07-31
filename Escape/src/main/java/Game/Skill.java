package Game;

/**
  * @ProjectName:    Escape
  * @ClassName:      Skill
  * @Description:    用于生成 Skill
  * @Author:         Han Chubo
  */


import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Skill {
    public int skillKind;
    public int posX;
    public int posY;

    public Skill(int kind, int x, int y) {
        skillKind = kind;
        posX = x;
        posY = y;
    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        switch (skillKind) {
            case 1 -> g2.setColor(new Color(0, 174, 255));
            case 2 -> g2.setColor(new Color(0x067B06));
            case 3 -> g2.setColor(new Color(0xEE602E));
        }

        Shape drawSkill = new Ellipse2D.Double(posX,posY,30,30);
        g2.fill(drawSkill);

    }
}

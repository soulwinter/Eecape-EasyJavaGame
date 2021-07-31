package UI;

/**
  * @ProjectName:    Escape
  * @ClassName:      Constant
  * @Description:    常量，用于储存一些在画图时需要用到的常量
  * @Author:         Han Chubo
  */


import java.util.ArrayList;

public class Constant {
    public static final int gWIDTH = 1500;
    public static final int gHEIGHT = 1000;

    public static final int loginWIDTH = 500;
    public static final int loginHEIGHT = 800;

    public static final ArrayList<String> forFirstPlayers = new ArrayList<>();
    static {
        forFirstPlayers.add("欢迎来到 Escape! 作为你的新朋友，我来为你讲解一下游戏的基本玩法吧。");
        forFirstPlayers.add("别怕！我为你开启了新手 45 秒无敌保护时间，可以认真听我说话了！");
        forFirstPlayers.add("在游戏中，你扮演一个黑色小球，它始终跟随你的鼠标运动。");
        forFirstPlayers.add("而你要做的，就是躲开所有红色小球的追击，并且时间越长越好。");
        forFirstPlayers.add("小球会越来越多，但是当它们互相接触时，会合二为一，新小球平均了两个原来小球的速度，并有微微增益。");
        forFirstPlayers.add("正如你看到的，你拥有两次启动无敌技能 5 秒的机会。");
        forFirstPlayers.add("无敌通过按下空格键启动！但是请不要现在按下，否则将会失去新手保护。");
        forFirstPlayers.add("同时，游戏中也会随机刷新技能标志（通常是带有颜色的圆形），当你碰到它们时，你就会获得相应技能。");
        forFirstPlayers.add("请注意：技能属性不能叠加。");
        forFirstPlayers.add("说完了！开始 Escape 吧！祝你游戏愉快！");
    }

}

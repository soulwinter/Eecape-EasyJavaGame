package Server;

/**
  * @ProjectName:    Escape
  * @ClassName:      Update
  * @Description:    服务器端用于更新游戏 Player，Enemy 位置
  * @Author:         Han Chubo
  */


import Game.Enemy;
import UI.Constant;

import java.util.Random;

public class Update implements Runnable{
    private int timeRound = 0;
    private Random rand = new Random();
    int enemyIndex = -1;


    @Override
    public void run() {
        while (true) {
            ServerUI.changed = true;
            timeRound++;

            if (timeRound % 300 == 0 && ServerUI.playersInServer.size() < 30) {
                for (int i=0; i < rand.nextInt(timeRound/200)+1; i++) {
                    switch (rand.nextInt(4)+1) {
                        case 1 -> ServerUI.enemiesInServer.add(new Enemy(Constant.gWIDTH+50, rand.nextInt(Constant.gHEIGHT), rand.nextInt(4) + 1));
                        case 2 -> ServerUI.enemiesInServer.add(new Enemy(-50, rand.nextInt(700), rand.nextInt(4) + 1));
                        case 3 -> ServerUI.enemiesInServer.add(new Enemy(rand.nextInt(Constant.gWIDTH), Constant.gHEIGHT+50, rand.nextInt(4) + 1));
                        case 4 -> ServerUI.enemiesInServer.add(new Enemy(rand.nextInt(Constant.gWIDTH), -50, rand.nextInt(4) + 1));
                    }
                    System.out.println("Update 运行正常");
                    // ServerUI.enemiesInServer.add(new Enemy(rand.nextInt(Constant.gWIDTH), rand.nextInt(Constant.gHEIGHT), rand.nextInt(4) + 1));
                }
            }

            if (ServerUI.playersInServer.size() >= 30 && timeRound % 300 == 0)
                for (int i = 0; i < 5; i++)
                    ServerUI.playersInServer.remove(i);


            for (Enemy i:ServerUI.enemiesInServer) {
                int enemyIndex = ServerUI.enemiesInServer.indexOf(i);
                if (ServerUI.playersInServer.size()!=0){
                    // i.chase(ServerUI.playersInServer.get(0));
                    i.chase(ServerUI.playersInServer.get(enemyIndex % ServerUI.playersInServer.size()));
                } else
                    i.move(0,0);

            }

            for (Enemy i: ServerUI.enemiesInServer) {
                for (Enemy j: ServerUI.enemiesInServer) {
                    if (i != j && i.contact(j)) {
                        enemyIndex = ServerUI.enemiesInServer.indexOf(j);
                        i.speed = (i.speed + j.speed)/2 + 1;
                        break;
                    }
                }
            }
            if (enemyIndex != -1) {
                ServerUI.enemiesInServer.remove(enemyIndex);
                enemyIndex = -1;
            }
            try {
                Thread.sleep(15);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

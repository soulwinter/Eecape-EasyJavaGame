package Server;

/**
  * @ProjectName:    Escape
  * @ClassName:      Receive
  * @Description:    用于客户端收数据并处理
  * @Author:         Han Chubo
  */


import Game.Enemy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;


public class Receive implements Runnable{

    private DataInputStream dis;
    private boolean flag = true;
    private String temp;
    private ArrayList<Enemy> EnemyReceived = new ArrayList<>();

    private int enemySize = 0;

    private int ranTime = 0;

    public Receive(Socket client) {
        try {
            dis = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            flag  = false;
            CloseUtil.closeAll(dis, client);
        }
    }

    private String getData() {
        try {
            temp = dis.readUTF();
            // System.out.println("Debug in [Receive]: 收到了信息: " + temp);
        } catch (Exception e) {
            e.printStackTrace();
            CloseUtil.closeAll(dis);
            flag = false;
        }
        return temp;
    }

    @Override
    public void run() {
        while (flag) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Enemy>>(){}.getType();
            try {
                EnemyReceived = gson.fromJson(getData(), type);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("接收数据出错");
            }

        }
    }

    public ArrayList<Enemy> getEnemyReceived() {
//
        return EnemyReceived;
    }

}

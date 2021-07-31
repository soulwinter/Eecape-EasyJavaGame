package Server;

/**
  * @ProjectName:    Escape
  * @ClassName:      MyChannel
  * @Description:    Server 端用于多线程与多台 Client 通信，收发数据
  * @Author:         Han Chubo
  */

import Game.Enemy;
import Game.Role;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyChannel implements Runnable {
    private DataOutputStream dos;
    private DataInputStream dis;
    private boolean flag = true;
    private String enemyToSend;
    private Role playerReceived;
    private ArrayList<Enemy> enemyToJSON;

    public MyChannel(Socket client) {
        try {
            dis = new DataInputStream(client.getInputStream()); // 发送数据流
            dos = new DataOutputStream(client.getOutputStream()); // 接受数据流
        } catch (IOException e) {
            System.out.println("[Error] 接收 Stream 数据出错");
//            e.printStackTrace();
            CloseUtil.closeAll(dos, dis);
            ServerUI.clients--;
            flag = false;
        }
    }

    private void send(String dataToSend) {
        if (dataToSend != null && dataToSend.length() != 0) {
            try {
//                System.out.println("发送数据：" + dataToSend);
                dos.writeUTF(dataToSend);
                dos.flush();
            } catch (IOException e) {
                flag = false;
                CloseUtil.closeAll(dis, dos);
//                e.printStackTrace();
                ServerUI.list.remove(this);
                ServerUI.clients--;
            }
        }
    }

    // 先写服务器发送服务器直接生成的数据
    private void Broadcast() {
        List<MyChannel> list = ServerUI.list;
        for (MyChannel other: list) {
            other.send(enemyToSend);
        }
    }

    private String receive() {
        String kindReceived;
        String temp = "";
        try {
            temp = dis.readUTF();
//            System.out.println("Debug in [MyChannel]: 收到 Player 信息=" + ":" + temp);
        } catch (IOException e) {
            flag = false;
            CloseUtil.closeAll(dis, dos);
            ServerUI.list.remove(this);
            ServerUI.clients--;
        }
        return temp;
    }

    private void convert() { // 将 ArrayList Enemy 转为 JSON，准备传输
        Gson gson = new Gson();
        enemyToJSON = (ArrayList<Enemy>) ServerUI.enemiesInServer.clone();
        enemyToSend = gson.toJson(enemyToJSON);
//        enemyToSend = JSON.toJSONString(ServerUI.enemiesInServer);
//        System.out.println("Debug in [MyChannel]: 已经转为 JSON.");

    }

    private void convertRole() { // 将收到的角色 JSON 文件转为 ArrayList 对象，并更新 ServerUI.playersInServer
        Gson gson = new Gson();
        Type type = new TypeToken<Role>(){}.getType();
        playerReceived = gson.fromJson(receive(), type);
        if (playerReceived != null)
            if (!ServerUI.playersName.contains(playerReceived.name)) {
                ServerUI.playersName.add(playerReceived.name);
                ServerUI.playersInServer.add(playerReceived);
            } else {
                ServerUI.playersInServer.set(ServerUI.playersName.indexOf(playerReceived.name), playerReceived);
            }

    }

    @Override
    public void run() {
        while (flag) {
            convertRole();
//            receive();

            if (ServerUI.changed) {
                convert();
                Broadcast();
                ServerUI.changed = false;
            } else {
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

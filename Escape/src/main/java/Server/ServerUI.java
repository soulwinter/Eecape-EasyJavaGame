package Server;

/**
  * @ProjectName:    Escape
  * @ClassName:      ServerUI
  * @Description:    服务器主体，以及简单 UI
  * @Author:         Han Chubo
  */


import Game.Enemy;
import Game.Role;
import UI.Constant;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerUI extends JFrame {




    static Thread gameThread;

    public static List<MyChannel> list = new ArrayList<>();
    public static ArrayList<Enemy> enemiesInServer = new ArrayList<>();
    public static ArrayList<Role> playersInServer = new ArrayList<>();
    public static ArrayList<String > playersName = new ArrayList<>();

    public static boolean changed = false;

    public static int clients = 0;

    public ServerUI() throws IOException {
        setTitle("Escape! Server");
        setSize(Constant.gWIDTH, Constant.gHEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawPanel serverPanel = new DrawPanel();
        setContentPane(serverPanel);
        serverPanel.setLayout(null);
        setVisible(true);



        for (int i=0; i < 10; i++) {
            enemiesInServer.add(new Enemy(i*200,800,1));
        }

        System.out.println("<<<< Escape! Server RUNNING >>>>");
        ServerSocket server = new ServerSocket(9999);

        Update gameUpdate = new Update();
        new Thread(gameUpdate).start();

        gameThread = new Thread(() -> {
            while (true) {
                repaint();
                try {
                    Thread.sleep(15);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.start();

        while (true) {
            Socket socket = server.accept();
            System.out.println("收到客户端连接...");
            clients++;

            MyChannel channel = new MyChannel(socket);
            list.add(channel);
            new Thread(channel).start();
        }
    }

    public static void main(String[] args) throws IOException {
        new ServerUI();
    }

    private class DrawPanel extends JPanel {
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;

//            for (Enemy i: enemiesInServer) {
//                i.draw(g);
//            }
            for (int i=0;i<enemiesInServer.size();i++)
            {
                enemiesInServer.get(i).draw(g);
            }
            for (Role i: playersInServer) {
                i.draw(g);
            }
        }

    }

}

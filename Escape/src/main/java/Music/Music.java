package Music;

/**
  * @ProjectName:    Escape
  * @ClassName:      Music
  * @Description:    用于播放、停止音乐、音效等
  * @Author:         Han Chubo
  */


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;

public class Music {
    FileInputStream FIS;
    BufferedInputStream BIS;
    public Player player;
    public long totalLength,pauseLocation;

    public String fileLocation;
    public boolean needLoop;

    public Music(boolean loop) {
        needLoop = loop;
    }

    public void stop() {
        if (player != null)
        {
            player.close();
        }
    }

    public void play(String path)  {
        try {
            FIS = new FileInputStream(path);
        } catch (FileNotFoundException e) {

        }
        BIS = new BufferedInputStream(FIS);
        try {
            player = new Player(BIS);
        } catch (JavaLayerException e) {

        }
        try {
            totalLength = FIS.available();
        } catch (IOException e) {

        }
        fileLocation = path + "";
        new Thread()
        {
            @Override
            public void run() {
                try {
                    player.play();
                    if (player.isComplete() && needLoop) {
                        play(fileLocation);
                    }

                } catch (JavaLayerException e) {

                }
            }
        }.start();

    }

    public void pause() throws IOException {
        if (player != null) {
            pauseLocation = FIS.available();
            player.close();
        }
    }

    public void resume() throws JavaLayerException, IOException {
        FIS = new FileInputStream(fileLocation);
        BIS = new BufferedInputStream(FIS);
        player = new Player(BIS);

        FIS.skip(totalLength - pauseLocation);
        new Thread()
        {
            @Override
            public void run() {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }






}

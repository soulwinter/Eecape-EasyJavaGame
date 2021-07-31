package Game;

/**
  * @ProjectName:    Escape
  * @ClassName:      RoleAchievement
  * @Description:    用于生成玩家和获取玩家历史战绩，并写入文件
  * @Author:         Han Chubo
  */


import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class RoleAchievement {

    private ArrayList<Double> score = new ArrayList<>();

    public ArrayList<Double> getScore() {
        return score;
    }

    public void setScore(ArrayList<Double> score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    transient String filePath;


    public RoleAchievement(ArrayList<Double> scoreGet, String nameGet) throws FileNotFoundException {
        score = scoreGet;
        name = nameGet;
        filePath = "src/data/" + name + ".escape";
    }

    public RoleAchievement(String nameGet) {
        name = nameGet;
        filePath = "src/data/" + name + ".escape";
    }

    public void write() throws IOException { // 将成绩写入文件
        File file = new File(filePath);
        file.createNewFile();
        Writer w = new FileWriter(filePath);
        Gson gson = new Gson();
        System.out.println(gson.toJson(this));
        w.write(gson.toJson(this));
        w.close();
    }

    public String read() throws IOException { // 读取成绩文件
        String temp;
        Reader r = new FileReader(filePath);
        BufferedReader br = new BufferedReader(r);
        return br.readLine();
    }


}

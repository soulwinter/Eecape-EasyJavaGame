package UI;

/**
  * @ProjectName:    Escape
  * @ClassName:      ScoreChart
  * @Description:    使用 JFreeChart 生成程序榜单
  * @Author:         Han Chubo
  */


import Game.RoleAchievement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ScoreChart extends JFrame {

    ArrayList<Double> scoreList;
    String name;
    int widthNeed;

    public ScoreChart(String nameOfRole) {
        name = nameOfRole;
        convert();
        widthNeed = scoreList.size()*400;
        if (widthNeed > 1000)
            widthNeed = 1000;
        setSize(widthNeed, 600);
        JPanel showPanel = createPanel();
        this.setContentPane(showPanel);
        showPanel.setBackground(new Color(0xF6DCD1));
        setVisible(true);
    }

    public CategoryDataset createDataset() {
        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
        for (int i = 0; i < scoreList.size(); i++) {
            dataset.setValue(scoreList.get(i), "分数", Integer.toString(i + 1));
        }
        return dataset;
    }

    public JFreeChart createChart(CategoryDataset dataset) //用数据集创建一个图表
    {
        JFreeChart chart= ChartFactory.createBarChart("hi", "序号",
                "分数", dataset, PlotOrientation.VERTICAL, true, true, false); //创建一个JFreeChart
        chart.setTitle(new TextTitle("分数统计",new Font("FZBiaoZhiS-R-GB",Font.BOLD+Font.ITALIC,20)));
        CategoryPlot plot= (CategoryPlot) chart.getPlot();//获得图标中间部分，即plot
        CategoryAxis categoryAxis = plot.getDomainAxis();//获得横坐标
        categoryAxis.setLabelFont(new Font("FZBiaoZhiS-R-GB",Font.BOLD,12));//设置横坐标字体
        return chart;
    }

    public JPanel createPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    public void convert() {
        RoleAchievement temp1 = null;
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<RoleAchievement>(){}.getType();
        try {
            temp1 = gson.fromJson(new RoleAchievement(name).read(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        scoreList = temp1.getScore();
//        Collections.sort(scoreList);
    }
}

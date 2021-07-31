package UI;

/**
  * @ProjectName:    Escape
  * @ClassName:      MyTextField
  * @Description:    重写了 JTextField，使其可以在未被 Focus 时显示灰色幽灵字体
  * @Author:         Han Chubo
  */


import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MyTextField extends JTextField implements FocusListener {
    private int state = 0; //1->修改 0->提示
    private String showText;
    public MyTextField(String text,int column){
        super(text,column);
        showText = text;
        setForeground(Color.GRAY);
        addFocusListener(this);
    }
    @Override
    public String getText(){
        if(this.state==1){
            return super.getText();
        }
        return "";
    }

    @Override
    public void focusGained(FocusEvent e) { // 获得 Focus 时将幽灵
        if(state==0){
            state = 1;
            setForeground(Color.BLACK);
            this.setText("");
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        String temp = getText();
        System.out.println(temp);
        if(temp.equals("")){
            setForeground(Color.GRAY);
            this.setText(showText);
            state = 0;
        }else{
            this.setText(temp);
        }
    }

}
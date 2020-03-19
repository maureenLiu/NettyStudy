package com.maureen.netty.s02;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends Frame {
    TextArea ta = new TextArea(); //多行文本
    TextField tf = new TextField(); //单行文本


    public ClientFrame() {
        this.setSize(600, 400);
        this.setLocation(100, 20);
        this.add(ta, BorderLayout.CENTER);
        this.add(tf, BorderLayout.SOUTH);
        tf.addActionListener(new ActionListener() { //按回车会触发actionPerformed
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //把字符串发送到服务器
                ta.setText(ta.getText() + tf.getText());
                tf.setText("");
            }
        });
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new ClientFrame();
    }
}

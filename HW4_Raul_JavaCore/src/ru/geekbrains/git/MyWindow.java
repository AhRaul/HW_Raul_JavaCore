package ru.geekbrains.git;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyWindow extends JFrame {
    public MyWindow() {
        setTitle("Test Window");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300,300,400,400);

        JButton sendMessageB = new JButton("Send message button");
        JTextArea textArea = new JTextArea();
        JTextField textField = new JTextField();

        setLayout(new BorderLayout());
        add(sendMessageB, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);
        add(textField, BorderLayout.SOUTH);

        setVisible(true);

        //обработка кнопки
        sendMessageB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button pressed..." + textField.getText());
                textArea.append(textField.getText() + "\n");                    //вывод в большое поле
            }
        });

        //обработка Enter
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Enter pressed..." + textField.getText());
                textArea.append(textField.getText() + "\n");                    //вывод в большое поле
            }
        });
    }
}
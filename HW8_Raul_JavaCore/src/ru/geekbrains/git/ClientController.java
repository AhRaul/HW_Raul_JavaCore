package ru.geekbrains.git;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ClientController implements Controller {
    private final static String SERVER_ADDR = "localhost";
    private final static int SERVER_PORT = 8189;

    private final static int IS_ACTIVE = 1;         //показатель активации логином паролем
    private final static int IS_DEACTIVE = 0;
    private static int active = IS_DEACTIVE;               //память показателя (по умолчанию не активен)

    private ClientUI ui;


    private Socket sock;
    private Scanner in;
    private PrintWriter out;
    private int index = new Random().nextInt(3) + 1;

    public ClientController() {
        initConnection();
    }

    public void showUI(ClientUI ui) {
        this.ui = ui;
        ui.showUI();
        sendMessage("/auth login" + index + " pass" + index);
    }

    private void initConnection() {
        try {
            sock = new Socket(SERVER_ADDR, SERVER_PORT);
            in = new Scanner(sock.getInputStream());
            out = new PrintWriter(sock.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }


        new Thread(() -> {
            try {

                long a = System.currentTimeMillis();                    //засекли время открытия канала
                while (true) {
                    long b = System.currentTimeMillis();        //сравнение времени закрытия
                    String w = in.nextLine();
                    if (b-a >= 120000 && active == IS_DEACTIVE) {
                        System.out.println("клиент не успел авторизоваться");
                        closeConnection();
                    } else {
                        break;
                    }
                }

                while (true) {
                    if (in.hasNext()) {
                        String w = in.nextLine();
                        if (w.startsWith("end session")) break;
                        ui.addMessage(w);
                    }

                }
            } catch (Exception e) {
            }
        }).start();
    }

    public static void activate_client() {              //настройка указателя активации методом
        active = IS_ACTIVE;
    }

    @Override
    public void sendMessage(String msg) {
        out.println(msg);
    }

    @Override
    public void closeConnection() {
        try {
            sendMessage("/exit");
            sock.close();
            out.close();
            in.close();
        } catch (IOException exc) {
        }
    }
}

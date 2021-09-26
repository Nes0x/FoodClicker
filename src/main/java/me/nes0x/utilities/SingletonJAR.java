package me.nes0x.utilities;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SingletonJAR implements Runnable{
    private static final int PORT = 5555;

    public SingletonJAR() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true) {
                try {
                    serverSocket.accept();
                } catch (IOException ex) {
                }
            }
        } catch (IOException ex) {
            try {
                new Socket(InetAddress.getLocalHost(), PORT);
            } catch (IOException ex1) {
            } finally {
                System.exit(0);
            }
        }
    }

}

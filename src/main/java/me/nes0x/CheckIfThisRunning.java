package me.nes0x;

import javax.swing.*;
import java.io.*;

public class CheckIfThisRunning {
    private static File file;

    public CheckIfThisRunning() throws IOException {
        file = new File(System.getenv().get("APPDATA") + "\\FoodClicker\\appRunning.dat");
        if (!file.exists()) {
            file.createNewFile();
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "App already running!",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE,
                    null
            );
            System.exit(1);
        }
    }

    public static File getFile() {
        return file;
    }
}

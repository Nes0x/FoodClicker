package me.nes0x;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class UpdateInformation {
    public UpdateInformation() throws IOException {
        URL url = new URL("");
        ArrayList<Character> characterArrayList = new ArrayList<>();
        URLConnection urlConnection = url.openConnection();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());



        int i = 0;
        while ((i = bufferedInputStream.read()) != -1) {
            characterArrayList.add((char)i);
        }

        StringBuilder version = new StringBuilder();

        for (Character charUpdate : characterArrayList) {
            version.append(charUpdate);
        }

        if (!version.toString().equalsIgnoreCase("4.0")) {
            int result = JOptionPane.showConfirmDialog(
                    Clicker.getFrame(),
                    "New version of FoodClicker! " + version + "\nDo you want to update it?",
                    "Update!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE

            );
            if (result == JOptionPane.YES_OPTION) {
                java.awt.Desktop.getDesktop().browse(URI.create("https://github.com/Nes0x/FoodClicker/releases/tag/" + version));
            }

        }

        bufferedInputStream.close();

    }
}

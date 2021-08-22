package me.nes0x;

import javax.swing.*;
import java.awt.*;
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


        int i = bufferedInputStream.read();
        characterArrayList.add((char) i);

        if (!characterArrayList.get(0).equals('2')) {
            int result = JOptionPane.showConfirmDialog(
                    Clicker.getFrame(),
                    "New version of FoodClicker! " + characterArrayList.get(0) + ".0" + "\nDo you want to update it?",
                    "Update!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE

            );
            if (result == JOptionPane.YES_OPTION) {
                java.awt.Desktop.getDesktop().browse(URI.create("https://github.com/Nes0x/FoodClicker/releases/tag/" + characterArrayList.get(0) + ".0"));
            }

        }

        bufferedInputStream.close();

    }
}





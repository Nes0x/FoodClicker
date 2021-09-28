package me.nes0x.utilities;

import me.nes0x.Clicker;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Updater {
    public Updater() throws IOException {
        URL url = new URL("");
        ArrayList<Character> updateCharacters = new ArrayList<>();
        URLConnection urlConnection = url.openConnection();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());


        int i = 0;
        while ((i = bufferedInputStream.read()) != -1) {
            updateCharacters.add((char)i);
        }


        StringBuilder update = new StringBuilder();

        for (Character character : updateCharacters) {
            update.append(character);
        }

        if (!update.toString().equalsIgnoreCase("5.0")) {
            int result = JOptionPane.showConfirmDialog(
                    Clicker.getFrame(),
                    "New version of FoodClicker! " + update  + "\nDo you want to update it?",
                    "Update!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE

            );
            if (result == JOptionPane.YES_OPTION) {
                java.awt.Desktop.getDesktop().browse(URI.create("https://github.com/Nes0x/FoodClicker/releases/tag/" + update));
            }

        }

        bufferedInputStream.close();

    }
}





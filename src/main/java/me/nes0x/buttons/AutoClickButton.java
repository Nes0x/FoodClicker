package me.nes0x.buttons;

import me.nes0x.Clicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoClickButton {
    private static int cost;
    private static boolean runThis = false;

    //ustawianie ceny przycisku
    public AutoClickButton(int cost) {
        this.cost = cost;
    }

    //metoda ktora dodaje autoClickButton
    public JButton initAutoClickButton() {

        JButton autoClickButton = new JButton("Auto clicker add 1 $ on 1 sec, cost: " + cost + " $");
        autoClickButton.setPreferredSize(new Dimension(280, 30));

        autoClickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pobieranie aktualnej ilosci pieniedzy, ile dodac do auto klikania

                //sprawdzanie czy uzytkownik ma wiecej pieniedzy niz koszt przycisku
                if (Clicker.variables.getInt("money", 0) >= cost) {
                    //sprawdzanie czy wątek dziala czy nie
                    if (!runThis && !Clicker.isRun()) {
                        AutoClick thread = new AutoClick();
                        thread.start();
                    }

                    JOptionPane.showMessageDialog(
                            Clicker.getFrame(),
                            "Success!",
                            "Success!",
                            JOptionPane.PLAIN_MESSAGE,
                            null
                    );
                    //usuwanie danej ilosci pieniedzy oraz dodanie do autoClick wartosci 1
                    Clicker.variables.putInt("money", Clicker.variables.getInt("money", 0) - cost);
                    Clicker.variables.putInt("autoclick", Clicker.variables.getInt("autoclick", 0) + 1);

                    //odswiezanie labeli i przycisku i dodanie 4000 do wartosci przycisku
                    Clicker.refreshLabels();
                    Clicker.variables.putInt("autoclickcost", Clicker.variables.getInt("autoclickcost", 900) + 900);

                    autoClickButton.setText("Auto clicker add 1 $ on 1 sec, cost: " + Clicker.variables.getInt("autoclickcost", 900) + " $");

                    //zmiana tej zmiennej na true oznacza ze watek aktualnie dziala
                    runThis = true;

                    //zapisywanie wartosci przycisku

                } else {
                    JOptionPane.showMessageDialog(
                            Clicker.getFrame(),
                            "Error! You don't have " + cost + "$",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        return autoClickButton;
    }
}

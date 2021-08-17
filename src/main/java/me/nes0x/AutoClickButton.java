package me.nes0x;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AutoClickButton extends SaveGlobalVariables{
    private static int cost;
    private static boolean runThis = false;

    //ustawianie ceny przycisku
    public AutoClickButton(int cost) {
        this.cost = cost;
    }

    //metoda ktora dodaje autoClickButton
    public JButton initAutoClickButton() {

        JButton autoClickButton = new JButton("Auto clicker add 1 $ on 1 sec without click! cost: " + cost + " $");

        autoClickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pobieranie aktualnej ilosci pieniedzy, ile dodac do auto klikania

                int money = getMoney();
                int autoClick = getAutoClick();

                //sprawdzanie czy uzytkownik ma wiecej pieniedzy niz koszt przycisku
                if (money >= cost) {
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
                    setMoney(money -= cost);
                    setAutoClick(autoClick += 1);

                    //odswiezanie labeli i przycisku i dodanie 4000 do wartosci przycisku
                    Clicker.refreshLabels();

                    cost += 900;
                    autoClickButton.setText("Auto clicker add 1 $ on 1 sec without click! cost: " + cost + " $");

                    //zmiana tej zmiennej na true oznacza ze watek aktualnie dziala
                    runThis = true;

                    //zapisywanie wartosci przycisku w pliku
                    DataOutputStream file = null;
                    try {
                        file = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(System.getenv().get("APPDATA") + "\\FoodClicker" + "\\" + "autoClicker.dat")));

                        file.writeInt(cost);

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } finally {
                        if (file != null) {
                            try {
                                file.close();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    }
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

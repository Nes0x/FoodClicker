package me.nes0x;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateButton extends SaveGlobalVariables {
    private String name;
    private int price;
    private int increasePrice;
    private int addToEarn;
    private JButton button;


    //konstruktor i dodanie action listenera do przycisku
    public CreateButton(String name, int addToEarn, int price, int increasePrice) {
        this.name = name;
        this.addToEarn = addToEarn;
        this.price = price;
        this.increasePrice = increasePrice;

        //tworzenie przycisku
        button = new JButton(name + " add " + addToEarn + " $ to earn, cost: " + price + " $");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pobieranie aktualnej ilosci pieniedzy, ilosc zdobywania pieniedzy, ilosci aktualnej wartosci dla kupna przycisku
                int money = getMoney();
                int earn = getEarn();
                int priceNow = getPrice();

                //sprawdzanie czy uzytkownik ma wiecej pieniedzy niz koszt przycisku
                if (money >= priceNow) {
                    JOptionPane.showMessageDialog(
                            Clicker.getFrame(),
                            "Success!",
                            "Success!",
                            JOptionPane.PLAIN_MESSAGE,
                            null
                    );
                    //usuwanie danej ilosci pieniedzy oraz dodanie do earn danej ilosci
                    setMoney(money -= priceNow);
                    setEarn(earn += addToEarn);

                    // odswiezanie labeli i przycisku
                    Clicker.refreshLabels();
                    try {
                        refreshButton(button);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            Clicker.getFrame(),
                            "Error! You don't have " + priceNow + "$",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    //metoda która odswieza przycisk i zapisuje aktualna jego wartosc do pliku
    private void refreshButton(JButton button) throws IOException {
        setPrice(getPrice() + getIncreasePrice());
        DataOutputStream file = null;


        try {
            file = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(System.getenv().get("APPDATA") + "\\FoodClicker" + "\\" + getName() + ".dat")));

            file.writeInt(price);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) file.close();
        }
        button.setText(name + " add " + addToEarn + " $ to earn, cost: " + price + " $");
    }


    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIncreasePrice() {
        return increasePrice;
    }

    public JButton getButton() {
        return button;
    }

}

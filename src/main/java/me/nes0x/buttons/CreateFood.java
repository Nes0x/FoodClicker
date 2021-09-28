package me.nes0x.buttons;

import me.nes0x.Clicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CreateFood {
    private String name;
    private int price;
    private int increasePrice;
    private int addToEarn;
    private JButton button;


    //konstruktor i dodanie action listenera do przycisku
    public CreateFood(String name, int addToEarn, int price, int increasePrice) {
        this.name = name;
        this.addToEarn = addToEarn;
        this.price = price;
        this.increasePrice = increasePrice;

        //tworzenie przycisku
        button = new JButton(name + " add " + addToEarn + " $ to earn, cost: " + price + " $");
        button.setPreferredSize(new Dimension(280, 30));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pobieranie aktualnej ilosci pieniedzy, ilosc zdobywania pieniedzy, ilosci aktualnej wartosci dla kupna przycisku
                int money = Clicker.variables.getInt("money", 0);
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
                    Clicker.variables.putInt("money", Clicker.variables.getInt("money", 0) - priceNow);
                    Clicker.variables.putInt("earn", Clicker.variables.getInt("earn", 1) + addToEarn);

                    //odswiezanie labeli i przycisku
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

    //metoda która odswieza przycisk i zapisuje aktualna jego wartosc
    private void refreshButton(JButton button) throws IOException {
        Clicker.variables.putInt(name, Clicker.variables.getInt(name, price) + increasePrice);
        setPrice(getPrice() + getIncreasePrice());
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

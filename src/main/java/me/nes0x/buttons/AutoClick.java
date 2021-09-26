package me.nes0x.buttons;

import me.nes0x.Clicker;

import javax.swing.*;

public class AutoClick extends Thread{
    //dodawanie x pieniedzy co 1 sekunde
    public void run() {
        while (true) {
            refresh();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //metoda która minimalizuje błąd
    private void refresh() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Clicker.variables.putInt("money", Clicker.variables.getInt("money", 0) + Clicker.variables.getInt("autoclick", 0));
                Clicker.refreshLabels();
            }
        });

    }


}

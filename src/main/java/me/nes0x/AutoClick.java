package me.nes0x;

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
                int money = SaveGlobalVariables.getMoney();
                SaveGlobalVariables.setMoney(money += SaveGlobalVariables.getAutoClick());
                Clicker.refreshLabels();
            }
        });

    }


}

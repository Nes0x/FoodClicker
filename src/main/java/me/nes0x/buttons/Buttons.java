package me.nes0x.buttons;

import me.nes0x.Clicker;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.BackingStoreException;

public class Buttons {

    //metoda która dodaje przycisk do zdobywania pieniedzy
    public JButton initEarnButton() {
        JButton earnButton = new JButton("Click me to earn money");
        earnButton.setPreferredSize(new Dimension(320, 30));
        earnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clicker.variables.putInt("money", Clicker.variables.getInt("money", 0) + Clicker.variables.getInt("earn", 1));
                Clicker.refreshLabels();
            }
        });
        return earnButton;
    }

    //metoda która dodaje przycisk do zgłaszania błędow
    public JButton initBugButton() {
        JButton bugButton = new JButton("Did you find a bug?");
        bugButton.setPreferredSize(new Dimension(280, 30));
        bugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        Clicker.getFrame(),
                        "Report it to Nes0x#6817 on discord!",
                        "Bug hunter!",
                        JOptionPane.PLAIN_MESSAGE,
                        null
                );
            }
        });
        return bugButton;

    }


    //metoda która dodaje przycisk który resetuje gre(postep)
    public JButton initResetGameButton() {
        JButton resetGameButton = new JButton("Reset game");
        resetGameButton.setPreferredSize(new Dimension(280, 30));
        resetGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int result = JOptionPane.showConfirmDialog(
                        Clicker.getFrame(),
                        "Are you sure? Required restart",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE

                );

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        Clicker.variables.removeNode();
                    } catch (BackingStoreException backingStoreException) {
                        backingStoreException.printStackTrace();
                    }
                    System.exit(0);

                }


            }
        });
        return resetGameButton;

    }

    public JButton initDisableBackground(String keyVariable) {
        JButton disableBackground = new JButton();
        disableBackground.setPreferredSize(new Dimension(280, 30));
        if (Clicker.variables.getBoolean(keyVariable, true)) {
            disableBackground.setText("Disable background music");
        } else {
            disableBackground.setText("Enable background music");
        }


        disableBackground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Clicker.background.isActive()) {
                    Clicker.variables.putBoolean(keyVariable, false);
                    Clicker.background.stop();
                    disableBackground.setText("Enable background music");
                } else {
                    Clicker.variables.putBoolean(keyVariable, true);
                    Thread music = new Thread() {
                        public void run() {
                            try {
                                Clicker.background.loop(Clip.LOOP_CONTINUOUSLY);
                                Clicker.background.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    music.start();
                    disableBackground.setText("Disable background music");
                }

            }
        });

        return disableBackground;
    }


    public JSlider initVolumeJSlider(String keyVariable) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 6, Clicker.variables.getInt(keyVariable, 6));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Clicker.backgroundVolume.setValue(slider.getValue());
                Clicker.variables.putInt(keyVariable, slider.getValue());
            }
        });

        return slider;

    }
}

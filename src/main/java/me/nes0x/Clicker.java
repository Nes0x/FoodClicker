package me.nes0x;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import me.nes0x.buttons.AutoClick;
import me.nes0x.buttons.AutoClickButton;
import me.nes0x.buttons.Buttons;
import me.nes0x.buttons.CreateFood;
import me.nes0x.utilities.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.io.*;

import java.util.Arrays;
import java.util.prefs.Preferences;

public class Clicker {
    public static Preferences variables = Preferences.userNodeForPackage(Clicker.class);
    private static JFrame frame;
    private static JLabel moneyLabel, autoClickLabel, earnLabel;
    public static Clip background;
    public static FloatControl backgroundVolume;
    private static boolean run;


    //metoda która odswieza informacje
    public static void refreshLabels() {
        moneyLabel.setText("Your money: " + variables.getInt("money", 0));
        earnLabel.setText("You earn: " + variables.getInt("earn", 1) + " $ for click");
        autoClickLabel.setText("You earn: " + variables.getInt("autoclick", 0) + " $/s");
    }


    //Main
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        //ustawianie
        new SingletonJAR();
        LafManager.install(new DarculaTheme());
        frame = new JFrame();
        moneyLabel = new JLabel("Your money: " + variables.getInt("money", 0));
        earnLabel = new JLabel("You earn: " + variables.getInt("earn", 1) + " $ for click");
        autoClickLabel = new JLabel("You earn: " + variables.getInt("autoclick", 0) + " $/s");
        run = false;

        //ustawianie ikonki
        frame.setIconImage(
                new ImageIcon(Clicker.class.getClassLoader().getResource("icon.png")).getImage()
        );

        Buttons buttons = new Buttons();

        //stworzenie głownej zakladki
        JTabbedPane main = new JTabbedPane();

        //stworzenie zakladek
        JPanel clicker = new JPanel();
        JPanel shop = new JPanel();
        JPanel settings = new JPanel();
        JPanel changelog = new JPanel();


        //dodawanie elementow do zakladki clicker


        clicker.add(buttons.initEarnButton());
        clicker.add(moneyLabel);
        clicker.add(earnLabel);
        clicker.add(autoClickLabel);


        //dodawanie elementow do zakladki settings


        settings.add(buttons.initBugButton());
        settings.add(buttons.initResetGameButton());
        settings.add(buttons.initDisableBackground("background"));
        settings.add(buttons.initVolumeJSlider("volume"));

        //dodanie elementu do changelogu

        new Changelog(changelog);

        //tworzenie przyciskow
        CreateFood buttonsFood[] = {
                new CreateFood("Wheat", 1, variables.getInt("Wheat", 500), 600),
                new CreateFood("Pumpkin", 5, variables.getInt("Pumpkin", 3000), 4000),
                new CreateFood("Watermelon", 10, variables.getInt("Watermelon", 5000), 6000),
                new CreateFood("Strawberry", 15, variables.getInt("Strawberry", 10000), 11000),
                new CreateFood("Orange", 25, variables.getInt("Orange", 20000), 21000),
                new CreateFood("Pizza", 90, variables.getInt("Pizza", 50000), 51000)

        };

        //dodawanie przyciskow
        Arrays.stream(buttonsFood).forEach(event -> shop.add(event.getButton()));

        AutoClickButton autoClickButton = new AutoClickButton(variables.getInt("autoclickcost", 900));
        shop.add(autoClickButton.initAutoClickButton());


        //dodawanie wszystkich zakladek
        main.add("Clicker", clicker);
        main.add("Shop", shop);
        main.add("Settings", settings);
        main.add("Changelog", changelog);


        //aktywowanie auto klikania jesli ulepszenie zostalo kupione
        if (variables.getInt("autoclick", 0) != 0) {
            run = true;
            AutoClick thread = new AutoClick();
            thread.start();
        }

        //dodanie integracji z discordem
        DiscordIntegration discordIntegration = new DiscordIntegration();
        discordIntegration.startRPC();

        //wyłączanie statusu discord podczas zamykania aplikacji
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                discordIntegration.stopRPC();
            }
        });


        //stworzenie okna
        frame.add(main);
        frame.setTitle("FoodClicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setVisible(true);
        new Updater();


        AudioInputStream input = AudioSystem.getAudioInputStream(Clicker.class.getClassLoader().getResource("background.wav"));
        background = AudioSystem.getClip();
        background.open(input);
        backgroundVolume = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);

        if (variables.getBoolean("background", true)) {
            Thread music = new Thread() {
                public void run() {
                    try {
                        backgroundVolume.setValue(Clicker.variables.getInt("volume", 6));
                        background.loop(Clip.LOOP_CONTINUOUSLY);
                        background.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            music.start();
        }


    }

    public static JFrame getFrame() {
        return frame;
    }

    public static boolean isRun() {
        return run;
    }

}

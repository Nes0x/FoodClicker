package me.nes0x;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import me.nes0x.buttons.AutoClick;
import me.nes0x.buttons.AutoClickButton;
import me.nes0x.buttons.CreateFood;
import me.nes0x.utilities.Changelog;
import me.nes0x.utilities.DiscordIntegration;
import me.nes0x.utilities.SingletonJAR;
import me.nes0x.utilities.Updater;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Clicker {
    public static Preferences variables = Preferences.userNodeForPackage(Clicker.class);
    private static JFrame frame;
    private static JLabel moneyLabel;
    private static JLabel earnLabel;
    private static JLabel autoClickLabel;
    private static boolean run;


    public Clicker() {

    }

    //metoda która odswieza informacje
    public static void refreshLabels() {
        moneyLabel.setText("Your money: " + variables.getInt("money", 0));
        earnLabel.setText("You earn: " + variables.getInt("earn", 1) + " $ for click");
        autoClickLabel.setText("You earn: " + variables.getInt("autoclick", 0) + " $/s");
    }

    //metoda która dodaje przycisk do zdobywania pieniedzy
    private JButton initEarnButton() {
        JButton earnButton = new JButton("Click me to earn money");

        earnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                variables.putInt("money", variables.getInt("money", 0) + variables.getInt("earn", 1));
                refreshLabels();
            }
        });
        return earnButton;
    }

    //metoda która dodaje przycisk do zgłaszania błędow
    private JButton initBugButton() {
        JButton bugButton = new JButton("Did you find a bug?");

        bugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        frame,
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
    private JButton initResetGameButton() {
        JButton resetGameButton = new JButton("Reset game!");

        resetGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int result = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure? Required restart",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE

                );

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        variables.removeNode();
                    } catch (BackingStoreException backingStoreException) {
                        backingStoreException.printStackTrace();
                    }
                    System.exit(0);

                }


            }
        });
        return resetGameButton;

    }

    //Main
    public static void main(String[] args) throws IOException {

        //ustawianie
        new SingletonJAR();
        LafManager.install(new DarculaTheme());
        frame = new JFrame();
        moneyLabel = new JLabel("Your money: " + variables.getInt("money", 0));
        earnLabel = new JLabel("You earn: " + variables.getInt("earn", 1) + " $ for click");
        autoClickLabel = new JLabel("You earn: " +variables.getInt("autoclick", 0) + " $/s");
        run = false;

        //ustawianie ikonki
        frame.setIconImage(
                new ImageIcon(Clicker.class.getClassLoader().getResource("icon.png")).getImage()
        );

        Clicker instance = new Clicker();

        //stworzenie głownej zakladki
        JTabbedPane main = new JTabbedPane();

        //stworzenie zakladek
        JPanel clicker = new JPanel();
        JPanel shop = new JPanel();
        JPanel about = new JPanel();
        JPanel changelog = new JPanel();


        //dodawanie elementow do zakladki clicker
        clicker.add(instance.initEarnButton());

        clicker.add(moneyLabel);
        clicker.add(earnLabel);
        clicker.add(autoClickLabel);

        //dodawanie elementow do zakladki help
        about.add(instance.initBugButton());
        about.add(instance.initResetGameButton());

        //dodanie elementu do changelogu

        new Changelog(changelog);

        //tworzenie przyciskow
        CreateFood buttons[] = {
                new CreateFood("Wheat", 1, variables.getInt("Wheat", 500), 600),
                new CreateFood("Pumpkin", 5, variables.getInt("Pumpkin", 3000), 4000),
                new CreateFood("Watermelon", 10, variables.getInt("Watermelon", 5000), 6000),
                new CreateFood("Strawberry", 15, variables.getInt("Strawberry", 10000), 11000),
                new CreateFood("Orange", 25, variables.getInt("Orange", 20000), 21000),
                new CreateFood("Pizza", 90, variables.getInt("Pizza", 50000), 51000)

        };

        //dodawanie przyciskow
        Arrays.stream(buttons).forEach(event -> shop.add(event.getButton()));

        AutoClickButton autoClickButton = new AutoClickButton(variables.getInt("autoclickcost", 900));
        shop.add(autoClickButton.initAutoClickButton());


        //dodawanie wszystkich zakladek
        main.add("Clicker", clicker);
        main.add("Shop", shop);
        main.add("About", about);
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
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
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

    }

    public static JFrame getFrame() {
        return frame;
    }

    public static boolean isRun() {
        return run;
    }

}

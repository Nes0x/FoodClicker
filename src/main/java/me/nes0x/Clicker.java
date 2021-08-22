package me.nes0x;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Clicker extends SaveGlobalVariables{
    private static JFrame frame;
    private static JLabel moneyLabel;
    private static JLabel earnLabel;
    private static JLabel autoClickLabel;
    private static boolean run;
    private int price = 0;


    public Clicker() {

    }

    //metoda która resetuje postęp gry
    private static void deleteFolder(File file) throws IOException {
        if (file.isDirectory()) {
            for (File c : file.listFiles())
                deleteFolder(c);
        }
        if (!file.delete())
            throw new FileNotFoundException();
    }

    //metoda która odswieza informacje
    public static void refreshLabels() {
        moneyLabel.setText("Your money: " + getMoney());
        earnLabel.setText("You earn: " + getEarn() + " $ for click");
        autoClickLabel.setText("You earn: " + getAutoClick() + " $/s");
    }

    //metoda która dodaje przycisk do zdobywania pieniedzy
    private JButton initEarnButton() {
        JButton earnButton = new JButton("Click me to earn money");

        earnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int money = getMoney();
                int earn = getEarn();
                setMoney(money += earn);
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
                        "Report it to Nes0x#7777 on discord!",
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
                        "Are you sure?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE

                );

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        Clicker.deleteFolder(new File(gameFolderPath));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    System.exit(0);

                }


            }
        });
        return resetGameButton;

    }

    //metoda która pobiera aktualną cene jedzenia
    private int getPrice(String fileName) throws IOException {
        DataInputStream file = null;

        try {
            file = new DataInputStream(new BufferedInputStream(new FileInputStream(System.getenv().get("APPDATA") + "\\FoodClicker" + "\\" + fileName + ".dat")));

            price = file.readInt();



        } catch (IOException e) {
            switch (fileName) {
                case "Wheat":
                    price = 500;
                    break;
                case "Pumpkin":
                    price = 3000;
                    break;
                case "Watermelon":
                    price = 5000;
                    break;
                case "Strawberry":
                    price = 10000;
                    break;
                case "Orange":
                    price = 20000;
                    break;
            }
        } finally {
            if (file != null) file.close();
        }

        return price;
    }

    //tworzenie i ladowanie plikow
    private void createAndLoadFile() throws IOException {
        createGlobalFile();
        loadGlobalFile();
    }

    //zapisywanie pliku
    private void saveFile() throws IOException {
        saveGlobalFile();
    }



    //Main
    public static void main(String[] args) throws IOException, URISyntaxException {

        //ustawianie
        LafManager.install(new DarculaTheme());
        new CheckIfThisRunning();
        frame = new JFrame();
        moneyLabel = new JLabel("Your money: " + getMoney());
        earnLabel = new JLabel("You earn: " + getEarn() + " $ for click");
        autoClickLabel = new JLabel("You earn: " + getAutoClick() + " $/s");
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
        new ChangelogPastebin(changelog);

        //tworzenie przyciskow
        CreateButton buttons[] = {
                new CreateButton("Wheat", 1, instance.getPrice("Wheat"), 600),
                new CreateButton("Pumpkin", 5, instance.getPrice("Pumpkin"), 4000),
                new CreateButton("Watermelon", 10, instance.getPrice("Watermelon"), 6000),
                new CreateButton("Strawberry", 15, instance.getPrice("Strawberry"), 11000),
                new CreateButton("Orange", 25, instance.getPrice("Orange"), 21000)

        };

        //dodawanie przyciskow
        Arrays.stream(buttons).forEach(event -> shop.add(event.getButton()));

        AutoClickButton autoClickButton = new AutoClickButton(loadAutoClickerFile());
        shop.add(autoClickButton.initAutoClickButton());


        //dodawanie wszystkich zakladek
        main.add("Clicker", clicker);
        main.add("Shop", shop);
        main.add("About", about);
        main.add("Changelog", changelog);

        //tworzenie i ladowanie plikow
        instance.createAndLoadFile();

        //aktywowanie auto klikania jesli ulepszenie zostalo kupione
        if (getAutoClick() != 0) {
            run = true;
            AutoClick thread = new AutoClick();
            thread.start();
        }

        //dodanie integracji z discordem
        DiscordIntegration discordIntegration = new DiscordIntegration();
        discordIntegration.startRPC();



        //dodanie windowsListenera ktory zapisuje postęp gry
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    instance.saveFile();
                    discordIntegration.stopRPC();
                    CheckIfThisRunning.getFile().delete();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

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
        new UpdateInformation();

    }

    public static JFrame getFrame() {
        return frame;
    }

    public static boolean isRun() {
        return run;
    }

}

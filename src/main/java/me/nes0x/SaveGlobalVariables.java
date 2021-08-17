package me.nes0x;

import java.io.*;

public class SaveGlobalVariables {
    private static int money;
    private static int earn;
    private static int autoClick;
    private static int costAutoClick;
    public String gameFolderPath = System.getenv().get("APPDATA") + "\\FoodClicker";
    private String gameFilePath = gameFolderPath + "\\settings.dat";


    //metoda ktora zapisuje do podanego pliku podane wartosci
    private void fileMethod(String nameFile, int money, int addMoney, int autoClick) throws IOException {

        DataOutputStream file = null;

        try {
            file = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(nameFile)));

            file.writeInt(money);
            file.writeInt(addMoney);
            file.writeInt(autoClick);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) file.close();
        }
    }

    //jesli nie ma folderu i pliku to go stworzy
    public void createGlobalFile() throws IOException {
        File gameFolder = new File(gameFolderPath);
        if (!gameFolder.exists()) {
            gameFolder.mkdir();
            File gameFile = new File(gameFilePath);
            if (!gameFile.exists()) {
                gameFile.createNewFile();
                fileMethod(gameFilePath, 0, 1, 0);
            }
        }

    }

    //metoda ktora zapisuje plik
    public void saveGlobalFile() throws IOException {
        fileMethod(gameFilePath, money, earn, autoClick);
    }

    //metoda ktora odczytuje zawartosc pliku
    public void loadGlobalFile() throws IOException {
        DataInputStream file = null;

        try {
            file = new DataInputStream(new BufferedInputStream(new FileInputStream(gameFilePath)));

            money = file.readInt();
            earn = file.readInt();
            autoClick = file.readInt();

            Clicker.refreshLabels();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) file.close();
        }
    }

    //odczytywanie wartosci autoClick
    public static int loadAutoClickerFile() throws IOException {
        DataInputStream file = null;

        try {
            file = new DataInputStream(new BufferedInputStream(new FileInputStream(System.getenv().get("APPDATA") + "\\FoodClicker" + "\\" + "autoClicker.dat")));

            costAutoClick = file.readInt();

            Clicker.refreshLabels();
        } catch (IOException e) {
            costAutoClick = 900;
        } finally {
            if (file != null) file.close();
        }
        return costAutoClick;
    }



    public static int getMoney() {
        return money;
    }

    public static void setMoney(int money) {
        SaveGlobalVariables.money = money;
    }

    public static int getEarn() {
        return earn;
    }

    public static void setEarn(int earn) {
        SaveGlobalVariables.earn = earn;
    }

    public static int getAutoClick() {
        return autoClick;
    }

    public static void setAutoClick(int autoClick) {
        SaveGlobalVariables.autoClick = autoClick;
    }
}


package game.util;

import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SaveManager {

    public enum Key {
        DIAMOND,
        GOLD,
        SILVER,
        GREEN,
        NONE;
    }

    public enum Attributes {
        ALL,
        OBSIDIAN,
        ANTIPROVIRUS,
        ANTIVIRUS,
        PROVIRUS,
        NONE;
    }

    private Map<String, Comparable> saveData;
    private Map<String, Integer> controlData;

    public SaveManager() {
        saveData = new HashMap<>();
        controlData = new HashMap<>();
        saveData = readSave();
        controlData = readControlSave();
    } // SaveManager

    public Map<String, Integer> getControls() {
        return controlData;
    } // getControls

    public Map<String, Comparable> getSave() {
        return saveData;
    } // getSave

    public void updateSave(String[] newData) {

    } // updateSave

    public void updateControls(String target, int newControl) {
        String data = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("controls.sav"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("controls.sav"))) {
            while (data != "") {
                writer.write(data.substring(0, data.indexOf(':')));
                if (data.substring(0, data.indexOf(':')).equals(target)) {
                    writer.write(":" + newControl + ";");
                } else {
                    writer.write(data.substring(data.indexOf(':'), data.indexOf(';') + 1));
                }
                writer.newLine();
                data = data.substring(data.indexOf(';') + 1, data.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // updateControls

    public void resetControls() {
        File delete = new File("controls.sav");
        delete.delete();
        controlData = readControlSave();
    } // resetControls

    private Map<String, Comparable> readSave() {
        String data = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("save.sav"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            makeSaveFile();
            return readSave();
        }
        int i = 0;
        while (!data.equals("")) {
            String mapString = data.substring(0, data.indexOf(':'));
            Comparable mapComparable;
            switch (i % 4) {
                case 0 ->
                    mapComparable = Double.valueOf(data.substring(data.indexOf(':') + 1, data.indexOf(';')));
                case 1 ->
                    mapComparable = SaveManager.Key.valueOf(data.substring(data.indexOf(':') + 1, data.indexOf(';')));
                case 2 ->
                    mapComparable = SaveManager.Attributes.valueOf(data.substring(data.indexOf(':') + 1, data.indexOf(';')));
                default ->
                    mapComparable = Boolean.valueOf(data.substring(data.indexOf(':') + 1, data.indexOf(';')));
            }
            data = data.substring(data.indexOf(';') + 1, data.length());
            saveData.put(mapString, mapComparable);
            i++;
        }
        return saveData;
    } // readSave

    private void makeSaveFile() {
        try {
            File save = new File("save.sav");
            save.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("save.sav"))) {
            for (int i = 0; i < 11; i++) {
                String levelNumber = "";
                switch (i) {
                    case 0 ->
                        levelNumber = "One";
                    case 1 ->
                        levelNumber = "Two";
                    case 2 ->
                        levelNumber = "Three";
                    case 3 ->
                        levelNumber = "Four";
                    case 4 ->
                        levelNumber = "Five";
                    case 5 ->
                        levelNumber = "Six";
                    case 6 ->
                        levelNumber = "Seven";
                    case 7 ->
                        levelNumber = "Eight";
                    case 8 ->
                        levelNumber = "Nine";
                    case 9 ->
                        levelNumber = "Boss";
                    case 10 ->
                        levelNumber = "Secret";
                }
                writer.write("level" + levelNumber + "Time:999.99;");
                writer.newLine();
                writer.write("level" + levelNumber + "Key:NONE;");
                writer.newLine();
                writer.write("level" + levelNumber + "Attributes:NONE;");
                writer.newLine();
                writer.write("level" + levelNumber + "Efficient:false;");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // makeSaveFile

    private Map<String, Integer> readControlSave() {
        String data = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("controls.sav"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            makeControlSaveFile();
            return readControlSave();
        }
        while (!data.equals("")) {
            String mapString = data.substring(0, data.indexOf(':'));
            int mapInt = Integer.parseInt(data.substring(data.indexOf(':') + 1, data.indexOf(';')));
            data = data.substring(data.indexOf(';') + 1, data.length());
            controlData.put(mapString, mapInt);
        }
        return controlData;
    } // readControlSave

    private void makeControlSaveFile() {
        try {
            File control = new File("controls.sav");
            control.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("controls.sav"))) {
            writer.write("left:263;");
            writer.newLine();
            writer.write("right:262;");
            writer.newLine();
            writer.write("up:265;");
            writer.newLine();
            writer.write("down:264;");
            writer.newLine();
            writer.write("jump:90;");
            writer.newLine();
            writer.write("flow:88;");
            writer.newLine();
            writer.write("attack:67;");
            writer.newLine();
            writer.write("pause:256;");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // makeControlSaveFile

} // SaveManager

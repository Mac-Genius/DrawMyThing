package io.github.mac_genius.drawmything.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.github.mac_genius.drawmything.DrawMyThing;
import io.github.mac_genius.drawmything.Word;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Mac on 11/10/2015.
 */
public class FileLoader {
    private File wordFile = new File("data", "words.txt");
    private File backup = new File("data", "words_backup.txt");
    private File jsonOut = new File("data", "words.json");
    private File jsonOutBackup = new File("data", "words_backup.json");

    public ArrayList<Word> getWords() {
        System.out.println("Loading dictionary...");
        ArrayList<Word> words = readJson(jsonOut);
        System.out.println("Finished loading dictionary.");
        System.out.println();
        return words;
    }

    public void addWord(String word) {
        Word temp = new Word(word, new int[word.length()], new ArrayList<>());
        if (!DrawMyThing.words.contains(temp)) {
            temp.getSearchMatches()[word.length() - 1]++;
            DrawMyThing.words.add(temp);
            writeJson(DrawMyThing.words, jsonOut);
            System.out.println("Added");
            System.out.println();
        } else {
            System.out.println("The list already contains that word.");
            System.out.println();
        }
    }

    public void organizeFile(ArrayList<Word> words) {
        writeJson(words, jsonOut);
    }

    public void deleteWord(ArrayList<Word> words, String toDelete) {
        Word temp = new Word(toDelete, new int[toDelete.length()], new ArrayList<>());

        if (words.contains(temp)) {
            words.remove(temp);
            writeJson(words, jsonOut);
            System.out.println(toDelete + " has been removed from the dictionary.");
        } else {
            DrawMyThing.logger.info("That word is not in the dictionary.");
        }
        System.out.println();
    }

    public void backup(ArrayList<Word> words) {
        try {
            if (!jsonOutBackup.exists()) {
                System.out.println("Creating a backup...");
                if (jsonOutBackup.createNewFile()) {
                    System.out.println("Done.");
                    System.out.println();
                }
            }
            writeJson(words, jsonOutBackup);
        } catch (IOException e) {
            System.out.println("Couldn't write to the file!");
        }
    }

    public boolean saveDefaultWords() {
        File words = new File(".\\data");
        File loc = new File(words, "words.json");
        if (!jsonOut.exists()) {
            if (jsonOut.mkdir()) {
                System.out.println("Created the data files.");
            } else {
                System.out.println("Could not create the data folder");
                return false;
            }
            try {
                if (loc.createNewFile()) {
                    System.out.println("Loading words...");
                } else return false;
                ArrayList<Word> word = readJson(getClass().getResourceAsStream("/words.json"));

                organizeFile(word);
                System.out.println("Done.");
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }

    public void updateWordFiles() {
        if (wordFile.exists()) {
            DrawMyThing.logger.info("Converting older files to json...");
            ArrayList<Word> words = new ArrayList<>();
            try {
                BufferedReader in = new BufferedReader(new FileReader(wordFile));
                while (in.ready()) {
                    String current = in.readLine();
                    DrawMyThing.logger.info(current);
                    words.add(new Word(current, new int[current.length()], new ArrayList<>()));
                }
                in.close();
                if (!jsonOut.exists()) {
                    writeJson(words, jsonOut);
                }
                wordFile.delete();
            } catch (IOException e) {
                System.out.println("Couldn't load from the file.");
            }
            if (!backup.exists()) {
                System.out.println("Finished converting the file to json.");
                System.out.println();
            }
        }

        if (backup.exists()) {
            backup.delete();
            System.out.println("Finished converting the file to json.");
            System.out.println();
        }
    }

    public void writeJson(ArrayList<Word> list, File file) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
            JsonArray jsonWords = new JsonArray();
            for (Word w : list) {
                jsonWords.add(w.getJson());
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            out.write(gson.toJson(jsonWords));
            out.close();
        } catch (IOException e) {
            DrawMyThing.logger.info("Could not write to the file \"" + file.getName() + "\".");
        }
    }

    public ArrayList<Word> readJson(File file) {
        ArrayList<Word> word = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String json = "";

            while (in.ready()) {
                json += in.readLine();
            }
            in.close();

            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(json).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                word.add(new Word(array.get(i).getAsJsonObject()));
            }
        } catch (IOException e) {
            DrawMyThing.logger.warning("Could not read from the file \"" + file.getName() + "\".");
        }
        return word;
    }

    public ArrayList<Word> readJson(InputStream stream) {
        ArrayList<Word> word = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String json = "";

            while (in.ready()) {
                json += in.readLine();
            }
            in.close();

            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(json).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                word.add(new Word(array.get(i).getAsJsonObject()));
            }
        } catch (IOException e) {
            DrawMyThing.logger.warning("Could not read from the file the jar.");
        }
        return word;
    }
}

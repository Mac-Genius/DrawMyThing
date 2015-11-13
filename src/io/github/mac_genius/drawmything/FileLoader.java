package io.github.mac_genius.drawmything;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * Created by Mac on 11/10/2015.
 */
public class FileLoader {
    private File wordFile = new File("data", "words.txt");
    private File backup = new File("data", "words_backup.txt");

    public TreeSet<String> getWords() {
        System.out.println("Loading dictionary...");
        TreeSet<String> words = new TreeSet<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(wordFile));
            while (in.ready()) {
                String current = in.readLine();
                words.add(current);
            }
        } catch (IOException e) {
            System.out.println("Couldn't load from the file.");
            return words;
        }
        System.out.println("Finished loading dictionary.");
        System.out.println();
        return words;
    }

    public void addWord(String word) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(wordFile, true));
            out.write(word);
            out.newLine();
            out.close();
        } catch (IOException e) {
            System.out.println("Couldn't write to the file!");
        }
    }

    public void organizeFile(TreeSet<String> words) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(wordFile, false));
            for (String w : words) {
                out.write(w);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Couldn't write to the file!");
        }
    }

    public void deleteWord(TreeSet<String> words, String toDelete) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(wordFile, false));
            for (String w : words) {
                if (!w.equalsIgnoreCase(toDelete)) {
                    out.write(w);
                    out.newLine();
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Couldn't write to the file!");
        }
    }

    public void backup(TreeSet<String> words) {
        try {
            if (!backup.exists()) {
                System.out.println("Creating a backup...");
                if (backup.createNewFile()) {
                    System.out.println("Done.");
                    System.out.println();
                }
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(backup, false));
            for (String w : words) {
                out.write(w);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Couldn't write to the file!");
        }
    }

    public boolean saveDefaultWords() {
        File words = new File(".\\data");
        File loc = new File(words, "words.txt");
        if (!words.exists()) {
            if (words.mkdir()) {
                System.out.println("Created the data files.");
            } else {
                System.out.println("Could not create the data folder");
                return false;
            }
            try {
                if (loc.createNewFile()) {
                    System.out.println("Loading words...");
                } else return false;
                TreeSet<String> word = new TreeSet<>();
                BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/words.txt")));
                while (in.ready()) {
                    word.add(in.readLine());
                }
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

    public boolean saveLib() {
        File lib = new File(".\\lib");
        if (!lib.exists()) {
            System.out.println("Running first time startup...");
            System.out.println("Creating library folder...");
            if (lib.mkdir()) {
                System.out.println("Done.");
                System.out.println();
            } else {
                System.out.println("Error creating library folder...");
                System.out.println();
                return false;
            }
            for (String jar : getResources()) {
                System.out.println("Extracting jar: " + jar);
                File output = new File(lib, jar);
                JarOutputStream out = null;
                try {
                    out = new JarOutputStream(new FileOutputStream(output));
                    JarInputStream in = new JarInputStream(getClass().getResourceAsStream("/lib/" + jar));
                    JarEntry entry;
                    while ((entry = in.getNextJarEntry()) != null) {
                        out.putNextEntry(new JarEntry(entry.getName()));
                        byte[] buffer = new byte[4096];
                        int readBytes = 0;
                        while ((readBytes = in.read(buffer)) > 0) {
                            out.write(buffer, 0, readBytes);
                        }
                        out.flush();
                        out.closeEntry();
                    }
                    in.close();
                    out.close();
                    System.out.println("Done.");
                    System.out.println();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    private List<String> getResources() {
        ArrayList<String> list = new ArrayList<>();
        list.add("jna-4.2.1.jar");
        list.add("jna-platform-4.2.1.jar");
        return list;
    }
}

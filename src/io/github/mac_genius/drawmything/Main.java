package io.github.mac_genius.drawmything;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

/**
 * Created by Mac on 11/10/2015.
 */
public class Main {
    private static TreeSet<String> words;
    private static boolean autoType = false;
    private static Thread thread;
    private static AutoType auto;
    private static int wordLimit = 5;

    public static void main(String[] args) {
        header();
        FileLoader file = new FileLoader();
        if (!file.saveLib() || !file.saveDefaultWords()) {
            System.out.println("Terminating program.");
            return;
        }
        words = file.getWords();
        info();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String input = in.readLine();
            while (!input.equalsIgnoreCase("/exit")) {
                if (input.startsWith("/")) {
                    String[] list = input.split(" ");
                    String out = "";
                    for (int i = 1; i < list.length; i++) {
                        out += list[i] + " ";
                    }
                    out = out.trim().toLowerCase();
                    if (list[0].substring(1).equalsIgnoreCase("a")) {
                        if (!words.contains(out)) {
                            words.add(out);
                            file.addWord(out);
                            System.out.println("Added");
                            System.out.println();
                        } else {
                            System.out.println("The list already contains that word.");
                            System.out.println();
                        }
                    } else if (list[0].substring(1).equalsIgnoreCase("clean")) {
                        file.organizeFile(words);
                        System.out.println("The file has been organized.");
                        System.out.println();
                    } else if (list[0].substring(1).equalsIgnoreCase("d")) {
                        words.remove(out);
                        file.deleteWord(words, out);
                        System.out.println("Deleted");
                        System.out.println();
                    } else if (list[0].substring(1).equalsIgnoreCase("list")) {
                        for (String w : words) {
                            System.out.println(w);
                        }
                        System.out.println();
                    } else if (list[0].substring(1).equalsIgnoreCase("amount")) {
                        System.out.println(words.size());
                        System.out.println();
                    } else if (list[0].substring(1).equalsIgnoreCase("help")) {
                        helpMenu();
                    } else if (list[0].substring(1).equalsIgnoreCase("toggle")) {
                        if (out.equalsIgnoreCase("auto-type")) {
                            if (autoType) {
                                autoType = false;
                                System.out.println("Auto-type disabled.");
                                System.out.println();
                            } else {
                                autoType = true;
                                System.out.println("Auto-type enabled.");
                                System.out.println();
                            }
                        }
                    } else if (list[0].substring(1).equalsIgnoreCase("c")) {
                        if (thread != null && thread.isAlive()) {
                            auto.terminate();
                            System.out.println("Stopped auto-typing");
                            System.out.println();
                        }
                    } else if (list[0].substring(1).equalsIgnoreCase("set")) {
                        if (list[1] != null && list[1].equalsIgnoreCase("wordlimit")) {
                            if (list[2] != null) {
                                wordLimit = Integer.parseInt(list[2]);
                                System.out.println("Set the word limit!");
                                System.out.println();
                            }
                        }
                    }
                } else {
                    System.out.println();
                    TreeSet<String> possibleWords = getPossibleWords(input.toLowerCase());
                    if (possibleWords.size() > 0) {
                        for (String s : possibleWords) {
                            System.out.println(s);
                        }
                        if (thread == null || !thread.isAlive()) {
                            if (autoType && possibleWords.size() <= wordLimit) {
                                auto = new AutoType(possibleWords);
                                thread = new Thread(auto);
                                thread.start();
                            }
                        }
                    } else {
                        System.out.println("No words found.");
                    }
                    System.out.println();
                }
                input = in.readLine();
            }
            System.out.println("Saving...");
            file.organizeFile(words);
            file.backup(words);
            System.out.println("Done.");
        } catch (IOException e) {
            System.out.println("Oops");
        }
    }

    public static TreeSet<String> getPossibleWords(String in) {
        TreeSet<String> temp = new TreeSet<>();
        for (String s : words) {
            if (s.length() == in.length()) {
                temp.add(s);
            }
        }
        TreeSet<String> finalW = new TreeSet<>();
        for (String s : temp) {
            boolean match = true;
            for (int i = 0; i < in.length(); i++) {
                if (in.charAt(i) != '_') {
                    if (in.charAt(i) != s.charAt(i)) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                finalW.add(s);
            }
        }
        return finalW;
    }

    public static void header() {
        System.out.println();
        System.out.println("Welcome to Draw My Thing word finder!");
        System.out.println("Version 1.1");
        System.out.println("Copyright John Harrison 2015");
        System.out.println();
    }

    public static void info() {
        System.out.println("If you need help, do /help.");
        System.out.println("");
        System.out.println("To search for a word, simply type the");
        System.out.println("what is displayed: blanks as underscores");
        System.out.println("and letters as themselves. Ex: _ _ c _ _");
        System.out.println("Type __c__ and it will find all matches");
        System.out.println();
    }

    public static void helpMenu() {
        System.out.println();
        System.out.println("<----- Help Menu ----->");
        System.out.println("/a <phrase> Add a new phrase to a list.");
        System.out.println("/d <phrase> Deletes a phrase from a list.");
        System.out.println("/clean Organizes the dictionary file by alphabetical order.");
        System.out.println("/list Lists all of the words in the dictionary.");
        System.out.println("/amount Prints how many words are in the dictionary.");
        System.out.println("/exit Closes the program.");
        System.out.println("/toggle [auto-type] Toggles whether the program auto-types for you.");
        System.out.println("/set wordlimit <int> Sets how many words will trigger auto-type.");
        System.out.println();
    }
}
package io.github.mac_genius.drawmything;

import io.github.mac_genius.drawmything.Util.AutoType;
import io.github.mac_genius.drawmything.Util.FileLoader;
import io.github.mac_genius.drawmything.Util.LibSupport;
import io.github.mac_genius.drawmything.Util.LoggerFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * Created by Mac on 11/10/2015.
 */
public class DrawMyThing {
    public static ArrayList<Word> words;
    public static boolean autoType = false;
    public static Thread thread;
    public static AutoType auto;
    public static int wordLimit = 5;
    public static Logger logger;
    public static boolean run;
    public static LibSupport lib;
    public static FileLoader file;

    public static void main(String[] args) {
        logger = setupLogger();
        run = true;
        header();

        lib = new LibSupport();
        if (!lib.saveLib()) {
            System.out.println("Terminating program.");
            return;
        }

        file = new FileLoader();
        file.updateWordFiles();
        if (!file.saveDefaultWords()) {
            System.out.println("Terminating program.");
            return;
        }

        words = file.getWords();
        info();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String input = in.readLine();
            while (run) {
                if (input.startsWith("/")) {
                    DefaultCommands.executeCommand(input);
                } else {
                    System.out.println();
                    TreeSet<Word> possibleWords = getPossibleWords(input.toLowerCase());
                    if (possibleWords.size() == 1 && possibleWords.first().getWord().equals(input.toLowerCase())) {
                        System.out.println("That word exists.");
                        System.out.println("It has a score of " + possibleWords.first().getRank() + ".");
                        System.out.println("It has appeared " + possibleWords.first().getSearchMatches()[possibleWords.first().getWord().length() - 1] + " time(s) in the game.");
                    } else if (possibleWords.size() > 0) {
                        for (Word s : possibleWords) {
                            String out = s.getWord() + "    |" + s.getRank() + "     ";
                            for (int i = 0; i < (possibleWords.last().getRank() + "").length() - (s.getRank() + "").length(); i++) {
                                out += " ";
                            }
                            out += "|" + s.getSearchMatches()[s.getWord().length() - 1];
                            System.out.println(out);
                        }
                            formatOutput(possibleWords.first().getWord().length() + 4, (possibleWords.last().getRank() + "").length() + 6);

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
                if (run) {
                    input = in.readLine();
                }
            }
            System.out.println("Saving...");
            file.organizeFile(words);
            file.backup(words);
            System.out.println("Done.");
        } catch (IOException e) {
            System.out.println("Oops");
        }
    }

    public static TreeSet<Word> getPossibleWords(String in) {
        int matchSize = in.replaceAll("_", "").length();
        TreeSet<Word> temp = new TreeSet<>();
        for (Word w : words) {
            if (w.getWord().length() == in.length()) {
                temp.add(w);
            }
        }

        TreeSet<Word> finalW = new TreeSet<>();
        for (Word s : temp) {
            boolean match = true;
            for (int i = 0; i < in.length(); i++) {
                if (in.charAt(i) != '_') {
                    if (in.charAt(i) != s.getWord().charAt(i)) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                finalW.add(s);
            }
        }
        if (matchSize > 0) {
            incrementFind(finalW, matchSize);
        }
        return finalW;
    }

    public static void header() {
        System.out.println();
        System.out.println("Welcome to Draw My Thing word finder!");
        System.out.println("Version 1.2");
        System.out.println("Copyright John Harrison 2016");
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
        System.out.println("/c Stops auto-typing .");
        //System.out.println("/s <terms> Searches for words based on meanings.");
        //System.out.println("/search <terms> Searches for words based on meanings.");
        System.out.println("/clean Organizes the dictionary file by alphabetical order.");
        System.out.println("/list Lists all of the words in the dictionary.");
        System.out.println("/amount Prints how many words are in the dictionary.");
        System.out.println("/exit Closes the program.");
        System.out.println("/stop Closes the program.");
        System.out.println("/toggle [auto-type] Toggles whether the program auto-types for you.");
        System.out.println("/set wordlimit <int> Sets how many words will trigger auto-type.");
        System.out.println("/filep <filename> Writes the words out to a text file.");
        System.out.println("/import <filename> Imports words from a file. Supports .txt and .json files.");
        System.out.println();
    }

    private static Logger setupLogger() {
        Logger log = Logger.getLogger("DrawMyThing");
        log.setUseParentHandlers(false);
        Handler handler = new ConsoleHandler();
        handler.setFormatter(new LoggerFormatter());
        log.addHandler(handler);
        return log;
    }

    private static void incrementFind(TreeSet<Word> list, int matchSize) {
        for (Word d : list) {
            d.getSearchMatches()[matchSize - 1]++;
        }
    }

    private static void formatOutput(int wordLength, int largestNum) {
        String title = "Words";
        int increase = 0;
        for (int i = title.length(); i < wordLength; i++) {
            title += " ";
            increase++;
        }
        title += "|Score";

        for (int i = "|score".length(); i < largestNum; i++) {
            title += " ";
        }

        title += "|Used";

        String separate = "";
        for (int i = 0; i < (title.length()); i++) {
            separate += "-";
        }
        System.out.println(separate);
        System.out.println(title);
    }
}
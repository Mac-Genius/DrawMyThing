package io.github.mac_genius.drawmything;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Mac on 3/2/2016.
 */
public class DefaultCommands {
    public static void executeCommand(String command) {
        String commandString;

        if (command.split(" ").length > 1) {
            commandString = command.substring(1, command.indexOf(" ")).toLowerCase();
        } else {
            commandString = command.substring(1);
        }
        String arg = command.substring(command.indexOf(" ") + 1).toLowerCase();
        String[] list = arg.split(" ");

        switch (commandString) {
            case "stop":
            case "exit":
                exit();
                break;
            case "a":
                addWord(list);
                break;
            case "d":
                removeWord(list);
                break;
            case "c":
                stopAutoTyping();
                break;
            case "s":
            case "search":
                searchTerms(list);
                break;
            case "clean":
                cleanFile(list);
                break;
            case "help":
                help();
                break;
            case "list":
                list();
                break;
            case "amount":
                amount();
                break;
            case "toggle":
                toggle(list);
                break;
            case "set":
                setProperty(list);
                break;
            case "filep":
                writeWords(list);
                break;
            case "import":
                importWords(list);
                break;
            default:
                System.out.println("Unknown command. Please do /help of a list of commands.");
                break;
        }
    }

    private static void exit() {
        DrawMyThing.run = false;
    }

    private static void addWord(String[] args) {
        String out = "";
        for (String s : args) {
            out += s + " ";
        }
        out = out.trim();
        DrawMyThing.file.addWord(out);
    }

    private static void removeWord(String[] args) {
        String out = "";
        for (String s : args) {
            out += s + " ";
        }
        out = out.trim();
        DrawMyThing.file.deleteWord(DrawMyThing.words, out);
    }

    private static void cleanFile(String[] args) {
        DrawMyThing.file.organizeFile(DrawMyThing.words);
        System.out.println("The file has been organized.");
        System.out.println();
    }

    private static void list() {
        for (Word w : DrawMyThing.words) {
            System.out.println(w.getWord());
        }
        System.out.println();
    }

    private static void amount() {
        System.out.println(DrawMyThing.words.size());
        System.out.println();
    }

    private static void help() {
        DrawMyThing.helpMenu();
    }

    private static void toggle(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("auto-type")) {
                if (DrawMyThing.autoType) {
                    DrawMyThing.autoType = false;
                    System.out.println("Auto-type disabled.");
                    System.out.println();
                } else {
                    DrawMyThing.autoType = true;
                    System.out.println("Auto-type enabled.");
                    System.out.println();
                }
            }
        } else {
            DrawMyThing.logger.info("Please enter a value to toggle.");
        }
    }

    private static void setProperty(String[] args) {
        if (args.length > 0) {
            if (args[0] != null && args[0].equalsIgnoreCase("wordlimit")) {
                if (args[1] != null) {
                    DrawMyThing.wordLimit = Integer.parseInt(args[1]);
                    System.out.println("Set the word limit!");
                    System.out.println();
                }
            }
        } else {
            DrawMyThing.logger.info("Please specify a property an a value for it.");
        }
    }

    private static void stopAutoTyping() {
        if (DrawMyThing.thread != null && DrawMyThing.thread.isAlive()) {
            DrawMyThing.auto.terminate();
            System.out.println("Stopped auto-typing");
            System.out.println();
        }
    }

    private static void writeWords(String[] args) {
        if (args.length > 0) {
            File wordsOut = new File(args[0]);
            ArrayList<String> wordList = new ArrayList<>();
            for (Word w : DrawMyThing.words) {
                wordList.add(w.getWord());
            }
            Collections.sort(wordList);
            try {
                System.out.println("Writing to the file...");
                BufferedWriter out = new BufferedWriter(new FileWriter(wordsOut, false));
                for (String s : wordList) {
                    out.write(s + "\n");
                }
                out.close();
                System.out.println("Done.");
                System.out.println();
            } catch (IOException e) {
                System.out.println("Failed to write to the file \"" + args[0] + "\".");
            }
        } else {
            System.out.println("Please specify a file to print to.");
        }
    }

    private static void importWords(String[] args) {
        if (args.length > 0) {
            System.out.println("Importing words...");
            try {
                ArrayList<Word> newWords;
                if (args[0].substring(args[0].indexOf(".")).equals(".json")) {
                    newWords = DrawMyThing.file.readJson(new File(args[0]));
                } else if (args[0].substring(args[0].indexOf(".")).equals(".txt")) {
                    BufferedReader in = new BufferedReader(new FileReader(new File(args[0])));
                    newWords = new ArrayList<>();
                    while (in.ready()) {
                        String current = in.readLine().toLowerCase();
                        newWords.add(new Word(current, new int[current.length()], new ArrayList<>()));
                    }
                    in.close();
                } else {
                    newWords = new ArrayList<>();
                    System.out.println("You are using an unsupported file type. Please use .txt or .json and use the correct formatting.");
                }
                int count = 0;
                for (Word w : newWords) {
                    if (!DrawMyThing.words.contains(w)) {
                        System.out.println(w.getWord());
                        DrawMyThing.words.add(w);
                        count++;
                    }
                }
                System.out.println("Done. Added " + count + " word(s) to the dictionary.");
            } catch (IOException e) {
                System.out.println("Could not import words from the file \"" + args[0] + "\".");
            }
        } else {
            System.out.println("Please specify a file to import words from.");
        }
        System.out.println();
    }

    private static void searchTerms(String[] args) {
        // TODO search for terms
    }
}

package io.github.mac_genius.drawmything.Util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import io.github.mac_genius.drawmything.Word;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Mac on 11/11/2015.
 */
public class AutoType implements Runnable {
    private TreeSet<Word> words;
    private boolean running = true;

    public AutoType(TreeSet<Word> words) {
        this.words = words;
    }

    @Override
    public void run() {
        typeWord();
    }

    public void terminate() {
        running = false;
    }

    public void typeWord() {
        try {
            Robot autoInput = new Robot();
            autoInput.delay(1000);
            ArrayList<Word> list = new ArrayList<>(words);
            Collections.shuffle(list);
            Random random = new Random();
            for (Word w : list) {
                if (running && isMinecraft()) {
                    for (int i = 0; i < w.getWord().length(); i++) {
                        autoInput.keyPress((int) w.getWord().toUpperCase().charAt(i));
                        autoInput.keyRelease((int) w.getWord().toUpperCase().charAt(i));
                    }
                    autoInput.keyPress(KeyEvent.VK_ENTER);
                    autoInput.keyRelease(KeyEvent.VK_ENTER);
                    autoInput.delay(200);
                    autoInput.keyPress(KeyEvent.VK_T);
                    autoInput.keyRelease(KeyEvent.VK_T);
                    autoInput.delay((random.nextInt(300) + 100) * w.getWord().length());
                } else {
                    break;
                }
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private boolean isMinecraft() {
        char[] buffer = new char[2048];
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowText(hwnd, buffer, 1048);
        String name = Native.toString(buffer);
        if (name.toLowerCase().contains("minecraft")) {
            return true;
        } else {
            return false;
        }
    }
}

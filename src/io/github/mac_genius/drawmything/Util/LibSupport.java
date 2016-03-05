package io.github.mac_genius.drawmything.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * Created by Mac on 3/2/2016.
 */
public class LibSupport {
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
        } else {
            for (String jar : getResources()) {
                File output = new File(lib, jar);
                if (!output.exists()) {
                    System.out.println("Extracting jar: " + jar);
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
        }
        return true;
    }

    private List<String> getResources() {
        ArrayList<String> list = new ArrayList<>();
        list.add("jna-4.2.1.jar");
        list.add("jna-platform-4.2.1.jar");
        list.add("gson-2.6.2.jar");
        return list;
    }
}

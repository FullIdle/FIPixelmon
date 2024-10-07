package com.fipixelmonmod.fipixelmon.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    public static List<File> loadedZipFile = new ArrayList<>();

    public static boolean isZip(File file) {
        String name = file.getName();
        return name.endsWith(".zip") ||
                name.endsWith(".jar") ||
                name.endsWith(".7z") ||
                name.endsWith(".rar");
    }
}

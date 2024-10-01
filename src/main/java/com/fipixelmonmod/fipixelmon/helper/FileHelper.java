package com.fipixelmonmod.fipixelmon.helper;

import java.io.File;

public class FileHelper {
    public static boolean isZip(File file) {
        String name = file.getName();
        return name.endsWith(".zip") ||
                name.endsWith(".jar") ||
                name.endsWith(".7z") ||
                name.endsWith(".rar");
    }
}

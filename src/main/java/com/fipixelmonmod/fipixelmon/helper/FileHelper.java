package com.fipixelmonmod.fipixelmon.helper;

import java.io.File;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileHelper {
    public static List<File> loadedZipFile = new ArrayList<>();

    public static boolean isZip(File file) {
        String name = file.getName();
        return name.endsWith(".zip") ||
                name.endsWith(".jar") ||
                name.endsWith(".7z") ||
                name.endsWith(".rar");
    }

    @Deprecated
    public static List<String> getZipFileList(ZipFile zipFile,String path){
        path = repPath(path);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        ArrayList<String> list = new ArrayList<>();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            String name = zipEntry.getName();
            if (name.equals(path + "/")) continue;
            if (name.startsWith(path)) list.add(name);
        }
        return list;
    }

    public static String repPath(String path){
        path = path.replace("\\","/");
        path = path.charAt(path.length()-1) == '/' ? path.substring(0,path.length()-1): path;
        return path.charAt(0) == '/' ? path.substring(1) : path;
    }
}

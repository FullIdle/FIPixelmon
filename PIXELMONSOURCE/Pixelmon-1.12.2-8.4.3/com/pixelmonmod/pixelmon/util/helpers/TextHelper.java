package com.pixelmonmod.pixelmon.util.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHelper {
   private static final String regex = "(&([a-f0-9k-r]))";
   private static final Pattern pattern = Pattern.compile("(&([a-f0-9k-r]))", 2);

   public static String format(String line) {
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
         line = line.replaceAll("(&([a-f0-9k-r]))", "§$2");
      }

      return line;
   }
}

package com.pixelmonmod.pixelmon.client.models.obj;

import com.pixelmonmod.pixelmon.client.models.obj.parser.DefaultParser;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;
import java.util.HashMap;
import java.util.regex.Pattern;

public abstract class LineParserFactory {
   private static final Pattern COMPILE = Pattern.compile("  ");
   private static final Pattern PATTERN = Pattern.compile("\t");
   protected HashMap parsers = new HashMap();
   protected WavefrontObject object = null;

   public LineParser getLineParser(String line) {
      if (line == null) {
         return null;
      } else {
         line = COMPILE.matcher(line).replaceAll(" ");
         line = PATTERN.matcher(line).replaceAll("");
         String[] lineWords = line.split(" ");
         if (lineWords.length < 1) {
            return new DefaultParser();
         } else {
            String lineType = lineWords[0];
            LineParser parser = (LineParser)this.parsers.get(lineType);
            if (parser == null) {
               parser = new DefaultParser();
            }

            ((LineParser)parser).setWords(lineWords);
            return (LineParser)parser;
         }
      }
   }
}

package com.pixelmonmod.pixelmon.client.models.obj.parser.mtl;

import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MaterialFileParser extends LineParser {
   HashMap materials = new HashMap();
   private WavefrontObject object;
   private MtlLineParserFactory parserFactory = null;

   public MaterialFileParser(WavefrontObject object) {
      this.object = object;
      this.parserFactory = new MtlLineParserFactory(object);
   }

   public void incoporateResults(WavefrontObject wavefrontObject) {
   }

   public void parse() {
      String pathToMTL = this.words[1];
      InputStream fileInput = this.getClass().getResourceAsStream(pathToMTL);
      File file;
      if (fileInput == null) {
         try {
            file = new File(pathToMTL);
            if (file.exists()) {
               fileInput = new FileInputStream(file);
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      file = null;

      try {
         BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)fileInput));

         String currentLine;
         while((currentLine = in.readLine()) != null) {
            LineParser parser = this.parserFactory.getLineParser(currentLine);
            parser.parse();
            parser.incoporateResults(this.object);
         }

         in.close();
      } catch (Exception var7) {
         var7.printStackTrace();
         System.out.println("Error on line:" + file);
         throw new RuntimeException("Error parsing :'" + pathToMTL + "'");
      }
   }
}

package com.pixelmonmod.pixelmon.client.models.obj.parser;

import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;

public abstract class LineParser {
   protected String[] words = null;

   public void setWords(String[] words) {
      this.words = words;
   }

   public abstract void parse();

   public abstract void incoporateResults(WavefrontObject var1);
}

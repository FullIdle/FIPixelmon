package com.pixelmonmod.pixelmon.client.models.obj.parser.obj;

import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class MaterialParser extends LineParser {
   String materialName = "";

   public void parse() {
      this.materialName = this.words[1];
   }

   public void incoporateResults(WavefrontObject wavefrontObject) {
   }
}

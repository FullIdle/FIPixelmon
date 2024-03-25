package com.pixelmonmod.pixelmon.client.models.obj.parser.mtl;

import com.pixelmonmod.pixelmon.client.models.obj.Material;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class NsParser extends LineParser {
   float ns;

   public void incoporateResults(WavefrontObject wavefrontObject) {
      Material currentMaterial = wavefrontObject.getCurrentMaterial();
      currentMaterial.setShininess(this.ns);
   }

   public void parse() {
      try {
         this.ns = Float.parseFloat(this.words[1]);
      } catch (Exception var2) {
         throw new RuntimeException("VertexParser Error");
      }
   }
}

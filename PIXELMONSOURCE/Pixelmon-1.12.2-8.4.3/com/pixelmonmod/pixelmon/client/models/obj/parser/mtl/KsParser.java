package com.pixelmonmod.pixelmon.client.models.obj.parser.mtl;

import com.pixelmonmod.pixelmon.client.models.obj.Material;
import com.pixelmonmod.pixelmon.client.models.obj.Vertex;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class KsParser extends LineParser {
   Vertex ks = null;

   public void incoporateResults(WavefrontObject wavefrontObject) {
      Material currentMaterial = wavefrontObject.getCurrentMaterial();
      currentMaterial.setKs(this.ks);
   }

   public void parse() {
      this.ks = new Vertex();

      try {
         this.ks.setX(Float.parseFloat(this.words[1]));
         this.ks.setY(Float.parseFloat(this.words[2]));
         this.ks.setZ(Float.parseFloat(this.words[3]));
      } catch (Exception var2) {
         throw new RuntimeException("VertexParser Error");
      }
   }
}

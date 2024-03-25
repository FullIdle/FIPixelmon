package com.pixelmonmod.pixelmon.client.models.obj.parser.mtl;

import com.pixelmonmod.pixelmon.client.models.obj.Material;
import com.pixelmonmod.pixelmon.client.models.obj.Vertex;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class KaParser extends LineParser {
   Vertex ka = null;

   public void incoporateResults(WavefrontObject wavefrontObject) {
      Material currentMaterial = wavefrontObject.getCurrentMaterial();
      currentMaterial.setKa(this.ka);
   }

   public void parse() {
      this.ka = new Vertex();

      try {
         this.ka.setX(Float.parseFloat(this.words[1]));
         this.ka.setY(Float.parseFloat(this.words[2]));
         this.ka.setZ(Float.parseFloat(this.words[3]));
      } catch (Exception var2) {
         throw new RuntimeException("VertexParser Error");
      }
   }
}

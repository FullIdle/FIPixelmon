package com.pixelmonmod.pixelmon.client.models.obj.parser.mtl;

import com.pixelmonmod.pixelmon.client.models.obj.Material;
import com.pixelmonmod.pixelmon.client.models.obj.Vertex;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class KdParser extends LineParser {
   Vertex kd = null;

   public void incoporateResults(WavefrontObject wavefrontObject) {
      Material currentMaterial = wavefrontObject.getCurrentMaterial();
      currentMaterial.setKd(this.kd);
   }

   public void parse() {
      this.kd = new Vertex();

      try {
         this.kd.setX(Float.parseFloat(this.words[1]));
         this.kd.setY(Float.parseFloat(this.words[2]));
         this.kd.setZ(Float.parseFloat(this.words[3]));
      } catch (Exception var2) {
         throw new RuntimeException("VertexParser Error");
      }
   }
}

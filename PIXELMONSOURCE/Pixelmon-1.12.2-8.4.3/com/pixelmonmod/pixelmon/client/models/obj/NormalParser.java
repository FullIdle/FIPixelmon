package com.pixelmonmod.pixelmon.client.models.obj;

import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class NormalParser extends LineParser {
   Vertex vertex = null;

   public void parse() {
      this.vertex = new Vertex();

      try {
         this.vertex.setX(Float.parseFloat(this.words[1]));
         this.vertex.setY(Float.parseFloat(this.words[2]));
         this.vertex.setZ(Float.parseFloat(this.words[3]));
      } catch (Exception var2) {
         throw new RuntimeException("NormalParser Error");
      }
   }

   public void incoporateResults(WavefrontObject wavefrontObject) {
      wavefrontObject.getNormals().add(this.vertex);
   }
}

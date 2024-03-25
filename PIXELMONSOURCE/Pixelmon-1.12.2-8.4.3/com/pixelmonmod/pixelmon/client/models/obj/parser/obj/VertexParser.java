package com.pixelmonmod.pixelmon.client.models.obj.parser.obj;

import com.pixelmonmod.pixelmon.client.models.obj.Vertex;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class VertexParser extends LineParser {
   Vertex vertex = null;

   public void parse() {
      this.vertex = new Vertex();

      try {
         this.vertex.setX(Float.parseFloat(this.words[1]));
         this.vertex.setY(Float.parseFloat(this.words[2]));
         this.vertex.setZ(Float.parseFloat(this.words[3]));
      } catch (Exception var2) {
         throw new RuntimeException("VertexParser Error");
      }
   }

   public void incoporateResults(WavefrontObject wavefrontObject) {
      this.vertex.setX((this.vertex.getX() + wavefrontObject.translate.getX()) * wavefrontObject.xScale);
      this.vertex.setY((this.vertex.getY() + wavefrontObject.translate.getY()) * wavefrontObject.yScale);
      this.vertex.setZ((this.vertex.getZ() + wavefrontObject.translate.getZ()) * wavefrontObject.zScale);
      wavefrontObject.getVertices().add(this.vertex);
   }
}

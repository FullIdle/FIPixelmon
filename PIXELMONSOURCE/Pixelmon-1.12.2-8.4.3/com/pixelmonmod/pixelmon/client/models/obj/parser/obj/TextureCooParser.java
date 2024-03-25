package com.pixelmonmod.pixelmon.client.models.obj.parser.obj;

import com.pixelmonmod.pixelmon.client.models.obj.TextureCoordinate;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class TextureCooParser extends LineParser {
   private TextureCoordinate coordinate = null;

   public void parse() {
      this.coordinate = new TextureCoordinate();

      try {
         if (this.words.length >= 2) {
            this.coordinate.setU(Float.parseFloat(this.words[1]));
         }

         if (this.words.length >= 3) {
            this.coordinate.setV(1.0F - Float.parseFloat(this.words[2]));
         }

         if (this.words.length >= 4) {
            this.coordinate.setW(Float.parseFloat(this.words[3]));
         }

      } catch (Exception var2) {
         throw new RuntimeException("TextureParser Error");
      }
   }

   public void incoporateResults(WavefrontObject wavefrontObject) {
      wavefrontObject.getTextureList().add(this.coordinate);
   }
}

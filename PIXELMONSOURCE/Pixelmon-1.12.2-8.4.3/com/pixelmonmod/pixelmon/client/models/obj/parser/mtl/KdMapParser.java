package com.pixelmonmod.pixelmon.client.models.obj.parser.mtl;

import com.pixelmonmod.pixelmon.client.models.obj.Material;
import com.pixelmonmod.pixelmon.client.models.obj.Texture;
import com.pixelmonmod.pixelmon.client.models.obj.TextureLoader;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class KdMapParser extends LineParser {
   private Texture texture = null;
   private String texName;

   public KdMapParser(WavefrontObject object) {
   }

   public void incoporateResults(WavefrontObject wavefrontObject) {
      if (this.texture != null) {
         Material currentMaterial = wavefrontObject.getCurrentMaterial();
         currentMaterial.texName = this.texName;
         currentMaterial.setTexture(this.texture);
      }

   }

   public void parse() {
      String textureFileName = this.words[this.words.length - 1];
      this.texName = textureFileName;
      this.texture = TextureLoader.instance().loadTexture(textureFileName);
   }
}

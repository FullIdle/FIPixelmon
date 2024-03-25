package com.pixelmonmod.pixelmon.client.models.obj.parser.mtl;

import com.pixelmonmod.pixelmon.client.models.obj.Material;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class MaterialParser extends LineParser {
   String materialName = "";

   public void incoporateResults(WavefrontObject wavefrontObject) {
      Material newMaterial = new Material(this.materialName);
      wavefrontObject.getMaterials().put(this.materialName, newMaterial);
      wavefrontObject.setCurrentMaterial(newMaterial);
   }

   public void parse() {
      this.materialName = this.words[1];
   }
}

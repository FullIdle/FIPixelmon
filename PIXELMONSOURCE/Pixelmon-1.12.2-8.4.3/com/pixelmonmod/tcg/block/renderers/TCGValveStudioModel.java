package com.pixelmonmod.tcg.block.renderers;

import com.pixelmonmod.pixelmon.client.models.smd.GabeNewellException;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import net.minecraft.util.ResourceLocation;

public class TCGValveStudioModel extends ValveStudioModel {
   public TCGValveStudioModel(ResourceLocation resource) throws GabeNewellException {
      super(resource);
   }

   public ResourceLocation getResource(String fileName) {
      String urlAsString = this.resource.func_110623_a();
      int lastIndex = urlAsString.lastIndexOf(47);
      String startString = urlAsString.substring(0, lastIndex);
      return new ResourceLocation("tcg", startString + "/" + fileName);
   }
}

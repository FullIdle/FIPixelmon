package com.pixelmonmod.pixelmon.enums;

import net.minecraft.util.ResourceLocation;

public enum EnumBreedingParticles {
   grey(1, new ResourceLocation("pixelmon:textures/particles/heartGrey.png")),
   purple(2, new ResourceLocation("pixelmon:textures/particles/heartPurple.png")),
   blue(3, new ResourceLocation("pixelmon:textures/particles/heartBlue.png")),
   yellow(4, new ResourceLocation("pixelmon:textures/particles/heartYellow.png")),
   red(5, new ResourceLocation("pixelmon:textures/particles/heartRed.png"));

   public int index;
   public ResourceLocation location;

   private EnumBreedingParticles(int index, ResourceLocation location) {
      this.index = index;
      this.location = location;
   }

   public static EnumBreedingParticles getFromIndex(int index) {
      EnumBreedingParticles[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumBreedingParticles e = var1[var3];
         if (e.index == index) {
            return e;
         }
      }

      return grey;
   }
}

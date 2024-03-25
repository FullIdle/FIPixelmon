package com.pixelmonmod.pixelmon.enums;

public enum EnumBreedingStrength {
   NONE(0.0F),
   LOW(0.5F),
   MEDIUM(1.0F),
   HIGH(1.5F),
   MAX(2.0F);

   public final float value;

   private EnumBreedingStrength(float breedingStrength) {
      this.value = breedingStrength;
   }

   public static EnumBreedingStrength of(float breedingStrength) {
      for(int i = values().length - 1; i >= 0; --i) {
         if (values()[i].value <= breedingStrength) {
            return values()[i];
         }
      }

      return NONE;
   }
}

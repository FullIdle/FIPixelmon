package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;

public enum Gender implements IEnumForm {
   Male,
   Female,
   None;

   public String getSpriteSuffix(boolean shiny) {
      return this == None ? "" : "-" + this.name().toLowerCase();
   }

   public String getFormSuffix() {
      return this == None ? "" : this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.gender.form." + this.name().toLowerCase();
   }

   public boolean isCompatible(Gender otherGender) {
      return this == Male && otherGender == Female || this == Female && otherGender == Male;
   }

   public static Gender getGender(short ord) {
      Gender[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Gender g = var1[var3];
         if (g.ordinal() == ord) {
            return g;
         }
      }

      return Male;
   }

   public static Gender getGender(String name) {
      Gender[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Gender gender = var1[var3];
         if (gender.name().toLowerCase().startsWith(name.toLowerCase())) {
            return gender;
         }
      }

      return null;
   }

   public static Gender getRandomGender(BaseStats baseStats) {
      if (baseStats.getMalePercent() < 0.0) {
         return None;
      } else {
         return RandomHelper.rand.nextDouble() * 100.0 < baseStats.getMalePercent() ? Male : Female;
      }
   }

   public String getName() {
      return this.name();
   }
}

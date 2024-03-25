package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.TerrainExamine;

public enum EnumBurmy implements IEnumForm {
   Plant,
   Sandy,
   Trash;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.burmy.form." + this.name().toLowerCase();
   }

   public static EnumBurmy getFromIndex(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return Plant;
      }
   }

   public static EnumBurmy getFromType(TerrainExamine.TerrainType type) {
      switch (type) {
         case Grass:
            return Plant;
         case Cave:
         case Sand:
            return Sandy;
         default:
            return Trash;
      }
   }

   public String getName() {
      return this.name();
   }
}

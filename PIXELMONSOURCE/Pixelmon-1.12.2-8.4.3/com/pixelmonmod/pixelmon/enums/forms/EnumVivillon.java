package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumVivillon implements IEnumForm {
   ARCHIPELAGO,
   CONTINENTAL,
   ELEGANT,
   GARDEN,
   HIGHPLAINS,
   ICYSNOW,
   JUNGLE,
   MARINE,
   MEADOW,
   MODERN,
   MONSOON,
   OCEAN,
   POLAR,
   RIVER,
   SANDSTORM,
   SAVANNA,
   SUN,
   TUNDRA,
   FANCY,
   POKEBALL;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.vivillon.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

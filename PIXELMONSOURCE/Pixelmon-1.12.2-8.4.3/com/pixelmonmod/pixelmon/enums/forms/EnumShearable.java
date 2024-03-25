package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumShearable implements IEnumForm {
   NORMAL(0),
   SHORN(0),
   BLACK(15),
   BLUE(11),
   BROWN(12),
   CYAN(9),
   GRAY(7),
   GREEN(13),
   LIGHTBLUE(3),
   LIGHTGRAY(8),
   LIME(5),
   MAGENTA(2),
   ORANGE(1),
   PINK(6),
   PURPLE(10),
   RED(14),
   YELLOW(4);

   private int color;

   private EnumShearable(int i) {
      this.color = i;
   }

   public String getFormSuffix() {
      return this != NORMAL && this != SHORN ? "-" + this.name().toLowerCase() : "";
   }

   public String getSpriteSuffix(boolean shiny) {
      return "";
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.shearable.form." + this.name().toLowerCase();
   }

   public int getColorMeta() {
      return this.color;
   }

   public String getName() {
      return this.name();
   }
}

package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.enums.EnumType;

public enum EnumArceus implements IEnumForm {
   NORMAL(EnumType.Normal),
   GRASS(EnumType.Grass),
   FIRE(EnumType.Fire),
   WATER(EnumType.Water),
   FLYING(EnumType.Flying),
   BUG(EnumType.Bug),
   POISON(EnumType.Poison),
   ELECTRIC(EnumType.Electric),
   PSYCHIC(EnumType.Psychic),
   ROCK(EnumType.Rock),
   GROUND(EnumType.Ground),
   DARK(EnumType.Dark),
   GHOST(EnumType.Ghost),
   STEEL(EnumType.Steel),
   FIGHTING(EnumType.Fighting),
   ICE(EnumType.Ice),
   DRAGON(EnumType.Dragon),
   FAIRY(EnumType.Fairy);

   public final EnumType type;

   private EnumArceus(EnumType type) {
      this.type = type;
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public static EnumArceus getForm(EnumType type) {
      EnumArceus[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumArceus arceus = var1[var3];
         if (arceus.type == type) {
            return arceus;
         }
      }

      return NORMAL;
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "type." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

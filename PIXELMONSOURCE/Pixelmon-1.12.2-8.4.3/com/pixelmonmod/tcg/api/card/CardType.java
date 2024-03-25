package com.pixelmonmod.tcg.api.card;

import net.minecraft.util.ResourceLocation;

public enum CardType {
   BASIC("BASIC"),
   STAGE1("STAGE1"),
   STAGE2("STAGE2"),
   MEGA("MEGA"),
   EX("EX"),
   LVLX("LVLX"),
   BREAK("BREAK"),
   LEGEND("LEGEND"),
   GX("GX"),
   TRAINER("TRAINER"),
   STADIUM("STADIUM"),
   ITEM("ITEM"),
   SUPPORTER("SUPPORTER"),
   ENERGY("ENERGY"),
   TOOL("TOOL"),
   TM("TECHNICAL MACHINE"),
   ASPEC("A-SPEC"),
   COSMETIC("COSMETIC");

   private final String asString;

   private CardType(String asString) {
      this.asString = asString;
   }

   public boolean isPokemon() {
      return this == BASIC || this == STAGE1 || this == STAGE2 || this == MEGA || this == LEGEND || this == LVLX || this == EX;
   }

   public boolean isCosmetic() {
      return this == COSMETIC;
   }

   public static CardType getCardTypeFromString(String name) {
      CardType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CardType value = var1[var3];
         if (value.getUnlocalizedString().equalsIgnoreCase(name)) {
            return value;
         }
      }

      return null;
   }

   public ResourceLocation getIcon() {
      if (this.isCosmetic()) {
         return new ResourceLocation("tcg:gui/cards/icons/cosmetic.png");
      } else {
         return this == EX ? new ResourceLocation("tcg:gui/cards/icons/basic.png") : new ResourceLocation("tcg:gui/cards/icons/" + this.asString + ".png");
      }
   }

   public String getUnlocalizedString() {
      return this.asString.toLowerCase();
   }
}

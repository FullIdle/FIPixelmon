package com.pixelmonmod.pixelmon.enums.battle;

import com.pixelmonmod.pixelmon.util.ITranslatable;

public enum BagSection implements ITranslatable {
   Pokeballs(0, "gui.choosebag.pokeballs"),
   HP(1, "gui.choosebag.hppp"),
   BattleItems(2, "gui.choosebag.battleitems"),
   StatusRestore(3, "gui.choosebag.statusrestore");

   public String translationKey;
   public int index;

   private BagSection(int index, String translationKey) {
      this.index = index;
      this.translationKey = translationKey;
   }

   public String getUnlocalizedName() {
      return this.translationKey;
   }

   public static BagSection getSection(int index) {
      try {
         return values()[index];
      } catch (Exception var2) {
         return null;
      }
   }

   public static boolean hasBag(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}

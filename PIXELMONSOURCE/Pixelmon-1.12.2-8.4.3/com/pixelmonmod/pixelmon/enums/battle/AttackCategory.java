package com.pixelmonmod.pixelmon.enums.battle;

import net.minecraft.util.text.translation.I18n;

public enum AttackCategory {
   PHYSICAL("attack.category.physical", "Physical"),
   SPECIAL("attack.category.special", "Special"),
   STATUS("attack.category.status", "Status");

   public final String lang;
   public final String englishName;

   private AttackCategory(String lang, String name) {
      this.lang = lang;
      this.englishName = name;
   }

   public static AttackCategory parseOrNull(String name) {
      try {
         return valueOf(name);
      } catch (Exception var2) {
         return null;
      }
   }

   public String getLocalizedName() {
      return I18n.func_74838_a(this.lang);
   }
}

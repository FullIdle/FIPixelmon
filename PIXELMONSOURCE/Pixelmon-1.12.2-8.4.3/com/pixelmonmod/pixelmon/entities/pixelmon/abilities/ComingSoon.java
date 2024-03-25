package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

public class ComingSoon extends AbilityBase {
   private final String truAbility;
   public static ComingSoon noAbility = new ComingSoon("");

   public ComingSoon(String truAbility) {
      this.truAbility = truAbility;
   }

   public String getTrueAbility() {
      return this.truAbility;
   }
}

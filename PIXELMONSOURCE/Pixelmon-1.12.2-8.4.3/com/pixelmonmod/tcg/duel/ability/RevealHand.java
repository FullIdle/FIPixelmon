package com.pixelmonmod.tcg.duel.ability;

public class RevealHand extends BaseAbilityEffect {
   public RevealHand() {
      super("RevealHand");
   }

   public boolean isPassive() {
      return true;
   }

   public boolean revealHand() {
      return true;
   }
}

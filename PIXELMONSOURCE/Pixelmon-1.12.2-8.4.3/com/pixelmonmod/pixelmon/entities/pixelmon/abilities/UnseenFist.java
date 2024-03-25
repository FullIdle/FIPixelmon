package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class UnseenFist extends AbilityBase {
   public boolean doesAttackUserIgnoreProtect(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a) {
      return a.originalMove != null ? a.originalMove.getActualMove().getMakesContact() : a.getActualMove().getMakesContact();
   }
}

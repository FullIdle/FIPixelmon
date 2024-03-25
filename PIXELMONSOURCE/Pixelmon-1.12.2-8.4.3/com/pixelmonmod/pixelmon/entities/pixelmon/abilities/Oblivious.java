package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Oblivious extends PreventStatus {
   public Oblivious() {
      super("pixelmon.abilities.oblivious", "pixelmon.abilities.obliviouscure", StatusType.Infatuated, StatusType.Taunt);
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.isAttack("Captivate") && !AbilityBase.ignoreAbility(user, pokemon)) {
         user.bc.sendToAll(this.immuneText, user.getNickname());
         return false;
      } else {
         return true;
      }
   }
}

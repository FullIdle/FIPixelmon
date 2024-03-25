package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class PreventSleep extends PreventStatus {
   public PreventSleep(String immuneText, String cureText) {
      super(immuneText, cureText, StatusType.Sleep);
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.isAttack("Yawn")) {
         user.bc.sendToAll(this.immuneText, pokemon.getNickname());
         return false;
      } else {
         return true;
      }
   }
}

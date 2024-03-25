package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class UnderGround extends StatusBase {
   public UnderGround() {
      super(StatusType.UnderGround);
   }

   public boolean stopsSwitching() {
      return true;
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (user.attack.isAttack("Earthquake", "Magnitude")) {
         Attack var10000 = user.attack;
         var10000.movePower *= 2;
         user.attack.moveAccuracy = -1;
         return false;
      } else if (user.attack.isAttack("Fissure")) {
         user.attack.moveAccuracy = -1;
         return false;
      } else {
         return user.attack.moveAccuracy != -2 && !pokemon.bc.simulateMode;
      }
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.battletext.missedattack", pokemon.getNickname());
   }
}

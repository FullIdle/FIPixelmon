package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Submerged extends StatusBase {
   public Submerged() {
      super(StatusType.Submerged);
   }

   public boolean stopsSwitching() {
      return true;
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (user.attack.isAttack("Surf", "Whirlpool")) {
         Attack var10000 = user.attack;
         var10000.movePower *= 2;
         return false;
      } else {
         return user.attack.moveAccuracy != -2 && !pokemon.bc.simulateMode;
      }
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.battletext.missedattack", pokemon.getNickname());
   }
}

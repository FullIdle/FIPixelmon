package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Flying extends StatusBase {
   public Flying() {
      super(StatusType.Flying);
   }

   public boolean stopsSwitching() {
      return true;
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (user.attack.isAttack("Gust", "Twister")) {
         Attack var10000 = user.attack;
         var10000.movePower *= 2;
         return false;
      } else {
         return user.attack.moveAccuracy != -2 && !user.attack.isAttack("Hurricane", "Sky Uppercut", "Smack Down", "Thousand Arrows", "Thunder", "Whirlwind") && !pokemon.bc.simulateMode;
      }
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.battletext.missedattack", pokemon.getNickname());
   }
}

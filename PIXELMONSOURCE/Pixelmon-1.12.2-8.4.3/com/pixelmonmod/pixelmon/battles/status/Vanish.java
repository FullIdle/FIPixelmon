package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Vanish extends StatusBase {
   public Vanish() {
      super(StatusType.Vanish);
   }

   public boolean stopsSwitching() {
      return true;
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return user.targets.contains(pokemon) && user.attack.moveAccuracy != -2;
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.battletext.missedattack", pokemon.getNickname());
   }
}

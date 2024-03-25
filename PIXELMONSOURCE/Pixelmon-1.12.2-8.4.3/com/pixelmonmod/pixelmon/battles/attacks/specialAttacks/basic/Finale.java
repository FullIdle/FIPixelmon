package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Iterator;

public class Finale extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      Iterator var3 = user.getParticipant().getActiveUnfaintedPokemon().iterator();

      while(var3.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var3.next();
         pw.healByPercent(16.666666F);
         user.bc.sendToAll("pixelmon.effect.restorehealth", pw.getNickname());
      }

   }
}

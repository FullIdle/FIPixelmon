package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Paralysis;
import java.util.ArrayList;
import java.util.Iterator;

public class VoltCrash extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      ArrayList targetAllies = target.getTeamPokemon();
      Iterator var4 = targetAllies.iterator();

      while(var4.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var4.next();
         Paralysis.paralyze(user, pw, user.attack, true);
      }

   }
}

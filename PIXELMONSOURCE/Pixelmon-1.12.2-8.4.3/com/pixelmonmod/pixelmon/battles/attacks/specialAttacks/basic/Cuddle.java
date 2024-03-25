package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Infatuated;
import java.util.ArrayList;
import java.util.Iterator;

public class Cuddle extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      ArrayList targets = user.bc.getAdjacentPokemonAndSelf(target);
      Iterator var4 = targets.iterator();

      while(var4.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var4.next();
         Infatuated.infatuate(user, pw, true);
      }

   }
}

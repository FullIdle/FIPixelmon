package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Moxie extends AbilityBase {
   public void tookDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper pokemon, Attack a) {
      if (pokemon.isFainted() && (!a.getMove().getMakesContact() || !(pokemon.getBattleAbility() instanceof Mummy))) {
         this.sendActivatedMessage(user);
         user.getBattleStats().modifyStat(1, (StatsType)StatsType.Attack);
      }

   }
}

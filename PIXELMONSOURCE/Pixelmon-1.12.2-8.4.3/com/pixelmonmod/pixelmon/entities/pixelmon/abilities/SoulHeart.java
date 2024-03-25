package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SoulHeart extends AbilityBase {
   public void onAllyFaint(PixelmonWrapper pokemon, PixelmonWrapper ally, PixelmonWrapper source) {
      this.applyBoost(pokemon);
   }

   public void onFoeFaint(PixelmonWrapper pokemon, PixelmonWrapper foe, PixelmonWrapper source) {
      this.applyBoost(pokemon);
   }

   private void applyBoost(PixelmonWrapper pokemon) {
      if (pokemon.getBattleStats().statCanBeRaised(StatsType.SpecialAttack)) {
         pokemon.bc.sendToAll("pixelmon.abilities.soulheart.activate", pokemon.getNickname());
         (new StatsEffect(StatsType.SpecialAttack, 1, true)).applyStatEffect(pokemon, pokemon, (AttackBase)null);
      }

   }
}

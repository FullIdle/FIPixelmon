package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Download;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Moody;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SpeedBoost;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.Iterator;

class SpeedPassClause extends BattleClause {
   private MoveClause batonPassClause = new MoveClause("", new String[]{"Baton Pass"});

   SpeedPassClause() {
      super("speedpass");
   }

   public boolean validateSingle(Pokemon pokemon) {
      if (this.batonPassClause.validateSingle(pokemon)) {
         return true;
      } else {
         boolean hasSpeedBoost = false;
         boolean hasOtherBoost = false;
         AbilityBase ability = pokemon.getAbility();
         if (ability.isAbility(Moody.class)) {
            return false;
         } else {
            if (ability.isAbility(SpeedBoost.class)) {
               hasSpeedBoost = true;
            } else if (ability.isAbility(Download.class)) {
               hasOtherBoost = true;
            }

            Attack[] var5 = pokemon.getMoveset().attacks;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Attack attack = var5[var7];
               if (attack != null) {
                  if (attack.isAttack("Geomancy")) {
                     return false;
                  }

                  Iterator var9 = attack.getMove().effects.iterator();

                  while(var9.hasNext()) {
                     EffectBase effect = (EffectBase)var9.next();
                     if (effect.getChance() >= 100.0F && effect instanceof StatsEffect) {
                        StatsEffect statsEffect = (StatsEffect)effect;
                        StatsType stat = statsEffect.getStatsType();
                        if (stat == StatsType.Speed) {
                           hasSpeedBoost = true;
                        } else {
                           hasOtherBoost = true;
                        }
                     }
                  }

                  if (attack.isAttack("Belly Drum", "Curse", "Growth", "Magnetic Flux", "Minimize", "Rototiller", "Skull Bash", "Stockpile")) {
                     hasOtherBoost = true;
                  }
               }
            }

            return !hasSpeedBoost || !hasOtherBoost;
         }
      }
   }
}

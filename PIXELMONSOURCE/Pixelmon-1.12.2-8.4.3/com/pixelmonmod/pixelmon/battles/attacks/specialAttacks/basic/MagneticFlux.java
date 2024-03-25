package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Minus;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Plus;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;

public class MagneticFlux extends SpecialAttackBase {
   private static final transient StatsType[] raiseStats;

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex > 0) {
         return AttackResult.succeeded;
      } else {
         boolean succeeded = false;
         Iterator var4 = user.getTeamPokemon().iterator();

         while(true) {
            PixelmonWrapper pw;
            AbilityBase ability;
            do {
               if (!var4.hasNext()) {
                  if (succeeded) {
                     return AttackResult.succeeded;
                  }

                  user.bc.sendToAll("pixelmon.effect.effectfailed");
                  return AttackResult.failed;
               }

               pw = (PixelmonWrapper)var4.next();
               ability = pw.getBattleAbility();
            } while(!(ability instanceof Plus) && !(ability instanceof Minus));

            pw.getBattleStats().modifyStat(1, (StatsType[])raiseStats);
            succeeded = true;
         }
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = pw.getTeamPokemon().iterator();

      while(true) {
         AbilityBase ability;
         do {
            if (!var7.hasNext()) {
               return;
            }

            PixelmonWrapper pokemon = (PixelmonWrapper)var7.next();
            ability = pokemon.getBattleAbility();
         } while(!(ability instanceof Plus) && !(ability instanceof Minus));

         userChoice.raiseWeight(15.0F);
      }
   }

   static {
      raiseStats = new StatsType[]{StatsType.Defence, StatsType.SpecialDefence};
   }
}

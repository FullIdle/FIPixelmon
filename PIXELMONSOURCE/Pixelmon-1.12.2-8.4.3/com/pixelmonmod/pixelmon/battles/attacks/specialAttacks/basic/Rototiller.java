package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;

public class Rototiller extends SpecialAttackBase {
   private static final transient StatsType[] raiseStats;

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex > 0) {
         return AttackResult.succeeded;
      } else {
         boolean succeeded = false;
         Iterator var4 = user.bc.getActiveUnfaintedPokemon().iterator();

         while(var4.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var4.next();
            if (!pw.isAirborne() && pw.hasType(EnumType.Grass)) {
               pw.getBattleStats().modifyStat(1, (StatsType[])raiseStats);
               succeeded = true;
            }
         }

         if (succeeded) {
            return AttackResult.succeeded;
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = pw.bc.getActiveUnfaintedPokemon().iterator();

      while(var7.hasNext()) {
         PixelmonWrapper pokemon = (PixelmonWrapper)var7.next();
         if (!pw.isAirborne()) {
            int weight = 25;
            if (!pw.isSameTeam(pokemon)) {
               weight = -weight;
            }

            userChoice.raiseWeight((float)weight);
         }
      }

   }

   static {
      raiseStats = new StatsType[]{StatsType.Attack, StatsType.SpecialAttack};
   }
}

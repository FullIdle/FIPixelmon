package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class Haze extends SpecialAttackBase {
   public boolean onlyLowered = false;

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.attack.isAttack("Haze")) {
         if (!this.onlyLowered) {
            target.getBattleStats().clearBattleStatsNoCrit();
            user.bc.sendToAll("pixelmon.effect.clearsmog", target.getNickname());
         } else {
            target.getBattleStats().resetLoweredBattleStats();
            user.bc.sendToAll("pixelmon.effect.resetstats", target.getNickname());
         }

         return AttackResult.proceed;
      } else {
         if (user.targetIndex == 0 || user.bc.simulateMode) {
            Iterator var3 = user.bc.getActiveUnfaintedPokemon().iterator();

            while(var3.hasNext()) {
               PixelmonWrapper current = (PixelmonWrapper)var3.next();
               if (this.onlyLowered) {
                  current.getBattleStats().resetLoweredBattleStats();
               } else {
                  current.getBattleStats().clearBattleStatsNoCrit();
               }
            }

            user.bc.sendToAll("pixelmon.effect.modifierscleared");
         }

         return AttackResult.succeeded;
      }
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return true;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      float weight = 0.0F;
      ArrayList allies = pw.getTeamPokemon();

      float weightDifference;
      for(Iterator var9 = userChoice.targets.iterator(); var9.hasNext(); weight += weightDifference) {
         PixelmonWrapper target = (PixelmonWrapper)var9.next();
         weightDifference = (float)(target.getBattleStats().getSumStages() * 20);
         if (allies.contains(target)) {
            weightDifference = -weightDifference;
         }
      }

      userChoice.raiseWeight(weight);
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;

public class VenomDrench extends SpecialAttackBase {
   private static final transient StatsType[] lowerStats;

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0) {
         boolean hit = false;
         Iterator var4 = user.targets.iterator();

         while(var4.hasNext()) {
            PixelmonWrapper pw = (PixelmonWrapper)var4.next();
            if (pw.hasStatus(StatusType.Poison, StatusType.PoisonBadly)) {
               hit = true;
               pw.getBattleStats().modifyStat(-1, lowerStats, user);
            }
         }

         if (hit) {
            return AttackResult.succeeded;
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }
      } else {
         return AttackResult.succeeded;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      StatsEffect[] statsEffects = new StatsEffect[lowerStats.length];

      for(int i = 0; i < lowerStats.length; ++i) {
         statsEffects[i] = new StatsEffect(lowerStats[i], -1, false);
      }

      Iterator var18 = MoveChoice.splitChoices(pw.getOpponentPokemon(), bestOpponentChoices).iterator();

      while(true) {
         while(var18.hasNext()) {
            ArrayList choices = (ArrayList)var18.next();
            Iterator var10 = choices.iterator();

            while(var10.hasNext()) {
               MoveChoice choice = (MoveChoice)var10.next();
               if (choice.isAttack() && choice.attack.getMove().getMakesContact()) {
                  ArrayList saveTargets = userChoice.targets;
                  ArrayList newTargets = new ArrayList();
                  newTargets.add(choice.user);
                  userChoice.targets = newTargets;
                  StatsEffect[] var14 = statsEffects;
                  int var15 = statsEffects.length;

                  for(int var16 = 0; var16 < var15; ++var16) {
                     StatsEffect statsEffect = var14[var16];
                     statsEffect.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
                  }

                  userChoice.targets = saveTargets;
                  break;
               }
            }
         }

         return;
      }
   }

   static {
      lowerStats = new StatsType[]{StatsType.Attack, StatsType.SpecialAttack, StatsType.Speed};
   }
}

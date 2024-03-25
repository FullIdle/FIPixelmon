package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Cursed;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;

public class Curse extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasType(EnumType.Ghost)) {
         if (user == target) {
            target = (PixelmonWrapper)RandomHelper.getRandomElementFromList(user.getOpponentPokemon());
            if (target == null) {
               return AttackResult.notarget;
            }
         }

         if (target.hasStatus(StatusType.Cursed)) {
            user.bc.sendToAll("attack.curse.alreadycursed", target.getNickname());
            return AttackResult.failed;
         } else if (target.addStatus(new Cursed(), user)) {
            user.bc.sendToAll("attack.curse.curse", user.getNickname(), target.getNickname());
            user.doBattleDamage(user, (float)user.getPercentMaxHealth(50.0F), DamageTypeEnum.STATUS);
            return AttackResult.proceed;
         } else {
            return AttackResult.failed;
         }
      } else {
         user.getBattleStats().modifyStat(new int[]{1, 1, -1}, new StatsType[]{StatsType.Attack, StatsType.Defence, StatsType.Speed});
         return AttackResult.proceed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (pw.hasType(EnumType.Ghost)) {
         if (!userChoice.hitsAlly() && !(MoveChoice.getMaxDamagePercent(pw, bestOpponentChoices) > pw.getHealthPercent() - 50.0F)) {
            userChoice.raiseWeight(40.0F);
         }
      } else {
         StatsEffect attackBoost = new StatsEffect(StatsType.Attack, 1, true);
         StatsEffect defenseBoost = new StatsEffect(StatsType.Defence, 1, true);
         attackBoost.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
         defenseBoost.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      }

   }
}

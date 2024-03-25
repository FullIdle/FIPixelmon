package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class Memento extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      user.doBattleDamage(user, (float)((int)Math.floor((double)user.getHealth())), DamageTypeEnum.SELF);
      target.getBattleStats().modifyStat(-2, new StatsType[]{StatsType.Attack, StatsType.SpecialAttack}, user);
      return AttackResult.succeeded;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (pw.getParticipant().countAblePokemon() > 1 && MoveChoice.canOutspeedAnd2HKO(bestOpponentChoices, pw, bestUserChoices)) {
         StatsEffect attackDrop = new StatsEffect(StatsType.Attack, -2, false);
         StatsEffect spAttackDrop = new StatsEffect(StatsType.SpecialAttack, -2, false);
         attackDrop.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
         spAttackDrop.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      }

   }
}

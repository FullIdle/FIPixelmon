package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import java.util.ArrayList;
import java.util.Set;

public class SkillSwap extends SpecialAttackBase {
   private static final Set invalidAbilities = Sets.newHashSet(new String[]{"AsOne", "BattleBond", "Comatose", "Disguise", "GulpMissile", "HungerSwitch", "IceFace", "Illusion", "Multitype", "NeutralizingGas", "PowerConstruct", "RKSSystem", "Schooling", "ShieldsDown", "StanceChange", "WonderGuard", "ZenMode"});

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.isDynamax()) {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         AbilityBase userAbility = user.getBattleAbility(false);
         AbilityBase targetAbility = target.getBattleAbility(false);
         if (!invalidAbilities.contains(userAbility.getName()) && !invalidAbilities.contains(targetAbility.getName())) {
            user.bc.sendToAll("pixelmon.effect.skillswap", user.getNickname(), targetAbility.getTranslatedName());
            user.setTempAbility(targetAbility);
            user.bc.sendToAll("pixelmon.effect.skillswap", target.getNickname(), userAbility.getTranslatedName());
            target.setTempAbility(userAbility);
            return AttackResult.succeeded;
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Entrainment entrainment = new Entrainment();
      entrainment.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }
}

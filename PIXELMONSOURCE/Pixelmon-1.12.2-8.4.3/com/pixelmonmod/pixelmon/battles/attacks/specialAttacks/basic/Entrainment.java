package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Entrainment extends SpecialAttackBase {
   private static final Set invalidTargetAbilities = Sets.newHashSet(new String[]{"Truant", "Multitype", "StanceChange", "Schooling", "Comatose", "ShieldsDown", "Disguise", "RKSSystem", "BattleBond", "IceFace", "GulpMissile", "AsOne", "PowerConstruct", "ZenMode"});
   private static final Set invalidUserAbilities = Sets.newHashSet(new String[]{"Trace", "Forecast", "FlowerGift", "ZenMode", "Illusion", "Imposter", "PowerOfAlchemy", "Receiver", "Disguise", "PowerConstruct", "IceFace", "HungerSwitch", "GulpMissile", "NeutralizingGas", "AsOne", "BattleBond", "Comatose", "Multitype", "RKSSystem", "Schooling", "ShieldsDown", "StanceChange"});

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      AbilityBase targetAbility = target.getBattleAbility(false);
      AbilityBase userAbility = user.getBattleAbility(false);
      if (target.isDynamax()) {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else if (!targetAbility.getName().equalsIgnoreCase(userAbility.getName()) && !invalidTargetAbilities.contains(targetAbility.getName()) && !invalidUserAbilities.contains(userAbility.getName())) {
         user.bc.sendToAll("pixelmon.effect.entrainment", target.getNickname(), target.getBattleAbility(false).getTranslatedName(), user.getBattleAbility(false).getTranslatedName());
         target.setTempAbility(user.getBattleAbility(false));
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      AbilityBase userAbility = pw.getBattleAbility(false);
      boolean userNegativeAbility = userAbility.isNegativeAbility();
      boolean allied = userChoice.hitsAlly();
      Iterator var10 = userChoice.targets.iterator();

      while(true) {
         while(true) {
            PixelmonWrapper target;
            do {
               if (!var10.hasNext()) {
                  return;
               }

               target = (PixelmonWrapper)var10.next();
            } while(target.isDynamax != 0);

            AbilityBase targetAbility = target.getBattleAbility(false);
            boolean targetNegativeAbility = targetAbility.isNegativeAbility();
            if (allied && targetNegativeAbility && !userNegativeAbility) {
               userChoice.raiseWeight(40.0F);
            } else if (!allied && !targetNegativeAbility) {
               if (userNegativeAbility) {
                  userChoice.raiseWeight(40.0F);
               } else {
                  userChoice.raiseWeight(25.0F);
               }
            }
         }
      }
   }
}

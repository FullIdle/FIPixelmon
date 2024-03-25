package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import java.util.Set;

public class RolePlay extends SpecialAttackBase {
   private static final Set invalidTargetAbilities = Sets.newHashSet(new String[]{"Trace", "WonderGuard", "Forecast", "FlowerGift", "Multitype", "Illusion", "ZenMode", "Imposter", "StanceChange", "PowerOfAlchemy", "Receiver", "Schooling", "Comatose", "ShieldsDown", "Disguise", "RKSSystem", "PowerConstruct", "BattleBond", "IceFace", "GulpMissile", "NeutralizingGas", "AsOne", "HungerSwitch"});
   private static final Set invalidUserAbilities = Sets.newHashSet(new String[]{"Multitype", "ZenMode", "StanceChange", "Schooling", "Comatose", "ShieldsDown", "Disguise", "RKSSystem", "PowerConstruct", "BattleBond", "IceFace", "GulpMissile", "AsOne"});

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      AbilityBase targetAbility = target.getBattleAbility(false);
      AbilityBase userAbility = user.getBattleAbility(false);
      if (!targetAbility.getName().equalsIgnoreCase(userAbility.getName()) && !invalidUserAbilities.contains(userAbility.getName()) && !invalidTargetAbilities.contains(targetAbility.getName())) {
         user.bc.sendToAll("pixelmon.effect.entrainment", user.getNickname(), userAbility.getTranslatedName(), targetAbility.getTranslatedName());
         user.setTempAbility(targetAbility);
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import java.util.Set;

public class WorrySeed extends ChangeAbility {
   private static final Set invalidTargetAbilities = Sets.newHashSet(new String[]{"AsOne", "BattleBond", "Comatose", "Disguise", "GulpMissile", "IceFace", "Multitype", "PowerConstruct", "RKSSystem", "Schooling", "ShieldsDown", "StanceChange", "Truant", "ZenMode"});

   public WorrySeed() {
      super("Insomnia");
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      AbilityBase targetAbility = target.getBattleAbility(false);
      if (invalidTargetAbilities.contains(targetAbility.getName())) {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         target.bc.sendToAll("pixelmon.status.worryseed", target.getNickname());
         target.setTempAbility((AbilityBase)AbilityBase.getAbility(this.ability).get());
         targetAbility.onAbilityLost(target);
         return AttackResult.succeeded;
      }
   }
}

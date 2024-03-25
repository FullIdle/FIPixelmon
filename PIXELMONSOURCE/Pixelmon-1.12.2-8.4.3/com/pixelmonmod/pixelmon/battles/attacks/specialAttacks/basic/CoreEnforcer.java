package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GastroAcid;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;

public class CoreEnforcer extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      int targetIndex = user.bc.turnList.indexOf(target);
      int userIndex = user.bc.turnList.indexOf(user);
      AbilityBase targetAbility = target.getBattleAbility(false);
      if (!target.isSwitching && userIndex >= targetIndex && !target.hasStatus(StatusType.GastroAcid) && !GastroAcid.noAbilityChange.contains(targetAbility.getName()) && target.addStatus(new GastroAcid(), target)) {
         target.bc.sendToAll("pixelmon.status.gastroacid", target.getNickname());
         targetAbility.onAbilityLost(target);
      }

      return AttackResult.proceed;
   }
}

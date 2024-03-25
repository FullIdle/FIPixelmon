package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Merciless extends AbilityBase {
   public static boolean willApply(PixelmonWrapper user, PixelmonWrapper target, Attack attack) {
      return user.getBattleAbility() instanceof Merciless && (target.hasStatus(StatusType.PoisonBadly) || target.hasStatus(StatusType.Poison)) && !(target.getBattleAbility() instanceof BattleArmour) && !(target.getBattleAbility() instanceof ShellArmour) && !target.hasStatus(StatusType.LuckyChant);
   }

   public float adjustCriticalHitChance(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a, float critChance) {
      return willApply(pokemon, target, a) ? 1.0F : critChance;
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class ScaleShot extends SpecialAttackBase {
   public void dealtDamage(PixelmonWrapper attacker, PixelmonWrapper defender, Attack attack, DamageTypeEnum damageType) {
      attacker.getBattleStats().increaseStat(1, StatsType.Speed, attacker, true);
      attacker.getBattleStats().decreaseStat(1, StatsType.Defence, attacker, true);
   }
}

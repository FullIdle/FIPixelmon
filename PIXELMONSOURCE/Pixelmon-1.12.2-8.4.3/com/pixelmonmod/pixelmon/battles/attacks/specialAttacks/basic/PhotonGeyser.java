package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class PhotonGeyser extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      user.attack.overrideAttackCategory(user.getBattleStats().getStatWithMod(StatsType.SpecialAttack) >= user.getBattleStats().getStatWithMod(StatsType.Attack) ? AttackCategory.SPECIAL : AttackCategory.PHYSICAL);
      return AttackResult.proceed;
   }
}

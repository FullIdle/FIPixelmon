package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Gooey extends AbilityBase {
   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      this.sendActivatedMessage(user);
      user.getBattleStats().modifyStat(-1, StatsType.Speed, target, false);
   }
}

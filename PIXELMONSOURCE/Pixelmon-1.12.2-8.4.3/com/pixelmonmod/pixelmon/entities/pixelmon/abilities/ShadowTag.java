package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class ShadowTag extends AbilityBase {
   public boolean stopsSwitching(PixelmonWrapper user, PixelmonWrapper opponent) {
      return !(opponent.getBattleAbility() instanceof ShadowTag);
   }
}

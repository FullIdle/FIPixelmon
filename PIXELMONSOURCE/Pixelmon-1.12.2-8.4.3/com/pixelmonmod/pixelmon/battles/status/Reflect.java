package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Reflect extends Screen {
   public Reflect() {
      this(5);
   }

   public Reflect(int turns) {
      super(StatusType.Reflect, StatsType.Defence, turns, "pixelmon.effect.reflectraised", "pixelmon.effect.alreadybarrier", "pixelmon.status.reflectoff");
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.hasStatus(StatusType.AuroraVeil)) {
         super.applyEffect(user, target);
      }

   }

   protected Screen getNewInstance(int effectTurns) {
      return new Reflect(effectTurns);
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class IgnoreDefense extends EffectBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return false;
   }
}

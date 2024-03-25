package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;

public class QuickDraw extends AbilityBase {
   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      if (RandomHelper.getRandomChance(30)) {
         pokemon.bc.sendToAll("pixelmon.abilities.quickdraw", pokemon.getNickname());
         priority += 0.2F;
         triggered.setTrue();
      }

      return priority;
   }
}

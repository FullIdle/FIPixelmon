package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;

public class Stall extends AbilityBase {
   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      triggered.setTrue();
      return priority - 0.1F;
   }
}

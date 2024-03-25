package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;

public class GaleWings extends AbilityBase {
   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      if (pokemon.attack.getType() == EnumType.Flying && pokemon.hasFullHealth()) {
         ++priority;
         triggered.setTrue();
      }

      return priority;
   }
}

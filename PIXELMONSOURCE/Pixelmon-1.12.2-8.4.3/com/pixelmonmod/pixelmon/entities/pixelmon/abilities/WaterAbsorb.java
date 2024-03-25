package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class WaterAbsorb extends Absorb {
   public WaterAbsorb() {
      super(EnumType.Water, "pixelmon.abilities.waterabsorb");
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      return a.isAttack("Dive") && !user.hasStatus(StatusType.Submerged) ? true : super.allowsIncomingAttack(pokemon, user, a);
   }
}

package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureLuxuryBall extends CaptureBase {
   public CaptureLuxuryBall() {
      super(EnumPokeballs.LuxuryBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      return type.getBallBonus();
   }
}

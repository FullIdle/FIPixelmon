package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureNestBall extends CaptureBase {
   public CaptureNestBall() {
      super(EnumPokeballs.NestBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      float lvl = (float)p2.getLevel();
      double bonus = 8.0 - 0.2 * (double)(lvl - 1.0F);
      if (bonus < 1.0) {
         bonus = 1.0;
      }

      return bonus;
   }
}

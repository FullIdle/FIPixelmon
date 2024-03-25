package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureHeavyBall extends CaptureBase {
   public CaptureHeavyBall() {
      super(EnumPokeballs.HeavyBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      return type.getBallBonus();
   }

   public int modifyCaptureRate(Pokemon pixelmon, int captureRate) {
      float weight = pixelmon.getBaseStats().getWeight();
      if ((double)weight < 199.9) {
         captureRate -= 20;
      } else if ((double)weight < 299.9) {
         captureRate += 20;
      } else if ((double)weight < 409.5) {
         captureRate += 30;
      } else {
         captureRate += 40;
      }

      return captureRate;
   }
}

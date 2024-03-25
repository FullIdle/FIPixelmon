package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureHealBall extends CaptureBase {
   public CaptureHealBall() {
      super(EnumPokeballs.HealBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      return type.getBallBonus();
   }

   public void doAfterEffect(EnumPokeballs type, EntityPixelmon p) {
      p.func_70606_j(p.func_110138_aP());
   }
}

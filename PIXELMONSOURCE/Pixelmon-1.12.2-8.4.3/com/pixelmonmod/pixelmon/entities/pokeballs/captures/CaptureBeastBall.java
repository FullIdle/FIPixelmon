package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureBeastBall extends CaptureBase {
   public CaptureBeastBall() {
      super(EnumPokeballs.BeastBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      if (p2.getSpecies().isUltraBeast()) {
         return 5.0;
      } else {
         return p2.getSpecies().getPossibleForms(true).contains(EnumSpecial.Alien) ? 1.0 : 0.1;
      }
   }
}

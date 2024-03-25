package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureSportBall extends CaptureBase {
   public CaptureSportBall() {
      super(EnumPokeballs.SportBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      return p2.getBaseStats().types.contains(EnumType.Bug) ? 1.5 : 1.0;
   }
}

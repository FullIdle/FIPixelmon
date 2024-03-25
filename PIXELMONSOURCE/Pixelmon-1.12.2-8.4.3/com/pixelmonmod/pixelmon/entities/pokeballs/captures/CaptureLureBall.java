package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureLureBall extends CaptureBase {
   public CaptureLureBall() {
      super(EnumPokeballs.LureBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      EntityPixelmon ep = p2.getPixelmonIfExists();
      return ep != null && ep.battleController != null && ep.battleController.wasFishing ? 5.0 : 1.0;
   }
}

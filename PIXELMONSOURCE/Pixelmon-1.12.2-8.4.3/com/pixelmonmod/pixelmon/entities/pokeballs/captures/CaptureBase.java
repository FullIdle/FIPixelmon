package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public abstract class CaptureBase {
   public EnumPokeballs pokeball;

   public CaptureBase(EnumPokeballs pokeball) {
      this.pokeball = pokeball;
   }

   public abstract double getBallBonus(EnumPokeballs var1, EntityPlayer var2, Pokemon var3, EnumPokeBallMode var4);

   public void doAfterEffect(EnumPokeballs type, EntityPixelmon p) {
   }

   public int modifyCaptureRate(Pokemon pixelmon, int captureRate) {
      return captureRate;
   }
}

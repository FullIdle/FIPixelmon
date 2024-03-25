package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class CaptureSafariBall extends CaptureBase {
   public CaptureSafariBall() {
      super(EnumPokeballs.SafariBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      EntityPixelmon ep = p2.getPixelmonIfExists();
      if (ep == null) {
         return 1.0;
      } else {
         Biome biome = ep.field_70170_p.func_180494_b(new BlockPos(MathHelper.func_76128_c(ep.field_70165_t), 0, MathHelper.func_76128_c(ep.field_70161_v)));
         return !biome.getRegistryName().func_110623_a().equals("plains") && !biome.getRegistryName().func_110623_a().equalsIgnoreCase("savanna") ? 1.0 : 1.5;
      }
   }
}

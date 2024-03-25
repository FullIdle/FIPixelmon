package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class CaptureDuskBall extends CaptureBase {
   public CaptureDuskBall() {
      super(EnumPokeballs.DuskBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      EntityPixelmon ep = p2.getPixelmonIfExists();
      if (ep == null) {
         return 1.0;
      } else if (ep.getPokemonData().getSpecies().isUltraBeast()) {
         return 0.1;
      } else {
         return (!(ep.field_70163_u <= 64.0) || ep.field_70170_p.func_175727_C(new BlockPos(MathHelper.func_76128_c(ep.field_70165_t), MathHelper.func_76128_c(ep.field_70163_u), MathHelper.func_76128_c(ep.field_70161_v))) || !(ep.field_70170_p.func_175724_o(new BlockPos(MathHelper.func_76128_c(ep.field_70165_t), MathHelper.func_76128_c(ep.field_70163_u), MathHelper.func_76128_c(ep.field_70161_v))) <= 14.0F)) && ep.field_70170_p.func_72935_r() ? 1.0 : 3.0;
      }
   }
}

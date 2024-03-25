package com.pixelmonmod.pixelmon.entities.pixelmon.particleEffects;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class DiglettParticles extends ParticleEffects {
   public void onUpdate(Entity2Client pokemon) {
      if (pokemon.field_70142_S - pokemon.field_70165_t != 0.0 || pokemon.field_70136_U - pokemon.field_70161_v != 0.0) {
         int i = MathHelper.func_76128_c(pokemon.field_70165_t);
         int j = MathHelper.func_76128_c(pokemon.field_70163_u - 0.20000000298023224 - pokemon.func_70033_W());
         int k = MathHelper.func_76128_c(pokemon.field_70161_v);
         IBlockState state = pokemon.field_70170_p.func_180495_p(new BlockPos(i, j, k));
         if (state.func_185904_a() != Material.field_151579_a) {
            pokemon.field_70170_p.func_175688_a(EnumParticleTypes.BLOCK_CRACK, pokemon.field_70165_t + ((double)this.rand.nextFloat() - 0.5) * (double)pokemon.field_70130_N, pokemon.func_174813_aQ().field_72338_b + 0.1, pokemon.field_70161_v + ((double)this.rand.nextFloat() - 0.5) * (double)pokemon.field_70130_N, -pokemon.field_70159_w * 4.0, 1.5, -pokemon.field_70179_y * 4.0, new int[]{Block.func_176210_f(state)});
         }
      }

   }
}

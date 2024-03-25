package com.pixelmonmod.pixelmon.entities.pixelmon.movement;

import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class Thermals {
   public static float getThermalPower(World world, BlockPos pos) {
      Biome biome = world.func_180494_b(pos);
      long seed = (long)(10000 + (int)((float)world.func_82737_E() / 24000.0F));
      Chunk chunk = world.func_175726_f(pos);
      Random rand = new Random((long)(chunk.field_76635_g * chunk.field_76647_h) + seed);
      if (rand.nextFloat() > 0.2F) {
         return 0.0F;
      } else {
         float thermalPower = (1.0F + biome.func_180626_a(pos)) / 3.0F;
         return thermalPower;
      }
   }
}

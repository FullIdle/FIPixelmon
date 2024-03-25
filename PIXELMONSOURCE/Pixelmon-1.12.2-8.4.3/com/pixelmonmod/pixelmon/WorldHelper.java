package com.pixelmonmod.pixelmon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class WorldHelper {
   public static final EnumFacing[] NWSE;

   public static Entity getEntityByUUID(World world, UUID uuid, Class clazz) {
      Iterator var3 = world.field_72996_f.iterator();

      Entity entity;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         entity = (Entity)var3.next();
      } while(!Objects.equals(entity.func_110124_au(), uuid) || !clazz.isInstance(entity));

      return entity;
   }

   public static boolean isHorizontal(EnumFacing dir) {
      return dir.func_82601_c() != 0;
   }

   public static Biome demandBiome(String name) {
      Biome result = parseBiome(name);
      if (result == null) {
         throw new IllegalArgumentException("No such Biome named \"" + name + "\"");
      } else {
         return result;
      }
   }

   public static Biome parseBiome(String name) {
      Iterator var1 = Biome.field_185377_q.iterator();

      Biome biome;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         biome = (Biome)var1.next();
      } while(biome == null || biome.getRegistryName() == null || !biome.getRegistryName().func_110623_a().equalsIgnoreCase(name));

      return biome;
   }

   public static ArrayList getDirectionsTowards(int distX, int distZ) {
      EnumFacing leftRight = distX == 0 ? null : (distX < 0 ? EnumFacing.WEST : EnumFacing.EAST);
      EnumFacing upDown = distZ == 0 ? null : (distZ < 0 ? EnumFacing.NORTH : EnumFacing.SOUTH);
      ArrayList result = new ArrayList(2);
      if (leftRight != null) {
         result.add(leftRight);
      }

      if (upDown != null) {
         result.add(upDown);
      }

      return result;
   }

   public static int firstBlockDownwardsFromY(World world, BlockPos pos, boolean countNonSolid) {
      for(Block block = null; !(block = world.func_180495_p(pos).func_177230_c()).equals(Blocks.field_150350_a) || !countNonSolid && !block.func_176205_b(world, pos); pos = pos.func_177977_b()) {
      }

      return pos.func_177956_o();
   }

   public static int getWaterDepth(BlockPos pos, World worldObj) {
      int count;
      for(count = 0; worldObj.func_180495_p(pos).func_177230_c() == Blocks.field_150355_j || worldObj.func_180495_p(pos).func_177230_c() == Blocks.field_150353_l; ++count) {
         pos = pos.func_177984_a();
      }

      return count;
   }

   public static boolean isWaterOrIce(World world, BlockPos pos) {
      Block block = world.func_180495_p(pos).func_177230_c();
      return block == Blocks.field_150355_j || block == Blocks.field_150432_aD;
   }

   public static void fixLighting(World world, StructureBoundingBox bb) {
      for(int i = bb.field_78897_a >> 4; i <= bb.field_78893_d >> 4; ++i) {
         for(int j = bb.field_78896_c >> 4; j <= bb.field_78892_f >> 4; ++j) {
            Chunk chunk = world.func_72964_e(i, j);
            chunk.func_76603_b();
            chunk.func_76613_n();
         }
      }

   }

   static {
      NWSE = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.EAST};
   }
}

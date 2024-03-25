package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class UltraSpaceTeleporter extends Teleporter {
   private final WorldServer worldServerInstance;
   private final Random random;

   public UltraSpaceTeleporter(WorldServer worldIn) {
      super(worldIn);
      this.worldServerInstance = worldIn;
      this.random = new Random(worldIn.func_72905_C());
   }

   public void func_180266_a(Entity entityIn, float rotationYaw) {
   }

   public boolean func_180620_b(Entity entityIn, float rotationYaw) {
      return false;
   }

   public boolean func_85188_a(Entity entityIn) {
      return false;
   }

   public static class PortalPosition extends BlockPos {
      public long lastUpdateTime;

      public PortalPosition(BlockPos pos, long lastUpdate) {
         super(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         this.lastUpdateTime = lastUpdate;
      }
   }
}

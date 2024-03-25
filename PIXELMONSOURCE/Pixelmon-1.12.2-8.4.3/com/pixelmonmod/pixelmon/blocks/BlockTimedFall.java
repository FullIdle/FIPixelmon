package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTimedFall extends GenericBlock {
   private static int ticksToFall = 300;
   public HashMap fallCounters = new HashMap();

   public BlockTimedFall() {
      super(Material.field_151575_d);
      this.func_149711_c(1.0F);
      this.func_149647_a(PixelmonCreativeTabs.utilityBlocks);
   }

   public void func_185477_a(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List list, Entity collidingEntity, boolean b) {
      if (!(collidingEntity instanceof EntityPlayer)) {
         super.func_185477_a(state, worldIn, pos, mask, list, collidingEntity, b);
      } else {
         EntityPlayer player = (EntityPlayer)collidingEntity;
         if (!this.fallCounters.containsKey(player.func_110124_au())) {
            this.fallCounters.put(player.func_110124_au(), ticksToFall);
         }

         int currentTickForEntity = (Integer)this.fallCounters.get(player.func_110124_au());
         this.fallCounters.put(player.func_110124_au(), currentTickForEntity - 1);
         if (currentTickForEntity >= 0) {
            super.func_185477_a(state, worldIn, pos, mask, list, collidingEntity, b);
         } else {
            if (currentTickForEntity <= -100) {
               this.fallCounters.remove(player.func_110124_au());
            }

         }
      }
   }
}

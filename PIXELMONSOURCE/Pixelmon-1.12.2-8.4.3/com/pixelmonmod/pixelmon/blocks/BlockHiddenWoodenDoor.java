package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockHiddenWoodenDoor extends BlockDoor {
   public BlockHiddenWoodenDoor() {
      super(Material.field_151575_d);
      this.func_149711_c(1.0F);
      this.func_149722_s();
      this.func_149663_c("hidden_wooden_door");
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(PixelmonItems.hiddenWoodenDoorItem);
   }
}

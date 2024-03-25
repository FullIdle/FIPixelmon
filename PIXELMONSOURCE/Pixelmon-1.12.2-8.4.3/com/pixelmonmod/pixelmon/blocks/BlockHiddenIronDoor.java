package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockHiddenIronDoor extends BlockDoor {
   public BlockHiddenIronDoor() {
      super(Material.field_151573_f);
      this.func_149711_c(2.0F);
      this.func_149722_s();
      this.func_149663_c("hidden_iron_door");
   }

   @Nullable
   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return PixelmonItems.hiddenIronDoorItem;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(PixelmonItems.hiddenIronDoorItem);
   }
}

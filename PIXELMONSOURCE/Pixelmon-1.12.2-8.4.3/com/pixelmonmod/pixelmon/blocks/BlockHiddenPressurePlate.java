package com.pixelmonmod.pixelmon.blocks;

import java.util.Random;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockPressurePlate.Sensitivity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockHiddenPressurePlate extends BlockPressurePlate {
   public BlockHiddenPressurePlate() {
      super(Material.field_151575_d, Sensitivity.MOBS);
      this.func_149711_c(1.0F);
      this.func_149722_s();
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150898_a(this);
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(Item.func_150898_a(this));
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public int func_149745_a(Random random) {
      return 1;
   }
}

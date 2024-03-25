package com.pixelmonmod.pixelmon.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class GenericModelBlock extends BlockContainer {
   protected GenericModelBlock(Material materialIn) {
      super(materialIn);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return null;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }
}

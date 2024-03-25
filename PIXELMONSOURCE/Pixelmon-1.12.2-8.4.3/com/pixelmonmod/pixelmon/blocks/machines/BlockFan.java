package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileFan;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFan extends BlockMachine {
   public BlockFan() {
      super("fan");
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileFan();
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}

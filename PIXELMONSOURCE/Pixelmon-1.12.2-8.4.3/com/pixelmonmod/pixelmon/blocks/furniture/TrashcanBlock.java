package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.GenericModelBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTrashcan;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TrashcanBlock extends GenericModelBlock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.30000001192092896, 0.0, 0.30000001192092896, 0.699999988079071, 0.5, 0.699999988079071);

   public TrashcanBlock() {
      super(Material.field_151573_f);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149663_c("Trashcan");
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityTrashcan();
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}

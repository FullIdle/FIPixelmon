package com.pixelmonmod.pixelmon.blocks.decorative;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMug extends Block {
   private static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.3, 0.0, 0.3, 0.7, 0.4, 0.7);

   public BlockMug(String name) {
      super(Material.field_151576_e);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149711_c(1.0F);
      this.func_149663_c(name);
      this.func_149647_a(PixelmonCreativeTabs.decoration);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockProperties.FACING, EnumFacing.SOUTH));
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING});
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, placer.func_174811_aO().func_176746_e());
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return STANDING_AABB;
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta));
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}

package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GenericDoubleRotatableModelBlock extends GenericRotatableModelBlock {
   public static final PropertyEnum BLOCKPOS = PropertyEnum.func_177709_a("blockpos", EnumBlockPos.class);

   protected GenericDoubleRotatableModelBlock(Material materialIn) {
      super(materialIn);
      this.func_180632_j(this.field_176227_L.func_177621_b());
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING, BLOCKPOS});
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta & 3)).func_177226_a(BLOCKPOS, meta > 3 ? EnumBlockPos.TOP : EnumBlockPos.BOTTOM);
   }

   public int func_176201_c(IBlockState state) {
      int meta = 0;
      meta |= ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b();
      return meta + (state.func_177229_b(BLOCKPOS) == EnumBlockPos.TOP ? 4 : 0);
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState iblockstate = this.func_176223_P();
      if (facing.func_176740_k().func_176722_c()) {
         iblockstate = iblockstate.func_177226_a(BlockProperties.FACING, facing);
      }

      return iblockstate.func_177226_a(BLOCKPOS, EnumBlockPos.BOTTOM);
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return worldIn.func_180495_p(pos.func_177984_a()).func_177230_c().func_176200_f(worldIn, pos.func_177984_a()) && super.func_176196_c(worldIn, pos);
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      super.func_180633_a(worldIn, pos, state, placer, stack);
      worldIn.func_180501_a(pos.func_177984_a(), state.func_177226_a(BlockProperties.FACING, placer.func_174811_aO().func_176734_d()).func_177226_a(BLOCKPOS, EnumBlockPos.TOP), 2);
   }

   public void func_176206_d(World worldIn, BlockPos pos, IBlockState state) {
      super.func_176206_d(worldIn, pos, state);
      if (state.func_177229_b(BLOCKPOS) == EnumBlockPos.TOP) {
         worldIn.func_175698_g(pos.func_177977_b());
      } else {
         worldIn.func_175698_g(pos.func_177984_a());
      }

   }
}

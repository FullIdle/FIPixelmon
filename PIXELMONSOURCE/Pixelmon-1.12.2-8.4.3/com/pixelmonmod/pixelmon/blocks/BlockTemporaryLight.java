package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTemporaryLight;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTemporaryLight extends Block implements ITileEntityProvider {
   public BlockTemporaryLight() {
      super(Material.field_151579_a, MapColor.field_151660_b);
      this.func_149715_a(1.0F);
      this.func_149713_g(0);
   }

   public void func_185477_a(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List list, Entity collidingEntity, boolean b) {
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public int func_149745_a(Random random) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public float func_185485_f(IBlockState state) {
      return 1.0F;
   }

   @Nullable
   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityTemporaryLight();
   }
}

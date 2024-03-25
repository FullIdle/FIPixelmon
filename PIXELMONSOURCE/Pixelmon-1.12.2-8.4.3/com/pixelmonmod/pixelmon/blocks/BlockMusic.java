package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityMusicBlock;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMusic extends Block implements ITileEntityProvider {
   private static final AxisAlignedBB EMPTY = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
   public static final PropertyDirection FACING;

   public BlockMusic() {
      super(Material.field_189963_J);
      this.field_149785_s = true;
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(FACING, EnumFacing.NORTH));
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(PixelmonBlocks.getItemFromBlock(this));
   }

   @Nullable
   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return field_185506_k;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return source instanceof World && ((World)source).field_72995_K && this.isClientCreative() ? field_185505_j : EMPTY;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return this.isClientCreative() ? state.func_185900_c(worldIn, pos).func_186670_a(pos) : EMPTY;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return true;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      EnumFacing enumfacing = placer.func_174811_aO().func_176734_d();
      return this.func_176223_P().func_177226_a(FACING, enumfacing);
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{FACING});
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumFacing)state.func_177229_b(FACING)).func_176736_b();
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(FACING, EnumFacing.func_176731_b(meta));
   }

   public EnumPushReaction func_149656_h(IBlockState state) {
      return EnumPushReaction.DESTROY;
   }

   public boolean func_176200_f(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
   }

   public int func_149745_a(Random random) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public float func_185485_f(IBlockState state) {
      return 1.0F;
   }

   public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
      return true;
   }

   @Nullable
   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityMusicBlock();
   }

   @SideOnly(Side.CLIENT)
   private boolean isClientCreative() {
      return Minecraft.func_71410_x().field_71439_g.func_184812_l_();
   }

   static {
      FACING = BlockHorizontal.field_185512_D;
   }
}

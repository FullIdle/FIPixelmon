package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPicketFenceNormal;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPicketFence extends GenericModelBlock {
   public static final PropertyBool NORTH = PropertyBool.func_177716_a("north");
   public static final PropertyBool EAST = PropertyBool.func_177716_a("east");
   public static final PropertyBool SOUTH = PropertyBool.func_177716_a("south");
   public static final PropertyBool WEST = PropertyBool.func_177716_a("west");
   protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[]{new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.375, 0.625, 1.0, 0.625), new AxisAlignedBB(0.0, 0.0, 0.375, 0.625, 1.0, 1.0), new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.0, 0.625), new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 0.625, 1.0, 0.625), new AxisAlignedBB(0.0, 0.0, 0.0, 0.625, 1.0, 1.0), new AxisAlignedBB(0.375, 0.0, 0.375, 1.0, 1.0, 0.625), new AxisAlignedBB(0.375, 0.0, 0.375, 1.0, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.0, 0.625), new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.0, 1.0), new AxisAlignedBB(0.375, 0.0, 0.0, 1.0, 1.0, 0.625), new AxisAlignedBB(0.375, 0.0, 0.0, 1.0, 1.0, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.625), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)};
   public static final AxisAlignedBB PILLAR_AABB = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.5, 0.625);
   public static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.375, 0.0, 0.625, 0.625, 1.5, 1.0);
   public static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.375, 0.375, 1.5, 0.625);
   public static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.5, 0.375);
   public static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.625, 0.0, 0.375, 1.0, 1.5, 0.625);

   public BlockPicketFence() {
      super(Material.field_151575_d);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149663_c("picket_fence_normal");
   }

   public void func_185477_a(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
      state = state.func_185899_b(worldIn, pos);
      func_185492_a(pos, entityBox, collidingBoxes, PILLAR_AABB);
      if ((Boolean)state.func_177229_b(NORTH)) {
         func_185492_a(pos, entityBox, collidingBoxes, NORTH_AABB);
      }

      if ((Boolean)state.func_177229_b(EAST)) {
         func_185492_a(pos, entityBox, collidingBoxes, EAST_AABB);
      }

      if ((Boolean)state.func_177229_b(SOUTH)) {
         func_185492_a(pos, entityBox, collidingBoxes, SOUTH_AABB);
      }

      if ((Boolean)state.func_177229_b(WEST)) {
         func_185492_a(pos, entityBox, collidingBoxes, WEST_AABB);
      }

   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      state = this.func_176221_a(state, source, pos);
      return BOUNDING_BOXES[getBoundingBoxIdx(state)];
   }

   private static int getBoundingBoxIdx(IBlockState state) {
      int i = 0;
      if ((Boolean)state.func_177229_b(NORTH)) {
         i |= 1 << EnumFacing.NORTH.func_176736_b();
      }

      if ((Boolean)state.func_177229_b(EAST)) {
         i |= 1 << EnumFacing.EAST.func_176736_b();
      }

      if ((Boolean)state.func_177229_b(SOUTH)) {
         i |= 1 << EnumFacing.SOUTH.func_176736_b();
      }

      if ((Boolean)state.func_177229_b(WEST)) {
         i |= 1 << EnumFacing.WEST.func_176736_b();
      }

      return i;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_176205_b(IBlockAccess worldIn, BlockPos pos) {
      return false;
   }

   public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
      IBlockState iblockstate = worldIn.func_180495_p(pos);
      BlockFaceShape blockfaceshape = iblockstate.func_193401_d(worldIn, pos, facing);
      Block block = iblockstate.func_177230_c();
      return !isExcepBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID || block instanceof BlockPicketFence;
   }

   protected static boolean isExcepBlockForAttachWithPiston(Block p_194142_0_) {
      return Block.func_193382_c(p_194142_0_) || p_194142_0_ == Blocks.field_180401_cv || p_194142_0_ == Blocks.field_150440_ba || p_194142_0_ == Blocks.field_150423_aK || p_194142_0_ == Blocks.field_150428_aP;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_176225_a(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return true;
   }

   public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      return worldIn.field_72995_K || ItemLead.func_180618_a(playerIn, worldIn, pos);
   }

   public int func_176201_c(IBlockState state) {
      return 0;
   }

   public IBlockState func_176221_a(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return state.func_177226_a(NORTH, this.canConnectTo(worldIn, pos.func_177978_c(), EnumFacing.SOUTH)).func_177226_a(EAST, this.canConnectTo(worldIn, pos.func_177974_f(), EnumFacing.WEST)).func_177226_a(SOUTH, this.canConnectTo(worldIn, pos.func_177968_d(), EnumFacing.NORTH)).func_177226_a(WEST, this.canConnectTo(worldIn, pos.func_177976_e(), EnumFacing.EAST));
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.CENTER;
   }

   public IBlockState func_185499_a(IBlockState state, Rotation rot) {
      switch (rot) {
         case CLOCKWISE_180:
            return state.func_177226_a(NORTH, state.func_177229_b(SOUTH)).func_177226_a(EAST, state.func_177229_b(WEST)).func_177226_a(SOUTH, state.func_177229_b(NORTH)).func_177226_a(WEST, state.func_177229_b(EAST));
         case COUNTERCLOCKWISE_90:
            return state.func_177226_a(NORTH, state.func_177229_b(EAST)).func_177226_a(EAST, state.func_177229_b(SOUTH)).func_177226_a(SOUTH, state.func_177229_b(WEST)).func_177226_a(WEST, state.func_177229_b(NORTH));
         case CLOCKWISE_90:
            return state.func_177226_a(NORTH, state.func_177229_b(WEST)).func_177226_a(EAST, state.func_177229_b(NORTH)).func_177226_a(SOUTH, state.func_177229_b(EAST)).func_177226_a(WEST, state.func_177229_b(SOUTH));
         default:
            return state;
      }
   }

   public IBlockState func_185471_a(IBlockState state, Mirror mirrorIn) {
      switch (mirrorIn) {
         case LEFT_RIGHT:
            return state.func_177226_a(NORTH, state.func_177229_b(SOUTH)).func_177226_a(SOUTH, state.func_177229_b(NORTH));
         case FRONT_BACK:
            return state.func_177226_a(EAST, state.func_177229_b(WEST)).func_177226_a(WEST, state.func_177229_b(EAST));
         default:
            return state;
      }
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{NORTH, EAST, WEST, SOUTH});
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(PixelmonBlocks.getItemFromBlock(PixelmonBlocks.picketFenceNormalBlock));
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return PixelmonBlocks.getItemFromBlock(PixelmonBlocks.picketFenceNormalBlock);
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityPicketFenceNormal();
   }
}

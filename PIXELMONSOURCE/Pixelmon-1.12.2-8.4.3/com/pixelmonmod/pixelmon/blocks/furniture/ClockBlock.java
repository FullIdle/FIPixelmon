package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.enums.ColorEnum;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityClock;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ClockBlock extends GenericRotatableModelBlock {
   private static final AxisAlignedBB AABBEast = new AxisAlignedBB(0.0, 0.25, 0.25, 0.1, 0.75, 0.75);
   private static final AxisAlignedBB AABBWest = new AxisAlignedBB(0.9, 0.25, 0.25, 1.0, 0.75, 0.75);
   private static final AxisAlignedBB AABBSouth = new AxisAlignedBB(0.25, 0.25, 0.0, 0.75, 0.75, 0.1);
   private static final AxisAlignedBB AABBNorth = new AxisAlignedBB(0.25, 0.25, 0.9, 0.75, 0.75, 1.0);
   private ColorEnum color;

   public ClockBlock(ColorEnum color) {
      super(Material.field_151573_f);
      this.color = color;
      this.func_149711_c(0.5F);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149663_c(color + "Clock");
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return this.getBlockBounds(blockState);
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return this.getBlockBounds(state);
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }

   private AxisAlignedBB getBlockBounds(IBlockState blockState) {
      EnumFacing enumfacing = (EnumFacing)blockState.func_177229_b(BlockProperties.FACING);
      switch (enumfacing) {
         case EAST:
            return AABBEast;
         case WEST:
            return AABBWest;
         case SOUTH:
            return AABBSouth;
         case NORTH:
            return AABBNorth;
         default:
            return null;
      }
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (player.func_184586_b(hand).func_77973_b() instanceof ItemDye) {
         IBlockState st = null;
         switch (player.func_184586_b(hand).func_77960_j()) {
            case 0:
               st = PixelmonBlocks.blackClockBlock.func_176223_P();
               break;
            case 1:
               st = PixelmonBlocks.redClockBlock.func_176223_P();
               break;
            case 2:
               st = PixelmonBlocks.greenClockBlock.func_176223_P();
               break;
            case 3:
               st = PixelmonBlocks.brownClockBlock.func_176223_P();
               break;
            case 4:
               st = PixelmonBlocks.blueClockBlock.func_176223_P();
               break;
            case 5:
               st = PixelmonBlocks.purpleClockBlock.func_176223_P();
               break;
            case 6:
               st = PixelmonBlocks.cyanClockBlock.func_176223_P();
               break;
            case 7:
            case 10:
            case 12:
            case 13:
               st = null;
               break;
            case 8:
               st = PixelmonBlocks.grayClockBlock.func_176223_P();
               break;
            case 9:
               st = PixelmonBlocks.pinkClockBlock.func_176223_P();
               break;
            case 11:
               st = PixelmonBlocks.yellowClockBlock.func_176223_P();
               break;
            case 14:
               st = PixelmonBlocks.orangeClockBlock.func_176223_P();
               break;
            default:
               st = PixelmonBlocks.whiteClockBlock.func_176223_P();
         }

         st = st.func_177226_a(BlockProperties.FACING, state.func_177229_b(BlockProperties.FACING));
         if (st != state) {
            world.func_175656_a(pos, st);
            player.func_184586_b(hand).func_190920_e(player.func_184586_b(hand).func_190916_E() - 1);
         }

         return true;
      } else {
         return super.func_180639_a(world, pos, state, player, hand, side, hitX, hitY, hitZ);
      }
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityClock();
   }

   public ColorEnum getColor() {
      return this.color;
   }
}

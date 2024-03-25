package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.GenericModelBlock;
import com.pixelmonmod.pixelmon.blocks.enums.ColorEnum;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityUmbrella;
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

public class UmbrellaBlock extends GenericModelBlock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.4, 0.0, 0.4, 0.6, 2.4, 0.6);
   private ColorEnum color;

   public UmbrellaBlock(ColorEnum color) {
      super(Material.field_151573_f);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185852_e);
      this.color = color;
      this.func_149663_c(color + "_umbrella");
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
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

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityUmbrella();
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (player.func_184586_b(hand).func_77973_b() instanceof ItemDye) {
         IBlockState st = null;
         switch (player.func_184586_b(hand).func_77960_j()) {
            case 0:
               st = PixelmonBlocks.blackUmbrellaBlock.func_176223_P();
               break;
            case 1:
               st = PixelmonBlocks.redUmbrellaBlock.func_176223_P();
               break;
            case 2:
               st = PixelmonBlocks.greenUmbrellaBlock.func_176223_P();
               break;
            case 3:
               st = PixelmonBlocks.brownUmbrellaBlock.func_176223_P();
               break;
            case 4:
               st = PixelmonBlocks.blueUmbrellaBlock.func_176223_P();
               break;
            case 5:
               st = PixelmonBlocks.purpleUmbrellaBlock.func_176223_P();
               break;
            case 6:
               st = PixelmonBlocks.cyanUmbrellaBlock.func_176223_P();
               break;
            case 7:
            case 10:
            case 12:
            case 13:
               st = null;
               break;
            case 8:
               st = PixelmonBlocks.grayUmbrellaBlock.func_176223_P();
               break;
            case 9:
               st = PixelmonBlocks.pinkUmbrellaBlock.func_176223_P();
               break;
            case 11:
               st = PixelmonBlocks.yellowUmbrellaBlock.func_176223_P();
               break;
            case 14:
               st = PixelmonBlocks.orangeUmbrellaBlock.func_176223_P();
               break;
            default:
               st = PixelmonBlocks.whiteUmbrellaBlock.func_176223_P();
         }

         if (st != state) {
            world.func_175656_a(pos, st);
            player.func_184586_b(hand).func_190920_e(player.func_184586_b(hand).func_190916_E() - 1);
         }

         return true;
      } else {
         return super.func_180639_a(world, pos, state, player, hand, side, hitX, hitY, hitZ);
      }
   }

   public ColorEnum getColor() {
      return this.color;
   }
}

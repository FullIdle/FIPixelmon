package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableSittableModelBlock;
import com.pixelmonmod.pixelmon.blocks.enums.ColorEnum;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCushionChair;
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

public class CushionChair extends GenericRotatableSittableModelBlock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.10000000149011612, 0.0, 0.10000000149011612, 0.8999999761581421, 0.5, 0.8999999761581421);
   private ColorEnum color;

   public CushionChair(ColorEnum color) {
      super(Material.field_151580_n);
      this.color = color;
      this.func_149711_c(0.5F);
      this.func_149672_a(SoundType.field_185854_g);
      this.func_149663_c(color + "CushionChair");
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityCushionChair();
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

   public ColorEnum getColor() {
      return this.color;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (player.func_184586_b(hand).func_77973_b() instanceof ItemDye) {
         IBlockState st = null;
         switch (player.func_184586_b(hand).func_77960_j()) {
            case 0:
               st = PixelmonBlocks.blackCushionChairBlock.func_176223_P();
               break;
            case 1:
               st = PixelmonBlocks.redCushionChairBlock.func_176223_P();
               break;
            case 2:
               st = PixelmonBlocks.greenCushionChairBlock.func_176223_P();
               break;
            case 3:
               st = PixelmonBlocks.brownCushionChairBlock.func_176223_P();
               break;
            case 4:
               st = PixelmonBlocks.blueCushionChairBlock.func_176223_P();
               break;
            case 5:
               st = PixelmonBlocks.purpleCushionChairBlock.func_176223_P();
               break;
            case 6:
               st = PixelmonBlocks.cyanCushionChairBlock.func_176223_P();
               break;
            case 7:
            case 10:
            case 12:
            case 13:
               return true;
            case 8:
               st = PixelmonBlocks.grayCushionChairBlock.func_176223_P();
               break;
            case 9:
               st = PixelmonBlocks.pinkCushionChairBlock.func_176223_P();
               break;
            case 11:
               st = PixelmonBlocks.yellowCushionChairBlock.func_176223_P();
               break;
            case 14:
               st = PixelmonBlocks.orangeCushionChairBlock.func_176223_P();
               break;
            default:
               st = PixelmonBlocks.whiteCushionChairBlock.func_176223_P();
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

   public float getSittingHeight() {
      return 0.45F;
   }
}

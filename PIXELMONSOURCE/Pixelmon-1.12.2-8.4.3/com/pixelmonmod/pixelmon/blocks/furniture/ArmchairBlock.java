package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableSittableModelBlock;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityArmchair;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ArmchairBlock extends GenericRotatableSittableModelBlock {
   private EnumDyeColor color;
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.10000000149011612, 0.0, 0.10000000149011612, 0.8999999761581421, 0.550000011920929, 0.8999999761581421);

   public ArmchairBlock(EnumDyeColor color) {
      super(Material.field_151575_d);
      this.func_149672_a(SoundType.field_185848_a);
      this.color = color;
      this.func_149711_c(0.5F);
      this.func_149663_c(color + "Armchair");
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityArmchair();
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

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (player.func_184586_b(hand).func_77973_b() instanceof ItemDye) {
         IBlockState st = null;
         switch (player.func_184586_b(hand).func_77960_j()) {
            case 0:
               st = PixelmonBlocks.blackArmchairBlock.func_176223_P();
               break;
            case 1:
               st = PixelmonBlocks.redArmchairBlock.func_176223_P();
               break;
            case 2:
               st = PixelmonBlocks.greenArmchairBlock.func_176223_P();
               break;
            case 3:
               st = PixelmonBlocks.brownArmchairBlock.func_176223_P();
               break;
            case 4:
               st = PixelmonBlocks.blueArmchairBlock.func_176223_P();
               break;
            case 5:
               st = PixelmonBlocks.purpleArmchairBlock.func_176223_P();
               break;
            case 6:
               st = PixelmonBlocks.cyanArmchairBlock.func_176223_P();
               break;
            case 7:
               st = PixelmonBlocks.silverArmchairBlock.func_176223_P();
               break;
            case 8:
               st = PixelmonBlocks.grayArmchairBlock.func_176223_P();
               break;
            case 9:
               st = PixelmonBlocks.pinkArmchairBlock.func_176223_P();
               break;
            case 10:
               st = PixelmonBlocks.limeArmchairBlock.func_176223_P();
               break;
            case 11:
               st = PixelmonBlocks.yellowArmchairBlock.func_176223_P();
               break;
            case 12:
               st = PixelmonBlocks.lightblueArmchairBlock.func_176223_P();
               break;
            case 13:
               st = PixelmonBlocks.magentaArmchairBlock.func_176223_P();
               break;
            case 14:
               st = PixelmonBlocks.orangeArmchairBlock.func_176223_P();
               break;
            default:
               st = PixelmonBlocks.whiteArmchairBlock.func_176223_P();
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

   public EnumDyeColor getColor() {
      return this.color;
   }

   public float getSittingHeight() {
      return 0.6F;
   }
}

package com.pixelmonmod.pixelmon.blocks.multiBlocks;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityMultiCouch;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Optional;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCouch extends BlockGenericSittableModelMultiblock {
   private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.1875, 0.9);
   private EnumDyeColor color;

   public BlockCouch(EnumDyeColor color) {
      super(Material.field_151580_n, 2, 0.75, 1);
      this.color = color;
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundType.field_185854_g);
      this.func_149663_c(color.toString().toLowerCase() + "_couch");
   }

   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return AABB;
   }

   public AxisAlignedBB func_180640_a(IBlockState state, World worldIn, BlockPos pos) {
      return AABB;
   }

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.SOLID;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(this.getFloatItem());
   }

   public Item getFloatItem() {
      return Item.func_150898_a(this);
   }

   public int func_149745_a(Random random) {
      return 1;
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return this.getFloatItem();
   }

   public Optional getTileEntity(World world, IBlockState state) {
      return Optional.of(new TileEntityMultiCouch());
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }

   public EnumDyeColor getColor() {
      return this.color;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (player.func_184586_b(hand).func_77973_b() instanceof ItemDye) {
         IBlockState st = null;
         switch (player.func_184586_b(hand).func_77960_j()) {
            case 0:
               st = PixelmonBlocks.blackCouchBlock.func_176223_P();
               break;
            case 1:
               st = PixelmonBlocks.redCouchBlock.func_176223_P();
               break;
            case 2:
               st = PixelmonBlocks.greenCouchBlock.func_176223_P();
               break;
            case 3:
               st = PixelmonBlocks.brownCouchBlock.func_176223_P();
               break;
            case 4:
               st = PixelmonBlocks.blueCouchBlock.func_176223_P();
               break;
            case 5:
               st = PixelmonBlocks.purpleCouchBlock.func_176223_P();
               break;
            case 6:
               st = PixelmonBlocks.cyanCouchBlock.func_176223_P();
               break;
            case 7:
               st = PixelmonBlocks.silverCouchBlock.func_176223_P();
               break;
            case 8:
               st = PixelmonBlocks.grayCouchBlock.func_176223_P();
               break;
            case 9:
               st = PixelmonBlocks.pinkCouchBlock.func_176223_P();
               break;
            case 10:
               st = PixelmonBlocks.limeCouchBlock.func_176223_P();
               break;
            case 11:
               st = PixelmonBlocks.yellowCouchBlock.func_176223_P();
               break;
            case 12:
               st = PixelmonBlocks.lightblueCouchBlock.func_176223_P();
               break;
            case 13:
               st = PixelmonBlocks.magentaCouchBlock.func_176223_P();
               break;
            case 14:
               st = PixelmonBlocks.orangeCouchBlock.func_176223_P();
               break;
            default:
               st = PixelmonBlocks.whiteCouchBlock.func_176223_P();
         }

         st = st.func_177226_a(BlockProperties.FACING, state.func_177229_b(BlockProperties.FACING)).func_177226_a(MULTIPOS, state.func_177229_b(MULTIPOS));
         if (st != state) {
            BlockPos oPos;
            if (state.func_177229_b(MULTIPOS) == EnumMultiPos.BASE) {
               oPos = pos.func_177972_a(((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176746_e());
            } else {
               oPos = pos.func_177972_a(((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176734_d());
            }

            IBlockState other = world.func_180495_p(oPos);
            world.func_175656_a(oPos, st.func_177226_a(BlockProperties.FACING, other.func_177229_b(BlockProperties.FACING)).func_177226_a(MULTIPOS, other.func_177229_b(MULTIPOS)));
            world.func_175656_a(pos, st);
            player.func_184586_b(hand).func_190920_e(player.func_184586_b(hand).func_190916_E() - 1);
         }

         return true;
      } else {
         return super.func_180639_a(world, pos, state, player, hand, side, hitX, hitY, hitZ);
      }
   }

   float getSittingHeight() {
      return 0.6F;
   }
}

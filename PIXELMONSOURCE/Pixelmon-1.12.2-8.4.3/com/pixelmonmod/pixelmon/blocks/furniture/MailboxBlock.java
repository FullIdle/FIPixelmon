package com.pixelmonmod.pixelmon.blocks.furniture;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.PixelmonBlock;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MailboxBlock extends PixelmonBlock {
   private static final Map colorMap = new HashMap();
   private static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.15, 0.0, 0.15, 0.85, 0.6, 0.85);
   public static final PropertyBool OPEN = PropertyBool.func_177716_a("open");
   public final EnumDyeColor color;

   public MailboxBlock(EnumDyeColor color) {
      super(Material.field_151575_d);
      this.color = color;
      colorMap.put(color, this);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149711_c(1.0F);
      this.func_149663_c("mailbox_" + color.func_176610_l());
      this.func_149647_a(PixelmonCreativeTabs.decoration);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockProperties.FACING, EnumFacing.SOUTH).func_177226_a(OPEN, false));
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && hand == EnumHand.MAIN_HAND) {
         ItemStack heldItem = player.func_184586_b(hand);
         if (!heldItem.func_190926_b() && heldItem.func_77973_b() instanceof ItemDye) {
            EnumDyeColor dyeColor = EnumDyeColor.func_176766_a(heldItem.func_77952_i());
            IBlockState newState = ((MailboxBlock)colorMap.get(dyeColor)).func_176203_a(this.func_176201_c(state));
            world.func_175656_a(pos, newState);
            if (!player.field_71075_bZ.field_75098_d) {
               heldItem.func_190918_g(1);
            }

            return true;
         }

         IBlockState newState = state.func_177226_a(OPEN, !(Boolean)state.func_177229_b(OPEN));
         world.func_175656_a(pos, newState);
      }

      return true;
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{BlockProperties.FACING, OPEN});
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.func_176223_P().func_177226_a(BlockProperties.FACING, placer.func_174811_aO().func_176734_d());
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
      IBlockState state = this.func_176223_P().func_177226_a(BlockProperties.FACING, EnumFacing.func_176731_b(meta & 3));
      if (meta >= 4) {
         state.func_177226_a(OPEN, true);
      } else {
         state.func_177226_a(OPEN, false);
      }

      return state;
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumFacing)state.func_177229_b(BlockProperties.FACING)).func_176736_b() + ((Boolean)state.func_177229_b(OPEN) ? 4 : 0);
   }

   public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
      return BlockFaceShape.UNDEFINED;
   }
}

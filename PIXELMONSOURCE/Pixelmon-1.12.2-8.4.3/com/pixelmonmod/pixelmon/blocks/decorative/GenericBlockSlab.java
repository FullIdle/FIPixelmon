package com.pixelmonmod.pixelmon.blocks.decorative;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GenericBlockSlab extends BlockSlab {
   public static final PropertyEnum VARIANT = PropertyEnum.func_177709_a("variant", Variant.class);
   public final Block block;
   private final boolean isDouble;

   public GenericBlockSlab(Block block, boolean isDouble) {
      super(block.func_149688_o((IBlockState)null));
      this.block = block;
      this.isDouble = isDouble;
      this.func_149647_a(block.func_149708_J());
      IBlockState iblockstate = this.field_176227_L.func_177621_b();
      if (!this.func_176552_j()) {
         iblockstate = iblockstate.func_177226_a(field_176554_a, EnumBlockHalf.BOTTOM);
      }

      this.func_180632_j(iblockstate.func_177226_a(VARIANT, GenericBlockSlab.Variant.DEFAULT));
      this.field_149762_H = block.func_185467_w();
      this.field_149783_u = true;
   }

   public String func_150002_b(int meta) {
      return super.func_149739_a();
   }

   public boolean func_176552_j() {
      return this.isDouble;
   }

   public IProperty func_176551_l() {
      return VARIANT;
   }

   public Comparable func_185674_a(ItemStack stack) {
      return GenericBlockSlab.Variant.DEFAULT;
   }

   public float func_176195_g(IBlockState blockState, World worldIn, BlockPos pos) {
      return this.block.func_176195_g(this.block.func_176223_P(), worldIn, pos);
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return PixelmonBlocks.getItemFromBlock(this);
   }

   public ItemStack func_185473_a(World worldIn, BlockPos pos, IBlockState state) {
      return new ItemStack(PixelmonBlocks.getItemFromBlock(this), 1, this.func_180651_a(state));
   }

   public IBlockState func_176203_a(int meta) {
      IBlockState iblockstate = this.func_176223_P().func_177226_a(VARIANT, GenericBlockSlab.Variant.DEFAULT);
      if (!this.func_176552_j()) {
         iblockstate = iblockstate.func_177226_a(field_176554_a, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
      }

      return iblockstate;
   }

   public int func_176201_c(IBlockState state) {
      int i = 0;
      if (!this.func_176552_j() && state.func_177229_b(field_176554_a) == EnumBlockHalf.TOP) {
         i |= 8;
      }

      return i;
   }

   protected BlockStateContainer func_180661_e() {
      return this.func_176552_j() ? new BlockStateContainer(this, new IProperty[]{VARIANT}) : new BlockStateContainer(this, new IProperty[]{field_176554_a, VARIANT});
   }

   public static enum Variant implements IStringSerializable {
      DEFAULT;

      public String func_176610_l() {
         return "default";
      }
   }
}

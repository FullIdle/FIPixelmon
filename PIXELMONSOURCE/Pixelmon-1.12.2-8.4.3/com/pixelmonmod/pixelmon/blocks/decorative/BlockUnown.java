package com.pixelmonmod.pixelmon.blocks.decorative;

import com.pixelmonmod.pixelmon.blocks.GenericBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUnown extends GenericBlock {
   static PropertyInteger alphabetIndex = PropertyInteger.func_177719_a("index", 0, 15);
   public String[] alphabetInUse = null;
   private static final String[] alphabet1 = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};
   private static final String[] alphabet2 = new String[]{"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "!", "?", "_"};

   public BlockUnown(boolean secondHalf) {
      super(Material.field_151576_e);
      this.func_149711_c(5.0F);
      if (secondHalf) {
         this.alphabetInUse = alphabet2;
      } else {
         this.alphabetInUse = alphabet1;
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_149666_a(CreativeTabs creativeTabs, NonNullList list) {
      for(int meta = 0; meta < this.alphabetInUse.length; ++meta) {
         list.add(new ItemStack(this, 1, meta));
      }

   }

   public int func_180651_a(IBlockState state) {
      return (Integer)state.func_177229_b(alphabetIndex);
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return super.func_180642_a(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).func_177226_a(alphabetIndex, meta);
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(alphabetIndex, meta);
   }

   public int func_176201_c(IBlockState state) {
      return (Integer)state.func_177229_b(alphabetIndex);
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{alphabetIndex});
   }

   public static int getNumUnownBlocks() {
      return alphabet1.length + alphabet2.length;
   }

   public static int getBlankUnownBlockIndex() {
      return alphabet2.length - 1;
   }
}

package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.RandomHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GenericOre extends Block {
   private List drops = new ArrayList(1);
   private int base = 0;
   private int deviant = 0;

   public GenericOre(Material par2Material) {
      super(par2Material);
      this.func_149711_c(0.5F);
   }

   public GenericOre addDrop(ItemStack drop) {
      this.drops.add(drop);
      return this;
   }

   public GenericOre setBase(int baseXP) {
      this.base = baseXP;
      return this;
   }

   public GenericOre setDeviant(int deviantXP) {
      this.deviant = deviantXP;
      return this;
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      if (this.drops.isEmpty()) {
         return super.getDrops(world, pos, state, fortune);
      } else {
         List drops1 = new ArrayList(this.drops.size());
         Iterator var6 = this.drops.iterator();

         while(var6.hasNext()) {
            ItemStack stack = (ItemStack)var6.next();
            ItemStack count = stack.func_77946_l();
            count.func_190920_e(RandomHelper.getFortuneAmount(fortune));
            drops1.add(count);
         }

         return drops1;
      }
   }

   public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
      int expValue = (int)(Math.random() * (double)this.deviant + (double)this.base);
      return expValue;
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return this.field_149764_J == Material.field_151592_s ? BlockRenderLayer.CUTOUT : super.func_180664_k();
   }

   public boolean func_149686_d(IBlockState state) {
      return this.field_149764_J == Material.field_151592_s ? false : super.func_149686_d(state);
   }

   public boolean func_149662_c(IBlockState state) {
      return this.field_149764_J != Material.field_151592_s;
   }

   public GenericOre setSound(SoundType type) {
      this.func_149672_a(type);
      return this;
   }
}

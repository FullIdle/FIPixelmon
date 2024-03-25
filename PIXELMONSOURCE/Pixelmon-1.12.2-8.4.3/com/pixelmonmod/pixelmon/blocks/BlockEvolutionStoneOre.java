package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumEvolutionStone;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockEvolutionStoneOre extends Block {
   private EnumEvolutionStone type = null;

   public BlockEvolutionStoneOre(EnumEvolutionStone type, float hardness, String itemName) {
      super(Material.field_151576_e);
      this.type = type;
      this.func_149711_c(hardness);
      this.func_149672_a(SoundType.field_185851_d);
      if (type == EnumEvolutionStone.Waterstone) {
         this.func_149715_a(0.5F);
      }

      this.func_149647_a(PixelmonCreativeTabs.natural);
      this.func_149663_c(itemName);
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.MODEL;
   }

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.SOLID;
   }

   public boolean func_149662_c(IBlockState state) {
      return true;
   }

   public boolean func_149686_d(IBlockState state) {
      return true;
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      ArrayList drops = new ArrayList();
      Item item = null;
      switch (this.type) {
         case Thunderstone:
            item = PixelmonItems.thunderStoneShard;
            break;
         case Leafstone:
            item = PixelmonItems.leafStoneShard;
            break;
         case Waterstone:
            item = PixelmonItems.waterStoneShard;
            break;
         case Firestone:
            item = PixelmonItems.fireStoneShard;
            break;
         case Sunstone:
            item = PixelmonItems.sunStoneShard;
            break;
         case Dawnstone:
            item = PixelmonItems.dawnStoneShard;
            break;
         case Duskstone:
            item = PixelmonItems.duskStoneShard;
            break;
         case Moonstone:
            item = PixelmonItems.moonStoneShard;
            break;
         case Shinystone:
            item = PixelmonItems.shinyStoneShard;
      }

      if (item != null) {
         drops.add(new ItemStack(item, RandomHelper.getFortuneAmount(fortune)));
      }

      return drops;
   }
}

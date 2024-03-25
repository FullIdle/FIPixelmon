package com.pixelmonmod.pixelmon.blocks.ultraspace;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockDeepSea extends Block {
   public BlockDeepSea(Material par2Material) {
      super(par2Material);
      this.func_149663_c("deep_sea");
      this.func_149672_a(SoundType.field_185853_f);
      this.func_149711_c(1.0F);
      this.func_149752_b(1.0F);
      this.func_149647_a(PixelmonCreativeTabs.natural);
      this.setHarvestLevel("pickaxe", 3);
   }

   public int func_149745_a(Random random) {
      return 1;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      double value = rand.nextDouble();
      if (value < 0.005) {
         return PixelmonItems.sapphire;
      } else if (value < 0.01) {
         return PixelmonItems.crystal;
      } else {
         return value < 0.015 ? PixelmonItems.amethyst : Items.field_190931_a;
      }
   }
}

package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.config.PixelmonItemsFossils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockFossil extends Block {
   public BlockFossil() {
      super(Material.field_151576_e);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149647_a(PixelmonCreativeTabs.natural);
      this.func_149663_c("fossil");
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.MODEL;
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      ArrayList items = new ArrayList();
      int amount = RandomHelper.getFortuneAmount(fortune);

      for(int i = 0; i < amount; ++i) {
         items.add(PixelmonItemsFossils.getRandomFossil());
      }

      return items;
   }
}

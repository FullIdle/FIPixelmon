package com.pixelmonmod.pixelmon.blocks.multiBlocks;

import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockGenericModelMultiblock extends MultiBlock {
   protected BlockGenericModelMultiblock(Material material, int width, double height, int length) {
      super(material, width, height, length);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public Item getDroppedItem(World world, BlockPos pos) {
      return null;
   }
}

package com.pixelmonmod.pixelmon.blocks.decorative;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockPlus extends Block {
   protected int idDropped;
   protected int amountDropped;
   protected boolean opaqueCube = true;

   public BlockPlus(Material par2Material) {
      super(par2Material);
   }

   public BlockPlus setOpaque(boolean opaque) {
      this.opaqueCube = opaque;
      return this;
   }

   public BlockPlus setRenderOptions(int renderType, boolean opaqueCube) {
      this.opaqueCube = opaqueCube;
      return this;
   }

   public BlockPlus setIdDropped(int id) {
      this.idDropped = id;
      return this;
   }

   public BlockPlus setAmountDropped(int amount) {
      this.amountDropped = amount;
      return this;
   }

   public boolean func_149662_c(IBlockState state) {
      return this.opaqueCube;
   }
}

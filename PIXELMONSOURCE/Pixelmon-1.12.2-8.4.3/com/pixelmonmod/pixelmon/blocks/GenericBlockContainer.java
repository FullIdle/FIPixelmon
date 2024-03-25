package com.pixelmonmod.pixelmon.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class GenericBlockContainer extends BlockContainer {
   protected GenericBlockContainer(Material materialIn) {
      super(materialIn);
   }

   public abstract TileEntity func_149915_a(World var1, int var2);
}

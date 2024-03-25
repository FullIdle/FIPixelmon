package com.pixelmonmod.pixelmon.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public interface IBlockHasOwner {
   void setOwner(BlockPos var1, EntityPlayer var2);
}

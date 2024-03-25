package com.pixelmonmod.pixelmon.blocks.tileEntities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileEntityDecorativeBase extends TileEntity {
   public boolean isAnotherWithSameDirectionOnSide(World world, int x, int y, int z, EnumFacing dir) {
      TileEntity ent = null;
      return this.isSameDirectionAndType((TileEntity)ent);
   }

   public boolean isSameDirectionAndType(TileEntity ent) {
      return ent != null && ent.getClass() == this.getClass() && (ent.func_145832_p() & 12) == (this.func_145832_p() & 12);
   }
}

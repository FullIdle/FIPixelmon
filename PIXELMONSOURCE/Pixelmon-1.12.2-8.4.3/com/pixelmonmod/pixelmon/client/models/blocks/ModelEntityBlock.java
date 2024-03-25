package com.pixelmonmod.pixelmon.client.models.blocks;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityDecorativeBase;
import net.minecraft.client.model.ModelBase;

public abstract class ModelEntityBlock extends ModelBase {
   public abstract void renderTileEntity(TileEntityDecorativeBase var1, float var2);
}

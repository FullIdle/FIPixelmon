package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityFoldingChair;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelFoldingChair;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import net.minecraft.block.state.IBlockState;

public class FoldingChairRenderer extends TileEntityRenderer {
   private static final GenericModelHolder model = new GenericModelHolder(ModelFoldingChair.class);

   public FoldingChairRenderer() {
      this.scale = 0.0625F;
      this.yOffset = 1.5F;
   }

   public void renderTileEntity(TileEntityFoldingChair foldingchair, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(foldingchair.getTexture());
      model.render();
   }
}

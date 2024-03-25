package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTV;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelTV;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class TVRenderer extends TileEntityRenderer {
   private static final GenericModelHolder model = new GenericModelHolder(ModelTV.class);
   private static final ResourceLocation texture = new ResourceLocation("pixelmon", "textures/blocks/TVModel.png");

   public TVRenderer() {
      this.correctionAngles = 180;
      this.scale = 0.0625F;
      this.yOffset = 1.5F;
   }

   public void renderTileEntity(TileEntityTV te, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(texture);
      model.render();
   }
}

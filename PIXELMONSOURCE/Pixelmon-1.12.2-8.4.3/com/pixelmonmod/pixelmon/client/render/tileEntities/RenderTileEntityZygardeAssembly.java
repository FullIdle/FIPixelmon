package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityZygardeAssembly;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderTileEntityZygardeAssembly extends TileEntityRenderer {
   private static final GenericModelHolder MODEL = new GenericModelHolder("blocks/reassembly_unit/reassembly_unit.pqc");
   private static final GenericModelHolder STONE = new GenericModelHolder("blocks/reassembly_unit/reassembly_unit_stone.pqc");
   private static final GenericSmdModel GLASS = new GenericSmdModel("models/blocks/reassembly_unit", "reassembly_unit_glass.pqc");
   private static final ResourceLocation INACTIVE_TEXTURE = new ResourceLocation("pixelmon", "textures/blocks/reassembly_unit/unit_inactive.png");
   private static final ResourceLocation ASSEMBLING_TEXTURE = new ResourceLocation("pixelmon", "textures/blocks/reassembly_unit/unit_assembling.png");
   private static final ResourceLocation SEPARATING_TEXTURE = new ResourceLocation("pixelmon", "textures/blocks/reassembly_unit/unit_separating.png");

   public RenderTileEntityZygardeAssembly() {
      GLASS.modelRenderer.setTransparent(0.5F);
   }

   public void renderTileEntity(TileEntityZygardeAssembly te, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(te.getMode() == TileEntityZygardeAssembly.Mode.ASSEMBLING ? ASSEMBLING_TEXTURE : (te.getMode() == TileEntityZygardeAssembly.Mode.SEPARATING ? SEPARATING_TEXTURE : INACTIVE_TEXTURE));
      if (MinecraftForgeClient.getRenderPass() == 0) {
         MODEL.render(0.04F);
         STONE.render(0.04F);
      } else {
         GLASS.renderModel(0.04F);
      }

   }
}

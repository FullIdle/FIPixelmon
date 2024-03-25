package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityFossilDisplay;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.items.ItemFossil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class FossilDisplayRenderer extends TileEntityRenderer {
   private static final GenericModelHolder fossilDisplayCase = new GenericModelHolder("pixelutilities/fossil_display/fossil_display.pqc");
   private static final GenericSmdModel fossilDisplayGlass = new GenericSmdModel("models/pixelutilities/fossil_display", "fossil_display_glass.pqc");
   private static final ResourceLocation texture = new ResourceLocation("pixelmon", "textures/blocks/FossilDisplayModel.png");

   public FossilDisplayRenderer() {
      this.correctionAngles = 180;
      fossilDisplayGlass.modelRenderer.setTransparent(0.5F);
   }

   public void renderTileEntity(TileEntityFossilDisplay fossilDisplay, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      if (fossilDisplay.renderPass == 1) {
         this.func_147499_a(texture);
         fossilDisplayGlass.setFrame(fossilDisplay.frame);
         fossilDisplayGlass.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
      } else {
         ItemFossil fossil = fossilDisplay.getItemInDisplay();
         this.func_147499_a(texture);
         fossilDisplayCase.render();
         if (fossil != null) {
            GlStateManager.func_179137_b(0.0, -0.85, 0.0);
            this.func_147499_a(fossil.getFossil().getTexture());
            ((GenericModelHolder)fossil.getFossil().getModel()).render();
         }
      }

   }
}

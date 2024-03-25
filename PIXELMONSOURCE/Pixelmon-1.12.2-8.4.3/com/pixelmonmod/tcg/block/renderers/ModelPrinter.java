package com.pixelmonmod.tcg.block.renderers;

import com.pixelmonmod.pixelmon.client.models.ModelCustomWrapper;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRenderer;
import com.pixelmonmod.pixelmon.client.models.smd.GabeNewellException;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelPrinter extends ModelBase {
   public PixelmonModelRenderer modelRenderer;
   private ValveStudioModel model;
   private ResourceLocation pqcResource = new ResourceLocation("tcg", "models/printer/printer.pqc");

   public ModelPrinter() throws GabeNewellException {
      this.reloadModel();
   }

   public void reloadModel() throws GabeNewellException {
      this.modelRenderer = new PixelmonModelRenderer(this, "body");
      this.model = new TCGValveStudioModel(this.pqcResource);
      this.modelRenderer.addCustomModel(new ModelCustomWrapper(this.model));
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      float scale = 0.3846154F;
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179114_b(0.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.modelRenderer.func_78785_a(f5);
   }
}

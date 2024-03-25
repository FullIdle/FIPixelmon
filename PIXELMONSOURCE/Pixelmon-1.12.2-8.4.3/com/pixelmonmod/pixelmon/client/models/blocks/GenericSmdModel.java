package com.pixelmonmod.pixelmon.client.models.blocks;

import com.pixelmonmod.pixelmon.client.models.ModelCustomWrapper;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRenderer;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimation;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModelLoader;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class GenericSmdModel extends ModelBase {
   public PixelmonModelRenderer modelRenderer;
   private ValveStudioModel model;
   private ResourceLocation pqcResource;
   private boolean overrideSmoothShading;

   public GenericSmdModel(ResourceLocation pqcResource, boolean overrideSmoothShading) {
      this.overrideSmoothShading = false;
      this.overrideSmoothShading = overrideSmoothShading;
      this.pqcResource = pqcResource;
      this.reloadModel();
   }

   public GenericSmdModel(String basePath, String pqcPath, boolean overrideSmoothShading) {
      this(new ResourceLocation("pixelmon:" + basePath + "/" + pqcPath), overrideSmoothShading);
   }

   public GenericSmdModel(String basePath, String pqcPath) {
      this(basePath, pqcPath, false);
   }

   public void reloadModel() {
      this.modelRenderer = new PixelmonModelRenderer(this, "body");
      this.model = (ValveStudioModel)ValveStudioModelLoader.instance.loadModel(this.pqcResource, this.overrideSmoothShading);
      this.modelRenderer.addCustomModel(new ModelCustomWrapper(this.model));
   }

   public ValveStudioModel getModel() {
      return this.model;
   }

   public void setFrame(int frame) {
      ValveStudioModel valveStudioModel = (ValveStudioModel)((ModelCustomWrapper)this.modelRenderer.objs.get(0)).model;
      SmdAnimation animation = valveStudioModel.currentSequence.checkForFinalFrame(frame);
      animation.setCurrentFrame((int)Math.floor((double)(frame % animation.getNumFrames())));
      valveStudioModel.animate();
   }

   public void renderModel(float scale) {
      this.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, scale);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.modelRenderer.render(f5, 0.0F);
   }
}

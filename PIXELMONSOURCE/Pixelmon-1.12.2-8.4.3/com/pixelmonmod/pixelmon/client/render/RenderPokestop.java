package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelPokestop;
import com.pixelmonmod.pixelmon.entities.EntityPokestop;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPokestop extends RenderLivingBase {
   private static final GenericModelHolder model = new GenericModelHolder(ModelPokestop.class);

   public RenderPokestop(RenderManager renderManagerIn) {
      super(renderManagerIn, model.getModel(), 0.0F);
   }

   @Nullable
   protected ResourceLocation getEntityTexture(EntityPokestop entity) {
      return null;
   }

   public void doRender(EntityPokestop entity, double x, double y, double z, float entityYaw, float partialTicks) {
      this.func_110776_a(GuiResources.whitePixel);
      GlStateManager.func_179141_d();
      GlStateManager.func_179145_e();
      GlStateManager.func_179091_B();
      boolean flag = this.func_177090_c(entity, partialTicks);
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(x, y, z);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.func_179114_b(entityYaw, 0.0F, 1.0F, 0.0F);
      float scale = 0.15F;
      GlStateManager.func_179152_a(scale, scale, scale);
      float size = entity.getSize();
      GlStateManager.func_179137_b(0.0, (double)size * -3.75, 0.0);
      GlStateManager.func_179152_a(size, size, size);
      int[] rgb = entity.getColor();
      GlStateManager.func_179131_c((float)rgb[0] / 255.0F, (float)rgb[1] / 255.0F, (float)rgb[2] / 255.0F, 1.0F);
      GlStateManager.func_179152_a(scale, scale, scale);
      ((ModelPokestop)model.getModel()).applyValuesFromEntity(entity);
      model.render();
      GlStateManager.func_179121_F();
      GlStateManager.func_179084_k();
      if (flag) {
         this.func_177091_f();
      }

      GlStateManager.func_179101_C();
   }
}

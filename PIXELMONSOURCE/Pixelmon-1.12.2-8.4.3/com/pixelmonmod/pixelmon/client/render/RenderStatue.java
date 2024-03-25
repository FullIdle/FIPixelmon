package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.client.models.PixelmonModelBase;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelSmd;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderStatue extends RenderLiving {
   private int defaultNameRenderDistance = 8;
   private int defaultBossNameRenderDistanceExtension = 8;
   private int configNameRenderMultiplier;
   private int nameRenderDistanceNormal;
   private int nameRenderDistanceBoss;

   public RenderStatue(RenderManager manager) {
      super(manager, (ModelBase)null, 0.5F);
      this.configNameRenderMultiplier = Math.max(1, Math.min(PixelmonConfig.nameplateRangeModifier, 3));
      this.nameRenderDistanceNormal = this.defaultNameRenderDistance * this.configNameRenderMultiplier;
      this.nameRenderDistanceBoss = this.nameRenderDistanceNormal + this.defaultBossNameRenderDistanceExtension;
   }

   public void doRender(EntityStatue statue, double d, double d1, double d2, float f, float f1) {
      GlStateManager.func_179092_a(516, 0.1F);
      if (statue.getPokemon() != null) {
         float distance = statue.func_70032_d(this.field_76990_c.field_78734_h);
         if (statue.getModel() != null) {
            this.renderStatue(statue, d, d1, d2, f, f1, false);
         }

         float renderdistance = statue.getBossMode() != EnumBossMode.NotBoss ? (float)this.nameRenderDistanceBoss : (float)this.nameRenderDistanceNormal;
         if (distance <= renderdistance) {
            this.drawNameTag(statue, d, d1, d2, false);
         }

      }
   }

   public void renderStatue(EntityStatue pixelmon, double x, double y, double z, float par8, float partialTicks, boolean fromPokedex) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179129_p();
      this.field_77045_g = pixelmon.getModel();
      this.field_77045_g.field_78095_p = this.func_77040_d(pixelmon, partialTicks);
      this.field_77045_g.field_78093_q = pixelmon.func_184218_aH();
      this.field_77045_g.field_78091_s = pixelmon.func_70631_g_();
      if (this.field_77045_g instanceof PixelmonModelSmd && ((PixelmonModelSmd)this.field_77045_g).theModel.hasAnimations()) {
         ((PixelmonModelSmd)this.field_77045_g).setupForRender(pixelmon);
         if (!pixelmon.getShouldAnimate()) {
            int frame = MathHelper.func_76125_a(pixelmon.getAnimationFrame(), 0, pixelmon.getFrameCount() - 1);
            ((PixelmonModelSmd)this.field_77045_g).theModel.currentSequence.current().setCurrentFrame(frame);
         }

         pixelmon.field_70170_p.field_72984_F.func_76320_a("statue_animate");
         ((PixelmonModelSmd)this.field_77045_g).theModel.animate();
         pixelmon.field_70170_p.field_72984_F.func_76319_b();
      }

      try {
         float f2 = this.func_77034_a(pixelmon.field_70760_ar, pixelmon.field_70761_aq, partialTicks);
         float f3 = this.func_77034_a(pixelmon.field_70758_at, pixelmon.field_70759_as, partialTicks);
         float f4 = f3 - f2;
         float f9 = pixelmon.field_70127_C + (pixelmon.field_70125_A - pixelmon.field_70127_C) * partialTicks;
         this.func_77039_a(pixelmon, x, y, z);
         float f5 = 0.0F;
         this.func_77043_a(pixelmon, f5, f2, partialTicks);
         GlStateManager.func_179091_B();
         GlStateManager.func_179152_a(-1.0F, -1.0F, 1.0F);
         this.preRenderCallback(pixelmon, partialTicks, fromPokedex);
         GlStateManager.func_179109_b(0.0F, -1.5F, 0.0F);
         float f7 = pixelmon.field_184618_aE + (pixelmon.field_70721_aZ - pixelmon.field_184618_aE) * partialTicks;
         float f8 = pixelmon.field_184619_aG - pixelmon.field_70721_aZ * (1.0F - partialTicks);
         GlStateManager.func_179141_d();
         this.field_77045_g.func_78086_a(pixelmon, f8, f7, partialTicks);
         this.field_77045_g.func_78087_a(f8, f7, f5, f4, f9, 0.0625F, pixelmon);
         if (pixelmon.getBossMode() != EnumBossMode.NotBoss) {
            GlStateManager.func_179131_c(pixelmon.getBossMode().r, pixelmon.getBossMode().g, pixelmon.getBossMode().b, 1.0F);
         } else if (pixelmon.getColor() != Color.WHITE) {
            GlStateManager.func_179131_c((float)pixelmon.getColor().getRed() / 255.0F, (float)pixelmon.getColor().getGreen() / 255.0F, (float)pixelmon.getColor().getBlue() / 255.0F, 1.0F);
         } else {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         }

         boolean flag = this.func_177090_c(pixelmon, partialTicks);
         pixelmon.field_70170_p.field_72984_F.func_76320_a("statue_render");
         this.func_77036_a(pixelmon, f8, f7, f5, 0.0F, 0.0F, 0.0625F);
         pixelmon.field_70170_p.field_72984_F.func_76319_b();
         float distance = pixelmon.func_70032_d(this.field_76990_c.field_78734_h);
         if (PixelmonConfig.emissiveTextures && EmissiveTextures.hasEmissive(pixelmon) && distance < (float)PixelmonConfig.emissiveTexturesDistance) {
            try {
               pixelmon.field_70170_p.field_72984_F.func_76320_a("statue_emissive");
               EmissiveTextures.startEmissive();
               this.func_77036_a(pixelmon, f8, f7, f5, 0.0F, 0.0F, 0.0625F);
            } finally {
               EmissiveTextures.stopEmissive();
               pixelmon.field_70170_p.field_72984_F.func_76319_b();
            }
         }

         if (flag) {
            this.func_177091_f();
         }

         GlStateManager.func_179132_a(true);
         GlStateManager.func_179101_C();
      } catch (Exception var24) {
         var24.printStackTrace();
      }

      OpenGlHelper.func_77473_a(OpenGlHelper.field_77476_b);
      if (!fromPokedex) {
         GlStateManager.func_179098_w();
      }

      OpenGlHelper.func_77473_a(OpenGlHelper.field_77478_a);
      GlStateManager.func_179089_o();
      GlStateManager.func_179121_F();
      this.func_177067_a(pixelmon, x, y, z);
   }

   public void drawNameTag(EntityStatue entityPixelmon, double par2, double par4, double par6, boolean owned) {
      if (Minecraft.func_71382_s()) {
         String s = entityPixelmon.getLabel();
         if (!entityPixelmon.func_70093_af() && !s.isEmpty()) {
            this.renderLabel(entityPixelmon, s, par2, par4, par6, owned);
         }
      }

   }

   protected void renderLabel(EntityStatue statue, String name, double x, double y, double z, boolean owned) {
      statue.func_70068_e(this.field_76990_c.field_78734_h);
      FontRenderer var12 = this.func_76983_a();
      float var13 = 1.6F;
      float baseScale = 0.016666668F * var13;
      GlStateManager.func_179094_E();
      float scaleFactor = PixelmonConfig.scaleModelsUp ? 1.3F : 1.0F;
      scaleFactor *= statue.getScaleFactor();
      GlStateManager.func_179109_b((float)x + 0.0F, (float)y + 0.7F + statue.field_70131_O * statue.getPixelmonScale() * scaleFactor, (float)z);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179152_a(-baseScale, -baseScale, baseScale);
      GlStateManager.func_179140_f();
      GlStateManager.func_179132_a(false);
      if (owned) {
         GlStateManager.func_179097_i();
      }

      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexbuffer = tessellator.func_178180_c();
      byte var16 = 0;
      GlStateManager.func_179090_x();
      int var17 = var12.func_78256_a(name) / 2;
      vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      vertexbuffer.func_181662_b((double)(-var17 - 1), (double)(-1 + var16), 0.0).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
      vertexbuffer.func_181662_b((double)(-var17 - 1), (double)(8 + var16), 0.0).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
      vertexbuffer.func_181662_b((double)(var17 + 1), (double)(8 + var16), 0.0).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
      vertexbuffer.func_181662_b((double)(var17 + 1), (double)(-1 + var16), 0.0).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179098_w();
      var12.func_78276_b(name, -var12.func_78256_a(name) / 2, var16, 553648127);
      if (owned) {
         GlStateManager.func_179126_j();
      }

      GlStateManager.func_179132_a(true);
      var12.func_78276_b(name, -var12.func_78256_a(name) / 2, var16, statue.getBossMode().getColourInt());
      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179121_F();
   }

   protected void preRenderScale(EntityStatue entity, float f, boolean fromGui) {
      float scaleFactor = 1.0F;
      if (entity.getModel() instanceof PixelmonModelBase) {
         scaleFactor *= ((PixelmonModelBase)entity.getModel()).getScale();
      }

      scaleFactor *= PixelmonConfig.scaleModelsUp ? 1.3F : 1.0F;
      scaleFactor *= entity.getScaleFactor();
      if (fromGui) {
         GlStateManager.func_179152_a(scaleFactor, scaleFactor, scaleFactor);
      } else {
         GlStateManager.func_179152_a(scaleFactor * entity.getPixelmonScale(), scaleFactor * entity.getPixelmonScale(), scaleFactor * entity.getPixelmonScale());
      }

   }

   protected void preRenderCallback(EntityStatue entityStatue, float f) {
      this.preRenderScale(entityStatue, f, false);
   }

   protected void preRenderCallback(EntityStatue entityLiving, float f, boolean fromGui) {
      this.preRenderScale(entityLiving, f, fromGui);
   }

   protected ResourceLocation getEntityTexture(EntityStatue entityStatue) {
      return entityStatue.getTexture();
   }

   protected boolean bindEntityTexture(EntityStatue entity) {
      ResourceLocation resourcelocation = this.getEntityTexture(entity);
      if (EmissiveTextures.isRendering() && resourcelocation != null) {
         resourcelocation = EmissiveTextures.getEmissive(resourcelocation);
      }

      if (resourcelocation == null) {
         return false;
      } else {
         this.func_110776_a(resourcelocation);
         return true;
      }
   }

   protected float func_77034_a(float par1, float par2, float par3) {
      float f3;
      for(f3 = par2 - par1; f3 < -180.0F; f3 += 360.0F) {
      }

      while(f3 >= 180.0F) {
         f3 -= 360.0F;
      }

      return par1 + par3 * f3;
   }
}

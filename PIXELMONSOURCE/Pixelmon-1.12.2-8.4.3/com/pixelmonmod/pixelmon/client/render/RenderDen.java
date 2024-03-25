package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.awt.Color;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDen extends RenderLivingBase {
   private static final GenericModelHolder model = new GenericModelHolder("blocks/den/raid_den.pqc");
   private static final ResourceLocation texture = new ResourceLocation("pixelmon", "textures/blocks/den/raid_den.png");

   public RenderDen(RenderManager renderManagerIn) {
      super(renderManagerIn, model.getModel(), 0.0F);
   }

   public boolean shouldRender(EntityDen livingEntity, ICamera camera, double camX, double camY, double camZ) {
      return true;
   }

   @Nullable
   protected ResourceLocation getEntityTexture(EntityDen entity) {
      return null;
   }

   public void doRender(EntityDen entity, double x, double y, double z, float entityYaw, float partialTicks) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (!entity.isBaseHidden()) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(x, y, z);
         this.renderModel(model, texture);
         GlStateManager.func_179121_F();
      }

   }

   public boolean func_188295_H_() {
      return true;
   }

   public void renderMultipass(EntityDen entity, double x, double y, double z, float entityYaw, float partialTicks) {
      Optional optional = entity.getData();
      boolean decoration = entity.isDecoration();
      boolean raid = optional.isPresent();
      boolean animate = entity.isAnimating();
      int beams = entity.getBeams();
      boolean beamOn = entity.isBeamOn();
      boolean beamTaper = entity.isBeamTaper();
      float beamLength = entity.getBeamLength();
      float beamWidth = entity.getBeamWidth();
      if (raid || decoration) {
         ++entity.ticker;
         float f = ((float)entity.ticker + partialTicks) / 8.0F;
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder bufferbuilder = tessellator.func_178180_c();
         Entity view = Minecraft.func_71410_x().func_175606_aa();
         if (view == null) {
            view = Minecraft.func_71410_x().field_71439_g;
         }

         float tx = (float)(((Entity)view).field_70169_q + (((Entity)view).field_70165_t - ((Entity)view).field_70169_q) * (double)partialTicks);
         float tz = (float)(((Entity)view).field_70166_s + (((Entity)view).field_70161_v - ((Entity)view).field_70166_s) * (double)partialTicks);
         float angle = (float)(Math.atan2(entity.field_70161_v - (double)tz, entity.field_70165_t - (double)tx) * 180.0 / Math.PI) + 90.0F;
         float dist = entity.func_70032_d((Entity)view) - 20.0F;
         float alpha = Math.min(1.0F, 1.0F - dist / 30.0F);
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(x, y, z);
         GlStateManager.func_179114_b(-angle + 45.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179117_G();
         RenderHelper.func_74518_a();
         GlStateManager.func_179090_x();
         GlStateManager.func_179103_j(7425);
         GlStateManager.func_179147_l();
         GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE);
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179141_d();
         GlStateManager.func_179089_o();
         if (raid || beamOn) {
            double rXa = beamWidth <= 0.0F ? -0.3 : (double)(-beamWidth);
            double rXb = beamWidth <= 0.0F ? 0.3 : (double)(-beamWidth);
            double rY = (double)beamLength;
            double rZa = beamWidth <= 0.0F ? -0.3 : (double)(-beamWidth);
            double rZb = beamWidth <= 0.0F ? 0.3 : (double)beamWidth;
            double[] rots = new double[]{Math.sin((double)f * 0.13) * 50.0, Math.sin((double)(-f) * 0.07) * 65.0, Math.cos((double)f * 0.1) * 55.0, Math.sin((double)(-f) * 0.14) * 50.0, Math.sin((double)f * 0.11) * 75.0, Math.cos((double)(-f) * 0.04) * 70.0};
            double[] sizes = new double[]{1.0, 0.75, 0.55, 0.4, 0.2, 0.1};
            Color color = entity.getColorRGBA();

            for(int i = 0; i < Math.min(5, raid ? ((RaidData)optional.get()).getStars() + 1 : beams); ++i) {
               double size = sizes[i];
               GlStateManager.func_179094_E();
               if (animate) {
                  size += Math.abs(rots[i]) * 0.002;
               } else {
                  GlStateManager.func_179114_b(45.0F, 0.0F, 1.0F, 0.0F);
               }

               GlStateManager.func_179137_b((double)i * -0.01, 0.0, 0.0);
               bufferbuilder.func_181668_a(beamTaper ? 4 : 7, DefaultVertexFormats.field_181706_f);
               bufferbuilder.func_181662_b(rXa * size, 0.05, rZa * size).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), 255).func_181675_d();
               bufferbuilder.func_181662_b(rXb * size, 0.05, rZb * size).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), 255).func_181675_d();
               if (beamTaper) {
                  bufferbuilder.func_181662_b((rXb + rXa) / 2.0 * size, rY * 2.0, (rZb + rZa) / 2.0 * size).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), 0).func_181675_d();
               } else {
                  bufferbuilder.func_181662_b(rXb * size, rY * 2.0, rZb * size).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), 255).func_181675_d();
                  bufferbuilder.func_181662_b(rXa * size, rY * 2.0, rZa * size).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), 255).func_181675_d();
               }

               tessellator.func_78381_a();
               GlStateManager.func_179121_F();
            }
         }

         GlStateManager.func_179141_d();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         RenderHelper.func_74519_b();
         if (alpha > 0.0F) {
            entity.getDisplay().ifPresent((display) -> {
               GlStateManager.func_179094_E();
               GlStateManager.func_179137_b(0.0, 1.5, 0.0);
               GlStateManager.func_179137_b(-1.25, 0.0, 1.25);
               GlStateManager.func_179139_a(1.5, 1.5, 1.5);
               GlStateManager.func_179114_b(f / 2.0F, 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179131_c(0.0F, 0.0F, 0.0F, alpha);

               try {
                  RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
                  Render entityClassRenderObject = renderManager.func_78715_a(EntityPixelmon.class);
                  RenderPixelmon rp = (RenderPixelmon)entityClassRenderObject;
                  rp.renderPixelmon(display, 0.0, 0.0, 0.0, partialTicks, true, 1, new float[]{0.0F, 0.0F, 0.0F, alpha});
                  renderManager.field_78735_i = 180.0F;
               } catch (Exception var7) {
                  var7.printStackTrace();
               }

               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179121_F();
            });
         }

         GlStateManager.func_179132_a(true);
         GlStateManager.func_179145_e();
         RenderHelper.func_74519_b();
         GlStateManager.func_179121_F();
         GlStateManager.func_179098_w();
      }

   }

   public void renderModel(GenericModelHolder model, ResourceLocation texture) {
      GlStateManager.func_179094_E();
      this.func_110776_a(texture);
      float scale = 0.6F;
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
      model.render();
      GlStateManager.func_179121_F();
   }
}

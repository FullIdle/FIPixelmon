package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPlateHolder;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.particles.SmallRising;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.enums.EnumPlate;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityPlateHolder extends TileEntityRenderer {
   private static final GenericModelHolder model = new GenericModelHolder("blocks/chalice/chalice.pqc");
   private static final ResourceLocation texture = new ResourceLocation("pixelmon", "textures/blocks/chalice.png");
   public int ticker = 0;

   public RenderTileEntityPlateHolder() {
      this.correctionAngles = 180;
   }

   public void renderTileEntity(TileEntityPlateHolder te, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(texture);
      float mRot = -90.0F;
      float mScale = 0.055F;
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(mScale, mScale, mScale);
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(mRot, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      model.render();
      GlStateManager.func_179121_F();
      ++this.ticker;
      double oy;
      if (te.itemThere) {
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder bufferbuilder = tessellator.func_178180_c();
         RenderHelper.func_74518_a();
         float f = ((float)this.ticker + partialTicks) / 200.0F;
         Random random = new Random(432L);
         GlStateManager.func_179090_x();
         GlStateManager.func_179103_j(7425);
         GlStateManager.func_179147_l();
         GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE);
         GlStateManager.func_179118_c();
         GlStateManager.func_179089_o();
         GlStateManager.func_179132_a(false);
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b(0.0F, -1.5F, 0.0F);
         oy = 5.0E-4;
         GlStateManager.func_179139_a(oy, oy, oy);

         for(int i = 0; i < 10; ++i) {
            GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179114_b(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
            float f2 = random.nextFloat() * 20.0F + 5.0F + 2000.0F;
            float f3 = random.nextFloat() * 2.0F + 1.0F + 400.0F;
            bufferbuilder.func_181668_a(6, DefaultVertexFormats.field_181706_f);
            bufferbuilder.func_181662_b(0.0, 0.0, 0.0).func_181669_b(255, 255, 255, 114).func_181675_d();
            bufferbuilder.func_181662_b(-0.866 * (double)f3, (double)f2, (double)(-0.5F * f3)).func_181669_b(255, 255, 0, 0).func_181675_d();
            bufferbuilder.func_181662_b(0.866 * (double)f3, (double)f2, (double)(-0.5F * f3)).func_181669_b(255, 255, 0, 0).func_181675_d();
            bufferbuilder.func_181662_b(0.0, (double)f2, (double)(1.0F * f3)).func_181669_b(255, 255, 0, 0).func_181675_d();
            bufferbuilder.func_181662_b(-0.866 * (double)f3, (double)f2, (double)(-0.5F * f3)).func_181669_b(255, 255, 0, 0).func_181675_d();
            tessellator.func_78381_a();
         }

         GlStateManager.func_179121_F();
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179129_p();
         GlStateManager.func_179084_k();
         GlStateManager.func_179103_j(7424);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179098_w();
         GlStateManager.func_179141_d();
         RenderHelper.func_74519_b();
      }

      if (te.animating) {
         EnumPlate plate = EnumPlate.values()[te.plateAdded];
         int i;
         if (te.animationTimer < 100) {
            for(i = 0; i < 4; ++i) {
               Minecraft.func_71410_x().field_71452_i.func_78873_a(new ParticleArcanery(this.func_178459_a(), (double)te.func_174877_v().func_177958_n() + 0.25 + this.func_178459_a().field_73012_v.nextDouble() / 2.0, (double)te.func_174877_v().func_177956_o() + this.func_178459_a().field_73012_v.nextDouble() + 0.65, (double)te.func_174877_v().func_177952_p() + 0.25 + this.func_178459_a().field_73012_v.nextDouble() / 2.0, 0.0, 0.0, 0.0, new SmallRising((float)plate.getRed() / 255.0F, (float)plate.getGreen() / 255.0F, (float)plate.getBlue() / 255.0F, 0.4F)));
            }
         } else if (te.animationTimer == 499) {
            for(i = 0; i < 400; ++i) {
               double ox = (double)te.func_174877_v().func_177958_n() + 0.5;
               oy = (double)(te.func_174877_v().func_177956_o() + 1);
               double oz = (double)te.func_174877_v().func_177952_p() + 0.5;
               double dx = (double)te.func_174877_v().func_177958_n() - 2.5 + this.func_178459_a().field_73012_v.nextDouble() * 6.0;
               double dy = (double)te.func_174877_v().func_177956_o() + this.func_178459_a().field_73012_v.nextDouble() * 6.0;
               double dz = (double)te.func_174877_v().func_177952_p() - 2.5 + this.func_178459_a().field_73012_v.nextDouble() * 6.0;
               double distSq = Math.abs(ox - dx) * Math.abs(ox - dx) + Math.abs(oy - dy) * Math.abs(oy - dy) + Math.abs(oz - dz) * Math.abs(oz - dz);
               if (distSq <= 9.0) {
                  Minecraft.func_71410_x().field_71452_i.func_78873_a(new ParticleArcanery(this.func_178459_a(), dx, dy, dz, 0.0, 0.0, 0.0, new SmallRising(1.0F, 1.0F, 0.75F, 1.0F)));
               }
            }
         }
      }

      float theta = 0.0F;
      if (te.platesIn.size() >= 16 && te.plateAdded != -1 && te.animationTimer > 150) {
         theta = ((float)te.animationTimer - 150.0F) / Math.max(10.0F, 350.0F - (float)te.animationTimer);
      }

      EnumPlate[] var33 = EnumPlate.values();
      int var34 = var33.length;

      for(int var35 = 0; var35 < var34; ++var35) {
         EnumPlate p = var33[var35];
         if (te.platesIn.contains(p.ordinal()) || te.animating && te.plateAdded == p.ordinal() && te.animationTimer > 80) {
            double radius = 2.0;
            if (te.animating && te.plateAdded == p.ordinal() && te.animationTimer > 80 && te.animationTimer < 100) {
               radius = (double)(te.animationTimer - 80) / 20.0 * 2.0;
            }

            if (te.platesIn.size() >= 16 && te.plateAdded != -1 && te.animationTimer > 150) {
               radius = 2.0 - (double)(((float)te.animationTimer - 150.0F) / 350.0F) * 2.0;
            }

            double ox = radius * Math.cos((double)theta);
            double oz = radius * Math.sin((double)theta);
            Minecraft.func_71410_x().field_71452_i.func_78873_a(new ParticleArcanery(this.func_178459_a(), (double)te.func_174877_v().func_177958_n() + 0.5 + ox, (double)te.func_174877_v().func_177956_o() + 1.2, (double)te.func_174877_v().func_177952_p() + 0.5 + oz, 0.0, 0.0, 0.0, new SmallRising((float)p.getRed() / 255.0F, (float)p.getGreen() / 255.0F, (float)p.getBlue() / 255.0F, 0.4F)));
         }

         theta = (float)((double)theta + 0.36959913571644626);
      }

   }
}

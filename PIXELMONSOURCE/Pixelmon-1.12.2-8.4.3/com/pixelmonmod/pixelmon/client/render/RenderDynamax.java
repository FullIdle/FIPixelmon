package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.client.models.IPixelmonModel;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.particles.Electric;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EntityBoundsData;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderDynamax {
   public static void dynamaxRender(RenderPixelmon rp, EntityPixelmon entity, double x, double y, double z, ResourceLocation tex, IPixelmonModel model, float partialTicks) {
      boolean calyrex = entity.getSpecies() == EnumSpecies.Calyrex;
      float scaleFactor = PixelmonConfig.scaleModelsUp ? 1.3F : 1.0F;
      scaleFactor *= entity.getScaleFactor() / 1.3F;
      if (entity.getSpecies() == EnumSpecies.Orbeetle) {
         scaleFactor /= 1.5F;
      }

      EntityBoundsData bounds = entity.getPokemonData().getBaseStats().getBoundsData();
      if (entity.transformed != null) {
         bounds = entity.transformed.getSpecies().getBaseStats().getBoundsData();
      }

      double targetMetric = Math.max(bounds.getHeight(), bounds.getWidth());
      float dynamaxScale = entity.getDynamaxScale();
      double targetHeight;
      if (dynamaxScale > 0.0F) {
         targetHeight = (double)PixelmonConfig.dynamaxSize;
         double currentHeight = targetMetric * (double)entity.getPixelmonScale();
         if (currentHeight < targetHeight) {
            double scaling = targetHeight / currentHeight;
            scaleFactor = (float)((double)scaleFactor * Math.max((double)dynamaxScale * scaling, (double)entity.getPixelmonScale()));
         } else {
            scaleFactor *= entity.getPixelmonScale();
         }
      } else {
         scaleFactor *= entity.getPixelmonScale();
      }

      GlStateManager.func_179094_E();
      targetHeight = y + targetMetric * (double)scaleFactor - 3.0;
      GlStateManager.func_179137_b(x, targetHeight, z);
      GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
      float scaleFinal = dynamaxScale * 1.5F;
      GlStateManager.func_179152_a(scaleFinal, scaleFinal, scaleFinal);
      float theta = (float)(Minecraft.func_71410_x().field_71441_e.func_82737_E() % 360L);
      GlStateManager.func_179114_b(theta, 0.0F, 0.0F, 1.0F);
      rp.func_110776_a(tex);
      if (calyrex) {
         GlStateManager.func_179131_c(0.0F, 0.35F, 1.0F, 1.0F);
      } else {
         GlStateManager.func_179131_c(1.0F, 0.0F, 0.0F, 1.0F);
      }

      model.renderAll(partialTicks);
      GlStateManager.func_179121_F();
      GlStateManager.func_179145_e();
      if (RandomHelper.rand.nextInt(35) == 0) {
         World w = Minecraft.func_71410_x().field_71441_e;
         ParticleArcanery parent = new ParticleArcanery(w, entity.field_70165_t + w.field_73012_v.nextDouble() * 1.5 - 0.75, entity.field_70163_u + targetMetric * (double)scaleFactor - 3.0, entity.field_70161_v + w.field_73012_v.nextDouble() * 1.5 - 0.75, 0.0, -1.0, 0.0, new Electric(20 + w.field_73012_v.nextInt(6), true, 90.0F, w.field_73012_v.nextFloat() * 360.0F, 0.4F, 0.2F, calyrex ? 0.0F : 1.0F, calyrex ? 0.1F : 0.0F, calyrex ? 1.0F : 0.0F));
         Minecraft.func_71410_x().field_71452_i.func_78873_a(parent);
      }

   }
}

package com.pixelmonmod.pixelmon.client.particle;

import com.pixelmonmod.pixelmon.client.render.blockReveal.BlockRevealRenderer;
import com.pixelmonmod.pixelmon.entities.EntityWormhole;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ParticleEvents {
   public static int arcaneryParticleCount = 0;

   @SubscribeEvent
   public void renderLast(RenderWorldLastEvent event) {
      Profiler profiler = Minecraft.func_71410_x().field_71424_I;
      profiler.func_76320_a("advanced_particles");
      Iterator var3 = Minecraft.func_71410_x().field_71441_e.field_72996_f.iterator();

      while(var3.hasNext()) {
         Entity e = (Entity)var3.next();
         if (e instanceof EntityWormhole) {
            EntityWormhole wh = (EntityWormhole)e;
            ParticleSystems.ULTRAWORMHOLE.getSystem().execute(Minecraft.func_71410_x(), Minecraft.func_71410_x().field_71441_e, e.field_70165_t, e.field_70163_u, e.field_70161_v, 0.0F, false, (double)wh.getColor(), (double)wh.field_70173_aa, (double)wh.getYaw(), (double)wh.getPitch());
         }
      }

      BlockRevealRenderer.doRender();
      Tessellator tessellator = Tessellator.func_178181_a();
      GL11.glPushAttrib(64);
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179147_l();
      GlStateManager.func_179094_E();
      GlStateManager.func_179140_f();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE);
      GlStateManager.func_179092_a(516, 0.003921569F);
      ParticleArcanery.dispatch(tessellator, event.getPartialTicks());
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.func_179084_k();
      GlStateManager.func_179092_a(516, 0.1F);
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179121_F();
      GL11.glPopAttrib();
      profiler.func_76319_b();
      GlStateManager.func_179098_w();
      GlStateManager.func_179145_e();
      GlStateManager.func_179126_j();
      GlStateManager.func_179141_d();
   }

   public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, double zLevel) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      bufferbuilder.func_181662_b((double)(x + 0), (double)(y + height), zLevel).func_187315_a((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).func_181675_d();
      bufferbuilder.func_181662_b((double)(x + width), (double)(y + height), zLevel).func_187315_a((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).func_181675_d();
      bufferbuilder.func_181662_b((double)(x + width), (double)(y + 0), zLevel).func_187315_a((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).func_181675_d();
      bufferbuilder.func_181662_b((double)(x + 0), (double)(y + 0), zLevel).func_187315_a((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).func_181675_d();
      tessellator.func_78381_a();
   }
}

package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockBell;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBell;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityBell extends TileEntityRenderer {
   private static final GenericModelHolder tidal_model = new GenericModelHolder("blocks/bells/tidal_bell.pqc");
   private static final ResourceLocation tidal_texture = new ResourceLocation("pixelmon", "textures/blocks/tidal_bell.png");
   private static final GenericModelHolder clear_model = new GenericModelHolder("blocks/bells/clear_bell.pqc");
   private static final ResourceLocation clear_texture = new ResourceLocation("pixelmon", "textures/blocks/clear_bell.png");
   private int ticker = 0;

   public void renderTileEntity(TileEntityBell block, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      if (state.func_177230_c().equals(PixelmonBlocks.clearBell) || state.func_177230_c().equals(PixelmonBlocks.tidalBell)) {
         switch (((BlockBell)state.func_177230_c()).getBell()) {
            case Clear:
               this.renderModel(clear_model, clear_texture);
               break;
            case Tidal:
               this.renderModel(tidal_model, tidal_texture);
         }

         if (block.spawning) {
            ++this.ticker;
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
            GlStateManager.func_179109_b(0.0F, -0.65F, 0.0F);
            int count = 15;
            double scale = 3.0E-4;
            if (block.spawning && this.func_178459_a().func_72820_D() > 12000L && this.func_178459_a().func_72820_D() < 14000L) {
               long factor = 13000L - this.func_178459_a().func_72820_D();
               double size = (double)factor / 1000.0;
               scale = Math.max((1.0 - size) / 100.0, 3.0E-4);
            }

            GlStateManager.func_179139_a(scale, scale, scale);

            for(int i = 0; i < count; ++i) {
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
      }

   }

   public void renderModel(GenericModelHolder model, ResourceLocation texture) {
      GlStateManager.func_179094_E();
      this.func_147499_a(texture);
      float rotate = -90.0F;
      float scale = 0.3F;
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179137_b(0.0, -1.3, 0.0);
      GlStateManager.func_179114_b(rotate, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
      model.render();
      GlStateManager.func_179121_F();
   }
}

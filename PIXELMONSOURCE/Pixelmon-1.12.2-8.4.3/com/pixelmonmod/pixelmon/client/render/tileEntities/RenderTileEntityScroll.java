package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityScroll;
import com.pixelmonmod.pixelmon.client.models.blocks.ModelScroll;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RenderTileEntityScroll extends TileEntitySpecialRenderer {
   private final ModelScroll model = new ModelScroll();

   public void render(TileEntityScroll te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      boolean flag = te.func_145831_w() != null;
      boolean flag1 = !flag || te.func_145838_q() == PixelmonBlocks.standingScroll;
      int i = flag ? te.func_145832_p() : 0;
      long j = flag ? te.func_145831_w().func_82737_E() : 0L;
      GlStateManager.func_179094_E();
      float f = 0.6666667F;
      float f2;
      if (flag1) {
         GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
         f2 = (float)(i * 360) / 16.0F;
         GlStateManager.func_179114_b(-f2, 0.0F, 1.0F, 0.0F);
         this.model.scrollStand.field_78806_j = true;
      } else {
         f2 = 0.0F;
         if (i == 2) {
            f2 = 180.0F;
         }

         if (i == 4) {
            f2 = 90.0F;
         }

         if (i == 5) {
            f2 = -90.0F;
         }

         GlStateManager.func_179109_b((float)x + 0.5F, (float)y - 0.16666667F, (float)z + 0.5F);
         GlStateManager.func_179114_b(-f2, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179109_b(0.0F, -1.2125F, -0.4375F);
         this.model.scrollStand.field_78806_j = false;
      }

      BlockPos blockpos = te.func_174877_v();
      float f3 = (float)(blockpos.func_177958_n() * 7 + blockpos.func_177956_o() * 9 + blockpos.func_177952_p() * 13) + (float)j + partialTicks;
      this.model.scrollSlate.field_78795_f = (-0.0125F + 0.01F * MathHelper.func_76134_b(f3 * 3.1415927F * 0.02F)) * 3.1415927F;
      GlStateManager.func_179091_B();
      ResourceLocation resourcelocation = this.getBannerResourceLocation(te);
      if (resourcelocation != null) {
         this.func_147499_a(resourcelocation);
         GlStateManager.func_179094_E();
         GlStateManager.func_179152_a(0.6666667F, -0.6666667F, -0.6666667F);
         this.model.renderScroll();
         GlStateManager.func_179121_F();
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, alpha);
      GlStateManager.func_179121_F();
   }

   private ResourceLocation getBannerResourceLocation(TileEntityScroll scroll) {
      return scroll.getResourceLocation();
   }
}

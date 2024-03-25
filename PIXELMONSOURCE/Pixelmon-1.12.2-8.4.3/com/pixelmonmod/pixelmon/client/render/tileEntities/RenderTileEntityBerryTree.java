package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBerryTree;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class RenderTileEntityBerryTree extends TileEntitySpecialRenderer {
   public void render(TileEntityBerryTree te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      try {
         EnumBerry berry = te.getType();
         if (berry.models[te.getStage() - 1].getModel() != null) {
            IBlockState state = te.func_145831_w().func_180495_p(te.func_174877_v());
            EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
            short rotateDegrees;
            if (facing == EnumFacing.EAST) {
               rotateDegrees = 0;
            } else if (facing == EnumFacing.NORTH) {
               rotateDegrees = 90;
            } else if (facing == EnumFacing.WEST) {
               rotateDegrees = 180;
            } else if (facing == EnumFacing.SOUTH) {
               rotateDegrees = 270;
            } else {
               rotateDegrees = 0;
            }

            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
            GlStateManager.func_179109_b(0.0F, -0.5F, 0.0F);
            GlStateManager.func_179152_a(1.0F, 1.0F, 1.0F);
            Minecraft.func_71410_x().field_71446_o.func_110577_a(te.getTexture());
            GlStateManager.func_179114_b((float)rotateDegrees, 0.0F, 1.0F, 0.0F);
            float scale = berry.scale;
            if (te.getStage() < 3) {
               scale = 0.5F;
            }

            GlStateManager.func_179152_a(scale, scale, scale);
            GlStateManager.func_179133_A();
            GlStateManager.func_179103_j(7425);
            ((WavefrontObject)berry.models[te.getStage() - 1].getModel()).renderAll(0.0F);
            GlStateManager.func_179121_F();
         }
      } catch (Exception var16) {
      }

   }
}

package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityRenderer extends TileEntitySpecialRenderer {
   public static boolean profileTileEntityRender = false;
   boolean blend = false;
   boolean disableCulling = false;
   boolean disableLighting = false;
   boolean flip = true;
   float scale = 0.0F;
   float yOffset = 0.0F;
   int correctionAngles = 0;

   public final void func_192841_a(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      if (profileTileEntityRender) {
         Minecraft.func_71410_x().field_71424_I.func_76320_a(te.getClass().getSimpleName());
      }

      IBlockState state = this.func_178459_a().func_180495_p(te.func_174877_v());
      int rotateDegrees = this.getRotation(state);
      GlStateManager.func_179091_B();
      GlStateManager.func_179094_E();
      if (this.blend) {
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179132_a(false);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (this.disableCulling) {
         GlStateManager.func_179129_p();
      }

      if (this.disableLighting) {
         GlStateManager.func_179140_f();
      }

      GlStateManager.func_179109_b((float)x + 0.5F, (float)y + this.yOffset, (float)z + 0.5F);
      GlStateManager.func_179114_b((float)(rotateDegrees + this.correctionAngles), 0.0F, 1.0F, 0.0F);
      if (this.flip) {
         GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      }

      if (this.scale != 0.0F) {
         GlStateManager.func_179152_a(this.scale, this.scale, this.scale);
      }

      this.renderTileEntity(te, state, x, y, z, partialTicks, destroyStage);
      if (this.blend) {
         GlStateManager.func_179084_k();
         GlStateManager.func_179132_a(true);
      }

      if (this.disableCulling) {
         GlStateManager.func_179089_o();
      }

      if (this.disableLighting) {
         GlStateManager.func_179145_e();
      }

      GlStateManager.func_179101_C();
      GlStateManager.func_179121_F();
      if (profileTileEntityRender) {
         Minecraft.func_71410_x().field_71424_I.func_76319_b();
      }

   }

   public abstract void renderTileEntity(TileEntity var1, IBlockState var2, double var3, double var5, double var7, float var9, int var10);

   protected int getRotation(IBlockState state) {
      Block block = state.func_177230_c();
      if (block instanceof MultiBlock || block instanceof GenericRotatableModelBlock) {
         EnumFacing facing = block instanceof MultiBlock ? (EnumFacing)state.func_177229_b(BlockProperties.FACING) : (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         if (facing == EnumFacing.EAST) {
            return 270;
         }

         if (facing == EnumFacing.NORTH) {
            return 0;
         }

         if (facing == EnumFacing.WEST) {
            return 90;
         }

         if (facing == EnumFacing.SOUTH) {
            return 180;
         }
      }

      return 0;
   }

   public boolean hasProperty(IBlockState state, IProperty property) {
      return state.func_177227_a().contains(property);
   }
}

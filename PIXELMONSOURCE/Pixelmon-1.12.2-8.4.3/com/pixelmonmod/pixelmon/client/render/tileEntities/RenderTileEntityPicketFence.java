package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockPicketFence;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class RenderTileEntityPicketFence extends GenericRenderer {
   public final GenericSmdModel connectorModel = new GenericSmdModel("models/blocks/picket_fence", "connector.pqc", true);

   public RenderTileEntityPicketFence() {
      super("picketFenceTexture.png", new GenericSmdModel("models/blocks/picket_fence", "fence_post.pqc", true), 0);
      super.disableCulling = true;
   }

   public void func_192841_a(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      IBlockState state = this.func_178459_a().func_180495_p(te.func_174877_v());
      if (state.func_177230_c() instanceof BlockPicketFence) {
         BlockPicketFence block = (BlockPicketFence)state.func_177230_c();
         this.func_147499_a(this.texture);
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

         GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 0.0F, (float)z + 0.5F);
         GlStateManager.func_179114_b((float)(this.rotateDegrees + this.correctionAngles), 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
         this.model.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
         if (block.canConnectTo(this.func_178459_a(), te.func_174877_v().func_177978_c(), EnumFacing.SOUTH)) {
            GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
            this.connectorModel.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179114_b(90.0F, 0.0F, 1.0F, 0.0F);
         }

         if (block.canConnectTo(this.func_178459_a(), te.func_174877_v().func_177968_d(), EnumFacing.NORTH)) {
            GlStateManager.func_179114_b(90.0F, 0.0F, 1.0F, 0.0F);
            this.connectorModel.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
         }

         if (block.canConnectTo(this.func_178459_a(), te.func_174877_v().func_177974_f(), EnumFacing.WEST)) {
            this.connectorModel.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
         }

         if (block.canConnectTo(this.func_178459_a(), te.func_174877_v().func_177976_e(), EnumFacing.EAST)) {
            GlStateManager.func_179114_b(-180.0F, 0.0F, 1.0F, 0.0F);
            this.connectorModel.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
         }

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
      }
   }
}

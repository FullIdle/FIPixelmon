package com.pixelmonmod.tcg.block.renderers;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.models.smd.GabeNewellException;
import com.pixelmonmod.tcg.helper.GuiHelper;
import com.pixelmonmod.tcg.tileentity.TileEntityPrinter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityPrinter extends TileEntitySpecialRenderer {
   private ResourceLocation texture = new ResourceLocation("tcg:textures/blocks/printer/purple_printer.png");
   public ModelBase model = new ModelPrinter();
   public HashMap childModels = Maps.newHashMap();
   protected int rotateDegrees = 0;
   public boolean blend = false;
   public boolean disableCulling = true;
   public boolean disableLighting = false;

   public RenderTileEntityPrinter() throws GabeNewellException {
   }

   public void render(TileEntityPrinter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      IBlockState state = Minecraft.func_71410_x().field_71441_e.func_180495_p(te.func_174877_v());
      Block block = state.func_177230_c();
      if (block instanceof GenericRotatableModelBlock) {
         EnumFacing facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         this.rotateDegrees = GuiHelper.getRotationFromFacing(facing);
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
         GlStateManager.func_179114_b((float)this.rotateDegrees, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
         this.model.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
         Iterator facing1 = this.childModels.entrySet().iterator();

         while(facing1.hasNext()) {
            Map.Entry childEntry = (Map.Entry)facing1.next();
            this.func_147499_a((ResourceLocation)childEntry.getValue());
            ((GenericSmdModel)childEntry.getKey()).func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
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

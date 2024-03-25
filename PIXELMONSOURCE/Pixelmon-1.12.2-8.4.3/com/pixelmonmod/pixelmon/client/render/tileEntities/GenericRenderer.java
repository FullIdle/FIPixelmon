package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class GenericRenderer extends TileEntitySpecialRenderer {
   protected ResourceLocation texture;
   public ModelBase model;
   public HashMap childModels = Maps.newHashMap();
   protected int correctionAngles = 0;
   protected int rotateDegrees = 0;
   protected float scale = 0.0F;
   protected float yOffset = 0.0F;
   public boolean blend = false;
   public boolean disableCulling = false;
   public boolean disableLighting = false;

   public GenericRenderer(String textureFileName, ModelBase model) {
      this.texture = new ResourceLocation("pixelmon:textures/blocks/" + textureFileName);
      this.model = model;
   }

   public GenericRenderer(String textureFileName, ModelBase model, int correctionAngles) {
      this.texture = new ResourceLocation("pixelmon:textures/blocks/" + textureFileName);
      this.model = model;
      this.correctionAngles = correctionAngles;
   }

   public void setTexture(ResourceLocation texture) {
      this.texture = texture;
   }

   public void func_192841_a(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
      IBlockState state = this.func_178459_a().func_180495_p(te.func_174877_v());
      Block block = state.func_177230_c();
      if (!(block instanceof MultiBlock) && !(block instanceof GenericRotatableModelBlock)) {
         this.rotateDegrees = 0;
      } else {
         EnumFacing facing;
         if (block instanceof MultiBlock) {
            facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         } else {
            facing = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
         }

         if (facing == EnumFacing.EAST) {
            this.rotateDegrees = 270;
         } else if (facing == EnumFacing.NORTH) {
            this.rotateDegrees = 0;
         } else if (facing == EnumFacing.WEST) {
            this.rotateDegrees = 90;
         } else if (facing == EnumFacing.SOUTH) {
            this.rotateDegrees = 180;
         } else {
            this.rotateDegrees = 0;
         }
      }

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

      GlStateManager.func_179109_b((float)x + 0.5F, (float)y + this.yOffset, (float)z + 0.5F);
      GlStateManager.func_179114_b((float)(this.rotateDegrees + this.correctionAngles), 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      if (this.scale != 0.0F) {
         GlStateManager.func_179152_a(this.scale, this.scale, this.scale);
      }

      this.model.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
      Iterator var15 = this.childModels.entrySet().iterator();

      while(var15.hasNext()) {
         Map.Entry childEntry = (Map.Entry)var15.next();
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

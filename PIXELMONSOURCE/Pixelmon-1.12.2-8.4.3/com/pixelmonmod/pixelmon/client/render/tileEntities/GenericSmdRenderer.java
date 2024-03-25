package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.pixelmonmod.pixelmon.blocks.tileEntities.IFrameCounter;
import com.pixelmonmod.pixelmon.blocks.tileEntities.ISpecialTexture;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import javax.vecmath.Vector3f;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GenericSmdRenderer extends TileEntityRenderer {
   private GenericModelHolder holder;
   ResourceLocation texture;
   private float angle;
   private Vector3f axis;

   public GenericSmdRenderer(GenericModelHolder modelHolder, ResourceLocation texture) {
      this.angle = 0.0F;
      this.axis = new Vector3f(1.0F, 0.0F, 0.0F);
      this.holder = modelHolder;
      this.texture = texture;
   }

   public GenericSmdRenderer(ResourceLocation model, ResourceLocation texture) {
      this(new GenericModelHolder(model), texture);
   }

   public GenericSmdRenderer(String pqcPath, String texture) {
      this(new ResourceLocation("pixelmon", "models/" + pqcPath), texture == null ? null : new ResourceLocation("pixelmon:textures/blocks/" + texture));
   }

   public void renderTileEntity(TileEntity te, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      if (te instanceof ISpecialTexture) {
         this.func_147499_a(((ISpecialTexture)te).getTexture());
      } else {
         this.func_147499_a(this.texture);
      }

      GenericSmdModel model = (GenericSmdModel)this.holder.getModel();
      if (te instanceof IFrameCounter) {
         model.setFrame(((IFrameCounter)te).getFrame());
      }

      GlStateManager.func_179094_E();
      GlStateManager.func_179114_b(this.angle, this.axis.x, this.axis.y, this.axis.z);
      GlStateManager.func_179133_A();
      GlStateManager.func_179103_j(7425);
      GlStateManager.func_179129_p();
      model.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
      GlStateManager.func_179121_F();
   }

   public GenericSmdRenderer enableBlend() {
      this.blend = true;
      return this;
   }

   public GenericSmdRenderer disableCulling() {
      this.disableCulling = true;
      return this;
   }

   public GenericSmdRenderer disableLighting() {
      this.disableLighting = true;
      return this;
   }

   public GenericSmdRenderer setCorrectionAngles(int correctionAngles) {
      this.correctionAngles = correctionAngles;
      return this;
   }

   public GenericSmdRenderer setYOffset(float yOffset) {
      this.yOffset = yOffset;
      return this;
   }

   public GenericSmdRenderer rotate(float d, float axisX, float axisY, float axisZ) {
      this.angle = d;
      this.axis = new Vector3f(axisX, axisY, axisZ);
      return this;
   }

   public GenericSmdRenderer scale(float scale) {
      this.scale = scale;
      return this;
   }
}

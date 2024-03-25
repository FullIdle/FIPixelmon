package com.pixelmonmod.pixelmon.client.render.custom;

import com.pixelmonmod.pixelmon.entities.custom.EntityPixelmonPainting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPixelmonPainting extends Render {
   private final Minecraft mc = Minecraft.func_71410_x();
   private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
   private RenderItem itemRenderer = Minecraft.func_71410_x().func_175599_af();

   public RenderPixelmonPainting(RenderManager renderManagerIn) {
      super(renderManagerIn);
   }

   public boolean shouldRender(EntityPixelmonPainting entity, ICamera camera, double camX, double camY, double camZ) {
      return true;
   }

   public void doRender(EntityPixelmonPainting entity, double x, double y, double z, float entityYaw, float partialTicks) {
      BlockRendererDispatcher blockrendererdispatcher = this.mc.func_175602_ab();
      ModelManager modelmanager = blockrendererdispatcher.func_175023_a().func_178126_b();
      IBakedModel model = modelmanager.func_174953_a(this.itemFrameModel);
      PerspectiveMapWrapper iPerspectiveAwareModel = null;
      if (model instanceof PerspectiveMapWrapper) {
         iPerspectiveAwareModel = (PerspectiveMapWrapper)model;
      }

      GlStateManager.func_179094_E();
      BlockPos blockpos = entity.func_174857_n();
      double d3 = (double)blockpos.func_177958_n() - entity.field_70165_t + x;
      double d4 = (double)blockpos.func_177956_o() - entity.field_70163_u + y;
      double d5 = (double)blockpos.func_177952_p() - entity.field_70161_v + z;
      GlStateManager.func_179137_b(d3 + 0.5, d4 + 0.5, d5 + 0.5);
      GlStateManager.func_179114_b(180.0F - entity.field_70177_z, 0.0F, 1.0F, 0.0F);
      this.field_76990_c.field_78724_e.func_110577_a(TextureMap.field_110575_b);
      GlStateManager.func_179094_E();
      GlStateManager.func_179139_a(2.67, 2.67, 2.67);
      GlStateManager.func_179137_b(-0.69, -0.69, -0.815);
      if (iPerspectiveAwareModel == null) {
         blockrendererdispatcher.func_175019_b().func_178262_a(model, 1.0F, 1.0F, 1.0F, 1.0F);
      } else {
         blockrendererdispatcher.func_175019_b().func_178262_a(iPerspectiveAwareModel, 1.0F, 1.0F, 1.0F, 1.0F);
      }

      GlStateManager.func_179121_F();
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.4375F);
      this.renderItem(entity);
      GlStateManager.func_179121_F();
   }

   private void renderItem(EntityPixelmonPainting itemFrame) {
      ItemStack itemstack = itemFrame.getDisplayedItem();
      if (itemstack != null) {
         EntityItem entityitem = new EntityItem(itemFrame.field_70170_p, 0.0, 0.0, 0.0, itemstack);
         entityitem.func_92059_d().func_190920_e(1);
         entityitem.field_70290_d = 0.0F;
         GlStateManager.func_179094_E();
         GlStateManager.func_179140_f();
         GlStateManager.func_179139_a(1.6, 1.6, 1.6);
         GlStateManager.func_179137_b(-0.315, -0.315, 0.0);
         if (!this.itemRenderer.func_175050_a(entityitem.func_92059_d())) {
            GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
         }

         GlStateManager.func_179123_a();
         RenderHelper.func_74519_b();
         this.itemRenderer.func_181564_a(entityitem.func_92059_d(), TransformType.FIXED);
         RenderHelper.func_74518_a();
         GlStateManager.func_179099_b();
         GlStateManager.func_179145_e();
         GlStateManager.func_179121_F();
      }

   }

   protected ResourceLocation getEntityTexture(EntityPixelmonPainting entity) {
      return null;
   }
}

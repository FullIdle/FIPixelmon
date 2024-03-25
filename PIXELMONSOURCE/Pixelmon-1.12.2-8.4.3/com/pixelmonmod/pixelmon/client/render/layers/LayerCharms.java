package com.pixelmonmod.pixelmon.client.render.layers;

import com.pixelmonmod.pixelmon.client.models.IPixelmonModel;
import com.pixelmonmod.pixelmon.enums.EnumCustomModel;
import com.pixelmonmod.pixelmon.enums.items.EnumCharms;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class LayerCharms implements LayerRenderer {
   private final ResourceLocation shinyCharmTexture = new ResourceLocation("pixelmon:textures/playeritems/shiny_charm.png");
   private final ResourceLocation ovalCharmTexture = new ResourceLocation("pixelmon:textures/playeritems/oval_charm.png");
   private final ResourceLocation expCharmTexture = new ResourceLocation("pixelmon:textures/playeritems/exp_charm.png");
   private final ResourceLocation catchingCharmTexture = new ResourceLocation("pixelmon:textures/playeritems/catching_charm.png");
   private final ResourceLocation markCharmTexture = new ResourceLocation("pixelmon:textures/playeritems/mark_charm.png");
   private final IPixelmonModel shinyCharmModel;
   private final IPixelmonModel ovalCharmModel;
   private final IPixelmonModel expCharmModel;
   private final IPixelmonModel catchingCharmModel;
   private final IPixelmonModel markCharmModel;
   private final RenderPlayer renderer;
   private static final float[][] yz = new float[][]{{0.7F, 0.04F}, {0.7F, -0.04F}, {0.8F, 0.08F}, {0.8F, 0.0F}, {0.8F, -0.08F}};
   private static final float[][] syz = new float[][]{{0.8F, 0.38F}, {0.85F, 0.3F}, {0.85F, 0.42F}, {0.9F, 0.34F}, {0.95F, 0.26F}};

   public LayerCharms(RenderPlayer renderer) {
      this.shinyCharmModel = (IPixelmonModel)EnumCustomModel.ShinyCharm.getModel();
      this.ovalCharmModel = (IPixelmonModel)EnumCustomModel.OvalCharm.getModel();
      this.expCharmModel = (IPixelmonModel)EnumCustomModel.ExpCharm.getModel();
      this.catchingCharmModel = (IPixelmonModel)EnumCustomModel.CatchingCharm.getModel();
      this.markCharmModel = (IPixelmonModel)EnumCustomModel.MarkCharm.getModel();
      this.renderer = renderer;
   }

   public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      Minecraft.func_71410_x().field_71424_I.func_76320_a("layer_charm_wear");
      int i = 0;
      EnumCharms[] var10 = EnumCharms.values();
      int var11 = var10.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         EnumCharms charm = var10[var12];
         boolean render = false;
         ResourceLocation texture = null;
         IPixelmonModel model = null;
         switch (charm) {
            case Shiny:
               if (EntityPlayerExtension.getPlayerShinyCharm(player).isActive()) {
                  render = true;
                  texture = this.shinyCharmTexture;
                  model = this.shinyCharmModel;
               }
               break;
            case Oval:
               if (EntityPlayerExtension.getPlayerOvalCharm(player).isActive()) {
                  render = true;
                  texture = this.ovalCharmTexture;
                  model = this.ovalCharmModel;
               }
               break;
            case Exp:
               if (EntityPlayerExtension.getPlayerExpCharm(player).isActive()) {
                  render = true;
                  texture = this.expCharmTexture;
                  model = this.expCharmModel;
               }
               break;
            case Catching:
               if (EntityPlayerExtension.getPlayerCatchingCharm(player).isActive()) {
                  render = true;
                  texture = this.catchingCharmTexture;
                  model = this.catchingCharmModel;
               }
               break;
            case Mark:
               if (EntityPlayerExtension.getPlayerMarkCharm(player).isActive()) {
                  render = true;
                  texture = this.markCharmTexture;
                  model = this.markCharmModel;
               }
         }

         if (render) {
            GlStateManager.func_179094_E();
            if (player.func_70093_af()) {
               GlStateManager.func_179109_b(-0.2525F, syz[i][0], syz[i][1]);
            } else {
               GlStateManager.func_179109_b(-0.2525F, yz[i][0], yz[i][1]);
            }

            scale = 3.5E-4F;
            GlStateManager.func_179152_a(scale, scale, scale);
            GlStateManager.func_179114_b(90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179109_b(-0.3F, 0.0F, 0.875F);
            this.renderer.func_110776_a(texture);
            GlStateManager.func_179089_o();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            model.renderAll(partialTicks);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.func_179121_F();
         }

         ++i;
      }

      Minecraft.func_71410_x().field_71424_I.func_76319_b();
   }

   public boolean func_177142_b() {
      return true;
   }
}

package com.pixelmonmod.pixelmon.client.render.player;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class PixelRenderPlayer extends RenderPlayer {
   public final boolean isAlex;

   public PixelRenderPlayer(RenderManager renderManager) {
      super(renderManager);
      this.isAlex = false;
   }

   public PixelRenderPlayer(RenderManager renderManager, boolean useSmallArms) {
      super(renderManager, useSmallArms);
      this.isAlex = useSmallArms;
   }

   public void func_76986_a(AbstractClientPlayer player, double x, double y, double z, float entityYaw, float partialTicks) {
      boolean cameraIsPlayer = Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer;
      if (!cameraIsPlayer || !player.func_175144_cb() || this.field_76990_c.field_78734_h == player) {
         double modY = y;
         if (player.func_70093_af() && !(player instanceof EntityPlayerSP)) {
            modY = y - 0.125;
         }

         Entity livingPlayer = this.field_76990_c.field_78734_h;
         if (!cameraIsPlayer) {
            this.setBattleModelVisibilities(player);
            this.field_76990_c.field_78734_h = player;
         } else {
            super.func_177137_d(player);
         }

         super.func_76986_a(player, x, modY, z, entityYaw, partialTicks);
         if (!cameraIsPlayer) {
            this.field_76990_c.field_78734_h = livingPlayer;
         }
      }

   }

   protected void renderModel(AbstractClientPlayer player, float p2, float p3, float p4, float p5, float p6, float p7) {
      super.func_77036_a(player, p2, p3, p4, p5, p6, p7);
   }

   public boolean func_177094_a(LayerRenderer layer) {
      if (layer instanceof LayerEntityOnShoulder) {
      }

      return super.func_177094_a(layer);
   }

   public void setBattleModelVisibilities(AbstractClientPlayer clientPlayer) {
      ModelPlayer modelplayer = this.func_177087_b();
      if (clientPlayer.func_175149_v()) {
         modelplayer.func_178719_a(false);
         modelplayer.field_78116_c.field_78806_j = true;
         modelplayer.field_178720_f.field_78806_j = true;
      } else {
         modelplayer.func_178719_a(false);
         modelplayer.field_178720_f.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.HAT);
         modelplayer.field_178730_v.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.JACKET);
         modelplayer.field_178733_c.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.LEFT_PANTS_LEG);
         modelplayer.field_178731_d.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.RIGHT_PANTS_LEG);
         modelplayer.field_178734_a.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.LEFT_SLEEVE);
         modelplayer.field_178732_b.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.RIGHT_SLEEVE);
         modelplayer.field_78117_n = clientPlayer.func_70093_af();
      }

   }

   public void setModel(ModelPlayer model) {
      this.field_77045_g = model;
   }

   protected void func_77043_a(AbstractClientPlayer entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
      super.func_77043_a(entityLiving, p_77043_2_, rotationYaw, partialTicks);
      if (entityLiving.func_184187_bx() != null) {
         Entity entity = entityLiving.func_184187_bx();
         if (entity instanceof EntityPixelmon) {
            EntityPixelmon pixelmon = (EntityPixelmon)entity;
            if (!pixelmon.getStaysHorizontalInRender() && pixelmon.getIsFlying()) {
               GlStateManager.func_179114_b(pixelmon.field_70125_A, 1.0F, 0.0F, 0.0F);
            }
         }
      }

   }
}

package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.client.render.layers.npc.LayerMegaItemsNPC;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import javax.vecmath.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderNPC extends RenderLiving {
   private int defaultNameRenderDistance = 8;
   private int defaultBossNameRenderDistanceExtension = 8;
   private int configNameRenderMultiplier;
   private int nameRenderDistanceNormal;
   private int nameRenderDistanceBoss;

   public RenderNPC(RenderManager manager) {
      super(manager, new ModelPlayer(0.0F, false), 0.5F);
      this.configNameRenderMultiplier = Math.max(1, Math.min(PixelmonConfig.nameplateRangeModifier, 3));
      this.nameRenderDistanceNormal = this.defaultNameRenderDistance * this.configNameRenderMultiplier;
      this.nameRenderDistanceBoss = this.nameRenderDistanceNormal + this.defaultBossNameRenderDistanceExtension;
      this.func_177094_a(new LayerHeldItem(this));
      this.func_177094_a(new LayerMegaItemsNPC(this));
   }

   public void doRender(EntityNPC npc, double d, double d1, double d2, float f, float f1) {
      if (this.field_77045_g != null) {
         float var10 = npc.func_70032_d(this.field_76990_c.field_78734_h);
         float renderdistance = npc.getBossMode() != EnumBossMode.NotBoss ? (float)this.nameRenderDistanceBoss : (float)this.nameRenderDistanceNormal;
         if (var10 <= renderdistance) {
            this.drawNameTag(npc, d, d1, d2);
         }

         super.func_76986_a(npc, d, d1, d2, f, f1);
      }
   }

   public void drawNameTag(EntityLiving entityliving, double x, double y, double z) {
      if (Minecraft.func_71382_s()) {
         try {
            EntityNPC npc = (EntityNPC)entityliving;
            String displayText = npc.getDisplayText();
            String subtitleText = npc.getSubTitleText();
            if (!displayText.equals("")) {
               this.renderLivingLabel(npc, displayText, subtitleText, x, y, z);
            }
         } catch (Exception var11) {
         }
      }

   }

   protected void renderLivingLabel(EntityNPC npc, String displayText, String subtitleText, double x, double y, double z) {
      FontRenderer fontRenderer = this.func_76983_a();
      float var13 = 1.6F;
      float var14 = 0.016666668F * var13;
      GlStateManager.func_179094_E();
      if (npc.getScale().y < 1.0F) {
         GlStateManager.func_179109_b((float)x, (float)y + 1.1F + 1.0F, (float)z);
      } else {
         GlStateManager.func_179109_b((float)x, (float)y + 1.1F + 1.4F, (float)z);
      }

      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179152_a(-var14, -var14, var14);
      GlStateManager.func_179140_f();
      GlStateManager.func_179126_j();
      GlStateManager.func_179118_c();
      GlStateManager.func_179098_w();
      if (npc instanceof NPCTrainer && npc.getBossMode() != EnumBossMode.NotBoss) {
         EnumBossMode enumBossMode = npc.getBossMode();
         String bossTag = I18n.func_135052_a("gui.boss.text", new Object[0]);
         String bossMode = enumBossMode.getBossText();
         int bossTagWidth = fontRenderer.func_78256_a(bossMode);
         int bossTagpos = bossTagWidth / 2 * -1;
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(-2.5 + (double)bossTagpos, -4.5, 0.0);
         GlStateManager.func_179139_a(0.5, 0.5, 0.5);
         fontRenderer.func_78276_b(bossTag, 0, 0, 553648127);
         GlStateManager.func_179121_F();
         fontRenderer.func_78276_b(bossMode, bossTagpos, 0, enumBossMode.getColourInt());
      } else {
         if (subtitleText != null) {
            int displayTextWidth = fontRenderer.func_78256_a(displayText);
            int displayTextPos = displayTextWidth / 2 * -1;
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b(-2.5 + (double)displayTextPos, -4.5, 0.0);
            GlStateManager.func_179139_a(0.5, 0.5, 0.5);
            fontRenderer.func_78276_b(subtitleText, 0, 0, 553648127);
            GlStateManager.func_179121_F();
            fontRenderer.func_78276_b(displayText, displayTextPos, 0, 553648127);
         }

         fontRenderer.func_78276_b(displayText, -fontRenderer.func_78256_a(displayText) / 2, 0, 553648127);
         GlStateManager.func_179132_a(true);
      }

      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179121_F();
   }

   protected boolean bindEntityTexture(EntityNPC entity) {
      return !entity.bindTexture() ? super.func_180548_c(entity) : true;
   }

   protected ResourceLocation getEntityTexture(EntityNPC entity) {
      return new ResourceLocation(entity.getTexture());
   }

   protected void preRenderCallback(EntityNPC npc, float partialTickTime) {
      Vector3f scale = npc.getScale();
      GlStateManager.func_179152_a(scale.x, scale.y, scale.z);
      super.func_77041_b(npc, partialTickTime);
   }
}

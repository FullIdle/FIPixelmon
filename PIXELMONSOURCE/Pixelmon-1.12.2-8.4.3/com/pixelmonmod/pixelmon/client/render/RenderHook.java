package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.entities.projectiles.EntityHook;
import com.pixelmonmod.pixelmon.items.ItemFishingRod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderHook extends RenderFish {
   private static final ResourceLocation field_110792_a = new ResourceLocation("textures/particle/particles.png");

   public RenderHook(RenderManager renderManager) {
      super(renderManager);
   }

   public void doRenderHook(EntityHook hook, double x, double y, double z, float p_180558_8_, float p_180558_9_) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)x, (float)y, (float)z);
      GlStateManager.func_179091_B();
      GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
      this.func_180548_c(hook);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexbuffer = tessellator.func_178180_c();
      byte b0 = 1;
      byte b1 = 2;
      float f2 = (float)(b0 * 8) / 128.0F;
      float f3 = (float)(b0 * 8 + 8) / 128.0F;
      float f4 = (float)(b1 * 8) / 128.0F;
      float f5 = (float)(b1 * 8 + 8) / 128.0F;
      float f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.5F;
      GlStateManager.func_179114_b(180.0F - this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181710_j);
      vertexbuffer.func_181662_b((double)(0.0F - f7), (double)(0.0F - f8), 0.0).func_187315_a((double)f2, (double)f5).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      vertexbuffer.func_181662_b((double)(f6 - f7), (double)(0.0F - f8), 0.0).func_187315_a((double)f3, (double)f5).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      vertexbuffer.func_181662_b((double)(f6 - f7), (double)(1.0F - f8), 0.0).func_187315_a((double)f3, (double)f4).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      vertexbuffer.func_181662_b((double)(0.0F - f7), (double)(1.0F - f8), 0.0).func_187315_a((double)f2, (double)f4).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179101_C();
      GlStateManager.func_179121_F();
      if (hook.field_146042_b != null) {
         this.renderRodAndHook(hook, x, y, z, p_180558_9_);
      }

      int state = (Integer)hook.func_184212_Q().func_187225_a(EntityHook.DATA_HOOK_STATE);
      if (state != -1 && Minecraft.func_71410_x().field_71439_g == hook.field_146042_b) {
         FontRenderer fontRenderer = this.func_76983_a();
         float var13 = 1.6F;
         float var14 = 0.02F * var13;
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b((float)x, (float)y + 1.0F, (float)z);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.func_179114_b(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179114_b(this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
         GlStateManager.func_179152_a(-var14, -var14, var14);
         GlStateManager.func_179140_f();
         GlStateManager.func_179126_j();
         GlStateManager.func_179118_c();
         GlStateManager.func_179098_w();
         String text = ". . .";
         if (state == 0) {
            if (hook.dotsShowing == 0) {
               text = ".";
            } else if (hook.dotsShowing == 1) {
               text = ". .";
            } else {
               text = ". . .";
            }
         } else if (state > 0) {
            text = "!";

            for(int i = 1; i < state; ++i) {
               text = text + " !";
            }
         }

         fontRenderer.func_78276_b(text, -fontRenderer.func_78256_a(text) / 2 + 1, 0, 553648127);
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179145_e();
         GlStateManager.func_179084_k();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179121_F();
      }

   }

   public void renderRodAndHook(EntityHook entity, double x, double y, double z, float partialTicks) {
      EntityPlayer entityplayer = entity.func_190619_l();
      if (entityplayer != null && !this.field_188301_f) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b((float)x, (float)y, (float)z);
         GlStateManager.func_179091_B();
         GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
         this.func_180548_c(entity);
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder bufferbuilder = tessellator.func_178180_c();
         GlStateManager.func_179114_b(180.0F - this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179114_b((float)(this.field_76990_c.field_78733_k.field_74320_O == 2 ? -1 : 1) * -this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
         if (this.field_188301_f) {
            GlStateManager.func_179142_g();
            GlStateManager.func_187431_e(this.func_188298_c(entity));
         }

         bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181710_j);
         bufferbuilder.func_181662_b(-0.5, -0.5, 0.0).func_187315_a(0.0625, 0.1875).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
         bufferbuilder.func_181662_b(0.5, -0.5, 0.0).func_187315_a(0.125, 0.1875).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
         bufferbuilder.func_181662_b(0.5, 0.5, 0.0).func_187315_a(0.125, 0.125).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
         bufferbuilder.func_181662_b(-0.5, 0.5, 0.0).func_187315_a(0.0625, 0.125).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
         tessellator.func_78381_a();
         if (this.field_188301_f) {
            GlStateManager.func_187417_n();
            GlStateManager.func_179119_h();
         }

         GlStateManager.func_179101_C();
         GlStateManager.func_179121_F();
         int k = entityplayer.func_184591_cq() == EnumHandSide.RIGHT ? 1 : -1;
         ItemStack itemstack = entityplayer.func_184614_ca();
         if (!(itemstack.func_77973_b() instanceof ItemFishingRod)) {
            k = -k;
         }

         float f7 = entityplayer.func_70678_g(partialTicks);
         float f8 = MathHelper.func_76126_a(MathHelper.func_76129_c(f7) * 3.1415927F);
         float f9 = (entityplayer.field_70760_ar + (entityplayer.field_70761_aq - entityplayer.field_70760_ar) * partialTicks) * 0.017453292F;
         double d0 = (double)MathHelper.func_76126_a(f9);
         double d1 = (double)MathHelper.func_76134_b(f9);
         double d2 = (double)k * 0.35;
         double d4;
         double d5;
         double d6;
         double d7;
         if ((this.field_76990_c.field_78733_k == null || this.field_76990_c.field_78733_k.field_74320_O <= 0) && entityplayer == Minecraft.func_71410_x().field_71439_g) {
            float f10 = this.field_76990_c.field_78733_k.field_74334_X;
            f10 /= 100.0F;
            Vec3d vec3d = new Vec3d((double)k * -0.36 * (double)f10, -0.045 * (double)f10, 0.4);
            vec3d = vec3d.func_178789_a(-(entityplayer.field_70127_C + (entityplayer.field_70125_A - entityplayer.field_70127_C) * partialTicks) * 0.017453292F);
            vec3d = vec3d.func_178785_b(-(entityplayer.field_70126_B + (entityplayer.field_70177_z - entityplayer.field_70126_B) * partialTicks) * 0.017453292F);
            vec3d = vec3d.func_178785_b(f8 * 0.5F);
            vec3d = vec3d.func_178789_a(-f8 * 0.7F);
            d4 = entityplayer.field_70169_q + (entityplayer.field_70165_t - entityplayer.field_70169_q) * (double)partialTicks + vec3d.field_72450_a;
            d5 = entityplayer.field_70167_r + (entityplayer.field_70163_u - entityplayer.field_70167_r) * (double)partialTicks + vec3d.field_72448_b;
            d6 = entityplayer.field_70166_s + (entityplayer.field_70161_v - entityplayer.field_70166_s) * (double)partialTicks + vec3d.field_72449_c;
            d7 = (double)entityplayer.func_70047_e();
         } else {
            d4 = entityplayer.field_70169_q + (entityplayer.field_70165_t - entityplayer.field_70169_q) * (double)partialTicks - d1 * d2 - d0 * 0.8;
            d5 = entityplayer.field_70167_r + (double)entityplayer.func_70047_e() + (entityplayer.field_70163_u - entityplayer.field_70167_r) * (double)partialTicks - 0.45;
            d6 = entityplayer.field_70166_s + (entityplayer.field_70161_v - entityplayer.field_70166_s) * (double)partialTicks - d0 * d2 + d1 * 0.8;
            d7 = entityplayer.func_70093_af() ? -0.1875 : 0.0;
         }

         double d13 = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)partialTicks;
         double d8 = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)partialTicks + 0.25;
         double d9 = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)partialTicks;
         double d10 = (double)((float)(d4 - d13));
         double d11 = (double)((float)(d5 - d8)) + d7 - 0.05;
         double d12 = (double)((float)(d6 - d9));
         GlStateManager.func_179090_x();
         GlStateManager.func_179140_f();
         bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);

         for(int i1 = 0; i1 <= 16; ++i1) {
            float f11 = (float)i1 / 16.0F;
            bufferbuilder.func_181662_b(x + d10 * (double)f11, y + d11 * (double)(f11 * f11 + f11) * 0.5 + 0.25, z + d12 * (double)f11).func_181669_b(0, 0, 0, 255).func_181675_d();
         }

         tessellator.func_78381_a();
         GlStateManager.func_179145_e();
         GlStateManager.func_179098_w();
      }

   }

   protected ResourceLocation func_110775_a(EntityFishHook par1Entity) {
      return field_110792_a;
   }

   public void func_76986_a(EntityFishHook par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderHook((EntityHook)par1Entity, par2, par4, par6, par8, par9);
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackLightning extends AttackSystemBase {
   List renderPoints = new ArrayList();

   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
   }

   public void onConstruct(AttackEffect effect) {
   }

   public void onInit(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onEnable(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdate(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onTarget(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdateEol(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdateLast(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onPreRender(ParticleArcanery particle, float partialTicks, AttackEffect effect) {
   }

   public void onPostRender(ParticleArcanery particle, float partialTicks, AttackEffect effect) {
      Minecraft mc = Minecraft.func_71410_x();
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(-mc.func_175606_aa().field_70165_t, -mc.func_175606_aa().field_70163_u, -mc.func_175606_aa().field_70161_v);
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      GlStateManager.func_179090_x();
      GlStateManager.func_179140_f();
      GlStateManager.func_179147_l();
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE);
      double[] adouble = new double[8];
      double[] adouble1 = new double[8];
      double d0 = 0.0;
      double d1 = 0.0;
      int r = effect.rand.nextInt(Integer.MAX_VALUE);
      GlStateManager.func_187441_d(0.2F);
      Random random = new Random((long)r);

      int k1;
      for(k1 = 7; k1 >= 0; --k1) {
         adouble[k1] = d0;
         adouble1[k1] = d1;
         d0 += (double)(random.nextInt(11) - 5);
         d1 += (double)(random.nextInt(11) - 5);
      }

      for(k1 = 0; k1 < 1; ++k1) {
         Random random1 = new Random((long)r);

         for(int j = 0; j < 3; ++j) {
            int k = 7;
            int l = 0;
            if (j > 0) {
               k = 7 - j;
            }

            if (j > 0) {
               l = k - 2;
            }

            double d2 = adouble[k] - d0;
            double d3 = adouble1[k] - d1;

            for(int i1 = k; i1 >= l; --i1) {
               if (j == 0) {
                  d2 += (double)(random1.nextInt(11) - 5);
                  d3 += (double)(random1.nextInt(11) - 5);
               } else {
                  d2 += (double)(random1.nextInt(31) - 15);
                  d3 += (double)(random1.nextInt(31) - 15);
               }

               bufferbuilder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
               double d6 = 0.1 + (double)k1 * 0.2;
               if (j == 0) {
                  d6 *= (double)i1 * 0.1 + 1.0;
               }

               double d7 = 0.1 + (double)k1 * 0.2;
               if (j == 0) {
                  d7 *= (double)(i1 - 1) * 0.1 + 1.0;
               }

               for(int j1 = 0; j1 < 5; ++j1) {
                  double d8 = (double)this.endPos[0] + 0.5 - d6;
                  double d9 = (double)this.endPos[2] + 0.5 - d6;
                  double var10000;
                  if (j1 == 1 || j1 == 2) {
                     var10000 = d8 + d6 * 2.0;
                  }

                  if (j1 == 2 || j1 == 3) {
                     var10000 = d9 + d6 * 2.0;
                  }

                  double d10 = (double)this.endPos[0] + 0.5 - d7;
                  double d11 = (double)this.endPos[2] + 0.5 - d7;
                  if (j1 == 1 || j1 == 2) {
                     var10000 = d10 + d7 * 2.0;
                  }

                  if (j1 == 2 || j1 == 3) {
                     var10000 = d11 + d7 * 2.0;
                  }
               }

               tessellator.func_78381_a();
            }
         }
      }

      GlStateManager.func_179084_k();
      GlStateManager.func_179145_e();
      GlStateManager.func_179098_w();
      GlStateManager.func_179121_F();
   }

   public boolean hasCustomRenderer(AttackEffect effect) {
      return false;
   }

   public void onRender(ParticleArcanery particle, Tessellator tessellator, float partialTicks, AttackEffect effect) {
   }

   public static class LightningData extends AttackAnimationData {
      public boolean fork = true;
      public boolean targetsFromSelf = true;

      public void writeToByteBuffer(ByteBuf buf) {
         buf.writeBoolean(this.fork);
         buf.writeBoolean(this.targetsFromSelf);
      }

      public AttackAnimationData readFromByteBuffer(ByteBuf buf) {
         this.fork = buf.readBoolean();
         this.targetsFromSelf = buf.readBoolean();
         return this;
      }

      public EnumEffectType getEffectEnum() {
         return EnumEffectType.LIGHTNING;
      }

      public void initFromAttack(AttackBase attack, int effectivePower, EnumType effectiveType) {
      }
   }
}

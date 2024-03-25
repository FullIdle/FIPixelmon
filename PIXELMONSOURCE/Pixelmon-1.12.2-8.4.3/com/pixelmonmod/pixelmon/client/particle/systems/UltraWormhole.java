package com.pixelmonmod.pixelmon.client.particle.systems;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.Blob;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class UltraWormhole extends ParticleSystem {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      GlStateManager.func_179094_E();
      long time = (long)args[1];
      long mod100 = time % 15L;
      double yaw = args[2] * 0.017453292519943295;
      double pitch = args[3] * 0.017453292519943295;
      Entity viewpoint = mc.func_175606_aa() == null ? mc.field_71439_g : mc.func_175606_aa();
      float tx = (float)(((Entity)viewpoint).field_70169_q + (((Entity)viewpoint).field_70165_t - ((Entity)viewpoint).field_70169_q) * (double)mc.func_184121_ak());
      float ty = (float)(((Entity)viewpoint).field_70167_r + (((Entity)viewpoint).field_70163_u - ((Entity)viewpoint).field_70167_r) * (double)mc.func_184121_ak());
      float tz = (float)(((Entity)viewpoint).field_70166_s + (((Entity)viewpoint).field_70161_v - ((Entity)viewpoint).field_70166_s) * (double)mc.func_184121_ak());
      GlStateManager.func_179090_x();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_179140_f();
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179109_b(-tx, -ty, -tz);
      GL11.glEnable(2848);
      GlStateManager.func_187441_d(1.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      double[] cx = new double[9];
      double[] cy = new double[9];
      double[] cz = new double[9];
      double[] coords = new double[768];
      int coordsIndex = 0;

      int i;
      double ox;
      double rad;
      double theta;
      double dx;
      double dx2;
      for(i = 0; i < 16; ++i) {
         ox = 0.0;
         rad = 0.1;
         float radiusDecay = 0.35F;
         double radius = 3.25;
         theta = Math.cos(0.39269908169872414 * (double)i);
         dx = Math.sin(0.39269908169872414 * (double)i);

         for(int segments = 0; segments < 8; ++segments) {
            dx2 = radius - (double)radiusDecay;
            coords[coordsIndex++] = x + theta * radius;
            coords[coordsIndex++] = y + dx * radius;
            coords[coordsIndex++] = z + ox;
            coords[coordsIndex++] = x + theta * dx2;
            coords[coordsIndex++] = y + dx * dx2;
            coords[coordsIndex++] = z + rad;
            if (segments == 0) {
               cx[segments] = radius;
               cy[segments] = radius;
               cz[segments] = ox;
            }

            if (i == 1) {
               cx[segments + 1] = dx2;
               cy[segments + 1] = dx2;
               cz[segments + 1] = rad;
            }

            ox = rad;
            rad += 0.2 * (double)(segments + 1);
            radius -= (double)radiusDecay;
            radiusDecay -= 0.02F;
         }
      }

      coordsIndex = 0;

      int colorPattern;
      int spokes;
      for(i = 0; i < 16; ++i) {
         GL11.glHint(3154, 4354);
         GL11.glBegin(3);

         for(spokes = 0; spokes < 8; ++spokes) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, (float)spokes / 9.0F);

            for(colorPattern = 0; colorPattern < 2; ++colorPattern) {
               this.rotatePointAndDraw(coords[coordsIndex++], coords[coordsIndex++], coords[coordsIndex++], x, y, z, yaw, pitch);
            }
         }

         GL11.glEnd();
      }

      double dx;
      for(i = 0; i < 9; ++i) {
         if (i > 1) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, (float)i / 9.0F * (i == 2 ? (float)mod100 / 15.0F * 2.0F : 1.0F));
            GL11.glHint(3154, 4354);
            GL11.glBegin(3);
            ox = cx[i];
            rad = cx[i];
            dx = cz[i];
            if (i != 8) {
               ox = cx[i] + (cx[i + 1] - cx[i]) * ((double)mod100 / 15.0);
               rad = cy[i] + (cy[i + 1] - cy[i]) * ((double)mod100 / 15.0);
               dx = cz[i] + (cz[i + 1] - cz[i]) * ((double)mod100 / 15.0);
            }

            for(int spokes = 0; spokes < 17; ++spokes) {
               theta = 0.39269908169872414 * (double)(spokes == 16 ? 0 : spokes);
               dx = Math.cos(theta);
               double dy = Math.sin(theta);
               this.rotatePointAndDraw(x + ox * dx, y + rad * dy, z + dx, x, y, z, yaw, pitch);
            }

            GL11.glEnd();
         }
      }

      int forks;
      double dz;
      int colorSpeed;
      double dy;
      for(i = 0; i < 9; ++i) {
         GlStateManager.func_179129_p();
         if (i != 8) {
            for(spokes = 0; spokes < 16; ++spokes) {
               colorPattern = (int)args[0];
               colorSpeed = 120;
               forks = ((int)args[1] + (int)w.func_82737_E() % colorSpeed) % colorSpeed + i * 4;
               GlStateManager.func_179131_c(this.calcRed(colorPattern, colorSpeed, forks), this.calcGreen(colorPattern, colorSpeed, forks), this.calcBlue(colorPattern, colorSpeed, forks), 1.0F - 0.11F * (float)(i + 1));
               GL11.glHint(3155, 4354);
               GL11.glBegin(7);
               dx = 0.39269908169872414 * (double)spokes;
               dy = Math.cos(dx);
               dz = Math.sin(dx);
               double theta2 = 0.39269908169872414 * (double)(spokes + 1 == 16 ? 0 : spokes + 1);
               dx2 = Math.cos(theta2);
               double dy2 = Math.sin(theta2);
               this.rotatePointAndDraw(x + cx[7 - i] * dy, y + cy[7 - i] * dz, z + cz[7 - i], x, y, z, yaw, pitch);
               this.rotatePointAndDraw(x + cx[7 - i] * dx2, y + cy[7 - i] * dy2, z + cz[7 - i], x, y, z, yaw, pitch);
               this.rotatePointAndDraw(x + cx[7 - i + 1] * dx2, y + cy[7 - i + 1] * dy2, z + cz[7 - i + 1], x, y, z, yaw, pitch);
               this.rotatePointAndDraw(x + cx[7 - i + 1] * dy, y + cy[7 - i + 1] * dz, z + cz[7 - i + 1], x, y, z, yaw, pitch);
               GL11.glEnd();
            }
         }

         GlStateManager.func_179089_o();
      }

      GlStateManager.func_187441_d(1.0F);
      long seed = time - mod100 + (long)x + (long)y + (long)z;
      Random rand = new Random(seed);

      for(colorSpeed = 0; colorSpeed < 3; ++colorSpeed) {
         if (mod100 < 7L) {
            forks = rand.nextInt(5) + 5;
            dx = x;
            dy = y;
            dz = z + 5.5;
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F - 0.14285715F * (float)mod100);

            for(int i = 0; i < forks; ++i) {
               GL11.glHint(3154, 4354);
               GL11.glBegin(3);
               this.rotatePointAndDraw(dx, dy, dz, x, y, z, yaw, pitch);
               dx += (rand.nextDouble() * 2.0 - 1.0) / 2.0;
               dy += rand.nextDouble() * 2.0 - 1.0;
               dz -= rand.nextDouble() * 2.0;
               this.rotatePointAndDraw(dx, dy, dz, x, y, z, yaw, pitch);
               GL11.glEnd();
               if (i > 4) {
                  GL11.glHint(3154, 4354);
                  GL11.glBegin(3);
                  this.rotatePointAndDraw(dx, dy, dz, x, y, z, yaw, pitch);
                  this.rotatePointAndDraw(dx + (rand.nextDouble() * 2.0 - 1.0) / 2.0, dy + rand.nextDouble() * 2.0 - 1.0, dz + rand.nextDouble() * 2.0, x, y, z, yaw, pitch);
                  GL11.glEnd();
               }
            }
         }
      }

      rad = 0.85;
      dx = w.field_73012_v.nextDouble() * rad * 2.0 - rad;
      dy = w.field_73012_v.nextDouble() * rad * 2.0 - rad;
      dz = w.field_73012_v.nextDouble() * rad * 2.0 - rad;
      if (Math.sqrt(dx * dx + dy * dy + dz * dz) <= rad) {
         double[] center = this.rotatePoint(x, y, z + 5.0, x, y, z, yaw, pitch);
         mc.field_71452_i.func_78873_a(new ParticleArcanery(w, center[0] + dx, center[1] + dy, center[2] + dz, 0.0, 0.0, 0.0, new Blob(0.0, 0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F, true, 10, (int)args[0], 1.7F)));
      }

      GL11.glDisable(2848);
      GlStateManager.func_187441_d(1.0F);
      GlStateManager.func_179084_k();
      GlStateManager.func_179098_w();
      GlStateManager.func_179145_e();
      GlStateManager.func_179126_j();
      GlStateManager.func_179141_d();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179121_F();
   }

   private double[] rotatePoint(double px, double py, double pz, double cx, double cy, double cz, double yaw, double pitch) {
      double rx = Math.cos(yaw) * (px - cx) - Math.sin(yaw) * (pz - cz) + cx;
      double rz = Math.sin(yaw) * (px - cx) + Math.cos(yaw) * (pz - cz) + cz;
      double ry = Math.sin(pitch) * (rz - cz) + Math.cos(pitch) * (py - cy) + cy;
      rz = Math.cos(pitch) * (rz - cz) - Math.sin(pitch) * (py - cy) + cz;
      return new double[]{rx, ry, rz};
   }

   private void rotatePointAndDraw(double px, double py, double pz, double cx, double cy, double cz, double yaw, double pitch) {
      double[] points = this.rotatePoint(px, py, pz, cx, cy, cz, yaw, pitch);
      GL11.glVertex3d(points[0], points[1], points[2]);
   }

   private float calcChangingColor(int colorSpeed, int colorDeterminant) {
      float c1 = 1.0F / (float)colorSpeed;
      float c2 = c1 * (float)colorDeterminant;
      if (c2 > 0.5F) {
         float c3 = c2 - 0.5F;
         c2 -= c3 * 2.0F;
      }

      return c2;
   }

   private float calcRed(int colorPattern, int colorSpeed, int colorDeterminant) {
      switch (colorPattern) {
         case 0:
         case 1:
            return this.calcChangingColor(colorSpeed, colorDeterminant);
         case 2:
         case 3:
            return 0.2F;
         case 4:
         case 5:
            return 0.7F;
         default:
            return 0.0F;
      }
   }

   private float calcGreen(int colorPattern, int colorSpeed, int colorDeterminant) {
      switch (colorPattern) {
         case 0:
         case 5:
            return 0.2F;
         case 1:
         case 3:
            return 0.7F;
         case 2:
         case 4:
            return this.calcChangingColor(colorSpeed, colorDeterminant);
         default:
            return 0.0F;
      }
   }

   private float calcBlue(int colorPattern, int colorSpeed, int colorDeterminant) {
      switch (colorPattern) {
         case 0:
         case 2:
            return 0.7F;
         case 1:
         case 4:
            return 0.2F;
         case 3:
         case 5:
            return this.calcChangingColor(colorSpeed, colorDeterminant);
         default:
            return 0.0F;
      }
   }
}

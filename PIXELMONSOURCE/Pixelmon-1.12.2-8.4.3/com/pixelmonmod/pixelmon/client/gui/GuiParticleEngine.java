package com.pixelmonmod.pixelmon.client.gui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Consumer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiParticleEngine {
   private final HashSet particles = new HashSet();

   public void draw() {
      this.particles.removeIf((rec$) -> {
         return ((GuiParticle)rec$).draw();
      });
   }

   public void draw(String caller) {
      Iterator iterator = this.particles.iterator();

      while(iterator.hasNext()) {
         GuiParticle particle = (GuiParticle)iterator.next();
         if (particle.draw(caller)) {
            iterator.remove();
         }
      }

   }

   public void drawAtOffset(String caller, double dx, double dy, double sdx, double sdy) {
      Iterator iterator = this.particles.iterator();

      while(iterator.hasNext()) {
         GuiParticle particle = (GuiParticle)iterator.next();
         if (particle.draw(caller, dx, dy, sdx, sdy)) {
            iterator.remove();
         }
      }

   }

   public void addParticle(GuiParticle particle) {
      this.particles.add(particle);
   }

   public static class GuiParticle {
      public ResourceLocation image;
      public double x;
      public double y;
      public double z;
      public double mX;
      public double mY;
      public double mZ;
      public float r;
      public float g;
      public float b;
      public float a;
      public float w;
      public float h;
      public int age;
      public int maxAge;
      public boolean setStaticOffsets;
      public double sdx;
      public double sdy;
      private final Consumer logic;
      private final String caller;

      public GuiParticle(ResourceLocation image, double x, double y, double z, double mX, double mY, double mZ, float r, float g, float b, float a, float w, float h, int maxAge, Consumer logic) {
         this((String)null, image, x, y, z, mX, mY, mZ, r, g, b, a, w, h, maxAge, logic);
      }

      public GuiParticle(String caller, ResourceLocation image, double x, double y, double z, double mX, double mY, double mZ, float r, float g, float b, float a, float w, float h, int maxAge, Consumer logic) {
         this.setStaticOffsets = false;
         this.caller = caller;
         this.image = image;
         this.x = x;
         this.y = y;
         this.z = z;
         this.mX = mX;
         this.mY = mY;
         this.mZ = mZ;
         this.r = r;
         this.g = g;
         this.b = b;
         this.a = a;
         this.w = w;
         this.h = h;
         this.age = 0;
         this.maxAge = maxAge;
         this.logic = logic;
      }

      private boolean draw() {
         return this.draw((String)null);
      }

      private boolean draw(String caller) {
         return this.draw(caller, 0.0, 0.0, 0.0, 0.0);
      }

      private boolean draw(String caller, double dx, double dy, double sdx, double sdy) {
         if (this.caller != null && !this.caller.equalsIgnoreCase(caller)) {
            return false;
         } else {
            if (!this.setStaticOffsets) {
               this.sdx = sdx;
               this.sdy = sdy;
               this.setStaticOffsets = true;
            }

            ++this.age;
            GlStateManager.func_179094_E();
            if (this.logic != null) {
               this.logic.accept(this);
            }

            if (this.image != null) {
               GlStateManager.func_179141_d();
               GlStateManager.func_179147_l();
               GlStateManager.func_179112_b(770, 771);
               GlStateManager.func_179131_c(this.r, this.g, this.b, this.a);
               GuiHelper.drawImage(this.image, dx + this.sdx + this.x, dy + this.sdy + this.y, (double)this.w, (double)this.h, (float)this.z);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GlStateManager.func_179121_F();
            this.x += this.mX;
            this.y += this.mY;
            this.z += this.mZ;
            return this.age >= this.maxAge;
         }
      }
   }
}

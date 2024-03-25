package com.pixelmonmod.pixelmon.client.materials;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public enum EnumMaterialOption {
   NO_LIGHTING {
      public void begin(Object... params) {
         EnumMaterialOption.FS.start_unlit();
      }

      public void end(Object... params) {
         EnumMaterialOption.FS.end_unlit();
      }
   },
   WIREFRAME {
      public void begin(Object... params) {
         EnumMaterialOption.FS.start_wire();
      }

      public void end(Object... params) {
         GL11.glPopAttrib();
      }
   },
   TRANSPARENCY {
      public void begin(Object... params) {
         EnumMaterialOption.FS.start_transparency(params);
      }

      public void end(Object... params) {
         EnumMaterialOption.FS.end_transparency();
      }
   },
   NOCULL {
      public void begin(Object... params) {
         EnumMaterialOption.FS.enable_nocull();
      }

      public void end(Object... params) {
         EnumMaterialOption.FS.disable_nocull();
      }
   };

   public static int cubemapID = 33986;

   private EnumMaterialOption() {
   }

   public abstract void begin(Object... var1);

   public abstract void end(Object... var1);

   // $FF: synthetic method
   EnumMaterialOption(Object x2) {
      this();
   }

   private static final class FS {
      public static void start_wire() {
         GL11.glPushAttrib(2880);
         GL11.glPolygonMode(1032, 6913);
      }

      public static void start_unlit() {
         GlStateManager.func_179140_f();
         Minecraft.func_71410_x().field_71460_t.func_175072_h();
      }

      public static void end_unlit() {
         GlStateManager.func_179145_e();
         Minecraft.func_71410_x().field_71460_t.func_180436_i();
      }

      public static void start_transparency(Object[] actuallyAFloat) {
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         if (actuallyAFloat != null && actuallyAFloat.length > 0 && actuallyAFloat[0] instanceof Float) {
            float alpha = (Float)actuallyAFloat[0];
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, alpha);
         }

      }

      public static void end_transparency() {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179084_k();
      }

      public static void enable_nocull() {
         GlStateManager.func_179089_o();
      }

      public static void disable_nocull() {
         GlStateManager.func_179129_p();
      }
   }
}

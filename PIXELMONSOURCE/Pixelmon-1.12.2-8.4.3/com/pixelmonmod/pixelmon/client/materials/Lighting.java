package com.pixelmonmod.pixelmon.client.materials;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Lighting {
   private static final ByteBuffer buffer = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder());

   public static void testLight(float x, float y, float z) {
      GlStateManager.func_179145_e();
      GL11.glEnable(16385);
      float[] lightAmbient = new float[]{1.0F, 0.0F, 1.0F, 1.0F};
      float[] lightDiffuse = new float[]{1.0F, 0.0F, 1.0F, 1.0F};
      float[] lightPosition = new float[]{x, y, z, 1.0F};
      GL11.glLight(16385, 4611, (FloatBuffer)buffer.asFloatBuffer().put(lightPosition).flip());
      GL11.glLight(16385, 4608, (FloatBuffer)buffer.asFloatBuffer().put(lightAmbient).flip());
      GL11.glLight(16385, 4609, (FloatBuffer)buffer.asFloatBuffer().put(lightDiffuse).flip());
   }

   public static void attempt(float x, float y, float z) {
      GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.func_179103_j(7425);
      float[] light_position = new float[]{x, y, z, 1.0F};
      float[] lightAmbient = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
      float[] lightDiffuse = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
      GlStateManager.func_179142_g();
      GL11.glColorMaterial(1032, 5634);
      GL11.glLight(16384, 4611, (FloatBuffer)buffer.asFloatBuffer().put(light_position).flip());
      GL11.glLight(16384, 4608, (FloatBuffer)buffer.asFloatBuffer().put(lightAmbient).flip());
      GL11.glLight(16384, 4609, (FloatBuffer)buffer.asFloatBuffer().put(lightDiffuse).flip());
      GlStateManager.func_179145_e();
      GL11.glEnable(16384);
   }

   public static void wat() {
   }
}

package com.pixelmonmod.pixelmon.util.helpers;

import java.lang.reflect.Field;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class VectorHelper {
   static final Vector3f X_AXIS = new Vector3f(1.0F, 0.0F, 0.0F);
   static final Vector3f Y_AXIS = new Vector3f(0.0F, 1.0F, 0.0F);
   static final Vector3f Z_AXIS = new Vector3f(0.0F, 0.0F, 1.0F);
   public static final float toDegrees = 57.29578F;
   public static final float toRadians = 0.017453292F;

   public static Vec3d add(Vec3d one, Vec3d other) {
      return one.func_72441_c(other.field_72450_a, other.field_72448_b, other.field_72449_c);
   }

   public static double[] rotate(double x, double y, double radians) {
      double cos = Math.cos(radians);
      double sin = Math.sin(radians);
      double newX = x * cos - y * sin;
      double newY = y * cos + x * sin;
      return new double[]{newX, newY};
   }

   public static Vector3f createOrNull(Number x, Number y, Number z) {
      return x != null && y != null && z != null ? new Vector3f(x.floatValue(), y.floatValue(), z.floatValue()) : null;
   }

   public static void print(Vec3d target) {
      System.out.println("Vector x = " + target.field_72450_a);
      System.out.println("Vector y = " + target.field_72448_b);
      System.out.println("Vector z = " + target.field_72449_c);
   }

   public static void print(Object target) {
      System.out.println(target);
   }

   public static void printAlternate(Matrix4f target) {
      Field[] fields = target.getClass().getFields();
      System.out.println("MATRIX DATA");
      System.out.println("~~~Standard Print~~~");
      print((Object)target);
      System.out.println("~~~In-Depth Print~~~");
      Field[] var2 = fields;
      int var3 = fields.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field f = var2[var4];
         String descript = f.getName() + " = ";

         try {
            descript = descript + f.getFloat(target);
         } catch (Exception var8) {
            descript = descript + "ERROR";
         }

         System.out.println(descript);
      }

   }

   public static Matrix4f matrix4FromLocRot(float xl, float yl, float zl, float xr, float yr, float zr) {
      Vector3f loc = new Vector3f(xl, yl, zl);
      Matrix4f part1 = new Matrix4f();
      part1.translate(loc);
      part1.rotate(zr, Z_AXIS);
      part1.rotate(yr, Y_AXIS);
      part1.rotate(xr, X_AXIS);
      return part1;
   }

   public static Matrix4f matrix4FromFloatArray(float[] vals) {
      return matrix4FromLocRot(vals[0], vals[1], vals[2], vals[3], vals[4], vals[5]);
   }

   public static Matrix4f matrix4fFromFloat(float val) {
      return matrix4FromLocRot(val, val, val, val, val, val);
   }

   public static Vector4f mul(Vector4f target, float factor, Vector4f dest) {
      if (dest == null) {
         dest = new Vector4f();
      }

      dest.x = target.x * factor;
      dest.y = target.y * factor;
      dest.z = target.z * factor;
      dest.w = target.w * factor;
      return dest;
   }

   public static Matrix4f mul(Matrix4f target, float factor, Matrix4f dest) {
      if (dest == null) {
         dest = new Matrix4f();
      }

      dest.m00 = target.m00 * factor;
      dest.m01 = target.m01 * factor;
      dest.m02 = target.m02 * factor;
      dest.m02 = target.m03 * factor;
      dest.m10 = target.m10 * factor;
      dest.m11 = target.m11 * factor;
      dest.m12 = target.m12 * factor;
      dest.m13 = target.m13 * factor;
      dest.m20 = target.m20 * factor;
      dest.m21 = target.m21 * factor;
      dest.m22 = target.m22 * factor;
      dest.m23 = target.m23 * factor;
      dest.m30 = target.m30 * factor;
      dest.m31 = target.m31 * factor;
      dest.m32 = target.m32 * factor;
      dest.m33 = target.m33 * factor;
      return target;
   }

   public static float[] getLoc(Matrix4f target) {
      return new float[]{target.m30, target.m31, target.m32};
   }

   public static Vector3f v3LocFromM4(Matrix4f target) {
      return new Vector3f(target.m30, target.m31, target.m32);
   }

   public static Vector3f getInverse(Vector3f target) {
      return new Vector3f(-target.x, -target.y, -target.z);
   }

   public static Vector3f Vec3dfFromStrings(String x, String y, String z) {
      float xl = Float.parseFloat(x);
      float yl = Float.parseFloat(y);
      float zl = Float.parseFloat(z);
      return new Vector3f(xl, yl, zl);
   }

   public static Vector4f copyVector4f(Vector4f src) {
      return new Vector4f(src.x, src.y, src.z, src.w);
   }
}

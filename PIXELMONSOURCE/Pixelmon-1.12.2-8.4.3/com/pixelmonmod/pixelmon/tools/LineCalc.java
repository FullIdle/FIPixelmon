package com.pixelmonmod.pixelmon.tools;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class LineCalc {
   public static float linearScaleInteger(float initial_value, float final_value, int animationCounterLength, int animationCounterValue) {
      return (final_value - initial_value) / (float)animationCounterLength * (float)animationCounterValue + initial_value;
   }

   public static void linearMorph(ModelRenderer rotObj, float startX, float startY, float startZ, float endX, float endY, float endZ, int animLength, int animValue) {
      float toRadians = 0.017453292F;
      float x = linearScaleInteger(startX * toRadians, endX * toRadians, animLength, animValue);
      float y = linearScaleInteger(startY * toRadians, endY * toRadians, animLength, animValue);
      float z = linearScaleInteger(startZ * toRadians, endZ * toRadians, animLength, animValue);
      rotObj.field_78795_f = x;
      rotObj.field_78796_g = y;
      rotObj.field_78808_h = z;
   }

   public static float lerp(float min, float max, float limit, float value) {
      return (max - min) / limit * value + min;
   }

   public static float lerp(float ratio, float min, float max) {
      return lerp(min, max, 1.0F, ratio);
   }

   public static float ratio(float value, float min, float max) {
      return MathHelper.func_76131_a((value - min) / (max - min), 0.0F, 1.0F);
   }

   public static float lerp(float scalar, float minScalar, float maxScalar, float minResult, float maxResult) {
      return lerp(ratio(scalar, minScalar, maxScalar), minResult, maxResult);
   }
}

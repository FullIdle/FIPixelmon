package com.pixelmonmod.pixelmon.client.models.obj;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class ObjLoader {
   public static WavefrontObject loadModel(ResourceLocation resource) {
      try {
         IResource res = Minecraft.func_71410_x().func_110442_L().func_110536_a(resource);
         return new WavefrontObject(res.func_110527_b());
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static boolean accepts(ResourceLocation modelLocation) {
      return modelLocation.func_110623_a().endsWith(".obj");
   }
}

package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.Pixelmon;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;

public class EmissiveTextures implements ISelectiveResourceReloadListener {
   private static final Map existsCache = new WeakHashMap(64);
   private static float lightMapX;
   private static float lightMapY;
   private static boolean isRendering;

   public static boolean hasEmissive(IHasTexture pixelmon) {
      ResourceLocation location = getEmissive(pixelmon.getTexture());
      if (location == null) {
         return false;
      } else if (existsCache.containsKey(location)) {
         return (Boolean)existsCache.getOrDefault(location, false);
      } else if (Pixelmon.proxy.resourceLocationExists(location)) {
         existsCache.put(location, true);
         return true;
      } else {
         existsCache.put(location, false);
         return false;
      }
   }

   public static boolean emissiveExists(ResourceLocation rl) {
      if (rl == null) {
         return false;
      } else if (existsCache.containsKey(rl)) {
         return (Boolean)existsCache.getOrDefault(rl, (Object)null);
      } else if (Pixelmon.proxy.resourceLocationExists(rl)) {
         existsCache.put(rl, true);
         return true;
      } else {
         return false;
      }
   }

   public static void startEmissive() {
      isRendering = true;
      lightMapX = OpenGlHelper.lastBrightnessX;
      lightMapY = OpenGlHelper.lastBrightnessY;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, lightMapY);
   }

   public static void stopEmissive() {
      isRendering = false;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, lightMapX, lightMapY);
      lightMapX = 0.0F;
      lightMapY = 0.0F;
   }

   public static boolean isRendering() {
      return isRendering;
   }

   public static ResourceLocation getEmissive(ResourceLocation normal) {
      if ("pixelmon".equals(normal.func_110624_b())) {
         String original = normal.func_110623_a();
         original = original.replace("pokemon-shiny/", "");
         int split = original.lastIndexOf("/") + 1;
         String path = original.substring(0, split);
         String file = original.substring(split);
         return new ResourceLocation("pixelmon", path + "emissive/" + file);
      } else {
         return null;
      }
   }

   public void onResourceManagerReload(IResourceManager resourceManager, Predicate resourcePredicate) {
      if (resourcePredicate.test(VanillaResourceType.TEXTURES)) {
         existsCache.clear();
      }

   }
}

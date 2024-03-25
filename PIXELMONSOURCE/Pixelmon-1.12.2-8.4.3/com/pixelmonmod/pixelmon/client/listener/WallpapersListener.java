package com.pixelmonmod.pixelmon.client.listener;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.pixelmonmod.pixelmon.Pixelmon;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

public class WallpapersListener implements IResourceManagerReloadListener {
   public static final String DEFAULT_WALLPAPER = "default";
   private static final ResourceLocation DEFAULT = new ResourceLocation("pixelmon", String.format("textures/gui/pc/wallpapers/%s.png", "default"));
   private static final String SEARCH_WALLPAPER = "search";
   private static final ResourceLocation SEARCH = new ResourceLocation("pixelmon", String.format("textures/gui/pc/wallpapers/%s.png", "search"));
   private static Map wallpapersMap = new LinkedHashMap();
   private static List wallpaperNames = new ArrayList();
   private static final Gson GSON = new Gson();

   public static List getWallpapers() {
      return wallpaperNames;
   }

   public static boolean hasWallpaper(String name) {
      if ("search".equals(name)) {
         return true;
      } else {
         return name != null && wallpaperNames.contains(name);
      }
   }

   public static ResourceLocation getWallpaper(String name) {
      if ("search".equals(name)) {
         return SEARCH;
      } else {
         ResourceLocation wallpaper = (ResourceLocation)wallpapersMap.get(name);
         return wallpaper == null ? DEFAULT : wallpaper;
      }
   }

   public void func_110549_a(IResourceManager manager) {
      wallpapersMap.clear();
      wallpapersMap.put("default", DEFAULT);

      try {
         Iterator var2 = manager.func_135056_b(new ResourceLocation("pixelmon", "textures/gui/pc/wallpapers/wallpapers.json")).iterator();

         while(var2.hasNext()) {
            IResource resource = (IResource)var2.next();

            try {
               String[] wallpapers = (String[])GSON.fromJson(new InputStreamReader(resource.func_110527_b()), String[].class);
               String[] var5 = wallpapers;
               int var6 = wallpapers.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  String fullName = var5[var7];
                  String name = fullName.substring(0, fullName.lastIndexOf("."));
                  if (!name.equals("default") && !name.equals("search")) {
                     wallpapersMap.put(name, new ResourceLocation("pixelmon", "textures/gui/pc/wallpapers/" + fullName));
                  }
               }
            } catch (JsonParseException var10) {
               Pixelmon.LOGGER.error(String.format("There was a problem trying to read 'wallpapers.json' from the resource pack '%s'.", resource.func_177240_d()), var10);
            }
         }
      } catch (IOException var11) {
         Pixelmon.LOGGER.error("There was a problem trying to read 'wallpapers.json'.", var11);
      }

      wallpaperNames = ImmutableList.copyOf(wallpapersMap.keySet());
   }
}

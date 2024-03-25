package com.pixelmonmod.pixelmon.client.models;

import com.google.common.collect.Sets;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;

public class ResourceLoader implements ISelectiveResourceReloadListener {
   public static final PixelmonModelBase DUMMY = new PixelmonModelBase() {
   };
   private static int cleanupTime;
   private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor((r) -> {
      return new Thread(r, "Pixelmon resources thread");
   });

   public static Future addTask(Callable callable) {
      return executor.submit(callable);
   }

   private static void cleanupModels() {
      long now = Instant.now().getEpochSecond();
      Set holders = Sets.newHashSet(ModelHolder.loadedHolders);
      holders.removeIf((holder) -> {
         return now - holder.lastAccess < (long)cleanupTime;
      });
      Minecraft.func_71410_x().func_152344_a(() -> {
         holders.forEach(ModelHolder::clear);
      });
   }

   public void onResourceManagerReload(IResourceManager resourceManager, Predicate resourcePredicate) {
      if (resourcePredicate.test(VanillaResourceType.MODELS)) {
         ModelHolder.loadedHolders.forEach(ModelHolder::clear);
      }

   }

   static {
      if (Runtime.getRuntime().maxMemory() / 1024L / 1024L <= 1024L) {
         cleanupTime = 120;
      } else {
         cleanupTime = 240;
      }

      executor.scheduleAtFixedRate(ResourceLoader::cleanupModels, (long)cleanupTime, (long)cleanupTime, TimeUnit.SECONDS);
   }
}

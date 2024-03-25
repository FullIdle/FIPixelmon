package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldLoaded {
   @SubscribeEvent
   public static void worldLoaded(WorldEvent.Load loadedArgs) {
      if (!PixelmonConfig.allowNonPixelmonMobs) {
         loadedArgs.getWorld().func_82736_K().func_82764_b("doMobSpawning", "false");
      } else {
         loadedArgs.getWorld().func_82736_K().func_82764_b("doMobSpawning", "true");
      }

      if (!loadedArgs.getWorld().field_72995_K) {
         loadedArgs.getWorld().func_72954_a(new WorldEventListener());
      }

   }
}

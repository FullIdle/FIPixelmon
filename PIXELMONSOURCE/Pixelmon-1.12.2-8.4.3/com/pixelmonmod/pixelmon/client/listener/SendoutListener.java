package com.pixelmonmod.pixelmon.client.listener;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(
   value = {Side.CLIENT},
   modid = "pixelmon"
)
public class SendoutListener {
   static Map map = Maps.newConcurrentMap();

   @SubscribeEvent
   public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
      if (event.getEntity() instanceof EntityPixelmon && event.getWorld() == Minecraft.func_71410_x().field_71441_e) {
         EntityPixelmon pixelmon = (EntityPixelmon)event.getEntity();
         PlayerPartyStorage pps = ClientStorageManager.party;

         for(int i = 0; i < 6; ++i) {
            Pokemon p = pps.get(i);
            if (p != null && p.getUUID().equals(pixelmon.func_110124_au())) {
               map.put(pixelmon.func_110124_au(), pixelmon.func_145782_y());
            }
         }
      }

   }

   public static boolean isInWorld(UUID uuid, World world) {
      map.values().removeIf((id) -> {
         return world.func_73045_a(id) == null;
      });
      if (map.containsKey(uuid)) {
         int entityID = (Integer)map.get(uuid);
         return world.func_73045_a(entityID) != null;
      } else {
         return false;
      }
   }

   public static EntityPixelmon getEntityInWorld(UUID uuid, World world) {
      if (map.containsKey(uuid)) {
         int entityID = (Integer)map.get(uuid);
         return (EntityPixelmon)world.func_73045_a(entityID);
      } else {
         return null;
      }
   }
}

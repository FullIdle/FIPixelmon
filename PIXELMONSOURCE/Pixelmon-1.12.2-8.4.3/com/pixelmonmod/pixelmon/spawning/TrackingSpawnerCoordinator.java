package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.api.spawning.SpawnerCoordinator;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class TrackingSpawnerCoordinator extends SpawnerCoordinator {
   public TrackingSpawnerCoordinator() {
      super();
   }

   public TrackingSpawnerCoordinator activate() {
      super.activate();
      this.spawners.clear();
      Iterator var1 = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_181057_v().iterator();

      while(var1.hasNext()) {
         EntityPlayerMP player = (EntityPlayerMP)var1.next();
         this.spawners.add(PixelmonSpawning.trackingSpawnerPreset.apply(new PlayerTrackingSpawner(player)));
      }

      PixelmonSpawning.addRegularSpawners(this);
      return this;
   }

   @SubscribeEvent
   public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         this.spawners.add(PixelmonSpawning.trackingSpawnerPreset.apply(new PlayerTrackingSpawner((EntityPlayerMP)event.player)));
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerDeath(LivingDeathEvent event) {
      if (!event.getEntityLiving().func_130014_f_().field_72995_K) {
         if (event.getEntityLiving() instanceof EntityPlayerMP) {
            event.getEntityLiving().field_70128_L = true;
         }
      }
   }

   @SubscribeEvent
   public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         this.spawners.removeIf((spawner) -> {
            return spawner instanceof PlayerTrackingSpawner && ((PlayerTrackingSpawner)spawner).playerUUID.equals(event.player.func_110124_au());
         });
      }

   }
}

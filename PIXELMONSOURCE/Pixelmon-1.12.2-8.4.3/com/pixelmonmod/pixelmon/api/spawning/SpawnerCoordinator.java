package com.pixelmonmod.pixelmon.api.spawning;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.spawners.TickingSpawner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

public class SpawnerCoordinator {
   public final ArrayList spawners = new ArrayList();
   public static ExecutorService PROCESSOR;
   private boolean active = false;

   public SpawnerCoordinator(AbstractSpawner... spawners) {
      AbstractSpawner[] var2 = spawners;
      int var3 = spawners.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         AbstractSpawner spawner = var2[var4];
         if (spawner != null) {
            this.spawners.add(spawner);
         }
      }

   }

   public static void start() {
      PROCESSOR = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setNameFormat("pixelmon-world-spawner-%d").build());
   }

   @SubscribeEvent
   public void onTick(TickEvent.ServerTickEvent event) {
      if (event.phase == Phase.END && event.side == Side.SERVER && this.active) {
         Iterator var2 = this.spawners.iterator();

         while(var2.hasNext()) {
            AbstractSpawner spawner = (AbstractSpawner)var2.next();
            if (spawner instanceof TickingSpawner && spawner.shouldDoSpawning()) {
               ((TickingSpawner)spawner).doPass(this);
            }
         }
      }

   }

   public boolean getActive() {
      return this.active;
   }

   public SpawnerCoordinator activate() {
      start();
      MinecraftForge.EVENT_BUS.register(this);
      this.active = true;
      return this;
   }

   public void deactivate() {
      PROCESSOR.shutdown();
      this.spawners.clear();
      MinecraftForge.EVENT_BUS.unregister(this);
      this.active = false;
   }

   @Nullable
   public AbstractSpawner getSpawner(String name) {
      Iterator var2 = this.spawners.iterator();

      AbstractSpawner spawner;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         spawner = (AbstractSpawner)var2.next();
      } while(spawner == null || !spawner.name.equalsIgnoreCase(name));

      return spawner;
   }

   static {
      start();
   }
}

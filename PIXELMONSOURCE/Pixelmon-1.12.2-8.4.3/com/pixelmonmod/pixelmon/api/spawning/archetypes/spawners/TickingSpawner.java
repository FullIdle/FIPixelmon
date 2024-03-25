package com.pixelmonmod.pixelmon.api.spawning.archetypes.spawners;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnerCoordinator;
import com.pixelmonmod.pixelmon.api.spawning.calculators.ICalculateSpawnLocations;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.util.Iterator;
import java.util.List;

public abstract class TickingSpawner extends AbstractSpawner {
   public long lastCycleTime = 0L;
   public long nextSpawnTime = 0L;
   public ICalculateSpawnLocations spawnLocationCalculator = ICalculateSpawnLocations.getDefault();
   public int capacity;
   public int spawnsPerPass;
   public float spawnFrequency;
   public float minDistBetweenSpawns;
   private int pass;

   public TickingSpawner(String name) {
      super(name);
      this.capacity = PixelmonConfig.entitiesPerPlayer;
      this.spawnsPerPass = PixelmonConfig.spawnsPerPass;
      this.spawnFrequency = PixelmonConfig.spawnFrequency;
      this.minDistBetweenSpawns = PixelmonConfig.minimumDistanceBetweenSpawns;
      this.pass = -1;
   }

   public abstract List getSpawns(int var1);

   public abstract int getNumPasses();

   public boolean hasCapacity(int numSpawns) {
      if (this.capacity != -1 && PixelmonConfig.maximumSpawnedPokemon >= 0) {
         return numSpawns + this.spawnedTracker.count() <= this.capacity;
      } else {
         return true;
      }
   }

   public boolean canSpawn(SpawnAction action) {
      return true;
   }

   public boolean shouldDoSpawning() {
      return super.shouldDoSpawning() && this.hasCapacity(1) && ((this.pass + 1) % this.getNumPasses() != 0 || System.currentTimeMillis() > this.nextSpawnTime);
   }

   public void onSpawnEnded() {
      this.lastCycleTime = System.currentTimeMillis();
      this.nextSpawnTime = this.lastCycleTime + (long)(60000.0 / (double)this.spawnFrequency);
   }

   public void doPass(SpawnerCoordinator coordinator) {
      List spawns = this.getSpawns(this.pass = (this.pass + 1) % this.getNumPasses());
      if (spawns != null && !spawns.isEmpty()) {
         label25:
         while(true) {
            if (this.hasCapacity(1) && spawns.size() <= this.spawnsPerPass) {
               Iterator var3 = spawns.iterator();

               while(true) {
                  if (!var3.hasNext()) {
                     break label25;
                  }

                  SpawnAction spawn = (SpawnAction)var3.next();
                  spawn.doSpawn(this);
               }
            }

            spawns.remove(RandomHelper.rand.nextInt(spawns.size()));
         }
      }

      this.onSpawnEnded();
   }

   public abstract static class TickingSpawnerBuilder extends AbstractSpawner.SpawnerBuilder {
      protected ICalculateSpawnLocations spawnLocationCalculator = ICalculateSpawnLocations.getDefault();
      protected Integer capacity = null;
      protected Integer spawnsPerPass = null;
      protected Float minDistBetweenSpawns = null;
      protected Float spawnFrequency = null;

      public TickingSpawnerBuilder setSpawnLocationCalculator(ICalculateSpawnLocations spawnLocationCalculator) {
         if (spawnLocationCalculator != null) {
            this.spawnLocationCalculator = spawnLocationCalculator;
         }

         return (TickingSpawnerBuilder)this.getThis();
      }

      public TickingSpawnerBuilder setSpawnFrequency(float spawnFrequency) {
         if (spawnFrequency == 0.0F) {
            spawnFrequency = 1.0E-5F;
         }

         this.spawnFrequency = spawnFrequency;
         return (TickingSpawnerBuilder)this.getThis();
      }

      public TickingSpawnerBuilder setCapacity(int capacity) {
         if (capacity < 0 && capacity != -1) {
            capacity = 0;
         }

         this.capacity = capacity;
         return (TickingSpawnerBuilder)this.getThis();
      }

      public TickingSpawnerBuilder setDistanceBetweenSpawns(float minDistBetweenSpawns) {
         if (minDistBetweenSpawns < 0.0F) {
            minDistBetweenSpawns = 0.0F;
         }

         this.minDistBetweenSpawns = minDistBetweenSpawns;
         return (TickingSpawnerBuilder)this.getThis();
      }

      public TickingSpawnerBuilder setSpawnsPerPass(int spawnsPerPass) {
         if (spawnsPerPass < 0) {
            spawnsPerPass = 0;
         }

         this.spawnsPerPass = spawnsPerPass;
         return (TickingSpawnerBuilder)this.getThis();
      }

      public TickingSpawner apply(TickingSpawner spawner) {
         spawner.spawnLocationCalculator = this.spawnLocationCalculator;
         spawner.capacity = this.capacity != null ? this.capacity : PixelmonConfig.entitiesPerPlayer;
         spawner.spawnsPerPass = this.spawnsPerPass != null ? this.spawnsPerPass : PixelmonConfig.spawnsPerPass;
         spawner.spawnFrequency = this.spawnFrequency != null ? this.spawnFrequency : PixelmonConfig.spawnFrequency;
         spawner.minDistBetweenSpawns = this.minDistBetweenSpawns != null ? this.minDistBetweenSpawns : PixelmonConfig.minimumDistanceBetweenSpawns;
         super.apply(spawner);
         return spawner;
      }
   }
}

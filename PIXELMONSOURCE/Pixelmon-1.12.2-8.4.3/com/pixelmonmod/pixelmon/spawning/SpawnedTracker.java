package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class SpawnedTracker {
   private final AbstractSpawner spawner;
   private final Map spawnedData = new ConcurrentHashMap();

   public SpawnedTracker(AbstractSpawner spawner) {
      this.spawner = spawner;
   }

   public SpawnedData get(UUID uuid) {
      return (SpawnedData)this.spawnedData.get(uuid);
   }

   public void add(SpawnedData data) {
      this.spawnedData.put(data.getEntityId(), data);
   }

   public boolean remove(SpawnedData data) {
      return this.spawnedData.remove(data.getEntityId()) != null;
   }

   public boolean remove(UUID uuid) {
      return this.spawnedData.remove(uuid) != null;
   }

   public boolean contains(UUID uuid) {
      return this.spawnedData.containsKey(uuid);
   }

   public boolean contains(SpawnedData data) {
      return this.spawnedData.containsKey(data.getEntityId());
   }

   public int count() {
      return this.spawnedData.size();
   }

   public void addEntity(Entity entity) {
      this.add(new SpawnedData(entity));
   }

   public boolean removeEntity(Entity entity) {
      return this.remove(entity.func_110124_au());
   }

   public boolean containsEntity(Entity entity) {
      return this.spawnedData.containsKey(entity.func_110124_au());
   }

   public void removeNonExistent() {
      this.spawnedData.values().removeIf((data) -> {
         return !data.isStillAlive();
      });
   }

   public int countAllFlyingPoke() {
      int flying = 0;
      Iterator var2 = this.spawnedData.values().iterator();

      while(var2.hasNext()) {
         SpawnedData data = (SpawnedData)var2.next();
         Entity entity = data.getEntity();
         if (entity instanceof EntityPixelmon) {
            EntityPixelmon pixelmon = (EntityPixelmon)entity;
            if (pixelmon.getBaseStats().getSpawnLocations().stream().anyMatch((it) -> {
               return it == SpawnLocationType.Air || it == SpawnLocationType.AirPersistent;
            })) {
               ++flying;
            }
         }
      }

      return flying;
   }

   public static class SpawnedData {
      private final UUID entityUUID;
      private final int dim;
      private final BlockPos location;

      public SpawnedData(Entity entity) {
         this.entityUUID = entity.func_110124_au();
         this.dim = entity.field_71093_bK;
         this.location = entity.func_180425_c();
      }

      public SpawnedData(UUID uuid, int dim, BlockPos location) {
         this.entityUUID = uuid;
         this.dim = dim;
         this.location = location;
      }

      public UUID getEntityId() {
         return this.entityUUID;
      }

      public BlockPos getSpawnLocation() {
         return this.location;
      }

      @Nullable
      public WorldServer getWorld() {
         return DimensionManager.getWorld(this.dim);
      }

      @Nullable
      public Entity getEntity() {
         return this.getWorld() != null ? this.getWorld().func_175733_a(this.getEntityId()) : null;
      }

      public boolean isStillAlive() {
         return this.getEntity() != null;
      }
   }
}

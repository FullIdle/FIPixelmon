package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnerCoordinator;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.spawners.TickingSpawner;
import com.pixelmonmod.pixelmon.api.world.BlockCollection;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PlayerTrackingSpawner extends TickingSpawner {
   public final UUID playerUUID;
   public PlayerPartyStorage playerStorage;
   public Long lastStorageCheck = 0L;
   protected List possibleSpawns = new ArrayList();
   public int minDistFromCentre;
   public int maxDistFromCentre;
   public float horizontalTrackFactor;
   public float verticalTrackFactor;
   public int horizontalSliceRadius;
   public int verticalSliceRadius;

   public PlayerTrackingSpawner(EntityPlayerMP player) {
      super(player.func_70005_c_());
      this.minDistFromCentre = PixelmonConfig.minimumDistanceFromCentre;
      this.maxDistFromCentre = PixelmonConfig.maximumDistanceFromCentre;
      this.horizontalTrackFactor = PixelmonConfig.horizontalTrackFactor;
      this.verticalTrackFactor = PixelmonConfig.verticalTrackFactor;
      this.horizontalSliceRadius = PixelmonConfig.horizontalSliceRadius;
      this.verticalSliceRadius = PixelmonConfig.verticalSliceRadius;
      this.playerUUID = player.func_110124_au();
   }

   public int getNumPasses() {
      return 2;
   }

   @Nullable
   public EntityPlayerMP getTrackedPlayer() {
      return FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.playerUUID);
   }

   public boolean hasCapacity(int numSpawns) {
      if (!super.hasCapacity(numSpawns)) {
         return false;
      } else {
         int effectiveCapacity = this.capacity;
         int playerSpawnerCount = 0;
         Iterator var4 = PixelmonSpawning.coordinator.spawners.iterator();

         while(var4.hasNext()) {
            AbstractSpawner spawner = (AbstractSpawner)var4.next();
            if (spawner instanceof PlayerTrackingSpawner) {
               ++playerSpawnerCount;
            }
         }

         if (playerSpawnerCount == 0) {
            return true;
         } else {
            if (PixelmonConfig.maximumSpawnedPokemon / playerSpawnerCount < effectiveCapacity) {
               effectiveCapacity = Math.min(effectiveCapacity, PixelmonConfig.maximumSpawnedPokemon / playerSpawnerCount);
            }

            return numSpawns + this.spawnedTracker.count() <= effectiveCapacity;
         }
      }
   }

   public boolean canSpawn(SpawnAction action) {
      EntityPlayerMP playerByUUID = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.playerUUID);
      if (playerByUUID != null && !playerByUUID.field_70128_L && !playerByUUID.func_193105_t()) {
         if (action instanceof SpawnActionPokemon) {
            EntityPixelmon pixelmon = (EntityPixelmon)((SpawnActionPokemon)action).getOrCreateEntity();
            if (pixelmon == null) {
               Pixelmon.LOGGER.error("Invalid spawnset: " + action);
               return false;
            }

            if (action.spawnInfo.stringLocationTypes != null && action.spawnInfo.stringLocationTypes.contains("Air") || pixelmon.getBaseStats() != null && pixelmon.getBaseStats().getSpawnLocations() != null && pixelmon.getBaseStats().getSpawnLocations().stream().anyMatch((it) -> {
               return it == SpawnLocationType.Air || it == SpawnLocationType.AirPersistent;
            })) {
               int playerFlyingPokes = this.spawnedTracker.countAllFlyingPoke();
               long maxFlying = (long)PixelmonConfig.maximumSpawnedFlyingPokemonPerPlayer;
               long playerSpawnerCount = PixelmonSpawning.coordinator.spawners.stream().filter((it) -> {
                  return it instanceof PlayerTrackingSpawner;
               }).count();
               if (playerSpawnerCount == 0L) {
                  playerSpawnerCount = 1L;
               }

               maxFlying = Math.min(maxFlying, (long)PixelmonConfig.maximumSpawnedFlyingPokemon / playerSpawnerCount);
               return (long)(playerFlyingPokes + 1) <= maxFlying;
            }
         }

         return super.canSpawn(action);
      } else {
         return false;
      }
   }

   public boolean shouldDoSpawning() {
      return super.shouldDoSpawning();
   }

   public boolean isTrackedPlayerOnline() {
      return this.getTrackedPlayer() != null;
   }

   public List getSpawns(int pass) {
      if (pass != 0) {
         if (this.possibleSpawns != null && !this.possibleSpawns.isEmpty()) {
            this.possibleSpawns.removeIf((action) -> {
               action.applyLocationMutations();
               if (!this.canSpawn(action)) {
                  return true;
               } else {
                  return !action.spawnLocation.location.world.func_175644_a(Entity.class, (entity) -> {
                     return entity.func_180425_c().func_185332_f(action.spawnLocation.location.pos.func_177958_n(), action.spawnLocation.location.pos.func_177956_o(), action.spawnLocation.location.pos.func_177952_p()) < (double)this.minDistBetweenSpawns;
                  }).isEmpty();
               }
            });
            return this.possibleSpawns;
         } else {
            return null;
         }
      } else {
         this.possibleSpawns = new ArrayList();
         EntityPlayerMP player = this.getTrackedPlayer();
         if (player != null) {
            if (player.field_70163_u < 0.0 || player.field_70163_u > 255.0) {
               return null;
            }

            if (this.playerStorage == null) {
               this.playerStorage = Pixelmon.storageManager.getParty(player);
               if (this.playerStorage == null) {
                  return null;
               }
            }

            if (this.lastStorageCheck < System.currentTimeMillis() + 10000L) {
               if (this.playerStorage != null) {
                  this.playerStorage.updatePartyCache();
               }

               this.lastStorageCheck = System.currentTimeMillis();
               this.spawnedTracker.removeNonExistent();
            }

            BlockCollection collection = this.getTrackedBlockCollection(player, this.horizontalTrackFactor, this.verticalTrackFactor, this.horizontalSliceRadius, this.verticalSliceRadius, this.minDistFromCentre, this.maxDistFromCentre);
            this.isBusy = true;
            SpawnerCoordinator.PROCESSOR.execute(() -> {
               try {
                  ArrayList spawnLocations = this.spawnLocationCalculator.calculateSpawnableLocations(collection);
                  Collections.shuffle(spawnLocations);
                  this.possibleSpawns = this.selectionAlgorithm.calculateSpawnActions(this, this.spawnSets, spawnLocations);
               } finally {
                  this.isBusy = false;
               }

            });
         }

         return null;
      }
   }

   public static class PlayerTrackingSpawnerBuilder extends TickingSpawner.TickingSpawnerBuilder {
      public Integer minDistFromCentre = null;
      public Integer maxDistFromCentre = null;
      public Float horizontalTrackFactor = null;
      public Float verticalTrackFactor = null;
      public Integer horizontalSliceRadius = null;
      public Integer verticalSliceRadius = null;

      public PlayerTrackingSpawnerBuilder setDistanceFromCentre(int minimum, int maximum) {
         if (minimum < 0) {
            minimum = 0;
         }

         this.minDistFromCentre = minimum;
         if (maximum < 0) {
            maximum = 0;
         }

         this.maxDistFromCentre = maximum;
         return (PlayerTrackingSpawnerBuilder)this.getThis();
      }

      public PlayerTrackingSpawnerBuilder setTrackFactors(float horizontal, float vertical) {
         this.horizontalTrackFactor = horizontal;
         this.verticalTrackFactor = vertical;
         return (PlayerTrackingSpawnerBuilder)this.getThis();
      }

      public PlayerTrackingSpawnerBuilder setSliceRadii(int horizontal, int vertical) {
         if (horizontal < 1) {
            horizontal = 1;
         }

         this.horizontalSliceRadius = horizontal;
         if (vertical < 1) {
            vertical = 1;
         }

         this.verticalSliceRadius = vertical;
         return (PlayerTrackingSpawnerBuilder)this.getThis();
      }

      public PlayerTrackingSpawner apply(PlayerTrackingSpawner spawner) {
         spawner.minDistFromCentre = this.minDistFromCentre != null ? this.minDistFromCentre : PixelmonConfig.minimumDistanceFromCentre;
         spawner.maxDistFromCentre = this.maxDistFromCentre != null ? this.maxDistFromCentre : PixelmonConfig.maximumDistanceFromCentre;
         spawner.verticalTrackFactor = this.verticalTrackFactor != null ? this.verticalTrackFactor : PixelmonConfig.verticalTrackFactor;
         spawner.horizontalTrackFactor = this.horizontalTrackFactor != null ? this.horizontalTrackFactor : PixelmonConfig.horizontalTrackFactor;
         spawner.verticalSliceRadius = this.verticalSliceRadius != null ? this.verticalSliceRadius : PixelmonConfig.verticalSliceRadius;
         spawner.horizontalSliceRadius = this.horizontalSliceRadius != null ? this.horizontalSliceRadius : PixelmonConfig.horizontalSliceRadius;
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(spawner.playerUUID);
         spawner.rarityTweaks.add(party);
         super.apply((TickingSpawner)spawner);
         return spawner;
      }
   }
}

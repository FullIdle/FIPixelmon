package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.spawning.LegendarySpawnEvent;
import com.pixelmonmod.pixelmon.api.spawning.SpawnerCoordinator;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.spawners.TickingSpawner;
import com.pixelmonmod.pixelmon.api.world.BlockCollection;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class LegendarySpawner extends TickingSpawner {
   public int minDistFromCentre;
   public int maxDistFromCentre;
   public int horizontalSliceRadius;
   public int verticalSliceRadius;
   public List possibleSpawns;
   public boolean firesChooseEvent;

   public LegendarySpawner(String name) {
      super(name);
      this.minDistFromCentre = PixelmonConfig.minimumDistanceFromCentre;
      this.maxDistFromCentre = PixelmonConfig.maximumDistanceFromCentre;
      this.horizontalSliceRadius = PixelmonConfig.horizontalSliceRadius;
      this.verticalSliceRadius = PixelmonConfig.verticalSliceRadius;
      this.possibleSpawns = null;
      this.firesChooseEvent = true;
   }

   public static void fillNearby(ArrayList allPlayers, ArrayList cluster, EntityPlayerMP focus) {
      for(int i = 0; i < allPlayers.size(); ++i) {
         if (allPlayers.get(i) != focus && !cluster.contains(allPlayers.get(i)) && focus.func_70032_d((Entity)allPlayers.get(i)) < 50.0F) {
            EntityPlayerMP newFocus = (EntityPlayerMP)allPlayers.get(i);
            cluster.add(newFocus);
            allPlayers.remove(newFocus);
            fillNearby(allPlayers, cluster, newFocus);
         }
      }

   }

   public void forcefullySpawn(@Nullable EntityPlayerMP onlyFocus) {
      ArrayList clusters = new ArrayList();
      ArrayList players = new ArrayList(FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_181057_v());
      if (onlyFocus == null) {
         while(!players.isEmpty()) {
            ArrayList cluster = new ArrayList();
            EntityPlayerMP focus = (EntityPlayerMP)players.remove(0);
            cluster.add(focus);
            fillNearby(players, cluster, focus);
            clusters.add(cluster);
         }
      }

      this.isBusy = true;
      SpawnerCoordinator.PROCESSOR.execute(() -> {
         if (onlyFocus != null) {
            this.possibleSpawns = this.doLegendarySpawn(onlyFocus);
         } else {
            Collections.shuffle(clusters);

            while(clusters.size() > 0) {
               for(int i = 0; i < clusters.size(); ++i) {
                  EntityPlayerMP player = (EntityPlayerMP)CollectionHelper.getRandomElement((List)clusters.get(i));
                  ((ArrayList)clusters.get(i)).remove(player);
                  if (((ArrayList)clusters.get(i)).isEmpty()) {
                     clusters.remove(i--);
                  }

                  if (this.firesChooseEvent) {
                     LegendarySpawnEvent.ChoosePlayer event = new LegendarySpawnEvent.ChoosePlayer(this, player, clusters);
                     if (Pixelmon.EVENT_BUS.post(event) || event.player == null) {
                        continue;
                     }

                     player = event.player;
                  }

                  this.possibleSpawns = this.doLegendarySpawn(player);
                  if (this.possibleSpawns != null) {
                     this.isBusy = false;
                     return;
                  }
               }
            }
         }

         this.isBusy = false;
      });
   }

   public List getSpawns(int pass) {
      if (pass == 0) {
         this.possibleSpawns = null;
         int numPlayers = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_72394_k();
         int baseSpawnTicks = this.firesChooseEvent ? PixelmonConfig.legendarySpawnTicks : PixelmonConfig.bossSpawnTicks;
         int spawnTicks = (int)((float)baseSpawnTicks / (1.0F + (float)(numPlayers - 1) * PixelmonConfig.spawnTicksPlayerMultiplier));
         this.spawnFrequency = RandomHelper.getRandomNumberBetween(0.6F, 1.4F) * (1200.0F / (float)spawnTicks);
         if (this.firesChooseEvent && !RandomHelper.getRandomChance(PixelmonConfig.getLegendaryRate()) || !this.firesChooseEvent && !RandomHelper.getRandomChance(PixelmonConfig.bossSpawnChance)) {
            return null;
         } else {
            if (FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_72394_k() > 0) {
               this.forcefullySpawn((EntityPlayerMP)null);
            }

            return null;
         }
      } else {
         return this.possibleSpawns != null && !this.possibleSpawns.isEmpty() ? this.possibleSpawns : null;
      }
   }

   public List doLegendarySpawn(EntityPlayerMP target) {
      BlockCollection collection = this.getTrackedBlockCollection(target, 0.0F, 0.0F, this.horizontalSliceRadius, this.verticalSliceRadius, this.minDistFromCentre, this.maxDistFromCentre);
      ArrayList spawnLocations = this.spawnLocationCalculator.calculateSpawnableLocations(collection);
      Collections.shuffle(spawnLocations);
      List possibleSpawns = this.selectionAlgorithm.calculateSpawnActions(this, this.spawnSets, spawnLocations);
      if (possibleSpawns != null && !possibleSpawns.isEmpty()) {
         possibleSpawns.forEach((spawnAction) -> {
            spawnAction.applyLocationMutations();
         });
         return possibleSpawns;
      } else {
         return null;
      }
   }

   public int getNumPasses() {
      return 2;
   }

   public static class LegendarySpawnerBuilder extends TickingSpawner.TickingSpawnerBuilder {
      protected Integer minDistFromCentre = null;
      protected Integer maxDistFromCentre = null;
      protected Integer horizontalSliceRadius = null;
      protected Integer verticalSliceRadius = null;
      protected boolean firesChooseEvent = true;

      public LegendarySpawnerBuilder setDistanceFromCentre(int minimum, int maximum) {
         if (minimum < 0) {
            minimum = 0;
         }

         this.minDistFromCentre = minimum;
         if (maximum < 0) {
            maximum = 0;
         }

         this.maxDistFromCentre = maximum;
         return (LegendarySpawnerBuilder)this.getThis();
      }

      public LegendarySpawnerBuilder setSliceRadii(int horizontal, int vertical) {
         if (horizontal < 1) {
            horizontal = 1;
         }

         this.horizontalSliceRadius = horizontal;
         if (vertical < 1) {
            vertical = 1;
         }

         this.verticalSliceRadius = vertical;
         return (LegendarySpawnerBuilder)this.getThis();
      }

      public LegendarySpawnerBuilder setFiresChooseEvent(boolean firesChooseEvent) {
         this.firesChooseEvent = firesChooseEvent;
         return (LegendarySpawnerBuilder)this.getThis();
      }

      public LegendarySpawner apply(LegendarySpawner spawner) {
         super.apply((TickingSpawner)spawner);
         spawner.minDistFromCentre = this.minDistFromCentre != null ? this.minDistFromCentre : PixelmonConfig.minimumDistanceFromCentre;
         spawner.maxDistFromCentre = this.maxDistFromCentre != null ? this.maxDistFromCentre : PixelmonConfig.maximumDistanceFromCentre;
         spawner.verticalSliceRadius = this.verticalSliceRadius != null ? this.verticalSliceRadius : PixelmonConfig.verticalSliceRadius;
         spawner.horizontalSliceRadius = this.horizontalSliceRadius != null ? this.horizontalSliceRadius : PixelmonConfig.horizontalSliceRadius;
         spawner.firesChooseEvent = this.firesChooseEvent;
         spawner.onSpawnEnded();
         return spawner;
      }
   }
}

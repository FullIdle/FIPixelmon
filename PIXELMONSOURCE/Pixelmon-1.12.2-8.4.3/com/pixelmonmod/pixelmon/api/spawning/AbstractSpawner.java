package com.pixelmonmod.pixelmon.api.spawning;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.spawning.CreateSpawnerEvent;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.selection.LocationWeightedAlgorithm;
import com.pixelmonmod.pixelmon.api.spawning.calculators.ICheckSpawns;
import com.pixelmonmod.pixelmon.api.spawning.calculators.ISelectionAlgorithm;
import com.pixelmonmod.pixelmon.api.spawning.conditions.RarityMultiplier;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import com.pixelmonmod.pixelmon.api.spawning.util.SpatialData;
import com.pixelmonmod.pixelmon.api.world.BlockCollection;
import com.pixelmonmod.pixelmon.spawning.SpawnedTracker;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public abstract class AbstractSpawner {
   public final String name;
   public final SpawnedTracker spawnedTracker = new SpawnedTracker(this);
   public ISelectionAlgorithm selectionAlgorithm = new LocationWeightedAlgorithm();
   public ICheckSpawns checkSpawns = ICheckSpawns.getDefault();
   public List tweaks = new ArrayList();
   public List conditions = new ArrayList();
   public List rarityTweaks = new ArrayList();
   public List rarityMultipliers = new ArrayList();
   public volatile boolean isBusy = false;
   public long lastSpawnTime;
   public List spawnSets = new ArrayList();
   public transient HashMap cacheSets = null;

   public AbstractSpawner(String name) {
      this.name = name;
   }

   public boolean shouldDoSpawning() {
      return !this.isBusy;
   }

   public ArrayList getSuitableSpawns(SpawnLocation spawnLocation) {
      ArrayList suitableSpawns = new ArrayList();
      if (this.cacheSets != null) {
         if (!this.cacheSets.containsKey(spawnLocation.biome)) {
            return new ArrayList();
         }

         List spawnInfos = (List)this.cacheSets.get(spawnLocation.biome);
         if (spawnInfos == null || spawnInfos.isEmpty()) {
            return new ArrayList();
         }

         for(int i = 0; i < spawnInfos.size(); ++i) {
            if (((SpawnInfo)spawnInfos.get(i)).fits(this, spawnLocation)) {
               suitableSpawns.add(spawnInfos.get(i));
            }
         }
      } else if (this.spawnSets != null && !this.spawnSets.isEmpty()) {
         for(int i = 0; i < this.spawnSets.size(); ++i) {
            suitableSpawns.addAll(((SpawnSet)this.spawnSets.get(i)).suitableSpawnsFor(this, spawnLocation));
         }
      }

      return suitableSpawns;
   }

   public boolean fits(SpawnInfo spawnInfo, SpawnLocation spawnLocation) {
      if (this.conditions != null && !this.conditions.isEmpty()) {
         for(int i = 0; i < this.conditions.size(); ++i) {
            if (!((ISpawnerCondition)this.conditions.get(i)).fits(this, spawnInfo, spawnLocation)) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   @Nullable
   public SpawnInfo getWeightedSpawnInfo(SpawnLocation spawnLocation) {
      ArrayList suitableSpawns = this.getSuitableSpawns(spawnLocation);
      return suitableSpawns.isEmpty() ? null : this.selectionAlgorithm.choose(this, spawnLocation, suitableSpawns);
   }

   public SpatialData calculateSpatialData(World world, BlockPos centre, int max, boolean includeDownwards, Predicate condition) {
      IBlockState state = world.func_180495_p(centre);
      if (!condition.test(state.func_177230_c())) {
         return new SpatialData(0, world.func_180495_p(centre.func_177977_b()).func_177230_c(), Sets.newHashSet(new Block[]{state.func_177230_c()}));
      } else {
         int r = 1;
         int[] xs = new int[]{-1, 1};
         int[] ys = includeDownwards ? new int[]{-1, 1} : new int[]{1};
         int[] zs = new int[]{-1, 1};

         int xOffset;
         int yOffset;
         int xMul;
         int y;
         int z;
         label78:
         for(int radius = r; radius <= max; r = radius++) {
            int[] var12 = xs;
            xOffset = xs.length;

            for(yOffset = 0; yOffset < xOffset; ++yOffset) {
               xMul = var12[yOffset];
               int[] var16 = zs;
               y = zs.length;

               for(z = 0; z < y; ++z) {
                  int zMul = var16[z];
                  int[] var20 = ys;
                  int var21 = ys.length;

                  for(int var22 = 0; var22 < var21; ++var22) {
                     int yMul = var20[var22];
                     state = world.func_180495_p(centre.func_177982_a(xMul * r, yMul * r, zMul * r));
                     if (!condition.test(state.func_177230_c())) {
                        r = radius;
                        break label78;
                     }
                  }
               }
            }
         }

         BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(0, 0, 0);
         Set uniqueSurroundingBlocks = Sets.newHashSet();

         for(xOffset = -r; xOffset <= r; ++xOffset) {
            for(yOffset = -r; yOffset <= r; ++yOffset) {
               for(xMul = -r; xMul <= r; ++xMul) {
                  int x = xOffset + centre.func_177958_n();
                  y = yOffset + centre.func_177956_o();
                  z = xMul + centre.func_177952_p();
                  Block block = world.func_180495_p(pos.func_181079_c(x, y, z)).func_177230_c();
                  if (!uniqueSurroundingBlocks.contains(block)) {
                     uniqueSurroundingBlocks.add(block);
                  }
               }
            }
         }

         return new SpatialData(r, world.func_180495_p(centre.func_177977_b()).func_177230_c(), uniqueSurroundingBlocks);
      }
   }

   public BlockCollection getTrackedBlockCollection(Entity entity, float horizontalTrackFactor, float verticalTrackFactor, int horizontalSliceRadius, int verticalSliceRadius, int minDistFromCentre, int maxDistFromCentre) {
      BlockPos centre = entity.func_180425_c().func_177963_a((double)horizontalTrackFactor * entity.field_70159_w, (double)verticalTrackFactor * entity.field_70181_x, (double)horizontalTrackFactor * entity.field_70179_y);
      float theta = RandomHelper.getRandomNumberBetween(0.0F, 6.2831855F);
      int r = RandomHelper.getRandomNumberBetween(minDistFromCentre, maxDistFromCentre);
      int xDisplacement = (int)((double)r * Math.cos((double)theta));
      int zDisplacement = (int)((double)r * Math.sin((double)theta));
      centre = centre.func_177982_a(xDisplacement, 0, zDisplacement);
      return new BlockCollection(entity, entity.field_70170_p, centre.func_177958_n() - horizontalSliceRadius, centre.func_177958_n() + horizontalSliceRadius, centre.func_177956_o() - verticalSliceRadius, centre.func_177956_o() + verticalSliceRadius, centre.func_177952_p() - horizontalSliceRadius, centre.func_177952_p() + horizontalSliceRadius);
   }

   public boolean hasTweak(Class clazz) {
      return CollectionHelper.containsA(this.tweaks, clazz);
   }

   @Nullable
   public ISpawningTweak getTweak(Class clazz) {
      return (ISpawningTweak)CollectionHelper.getFirst(this.tweaks, clazz);
   }

   public boolean hasCondition(Class clazz) {
      return CollectionHelper.containsA(this.conditions, clazz);
   }

   @Nullable
   public ISpawnerCondition getCondition(Class clazz) {
      return (ISpawnerCondition)CollectionHelper.getFirst(this.conditions, clazz);
   }

   public static class SpawnerBuilder {
      public HashMap cacheMap = new HashMap();
      public List spawnSets = new ArrayList();
      public ISelectionAlgorithm selectionAlgorithm = new LocationWeightedAlgorithm();
      public ICheckSpawns checkSpawns = ICheckSpawns.getDefault();
      public List tweaks = new ArrayList();
      public List conditions = new ArrayList();
      public List rarityMultipliers = new ArrayList();

      public SpawnerBuilder addSpawnSets(SpawnSet... spawnSets) {
         SpawnSet[] var2 = spawnSets;
         int var3 = spawnSets.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SpawnSet spawnSet = var2[var4];
            this.spawnSets.add(spawnSet);
         }

         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder addSpawnSets(Collection spawnSets) {
         this.spawnSets.addAll(spawnSets);
         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder setSpawnSets(SpawnSet... spawnSets) {
         this.spawnSets.clear();
         return this.addSpawnSets(spawnSets);
      }

      public SpawnerBuilder setSpawnSets(List spawnSets) {
         this.spawnSets = spawnSets;
         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder setupCache() {
         this.cacheMap.clear();
         Iterator var1 = this.spawnSets.iterator();

         while(var1.hasNext()) {
            SpawnSet set = (SpawnSet)var1.next();
            Iterator var3 = set.iterator();

            while(var3.hasNext()) {
               SpawnInfo info = (SpawnInfo)var3.next();
               ArrayList biomes = new ArrayList();
               Biome.field_185377_q.forEach(biomes::add);
               if (info.condition != null && info.condition.biomes != null && !info.condition.biomes.isEmpty()) {
                  biomes.removeIf((biomex) -> {
                     return !info.condition.biomes.contains(biomex);
                  });
               }

               Iterator var6;
               if (info.compositeCondition != null && info.compositeCondition.conditions != null) {
                  var6 = info.compositeCondition.conditions.iterator();

                  label64:
                  while(true) {
                     SpawnCondition condition;
                     do {
                        do {
                           if (!var6.hasNext()) {
                              break label64;
                           }

                           condition = (SpawnCondition)var6.next();
                        } while(condition.biomes == null);
                     } while(condition.biomes.isEmpty());

                     Iterator var8 = condition.biomes.iterator();

                     while(var8.hasNext()) {
                        Biome biome = (Biome)var8.next();
                        if (!biomes.contains(biome)) {
                           biomes.add(biome);
                        }
                     }
                  }
               }

               Biome biome;
               for(var6 = biomes.iterator(); var6.hasNext(); ((List)this.cacheMap.get(biome)).add(info)) {
                  biome = (Biome)var6.next();
                  if (!this.cacheMap.containsKey(biome)) {
                     this.cacheMap.put(biome, new ArrayList());
                  }
               }
            }
         }

         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder setSelectionAlgorithm(ISelectionAlgorithm selectionAlgorithm) {
         this.selectionAlgorithm = selectionAlgorithm;
         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder setCheckSpawns(ICheckSpawns checkSpawns) {
         this.checkSpawns = checkSpawns;
         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder addTweak(ISpawningTweak tweak) {
         this.tweaks.add(tweak);
         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder setTweaks(List tweaks) {
         this.tweaks = tweaks;
         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder addCondition(ISpawnerCondition condition) {
         this.conditions.add(condition);
         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder setConditions(List conditions) {
         this.conditions = conditions;
         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder addRarityMultiplier(RarityMultiplier rarityMultiplier) {
         this.rarityMultipliers.add(rarityMultiplier);
         return (SpawnerBuilder)this.getThis();
      }

      public SpawnerBuilder setRarityMultipliers(List rarityMultipliers) {
         this.rarityMultipliers = rarityMultipliers;
         return (SpawnerBuilder)this.getThis();
      }

      public AbstractSpawner apply(AbstractSpawner spawner) {
         spawner.spawnSets = this.spawnSets;
         spawner.selectionAlgorithm = this.selectionAlgorithm;
         spawner.checkSpawns = this.checkSpawns;
         spawner.tweaks = new ArrayList(this.tweaks);
         spawner.conditions = this.conditions;
         spawner.rarityMultipliers = this.rarityMultipliers;
         if (this.cacheMap != null && !this.cacheMap.isEmpty()) {
            spawner.cacheSets = this.cacheMap;
         }

         Pixelmon.EVENT_BUS.post(new CreateSpawnerEvent(spawner, this));
         return spawner;
      }

      protected Object getThis() {
         return this;
      }
   }
}

package com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.selection;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.spawners.TickingSpawner;
import com.pixelmonmod.pixelmon.api.spawning.calculators.ISelectionAlgorithm;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.util.math.BlockPos;

public class LocationWeightedAlgorithm implements ISelectionAlgorithm {
   public List calculateSpawnActions(AbstractSpawner spawner, List spawnSets, List spawnLocations) {
      TickingSpawner tickingSpawner = spawner instanceof TickingSpawner ? (TickingSpawner)spawner : null;
      ArrayList toSpawn = new ArrayList();
      HashMap potentialSpawns = new HashMap();
      Iterator var7 = spawnLocations.iterator();

      while(var7.hasNext()) {
         SpawnLocation spawnLocation = (SpawnLocation)var7.next();
         SpawnInfo weightedSpawnInfo = spawner.getWeightedSpawnInfo(spawnLocation);
         if (weightedSpawnInfo != null) {
            potentialSpawns.put(spawnLocation, weightedSpawnInfo);
         }
      }

      BlockPos.MutableBlockPos pos;
      SpawnLocation location;
      for(; !potentialSpawns.isEmpty() && (tickingSpawner == null || tickingSpawner.hasCapacity(toSpawn.size() + 1) && toSpawn.size() <= tickingSpawner.spawnsPerPass); potentialSpawns.keySet().removeIf((spawnLocationx) -> {
         if (tickingSpawner == null) {
            return false;
         } else if (spawnLocationx.location.world != location.location.world) {
            return false;
         } else {
            return !(spawnLocationx.location.pos.func_185332_f(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()) >= (double)tickingSpawner.minDistBetweenSpawns);
         }
      })) {
         location = (SpawnLocation)CollectionHelper.getRandomElement((List)Lists.newArrayList(potentialSpawns.keySet()));
         SpawnInfo spawnInfo = (SpawnInfo)potentialSpawns.get(location);
         potentialSpawns.remove(location);
         SpawnAction action = spawnInfo.construct(spawner, location);
         pos = location.location.pos;
         if (action != null) {
            toSpawn.add(action);
         }
      }

      return toSpawn;
   }

   public Map getAdjustedRarities(AbstractSpawner spawner, Map possibleSpawns) {
      HashMap adjustedRarities = new HashMap();
      Iterator var4 = possibleSpawns.keySet().iterator();

      label31:
      while(var4.hasNext()) {
         SpawnLocation spawnLocation = (SpawnLocation)var4.next();
         Iterator var6 = ((List)possibleSpawns.get(spawnLocation)).iterator();

         while(true) {
            SpawnInfo spawnInfo;
            do {
               if (!var6.hasNext()) {
                  continue label31;
               }

               spawnInfo = (SpawnInfo)var6.next();
            } while(spawnInfo.percentage != null && spawnInfo.percentage > 0.0F);

            float adjusted = spawnInfo.getAdjustedRarity(spawner, spawnLocation);
            if (adjustedRarities.containsKey(spawnInfo)) {
               adjusted += (Float)adjustedRarities.get(spawnInfo);
            }

            adjustedRarities.put(spawnInfo, adjusted);
         }
      }

      return adjustedRarities;
   }
}

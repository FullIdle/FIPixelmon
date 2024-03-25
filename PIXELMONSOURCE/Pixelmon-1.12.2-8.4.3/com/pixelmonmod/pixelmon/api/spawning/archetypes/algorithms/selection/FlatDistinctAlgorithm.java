package com.pixelmonmod.pixelmon.api.spawning.archetypes.algorithms.selection;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.calculators.ISelectionAlgorithm;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FlatDistinctAlgorithm implements ISelectionAlgorithm {
   public List calculateSpawnActions(AbstractSpawner spawner, List spawnSets, List spawnLocations) {
      HashMap distinctSpawns = new HashMap();
      Iterator var5 = spawnLocations.iterator();

      Iterator var8;
      while(var5.hasNext()) {
         SpawnLocation spawnLocation = (SpawnLocation)var5.next();
         ArrayList spawnInfos = spawner.getSuitableSpawns(spawnLocation);
         var8 = spawnInfos.iterator();

         while(var8.hasNext()) {
            SpawnInfo spawnInfo = (SpawnInfo)var8.next();
            distinctSpawns.putIfAbsent(spawnInfo, new ArrayList());
            ((ArrayList)distinctSpawns.get(spawnInfo)).add(spawnLocation);
         }
      }

      SpawnInfo percentageSelection = this.chooseViaPercentage(spawner, new ArrayList(distinctSpawns.keySet()));
      if (percentageSelection != null) {
         return Lists.newArrayList(new SpawnAction[]{percentageSelection.construct(spawner, (SpawnLocation)CollectionHelper.getRandomElement((List)distinctSpawns.get(percentageSelection)))});
      } else {
         HashMap averageRarities = new HashMap();
         float raritySum = 0.0F;
         var8 = distinctSpawns.entrySet().iterator();

         while(var8.hasNext()) {
            Map.Entry entry = (Map.Entry)var8.next();
            float totalRarity = 0.0F;

            SpawnLocation spawnLocation;
            for(Iterator var11 = ((ArrayList)entry.getValue()).iterator(); var11.hasNext(); totalRarity += ((SpawnInfo)entry.getKey()).getAdjustedRarity(spawner, spawnLocation)) {
               spawnLocation = (SpawnLocation)var11.next();
            }

            float averageRarity = totalRarity / (float)((ArrayList)entry.getValue()).size();
            raritySum += averageRarity;
            averageRarities.put(entry.getKey(), averageRarity);
         }

         SpawnInfo chosenSpawn = null;
         float chosenSum = RandomHelper.getRandomNumberBetween(0.0F, raritySum);
         raritySum = 0.0F;
         Iterator var19 = averageRarities.entrySet().iterator();

         while(var19.hasNext()) {
            Map.Entry entry = (Map.Entry)var19.next();
            raritySum += (Float)entry.getValue();
            if (raritySum >= chosenSum && (Float)entry.getValue() > 0.0F) {
               chosenSpawn = (SpawnInfo)entry.getKey();
               break;
            }
         }

         if (chosenSpawn == null) {
            return null;
         } else {
            return Lists.newArrayList(new SpawnAction[]{chosenSpawn.construct(spawner, (SpawnLocation)CollectionHelper.getRandomElement((List)distinctSpawns.get(chosenSpawn)))});
         }
      }
   }

   public Map getAdjustedRarities(AbstractSpawner spawner, Map possibleSpawns) {
      HashMap distinctSpawns = new HashMap();
      Iterator var4 = possibleSpawns.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry entry = (Map.Entry)var4.next();
         Iterator var6 = ((List)entry.getValue()).iterator();

         while(var6.hasNext()) {
            SpawnInfo spawnInfo = (SpawnInfo)var6.next();
            distinctSpawns.putIfAbsent(spawnInfo, new ArrayList());
            ((List)distinctSpawns.get(spawnInfo)).add(entry.getKey());
         }
      }

      HashMap adjustedRarities = new HashMap();
      Iterator var11 = possibleSpawns.keySet().iterator();

      label39:
      while(var11.hasNext()) {
         SpawnLocation spawnLocation = (SpawnLocation)var11.next();
         Iterator var13 = ((List)possibleSpawns.get(spawnLocation)).iterator();

         while(true) {
            SpawnInfo spawnInfo;
            do {
               do {
                  if (!var13.hasNext()) {
                     continue label39;
                  }

                  spawnInfo = (SpawnInfo)var13.next();
               } while(adjustedRarities.containsKey(spawnInfo));
            } while(spawnInfo.percentage != null && spawnInfo.percentage > 0.0F);

            float adjusted = spawnInfo.getAdjustedRarity(spawner, spawnLocation);
            adjustedRarities.put(spawnInfo, adjusted);
         }
      }

      return adjustedRarities;
   }
}

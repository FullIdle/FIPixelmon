package com.pixelmonmod.pixelmon.api.spawning.calculators;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.IRarityTweak;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Level;

public interface ISelectionAlgorithm {
   List calculateSpawnActions(AbstractSpawner var1, List var2, List var3);

   Map getAdjustedRarities(AbstractSpawner var1, Map var2);

   default Map getPercentages(AbstractSpawner spawner, Map possibleSpawns) {
      Map adjustedRarities = this.getAdjustedRarities(spawner, possibleSpawns);
      float raritySum = 0.0F;
      float totalPercentage = 0.0F;
      DecimalFormat df = new DecimalFormat(".####");
      DecimalFormat df2 = new DecimalFormat(".##");
      ArrayList allSpawns = new ArrayList();
      Iterator var9 = possibleSpawns.values().iterator();

      while(var9.hasNext()) {
         List spawns = (List)var9.next();
         Iterator var11 = spawns.iterator();

         while(var11.hasNext()) {
            SpawnInfo spawn = (SpawnInfo)var11.next();
            if (!allSpawns.contains(spawn)) {
               allSpawns.add(spawn);
            }
         }
      }

      var9 = allSpawns.iterator();

      SpawnInfo spawnInfo;
      while(var9.hasNext()) {
         spawnInfo = (SpawnInfo)var9.next();
         if (adjustedRarities.containsKey(spawnInfo)) {
            raritySum += (Float)adjustedRarities.get(spawnInfo);
         }

         if (spawnInfo.percentage != null && spawnInfo.percentage > 0.0F) {
            totalPercentage += spawnInfo.percentage;
         }
      }

      var9 = allSpawns.iterator();

      while(true) {
         do {
            if (!var9.hasNext()) {
               if (totalPercentage > 0.0F) {
                  var9 = adjustedRarities.keySet().iterator();

                  while(var9.hasNext()) {
                     spawnInfo = (SpawnInfo)var9.next();
                     if (spawnInfo.percentage == null) {
                        adjustedRarities.put(spawnInfo, (Float)adjustedRarities.get(spawnInfo) * (100.0F - totalPercentage) / 100.0F);
                     }
                  }
               }

               HashMap percentages = new HashMap();
               Iterator var18 = allSpawns.iterator();

               while(true) {
                  while(var18.hasNext()) {
                     SpawnInfo spawnInfo = (SpawnInfo)var18.next();
                     String label = spawnInfo.toString();
                     double percentage;
                     if (spawnInfo.percentage != null && spawnInfo.percentage > 0.0F) {
                        percentage = (double)spawnInfo.percentage;
                     } else {
                        float adjusted = (Float)adjustedRarities.get(spawnInfo) * 100.0F;
                        percentage = (double)(adjusted / raritySum) + (Double)percentages.getOrDefault(label, 0.0);
                     }

                     if (!(percentage < 0.01) && !(percentage > 99.99)) {
                        percentages.put(label, Double.parseDouble(df2.format(percentage).replace(",", ".")));
                     } else {
                        percentages.put(label, Double.parseDouble(df.format(percentage).replace(",", ".")));
                     }
                  }

                  return percentages;
               }
            }

            spawnInfo = (SpawnInfo)var9.next();
         } while(!adjustedRarities.containsKey(spawnInfo));

         float rarity = (Float)adjustedRarities.get(spawnInfo);

         for(Iterator var21 = spawner.rarityTweaks.iterator(); var21.hasNext(); raritySum += rarity - (Float)adjustedRarities.get(spawnInfo)) {
            IRarityTweak rarityTweak = (IRarityTweak)var21.next();
            rarity *= rarityTweak.getMultiplier(spawner, spawnInfo, raritySum, rarity);
         }

         adjustedRarities.put(spawnInfo, rarity);
      }
   }

   @Nullable
   default SpawnInfo chooseViaPercentage(AbstractSpawner spawner, ArrayList spawnInfos) {
      ArrayList percentageSpawns = new ArrayList();
      float percentSum = 0.0F;
      Iterator var5 = spawnInfos.iterator();

      while(var5.hasNext()) {
         SpawnInfo spawnInfo = (SpawnInfo)var5.next();
         if (spawnInfo.percentage != null && spawnInfo.percentage > 0.0F) {
            percentageSpawns.add(spawnInfo);
            if ((percentSum += spawnInfo.percentage) > 100.0F) {
               Pixelmon.LOGGER.error("A sum of percentages exceeds 100, starting in: " + spawnInfo.set.id);
               return null;
            }
         }
      }

      if (percentSum == 0.0F) {
         return null;
      } else {
         float chosenPercentPoint = RandomHelper.getRandomNumberBetween(0.0F, 100.0F);
         if (chosenPercentPoint > percentSum) {
            return null;
         } else {
            percentSum = 0.0F;
            Iterator var9 = percentageSpawns.iterator();

            SpawnInfo spawnInfo;
            do {
               if (!var9.hasNext()) {
                  Pixelmon.LOGGER.log(Level.WARN, "Unable to choose a SpawnInfo based on rarities during percentage chooser. This shouldn't be possible.");
                  return null;
               }

               spawnInfo = (SpawnInfo)var9.next();
            } while(!((percentSum += spawnInfo.percentage) >= chosenPercentPoint));

            return spawnInfo;
         }
      }
   }

   default SpawnInfo choose(AbstractSpawner spawner, SpawnLocation spawnLocation, ArrayList spawnInfos) {
      if (!spawnInfos.isEmpty() && (spawnInfos.size() != 1 || !(((SpawnInfo)spawnInfos.get(0)).rarity <= 0.0F))) {
         SpawnInfo percentSelection = this.chooseViaPercentage(spawner, spawnInfos);
         if (percentSelection != null) {
            return percentSelection;
         } else if (spawnInfos.size() == 1) {
            return (SpawnInfo)spawnInfos.get(0);
         } else {
            HashMap finalRarities = new HashMap();
            float raritySum = 0.0F;

            int i;
            SpawnInfo spawnInfo;
            for(i = 0; i < spawnInfos.size(); ++i) {
               spawnInfo = (SpawnInfo)spawnInfos.get(i);
               float rarity = spawnInfo.getAdjustedRarity(spawner, spawnLocation);
               finalRarities.put(spawnInfo, rarity);
               raritySum += rarity;
            }

            for(i = 0; i < spawnInfos.size(); ++i) {
               spawnInfo = (SpawnInfo)spawnInfos.get(i);
               Float rarity = (Float)finalRarities.get(spawnInfo);
               Float originalRarity = (Float)finalRarities.get(spawnInfo);

               for(int j = 0; j < spawner.rarityTweaks.size(); ++j) {
                  IRarityTweak rarityTweak = (IRarityTweak)spawner.rarityTweaks.get(j);
                  rarity = rarity * rarityTweak.getMultiplier(spawner, spawnInfo, raritySum, rarity);
                  raritySum += rarity - originalRarity;
               }

               finalRarities.put(spawnInfo, rarity);
            }

            if (raritySum <= 0.0F) {
               return null;
            } else {
               float selected = RandomHelper.getRandomNumberBetween(0.0F, raritySum);
               raritySum = 0.0F;
               Iterator var14 = spawnInfos.iterator();

               SpawnInfo spawnInfo;
               do {
                  if (!var14.hasNext()) {
                     Pixelmon.LOGGER.log(Level.WARN, "Unable to choose a SpawnInfo based on rarities. This shouldn't be possible.");
                     return null;
                  }

                  spawnInfo = (SpawnInfo)var14.next();
               } while(!((raritySum += (Float)finalRarities.get(spawnInfo)) >= selected) || (Float)finalRarities.get(spawnInfo) == 0.0F);

               return spawnInfo;
            }
         }
      } else {
         return null;
      }
   }
}

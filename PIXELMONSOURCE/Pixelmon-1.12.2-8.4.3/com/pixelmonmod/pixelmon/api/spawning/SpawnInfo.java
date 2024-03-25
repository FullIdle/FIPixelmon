package com.pixelmonmod.pixelmon.api.spawning;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.collection.SpawnInfoCollection;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.items.SpawnInfoItem;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs.SpawnInfoNPC;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs.trainers.SpawnInfoNPCTrainer;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.wormholes.SpawnInfoWormhole;
import com.pixelmonmod.pixelmon.api.spawning.conditions.LocationType;
import com.pixelmonmod.pixelmon.api.spawning.conditions.RarityMultiplier;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import com.pixelmonmod.pixelmon.api.spawning.util.SpawnConditionTypeAdapter;
import com.pixelmonmod.pixelmon.api.spawning.util.SpawnInfoTypeAdapter;
import com.pixelmonmod.pixelmon.api.world.WeatherType;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class SpawnInfo {
   public static final Gson GSON;
   public static HashMap spawnInfoTypes;
   public String typeID;
   /** @deprecated */
   @Deprecated
   private String interval = null;
   public ArrayList tags = Lists.newArrayList(new String[]{"default"});
   public SpawnCondition condition = new SpawnCondition();
   public SpawnCondition anticondition = null;
   public CompositeSpawnCondition compositeCondition = null;
   public ArrayList rarityMultipliers = null;
   public int requiredSpace = -1;
   public transient SpawnSet set;
   public float rarity = 0.0F;
   public Float percentage = null;
   public transient ArrayList locationTypes = new ArrayList();
   public ArrayList stringLocationTypes = new ArrayList();

   public SpawnInfo(String typeID) {
      this.typeID = typeID;
   }

   public abstract SpawnAction construct(AbstractSpawner var1, SpawnLocation var2);

   public boolean fits(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      if (spawnLocation.diameter < this.requiredSpace) {
         return false;
      } else if (this.condition != null && !this.condition.fits(this, spawnLocation)) {
         return false;
      } else if (this.anticondition != null && this.anticondition.fits(this, spawnLocation)) {
         return false;
      } else if (this.compositeCondition != null && !this.compositeCondition.fits(this, spawnLocation)) {
         return false;
      } else if (BetterSpawnerConfig.INSTANCE.globalCompositeCondition != null && !BetterSpawnerConfig.INSTANCE.globalCompositeCondition.fits(this, spawnLocation)) {
         return false;
      } else if (!spawner.fits(this, spawnLocation)) {
         return false;
      } else {
         if (this.locationTypes != null && !this.locationTypes.isEmpty()) {
            for(int i = 0; i < this.locationTypes.size(); ++i) {
               if (spawnLocation.types.contains(this.locationTypes.get(i))) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public void onExport() {
      if (this.condition != null) {
         this.condition.onExport();
      }

      if (this.anticondition != null) {
         this.anticondition.onExport();
      }

   }

   public void onImport() {
      if (this.requiredSpace == -1) {
         this.requiredSpace = 1;
      }

      if (this.interval != null && !this.tags.contains(this.interval)) {
         this.tags.add(this.interval);
      }

      if (this.condition != null) {
         this.condition.onImport();
      }

      if (this.anticondition != null) {
         this.anticondition.onImport();
      }

      if (this.rarityMultipliers != null) {
         this.rarityMultipliers.forEach(RarityMultiplier::onImport);
      }

      if (this.compositeCondition != null) {
         this.compositeCondition.onImport();
      }

      if (this.stringLocationTypes != null && !this.stringLocationTypes.isEmpty()) {
         Iterator var1 = this.stringLocationTypes.iterator();

         while(var1.hasNext()) {
            String locationTypeString = (String)var1.next();
            Iterator var3 = LocationType.locationTypes.iterator();

            while(var3.hasNext()) {
               LocationType locationType = (LocationType)var3.next();
               if (locationType.name.equalsIgnoreCase(locationTypeString)) {
                  this.locationTypes.add(locationType);
               }
            }
         }
      }

      if (this.locationTypes.isEmpty()) {
         Pixelmon.LOGGER.warn("A SpawnInfo in set: " + this.set.id + " has no LocationTypes. Was this intentional? I doubt it");
      }

   }

   public float getAdjustedRarity(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      float rarity = this.rarity;
      rarity = this.calculateAdjustment(rarity, spawnLocation, this.rarityMultipliers);
      rarity = this.calculateAdjustment(rarity, spawnLocation, spawner.rarityMultipliers);
      rarity = this.calculateAdjustment(rarity, spawnLocation, BetterSpawnerConfig.INSTANCE.globalRarityMultipliers);
      return rarity;
   }

   private float calculateAdjustment(float rarity, SpawnLocation spawnLocation, List rarityMultiplierList) {
      if (rarityMultiplierList != null && !rarityMultiplierList.isEmpty()) {
         for(int i = 0; i < rarityMultiplierList.size(); ++i) {
            rarity = ((RarityMultiplier)rarityMultiplierList.get(i)).apply(this, spawnLocation, rarity);
         }

         return rarity;
      } else {
         return rarity;
      }
   }

   public abstract String toString();

   public float calculateNominalRarity() {
      if (this.rarity <= 0.0F) {
         return 0.0F;
      } else if (this.condition == null) {
         return 75.0F;
      } else {
         float biomeMultiplier = 1.0F;
         if (this.condition.biomes != null && !this.condition.biomes.isEmpty()) {
            biomeMultiplier = (float)this.condition.biomes.size() / (float)GameRegistry.findRegistry(Biome.class).getEntries().size();
            if (!this.condition.biomes.contains(Biomes.field_76771_b) && !this.condition.biomes.contains(Biomes.field_76781_i)) {
               if (this.condition.biomes.contains(Biomes.field_150575_M)) {
                  biomeMultiplier = 0.5F;
               }
            } else {
               biomeMultiplier = 1.0F;
            }
         }

         float timeMultiplier = 0.0F;
         if (this.condition.times != null && !this.condition.times.isEmpty()) {
            if (this.condition.times.contains(WorldTime.DAY)) {
               timeMultiplier += 0.75F;
            }

            if (this.condition.times.contains(WorldTime.NIGHT)) {
               timeMultiplier += 0.75F;
            }

            if (!this.condition.times.contains(WorldTime.DAY) && !this.condition.times.contains(WorldTime.NIGHT)) {
               if (!this.condition.times.contains(WorldTime.AFTERNOON) && !this.condition.times.contains(WorldTime.MORNING)) {
                  if (!this.condition.times.contains(WorldTime.DAWN) && !this.condition.times.contains(WorldTime.DUSK)) {
                     if (!this.condition.times.contains(WorldTime.MIDNIGHT) && !this.condition.times.contains(WorldTime.MIDDAY)) {
                        return 0.0F;
                     }

                     timeMultiplier = 0.1F;
                  } else {
                     timeMultiplier = 0.3F;
                  }
               } else {
                  timeMultiplier = 0.5F;
               }
            }
         } else {
            timeMultiplier = 1.0F;
         }

         if (timeMultiplier > 1.0F) {
            timeMultiplier = 1.0F;
         }

         float weatherMultiplier = 0.0F;
         if (this.condition.weathers != null && !this.condition.weathers.isEmpty()) {
            if (this.condition.weathers.contains(WeatherType.CLEAR)) {
               weatherMultiplier += 0.7F;
            }

            if (this.condition.weathers.contains(WeatherType.RAIN)) {
               weatherMultiplier += 0.3F;
            }

            if (this.condition.weathers.contains(WeatherType.STORM)) {
               weatherMultiplier += 0.1F;
            }

            if (weatherMultiplier > 1.0F) {
               weatherMultiplier = 1.0F;
            }
         } else {
            weatherMultiplier = 1.0F;
         }

         return this.rarity * weatherMultiplier * timeMultiplier * (0.5F + biomeMultiplier / 2.0F);
      }
   }

   static {
      GSON = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(SpawnInfo.class, new SpawnInfoTypeAdapter()).registerTypeAdapter(SpawnCondition.class, new SpawnConditionTypeAdapter()).registerTypeAdapter(PokemonSpec.class, PokemonSpec.SPEC_ADAPTER).create();
      spawnInfoTypes = new HashMap();
      spawnInfoTypes.put("collection", SpawnInfoCollection.class);
      spawnInfoTypes.put("pokemon", SpawnInfoPokemon.class);
      spawnInfoTypes.put("npc", SpawnInfoNPC.class);
      spawnInfoTypes.put("item", SpawnInfoItem.class);
      spawnInfoTypes.put("trainer", SpawnInfoNPCTrainer.class);
      spawnInfoTypes.put("wormhole", SpawnInfoWormhole.class);
   }
}

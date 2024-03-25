package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.IVEVSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EntityBoundsData;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import com.pixelmonmod.pixelmon.enums.EnumPokerusType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.playerData.CaptureCombo;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.translation.I18n;

public class SpawnInfoPokemon extends SpawnInfo {
   public static final String TYPE_ID_POKEMON = "pokemon";
   public transient EnumSpecies species;
   private PokemonSpec spec = PokemonSpec.from("Psyduck");
   public ArrayList specs = null;
   public int minLevel = 1;
   public int maxLevel = 100;
   public Float spawnSpecificShinyRate = null;
   public Float spawnSpecificBossRate = null;
   public Float spawnSpecificPokerusRate = null;
   public ArrayList heldItems = null;

   public SpawnInfoPokemon() {
      super("pokemon");
   }

   public void onImport() {
      this.calculateRequiredSpace();
      super.onImport();
      if (this.minLevel > this.maxLevel) {
         Pixelmon.LOGGER.warn("A SpawnInfo for " + this.spec.name + " has minLevel=" + this.minLevel + " and maxLevel=" + this.maxLevel + " which is weird. Fixing.");
         int temp = this.minLevel;
         this.minLevel = this.maxLevel;
         this.maxLevel = temp;
      }

      if (this.spec != null) {
         Pokemon dummyPokemon = this.spec.create();
         if (BetterSpawnerConfig.INSTANCE.autoTagSpecs != null) {
            Iterator var2 = BetterSpawnerConfig.INSTANCE.autoTagSpecs.entrySet().iterator();

            while(var2.hasNext()) {
               Map.Entry entry = (Map.Entry)var2.next();
               if (((PokemonSpec)entry.getValue()).matches(dummyPokemon) && !this.tags.contains(entry.getKey())) {
                  this.tags.add(entry.getKey());
               }
            }
         }
      }

   }

   public void setPokemon(PokemonSpec spec) {
      this.spec = spec;
      this.requiredSpace = -1;
      this.calculateRequiredSpace();
   }

   public void calculateRequiredSpace() {
      if (this.spec.name != null && this.requiredSpace == -1) {
         if (EnumSpecies.hasPokemon(this.spec.name)) {
            BaseStats baseStats = EnumSpecies.getFromNameAnyCaseNoTranslate(this.spec.name).getBaseStats();
            EntityBoundsData boundsData = baseStats.getBoundsData();
            this.requiredSpace = (int)Math.ceil(boundsData.getWidth() > boundsData.getHeight() ? boundsData.getWidth() : boundsData.getHeight());
         } else {
            this.requiredSpace = 0;
         }
      }

   }

   public SpawnAction construct(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      PokemonSpec usingSpec;
      PokemonSpec baseSpec;
      if (this.spec == null && this.specs != null && this.specs.size() > 0) {
         usingSpec = (baseSpec = (PokemonSpec)this.specs.get(RandomHelper.getRandomNumberBetween(0, this.specs.size()))).copy();
      } else {
         usingSpec = (baseSpec = this.spec).copy();
      }

      if (usingSpec.name == null) {
         usingSpec.name = EnumSpecies.Psyduck.name;
      } else if (usingSpec.name.equalsIgnoreCase("random")) {
         usingSpec.name = EnumSpecies.randomPoke(false).name;
      } else if (usingSpec.name.equalsIgnoreCase("randomlegendary")) {
         usingSpec.name = ((EnumSpecies)CollectionHelper.getRandomElement(EnumSpecies.legendaries)).name;
      }

      if (usingSpec.level == null) {
         usingSpec.level = RandomHelper.getRandomNumberBetween(Math.min(this.minLevel, PixelmonServerConfig.maxLevel), Math.min(this.maxLevel, PixelmonServerConfig.maxLevel));
      }

      if (usingSpec.shiny == null) {
         if (this.spawnSpecificShinyRate != null) {
            if (this.spawnSpecificShinyRate == 0.0F) {
               usingSpec.shiny = false;
            } else if (this.spawnSpecificShinyRate > 0.0F) {
               usingSpec.shiny = RandomHelper.getRandomChance(1.0F / this.spawnSpecificShinyRate);
            }
         } else if (this.set.setSpecificShinyRate != null) {
            if (this.set.setSpecificShinyRate == 0.0F) {
               usingSpec.shiny = false;
            } else if (this.set.setSpecificShinyRate > 0.0F) {
               usingSpec.shiny = RandomHelper.getRandomChance(1.0F / this.set.setSpecificShinyRate);
            }
         } else if (PixelmonConfig.getShinyRate(spawnLocation.location.world.field_73011_w.getDimension()) > 0.0F) {
            float shinyChanceMultiplier = 1.0F;
            if (spawnLocation.cause instanceof EntityPlayerMP) {
               PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)spawnLocation.cause);
               if (party.getShinyCharm().isActive()) {
                  shinyChanceMultiplier *= 3.0F;
               }

               if (party.getLure() != null && party.getLure().type == ItemLure.LureType.SHINY) {
                  shinyChanceMultiplier *= (float)Math.sqrt((double)party.getLure().strength.multiplier);
               }

               CaptureCombo combo = party.transientData.captureCombo;
               if (combo.getShinyModifier() != 1.0F && combo.getCurrentSpecies() != null && combo.getCurrentSpecies().getPokemonName().equals(usingSpec.name)) {
                  shinyChanceMultiplier *= combo.getShinyModifier();
               }
            }

            usingSpec.shiny = RandomHelper.getRandomChance(shinyChanceMultiplier / PixelmonConfig.getShinyRate(spawnLocation.location.world.field_73011_w.getDimension()));
         } else {
            usingSpec.shiny = false;
         }
      }

      if (usingSpec.boss == null) {
         boolean willBeBoss = false;
         if (this.spawnSpecificBossRate != null && this.spawnSpecificBossRate > 0.0F && RandomHelper.getRandomChance(1.0F / this.spawnSpecificBossRate)) {
            willBeBoss = true;
         } else if (PixelmonConfig.getBossRate(spawnLocation.location.world.field_73011_w.getDimension()) > 0.0F && RandomHelper.getRandomChance(1.0F / PixelmonConfig.getBossRate(spawnLocation.location.world.field_73011_w.getDimension()))) {
            willBeBoss = true;
         }

         if (willBeBoss) {
            EnumBossMode bossMode = null;
            if (PixelmonConfig.bossesAlwaysMegaIfPossible) {
               EnumSpecies species = EnumSpecies.getFromNameAnyCaseNoTranslate(usingSpec.name);
               boolean legendary = species.isLegendary();
               if (EnumMegaPokemon.getMega(species) != null) {
                  if (legendary) {
                     bossMode = EnumBossMode.Ultimate;
                  } else {
                     bossMode = EnumBossMode.getRandomModeMega();
                  }
               } else if (legendary) {
                  bossMode = EnumBossMode.Ultimate;
               }
            }

            if (bossMode == null) {
               bossMode = EnumBossMode.getWeightedBossMode();
            }

            if (bossMode == null) {
               bossMode = EnumBossMode.getRandomMode();
            }

            usingSpec.boss = (byte)bossMode.index;
         }
      }

      if (usingSpec.pokerusType == null && PixelmonConfig.pokerusEnabled) {
         if (this.spawnSpecificPokerusRate != null) {
            if (this.spawnSpecificPokerusRate > 0.0F) {
               usingSpec.pokerusType = RandomHelper.getRandomChance(1.0F / this.spawnSpecificPokerusRate) ? (byte)EnumPokerusType.getRandomType().ordinal() : null;
            }
         } else if (this.set.setSpecificPokerusRate != null) {
            if (this.set.setSpecificPokerusRate > 0.0F) {
               usingSpec.pokerusType = RandomHelper.getRandomChance(1.0F / this.set.setSpecificPokerusRate) ? (byte)EnumPokerusType.getRandomType().ordinal() : null;
            }
         } else if (PixelmonConfig.pokerusRate > 0.0F) {
            usingSpec.pokerusType = RandomHelper.getRandomChance(1.0F / PixelmonConfig.pokerusRate) ? (byte)EnumPokerusType.getRandomType().ordinal() : null;
         }
      }

      if (usingSpec.extraSpecs == null || usingSpec.extraSpecs.stream().noneMatch((it) -> {
         return it instanceof IVEVSpec && ((IVEVSpec)it).isIVs;
      })) {
         PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)spawnLocation.cause);
         CaptureCombo combo = party.transientData.captureCombo;
         if (combo.getPerfIVCount() != 0 && combo.getCurrentSpecies() != null && combo.getCurrentSpecies().getPokemonName().equals(usingSpec.name)) {
            if (usingSpec.extraSpecs == null) {
               usingSpec.extraSpecs = new ArrayList();
            }

            Set stats = Sets.newHashSet(StatsType.getStatValues());

            for(int i = 0; i < combo.getPerfIVCount(); ++i) {
               StatsType type = (StatsType)RandomHelper.getRandomElementFromCollection(stats);
               stats.remove(type);
               PokemonSpec spec = PokemonSpec.from("iv" + type.name().toLowerCase() + ":31");
               usingSpec.extraSpecs.addAll(spec.extraSpecs);
            }
         }
      }

      return new SpawnActionPokemon(this, spawnLocation, baseSpec, usingSpec);
   }

   public PokemonSpec getPokemonSpec() {
      return this.spec;
   }

   public EnumSpecies getSpecies() {
      if (this.species == null) {
         this.species = EnumSpecies.getFromNameAnyCase(this.spec.name);
         if (this.species == null) {
            Pixelmon.LOGGER.warn("Bad Pokémon name: " + this.spec.name + " in set " + this.set.id);
         }
      }

      return this.species;
   }

   public String toString() {
      return this.spec.name != null && !this.spec.name.equals("random") ? I18n.func_74838_a("pixelmon." + this.spec.name.toLowerCase() + ".name") : "Random";
   }
}

package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs.trainers;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.TrainerData;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SpawnActionNPCTrainer extends SpawnAction {
   public String trainerType = "Youngster";

   public SpawnActionNPCTrainer(SpawnInfoNPCTrainer spawnInfo, SpawnLocation spawnLocation) {
      super(spawnInfo, spawnLocation);
      this.trainerType = spawnInfo.trainerType;
   }

   protected NPCTrainer createEntity() {
      if (PixelmonConfig.allGenerationsDisabled()) {
         return null;
      } else {
         NPCTrainer trainer = (NPCTrainer)PixelmonEntityList.createEntityByName(this.trainerType, this.spawnLocation.location.world);
         if (trainer == null) {
            return null;
         } else {
            SpawnInfoNPCTrainer spawnInfo = (SpawnInfoNPCTrainer)this.spawnInfo;
            if (spawnInfo.winMoney != -1) {
               trainer.winMoney = spawnInfo.winMoney;
            }

            int level = RandomHelper.getRandomNumberBetween(spawnInfo.getMinLevel(), spawnInfo.getMaxLevel());
            trainer.setLevel(level);

            for(int i = 0; i < 6; ++i) {
               trainer.getPokemonStorage().set(i, (Pokemon)null);
            }

            if (spawnInfo.name != null) {
               trainer.setName(spawnInfo.name);
               trainer.usingDefaultName = false;
            }

            Function createPoke = (specx) -> {
               Pokemon pokemon = specx.create();
               if (specx.level == null) {
                  pokemon.getLevelContainer().setLevel(level);
               }

               return pokemon;
            };
            int pokemonCount = 0;
            if (spawnInfo.guaranteedPokemon != null && !spawnInfo.guaranteedPokemon.isEmpty()) {
               Iterator var6 = spawnInfo.guaranteedPokemon.iterator();

               while(var6.hasNext()) {
                  PokemonSpec spec = (PokemonSpec)var6.next();
                  if (EnumSpecies.hasPokemonAnyCase(spec.name)) {
                     Pokemon pokemon = (Pokemon)createPoke.apply(spec);
                     if (!PixelmonConfig.isGenerationEnabled(pokemon.getSpecies().getGeneration())) {
                        return null;
                     }

                     ++pokemonCount;
                     trainer.getPokemonStorage().add(pokemon);
                  }
               }
            }

            ArrayList filteredPossiblePokemon = null;
            PokemonSpec spec;
            if (PixelmonConfig.allGenerationsEnabled()) {
               filteredPossiblePokemon = spawnInfo.possiblePokemon;
            } else if (spawnInfo.possiblePokemon != null) {
               Iterator var12 = spawnInfo.possiblePokemon.iterator();

               while(var12.hasNext()) {
                  spec = (PokemonSpec)var12.next();
                  Optional species = EnumSpecies.getFromName(spec.name);
                  if (species.isPresent() && PixelmonConfig.isGenerationEnabled(((EnumSpecies)species.get()).getGeneration())) {
                     if (filteredPossiblePokemon == null) {
                        filteredPossiblePokemon = new ArrayList();
                     }

                     filteredPossiblePokemon.add(spec);
                  }
               }
            }

            int chosenPartyCount = RandomHelper.getRandomNumberBetween(spawnInfo.getMinPartySize(), spawnInfo.getMaxPartySize());

            while(chosenPartyCount > pokemonCount && filteredPossiblePokemon != null && !filteredPossiblePokemon.isEmpty()) {
               spec = (PokemonSpec)CollectionHelper.getRandomElement((List)filteredPossiblePokemon);
               if (EnumSpecies.hasPokemonAnyCase(spec.name)) {
                  Pokemon pokemon = (Pokemon)createPoke.apply(spec);
                  trainer.getPokemonStorage().add(pokemon);
                  ++pokemonCount;
               }
            }

            if (trainer.getPokemonStorage().countPokemon() == 0) {
               TrainerData data = ServerNPCRegistry.trainers.getRandomData(this.trainerType);
               if (data != null) {
                  ArrayList randomParty = data.getRandomParty();
                  if (randomParty != null && !randomParty.isEmpty()) {
                     trainer.loadPokemon(randomParty);
                  }
               } else if (PixelmonConfig.allGenerationsEnabled()) {
                  trainer.getPokemonStorage().add((new PokemonSpec(new String[]{"random", "lvl:" + spawnInfo.getMinLevel()})).create());
               } else {
                  trainer.getPokemonStorage().add((new PokemonSpec(new String[]{"random", "lvl:" + spawnInfo.getMinLevel(), "generation:" + PixelmonConfig.getRandomEnabledGeneration()})).create());
               }

               label111:
               while(true) {
                  while(true) {
                     if (trainer.getPokemonStorage().countPokemon() <= spawnInfo.getMaxPartySize()) {
                        break label111;
                     }

                     for(int i = 5; i >= 0; --i) {
                        if (trainer.getPokemonStorage().get(i) != null) {
                           trainer.getPokemonStorage().set(i, (Pokemon)null);
                           break;
                        }
                     }
                  }
               }
            }

            if (trainer.getPokemonStorage().countPokemon() == 0) {
               return null;
            } else {
               if (spawnInfo.greeting != null) {
                  trainer.usingDefaultGreeting = false;
                  trainer.greeting = spawnInfo.greeting;
               }

               if (spawnInfo.winMessage != null) {
                  trainer.winMessage = spawnInfo.winMessage;
                  trainer.usingDefaultWin = false;
               }

               if (spawnInfo.loseMessage != null) {
                  trainer.loseMessage = spawnInfo.loseMessage;
                  trainer.usingDefaultLose = false;
               }

               if (spawnInfo.bossMode != null) {
                  trainer.setBossMode(spawnInfo.bossMode);
               }

               return trainer;
            }
         }
      }
   }
}

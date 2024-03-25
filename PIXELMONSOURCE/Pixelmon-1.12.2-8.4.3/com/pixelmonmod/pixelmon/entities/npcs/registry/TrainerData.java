package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.util.ArrayList;
import java.util.Iterator;

public class TrainerData implements ITrainerData {
   public BaseTrainer trainerType;
   int minLevel;
   int maxLevel;
   int minPartyPokemon;
   int maxPartyPokemon;
   public int winnings;
   ArrayList pokemonList = new ArrayList(6);
   ArrayList names = new ArrayList();
   ArrayList textures = new ArrayList();
   ArrayList chat = new ArrayList();
   public String id;

   public TrainerData(String id) {
      this.id = id;
   }

   void addName(String name) {
      this.names.add(name);
   }

   void addTexture(String texture) {
      this.textures.add(texture);
   }

   public void addPokemon(PokemonForm poke) {
      this.pokemonList.add(poke);
   }

   public void addChat(String opening, String win, String lose) {
      this.chat.add(new TrainerChat(opening, win, lose));
   }

   public ArrayList getRandomParty() {
      int numPokemon = RandomHelper.getRandomNumberBetween(this.minPartyPokemon, this.maxPartyPokemon);
      ArrayList list = new ArrayList();
      ArrayList filteredForms = null;
      PokemonForm pkForm;
      if (!PixelmonConfig.allGenerationsEnabled()) {
         filteredForms = new ArrayList();
         Iterator var4 = this.pokemonList.iterator();

         while(var4.hasNext()) {
            pkForm = (PokemonForm)var4.next();
            if (PixelmonConfig.isGenerationEnabled(pkForm.pokemon.getGeneration())) {
               filteredForms.add(pkForm);
            }
         }
      } else {
         filteredForms = this.pokemonList;
      }

      if (filteredForms.isEmpty()) {
         Pixelmon.LOGGER.error("Tried to generate a random party on TrainerData \"" + this.id + "\" with no valid Pokémon on the enabled generations!");
         return list;
      } else {
         for(int i = 0; i < numPokemon; ++i) {
            pkForm = (PokemonForm)RandomHelper.getRandomElementFromList(filteredForms);
            PokemonSpec spec = new PokemonSpec(pkForm.pokemon.name);
            spec.form = pkForm.getForm();
            if (pkForm.gender != null) {
               spec.gender = pkForm.gender.getForm();
            }

            list.add(spec.create());
         }

         return list;
      }
   }

   public int getRandomName() {
      return RandomHelper.getRandomNumberBetween(0, this.names.size() - 1);
   }

   public int getRandomChat() {
      return RandomHelper.getRandomNumberBetween(0, this.chat.size() - 1);
   }

   public int getRandomLevel() {
      return RandomHelper.getRandomNumberBetween(this.minLevel, this.maxLevel);
   }

   public int getMinLevel() {
      return this.minLevel;
   }

   public int getMaxLevel() {
      return this.maxLevel;
   }

   public int getMinPartyPokemon() {
      return this.minPartyPokemon;
   }

   public int getMaxPartyPokemon() {
      return this.maxPartyPokemon;
   }

   public String getName(int index) {
      return index < this.names.size() && index >= 0 ? (String)this.names.get(index) : (String)this.names.get(0);
   }

   public TrainerChat getChat(int index) {
      return index < this.chat.size() && index >= 0 ? (TrainerChat)this.chat.get(index) : (TrainerChat)this.chat.get(0);
   }
}

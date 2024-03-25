package com.pixelmonmod.pixelmon.worldGeneration.structure.gyms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.ArrayList;

public class PokemonDefinition {
   public EnumSpecies pokemon;
   public float rarity = 1.0F;
   public int minLevel = 0;
   public int maxLevel;
   public ArrayList movesets;

   public PokemonDefinition() {
      this.maxLevel = PixelmonServerConfig.maxLevel;
      this.movesets = new ArrayList();
   }

   public static PokemonDefinition readPokemonDefinition(String gymName, JsonObject obj) {
      PokemonDefinition def = new PokemonDefinition();
      String pokemonName = obj.get("name").getAsString();
      if (!EnumSpecies.hasPokemon(pokemonName)) {
         Pixelmon.LOGGER.warn("Couldn't find pokemon " + pokemonName + " for gym " + gymName);
      } else {
         def.pokemon = EnumSpecies.get(pokemonName);
      }

      if (obj.has("minLevel")) {
         def.minLevel = obj.get("minLevel").getAsInt();
      }

      if (obj.has("maxLevel")) {
         def.maxLevel = obj.get("maxLevel").getAsInt();
      }

      if (obj.has("rarity")) {
         def.rarity = obj.get("rarity").getAsFloat();
      }

      JsonArray movesetArray = obj.getAsJsonArray("sets");

      for(int j = 0; j < movesetArray.size(); ++j) {
         JsonObject setObj = movesetArray.get(j).getAsJsonObject();
         def.movesets.add(MovesetDefinition.readMovesetDefinition(setObj));
      }

      return def;
   }
}

package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StarterList {
   public static final int NUM_STARTERS = 24;
   private static PokemonForm[] starterList;

   public static void setStarterList(PokemonForm[] newStarterList) {
      starterList = newStarterList;
   }

   public static PokemonForm[] getStarterList() {
      if (!PixelmonConfig.useCustomStarters) {
         return starterList;
      } else {
         List list = new ArrayList();
         Iterator var1 = PixelmonConfig.starterList.iterator();

         while(true) {
            while(var1.hasNext()) {
               String str = (String)var1.next();
               PokemonSpec spec = PokemonSpec.from(str.split(" "));
               Pokemon pokemon = Pixelmon.pokemonFactory.create(spec);
               if (pokemon != null && pokemon.getSpecies() != null) {
                  EnumSpecies species = pokemon.getSpecies();
                  PokemonForm poke = new PokemonForm(species);
                  poke.form = pokemon.getForm();
                  poke.gender = pokemon.getGender();
                  poke.shiny = pokemon.isShiny() || PixelmonConfig.shinyStarter;
                  list.add(poke);
               } else {
                  Pixelmon.LOGGER.error("Invalid starter spec - " + str);
               }
            }

            return (PokemonForm[])list.toArray(new PokemonForm[0]);
         }
      }
   }

   static {
      EnumSpecies[] starterSpecies = new EnumSpecies[]{EnumSpecies.Bulbasaur, EnumSpecies.Squirtle, EnumSpecies.Charmander, EnumSpecies.Chikorita, EnumSpecies.Totodile, EnumSpecies.Cyndaquil, EnumSpecies.Treecko, EnumSpecies.Mudkip, EnumSpecies.Torchic, EnumSpecies.Turtwig, EnumSpecies.Piplup, EnumSpecies.Chimchar, EnumSpecies.Snivy, EnumSpecies.Oshawott, EnumSpecies.Tepig, EnumSpecies.Chespin, EnumSpecies.Froakie, EnumSpecies.Fennekin, EnumSpecies.Rowlet, EnumSpecies.Popplio, EnumSpecies.Litten, EnumSpecies.Grookey, EnumSpecies.Sobble, EnumSpecies.Scorbunny};
      starterList = new PokemonForm[starterSpecies.length];

      for(int i = 0; i < starterSpecies.length; ++i) {
         starterList[i] = new PokemonForm(starterSpecies[i]);
         starterList[i].shiny = PixelmonConfig.shinyStarter;
      }

   }
}

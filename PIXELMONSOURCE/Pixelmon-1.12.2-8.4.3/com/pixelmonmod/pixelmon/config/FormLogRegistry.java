package com.pixelmonmod.pixelmon.config;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumClobbopus;
import com.pixelmonmod.pixelmon.enums.forms.EnumMagikarp;
import com.pixelmonmod.pixelmon.enums.forms.EnumShellos;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpheal;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.commons.lang3.tuple.Pair;

public class FormLogRegistry {
   private static Map registry = Maps.newHashMap();
   public static Object[] combinedWeights;
   public static double totalWeight;

   public static void init() {
      for(int i = 0; i < combinedWeights.length; ++i) {
         totalWeight += (double)(Integer)((Pair)combinedWeights[i]).getRight();
      }

      registry.clear();
      createCategory(EnumSpecies.Magikarp, 0, new PokemonSpec("form:0 !s"), new PokemonSpec("form:2 !s"));
      createCategory(EnumSpecies.Magikarp, 1, new PokemonSpec("form:3 !s"), new PokemonSpec("form:4 !s"), new PokemonSpec("form:5 !s"), new PokemonSpec("form:6 !s"));
      createCategory(EnumSpecies.Magikarp, 2, new PokemonSpec("form:7 !s"), new PokemonSpec("form:8 !s"), new PokemonSpec("form:9 !s"));
      createCategory(EnumSpecies.Magikarp, 3, new PokemonSpec("form:10 !s"), new PokemonSpec("form:11 !s"), new PokemonSpec("form:12 !s"));
      createCategory(EnumSpecies.Magikarp, 4, new PokemonSpec("form:13 !s"), new PokemonSpec("form:14 !s"), new PokemonSpec("form:15 !s"));
      createCategory(EnumSpecies.Magikarp, 5, new PokemonSpec("form:16 !s"), new PokemonSpec("form:17 !s"), new PokemonSpec("form:18 !s"));
      createCategory(EnumSpecies.Magikarp, 6, new PokemonSpec("form:19 !s"), new PokemonSpec("form:20 !s"), new PokemonSpec("form:21 !s"));
      createCategory(EnumSpecies.Magikarp, 7, new PokemonSpec("form:22 !s"), new PokemonSpec("form:23 !s"), new PokemonSpec("form:24 !s"));
      createCategory(EnumSpecies.Magikarp, 8, new PokemonSpec("form:25 !s"), new PokemonSpec("form:26 !s"));
      createCategory(EnumSpecies.Magikarp, 9, new PokemonSpec("form:27 !s"), new PokemonSpec("form:28 !s"));
      createCategory(EnumSpecies.Magikarp, 10, new PokemonSpec("form:29 !s"), new PokemonSpec("form:30 !s"));
      createCategory(EnumSpecies.Magikarp, 11, new PokemonSpec("form:31 !s"), new PokemonSpec("form:32 !s"));
      createCategory(EnumSpecies.Magikarp, 12, new PokemonSpec("shiny"), new PokemonSpec("form:1 !s"), new PokemonSpec("form:106 !s"));
      createCategory(EnumSpecies.Shellos, 0, new PokemonSpec("form:0 !s"));
      createCategory(EnumSpecies.Shellos, 1, new PokemonSpec("form:1 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(0).add(new PokemonSpec("form:2 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(1).add(new PokemonSpec("form:3 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(0).add(new PokemonSpec("form:4 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(1).add(new PokemonSpec("form:5 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(0).add(new PokemonSpec("form:6 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(1).add(new PokemonSpec("form:7 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(0).add(new PokemonSpec("form:8 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(1).add(new PokemonSpec("form:9 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(0).add(new PokemonSpec("form:10 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(1).add(new PokemonSpec("form:11 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(0).add(new PokemonSpec("form:12 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(1).add(new PokemonSpec("form:13 !s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(0).add(new PokemonSpec("form:0 s"));
      ((LinkedHashMultimap)registry.get(EnumSpecies.Shellos)).get(1).add(new PokemonSpec("form:1 s"));
      createCategory(EnumSpecies.Clobbopus, 0, new PokemonSpec("form:0 !s"), new PokemonSpec("form:1 !s"), new PokemonSpec("form:2 !s"), new PokemonSpec("form:3 !s"), new PokemonSpec("form:4 !s"), new PokemonSpec("form:5 !s"), new PokemonSpec("form:6 !s"), new PokemonSpec("form:7 !s"), new PokemonSpec("form:8 !s"), new PokemonSpec("form:9 !s"), new PokemonSpec("shiny"));
      createCategory(EnumSpecies.Spheal, 0, new PokemonSpec("form:0 !s"), new PokemonSpec("form:1 !s"), new PokemonSpec("form:2 !s"), new PokemonSpec("form:3 !s"), new PokemonSpec("form:4 !s"), new PokemonSpec("form:5 !s"), new PokemonSpec("form:6 !s"), new PokemonSpec("form:7 !s"), new PokemonSpec("form:8 !s"), new PokemonSpec("form:9 !s"), new PokemonSpec("shiny"));
      createCategory(EnumSpecies.Slugma, 0, new PokemonSpec("form:0 !s"), new PokemonSpec("form:1 !s"), new PokemonSpec("form:2 !s"), new PokemonSpec("form:3 !s"), new PokemonSpec("form:4 !s"), new PokemonSpec("form:5 !s"), new PokemonSpec("form:6 !s"), new PokemonSpec("form:7 !s"), new PokemonSpec("form:8 !s"), new PokemonSpec("form:9 !s"), new PokemonSpec("shiny"));
   }

   public static Stream getSpeciesList() {
      return registry.keySet().stream().sorted();
   }

   public static LinkedHashMultimap getFormsForSpecies(EnumSpecies species) {
      if (registry.isEmpty()) {
         init();
      }

      return (LinkedHashMultimap)registry.getOrDefault(species, LinkedHashMultimap.create());
   }

   public static int getFormId(EnumSpecies species, PokemonSpec form) {
      LinkedHashMultimap map = getFormsForSpecies(species);
      List specs = Lists.newArrayList(map.values());
      return specs.indexOf(form);
   }

   public static int getFirstIdFromPokemon(Pokemon pokemon) {
      LinkedHashMultimap map = getFormsForSpecies(pokemon.getSpecies());
      List specs = Lists.newArrayList(map.values());

      for(int i = 0; i < specs.size(); ++i) {
         if (((PokemonSpec)specs.get(i)).matches(pokemon)) {
            return i;
         }
      }

      return -1;
   }

   public static PokemonSpec getFormFromId(EnumSpecies species, int id) {
      LinkedHashMultimap map = getFormsForSpecies(species);
      List specs = Lists.newArrayList(map.values());
      return (PokemonSpec)specs.get(id);
   }

   public static void createCategory(EnumSpecies species, int category, PokemonSpec... forms) {
      if (!registry.containsKey(species)) {
         registry.put(species, LinkedHashMultimap.create());
      }

      Multimap map = (Multimap)registry.get(species);
      map.putAll(category, Lists.newArrayList(forms));
   }

   public static void createCategory(EnumSpecies species, int category, int entries) {
      if (!registry.containsKey(species)) {
         registry.put(species, LinkedHashMultimap.create());
      }

      Multimap map = (Multimap)registry.get(species);
      int max = map.values().stream().mapToInt((spec) -> {
         return spec.form + 1;
      }).max().orElse(0);
      List forms = Lists.newArrayList();

      for(int i = max; i < entries + max; ++i) {
         forms.add(new PokemonSpec("form:" + i));
      }

      map.putAll(category, forms);
   }

   public static void createCategory(EnumSpecies species, int entries) {
      if (!registry.containsKey(species)) {
         createCategory(species, 1, entries);
      } else {
         Multimap map = (Multimap)registry.get(species);
         int max = map.keySet().stream().mapToInt((spec) -> {
            return spec;
         }).max().orElse(0);
         createCategory(species, max + 1, entries);
      }

   }

   public static int[] getData(EntityPlayerMP target, EnumSpecies species) {
      Map cells = Pixelmon.storageManager.getParty(target).pokedex.seenForms(species);
      int[] data = new int[getFormsForSpecies(species).size()];
      cells.forEach((key, value) -> {
         data[key] = value;
      });
      return data;
   }

   public static int[] getMenuData(EntityPlayerMP playerIn) {
      ArrayList list = new ArrayList();
      list.add(registry.size());
      getSpeciesList().forEachOrdered((species) -> {
         list.add(species.getNationalPokedexInteger());
         int[] data = getData(playerIn, species);
         list.add(data.length);
         int[] var4 = data;
         int var5 = data.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            int i = var4[var6];
            list.add(i);
         }

      });
      int[] ret = new int[list.size()];

      for(int i = 0; i < list.size(); ++i) {
         ret[i] = (Integer)list.get(i);
      }

      return ret;
   }

   public static IEnumForm getRandomFish() {
      int randomIndex = -1;
      double random = Math.random() * totalWeight;

      for(int i = 0; i < combinedWeights.length; ++i) {
         random -= (double)(Integer)((Pair)combinedWeights[i]).getRight();
         if (random <= 0.0) {
            randomIndex = i;
            break;
         }
      }

      return (IEnumForm)((Pair)combinedWeights[randomIndex]).getLeft();
   }

   static {
      combinedWeights = Stream.of(EnumClobbopus.OLD_ROD_WEIGHTS, EnumMagikarp.OLD_ROD_WEIGHTS, EnumShellos.OLD_ROD_WEIGHTS, EnumSpheal.OLD_ROD_WEIGHTS, EnumClobbopus.OLD_ROD_WEIGHTS).flatMap(Stream::of).toArray();
      totalWeight = 0.0;
   }
}

package com.pixelmonmod.pixelmon.util.helpers;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.EnumInitializeCategory;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumEggGroup;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.FormAttributes;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.enums.forms.RegionalForms;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

public class BreedLogic {
   public static boolean canBreed(Pokemon parent1, Pokemon parent2) {
      boolean is1Ditto = parent1.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto});
      boolean is2Ditto = parent2.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto});
      if (!is1Ditto && !is2Ditto) {
         if (parent1.getGender() == parent2.getGender()) {
            return false;
         } else if (parent1.getGender() != Gender.None && parent2.getGender() != Gender.None) {
            return getEggGroupForPair(parent1, parent2) != EnumEggGroup.Undiscovered;
         } else {
            return false;
         }
      } else if (is1Ditto && is2Ditto) {
         return PixelmonConfig.allowDittoDittoBreeding;
      } else {
         EnumEggGroup[] groupsNonDitto = is1Ditto ? EnumEggGroup.getEggGroups(parent2) : EnumEggGroup.getEggGroups(parent1);
         EnumEggGroup[] var5 = groupsNonDitto;
         int var6 = groupsNonDitto.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            EnumEggGroup groupNonDitto = var5[var7];
            if (groupNonDitto == EnumEggGroup.Undiscovered) {
               return false;
            }
         }

         return true;
      }
   }

   public static Pokemon makeRandomEgg(Pokemon parent1, Pokemon parent2) {
      EnumSpecies species = EnumSpecies.randomPoke(PixelmonConfig.allowRandomBreedingEggsToBeLegendary);
      Pokemon pokemon = PokemonSpec.from(species.name, "lvl:1").create().makeEgg();
      pokemon.initialize(EnumInitializeCategory.SPECIES, EnumInitializeCategory.INTRINSIC);
      int slot = pokemon.getBaseStats().getAbilitiesArray()[1] != null ? RandomHelper.getRandomNumberBetween(0, 1) : 0;
      pokemon.setAbility(pokemon.getBaseStats().getAbilitiesArray()[slot]);
      pokemon.getIVs().CopyIVs(getIVsForEgg(parent1, parent2));
      pokemon.setNature(getNatureForEgg(parent1, parent2));
      pokemon.setGrowth(getEggGrowth(parent1, parent2));
      pokemon.setShiny(getEggIsShiny(parent1, parent2));
      return pokemon;
   }

   @Nullable
   public static Pokemon makeEgg(Pokemon parent1, Pokemon parent2) {
      if (canBreed(parent1, parent2)) {
         if (parent1.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto}) && parent2.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto}) && PixelmonConfig.allowDittoDittoBreeding) {
            return makeRandomEgg(parent1, parent2);
         } else {
            EnumSpecies species = getPokemonInEggName(parent1, parent2);
            IEnumForm form = getPokemonInEggForm(species, parent1, parent2);
            Pokemon pokemon = PokemonSpec.from(species.name, "f:" + form.getForm()).create().makeEgg();
            pokemon.getIVs().CopyIVs(getIVsForEgg(parent1, parent2));
            pokemon.setNature(getNatureForEgg(parent1, parent2));
            pokemon.setCaughtBall(getMotherPokeball(parent1, parent2));
            pokemon.setGrowth(getEggGrowth(parent1, parent2));
            pokemon.setAbility(pokemon.getBaseStats().getAbilitiesArray()[getEggAbilitySlot(pokemon, parent1, parent2)]);
            pokemon.setShiny(getEggIsShiny(parent1, parent2));
            Moveset moveset = getEggMoveset(pokemon, species, parent1, parent2);
            Moveset thisMoveset = pokemon.getMoveset();
            thisMoveset.set(0, (Attack)moveset.get(0));
            thisMoveset.set(1, (Attack)moveset.get(1));
            thisMoveset.set(2, (Attack)moveset.get(2));
            thisMoveset.set(3, (Attack)moveset.get(3));
            return pokemon;
         }
      } else {
         Pixelmon.LOGGER.info("Error occurred in breeding; incompatible pair passed to Egg initialization.");
         return null;
      }
   }

   public static IEnumForm getPokemonInEggForm(EnumSpecies childSpecies, Pokemon parent1, Pokemon parent2) {
      Pokemon mother = findMother(parent1, parent2);
      Pokemon father = findFather(parent1, parent2);
      IEnumForm form = null;
      if (mother != null && mother.getHeldItem().func_77973_b().equals(PixelmonItemsHeld.everStone)) {
         form = mother.getFormEnum();
      } else if (father != null && father.getHeldItem().func_77973_b().equals(PixelmonItemsHeld.everStone)) {
         form = father.getFormEnum();
      }

      List forms;
      if (form != null && form.getFormAttributes().contains(FormAttributes.GALARIAN)) {
         if (childSpecies.getPossibleForms(false).contains(form)) {
            return form;
         }

         forms = childSpecies.getPossibleFormsFor(FormAttributes.GALARIAN);
         if (!forms.isEmpty()) {
            return (IEnumForm)forms.get(0);
         }
      }

      if (form != null && form.getFormAttributes().contains(FormAttributes.ALOLAN)) {
         if (childSpecies.getPossibleForms(false).contains(form)) {
            return form;
         }

         forms = childSpecies.getPossibleFormsFor(FormAttributes.ALOLAN);
         if (!forms.isEmpty()) {
            return (IEnumForm)forms.get(0);
         }
      }

      if (form != null && form.getFormAttributes().contains(FormAttributes.HISUIAN)) {
         if (childSpecies.getPossibleForms(false).contains(form)) {
            return form;
         }

         forms = childSpecies.getPossibleFormsFor(FormAttributes.HISUIAN);
         if (!forms.isEmpty()) {
            return (IEnumForm)forms.get(0);
         }
      }

      if (PixelmonConfig.regionalFormsByDimension) {
         int world = 0;
         if (parent1.getWorld() != null) {
            world = parent1.getWorld().field_73011_w.getDimension();
         } else if (parent2.getWorld() != null) {
            world = parent1.getWorld().field_73011_w.getDimension();
         }

         List forms;
         if (PixelmonConfig.galarianEggDimensions.contains(world)) {
            forms = childSpecies.getPossibleFormsFor(FormAttributes.GALARIAN);
            if (!forms.isEmpty()) {
               return (IEnumForm)forms.get(0);
            }
         }

         if (PixelmonConfig.alolanEggDimensions.contains(world)) {
            forms = childSpecies.getPossibleFormsFor(FormAttributes.ALOLAN);
            if (!forms.isEmpty()) {
               return (IEnumForm)forms.get(0);
            }
         }
      }

      List inheritors = Lists.newArrayList(new EnumSpecies[]{EnumSpecies.Shellos, EnumSpecies.Burmy, EnumSpecies.Basculin, EnumSpecies.Deerling, EnumSpecies.Flabebe});
      if (inheritors.contains(childSpecies)) {
         if (mother != null && mother.getSpecies() == childSpecies) {
            return mother.getFormEnum();
         }

         if (father != null && father.getSpecies() == childSpecies) {
            return father.getFormEnum();
         }
      }

      return (IEnumForm)CollectionHelper.getRandomElement(childSpecies.getDefaultForms());
   }

   private static EnumSpecies getPokemonInEggName(Pokemon parent1, Pokemon parent2) {
      boolean inherit1 = !parent1.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto}) && (parent2.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto}) || parent2.getGender() == Gender.Male);
      Pokemon parentForEggLine = inherit1 ? parent1 : parent2;
      Item itemFromParent = parentForEggLine.getHeldItem().func_77973_b();
      Item itemFromOtherParent = inherit1 ? parent2.getHeldItem().func_77973_b() : parent1.getHeldItem().func_77973_b();
      EnumSpecies[] eggForms;
      if (parentForEggLine.isPokemon(new EnumSpecies[]{EnumSpecies.Nidoranfemale, EnumSpecies.Nidoranmale, EnumSpecies.Nidorino, EnumSpecies.Nidoking})) {
         eggForms = new EnumSpecies[]{EnumSpecies.Nidoranfemale, EnumSpecies.Nidoranmale};
         return (EnumSpecies)RandomHelper.getRandomElementFromArray(eggForms);
      } else if (parentForEggLine.isPokemon(new EnumSpecies[]{EnumSpecies.Illumise, EnumSpecies.Volbeat})) {
         eggForms = new EnumSpecies[]{EnumSpecies.Illumise, EnumSpecies.Volbeat};
         return (EnumSpecies)RandomHelper.getRandomElementFromArray(eggForms);
      } else if (checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.seaIncense, EnumSpecies.Azurill, EnumSpecies.Marill, EnumSpecies.Azumarill)) {
         return EnumSpecies.Marill;
      } else if (checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.laxIncense, EnumSpecies.Wynaut, EnumSpecies.Wobbuffet)) {
         return EnumSpecies.Wobbuffet;
      } else if (checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.roseIncense, EnumSpecies.Budew, EnumSpecies.Roselia, EnumSpecies.Roserade)) {
         return EnumSpecies.Roselia;
      } else if (checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.pureIncense, EnumSpecies.Chingling, EnumSpecies.Chimecho)) {
         return EnumSpecies.Chimecho;
      } else if (checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.rockIncense, EnumSpecies.Bonsly, EnumSpecies.Sudowoodo)) {
         return EnumSpecies.Sudowoodo;
      } else if (checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.oddIncense, EnumSpecies.MimeJr, EnumSpecies.MrMime)) {
         return EnumSpecies.MrMime;
      } else if (checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.oddIncense, EnumSpecies.MimeJr, EnumSpecies.MrRime)) {
         return EnumSpecies.MrMime;
      } else if (checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.luckIncense, EnumSpecies.Happiny, EnumSpecies.Chansey, EnumSpecies.Blissey)) {
         return EnumSpecies.Chansey;
      } else if (checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.waveIncense, EnumSpecies.Mantyke, EnumSpecies.Mantine)) {
         return EnumSpecies.Mantine;
      } else {
         return checkIncense(itemFromParent, itemFromOtherParent, parentForEggLine, PixelmonItemsHeld.fullIncense, EnumSpecies.Munchlax, EnumSpecies.Snorlax) ? EnumSpecies.Snorlax : parentForEggLine.getSpecies().getBaseSpecies();
      }
   }

   public static EnumEggGroup getEggGroupForPair(PokemonBase parent1, PokemonBase parent2) {
      EnumEggGroup[] groups1 = EnumEggGroup.getEggGroups(parent1);
      EnumEggGroup[] groups2 = EnumEggGroup.getEggGroups(parent2);
      ArrayList groupsEgg = new ArrayList();
      EnumEggGroup[] var5 = groups1;
      int var6 = groups1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         EnumEggGroup group1 = var5[var7];
         EnumEggGroup[] var9 = groups2;
         int var10 = groups2.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            EnumEggGroup group2 = var9[var11];
            if (group1 == group2 && group2 != EnumEggGroup.Undiscovered) {
               groupsEgg.add(group2);
            }
         }
      }

      if (groupsEgg.isEmpty()) {
         return EnumEggGroup.Undiscovered;
      } else {
         return (EnumEggGroup)groupsEgg.get(0);
      }
   }

   public static IVStore getIVsForEgg(Pokemon parent1, Pokemon parent2) {
      ArrayList masterStatsList = new ArrayList(Arrays.asList("HP1", "ATK1", "DEF1", "SPATK1", "SPDEF1", "SPD1", "HP2", "ATK2", "DEF2", "SPATK2", "SPDEF2", "SPD2"));
      ArrayList strStatsToInherit = new ArrayList();
      IVStore newIVs = IVStore.CreateNewIVs();
      int intNumberToInherit = 3;
      ItemHeld heldItem1 = parent1.getHeldItemAsItemHeld();
      ItemHeld heldItem2 = parent2.getHeldItemAsItemHeld();
      if (heldItem1 == PixelmonItemsHeld.destinyKnot || heldItem2 == PixelmonItemsHeld.destinyKnot) {
         intNumberToInherit = 5;
      }

      boolean skipSecondHeldItem = false;
      if (heldItem1 != NoItem.noItem) {
         if (heldItem1 == PixelmonItemsHeld.powerWeight) {
            strStatsToInherit.add("HP1");
            --intNumberToInherit;
         } else if (heldItem1 == PixelmonItemsHeld.powerBracer) {
            strStatsToInherit.add("ATK1");
            --intNumberToInherit;
         } else if (heldItem1 == PixelmonItemsHeld.powerBelt) {
            strStatsToInherit.add("DEF1");
            --intNumberToInherit;
         } else if (heldItem1 == PixelmonItemsHeld.powerLens) {
            strStatsToInherit.add("SPATK1");
            --intNumberToInherit;
         } else if (heldItem1 == PixelmonItemsHeld.powerBand) {
            strStatsToInherit.add("SPDEF1");
            --intNumberToInherit;
         } else if (heldItem1 == PixelmonItemsHeld.powerAnklet) {
            strStatsToInherit.add("SPD1");
            --intNumberToInherit;
         }

         if (heldItem2 != NoItem.noItem && heldItem1 == heldItem2) {
            if (RandomHelper.getRandomChance()) {
               skipSecondHeldItem = true;
            } else {
               strStatsToInherit = new ArrayList();
            }
         }
      }

      Iterator var9;
      String strStat;
      if (!strStatsToInherit.isEmpty()) {
         var9 = strStatsToInherit.iterator();

         while(var9.hasNext()) {
            strStat = (String)var9.next();
            masterStatsList.remove(strStat);
            if (strStat.contains("1")) {
               masterStatsList.remove(RegexPatterns.NUMBER_ONE.matcher(strStat).replaceAll("2"));
            } else {
               masterStatsList.remove(RegexPatterns.NUMBER_TWO.matcher(strStat).replaceAll("1"));
            }
         }
      }

      if (!skipSecondHeldItem && heldItem2 != NoItem.noItem) {
         if (heldItem2 == PixelmonItemsHeld.powerWeight) {
            strStatsToInherit.add("HP2");
            --intNumberToInherit;
         } else if (heldItem2 == PixelmonItemsHeld.powerBracer) {
            strStatsToInherit.add("ATK2");
            --intNumberToInherit;
         } else if (heldItem2 == PixelmonItemsHeld.powerBelt) {
            strStatsToInherit.add("DEF2");
            --intNumberToInherit;
         } else if (heldItem2 == PixelmonItemsHeld.powerLens) {
            strStatsToInherit.add("SPATK2");
            --intNumberToInherit;
         } else if (heldItem2 == PixelmonItemsHeld.powerBand) {
            strStatsToInherit.add("SPDEF2");
            --intNumberToInherit;
         } else if (heldItem2 == PixelmonItemsHeld.powerAnklet) {
            strStatsToInherit.add("SPD2");
            --intNumberToInherit;
         }
      }

      if (!strStatsToInherit.isEmpty()) {
         var9 = strStatsToInherit.iterator();

         while(var9.hasNext()) {
            strStat = (String)var9.next();
            masterStatsList.remove(strStat);
            if (strStat.contains("1")) {
               masterStatsList.remove(RegexPatterns.NUMBER_ONE.matcher(strStat).replaceAll("2"));
            } else {
               masterStatsList.remove(RegexPatterns.NUMBER_TWO.matcher(strStat).replaceAll("1"));
            }
         }
      }

      for(int i = 0; i < intNumberToInherit; ++i) {
         strStat = (String)RandomHelper.getRandomElementFromList(masterStatsList);
         strStatsToInherit.add(strStat);
         masterStatsList.remove(strStat);
         if (strStat.contains("1")) {
            masterStatsList.remove(RegexPatterns.NUMBER_ONE.matcher(strStat).replaceAll("2"));
         } else {
            masterStatsList.remove(RegexPatterns.NUMBER_TWO.matcher(strStat).replaceAll("1"));
         }
      }

      var9 = strStatsToInherit.iterator();

      while(var9.hasNext()) {
         strStat = (String)var9.next();
         if (strStat.equalsIgnoreCase("HP1")) {
            newIVs.hp = parent1.getIVs().hp;
         } else if (strStat.equalsIgnoreCase("ATK1")) {
            newIVs.attack = parent1.getIVs().attack;
         } else if (strStat.equalsIgnoreCase("DEF1")) {
            newIVs.defence = parent1.getIVs().defence;
         } else if (strStat.equalsIgnoreCase("SPATK1")) {
            newIVs.specialAttack = parent1.getIVs().specialAttack;
         } else if (strStat.equalsIgnoreCase("SPDEF1")) {
            newIVs.specialDefence = parent1.getIVs().specialDefence;
         } else if (strStat.equalsIgnoreCase("SPD1")) {
            newIVs.speed = parent1.getIVs().speed;
         } else if (strStat.equalsIgnoreCase("HP2")) {
            newIVs.hp = parent2.getIVs().hp;
         } else if (strStat.equalsIgnoreCase("ATK2")) {
            newIVs.attack = parent2.getIVs().attack;
         } else if (strStat.equalsIgnoreCase("DEF2")) {
            newIVs.defence = parent2.getIVs().defence;
         } else if (strStat.equalsIgnoreCase("SPATK2")) {
            newIVs.specialAttack = parent2.getIVs().specialAttack;
         } else if (strStat.equalsIgnoreCase("SPDEF2")) {
            newIVs.specialDefence = parent2.getIVs().specialDefence;
         } else if (strStat.equalsIgnoreCase("SPD2")) {
            newIVs.speed = parent2.getIVs().speed;
         }
      }

      return newIVs;
   }

   public static EnumNature getNatureForEgg(Pokemon parent1, Pokemon parent2) {
      boolean isEverstone1 = parent1.getHeldItemAsItemHeld() == PixelmonItemsHeld.everStone;
      boolean isEverstone2 = parent2.getHeldItemAsItemHeld() == PixelmonItemsHeld.everStone;
      if (isEverstone1 && isEverstone2) {
         return RandomHelper.getRandomChance() ? parent1.getBaseNature() : parent2.getBaseNature();
      } else if (isEverstone1) {
         return parent1.getBaseNature();
      } else {
         return isEverstone2 ? parent2.getBaseNature() : EnumNature.getRandomNature();
      }
   }

   public static EnumPokeballs getMotherPokeball(Pokemon parent1, Pokemon parent2) {
      Pokemon inheriting;
      if (parent1.getSpecies() == parent2.getSpecies()) {
         inheriting = RandomHelper.getRandomChance(50) ? parent1 : parent2;
      } else if (parent1.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto})) {
         inheriting = parent2;
      } else if (parent2.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto})) {
         inheriting = parent1;
      } else {
         inheriting = findMother(parent1, parent2);
         if (inheriting == null) {
            inheriting = RandomHelper.getRandomChance(50) ? parent1 : parent2;
         }
      }

      Pokemon other = inheriting == parent1 ? parent2 : parent1;
      if (!inheriting.getCaughtBall().isPokeball(EnumPokeballs.MasterBall, EnumPokeballs.CherishBall)) {
         return inheriting.getCaughtBall();
      } else {
         return !other.getCaughtBall().isPokeball(EnumPokeballs.MasterBall, EnumPokeballs.CherishBall) ? other.getCaughtBall() : EnumPokeballs.PokeBall;
      }
   }

   public static EnumGrowth getEggGrowth(Pokemon parent1, Pokemon parent2) {
      double averageOrdinal = (double)((float)(parent1.getGrowth().scaleOrdinal + parent2.getGrowth().scaleOrdinal) / 2.0F);
      int ordinal;
      if ((double)((int)averageOrdinal) != averageOrdinal) {
         ordinal = (int)(RandomHelper.getRandomChance() ? Math.floor(averageOrdinal) : Math.ceil(averageOrdinal));
      } else {
         ordinal = (int)averageOrdinal;
      }

      ordinal = MathHelper.func_76125_a(ordinal, 1, 7);
      int rand = RandomHelper.getRandomNumberBetween(ordinal - 1, ordinal + 1);
      return EnumGrowth.getGrowthFromScaleOrdinal(rand);
   }

   public static Integer getEggAbilitySlot(Pokemon child, Pokemon parent1, Pokemon parent2) {
      String[] abilities = child.getBaseStats().getAbilitiesArray();
      boolean is1Ditto = parent1.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto});
      boolean is2Ditto = parent2.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto});
      int intPercent = 80;
      Pokemon inheritParent;
      if (!is1Ditto && !is2Ditto) {
         if (parent1.getGender() == Gender.Female) {
            inheritParent = parent1;
         } else {
            inheritParent = parent2;
         }
      } else {
         if (is1Ditto && is2Ditto) {
            return abilities[1] != null ? RandomHelper.getRandomNumberBetween(0, 1) : 0;
         }

         inheritParent = is1Ditto ? parent2 : parent1;
         if (inheritParent.getGender() == Gender.Male || inheritParent.getAbilitySlot() == 2) {
            intPercent = 60;
         }
      }

      if (inheritParent.getFormEnum() == RegionalForms.ALOLAN && child.getFormEnum() != RegionalForms.ALOLAN) {
         intPercent = 0;
      }

      if (inheritParent.getFormEnum() == RegionalForms.NORMAL && child.getFormEnum() == RegionalForms.ALOLAN) {
         intPercent = 0;
      }

      if (RandomHelper.getRandomChance(intPercent)) {
         int currentSlot = inheritParent.getAbilitySlot();
         return currentSlot == -1 ? abilities[1] != null ? RandomHelper.getRandomNumberBetween(0, 1) : 0 : abilities[currentSlot] != null ? currentSlot : 0;
      } else {
         return abilities[1] != null ? RandomHelper.getRandomNumberBetween(0, 1) : 0;
      }
   }

   public static boolean getEggIsShiny(Pokemon parent1, Pokemon parent2) {
      float intDifferentTrainerFactor = 1.0F;
      if (!Objects.equals(parent1.getOriginalTrainerUUID(), parent2.getOriginalTrainerUUID())) {
         intDifferentTrainerFactor = 6.0F;
      }

      if (Pixelmon.storageManager.getParty(parent1.getOwnerPlayerUUID()).getShinyCharm().isActive()) {
         intDifferentTrainerFactor *= 3.0F;
      }

      return PixelmonConfig.getShinyRate(0) != 0.0F && RandomHelper.rand.nextFloat() < intDifferentTrainerFactor / PixelmonConfig.getShinyRate(0);
   }

   public static Moveset getEggMoveset(Pokemon egg, EnumSpecies pokemonSpecies, Pokemon parent1, Pokemon parent2) {
      ArrayList possibleEggMoves = egg.getBaseStats().getEggMoves();
      ArrayList lvl1Moves = egg.getBaseStats().getMovesAtLevel(1);
      ArrayList levelupMoves = getLevelupMoves(egg, parent1, parent2);
      Pokemon father = findFather(parent1, parent2);
      ArrayList fathersTMHMTutorMoves = getFathersTMHMTutorMoves(egg, father);
      ArrayList fathersEggMoves = getEggMoves(egg, father, possibleEggMoves);
      ArrayList mothersEggMoves = getEggMoves(egg, findMother(parent1, parent2), possibleEggMoves);
      ArrayList masterAttackList = new ArrayList();
      if (canLearnVoltTackle(pokemonSpecies, parent1, parent2)) {
         Attack voltTackle = new Attack("Volt Tackle");
         masterAttackList.add(voltTackle);
         possibleEggMoves.add(voltTackle);
      }

      Predicate doesNotContain = (attack) -> {
         return !masterAttackList.contains(attack);
      };
      mothersEggMoves.stream().filter(doesNotContain).forEach(masterAttackList::add);
      fathersEggMoves.stream().filter(doesNotContain).forEach(masterAttackList::add);
      fathersTMHMTutorMoves.stream().filter(doesNotContain).forEach(masterAttackList::add);
      levelupMoves.stream().filter(doesNotContain).forEach(masterAttackList::add);
      lvl1Moves.stream().filter(doesNotContain).forEach(masterAttackList::add);
      Moveset moveset = getFirstFourMoves(egg, masterAttackList);
      Iterator var14 = moveset.iterator();

      while(var14.hasNext()) {
         Attack move = (Attack)var14.next();
         if (move != null && possibleEggMoves.contains(move) && !egg.getRelearnableMoves().contains(move.getActualMove().getAttackId())) {
            egg.getRelearnableMoves().add(move.getActualMove().getAttackId());
         }
      }

      return moveset;
   }

   public static ArrayList getLevelupMoves(Pokemon egg, Pokemon parent1, Pokemon parent2) {
      ArrayList allBabyAttacks = egg.getBaseStats().getMovesUpToLevel(100);
      ArrayList pixelmon1Attacks = new ArrayList(Arrays.asList(parent1.getMoveset().attacks));
      ArrayList pixelmon2Attacks = new ArrayList(Arrays.asList(parent2.getMoveset().attacks));
      ArrayList levelupMoves = (ArrayList)allBabyAttacks.stream().filter((attack) -> {
         return pixelmon1Attacks.contains(attack) && pixelmon2Attacks.contains(attack);
      }).collect(Collectors.toList());
      return levelupMoves;
   }

   public static Moveset getFirstFourMoves(Pokemon pokemon, ArrayList masterAttackList) {
      Moveset moveset = (new Moveset()).withPokemon(pokemon);
      moveset.set(0, (Attack)(new Attack("Tackle")));
      moveset.set(1, (Attack)null);
      moveset.set(2, (Attack)null);
      moveset.set(3, (Attack)null);
      if (!masterAttackList.isEmpty()) {
         for(int i = 0; i < Math.min(4, masterAttackList.size()); ++i) {
            if (masterAttackList.get(i) != null) {
               moveset.set(i, (Attack)masterAttackList.get(i));
            }
         }
      }

      return moveset;
   }

   public static ArrayList getEggMoves(Pokemon egg, Pokemon parent, ArrayList allEggMoves) {
      ArrayList eggMoves = new ArrayList();
      if (parent != null) {
         ArrayList allParentsMoves = new ArrayList(Arrays.asList(parent.getMoveset().attacks));
         Stream var10001 = allParentsMoves.stream();
         allEggMoves.getClass();
         eggMoves.addAll((Collection)var10001.filter(allEggMoves::contains).collect(Collectors.toList()));
      }

      return eggMoves;
   }

   public static boolean canLearnVoltTackle(EnumSpecies species, Pokemon parent1, Pokemon parent2) {
      if (species != EnumSpecies.Pichu) {
         return false;
      } else {
         return parent1.getHeldItemAsItemHeld() == PixelmonItemsHeld.lightBall || parent2.getHeldItemAsItemHeld() == PixelmonItemsHeld.lightBall;
      }
   }

   public static ArrayList getFathersTMHMTutorMoves(Pokemon egg, Pokemon father) {
      ArrayList tmhmTutorMoves = new ArrayList();
      if (father != null) {
         LinkedHashSet allBabyTMHMTutorMoves = egg.getBaseStats().getTMHMMoves();
         allBabyTMHMTutorMoves.addAll(egg.getBaseStats().getTutorMoves());
         allBabyTMHMTutorMoves.addAll(egg.getBaseStats().getTransferMoves());
         ArrayList allFathersMoves = new ArrayList(Arrays.asList(father.getMoveset().attacks));
         Stream var10001 = allFathersMoves.stream();
         allBabyTMHMTutorMoves.getClass();
         tmhmTutorMoves.addAll((Collection)var10001.filter(allBabyTMHMTutorMoves::contains).collect(Collectors.toList()));
      }

      return tmhmTutorMoves;
   }

   public static Pokemon findFather(Pokemon parent1, Pokemon parent2) {
      boolean is1Ditto = parent1.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto});
      boolean is2Ditto = parent2.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto});
      if (!is1Ditto && !is2Ditto) {
         return parent1.getGender() == Gender.Male ? parent1 : parent2;
      } else if (is1Ditto && parent2.getGender() != Gender.Female) {
         return parent2;
      } else {
         return is2Ditto && parent1.getGender() != Gender.Female ? parent1 : null;
      }
   }

   public static Pokemon findMother(Pokemon parent1, Pokemon parent2) {
      boolean is1Ditto = parent1.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto});
      boolean is2Ditto = parent1.isPokemon(new EnumSpecies[]{EnumSpecies.Ditto});
      if (!is1Ditto && !is2Ditto) {
         return parent1.getGender() == Gender.Female ? parent1 : parent2;
      } else if (is1Ditto && parent2.getGender() != Gender.Male) {
         return parent2;
      } else {
         return is2Ditto && parent1.getGender() != Gender.Male ? parent1 : null;
      }
   }

   private static boolean checkIncense(Item itemTypeParent, Item itemTypeOther, Pokemon parentForEggLine, Item neededItem, EnumSpecies baby, EnumSpecies... parents) {
      if (!parentForEggLine.isPokemon(parents)) {
         return false;
      } else if (itemTypeParent != neededItem && itemTypeOther != neededItem) {
         return true;
      } else {
         return PixelmonConfig.isGenerationEnabled(parentForEggLine.getSpecies().getGeneration()) && !PixelmonConfig.isGenerationEnabled(baby.getGeneration());
      }
   }
}

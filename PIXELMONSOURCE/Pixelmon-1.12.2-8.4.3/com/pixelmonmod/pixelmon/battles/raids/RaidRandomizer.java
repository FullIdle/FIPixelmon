package com.pixelmonmod.pixelmon.battles.raids;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.raids.RandomizeRaidAllyEvent;
import com.pixelmonmod.pixelmon.api.events.raids.RandomizeRaidEvent;
import com.pixelmonmod.pixelmon.api.events.raids.RegisterRaidAllyEvent;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.npcs.registry.RaidSpawningRegistry;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumSlowbro;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.item.Item;
import net.minecraft.util.Tuple;
import net.minecraft.world.biome.Biome;

public class RaidRandomizer {
   private static final ArrayList possibleAllies = new ArrayList();

   public static Optional getRandomRaid(EntityDen den, boolean force) {
      if (!force) {
         RandomizeRaidEvent.RollChance rollEvent = new RandomizeRaidEvent.RollChance(den, PixelmonConfig.denRespawnChance);
         if (Pixelmon.EVENT_BUS.post(rollEvent) || !RandomHelper.getRandomChance(rollEvent.getChance())) {
            return Optional.empty();
         }
      }

      int stars = RandomHelper.getRandomIndexFromWeights(PixelmonConfig.denStarWeights) + 1;
      RandomizeRaidEvent.ChooseStarLevel starEvent = new RandomizeRaidEvent.ChooseStarLevel(den, stars);
      if (Pixelmon.EVENT_BUS.post(starEvent)) {
         return Optional.empty();
      } else {
         Biome biome = den.func_130014_f_().func_180494_b(den.func_180425_c());
         String biomeName = den.func_130014_f_().func_180494_b(den.func_180425_c()).field_76791_y.toLowerCase(Locale.ROOT).replace("_", " ");
         HashMap raids = (HashMap)RaidSpawningRegistry.map.get(biomeName);
         if (raids == null && biome.getRegistryName() != null) {
            raids = (HashMap)RaidSpawningRegistry.map.get(biome.getRegistryName().toString().toLowerCase(Locale.ROOT).replace("_", " "));
         }

         if (raids == null) {
            return Optional.empty();
         } else {
            Tuple data = (Tuple)RandomHelper.getRandomElementFromCollection((Collection)raids.get(starEvent.getStars()));
            RaidData raid = data == null ? null : new RaidData(den.func_145782_y(), starEvent.getStars(), (EnumSpecies)data.func_76341_a(), (IEnumForm)((Optional)data.func_76340_b()).orElse(RandomHelper.getRandomElementFromCollection(((EnumSpecies)data.func_76341_a()).getDefaultForms())));
            RandomizeRaidEvent.ChooseSpecies speciesEvent = new RandomizeRaidEvent.ChooseSpecies(den, raid);
            if (Pixelmon.EVENT_BUS.post(speciesEvent)) {
               return Optional.empty();
            } else {
               return Optional.ofNullable(speciesEvent.getRaid());
            }
         }
      }
   }

   public static RaidPokemon getRandomAlly(RaidData raid) {
      if (possibleAllies.isEmpty()) {
         RegisterRaidAllyEvent event = new RegisterRaidAllyEvent(possibleAllies);
         Pixelmon.EVENT_BUS.post(event);
         if (event.shouldUseDefaults()) {
            possibleAllies.add(new RaidPokemon("Noah", EnumSpecies.Qwilfish, PixelmonItemsHeld.focusSash, 2, AttackBase.getAttackBases("Brine", "Waterfall", "Poison Jab", "Pin Missile")));
            possibleAllies.add(new RaidPokemon("Dean", EnumSpecies.Throh, (Item)null, 1, AttackBase.getAttackBases("Mega Punch", "Revenge", "Bind", "Brick Break")));
            possibleAllies.add(new RaidPokemon("Martin", EnumSpecies.Solrock, (Item)null, 0, AttackBase.getAttackBases("Cosmic Power", "Rock Throw", "Psychic", "Rock Polish")));
            possibleAllies.add(new RaidPokemon("Kit", EnumSpecies.Dhelmise, (Item)null, 0, AttackBase.getAttackBases("Gyro Ball", "Slam", "Shadow Ball", "Giga Drain")));
            possibleAllies.add(new RaidPokemon("Portia", EnumSpecies.Heatmor, (Item)null, 0, AttackBase.getAttackBases("Fire Lash", "Bind", "Thunder Punch", "Slash")));
            possibleAllies.add(new RaidPokemon("Sophie", EnumSpecies.Maractus, (Item)null, 0, AttackBase.getAttackBases("Sucker Punch", "Giga Drain", "Solar Beam", "Pin Missile")));
            possibleAllies.add(new RaidPokemon("Catherine", EnumSpecies.Togepi, PixelmonItemsHeld.focusSash, 1, AttackBase.getAttackBases("Life Dew", "Ancient Power", "Draining Kiss", "Swift")));
            possibleAllies.add(new RaidPokemon("Janet", EnumSpecies.Snorlax, (Item)null, 0, AttackBase.getAttackBases("Giga Impact", "Body Slam", "Crunch", "Stomping Tantrum")));
            possibleAllies.add(new RaidPokemon("Sean", EnumSpecies.Torkoal, (Item)null, 1, AttackBase.getAttackBases("Rapid Spin", "Flame Wheel", "Flamethrower", "Clear Smog")));
            possibleAllies.add(new RaidPokemon("Patricia", EnumSpecies.Wishiwashi, (Item)null, 0, AttackBase.getAttackBases("Liquidation", "Aqua Tail", "Whirlpool", "Brine")));
            possibleAllies.add(new RaidPokemon("William", EnumSpecies.Mudbray, PixelmonItemsHeld.focusSash, 0, AttackBase.getAttackBases("Strength", "Rock Tomb", "High Horsepower", "Superpower")));
            possibleAllies.add(new RaidPokemon("Isabella", EnumSpecies.Magikarp, PixelmonItemsHeld.focusSash, 0, AttackBase.getAttackBases("Flail", "Tackle", "Hydro Pump")));
            possibleAllies.add(new RaidPokemon("Nicki", EnumSpecies.Jolteon, (Item)null, 0, AttackBase.getAttackBases("Double Kick", "Quick Attack", "Electro Ball", "Swift")));
            possibleAllies.add(new RaidPokemon("Alfie", EnumSpecies.Wobbuffet, (Item)null, 0, AttackBase.getAttackBases("Counter", "Mirror Coat", "Safeguard", "Amnesia")));
            possibleAllies.add(new RaidPokemon("Oscar", EnumSpecies.Hawlucha, (Item)null, 0, AttackBase.getAttackBases("Wing Attack", "Feather Dance", "Flying Press", "Thunder Punch")));
            possibleAllies.add(new RaidPokemon("Amelia", EnumSpecies.Clefairy, PixelmonItemsHeld.focusSash, 1, AttackBase.getAttackBases("Life Dew", "Follow Me", "Dazzling Gleam", "Disarming Voice")));
            possibleAllies.add(new RaidPokemon("Poppy", EnumSpecies.Pikachu, PixelmonItemsHeld.focusSash, 0, AttackBase.getAttackBases("Thunderbolt", "Quick Attack", "Iron Tail", "Electroweb")));
            possibleAllies.add(new RaidPokemon("Ivy", EnumSpecies.Weavile, (Item)null, 0, AttackBase.getAttackBases("Metal Claw", "Slash", "Assurance", "Ice Shard")));
            possibleAllies.add(new RaidPokemon("Freya", EnumSpecies.Eevee, PixelmonItemsHeld.focusSash, 1, AttackBase.getAttackBases("Helping Hand", "Round", "Quick Attack", "Bite")));
            possibleAllies.add(new RaidPokemon("Arthur", EnumSpecies.Salazzle, (Item)null, 0, AttackBase.getAttackBases("Flamethrower", "Fire Lash", "Sludge Bomb")));
            possibleAllies.add((new RaidPokemon("Klara", EnumSpecies.Slowbro, (Item)null, 0, AttackBase.getAttackBases("Scald", "Shell Side Arm", "Focus Blast", "Psychic"))).setForm(EnumSlowbro.Galarian));
         }
      }

      RaidPokemon ally = (RaidPokemon)CollectionHelper.getRandomElement((List)possibleAllies);
      RandomizeRaidAllyEvent allyEvent = new RandomizeRaidAllyEvent(raid, ally);
      Pixelmon.EVENT_BUS.post(allyEvent);
      return allyEvent.getAlly();
   }

   public static void addAlly(RaidPokemon pokemon) {
      possibleAllies.add(pokemon);
   }

   public static void clearAllyInfo() {
      possibleAllies.clear();
   }
}

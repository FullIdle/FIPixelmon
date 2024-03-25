package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SkyBattle extends BattleClause {
   private static final Set ALLOWED_POKEMON = new HashSet();
   private static final Set PROHIBITED_MOVES = new HashSet();

   SkyBattle() {
      super("sky");
   }

   public boolean validateSingle(Pokemon pokemon) {
      return ALLOWED_POKEMON.contains(pokemon.getBaseStats().getSpecies());
   }

   public static boolean isMoveAllowed(Attack move) {
      return !PROHIBITED_MOVES.contains(move.getMove().getAttackName());
   }

   static {
      ALLOWED_POKEMON.addAll(Arrays.asList(EnumSpecies.Charizard, EnumSpecies.Butterfree, EnumSpecies.Pidgeotto, EnumSpecies.Pidgeot, EnumSpecies.Fearow, EnumSpecies.Zubat, EnumSpecies.Golbat, EnumSpecies.Gastly, EnumSpecies.Haunter, EnumSpecies.Koffing, EnumSpecies.Weezing, EnumSpecies.Scyther, EnumSpecies.Gyarados, EnumSpecies.Aerodactyl, EnumSpecies.Articuno, EnumSpecies.Zapdos, EnumSpecies.Moltres, EnumSpecies.Dragonite, EnumSpecies.Noctowl, EnumSpecies.Ledyba, EnumSpecies.Ledian, EnumSpecies.Crobat, EnumSpecies.Togetic, EnumSpecies.Xatu, EnumSpecies.Hoppip, EnumSpecies.Skiploom, EnumSpecies.Jumpluff, EnumSpecies.Yanma, EnumSpecies.Misdreavus, EnumSpecies.Unown, EnumSpecies.Gligar, EnumSpecies.Mantine, EnumSpecies.Skarmory, EnumSpecies.Lugia, EnumSpecies.Hooh, EnumSpecies.Beautifly, EnumSpecies.Swellow, EnumSpecies.Wingull, EnumSpecies.Pelipper, EnumSpecies.Masquerain, EnumSpecies.Ninjask, EnumSpecies.Vibrava, EnumSpecies.Flygon, EnumSpecies.Swablu, EnumSpecies.Altaria, EnumSpecies.Lunatone, EnumSpecies.Solrock, EnumSpecies.Baltoy, EnumSpecies.Claydol, EnumSpecies.Duskull, EnumSpecies.Tropius, EnumSpecies.Chimecho, EnumSpecies.Salamence, EnumSpecies.Latias, EnumSpecies.Latios, EnumSpecies.Rayquaza, EnumSpecies.Staravia, EnumSpecies.Staraptor, EnumSpecies.Mothim, EnumSpecies.Combee, EnumSpecies.Vespiquen, EnumSpecies.Drifloon, EnumSpecies.Drifblim, EnumSpecies.Mismagius, EnumSpecies.Honchkrow, EnumSpecies.Chingling, EnumSpecies.Mantyke, EnumSpecies.Togekiss, EnumSpecies.Yanmega, EnumSpecies.Gliscor, EnumSpecies.Uxie, EnumSpecies.Mesprit, EnumSpecies.Azelf, EnumSpecies.Sigilyph, EnumSpecies.Archeops, EnumSpecies.Emolga, EnumSpecies.Cryogonal, EnumSpecies.Braviary));
      PROHIBITED_MOVES.addAll(Arrays.asList("Body Slam", "Bulldoze", "Dig", "Dive", "Earth Power", "Earthquake", "Electric Terrain", "Fissure", "Fire Pledge", "Flying Press", "Frenzy Plant", "Geomancy", "Grass Knot", "Grass Pledge", "Grassy Terrain", "Gravity", "Heat Crash", "Heavy Slam", "Ingrain", "Land's Wrath", "Magnitude", "Mat Block", "Misty Terrain", "Mud Sport", "Muddy Water", "Rototiller", "Seismic Toss", "Slam", "Smack Down", "Spikes", "Stomp", "Substitute", "Surf", "Toxic Spikes", "Thousand Arrows", "Thousand Waves", "Water Pledge", "Water Sport"));
   }
}

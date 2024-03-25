package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.enums.forms.EnumMeowth;
import com.pixelmonmod.pixelmon.enums.forms.EnumToxtricity;
import com.pixelmonmod.pixelmon.enums.forms.EnumUrshifu;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.Iterator;

public enum EnumGigantamaxPokemon {
   Charizard(EnumSpecies.Charizard, new IEnumForm[]{null}, new String[]{"G-Max Wildfire"}, new EnumType[]{EnumType.Fire}),
   Butterfree(EnumSpecies.Butterfree, new IEnumForm[]{null}, new String[]{"G-Max Befuddle"}, new EnumType[]{EnumType.Bug}),
   Pikachu(EnumSpecies.Pikachu, new IEnumForm[]{null}, new String[]{"G-Max Volt Crash"}, new EnumType[]{EnumType.Electric}),
   Meowth(EnumSpecies.Meowth, new IEnumForm[]{EnumMeowth.Normal}, new String[]{"G-Max Gold Rush"}, new EnumType[]{EnumType.Normal}),
   Machamp(EnumSpecies.Machamp, new IEnumForm[]{null}, new String[]{"G-Max Chi Strike"}, new EnumType[]{EnumType.Fighting}),
   Gengar(EnumSpecies.Gengar, new IEnumForm[]{null}, new String[]{"G-Max Terror"}, new EnumType[]{EnumType.Ghost}),
   Kingler(EnumSpecies.Kingler, new IEnumForm[]{null}, new String[]{"G-Max Foam Burst"}, new EnumType[]{EnumType.Water}),
   Lapras(EnumSpecies.Lapras, new IEnumForm[]{null}, new String[]{"G-Max Resonance"}, new EnumType[]{EnumType.Ice}),
   Eevee(EnumSpecies.Eevee, new IEnumForm[]{null}, new String[]{"G-Max Cuddle"}, new EnumType[]{EnumType.Normal}),
   Snorlax(EnumSpecies.Snorlax, new IEnumForm[]{null}, new String[]{"G-Max Replenish"}, new EnumType[]{EnumType.Normal}),
   Garbodor(EnumSpecies.Garbodor, new IEnumForm[]{null}, new String[]{"G-Max Malodor"}, new EnumType[]{EnumType.Poison}),
   Melmetal(EnumSpecies.Melmetal, new IEnumForm[]{null}, new String[]{"G-Max Meltdown"}, new EnumType[]{EnumType.Steel}),
   Corviknight(EnumSpecies.Corviknight, new IEnumForm[]{null}, new String[]{"G-Max Wind Rage"}, new EnumType[]{EnumType.Flying}),
   Orbeetle(EnumSpecies.Orbeetle, new IEnumForm[]{null}, new String[]{"G-Max Gravitas"}, new EnumType[]{EnumType.Psychic}),
   Drednaw(EnumSpecies.Drednaw, new IEnumForm[]{null}, new String[]{"G-Max Stonesurge"}, new EnumType[]{EnumType.Water}),
   Coalossal(EnumSpecies.Coalossal, new IEnumForm[]{null}, new String[]{"G-Max Volcalith"}, new EnumType[]{EnumType.Rock}),
   Flapple(EnumSpecies.Flapple, new IEnumForm[]{null}, new String[]{"G-Max Tartness"}, new EnumType[]{EnumType.Grass}),
   Appletun(EnumSpecies.Appletun, new IEnumForm[]{null}, new String[]{"G-Max Sweetness"}, new EnumType[]{EnumType.Grass}),
   Sandaconda(EnumSpecies.Sandaconda, new IEnumForm[]{null}, new String[]{"G-Max Sandblast"}, new EnumType[]{EnumType.Ground}),
   Toxtricity(EnumSpecies.Toxtricity, new IEnumForm[]{EnumToxtricity.AMPED, EnumToxtricity.LOWKEY}, new String[]{"G-Max Stun Shock"}, new EnumType[]{EnumType.Electric}),
   Centiskorch(EnumSpecies.Centiskorch, new IEnumForm[]{null}, new String[]{"G-Max Centiferno"}, new EnumType[]{EnumType.Fire}),
   Hatterene(EnumSpecies.Hatterene, new IEnumForm[]{null}, new String[]{"G-Max Smite"}, new EnumType[]{EnumType.Fairy}),
   Grimmsnarl(EnumSpecies.Grimmsnarl, new IEnumForm[]{null}, new String[]{"G-Max Snooze"}, new EnumType[]{EnumType.Dark}),
   Alcremie(EnumSpecies.Alcremie, new IEnumForm[]{null}, new String[]{"G-Max Finale"}, new EnumType[]{EnumType.Fairy}),
   Copperajah(EnumSpecies.Copperajah, new IEnumForm[]{null}, new String[]{"G-Max Steelsurge"}, new EnumType[]{EnumType.Steel}),
   Duraludon(EnumSpecies.Duraludon, new IEnumForm[]{null}, new String[]{"G-Max Depletion"}, new EnumType[]{EnumType.Dragon}),
   Venusaur(EnumSpecies.Venusaur, new IEnumForm[]{null}, new String[]{"G-Max Vine Lash"}, new EnumType[]{EnumType.Grass}),
   Blastoise(EnumSpecies.Blastoise, new IEnumForm[]{null}, new String[]{"G-Max Cannonade"}, new EnumType[]{EnumType.Water}),
   Rillaboom(EnumSpecies.Rillaboom, new IEnumForm[]{null}, new String[]{"G-Max Drum Solo"}, new EnumType[]{EnumType.Grass}),
   Cinderace(EnumSpecies.Cinderace, new IEnumForm[]{null}, new String[]{"G-Max Fireball"}, new EnumType[]{EnumType.Fire}),
   Inteleon(EnumSpecies.Inteleon, new IEnumForm[]{null}, new String[]{"G-Max Hydrosnipe"}, new EnumType[]{EnumType.Water}),
   Urshifu(EnumSpecies.Urshifu, new IEnumForm[]{EnumUrshifu.SingleStrike, EnumUrshifu.RapidStrike}, new String[]{"G-Max One Blow", "G-Max Rapid Flow"}, new EnumType[]{EnumType.Dark, EnumType.Water});

   private static final EnumGigantamaxPokemon[] VALUES = values();
   public EnumSpecies pokemon;
   public IEnumForm[] forms;
   public String[] gmaxMoves;
   public EnumType[] gmaxMovesTypes;

   private EnumGigantamaxPokemon(EnumSpecies pokemon, IEnumForm[] forms, String[] gmaxMoves, EnumType[] gmaxMovesTypes) {
      this.pokemon = pokemon;
      this.forms = forms;
      this.gmaxMoves = gmaxMoves;
      this.gmaxMovesTypes = gmaxMovesTypes;
   }

   public String getGmaxMoveIfPossible(EnumType type, int ordinal) {
      if (ordinal >= this.gmaxMoves.length) {
         return null;
      } else {
         return this.gmaxMovesTypes[ordinal] == type ? this.gmaxMoves[ordinal] : null;
      }
   }

   public static EnumGigantamaxPokemon getGigantamax(EnumSpecies pokemon) {
      EnumGigantamaxPokemon[] var1 = VALUES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumGigantamaxPokemon e = var1[var3];
         if (e.pokemon == pokemon) {
            return e;
         }
      }

      return null;
   }

   public static boolean hasGigantamaxForm(Pokemon pokemon, boolean checkEvolutions) {
      return hasGigantamaxForm(pokemon.getSpecies(), pokemon.getFormEnum(), checkEvolutions);
   }

   public static boolean hasGigantamaxForm(Pokemon pokemon) {
      return hasGigantamaxForm(pokemon, false);
   }

   public static boolean hasGigantamaxForm(EnumSpecies pokemon, IEnumForm form, boolean checkEvolutions) {
      if (checkEvolutions) {
         Iterator var3 = pokemon.getBaseStats(form).getEvolutions().iterator();

         while(var3.hasNext()) {
            Evolution evolution = (Evolution)var3.next();
            PokemonSpec spec = evolution.to;
            Pokemon evoInstance = Pixelmon.pokemonFactory.create(spec);
            if (hasGigantamaxForm(evoInstance.getSpecies(), evoInstance.getFormEnum(), true)) {
               return true;
            }
         }
      }

      return hasGigantamaxForm(pokemon, form);
   }

   public static boolean hasGigantamaxForm(EnumSpecies pokemon, IEnumForm form) {
      EnumGigantamaxPokemon gmax = getGigantamax(pokemon);
      if (gmax != null) {
         IEnumForm[] var3 = gmax.forms;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            IEnumForm gmaxForm = var3[var5];
            if (gmaxForm == null || gmaxForm == form) {
               return true;
            }
         }
      }

      return false;
   }

   public static int getNumberOfForms(EnumSpecies pokemon) {
      EnumGigantamaxPokemon gmax;
      return (gmax = getGigantamax(pokemon)) != null ? gmax.forms.length : 0;
   }
}

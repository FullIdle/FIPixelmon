package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

public class PartyCondition extends EvoCondition {
   public ArrayList withPokemon;
   public ArrayList withTypes;
   public ArrayList withForms;

   public PartyCondition() {
      super("party");
      this.withPokemon = new ArrayList();
      this.withTypes = new ArrayList();
      this.withForms = new ArrayList();
   }

   public PartyCondition(EnumSpecies... with) {
      this();
      EnumSpecies[] var2 = with;
      int var3 = with.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumSpecies pokemon = var2[var4];
         if (pokemon != null) {
            this.withPokemon.add(pokemon);
         }
      }

   }

   public PartyCondition(EnumType... types) {
      this();
      EnumType[] var2 = types;
      int var3 = types.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumType type = var2[var4];
         if (types != null) {
            this.withTypes.add(type);
         }
      }

   }

   public PartyCondition(String... forms) {
      this();
      String[] var2 = forms;
      int var3 = forms.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String form = var2[var4];
         this.withForms.add(form.toLowerCase());
      }

   }

   public boolean passes(EntityPixelmon pixelmon) {
      PlayerPartyStorage storage = pixelmon.getPlayerParty();
      if (storage != null) {
         Iterator var3 = storage.getTeam().iterator();

         while(var3.hasNext()) {
            Pokemon pokemon = (Pokemon)var3.next();
            EnumSpecies partyPokemon = pokemon.getSpecies();
            if (this.withPokemon.contains(partyPokemon)) {
               return true;
            }

            BaseStats bs = pokemon.getBaseStats();
            if (partyPokemon != null && (this.withTypes.contains(bs.getType1()) || bs.getType2() != null && this.withTypes.contains(bs.getType2()))) {
               return true;
            }

            Stream var10000 = this.withForms.stream();
            String var10001 = pokemon.getFormEnum().toString();
            var10001.getClass();
            if (var10000.anyMatch(var10001::equalsIgnoreCase)) {
               return true;
            }
         }
      }

      return false;
   }
}

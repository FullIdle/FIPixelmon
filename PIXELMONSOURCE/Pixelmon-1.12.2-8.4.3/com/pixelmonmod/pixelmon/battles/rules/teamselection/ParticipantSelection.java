package com.pixelmonmod.pixelmon.battles.rules.teamselection;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.storage.TrainerPartyStorage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ParticipantSelection {
   final PartyStorage storage;
   List team = new ArrayList();
   final boolean isNPC;
   boolean confirmed;
   String[] disabled = new String[6];

   ParticipantSelection(PartyStorage storage) {
      this.storage = storage;
      this.isNPC = storage instanceof TrainerPartyStorage;
      if (this.isNPC) {
         this.confirmed = true;
      }

   }

   void setTeam(int[] selection) {
      List team = new ArrayList();
      int[] var3 = selection;
      int s = selection.length;

      for(int var5 = 0; var5 < s; ++var5) {
         int s = var3[var5];
         if (s > -1) {
            team.add((Object)null);
         }
      }

      for(int i = 0; i < selection.length; ++i) {
         s = selection[i];
         if (s > -1) {
            Pokemon pokemon = this.storage.get(i);
            if (pokemon == null) {
               return;
            }

            team.set(s, pokemon);
         }
      }

      Iterator var8 = team.iterator();

      Pokemon pokemon;
      do {
         if (!var8.hasNext()) {
            this.team = team;
            return;
         }

         pokemon = (Pokemon)var8.next();
      } while(pokemon != null);

   }

   void addTeamMember(int index) {
      Pokemon pokemon = this.storage.get(index);
      if (pokemon != null && pokemon.canBattle()) {
         this.team.add(pokemon);
      }

   }

   void removeTeamMember() {
      if (!this.team.isEmpty()) {
         this.team.remove(this.team.size() - 1);
      }

   }
}

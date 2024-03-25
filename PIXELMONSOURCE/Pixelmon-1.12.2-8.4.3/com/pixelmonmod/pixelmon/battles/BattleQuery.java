package com.pixelmonmod.pixelmon.battles;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelectionList;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.EnumRulesGuiState;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BattleQueryPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.EnumBattleQueryResponse;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.CheckRulesVersionChoose;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.DisplayBattleQueryRules;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.SetProposedRules;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.UpdateBattleQueryRules;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;

public class BattleQuery {
   private static ArrayList queryList = new ArrayList();
   static int index = 0;
   private BattleQueryPlayer[] players;
   public int queryIndex;
   public BattleRules battleRules = new BattleRules();

   public BattleQuery(EntityPlayerMP player1, EntityPixelmon pokemon1, EntityPlayerMP player2, EntityPixelmon pokemon2) {
      this.players = new BattleQueryPlayer[]{new BattleQueryPlayer(player1, pokemon1), new BattleQueryPlayer(player2, pokemon2)};
      this.queryIndex = index++;
      this.sendQuery();
   }

   private void sendQuery() {
      PlayerPartyStorage[] storages = new PlayerPartyStorage[this.players.length];

      int i;
      for(i = 0; i < this.players.length; ++i) {
         storages[i] = Pixelmon.storageManager.getParty(this.players[i].player);
      }

      for(i = 0; i < this.players.length; ++i) {
         int other = i == 0 ? 1 : 0;
         BattleQueryPacket packet = new BattleQueryPacket(this.queryIndex, this.players[other].player.func_110124_au(), storages[other]);
         Pixelmon.network.sendTo(packet, this.players[i].player);
      }

      queryList.add(this);
   }

   public void acceptQuery(EntityPlayerMP player, EnumBattleQueryResponse response) {
      BattleQueryPlayer currentPlayer = null;
      BattleQueryPlayer[] var4 = this.players;
      int var5 = var4.length;

      BattleQueryPlayer otherPlayer;
      for(int var6 = 0; var6 < var5; ++var6) {
         otherPlayer = var4[var6];
         if (player == otherPlayer.player) {
            otherPlayer.response = response;
            currentPlayer = otherPlayer;
            break;
         }
      }

      if (currentPlayer != null) {
         boolean allConfirm = true;
         boolean allAccept = true;
         BattleQueryPlayer[] var14 = this.players;
         int var16 = var14.length;

         int clauseVersion;
         for(clauseVersion = 0; clauseVersion < var16; ++clauseVersion) {
            BattleQueryPlayer queryPlayer = var14[clauseVersion];
            if (!queryPlayer.response.isAcceptResponse()) {
               allConfirm = false;
            }

            if (queryPlayer.response != EnumBattleQueryResponse.Accept) {
               allAccept = false;
            }
         }

         if (allConfirm) {
            if (allAccept) {
               this.startBattle();
            } else {
               BattleQueryPlayer proposePlayer = this.players[currentPlayer == this.players[0] ? 1 : 0];
               if (proposePlayer.response != EnumBattleQueryResponse.Rules) {
                  proposePlayer = currentPlayer;
               }

               otherPlayer = proposePlayer;
               BattleQueryPlayer[] var17 = this.players;
               int var18 = var17.length;

               for(int var10 = 0; var10 < var18; ++var10) {
                  BattleQueryPlayer queryPlayer = var17[var10];
                  queryPlayer.response = queryPlayer == proposePlayer ? EnumBattleQueryResponse.Accept : EnumBattleQueryResponse.Rules;
                  if (queryPlayer != proposePlayer) {
                     otherPlayer = queryPlayer;
                  }
               }

               clauseVersion = BattleClauseRegistry.getClauseVersion();
               Pixelmon.network.sendTo(new CheckRulesVersionChoose(clauseVersion, new DisplayBattleQueryRules(this.queryIndex, true)), proposePlayer.player);
               Pixelmon.network.sendTo(new CheckRulesVersionChoose(clauseVersion, new DisplayBattleQueryRules(this.queryIndex, false)), otherPlayer.player);
            }
         }

      }
   }

   private void startBattle() {
      PartyStorage[] storages = new PartyStorage[this.players.length];

      int i;
      for(i = 0; i < this.players.length; ++i) {
         storages[i] = Pixelmon.storageManager.getParty(this.players[i].player);
      }

      if (this.battleRules.isDefault()) {
         for(i = 0; i < storages.length; ++i) {
            BattleQueryPlayer queryPlayer = this.players[i];
            if (queryPlayer.pokemon == null) {
               queryPlayer.pokemon = storages[i].findOne((pokemonx) -> {
                  return pokemonx.getHealth() > 0 && !pokemonx.isEgg() && pokemonx.getPixelmonIfExists() == null;
               }).getOrSpawnPixelmon(queryPlayer.player);
            }
         }

         if (this.battleRules.battleType == EnumBattleType.Single) {
            this.players[0].pokemon.startBattle(this.players[0].getParticipant(), this.players[1].getParticipant(), this.battleRules);
         } else {
            EntityPixelmon[] secondPokemon = new EntityPixelmon[this.players.length];
            boolean valid = true;

            for(int j = 0; j < secondPokemon.length; ++j) {
               PartyStorage storage = storages[j];
               BattleQueryPlayer player = this.players[j];

               for(int i = 0; i < 6; ++i) {
                  UUID pixId = null;
                  Pokemon pokemon = storage.get(i);
                  pixId = storage.get(i).getUUID();
                  if (!player.pokemon.getPokemonData().getUUID().equals(pixId) && pokemon.getHealth() > 0 && !pokemon.isEgg()) {
                     secondPokemon[j] = pokemon.getOrSpawnPixelmon(player.player);
                     break;
                  }
               }

               if (secondPokemon[j] == null) {
                  ChatHandler.sendChat(this.players[0].player, this.players[1].player, "gui.acceptdeny.invaliddouble", player.player.func_145748_c_());
                  valid = false;
                  break;
               }
            }

            if (valid) {
               this.players[0].pokemon.startBattle(this.players[0].getParticipant(secondPokemon[0]), this.players[0].getParticipant(secondPokemon[1]), this.battleRules);
            }
         }
      } else {
         TeamSelectionList.addTeamSelection(this.battleRules, false, storages);
      }

      queryList.remove(this);
   }

   public void declineQuery(EntityPlayerMP player) {
      boolean valid = false;
      BattleQueryPlayer[] var3 = this.players;
      int var4 = var3.length;

      int var5;
      BattleQueryPlayer queryPlayer;
      for(var5 = 0; var5 < var4; ++var5) {
         queryPlayer = var3[var5];
         if (player == queryPlayer.player) {
            valid = true;
            break;
         }
      }

      if (valid) {
         var3 = this.players;
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            queryPlayer = var3[var5];
            if (player != queryPlayer.player) {
               queryPlayer.player.func_71053_j();
            }
         }

         ChatHandler.sendChat(this.players[0].player, this.players[1].player, "battlequery.declined", player.getDisplayNameString());
         queryList.remove(this);
      }
   }

   public void proposeRules(EntityPlayerMP player, BattleRules rules) {
      boolean valid = false;
      BattleQueryPlayer[] var4 = this.players;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BattleQueryPlayer queryPlayer = var4[var6];
         if (player == queryPlayer.player) {
            queryPlayer.response = EnumBattleQueryResponse.Accept;
            valid = true;
            break;
         }
      }

      if (valid) {
         rules.validateRules();
         this.battleRules = rules;
         EntityPlayerMP otherPlayer = this.getOtherPlayer(player);
         Pixelmon.network.sendTo(new SetProposedRules(this.battleRules), otherPlayer);
         Pixelmon.network.sendTo(new UpdateBattleQueryRules(EnumRulesGuiState.WaitAccept), player);
         Pixelmon.network.sendTo(new UpdateBattleQueryRules(EnumRulesGuiState.Accept), otherPlayer);
      }
   }

   public void changeRules(EntityPlayerMP player) {
      Pixelmon.network.sendTo(new UpdateBattleQueryRules(EnumRulesGuiState.Propose), player);
      Pixelmon.network.sendTo(new UpdateBattleQueryRules(EnumRulesGuiState.WaitChange), this.getOtherPlayer(player));
   }

   private EntityPlayerMP getOtherPlayer(EntityPlayerMP player) {
      return player == this.players[0].player ? this.players[1].player : this.players[0].player;
   }

   public static BattleQuery getQuery(int index) {
      Iterator var1 = queryList.iterator();

      BattleQuery aQueryList;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         aQueryList = (BattleQuery)var1.next();
      } while(aQueryList.queryIndex != index);

      return aQueryList;
   }

   public static BattleQuery getQuery(EntityPlayerMP player) {
      Iterator var1 = queryList.iterator();

      while(var1.hasNext()) {
         BattleQuery aQueryList = (BattleQuery)var1.next();
         BattleQueryPlayer[] var3 = aQueryList.players;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            BattleQueryPlayer queryPlayer = var3[var5];
            if (queryPlayer.player == player) {
               return aQueryList;
            }
         }
      }

      return null;
   }
}

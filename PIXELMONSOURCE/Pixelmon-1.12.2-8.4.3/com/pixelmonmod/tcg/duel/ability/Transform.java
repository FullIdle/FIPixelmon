package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import org.lwjgl.util.vector.Vector3f;

public class Transform extends BaseAbilityEffect {
   public Transform() {
      super("Transform");
   }

   public boolean isPassive() {
      return true;
   }

   public boolean disableEvolution(PokemonCardState affecting, PokemonCardState pokemon, GameClientState client) {
      return pokemon == affecting;
   }

   public boolean onCondition(PokemonCardState pokemon, PokemonCardState attacker, CardCondition cardCondition, GameServerState server) {
      PlayerServerState[] var5 = server.getPlayers();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         PlayerServerState player = var5[var7];
         if (player.getActiveCard() == pokemon && !this.isEnabled(pokemon, new GameClientState(server)) && server.getGamePhase() != GamePhase.PreMatch) {
            pokemon.transform(pokemon.getData(), pokemon.getStatus());
            pokemon.setHiddenAbility((AbilityCard)null);
            pokemon.setOverrideModelColor((Vector3f)null);
            break;
         }
      }

      return true;
   }

   public void onPlay(PokemonCardState newPokemon, PlayerCommonState playingPlayer, PokemonCardState pokemon, PlayerServerState player, GameServerState server) {
      if (newPokemon == pokemon && player.getActiveCard() == pokemon && server.getOpponent(player).getActiveCard() != null && server.getGamePhase() != GamePhase.PreMatch) {
         pokemon.transform(server.getOpponent(player).getActiveCard().getData(), pokemon.getStatus());
         pokemon.setHiddenAbility(pokemon.getData().getAbility());
         pokemon.setOverrideModelColor(new Vector3f(0.44F, 0.25F, 0.63F));
      }

   }

   public void onStartGame(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState[] var3 = server.getPlayers();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PlayerServerState player = var3[var5];
         if (player.getActiveCard() == pokemon && this.isEnabled(pokemon, new GameClientState(server))) {
            pokemon.transform(server.getOpponent(player).getActiveCard().getData(), pokemon.getStatus());
            pokemon.setHiddenAbility(pokemon.getData().getAbility());
            pokemon.setOverrideModelColor(new Vector3f(0.44F, 0.25F, 0.63F));
            break;
         }
      }

   }

   public void onSwitchActiveCard(PokemonCardState newActive, PokemonCardState oldActive, PlayerCommonState switchingPlayer, PokemonCardState pokemon, PlayerServerState player, GameServerState server) {
      if (player.getActiveCard() == pokemon && server.getOpponent(player).getActiveCard() != null && (switchingPlayer != player || newActive == pokemon) && server.getGamePhase() != GamePhase.PreMatch) {
         pokemon.transform(server.getOpponent(player).getActiveCard().getData(), pokemon.getStatus());
         pokemon.setHiddenAbility(pokemon.getData().getAbility());
         pokemon.setOverrideModelColor(new Vector3f(0.44F, 0.25F, 0.63F));
      } else if (player.getActiveCard() != pokemon && server.getGamePhase() != GamePhase.PreMatch) {
         pokemon.transform(pokemon.getData(), pokemon.getStatus());
         pokemon.setHiddenAbility((AbilityCard)null);
         pokemon.setOverrideModelColor((Vector3f)null);
      }

   }

   public boolean ignoreEnergyTypes() {
      return true;
   }
}

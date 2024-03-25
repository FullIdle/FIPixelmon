package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import java.util.ArrayList;
import java.util.List;

public class Heal extends BaseAbilityEffect {
   public Heal() {
      super("Heal");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      return super.isEnabled(pokemon, client) && pokemon.getParameters().isEmpty() && client.getMe().getActiveAndBenchCards().stream().anyMatch((c) -> {
         return c.getStatus().getDamage() > 0;
      });
   }

   public List flipCoin() {
      List coins = new ArrayList();
      coins.add(CoinSide.getRandom());
      return coins;
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      if (this.isCorrectCoinSide(server)) {
         PlayerServerState player = server.getPlayer(server.getCurrentTurn());
         CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, "ability.heal.selector.pokemon");
         player.getActiveAndBenchCards(true).stream().filter((location) -> {
            return ((PokemonCardState)location.getCard()).getStatus().getDamage() > 0;
         }).forEach((location) -> {
            selector.getCardList().add(location);
         });
         return selector;
      } else {
         return null;
      }
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState server) {
      return !this.isCorrectCoinSide(server) || !pokemon.getParameters().isEmpty();
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      if (this.isCorrectCoinSide(server)) {
         PokemonCardState heal = (PokemonCardState)pokemon.getParameters().get(0);
         heal.getStatus().healDamage(10);
      }

      pokemon.getParameters().add(pokemon);
   }

   public void cleanUp(PokemonCardState pokemon, GameServerState server) {
   }

   private boolean isCorrectCoinSide(GameServerState server) {
      return server.getCoinFlip() != null && server.getCoinFlip().getResults().get(0) == CoinSide.Head;
   }
}

package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Gambler extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return true;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return true;
   }

   public List flipCoin() {
      List coin = new ArrayList();
      coin.add(CoinSide.getRandom());
      return coin;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      Iterator var4 = me.getHand().iterator();

      while(var4.hasNext()) {
         ImmutableCard c = (ImmutableCard)var4.next();
         me.getDeck().add(c);
      }

      me.getHand().clear();
      LogicHelper.shuffleCardList(me.getDeck());
      if (this.isCorrectCoinSide(server)) {
         me.drawCards(8, server);
      } else {
         me.drawCards(1, server);
      }

   }

   private boolean isCorrectCoinSide(GameServerState server) {
      return server.getCoinFlip() != null && server.getCoinFlip().getResults().get(0) == CoinSide.Head;
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

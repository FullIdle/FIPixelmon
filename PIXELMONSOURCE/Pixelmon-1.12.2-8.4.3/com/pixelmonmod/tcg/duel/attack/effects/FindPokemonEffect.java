package com.pixelmonmod.tcg.duel.attack.effects;

import com.google.common.collect.Lists;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.Iterator;
import java.util.List;

public class FindPokemonEffect extends BaseAttackEffect {
   private static final String CODE = "FIND_POKEMON";
   private int count;
   private Type type;
   private List pokedexNumbers;
   private Energy energy;

   public FindPokemonEffect() {
      super("FIND_POKEMON");
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      int emptySlot = 0;
      PokemonCardState[] var5 = player.getBenchCards();
      int var6 = var5.length;

      int i;
      for(i = 0; i < var6; ++i) {
         PokemonCardState card = var5[i];
         if (card == null) {
            ++emptySlot;
         }
      }

      int realCount = Math.min(this.count, emptySlot);
      if (realCount == 0) {
         return null;
      } else {
         CardSelectorState selector = new CardSelectorState(1, realCount, CardSelectorDisplay.Draw, false);

         for(i = 0; i < player.getDeck().size(); ++i) {
            ImmutableCard card = (ImmutableCard)player.getDeck().get(i);
            if (card.isPokemonCard() && (this.type == FindPokemonEffect.Type.ByPokemon && this.pokedexNumbers.contains(card.getPokemonID()) || this.type == FindPokemonEffect.Type.ByType && card.getMainEnergy() == this.energy && card.getCardType() == CardType.BASIC)) {
               selector.getCardList().add(new CardWithLocation(new CommonCardState(card), true, BoardLocation.Deck, i));
            }
         }

         if (selector.getCardList().isEmpty()) {
            return null;
         } else {
            return selector;
         }
      }
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      CardSelectorState selector = this.getSelectorState(parameters, server);
      return selector == null || parameters.size() > 0;
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (parameters != null) {
         PlayerServerState player = server.getPlayer(server.getCurrentTurn());

         CommonCardState selected;
         for(Iterator var6 = parameters.iterator(); var6.hasNext(); player.getDeck().remove(selected.getData())) {
            Object obj = var6.next();
            selected = (CommonCardState)obj;

            for(int i = 0; i < player.getBenchCards().length; ++i) {
               if (player.getBenchCards()[i] == null) {
                  player.getBenchCards()[i] = new PokemonCardState(selected.getData(), server.getTurnCount());
                  break;
               }
            }
         }

         LogicHelper.shuffleCardList(player.getDeck());
      }
   }

   public BaseAttackEffect parse(String... args) {
      this.count = Integer.parseInt(args[1]);
      this.pokedexNumbers = null;
      this.energy = null;
      if (args[2].equalsIgnoreCase("POKEMON")) {
         this.type = FindPokemonEffect.Type.ByPokemon;
         this.pokedexNumbers = Lists.newArrayList();
         String[] var2 = args[3].split(",");
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String idString = var2[var4];
            this.pokedexNumbers.add(Integer.parseInt(idString));
         }
      } else {
         this.type = FindPokemonEffect.Type.ByType;
         this.energy = Energy.getEnergyFromString(args[3]);
      }

      return super.parse(args);
   }

   public static enum Type {
      ByPokemon,
      ByType;
   }
}

package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.dto.CustomGUI;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CustomGUIResult;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import java.util.List;

public class SwitchPokemonEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String[] CODES = new String[]{"SELF_SWITCH", "OPP_SWITCH"};
   private boolean myAction;
   private boolean myPokemons;
   private boolean isOptional;

   public SwitchPokemonEffect() {
      super(CODES);
   }

   public boolean isOptional() {
      return this.isOptional;
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      if (this.isOptional) {
         PlayerServerState player = server.getPlayer(server.getCurrentTurn());
         if (player.getCustomGUIResult() == null || player.getCustomGUIResult().getResult() == null || player.getCustomGUIResult().getResult().length == 0) {
            return false;
         }

         player.setCustomGUI((CustomGUI)null);
      }

      if (this.isOptional && server.getPlayer(server.getCurrentTurn()).getCustomGUIResult().getResult()[0] == 2) {
         return true;
      } else {
         PokemonCardState[] benchCards = server.getPlayer(this.myPokemons ? server.getCurrentTurn() : server.getNextTurn()).getBenchCards();
         PokemonCardState[] var5 = benchCards;
         int var6 = benchCards.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            PokemonCardState card = var5[var7];
            if (card != null) {
               return parameters.size() == 1;
            }
         }

         return true;
      }
   }

   public CustomGUI getCustomGUI(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      if (player.getCustomGUIResult() != null && player.getCustomGUIResult().getResult() != null && player.getCustomGUIResult().getResult().length != 0) {
         return null;
      } else {
         CustomGUIResult defaultResult = new CustomGUIResult();
         defaultResult.setOpened(true);
         defaultResult.setResult(new int[]{1});
         return new CustomGUI("GUI_OPTIONAL", defaultResult);
      }
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      return this.myAction ? SelectorHelper.generateSelectorForBench(server.getPlayer(this.myPokemons ? server.getCurrentTurn() : server.getNextTurn()), (String)null) : null;
   }

   public CardSelectorState getOpponentSelectorState(GameServerState server) {
      return !this.myAction ? SelectorHelper.generateSelectorForBench(server.getPlayer(this.myPokemons ? server.getCurrentTurn() : server.getNextTurn()), "attack.effect.switch.byopp") : null;
   }

   public void applyOnCorrectCoinSideAfterDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (!parameters.isEmpty() && (!this.isOptional || server.getPlayer(server.getCurrentTurn()).getCustomGUIResult().getResult()[0] == 1)) {
         PokemonCardState bench = (PokemonCardState)parameters.get(0);
         server.getPlayer(this.myPokemons ? server.getCurrentTurn() : server.getNextTurn()).switchActive(bench, server);
      }

      server.getPlayer(server.getCurrentTurn()).setCustomGUIResult((CustomGUIResult)null);
      server.getPlayer(server.getCurrentTurn()).setCustomGUI((CustomGUI)null);
   }

   public BaseAttackEffect parse(String... args) {
      String[] types = args[0].split("_");
      this.myPokemons = types[0].equalsIgnoreCase("SELF");
      this.myAction = this.myPokemons;
      if (args.length > 1) {
         this.myAction = args[1].equalsIgnoreCase("BY_SELF");
      }

      return super.parse(args);
   }
}

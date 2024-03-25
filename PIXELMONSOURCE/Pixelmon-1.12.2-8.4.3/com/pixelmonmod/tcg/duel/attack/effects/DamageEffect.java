package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.attack.enums.EffectLocation;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import java.util.Iterator;
import java.util.List;

public class DamageEffect extends BaseAttackEffectWithOptionalFlip {
   private static final String[] CODES = new String[]{"SELF_DAMAGE", "OPP_DAMAGE"};
   public static final int ALL_COUNT = -1;
   private boolean onMe;
   private EffectLocation location;
   private int count;
   private int amount;

   public DamageEffect() {
      super(CODES);
   }

   public CardSelectorState getSelectorState(List parameters, GameServerState server) {
      if (this.count != -1) {
         PlayerServerState player = server.getPlayer(this.onMe ? server.getCurrentTurn() : server.getNextTurn());
         switch (this.location) {
            case All:
               return SelectorHelper.generateSelectorForActiveAndBench(player, (String)null);
            case Bench:
               CardSelectorState state = SelectorHelper.generateSelectorForBench(player, (String)null);
               return state.getCardList().isEmpty() ? null : state;
         }
      }

      return null;
   }

   public boolean canApply(List parameters, AttackCard cardAttack, GameServerState server) {
      return this.getSelectorState(parameters, server) == null || !parameters.isEmpty();
   }

   public void applyOnCorrectCoinSideBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      if (this.count == -1) {
         PlayerServerState player;
         if (this.onMe) {
            player = server.getPlayer(server.getCurrentTurn());
         } else {
            player = server.getPlayer(server.getNextTurn());
         }

         if (this.location == EffectLocation.All || this.location == EffectLocation.Active) {
            player.getActiveCard().addDamage(card, this.amount, server);
         }

         if (this.location == EffectLocation.All || this.location == EffectLocation.Bench) {
            PokemonCardState[] var6 = player.getBenchCards();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               PokemonCardState bench = var6[var8];
               if (bench != null) {
                  bench.addDamage(card, this.amount, server);
               }
            }
         }
      } else {
         Iterator var10 = parameters.iterator();

         while(var10.hasNext()) {
            Object parameter = var10.next();
            PokemonCardState pokemon = (PokemonCardState)parameter;
            pokemon.addDamage(pokemon, this.amount, server);
         }
      }

   }

   public BaseAttackEffect parse(String... args) {
      String[] types = args[0].split("_");
      this.location = EffectLocation.Active;
      this.count = -1;
      this.onMe = types[0].equalsIgnoreCase("SELF");
      if (args.length > 2) {
         this.location = EffectLocation.getFromDbString(args[1]);
         if (!args[2].equals("*") && !args[2].equals("ALL")) {
            this.count = Integer.parseInt(args[2]);
         }

         this.amount = Integer.parseInt(args[3]);
      } else {
         this.amount = Integer.parseInt(args[1]);
      }

      return super.parse(args);
   }
}

package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.ArrayList;
import java.util.List;

public class FlipsMultiplyEffect extends BaseAttackEffect {
   private static final String CODE = "FLIPS_MULTIPLY";
   private int flipCount;
   private Type type;
   public int counter = 0;

   public FlipsMultiplyEffect() {
      super("FLIPS_MULTIPLY");
   }

   public List flipCoin(List currentResult, PokemonCardState pokemon, GameServerState server) {
      List currentResult = new ArrayList();
      int count = this.getCount(pokemon, server);
      if (count == -1) {
         while(true) {
            CoinSide toAdd = CoinSide.getRandom();
            currentResult.add(toAdd);
            if (toAdd == CoinSide.Tail) {
               break;
            }

            ++this.counter;
         }
      } else {
         for(int i = 0; i < count; ++i) {
            currentResult.add(CoinSide.getRandom());
         }
      }

      return currentResult;
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      int damage = 0;

      for(int i = 0; i < server.getCoinFlip().getResults().size(); ++i) {
         if (server.getCoinFlip().getResults().get(i) == CoinSide.Head) {
            damage += attack.getData().getDamage();
         }
      }

      attack.setDamage(damage);
   }

   private int getCount(PokemonCardState pokemon, GameServerState server) {
      int count = 0;
      PokemonCardState[] var4;
      int var5;
      int var6;
      PokemonCardState state;
      switch (this.type) {
         case Exact:
            count = this.flipCount;
            break;
         case PerEnergy:
            count = (int)pokemon.getAttachments().stream().filter(CommonCardState::isEnergyEquivalence).count();
            break;
         case PerOppBench:
            var4 = server.getPlayer(server.getNextTurn()).getBenchCards();
            var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
               state = var4[var6];
               if (state != null && state.getData() != null) {
                  ++count;
               }
            }

            return count;
         case PerSelfBench:
            var4 = server.getPlayer(server.getCurrentTurn()).getBenchCards();
            var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
               state = var4[var6];
               if (state != null && state.getData() != null) {
                  ++count;
               }
            }

            return count;
         case UntilFail:
            count = -1;
      }

      return count;
   }

   public BaseAttackEffect parse(String... args) {
      this.flipCount = 0;
      if (!args[1].equalsIgnoreCase("ENERGY") && !args[1].equalsIgnoreCase("PER_ENERGY")) {
         if (args[1].equalsIgnoreCase("UNTILFAIL")) {
            this.type = FlipsMultiplyEffect.Type.UntilFail;
         } else if (args[1].equalsIgnoreCase("PER_SELF_BENCH")) {
            this.type = FlipsMultiplyEffect.Type.PerSelfBench;
         } else if (args[1].equalsIgnoreCase("PER_OPP_BENCH")) {
            this.type = FlipsMultiplyEffect.Type.PerOppBench;
         } else {
            this.type = FlipsMultiplyEffect.Type.Exact;
            this.flipCount = Integer.parseInt(args[1]);
         }
      } else {
         this.type = FlipsMultiplyEffect.Type.PerEnergy;
      }

      return super.parse(args);
   }

   public static enum Type {
      Exact,
      PerEnergy,
      PerSelfBench,
      PerOppBench,
      UntilFail;
   }
}

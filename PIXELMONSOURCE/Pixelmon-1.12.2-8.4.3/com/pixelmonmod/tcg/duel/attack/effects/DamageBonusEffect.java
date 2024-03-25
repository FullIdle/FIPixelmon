package com.pixelmonmod.tcg.duel.attack.effects;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.duel.attack.enums.DamageBonusType;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DamageBonusEffect extends BaseAttackEffect {
   private static final String[] CODES = new String[]{"DAMAGE_BONUS"};
   private boolean conditionOnMe;
   private DamageBonusType type;
   private Energy energy;
   private int pokedexNumber;
   private int value;
   private Integer maximumBonus;
   private boolean ignoreBaseDamage;

   public DamageBonusEffect() {
      super(CODES);
   }

   public void applyBeforeDamage(List parameters, PokemonAttackStatus attack, PokemonCardState card, GameServerState server) {
      int count;
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      PokemonCardState active = this.conditionOnMe ? player.getActiveCard() : opp.getActiveCard();
      count = 0;
      int var22;
      label116:
      switch (this.type) {
         case PerBench:
            PokemonCardState[] bench = this.conditionOnMe ? player.getBenchCards() : opp.getBenchCards();
            PokemonCardState[] var18 = bench;
            int var21 = bench.length;
            var22 = 0;

            while(true) {
               if (var22 >= var21) {
                  break label116;
               }

               PokemonCardState benchCard = var18[var22];
               if (benchCard != null) {
                  ++count;
               }

               ++var22;
            }
         case PerDamage:
            count = active.getStatus().getDamage() / 10;
            break;
         case PerPokemon:
            count = (int)(this.conditionOnMe ? player : opp).getActiveAndBenchCards().stream().filter((c) -> {
               return c.getPokemonID() == this.pokedexNumber;
            }).count();
            break;
         case PerEnergy:
            List energyCards = (List)active.getAttachments().stream().filter(CommonCardState::isEnergyEquivalence).collect(Collectors.toList());
            Iterator var11 = energyCards.iterator();

            label108:
            while(var11.hasNext()) {
               CommonCardState energyCard = (CommonCardState)var11.next();
               List energies = new ArrayList();
               if (energyCard.getCardType() == CardType.ENERGY) {
                  energies.add(energyCard.getMainEnergy());
                  if (energyCard.getSecondaryEnergy() != null) {
                     energies.add(energyCard.getSecondaryEnergy());
                  }
               } else if (energyCard.getAbility() != null && energyCard.getAbility().getEffect() != null) {
                  List equi = energyCard.getAbility().getEffect().getEnergyEquivalence(active);
                  if (equi != null) {
                     Iterator var15 = equi.iterator();

                     while(var15.hasNext()) {
                        CommonCardState equiEnergy = (CommonCardState)var15.next();
                        energies.add(equiEnergy.getMainEnergy());
                     }
                  }
               }

               Iterator var25 = energies.iterator();

               while(true) {
                  Energy energy;
                  do {
                     if (!var25.hasNext()) {
                        continue label108;
                     }

                     energy = (Energy)var25.next();
                  } while(this.energy != null && energy != this.energy);

                  ++count;
               }
            }

            if (this.conditionOnMe) {
               Energy[] var20 = attack.getData().getEnergy();
               var22 = var20.length;

               for(int var23 = 0; var23 < var22; ++var23) {
                  Energy costEnergy = var20[var23];
                  if (this.energy == null || costEnergy == this.energy || costEnergy == Energy.COLORLESS) {
                     --count;
                  }
               }
            }
      }

      int damage = this.ignoreBaseDamage ? 0 : attack.getDamage();
      int bonus = count * this.value;
      if (this.maximumBonus != null && bonus > this.maximumBonus) {
         bonus = this.maximumBonus;
      }

      attack.setDamage(damage + bonus);
   }

   public BaseAttackEffect parse(String... args) {
      this.conditionOnMe = args[1].equalsIgnoreCase("SELF");
      int index = 2;
      if (args[index].equalsIgnoreCase("PER_DAMAGE")) {
         this.type = DamageBonusType.PerDamage;
         ++index;
      } else if (args[index].equalsIgnoreCase("PER_BENCH")) {
         this.type = DamageBonusType.PerBench;
         ++index;
      } else if (args[index].equalsIgnoreCase("PER_ENERGY")) {
         this.type = DamageBonusType.PerEnergy;
         ++index;
      } else if (args[index].equalsIgnoreCase("PER_POKEMON")) {
         this.type = DamageBonusType.PerPokemon;
         ++index;
      } else {
         this.type = DamageBonusType.PerEnergy;
      }

      if (this.type == DamageBonusType.PerEnergy) {
         this.energy = Energy.getEnergyFromString(args[index++]);
      } else if (this.type == DamageBonusType.PerPokemon) {
         this.pokedexNumber = Integer.parseInt(args[index++]);
      }

      if (args[index].startsWith("*") || args[index].startsWith("x")) {
         this.ignoreBaseDamage = true;
         args[index] = args[index].substring(1);
      }

      this.value = Integer.parseInt(args[index++]);
      if (args.length == index + 1) {
         this.maximumBonus = Integer.parseInt(args[index]);
      } else {
         this.maximumBonus = null;
      }

      return super.parse(args);
   }
}

package com.pixelmonmod.pixelmon.battles.controller.ai;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class OpponentMemory {
   UUID pokemonUUID;
   private ArrayList guessedAttacks;
   private ArrayList knownAttacks;

   public OpponentMemory(PixelmonWrapper pokemon) {
      this.pokemonUUID = pokemon.getPokemonUUID();
      this.guessedAttacks = new ArrayList(pokemon.type.size());
      this.knownAttacks = new ArrayList(4);
      BaseStats stats = pokemon.getBaseStats();
      boolean physical = stats.getStat(StatsType.Attack) >= stats.getStat(StatsType.SpecialAttack);
      Iterator var4 = pokemon.type.iterator();

      while(var4.hasNext()) {
         EnumType type = (EnumType)var4.next();
         if (type != null) {
            String guess = null;
            switch (type) {
               case Bug:
                  guess = physical ? "X-Scissor" : "Bug Buzz";
                  break;
               case Dark:
                  guess = physical ? "Crunch" : "Dark Pulse";
                  break;
               case Dragon:
                  guess = physical ? "Dragon Claw" : "Dragon Pulse";
                  break;
               case Electric:
                  guess = physical ? "Wild Charge" : "Thunderbolt";
                  break;
               case Fairy:
                  guess = physical ? "Play Rough" : "Moonblast";
                  break;
               case Fighting:
                  guess = physical ? "Close Combat" : "Focus Blast";
                  break;
               case Fire:
                  guess = physical ? "Flare Blitz" : "Flamethrower";
                  break;
               case Flying:
                  guess = physical ? "Brave Bird" : "Air Slash";
                  break;
               case Ghost:
                  guess = physical ? "Shadow Claw" : "Shadow Ball";
                  break;
               case Grass:
                  guess = physical ? "Leaf Blade" : "Energy Ball";
                  break;
               case Ground:
                  guess = physical ? "Earthquake" : "Earth Power";
                  break;
               case Ice:
                  guess = physical ? "Icicle Crash" : "Ice Beam";
                  break;
               case Normal:
                  guess = physical ? "Return" : "Hyper Voice";
                  break;
               case Poison:
                  guess = physical ? "Poison Jab" : "Sludge Bomb";
                  break;
               case Psychic:
                  guess = physical ? "Zen Headbutt" : "Psychic";
                  break;
               case Rock:
                  guess = physical ? "Stone Edge" : "Power Gem";
                  break;
               case Steel:
                  guess = physical ? "Iron Head" : "Flash Cannon";
                  break;
               case Water:
                  guess = physical ? "Waterfall" : "Surf";
            }

            if (guess != null) {
               this.guessedAttacks.add(new Attack(guess));
            }
         }
      }

   }

   public void seeAttack(Attack attack) {
      if (!this.knownAttacks.contains(attack)) {
         if (attack.getAttackCategory() != AttackCategory.STATUS) {
            for(int i = 0; i < this.guessedAttacks.size(); ++i) {
               if (attack.getType() == ((Attack)this.guessedAttacks.get(i)).getType()) {
                  this.guessedAttacks.remove(i);
                  break;
               }
            }
         }

         if (this.guessedAttacks.size() + this.knownAttacks.size() >= 4) {
            if (this.guessedAttacks.isEmpty()) {
               this.knownAttacks.remove(0);
            } else {
               this.guessedAttacks.remove(RandomHelper.getRandomNumberBetween(0, this.guessedAttacks.size() - 1));
            }
         }

         this.knownAttacks.add(attack);
      }
   }

   public ArrayList getAttacks() {
      ArrayList allAttacks = new ArrayList(4);
      allAttacks.addAll(this.guessedAttacks);
      allAttacks.addAll(this.knownAttacks);
      return allAttacks;
   }
}

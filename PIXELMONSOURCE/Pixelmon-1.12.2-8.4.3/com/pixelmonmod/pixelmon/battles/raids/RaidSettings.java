package com.pixelmonmod.pixelmon.battles.raids;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.MaxMoveConverter;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import com.pixelmonmod.pixelmon.items.ItemTechnicalMove;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class RaidSettings {
   public int participants;
   public int lives;
   public int turns;
   public boolean autoGenerate;
   public int stars;
   public int attacksPerTurn;
   public int shields;
   public int shieldStrength;
   public int perfectIVs;
   public int functionalDynamaxLevel;
   public int trueDynamaxLevel;
   public boolean hiddenAbility;
   public boolean shiny;
   public boolean gigantamax;
   public double shockwaveChance;
   public double cheerSuccessChance;
   public int level;
   public int trueLevel;
   public boolean canCatch;
   public EnumBossMode bossMode;
   public ArrayList moveset;
   public int rewardCount;
   public final WeightedItemStacks potentialRewards;
   public int[] ivOverrides;
   public int[] evOverrides;
   public int[] statOverrides;

   private RaidSettings() {
      this.participants = 4;
      this.lives = 4;
      this.turns = 10;
      this.autoGenerate = true;
      this.stars = 1;
      this.attacksPerTurn = 1;
      this.shields = 0;
      this.shieldStrength = 2;
      this.perfectIVs = 1;
      this.functionalDynamaxLevel = 0;
      this.trueDynamaxLevel = 0;
      this.hiddenAbility = false;
      this.shiny = false;
      this.gigantamax = false;
      this.shockwaveChance = 0.1;
      this.cheerSuccessChance = 0.2;
      this.level = 50;
      this.trueLevel = 50;
      this.canCatch = true;
      this.bossMode = EnumBossMode.NotBoss;
      this.moveset = new ArrayList();
      this.rewardCount = 1;
      this.potentialRewards = WeightedItemStacks.create();
      this.ivOverrides = new int[]{-1, -1, -1, -1, -1, -1};
      this.evOverrides = new int[]{-1, -1, -1, -1, -1, -1};
      this.statOverrides = new int[]{-1, -1, -1, -1, -1, -1};
   }

   protected void init(EntityPixelmon entity) {
      Pokemon pokemon = entity.getPokemonData();
      if (this.autoGenerate) {
         switch (this.stars) {
            case 1:
               this.shields = 0;
               this.attacksPerTurn = 1;
               this.functionalDynamaxLevel = 0;
               this.trueDynamaxLevel = 2;
               this.trueLevel = RandomHelper.getRandomNumberBetween(20, 30);
               this.perfectIVs = 1;
               this.shockwaveChance = 0.15;
               break;
            case 2:
               this.shields = 0;
               this.attacksPerTurn = RandomHelper.getRandomNumberBetween(1, 2);
               this.functionalDynamaxLevel = 2;
               this.trueDynamaxLevel = 4;
               this.trueLevel = RandomHelper.getRandomNumberBetween(30, 40);
               this.perfectIVs = 2;
               this.shockwaveChance = 0.2;
               break;
            case 3:
               this.shields = 1;
               this.attacksPerTurn = RandomHelper.getRandomNumberBetween(1, 3);
               this.functionalDynamaxLevel = 8;
               this.trueDynamaxLevel = 6;
               this.trueLevel = RandomHelper.getRandomNumberBetween(40, 50);
               this.perfectIVs = 3;
               this.shockwaveChance = 0.25;
               break;
            case 4:
               this.shields = 1;
               this.attacksPerTurn = RandomHelper.getRandomNumberBetween(2, 4);
               this.functionalDynamaxLevel = 20;
               this.trueDynamaxLevel = 8;
               this.trueLevel = RandomHelper.getRandomNumberBetween(50, 60);
               this.perfectIVs = 4;
               this.shockwaveChance = 0.3;
               break;
            case 5:
               this.shields = 2;
               this.attacksPerTurn = RandomHelper.getRandomNumberBetween(3, 4);
               this.functionalDynamaxLevel = 30;
               this.trueDynamaxLevel = 10;
               this.trueLevel = RandomHelper.getRandomNumberBetween(60, 70);
               this.perfectIVs = 4;
               this.shockwaveChance = 0.35;
         }

         Stats stats = pokemon.getStats();
         int defence = stats.defence + stats.specialDefence;
         this.shieldStrength = MathHelper.func_76125_a(defence / 35, 2, 8);
         this.shiny = RandomHelper.getRandomChance(1.0F / PixelmonConfig.getRaidShinyRate(this.stars));
         this.gigantamax = RandomHelper.getRandomChance(1.0F / PixelmonConfig.getRaidGigantamaxFactorRate(this.stars));
         this.hiddenAbility = RandomHelper.getRandomChance(1.0F / PixelmonConfig.getRaidHARate(this.stars));
         if (DropItemRegistry.raidDrops != null) {
            HashMap drops = (HashMap)DropItemRegistry.raidDrops.get(this.stars);
            if (drops != null) {
               WeightedItemStacks wis1 = (WeightedItemStacks)drops.get((Object)null);
               if (wis1 != null) {
                  this.potentialRewards.addAll(wis1);
               }

               Iterator var7 = pokemon.getBaseStats().getTypeList().iterator();

               while(var7.hasNext()) {
                  EnumType type = (EnumType)var7.next();
                  if (type != null) {
                     WeightedItemStacks wis2 = (WeightedItemStacks)drops.get(type);
                     if (wis2 != null) {
                        this.potentialRewards.addAll(wis2);
                     }
                  }
               }
            } else {
               Pixelmon.LOGGER.error("Error in raid drops! No drops found for star level " + this.stars + "!");
            }
         } else {
            Pixelmon.LOGGER.error("Error in raid drops! No drops found at all, file missing perhaps?");
         }

         this.rewardCount = PixelmonConfig.getRaidDropCount(this.stars);
         this.moveset.clear();
         this.fillMoveset(pokemon);
      }

      this.populatePokemon(pokemon);
      entity.update(EnumUpdateType.ALL);
   }

   public Pokemon getCatchablePokemon(Pokemon boss) {
      Pokemon pokemon = Pixelmon.pokemonFactory.create(boss.getSpecies());
      pokemon.setForm(boss.getForm());
      pokemon.setGender(boss.getGender());
      pokemon.setLevel(this.trueLevel);
      pokemon.setAbilitySlot(boss.getAbilitySlot());
      pokemon.rerollMoveset();
      pokemon.getIVs().fillFromArray(boss.getIVs().getArray());
      pokemon.setShiny(this.shiny);
      pokemon.setGigantamaxFactor(this.gigantamax);
      pokemon.setDynamaxLevel(this.trueDynamaxLevel);
      return pokemon;
   }

   public ArrayList getDrops(List types) {
      ArrayList stacks = new ArrayList();

      for(int i = 0; i < this.rewardCount; ++i) {
         ItemStack stack = this.potentialRewards.get();
         if (!stack.func_190926_b()) {
            boolean isTM = false;
            if (stack.func_77973_b() instanceof ItemTechnicalMove) {
               ITechnicalMove tm = ItemTechnicalMove.getInstanceOf(stack);
               if (tm == null) {
                  continue;
               }

               List moves = ITechnicalMove.getForType(tm, (EnumType)RandomHelper.getRandomElementFromCollection(types));
               if (moves == null) {
                  continue;
               }

               ITechnicalMove move = (ITechnicalMove)RandomHelper.getRandomElementFromCollection(moves);
               if (move == null) {
                  continue;
               }

               if (!stack.func_77942_o()) {
                  stack.func_77982_d(new NBTTagCompound());
               }

               stack.func_77978_p().func_74777_a("tm", (short)move.getId());
               isTM = true;
            }

            boolean grown = false;
            if (!isTM) {
               Iterator var10 = stacks.iterator();

               while(var10.hasNext()) {
                  ItemStack otherStack = (ItemStack)var10.next();
                  if (otherStack.func_77973_b() == stack.func_77973_b()) {
                     otherStack.func_190917_f(stack.func_190916_E());
                     grown = true;
                     break;
                  }
               }
            }

            if (!grown) {
               stacks.add(stack);
            }
         }
      }

      return stacks;
   }

   private void populatePokemon(Pokemon pokemon) {
      pokemon.setAbilitySlot(this.hiddenAbility ? 2 : RandomHelper.rand.nextInt(2));
      pokemon.setShiny(this.shiny);
      pokemon.setGigantamaxFactor(this.gigantamax);
      pokemon.setLevel(this.level);
      pokemon.setDynamaxLevel(this.functionalDynamaxLevel);
      int[] ivs = pokemon.getIVs().getArray();
      int[] evs = pokemon.getEVs().getArray();
      Set stats = new HashSet(Arrays.asList(StatsType.HP, StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed));

      int i;
      for(i = 0; i < this.perfectIVs; ++i) {
         StatsType stat = (StatsType)RandomHelper.removeRandomElementFromCollection(stats);
         if (stat != null) {
            ivs[stat.ordinal() - 1] = 31;
         }
      }

      for(i = 0; i < 6; ++i) {
         if (this.ivOverrides[i] > -1) {
            ivs[i] = this.ivOverrides[i];
         }

         if (this.evOverrides[i] > -1) {
            evs[i] = this.evOverrides[i];
         }
      }

      this.fillMoveset(pokemon);
   }

   public void rerollMoveset(Pokemon pokemon) {
      this.moveset.clear();
      this.fillMoveset(pokemon);
   }

   private void fillMoveset(Pokemon pokemon) {
      if (this.moveset.isEmpty()) {
         PixelmonWrapper pw = pokemon.getPixelmonWrapperIfExists();
         Moveset moveset;
         if (pw != null && pw.entity != null && pw.entity.transformed != null) {
            moveset = pw.entity.transformed.getBaseStats().loadMoveset(this.level).withPokemon(pokemon);
         } else {
            moveset = pokemon.getBaseStats().loadMoveset(this.level).withPokemon(pokemon);
         }

         Attack[] var4 = moveset.attacks;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Attack attack = var4[var6];
            if (attack != null) {
               if (this.stars >= 3) {
                  this.moveset.add(attack);
               }

               if (this.gigantamax) {
                  this.moveset.add(MaxMoveConverter.getGMaxMoveFromAttack(attack, (PixelmonWrapper)null, pokemon.getSpecies(), pokemon.getFormEnum()));
               } else {
                  this.moveset.add(MaxMoveConverter.getMaxMoveFromAttack(attack, (PixelmonWrapper)null));
               }
            }
         }
      }

   }

   public static Builder builder() {
      return new Builder();
   }

   // $FF: synthetic method
   RaidSettings(Object x0) {
      this();
   }

   public static class Builder {
      private final RaidSettings settings;

      private Builder() {
         this.settings = new RaidSettings();
      }

      public Builder setParticipantCount(int participants) {
         this.settings.participants = participants;
         return this;
      }

      public Builder setLives(int lives) {
         this.settings.lives = lives;
         return this;
      }

      public Builder setTurns(int turns) {
         this.settings.turns = turns;
         return this;
      }

      public Builder setAutoGenerate(boolean autoGenerate) {
         this.settings.autoGenerate = autoGenerate;
         return this;
      }

      public Builder setStars(int stars) {
         this.settings.stars = stars;
         return this;
      }

      public Builder setPerfectIVs(int perfectIVs) {
         this.settings.perfectIVs = perfectIVs;
         return this;
      }

      public Builder setDynamaxLevel(int functionalLevel, int trueLevel) {
         this.settings.functionalDynamaxLevel = functionalLevel;
         this.settings.trueDynamaxLevel = trueLevel;
         return this;
      }

      public Builder setHiddenAbility(boolean hiddenAbility) {
         this.settings.hiddenAbility = hiddenAbility;
         return this;
      }

      public Builder setShiny(boolean shiny) {
         this.settings.shiny = shiny;
         return this;
      }

      public Builder setGigantamax(boolean gigantamax) {
         this.settings.gigantamax = gigantamax;
         return this;
      }

      public Builder setLevel(int level) {
         this.settings.level = level;
         return this;
      }

      public Builder setTrueLevel(int trueLevel) {
         this.settings.trueLevel = trueLevel;
         return this;
      }

      public Builder setBossMode(EnumBossMode bossMode) {
         this.settings.bossMode = bossMode;
         return this;
      }

      public Builder setRewardCount(int rewardCount) {
         this.settings.rewardCount = rewardCount;
         return this;
      }

      public Builder addReward(int weight, ItemStack stack) {
         this.settings.potentialRewards.add(weight, stack);
         return this;
      }

      public Builder setIVOverrides(int[] ivs) {
         this.settings.ivOverrides = ivs;
         return this;
      }

      public Builder setIVOverride(StatsType stat, int iv) {
         this.settings.ivOverrides[stat.ordinal() - 1] = iv;
         return this;
      }

      public Builder setEVOverrides(int[] evs) {
         this.settings.evOverrides = evs;
         return this;
      }

      public Builder setEVOverride(StatsType stat, int ev) {
         this.settings.evOverrides[stat.ordinal() - 1] = ev;
         return this;
      }

      public Builder setStatOverrides(int[] stats) {
         this.settings.statOverrides = stats;
         return this;
      }

      public Builder setStatOverride(StatsType stat, int s) {
         this.settings.statOverrides[stat.ordinal() - 1] = s;
         return this;
      }

      public Builder setMoveset(ArrayList moveset) {
         this.settings.moveset = moveset;
         return this;
      }

      public Builder addAttack(Attack attack) {
         this.settings.moveset.add(attack);
         return this;
      }

      public Builder setShields(int shields, int strength) {
         this.settings.shields = shields;
         this.settings.shieldStrength = strength;
         return this;
      }

      public Builder setCanCatch(boolean canCatch) {
         this.settings.canCatch = canCatch;
         return this;
      }

      public Builder setShockwaveChance(double shockwaveChance) {
         this.settings.shockwaveChance = shockwaveChance;
         return this;
      }

      public Builder setCheerSuccessChance(double cheerSuccessChance) {
         this.settings.cheerSuccessChance = cheerSuccessChance;
         return this;
      }

      public RaidSettings build() {
         return this.settings;
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}

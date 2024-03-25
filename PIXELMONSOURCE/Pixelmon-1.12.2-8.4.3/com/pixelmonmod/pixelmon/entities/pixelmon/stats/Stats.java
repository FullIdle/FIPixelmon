package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;

public class Stats {
   public transient Pokemon pokemon;
   public int hp;
   public int attack;
   public int defence;
   public int specialAttack;
   public int specialDefence;
   public int speed;
   public IVStore ivs = new IVStore();
   public EVStore evs = new EVStore();

   public Stats setLevelStats(EnumNature nature, BaseStats baseStats, int level) {
      this.hp = this.calculateHP(baseStats, level);
      if (this.pokemon != null) {
         this.pokemon.ifEntityExists((pixelmon) -> {
            pixelmon.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)this.hp);
         });
      }

      this.attack = this.calculateStat(StatsType.Attack, nature, baseStats, level);
      this.defence = this.calculateStat(StatsType.Defence, nature, baseStats, level);
      this.specialAttack = this.calculateStat(StatsType.SpecialAttack, nature, baseStats, level);
      this.specialDefence = this.calculateStat(StatsType.SpecialDefence, nature, baseStats, level);
      this.speed = this.calculateStat(StatsType.Speed, nature, baseStats, level);
      return this;
   }

   public void recalculateStats() {
      float percent = this.pokemon.getHealthPercentage();
      this.setLevelStats(this.pokemon.getNature(), this.pokemon.getBaseStats(), this.pokemon.getLevel());
      this.pokemon.setHealthPercentage(percent);
   }

   public Stats withPokemon(Pokemon pokemon) {
      this.ivs.withPokemon(pokemon);
      this.evs.withPokemon(pokemon);
      this.pokemon = pokemon;
      return this;
   }

   public int calculateHP(BaseStats baseStats, int level) {
      return baseStats.getStat(StatsType.HP) == 1 ? 1 : (int)(((float)(this.ivs.isHyperTrained(StatsType.HP) ? 31 : this.ivs.getStat(StatsType.HP)) + 2.0F * (float)baseStats.getStat(StatsType.HP) + (float)this.evs.getStat(StatsType.HP) / 4.0F + 100.0F) * (float)level / 100.0F + 10.0F);
   }

   public int calculateStat(StatsType stat, EnumNature nature, BaseStats baseStats, int level) {
      float val = ((float)(this.ivs.isHyperTrained(stat) ? 31 : this.ivs.getStat(stat)) + 2.0F * (float)baseStats.getStat(stat) + (float)this.evs.getStat(stat) / 4.0F) * (float)level;
      val /= 100.0F;
      val += 5.0F;
      val = (float)Math.floor((double)val);
      if (stat == nature.increasedStat) {
         val *= 1.1F;
      } else if (stat == nature.decreasedStat) {
         val *= 0.9F;
      }

      return (int)val;
   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.func_74777_a("StatsHP", (short)this.hp);
      var1.func_74777_a("StatsAttack", (short)this.attack);
      var1.func_74777_a("StatsDefence", (short)this.defence);
      var1.func_74777_a("StatsSpecialAttack", (short)this.specialAttack);
      var1.func_74777_a("StatsSpecialDefence", (short)this.specialDefence);
      var1.func_74777_a("StatsSpeed", (short)this.speed);
      this.ivs.writeToNBT(var1);
      this.evs.writeToNBT(var1);
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.hp = var1.func_74765_d("StatsHP");
      this.attack = var1.func_74765_d("StatsAttack");
      this.defence = var1.func_74765_d("StatsDefence");
      this.specialAttack = var1.func_74765_d("StatsSpecialAttack");
      this.specialDefence = var1.func_74765_d("StatsSpecialDefence");
      this.speed = var1.func_74765_d("StatsSpeed");
      this.ivs.readFromNBT(var1);
      this.evs.readFromNBT(var1);
      this.recalculateStats();
   }

   public int get(StatsType stat) {
      switch (stat) {
         case HP:
            return this.hp;
         case Attack:
            return this.attack;
         case Defence:
            return this.defence;
         case SpecialAttack:
            return this.specialAttack;
         case SpecialDefence:
            return this.specialDefence;
         case Speed:
            return this.speed;
         default:
            return -1;
      }
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.pokemon.EVsGainedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class EVStore implements IStatStore {
   private Pokemon pokemon = null;
   /** @deprecated */
   @Deprecated
   public int hp = 0;
   /** @deprecated */
   @Deprecated
   public int attack = 0;
   /** @deprecated */
   @Deprecated
   public int defence = 0;
   /** @deprecated */
   @Deprecated
   public int specialAttack = 0;
   /** @deprecated */
   @Deprecated
   public int specialDefence = 0;
   /** @deprecated */
   @Deprecated
   public int speed = 0;
   public static int MAX_EVS = 252;
   public static int MAX_TOTAL_EVS = 510;

   public EVStore() {
   }

   public EVStore(int[] evs) {
      this.fillFromArray(evs);
   }

   public EVStore(HashMap evGain) {
      int[] evs = new int[]{0, 0, 0, 0, 0, 0};

      for(int i = 1; i < 7; ++i) {
         if (evGain.containsKey(StatsType.values()[i])) {
            evs[i - 1] = (Integer)evGain.get(StatsType.values()[i]);
         }
      }

      this.fillFromArray(evs);
   }

   public EVStore withPokemon(Pokemon pokemon) {
      this.pokemon = pokemon;
      return this;
   }

   public int getStat(StatsType type) {
      switch (type) {
         case Attack:
            return this.attack;
         case Defence:
            return this.defence;
         case HP:
            return this.hp;
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

   public void setStat(StatsType type, int value) {
      value = MathHelper.func_76125_a(value, 0, MAX_EVS);
      switch (type) {
         case Attack:
            this.attack = value;
            break;
         case Defence:
            this.defence = value;
            break;
         case HP:
            this.hp = value;
            break;
         case SpecialAttack:
            this.specialAttack = value;
            break;
         case SpecialDefence:
            this.specialDefence = value;
            break;
         case Speed:
            this.speed = value;
      }

      if (this.pokemon != null) {
         this.pokemon.getStats().recalculateStats();
      }

      this.markDirty();
   }

   public int getTotal() {
      return this.attack + this.defence + this.hp + this.specialAttack + this.specialDefence + this.speed;
   }

   public void markDirty() {
      if (this.pokemon != null) {
         this.pokemon.markDirty(EnumUpdateType.EVs, EnumUpdateType.Stats);
      }

   }

   public void fillFromArray(int[] evs) {
      this.hp = evs[0];
      this.attack = evs[1];
      this.defence = evs[2];
      this.specialAttack = evs[3];
      this.specialDefence = evs[4];
      this.speed = evs[5];
      this.markDirty();
   }

   public void gainEV(EVStore evGain) {
      int remainingEVs = this.getRemainingEVs();
      if (this.pokemon != null) {
         EVsGainedEvent event = new EVsGainedEvent(this.pokemon, this, evGain.getArray());
         if (Pixelmon.EVENT_BUS.post(event)) {
            return;
         }

         evGain.fillFromArray(event.evs);
      }

      this.hp = Math.min(MAX_EVS, this.hp + Math.min(remainingEVs, evGain.hp));
      remainingEVs = this.getRemainingEVs();
      this.attack = Math.min(MAX_EVS, this.attack + Math.min(remainingEVs, evGain.attack));
      remainingEVs = this.getRemainingEVs();
      this.defence = Math.min(MAX_EVS, this.defence + Math.min(remainingEVs, evGain.defence));
      remainingEVs = this.getRemainingEVs();
      this.specialAttack = Math.min(MAX_EVS, this.specialAttack + Math.min(remainingEVs, evGain.specialAttack));
      remainingEVs = this.getRemainingEVs();
      this.specialDefence = Math.min(MAX_EVS, this.specialDefence + Math.min(remainingEVs, evGain.specialDefence));
      remainingEVs = this.getRemainingEVs();
      this.speed = Math.min(MAX_EVS, this.speed + Math.min(remainingEVs, evGain.speed));
      this.markDirty();
   }

   private int getRemainingEVs() {
      return Math.max(0, MAX_TOTAL_EVS - this.hp - this.attack - this.defence - this.specialAttack - this.specialDefence - this.speed);
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74777_a("EVHP", (short)this.hp);
      nbt.func_74777_a("EVAttack", (short)this.attack);
      nbt.func_74777_a("EVDefence", (short)this.defence);
      nbt.func_74777_a("EVSpecialAttack", (short)this.specialAttack);
      nbt.func_74777_a("EVSpecialDefence", (short)this.specialDefence);
      nbt.func_74777_a("EVSpeed", (short)this.speed);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.hp = nbt.func_74765_d("EVHP");
      this.attack = nbt.func_74765_d("EVAttack");
      this.defence = nbt.func_74765_d("EVDefence");
      this.specialAttack = nbt.func_74765_d("EVSpecialAttack");
      this.specialDefence = nbt.func_74765_d("EVSpecialDefence");
      this.speed = nbt.func_74765_d("EVSpeed");
   }

   public void writeToByteBuffer(ByteBuf buf) {
      buf.writeByte(this.getStat(StatsType.HP));
      buf.writeByte(this.getStat(StatsType.Attack));
      buf.writeByte(this.getStat(StatsType.Defence));
      buf.writeByte(this.getStat(StatsType.SpecialAttack));
      buf.writeByte(this.getStat(StatsType.SpecialDefence));
      buf.writeByte(this.getStat(StatsType.Speed));
   }

   public EVStore readFromByteBuffer(ByteBuf buf) {
      this.hp = buf.readUnsignedByte();
      this.attack = buf.readUnsignedByte();
      this.defence = buf.readUnsignedByte();
      this.specialAttack = buf.readUnsignedByte();
      this.specialDefence = buf.readUnsignedByte();
      this.speed = buf.readUnsignedByte();
      return this;
   }

   public int[] getArray() {
      return new int[]{this.hp, this.attack, this.defence, this.specialAttack, this.specialDefence, this.speed};
   }

   public EVStore cloneEVs() {
      EVStore s = new EVStore();
      s.hp = this.hp;
      s.attack = this.attack;
      s.defence = this.defence;
      s.specialAttack = this.specialAttack;
      s.specialDefence = this.specialDefence;
      s.speed = this.speed;
      return s;
   }

   public void doubleValues() {
      this.attack *= 2;
      this.defence *= 2;
      this.hp *= 2;
      this.specialAttack *= 2;
      this.specialDefence *= 2;
      this.speed *= 2;
      this.markDirty();
   }

   public boolean berryEVs(StatsType stat) {
      if (stat == StatsType.Attack) {
         if (this.attack > 0) {
            this.setStat(stat, Math.max(this.attack - 10, 0));
            return true;
         }
      } else if (stat == StatsType.Defence) {
         if (this.defence > 0) {
            this.setStat(stat, this.defence = Math.max(this.defence - 10, 0));
            return true;
         }
      } else if (stat == StatsType.HP) {
         if (this.hp > 0) {
            this.setStat(stat, this.hp = Math.max(this.hp - 10, 0));
            return true;
         }
      } else if (stat == StatsType.SpecialAttack) {
         if (this.specialAttack > 0) {
            this.setStat(stat, this.specialAttack = Math.max(this.specialAttack - 10, 0));
            return true;
         }
      } else if (stat == StatsType.SpecialDefence) {
         if (this.specialDefence > 0) {
            this.setStat(stat, this.specialDefence = Math.max(this.specialDefence - 10, 0));
            return true;
         }
      } else if (stat == StatsType.Speed && this.speed > 0) {
         this.setStat(stat, this.speed = Math.max(this.speed - 10, 0));
         return true;
      }

      return false;
   }

   public boolean vitaminEVs(StatsType stat) {
      return this.addEVsOfType(stat, 10, 252);
   }

   public boolean wingEVs(StatsType stat) {
      return this.addEVsOfType(stat, 1, 255);
   }

   public boolean addEVsOfType(StatsType stat, int evIncrease, int maxValue) {
      int remainingEVs = this.getRemainingEVs();
      evIncrease = Math.min(evIncrease, remainingEVs);
      if (remainingEVs > 0) {
         if (stat == StatsType.Attack) {
            if (this.attack < maxValue) {
               this.setStat(stat, Math.min(this.attack + evIncrease, maxValue));
               return true;
            }
         } else if (stat == StatsType.Defence) {
            if (this.defence < maxValue) {
               this.setStat(stat, Math.min(this.defence + evIncrease, maxValue));
               return true;
            }
         } else if (stat == StatsType.HP) {
            if (this.hp < maxValue) {
               this.setStat(stat, Math.min(this.hp + evIncrease, maxValue));
               return true;
            }
         } else if (stat == StatsType.SpecialAttack) {
            if (this.specialAttack < maxValue) {
               this.setStat(stat, Math.min(this.specialAttack + evIncrease, maxValue));
               return true;
            }
         } else if (stat == StatsType.SpecialDefence) {
            if (this.specialDefence < maxValue) {
               this.setStat(stat, Math.min(this.specialDefence + evIncrease, maxValue));
               return true;
            }
         } else if (stat == StatsType.Speed && this.speed < maxValue) {
            this.setStat(stat, Math.min(this.speed + evIncrease, maxValue));
            return true;
         }
      }

      return false;
   }

   public void randomizeMaxEVs() {
      int remainingEVs = MAX_TOTAL_EVS;
      int[] evs = new int[6];

      while(remainingEVs > 0) {
         int index = RandomHelper.getRandomNumberBetween(0, evs.length - 1);
         if (evs[index] < MAX_EVS) {
            int var10002 = evs[index]++;
            --remainingEVs;
         }
      }

      this.hp = evs[0];
      this.attack = evs[1];
      this.defence = evs[2];
      this.specialAttack = evs[3];
      this.specialDefence = evs[4];
      this.speed = evs[5];
      this.pokemon.getStats().recalculateStats();
      this.markDirty();
   }

   /** @deprecated */
   @Deprecated
   public int get(StatsType stat) {
      return this.getStat(stat);
   }

   /** @deprecated */
   @Deprecated
   public void set(StatsType stat, int amount) {
      this.setStat(stat, amount);
   }

   /** @deprecated */
   @Deprecated
   public void addEVs(int i, StatsType stat) {
      this.addStat(stat, i);
   }
}

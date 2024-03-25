package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.HyperTrainEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.BitSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class IVStore implements IStatStore {
   public static final int MAX_IVS = 31;
   /** @deprecated */
   @Deprecated
   public int hp;
   /** @deprecated */
   @Deprecated
   public int attack;
   /** @deprecated */
   @Deprecated
   public int defence;
   /** @deprecated */
   @Deprecated
   public int specialAttack;
   /** @deprecated */
   @Deprecated
   public int specialDefence;
   /** @deprecated */
   @Deprecated
   public int speed;
   private BitSet hyperTrained = new BitSet(6);
   private Pokemon pokemon = null;

   public IVStore() {
   }

   public IVStore(int[] ivs) {
      this.hp = ivs[0];
      this.attack = ivs[1];
      this.defence = ivs[2];
      this.specialAttack = ivs[3];
      this.specialDefence = ivs[4];
      this.speed = ivs[5];
   }

   public IVStore(int hp, int attack, int defence, int specialAttack, int specialDefence, int speed) {
      this.hp = hp;
      this.attack = attack;
      this.defence = defence;
      this.specialAttack = specialAttack;
      this.specialDefence = specialDefence;
      this.speed = speed;
   }

   public IVStore withPokemon(Pokemon pokemon) {
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
      value = MathHelper.func_76125_a(value, 0, 31);
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
            break;
         default:
            return;
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
         this.pokemon.markDirty(EnumUpdateType.IVs, EnumUpdateType.Stats);
      }

   }

   public static IVStore CreateNewIVs() {
      IVStore iv = new IVStore();
      iv.specialDefence = RandomHelper.getRandomNumberBetween(0, 31);
      iv.specialAttack = RandomHelper.getRandomNumberBetween(0, 31);
      iv.speed = RandomHelper.getRandomNumberBetween(0, 31);
      iv.defence = RandomHelper.getRandomNumberBetween(0, 31);
      iv.attack = RandomHelper.getRandomNumberBetween(0, 31);
      iv.hp = RandomHelper.getRandomNumberBetween(0, 31);
      return iv;
   }

   public static IVStore CreateNewIVs3Perfect() {
      int[] ivs = new int[6];
      int[] maxIVs = RandomHelper.getRandomDistinctNumbersBetween(0, 5, 3);
      Arrays.sort(maxIVs);
      int maxIVCounter = 0;

      for(int i = 0; i < 6; ++i) {
         if (maxIVs[maxIVCounter] == i) {
            ivs[i] = 31;
            if (maxIVCounter < maxIVs.length - 1) {
               ++maxIVCounter;
            }
         } else {
            ivs[i] = RandomHelper.getRandomNumberBetween(0, 31);
         }
      }

      return new IVStore(ivs);
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74774_a("IVHP", (byte)this.hp);
      nbt.func_74774_a("IVAttack", (byte)this.attack);
      nbt.func_74774_a("IVDefence", (byte)this.defence);
      nbt.func_74774_a("IVSpAtt", (byte)this.specialAttack);
      nbt.func_74774_a("IVSpDef", (byte)this.specialDefence);
      nbt.func_74774_a("IVSpeed", (byte)this.speed);
      StatsType[] values = StatsType.getStatValues();

      for(int i = 0; i < 6; ++i) {
         if (this.hyperTrained.get(i)) {
            nbt.func_74757_a("IV_HT" + values[i].name(), true);
         }
      }

   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.hp = nbt.func_74771_c("IVHP");
      this.attack = nbt.func_74771_c("IVAttack");
      this.defence = nbt.func_74771_c("IVDefence");
      this.specialAttack = nbt.func_74771_c("IVSpAtt");
      this.specialDefence = nbt.func_74771_c("IVSpDef");
      this.speed = nbt.func_74771_c("IVSpeed");
      StatsType[] values = StatsType.getStatValues();

      for(int i = 0; i < 6; ++i) {
         if (nbt.func_74767_n("IV_HT" + values[i].name())) {
            this.hyperTrained.set(i);
         } else {
            this.hyperTrained.set(i, false);
         }
      }

   }

   public void writeToByteBuffer(ByteBuf buf) {
      buf.writeByte(this.hp);
      buf.writeByte(this.attack);
      buf.writeByte(this.defence);
      buf.writeByte(this.specialAttack);
      buf.writeByte(this.specialDefence);
      buf.writeByte(this.speed);
      byte[] arr = this.hyperTrained.toByteArray();
      buf.writeByte(arr.length == 0 ? 0 : arr[0]);
   }

   public IVStore readFromByteBuffer(ByteBuf buf) {
      this.hp = buf.readByte();
      this.attack = buf.readByte();
      this.defence = buf.readByte();
      this.specialAttack = buf.readByte();
      this.specialDefence = buf.readByte();
      this.speed = buf.readByte();
      byte b = buf.readByte();
      this.hyperTrained = BitSet.valueOf(new byte[]{b});
      return this;
   }

   public double getPercentage(int decimalPlaces) {
      int total = 0;
      int[] var3 = this.getArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int value = var3[var5];
         total += value;
      }

      double percentage = (double)total / 186.0 * 100.0;
      return Math.floor(percentage * Math.pow(10.0, (double)decimalPlaces)) / Math.pow(10.0, (double)decimalPlaces);
   }

   public String getPercentageString(int decimalPlaces) {
      return String.format("%." + decimalPlaces + "f", this.getPercentage(decimalPlaces));
   }

   public String getShorthand(String separator) {
      return this.getStat(StatsType.HP) + separator + this.getStat(StatsType.Attack) + separator + this.getStat(StatsType.Defence) + separator + this.getStat(StatsType.SpecialAttack) + separator + this.getStat(StatsType.SpecialDefence) + separator + this.getStat(StatsType.Speed);
   }

   public void CopyIVs(IVStore ivs) {
      this.hp = ivs.hp;
      this.attack = ivs.attack;
      this.defence = ivs.defence;
      this.specialAttack = ivs.specialAttack;
      this.specialDefence = ivs.specialDefence;
      this.speed = ivs.speed;
   }

   public int[] getArray() {
      return new int[]{this.hp, this.attack, this.defence, this.specialAttack, this.specialDefence, this.speed};
   }

   public void maximizeIVs() {
      this.hp = this.attack = this.defence = this.specialAttack = this.specialDefence = this.speed = 31;
      this.pokemon.getStats().recalculateStats();
      this.markDirty();
   }

   public void setHyperTrained(StatsType stat, boolean hyperTrained) {
      if (!Pixelmon.EVENT_BUS.post(new HyperTrainEvent(stat, hyperTrained, this.pokemon))) {
         if (stat == null) {
            this.hyperTrained.set(0, hyperTrained);
            this.hyperTrained.set(1, hyperTrained);
            this.hyperTrained.set(2, hyperTrained);
            this.hyperTrained.set(3, hyperTrained);
            this.hyperTrained.set(4, hyperTrained);
            this.hyperTrained.set(5, hyperTrained);
            return;
         }

         switch (stat) {
            case Attack:
               this.hyperTrained.set(1, hyperTrained);
               break;
            case Defence:
               this.hyperTrained.set(2, hyperTrained);
               break;
            case HP:
               this.hyperTrained.set(0, hyperTrained);
               break;
            case SpecialAttack:
               this.hyperTrained.set(3, hyperTrained);
               break;
            case SpecialDefence:
               this.hyperTrained.set(4, hyperTrained);
               break;
            case Speed:
               this.hyperTrained.set(5, hyperTrained);
               break;
            default:
               this.hyperTrained.set(0, hyperTrained);
               this.hyperTrained.set(1, hyperTrained);
               this.hyperTrained.set(2, hyperTrained);
               this.hyperTrained.set(3, hyperTrained);
               this.hyperTrained.set(4, hyperTrained);
               this.hyperTrained.set(5, hyperTrained);
         }
      }

   }

   public boolean isHyperTrained(StatsType stat) {
      switch (stat) {
         case Attack:
            return this.hyperTrained.get(1);
         case Defence:
            return this.hyperTrained.get(2);
         case HP:
            return this.hyperTrained.get(0);
         case SpecialAttack:
            return this.hyperTrained.get(3);
         case SpecialDefence:
            return this.hyperTrained.get(4);
         case Speed:
            return this.hyperTrained.get(5);
         default:
            return false;
      }
   }

   public void fillFromArray(int[] ivs) {
      this.hp = ivs[0];
      this.attack = ivs[1];
      this.defence = ivs[2];
      this.specialAttack = ivs[3];
      this.specialDefence = ivs[4];
      this.speed = ivs[5];
      this.markDirty();
   }

   /** @deprecated */
   @Deprecated
   public int get(StatsType stat) {
      return this.getStat(stat);
   }

   /** @deprecated */
   @Deprecated
   public void set(StatsType stat, int value) {
      this.setStat(stat, value);
   }

   /** @deprecated */
   @Deprecated
   public void add(StatsType stat, int value) {
      this.addStat(stat, value);
   }
}

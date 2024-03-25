package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class BonusStats {
   private int accuracy;
   private int evasion;
   private int attack;
   private int defence;
   private int specialAttack;
   private int specialDefence;
   private int speed;
   private boolean persistentAfterCapture;
   private boolean preventsCapture;

   public BonusStats() {
      this.persistentAfterCapture = false;
      this.preventsCapture = false;
      this.accuracy = 0;
      this.evasion = 0;
      this.attack = 0;
      this.defence = 0;
      this.specialAttack = 0;
      this.specialDefence = 0;
      this.speed = 0;
   }

   public BonusStats(int accuracy, int evasion, int attack, int defence, int specialAttack, int specialDefence, int speed) {
      this.persistentAfterCapture = false;
      this.preventsCapture = false;
      this.accuracy = accuracy;
      this.evasion = evasion;
      this.attack = attack;
      this.defence = defence;
      this.specialAttack = specialAttack;
      this.specialDefence = specialDefence;
      this.speed = speed;
   }

   public BonusStats(ByteBuf buf) {
      this.persistentAfterCapture = false;
      this.preventsCapture = false;
      this.readFromByteBuffer(buf);
   }

   public BonusStats(EnumSpecies species) {
      this();
      switch (species) {
         case Nihilego:
            this.specialDefence = 2;
            break;
         case Kartana:
         case Guzzlord:
         case Buzzwole:
            this.attack = 2;
            break;
         case Pheromosa:
         case Poipole:
            this.speed = 2;
            break;
         case Celesteela:
         case Xurkitree:
         case Blacephalon:
         case Naganadel:
            this.specialAttack = 2;
            break;
         case Stakataka:
            this.defence = 2;
      }

   }

   public boolean isPersistentAfterCapture() {
      return this.persistentAfterCapture;
   }

   public boolean preventsCapture() {
      return this.preventsCapture;
   }

   public void setPersistentAfterCapture(boolean persistentAfterCapture) {
      this.persistentAfterCapture = persistentAfterCapture;
   }

   public void setPreventsCapture(boolean preventsCapture) {
      this.preventsCapture = preventsCapture;
   }

   public boolean modifyPokemon(PixelmonWrapper pw) {
      BattleStats bs = pw.getBattleStats();
      if (bs != null) {
         bs.increaseStat(this.accuracy, StatsType.Accuracy, pw, false, false);
         bs.increaseStat(this.evasion, StatsType.Evasion, pw, false, false);
         bs.increaseStat(this.attack, StatsType.Attack, pw, false, false);
         bs.increaseStat(this.defence, StatsType.Defence, pw, false, false);
         bs.increaseStat(this.specialAttack, StatsType.SpecialAttack, pw, false, false);
         bs.increaseStat(this.specialDefence, StatsType.SpecialDefence, pw, false, false);
         bs.increaseStat(this.speed, StatsType.Speed, pw, false, false);
      }

      return this.accuracy > 0 || this.evasion > 0 || this.attack > 0 || this.defence > 0 || this.specialAttack > 0 || this.specialDefence > 0 || this.speed > 0;
   }

   public StatsType getStatModified() {
      StatsType stat = null;
      int modified = 0;
      if (this.accuracy > 0) {
         stat = StatsType.Accuracy;
         ++modified;
      }

      if (this.evasion > 0) {
         stat = StatsType.Evasion;
         ++modified;
      }

      if (this.attack > 0) {
         stat = StatsType.Attack;
         ++modified;
      }

      if (this.defence > 0) {
         stat = StatsType.Defence;
         ++modified;
      }

      if (this.specialAttack > 0) {
         stat = StatsType.SpecialAttack;
         ++modified;
      }

      if (this.specialDefence > 0) {
         stat = StatsType.SpecialDefence;
         ++modified;
      }

      if (this.speed > 0) {
         stat = StatsType.Speed;
         ++modified;
      }

      return modified == 1 ? stat : null;
   }

   public int getStatModificationRank() {
      int rank = 0;
      if (this.accuracy > rank) {
         rank = this.accuracy;
      }

      if (this.evasion > rank) {
         rank = this.evasion;
      }

      if (this.attack > rank) {
         rank = this.attack;
      }

      if (this.defence > rank) {
         rank = this.defence;
      }

      if (this.specialAttack > rank) {
         rank = this.specialAttack;
      }

      if (this.specialDefence > rank) {
         rank = this.specialDefence;
      }

      if (this.speed > rank) {
         rank = this.speed;
      }

      return rank;
   }

   public void writeToByteBuffer(ByteBuf buf) {
      buf.writeInt(this.accuracy);
      buf.writeInt(this.evasion);
      buf.writeInt(this.attack);
      buf.writeInt(this.defence);
      buf.writeInt(this.specialAttack);
      buf.writeInt(this.specialDefence);
      buf.writeInt(this.speed);
      buf.writeBoolean(this.persistentAfterCapture);
      buf.writeBoolean(this.preventsCapture);
   }

   public void readFromByteBuffer(ByteBuf buf) {
      this.accuracy = buf.readInt();
      this.evasion = buf.readInt();
      this.attack = buf.readInt();
      this.defence = buf.readInt();
      this.specialAttack = buf.readInt();
      this.specialDefence = buf.readInt();
      this.speed = buf.readInt();
      this.persistentAfterCapture = buf.readBoolean();
      this.preventsCapture = buf.readBoolean();
   }

   public void writeToNBT(NBTTagCompound tag) {
      if (this.accuracy != 0) {
         tag.func_74768_a("BonusAccuracy", this.accuracy);
      }

      if (this.evasion != 0) {
         tag.func_74768_a("BonusEvasion", this.evasion);
      }

      if (this.attack != 0) {
         tag.func_74768_a("BonusAttack", this.attack);
      }

      if (this.defence != 0) {
         tag.func_74768_a("BonusDefence", this.defence);
      }

      if (this.specialAttack != 0) {
         tag.func_74768_a("BonusSpAttack", this.specialAttack);
      }

      if (this.specialDefence != 0) {
         tag.func_74768_a("BonusSpDefence", this.specialDefence);
      }

      if (this.speed != 0) {
         tag.func_74768_a("BonusSpeed", this.speed);
      }

      if (this.persistentAfterCapture) {
         tag.func_74757_a("BonusPersistent", this.persistentAfterCapture);
      }

      if (this.preventsCapture) {
         tag.func_74757_a("BonusCapture", this.preventsCapture);
      }

   }

   public void readFromNBT(NBTTagCompound tag) {
      this.accuracy = tag.func_74762_e("BonusAccuracy");
      this.evasion = tag.func_74762_e("BonusEvasion");
      this.attack = tag.func_74762_e("BonusAttack");
      this.defence = tag.func_74762_e("BonusDefence");
      this.specialAttack = tag.func_74762_e("BonusSpAttack");
      this.specialDefence = tag.func_74762_e("BonusSpDefence");
      this.speed = tag.func_74762_e("BonusSpeed");
      this.persistentAfterCapture = tag.func_74767_n("BonusPersistent");
      this.preventsCapture = tag.func_74767_n("BonusCapture");
   }
}

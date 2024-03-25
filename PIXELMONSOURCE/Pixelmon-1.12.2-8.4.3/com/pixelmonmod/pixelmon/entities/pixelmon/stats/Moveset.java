package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.MovesetEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import io.netty.buffer.ByteBuf;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.RandomAccess;
import java.util.function.Predicate;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;

public class Moveset extends AbstractList implements RandomAccess, Cloneable {
   public Pokemon pokemon = null;
   public Attack[] attacks = new Attack[4];
   private final List reminderMoves = Lists.newArrayList();

   public Moveset() {
   }

   public Moveset(Attack[] attacks) {
      this.attacks = attacks;
   }

   public Moveset(int index, Attack a) {
      this.attacks[0] = a;
   }

   public Moveset withPokemon(Pokemon pokemon) {
      this.pokemon = pokemon;
      return this;
   }

   public Attack get(int index) {
      return index >= 0 && index <= 3 ? this.attacks[index] : null;
   }

   public boolean add(Attack a) {
      if (this.size() >= 4) {
         return false;
      } else {
         this.set(this.size(), a);
         return true;
      }
   }

   public Attack set(int index, Attack a) {
      Attack previousAttack = this.attacks[index];
      this.attacks[index] = a;
      this.tryNotifyPokemon();
      if (this.pokemon != null && this.pokemon.getOwnerPlayer() != null) {
         if (previousAttack != null) {
            Pixelmon.EVENT_BUS.post(new MovesetEvent.ForgotMoveEvent(this.pokemon, this, previousAttack));
         }

         if (a != null) {
            Pixelmon.EVENT_BUS.post(new MovesetEvent.LearntMoveEvent(this.pokemon, this, previousAttack, a));
         }

         return previousAttack;
      } else {
         return previousAttack;
      }
   }

   public void tryNotifyPokemon() {
      if (this.pokemon != null) {
         this.pokemon.markDirty(EnumUpdateType.Moveset);
      }

   }

   public void swap(int index, int index2) {
      Attack a = this.attacks[index];
      this.attacks[index] = this.attacks[index2];
      this.attacks[index2] = a;
      this.tryNotifyPokemon();
   }

   public Attack remove(int index) {
      Attack a = this.get(index);
      int oldSize = this.size();

      for(int i = index + 1; i < this.size(); ++i) {
         this.set(i - 1, this.get(i));
      }

      this.set(oldSize - 1, (Attack)null);
      return a;
   }

   public boolean removeIf(Predicate predicate) {
      for(int i = 0; i < this.attacks.length; ++i) {
         if (predicate.test(this.attacks[i])) {
            this.remove(i);
            return true;
         }
      }

      return false;
   }

   public boolean remove(Object o) {
      if (!(o instanceof Attack)) {
         return false;
      } else {
         for(int i = 0; i < this.size(); ++i) {
            if (this.attacks[i] == o) {
               this.set(i, (Attack)null);
               return true;
            }
         }

         return false;
      }
   }

   public int size() {
      int count = 0;

      for(int i = 0; i < 4; ++i) {
         if (this.attacks[i] != null) {
            ++count;
         }
      }

      return count;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean contains(Object o) {
      if (this.isEmpty()) {
         return false;
      } else {
         if (o instanceof Attack) {
            for(int i = 0; i < this.size(); ++i) {
               if (this.attacks[i].equals(o)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public void clear() {
      this.attacks = new Attack[4];
   }

   public void writeToNBT(NBTTagCompound nbt) {
      NBTTagList list = new NBTTagList();

      for(int i = 0; i < this.size(); ++i) {
         Attack attack = this.get(i);
         if (attack != null && attack.getActualMove() != null && attack.getActualMove().getAttackId() != -1) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.func_74777_a("MoveID", (short)attack.getActualMove().getAttackId());
            compound.func_74774_a("MovePP", (byte)attack.pp);
            if (attack.ppLevel != 0) {
               compound.func_74774_a("MovePPLevel", (byte)attack.ppLevel);
            }

            list.func_74742_a(compound);
         }
      }

      nbt.func_74782_a("Moveset", list);
      NBTTagList relearn = new NBTTagList();
      this.reminderMoves.forEach((ab) -> {
         relearn.func_74742_a(new NBTTagInt(ab.getAttackId()));
      });
      nbt.func_74782_a("RelrnMoves", relearn);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.clear();
      NBTTagList list;
      Iterator var3;
      NBTBase base;
      if (nbt.func_74764_b("Moveset")) {
         list = nbt.func_150295_c("Moveset", 10);
         var3 = list.iterator();

         while(var3.hasNext()) {
            base = (NBTBase)var3.next();
            NBTTagCompound compound = (NBTTagCompound)base;
            int moveID = compound.func_74765_d("MoveID");
            int movePP = compound.func_74765_d("MovePP");
            int movePPLevel = compound.func_74765_d("MovePPLevel");
            Attack attack = new Attack(moveID);
            if (moveID != -1 && attack.getActualMove() != null) {
               attack.pp = movePP;
               attack.ppLevel = movePPLevel;
               this.add(attack);
            }
         }
      } else if (nbt.func_74764_b("PixelmonNumberMoves")) {
         int numMoves = nbt.func_74771_c("PixelmonNumberMoves");

         for(int i = 0; i < numMoves; ++i) {
            Attack a = null;
            if (!nbt.func_74764_b("PixelmonMoveID" + i) && nbt.func_74764_b("PixelmonMoveName" + i)) {
               a = new Attack(nbt.func_74779_i("PixelmonMoveName" + i));
               if (a.getActualMove() == null) {
                  a = null;
               }
            } else {
               int id = nbt.func_74765_d("PixelmonMoveID" + i);
               if (id != -1) {
                  a = new Attack(id);
                  if (a.getActualMove() == null) {
                     a = null;
                  }
               }
            }

            if (a != null) {
               if (nbt.func_74764_b("PixelmonMovePP" + i)) {
                  a.pp = nbt.func_74771_c("PixelmonMovePP" + i);
               }

               if (nbt.func_74764_b("PixelmonMovePPBase" + i)) {
               }

               this.add(a);
            }
         }
      }

      this.reminderMoves.clear();
      if (nbt.func_74764_b("RelrnMoves")) {
         list = nbt.func_150295_c("Moveset", 3);
         var3 = list.iterator();

         while(var3.hasNext()) {
            base = (NBTBase)var3.next();
            NBTTagInt id = (NBTTagInt)base;
            Optional var10000 = AttackBase.getAttackBase(id.func_150287_d());
            List var10001 = this.reminderMoves;
            var10000.ifPresent(var10001::add);
         }
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.clear();
      int numberOfMoves = buf.readByte();

      for(int j = 0; j < numberOfMoves; ++j) {
         Attack attack = new Attack(buf.readShort());
         attack.pp = buf.readByte();
         attack.ppLevel = buf.readByte();
         attack.overridePPMax(buf.readByte());
         attack.setDisabled(buf.readBoolean(), (PixelmonWrapper)null);
         this.attacks[j] = attack;
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.size());
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Attack a = (Attack)var2.next();
         if (a != null) {
            buf.writeShort(a.getActualMove().getAttackId());
            buf.writeByte(a.pp);
            buf.writeByte(a.ppLevel);
            buf.writeByte(a.getOverriddenPPMax() == null ? -1 : a.getOverriddenPPMax());
            buf.writeBoolean(a.getDisabled());
         }
      }

   }

   public boolean hasAttack(Attack a) {
      Attack[] var2 = this.attacks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Attack attack = var2[var4];
         if (attack != null && attack.getActualMove().getAttackId() == a.getActualMove().getAttackId()) {
            return true;
         }
      }

      return false;
   }

   public boolean hasAttack(String... attackNames) {
      Attack[] var2 = this.attacks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Attack attack = var2[var4];
         if (attack != null && attack.isAttack(attackNames)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasAttack(AttackBase... moves) {
      Attack[] var2 = this.attacks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Attack attack = var2[var4];
         AttackBase[] var6 = moves;
         int var7 = moves.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            AttackBase base = var6[var8];
            if (attack != null && attack.getActualMove() == base) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean hasAttackCategory(AttackCategory attackCategory) {
      Attack[] var2 = this.attacks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Attack attack = var2[var4];
         if (attack != null && attack.getActualMove().getAttackCategory() == attackCategory) {
            return true;
         }
      }

      return false;
   }

   public boolean hasOffensiveAttackType(EnumType... types) {
      Attack[] var2 = this.attacks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Attack attack = var2[var4];
         EnumType[] var6 = types;
         int var7 = types.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            EnumType type = var6[var8];
            if (attack != null && attack.getActualMove().getAttackCategory() != AttackCategory.STATUS && attack.getType() == type) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean removeAttack(String attackName) {
      for(int i = 0; i < this.attacks.length; ++i) {
         Attack attack = this.attacks[i];
         if (attack != null && attack.isAttack(attackName)) {
            this.remove(i);
            return true;
         }
      }

      return false;
   }

   public void replaceWith(ArrayList attackIds) {
      this.clear();
      Iterator var2 = attackIds.iterator();

      while(var2.hasNext()) {
         Integer attackIndex = (Integer)var2.next();
         this.add(new Attack(attackIndex));
      }

   }

   public boolean hasFullPP() {
      for(int i = 0; i < this.size(); ++i) {
         Attack a = this.attacks[i];
         if (a != null && a.pp < a.getMaxPP()) {
            return false;
         }
      }

      return true;
   }

   public Moveset copy() {
      Attack[] attacks = new Attack[this.attacks.length];

      for(int i = 0; i < this.attacks.length; ++i) {
         if (this.attacks[i] != null) {
            attacks[i] = this.attacks[i].copy();
         }
      }

      return new Moveset(attacks);
   }

   public boolean replaceMove(String oldMove, Attack newMove) {
      for(int i = 0; i < this.size(); ++i) {
         if (this.attacks[i] != null && this.attacks[i].isAttack(oldMove)) {
            this.attacks[i] = newMove;
            this.tryNotifyPokemon();
            return true;
         }
      }

      return false;
   }

   public void healAllPP() {
      for(int i = 0; i < this.size(); ++i) {
         Attack attack = this.attacks[i];
         if (attack != null) {
            attack.pp = attack.getMaxPP();
         }
      }

      this.tryNotifyPokemon();
   }

   public List getReminderMoves() {
      return this.reminderMoves;
   }

   public static Moveset loadMoveset(PokemonLink pokemon) {
      return pokemon.getBaseStats().loadMoveset(pokemon.getLevel());
   }
}

package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.tcg.api.card.CardCondition;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class PokemonCardStatus {
   private static final int NULL_VALUE = -100;
   private int damage = 0;
   private List conditions = new ArrayList();
   private List clonedConditions = new ArrayList();
   private boolean damageImmune;
   private boolean conditionImmune;
   private boolean startTurnParalyzed = false;

   public PokemonCardStatus() {
   }

   public PokemonCardStatus(ByteBuf buf) {
      this.damage = buf.readInt();
      int effectSize = buf.readInt();

      for(int i = 0; i < effectSize; ++i) {
         int statusOrdinal = buf.readInt();
         Integer modifier = buf.readInt();
         this.conditions.add(new MutablePair(CardCondition.values()[statusOrdinal], modifier == -100 ? null : modifier));
      }

      this.damageImmune = buf.readBoolean();
      this.conditionImmune = buf.readBoolean();
   }

   public void write(ByteBuf buf) {
      buf.writeInt(this.damage);
      buf.writeInt(this.conditions.size());
      Iterator var2 = this.conditions.iterator();

      while(var2.hasNext()) {
         Pair effect = (Pair)var2.next();
         buf.writeInt(((CardCondition)effect.getLeft()).ordinal());
         buf.writeInt(effect.getRight() == null ? -100 : (Integer)effect.getRight());
      }

      buf.writeBoolean(this.damageImmune);
      buf.writeBoolean(this.conditionImmune);
   }

   public List getConditions() {
      return this.conditions;
   }

   public void cloneConditions() {
      this.clonedConditions = new ArrayList();
      if (this.conditions != null) {
         this.clonedConditions.addAll((Collection)this.conditions.stream().map((pair) -> {
            return new ImmutablePair(pair.getLeft(), pair.getRight());
         }).collect(Collectors.toList()));
      }

   }

   public List getClonedConditions() {
      return this.clonedConditions;
   }

   public int getDamage() {
      return this.damage;
   }

   public void healDamage(int d) {
      this.setDamage(this.damage - d);
   }

   public void setDamage(int d) {
      if (d < 0) {
         this.damage = 0;
      } else {
         this.damage = d;
      }

   }

   public boolean hasCondition(CardCondition cardCondition) {
      return this.conditions.stream().anyMatch((c) -> {
         return c.getLeft() == cardCondition;
      });
   }

   public void addCondition(CardCondition cardCondition, Integer modifier) {
      if (!this.isConditionImmune()) {
         if (cardCondition == CardCondition.ASLEEP || cardCondition == CardCondition.CONFUSED || cardCondition == CardCondition.PARALYZED) {
            List overlaps = (List)this.conditions.stream().filter((c) -> {
               return c.getLeft() == CardCondition.ASLEEP || c.getLeft() == CardCondition.CONFUSED || c.getLeft() == CardCondition.PARALYZED;
            }).collect(Collectors.toList());
            Iterator var4 = overlaps.iterator();

            while(var4.hasNext()) {
               Pair overlap = (Pair)var4.next();
               this.conditions.remove(overlap);
            }
         }

         if (cardCondition == CardCondition.PARALYZED) {
            this.startTurnParalyzed = false;
         }

         Optional currentCondition = this.conditions.stream().filter((c) -> {
            return c.getLeft() == cardCondition;
         }).findFirst();
         if (currentCondition.isPresent() && modifier != null) {
            ((Pair)currentCondition.get()).setValue(modifier);
         } else {
            this.conditions.add(new MutablePair(cardCondition, modifier));
         }

      }
   }

   public void removeCondition(CardCondition cardCondition) {
      Optional currentStatus = this.conditions.stream().filter((s) -> {
         return s.getLeft() == cardCondition;
      }).findFirst();
      if (currentStatus.isPresent()) {
         this.conditions.remove(currentStatus.get());
      }

   }

   public void removeAllConditions() {
      this.conditions = new ArrayList();
   }

   public boolean isStartTurnParalyzed() {
      return this.startTurnParalyzed;
   }

   public void setStartTurnParalyzed(boolean startTurnParalyzed) {
      this.startTurnParalyzed = startTurnParalyzed;
   }

   public boolean isDamageImmune() {
      return this.damageImmune;
   }

   public void setDamageImmune(boolean damageImmune) {
      this.damageImmune = damageImmune;
   }

   public boolean isConditionImmune() {
      return this.conditionImmune;
   }

   public void setConditionImmune(boolean conditionImmune) {
      this.conditionImmune = conditionImmune;
   }
}

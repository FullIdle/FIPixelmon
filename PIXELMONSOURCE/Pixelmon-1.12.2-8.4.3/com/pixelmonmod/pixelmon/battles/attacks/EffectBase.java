package com.pixelmonmod.pixelmon.battles.attacks;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.modifiers.ModifierBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.modifiers.ModifierType;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class EffectBase {
   public ArrayList modifiers;
   private boolean persists;

   public EffectBase() {
      this(false);
   }

   public EffectBase(boolean persists) {
      this.modifiers = new ArrayList();
      this.persists = persists;
   }

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      return AttackResult.proceed;
   }

   public abstract void applyEffect(PixelmonWrapper var1, PixelmonWrapper var2);

   public boolean checkChance() {
      Iterator var1 = this.modifiers.iterator();

      ModifierBase m;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         m = (ModifierBase)var1.next();
      } while(m.type != ModifierType.Chance);

      boolean b = (float)RandomHelper.rand.nextInt(100) < m.value * (float)(m.multiplier == null ? 1 : m.multiplier);
      m.multiplier = 1;
      return b;
   }

   public boolean isChance() {
      Iterator var1 = this.modifiers.iterator();

      ModifierBase m;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         m = (ModifierBase)var1.next();
      } while(m.type != ModifierType.Chance);

      return true;
   }

   public float getChance() {
      Iterator var1 = this.modifiers.iterator();

      ModifierBase m;
      do {
         if (!var1.hasNext()) {
            return 100.0F;
         }

         m = (ModifierBase)var1.next();
      } while(m.type != ModifierType.Chance);

      return m.value;
   }

   public void changeChance(int multiplier) {
      Iterator var2 = this.modifiers.iterator();

      while(var2.hasNext()) {
         ModifierBase m = (ModifierBase)var2.next();
         if (m.type == ModifierType.Chance) {
            m.multiplier = multiplier;
         }
      }

   }

   public abstract boolean cantMiss(PixelmonWrapper var1);

   public boolean doesPersist(PixelmonWrapper user) {
      return this.persists;
   }

   public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public void applyEarlyEffect(PixelmonWrapper user) {
   }

   public void applyEffectAfterAllTargets(PixelmonWrapper user) {
   }

   public void dealtDamage(PixelmonWrapper attacker, PixelmonWrapper defender, Attack attack, DamageTypeEnum damageType) {
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
   }

   protected float getWeightWithChance(float weight) {
      float chance = this.getChance() / 100.0F;
      return chance >= 0.5F ? weight : chance;
   }

   public double modifyTypeEffectiveness(List effectiveTypes, EnumType moveType, double baseEffectiveness) {
      return baseEffectiveness;
   }

   public double modifyTypeEffectiveness(List effectiveTypes, EnumType moveType, double baseEffectiveness, BattleControllerBase bc) {
      return this.modifyTypeEffectiveness(effectiveTypes, moveType, baseEffectiveness);
   }

   public int modifyPriority(int priority, AttackBase attack, PixelmonWrapper pw) {
      return priority;
   }
}

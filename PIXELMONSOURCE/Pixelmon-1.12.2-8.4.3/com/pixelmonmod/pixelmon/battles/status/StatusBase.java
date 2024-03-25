package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class StatusBase extends EffectBase {
   public StatusType type;

   public StatusBase() {
   }

   public StatusBase(StatusType type) {
      this.type = type;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public void onAttackUsed(PixelmonWrapper user, Attack attack) {
   }

   public boolean stopsSwitching() {
      return false;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
   }

   public void onAttackEnd(PixelmonWrapper pw) {
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      return true;
   }

   public boolean stopsSelfStatusMove(PixelmonWrapper user, PixelmonWrapper opponent, Attack attack) {
      return false;
   }

   public void onEndOfTurn(PixelmonWrapper pw) {
   }

   public void onEndOfAttackersTurn(PixelmonWrapper statusedPokemon, PixelmonWrapper attacker) {
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{power, accuracy};
   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{power, accuracy};
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return false;
   }

   public boolean stopsIncomingAttackUser(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return false;
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
   }

   public boolean stopsStatusChange(StatusType t, PixelmonWrapper target, PixelmonWrapper user) {
      return false;
   }

   public void onDamageReceived(PixelmonWrapper userWrapper, PixelmonWrapper pokemon, Attack a, int damage, DamageTypeEnum damageType) {
   }

   public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a, DamageTypeEnum damageType) {
      return damage;
   }

   public boolean isTeamStatus() {
      return false;
   }

   public boolean isWholeTeamStatus() {
      return this.isTeamStatus();
   }

   public void applySwitchOutEffect(PixelmonWrapper outgoing, PixelmonWrapper incoming) {
   }

   public void applyEffectOnSwitch(PixelmonWrapper pw) {
   }

   public void applyBeforeEffect(PixelmonWrapper victim, PixelmonWrapper opponent) {
   }

   public void applyEndOfBattleEffect(PixelmonWrapper pokemon) {
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      return target.type;
   }

   public int[] modifyBaseStats(PixelmonWrapper user, int[] stats) {
      return stats;
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      return stats;
   }

   public int[] modifyStatsCancellable(PixelmonWrapper user, int[] stats) {
      return stats;
   }

   public boolean ignoreStatus(PixelmonWrapper user, PixelmonWrapper target) {
      return false;
   }

   public boolean allowsStatChange(PixelmonWrapper pokemon, PixelmonWrapper user, StatsEffect e) {
      return true;
   }

   public float modifyWeight(float initWeight) {
      return initWeight;
   }

   public boolean redirectAttack(PixelmonWrapper user, PixelmonWrapper targetAlly, Attack attack) {
      return false;
   }

   public boolean isImmune(PixelmonWrapper pokemon) {
      return false;
   }

   public StatusBase copy() {
      return this;
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return false;
   }

   public String getCureMessage() {
      return "";
   }

   public String getCureMessageItem() {
      return "";
   }

   public static StatusBase getNewInstance(Class statusClass) {
      try {
         return (StatusBase)statusClass.getConstructor().newInstance();
      } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException var2) {
         return NoStatus.noStatus;
      }
   }
}

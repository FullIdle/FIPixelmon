package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.IRarityTweak;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public abstract class AbilityBase implements ITranslatable, IRarityTweak {
   public final String getName() {
      return this.getClass().getSimpleName();
   }

   public String getUnlocalizedName() {
      return "ability." + this.getName() + ".name";
   }

   public void startMove(PixelmonWrapper user) {
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      return true;
   }

   public int modifyDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return damage;
   }

   public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return damage;
   }

   public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return damage;
   }

   public int modifyDamageTeammate(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return damage;
   }

   public void tookDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
   }

   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
   }

   public void tookDamageTargetAfterMove(PixelmonWrapper user, PixelmonWrapper target, Attack a, float damage) {
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{power, accuracy};
   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{power, accuracy};
   }

   public int[] modifyPowerAndAccuracyTeammate(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{power, accuracy};
   }

   public void beforeSwitch(PixelmonWrapper newPokemon) {
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
   }

   public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
   }

   public void applyFoeSwitchInEffect(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public void applyDynamaxEffect(PixelmonWrapper pokemon) {
   }

   public void applyEndOfBattleEffect(PixelmonWrapper pokemon) {
   }

   public boolean doesContactAttackMakeContact(PixelmonWrapper user, Attack a) {
      return true;
   }

   public void applyStartOfBattleHeadOfPartyEffect(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public void applyEffectOnContactUser(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public void applyEffectOnContactTargetLate(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      return stats;
   }

   public int[] modifyStatsTeammate(PixelmonWrapper pokemon, int[] stats) {
      return stats;
   }

   public int[] modifyStatsCancellable(PixelmonWrapper user, int[] stats) {
      return stats;
   }

   public int[] modifyStatsCancellableTeammate(PixelmonWrapper pokemon, int[] stats) {
      return stats;
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
   }

   public void applyRepeatedEffectAfterStatus(PixelmonWrapper pokemon) {
   }

   public boolean allowsStatChange(PixelmonWrapper pokemon, PixelmonWrapper user, StatsEffect e) {
      return true;
   }

   public boolean allowsStatChangeTeammate(PixelmonWrapper pokemon, PixelmonWrapper target, PixelmonWrapper user, StatsEffect e) {
      return true;
   }

   public boolean allowsOutgoingAttack(PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return true;
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      return true;
   }

   public boolean allowsIncomingAttackTeammate(PixelmonWrapper pokemon, PixelmonWrapper target, PixelmonWrapper user, Attack a) {
      return true;
   }

   public void allowsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
   }

   public void preProcessAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
   }

   public void preProcessAttackUser(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a) {
   }

   public void postProcessAttackUser(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a) {
   }

   public void postProcessAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
   }

   public void postProcessAttackOther(PixelmonWrapper pokemon, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
   }

   public boolean doesAttackUserIgnoreProtect(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a) {
      return false;
   }

   public float adjustCriticalHitChance(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a, float critChance) {
      return critChance;
   }

   public boolean preventsCriticalHits(PixelmonWrapper opponent) {
      return false;
   }

   public boolean allowsStatus(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper user) {
      return true;
   }

   public boolean allowsStatusTeammate(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper target, PixelmonWrapper user) {
      return true;
   }

   public double modifyStab(double stab) {
      return stab;
   }

   public EnumType modifyType(PixelmonWrapper pw, Attack attack) {
      return null;
   }

   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      return priority;
   }

   public boolean stopsSwitching(PixelmonWrapper user, PixelmonWrapper opponent) {
      return false;
   }

   public float modifyWeight(float initWeight) {
      return initWeight;
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      return target.type;
   }

   public void onStatusAdded(StatusBase status, PixelmonWrapper user, PixelmonWrapper opponent) {
   }

   public void onDamageReceived(PixelmonWrapper user, PixelmonWrapper pokemon, Attack a, int damage, DamageTypeEnum damagetype) {
   }

   public boolean ignoreWeather() {
      return false;
   }

   public boolean redirectAttack(PixelmonWrapper user, PixelmonWrapper targetAlly, Attack attack) {
      return false;
   }

   public void onItemConsumed(PixelmonWrapper pokemon, PixelmonWrapper consumer, ItemHeld heldItem) {
   }

   public void onAbilityLost(PixelmonWrapper pokemon) {
   }

   public void onAllyFaint(PixelmonWrapper pokemon, PixelmonWrapper fainted, PixelmonWrapper source) {
   }

   public void onSelfFaint(PixelmonWrapper pokemon, PixelmonWrapper source) {
   }

   public void onFoeFaint(PixelmonWrapper pokemon, PixelmonWrapper fainted, PixelmonWrapper source) {
   }

   public boolean needNewInstance() {
      return false;
   }

   public boolean isNegativeAbility() {
      return this instanceof Defeatist || this instanceof SlowStart || this instanceof Truant;
   }

   public void onWeatherChange(PixelmonWrapper pw, Weather weather) {
   }

   public void onTerrainSwitch(BattleControllerBase bc, PixelmonWrapper user, Terrain terrain) {
   }

   public boolean canBeIgnored() {
      return true;
   }

   public static boolean ignoreAbility(PixelmonWrapper pokemon, BattleControllerBase bc) {
      if (pokemon == null) {
         return false;
      } else {
         if (bc != null) {
            Optional optionalNeutralizingGas = bc.getAbilityIfPresent(NeutralizingGas.class);
            if (optionalNeutralizingGas.isPresent() && NeutralizingGas.isAbilityDisabled(pokemon.getBattleAbility(false))) {
               return true;
            }
         }

         return pokemon.hasStatus(StatusType.GastroAcid);
      }
   }

   public static boolean ignoreAbility(PixelmonWrapper user, PixelmonWrapper opponent) {
      if (ignoreAbility(opponent, opponent.bc)) {
         return true;
      } else if (!opponent.getBattleAbility(false).canBeIgnored()) {
         return false;
      } else if (user == null) {
         return false;
      } else {
         AbilityBase userAbility = user.getBattleAbility();
         if (user.attack != null && user.attack.getMove().getIgnoresAbilities() && user != opponent) {
            return true;
         } else {
            return userAbility instanceof MoldBreaker || userAbility instanceof Teravolt || userAbility instanceof Turboblaze;
         }
      }
   }

   public boolean equals(Object o) {
      return o instanceof AbilityBase && ((AbilityBase)o).getName().equals(this.getName());
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public boolean isAbility(Class ability) {
      return ability.isInstance(this);
   }

   @SafeVarargs
   public final boolean isAbility(Class... abilities) {
      return ArrayHelper.contains(abilities, this.getClass());
   }

   public static Optional getAbility(String name) {
      name = RegexPatterns.SPACE_SYMBOL.matcher(name).replaceAll("");

      try {
         Class abilityClass = Class.forName("com.pixelmonmod.pixelmon.entities.pixelmon.abilities." + name);
         AbilityBase ability = (AbilityBase)abilityClass.getConstructor().newInstance();
         return Optional.of(ability);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException | ClassNotFoundException var3) {
         return name.contains("Armor") ? getAbility(name.replace("Armor", "Armour")) : Optional.empty();
      }
   }

   public AbilityBase getNewInstance() {
      return getNewInstance(this.getClass());
   }

   public void sendActivatedMessage(PixelmonWrapper pw) {
      pw.bc.sendToAll("pixelmon.abilities.activated", pw.getNickname(), this.getTranslatedName());
   }

   public void onItemChanged(PixelmonWrapper pw, ItemHeld newItem) {
   }

   public float getMultiplier(AbstractSpawner spawner, SpawnInfo spawnInfo, float sum, float rarity) {
      return 1.0F;
   }

   public static AbilityBase getNewInstance(Class abilityClass) {
      try {
         return (AbilityBase)abilityClass.getConstructor().newInstance();
      } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException var2) {
         return ComingSoon.noAbility;
      }
   }
}

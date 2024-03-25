package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.tcg.duel.ability.BaseAbilityEffect;
import com.pixelmonmod.tcg.duel.ability.Bounce;
import com.pixelmonmod.tcg.duel.ability.Buzzap;
import com.pixelmonmod.tcg.duel.ability.Curse;
import com.pixelmonmod.tcg.duel.ability.DamageSwap;
import com.pixelmonmod.tcg.duel.ability.DamageSwapEnemy;
import com.pixelmonmod.tcg.duel.ability.DamageSwapToSelfOnly;
import com.pixelmonmod.tcg.duel.ability.DisableEvolution;
import com.pixelmonmod.tcg.duel.ability.EnergyTransfer;
import com.pixelmonmod.tcg.duel.ability.EvolutionaryLight;
import com.pixelmonmod.tcg.duel.ability.FlipToEvade;
import com.pixelmonmod.tcg.duel.ability.HalfDamage;
import com.pixelmonmod.tcg.duel.ability.Heal;
import com.pixelmonmod.tcg.duel.ability.InvisibleWall;
import com.pixelmonmod.tcg.duel.ability.Peek;
import com.pixelmonmod.tcg.duel.ability.RainDance;
import com.pixelmonmod.tcg.duel.ability.RetreatAid;
import com.pixelmonmod.tcg.duel.ability.RevealHand;
import com.pixelmonmod.tcg.duel.ability.Shift;
import com.pixelmonmod.tcg.duel.ability.SpecialDelivery;
import com.pixelmonmod.tcg.duel.ability.StepIn;
import com.pixelmonmod.tcg.duel.ability.StrikesBack;
import com.pixelmonmod.tcg.duel.ability.ThickSkinned;
import com.pixelmonmod.tcg.duel.ability.ToxicGas;
import com.pixelmonmod.tcg.duel.ability.Transform;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class AbilityEffectRegistry {
   private static final ConcurrentMap REGISTRY = Maps.newConcurrentMap();
   public static final BaseAbilityEffect DAMAGE_SWAP = register(new DamageSwap());
   public static final BaseAbilityEffect RAIN_DANCE = register(new RainDance());
   public static final BaseAbilityEffect STRIKES_BACK = register(new StrikesBack("StrikesBack", 10));
   public static final BaseAbilityEffect ENERGY_TRANSFER = register(new EnergyTransfer());
   public static final BaseAbilityEffect BUZZAP = register(new Buzzap());
   public static final BaseAbilityEffect INVISIBLE_WALL = register(new InvisibleWall("InvisibleWall", 30));
   public static final BaseAbilityEffect THICK_SKIN = register(new ThickSkinned());
   public static final BaseAbilityEffect SHIFT = register(new Shift());
   public static final BaseAbilityEffect HEAL = register(new Heal());
   public static final BaseAbilityEffect RETREAT_AID = register(new RetreatAid());
   public static final BaseAbilityEffect PEEK = register(new Peek());
   public static final BaseAbilityEffect DISABLE_EVOLUTION = register(new DisableEvolution());
   public static final BaseAbilityEffect TRANSFORM = register(new Transform());
   public static final BaseAbilityEffect STEP_IN = register(new StepIn());
   public static final BaseAbilityEffect DAMAGE_SWAP_ENEMY = register(new DamageSwapEnemy());
   public static final BaseAbilityEffect FLIP_TO_EVADE = register(new FlipToEvade());
   public static final BaseAbilityEffect TOXIC_GAS = register(new ToxicGas());
   public static final BaseAbilityEffect DAMAGE_SWAP_TO_SELF_ONLY = register(new DamageSwapToSelfOnly());
   public static final BaseAbilityEffect HALF_DAMAGE = register(new HalfDamage());
   public static final BaseAbilityEffect REVEAL_HAND = register(new RevealHand());
   public static final BaseAbilityEffect BOUNCE = register(new Bounce());
   public static final BaseAbilityEffect SPECIAL_DELIVERY = register(new SpecialDelivery());
   public static final BaseAbilityEffect CURSE = register(new Curse());
   public static final BaseAbilityEffect EVOLUTIONARY_LIGHT = register(new EvolutionaryLight());

   public static BaseAbilityEffect register(BaseAbilityEffect t) {
      REGISTRY.put(t.getCode().toLowerCase(), t);
      return t;
   }

   public static List getAll() {
      return Lists.newArrayList(REGISTRY.values());
   }

   public static BaseAbilityEffect get(String name) {
      return (BaseAbilityEffect)REGISTRY.get(name.toLowerCase());
   }

   public static BaseAbilityEffect getRandomEffect() {
      return (BaseAbilityEffect)RandomHelper.getRandomElementFromCollection(REGISTRY.values());
   }

   public static void load() {
   }
}

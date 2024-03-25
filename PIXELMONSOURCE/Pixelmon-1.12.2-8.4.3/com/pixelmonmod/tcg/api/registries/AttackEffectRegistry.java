package com.pixelmonmod.tcg.api.registries;

import com.google.common.collect.Maps;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.duel.attack.effects.AcidEffect;
import com.pixelmonmod.tcg.duel.attack.effects.AttachDiscardEffect;
import com.pixelmonmod.tcg.duel.attack.effects.BaseAttackEffect;
import com.pixelmonmod.tcg.duel.attack.effects.BenchDamageOppSelectEffect;
import com.pixelmonmod.tcg.duel.attack.effects.ChainLightningEffect;
import com.pixelmonmod.tcg.duel.attack.effects.ChangeResistanceEffect;
import com.pixelmonmod.tcg.duel.attack.effects.ChangeWeaknessEffect;
import com.pixelmonmod.tcg.duel.attack.effects.ConditionEffect;
import com.pixelmonmod.tcg.duel.attack.effects.DamageBonusEffect;
import com.pixelmonmod.tcg.duel.attack.effects.DamageEffect;
import com.pixelmonmod.tcg.duel.attack.effects.DestinyBondEffect;
import com.pixelmonmod.tcg.duel.attack.effects.DevolveEffect;
import com.pixelmonmod.tcg.duel.attack.effects.DisableAttackEffect;
import com.pixelmonmod.tcg.duel.attack.effects.DisableCardEffect;
import com.pixelmonmod.tcg.duel.attack.effects.DiscardAttachmentEffect;
import com.pixelmonmod.tcg.duel.attack.effects.DrawCardEffect;
import com.pixelmonmod.tcg.duel.attack.effects.DreamEaterEffect;
import com.pixelmonmod.tcg.duel.attack.effects.FindPokemonEffect;
import com.pixelmonmod.tcg.duel.attack.effects.FlipsMultiplyEffect;
import com.pixelmonmod.tcg.duel.attack.effects.HardenEffect;
import com.pixelmonmod.tcg.duel.attack.effects.HealEffect;
import com.pixelmonmod.tcg.duel.attack.effects.HurricaneEffect;
import com.pixelmonmod.tcg.duel.attack.effects.IgnoreStatsEffect;
import com.pixelmonmod.tcg.duel.attack.effects.ImmuneEffect;
import com.pixelmonmod.tcg.duel.attack.effects.LeechSeedEffect;
import com.pixelmonmod.tcg.duel.attack.effects.MetronomeEffect;
import com.pixelmonmod.tcg.duel.attack.effects.MirrorMoveEffect;
import com.pixelmonmod.tcg.duel.attack.effects.MissedEffect;
import com.pixelmonmod.tcg.duel.attack.effects.ProphecyEffect;
import com.pixelmonmod.tcg.duel.attack.effects.ReduceDamageEffect;
import com.pixelmonmod.tcg.duel.attack.effects.SandAttackEffect;
import com.pixelmonmod.tcg.duel.attack.effects.SearchDiscardEffect;
import com.pixelmonmod.tcg.duel.attack.effects.SearchDiscardWithEnergyCostEffect;
import com.pixelmonmod.tcg.duel.attack.effects.SuperFangEffect;
import com.pixelmonmod.tcg.duel.attack.effects.SwitchPokemonEffect;
import com.pixelmonmod.tcg.duel.attack.effects.SwordsDanceEffect;
import com.pixelmonmod.tcg.duel.attack.effects.ThunderstormEffect;
import com.pixelmonmod.tcg.duel.attack.effects.WildfireEffect;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class AttackEffectRegistry {
   private static final ConcurrentMap REGISTRY = Maps.newConcurrentMap();
   public static final BaseAttackEffect BENCH_DAMAGE_OPP_SELECT = register(BenchDamageOppSelectEffect.class);
   public static final BaseAttackEffect DEVOLVE = register(DevolveEffect.class);
   public static final BaseAttackEffect ATTACH_DISCARD_EFFECT = register(AttachDiscardEffect.class);
   public static final BaseAttackEffect CONDITION_EFFECT = register(ConditionEffect.class);
   public static final BaseAttackEffect HEAL_EFFECT = register(HealEffect.class);
   public static final BaseAttackEffect DISCARD_ATTACHMENT_EFFECT = register(DiscardAttachmentEffect.class);
   public static final BaseAttackEffect DAMAGE_EFFECT = register(DamageEffect.class);
   public static final BaseAttackEffect IMMUNE_EFFECT = register(ImmuneEffect.class);
   public static final BaseAttackEffect MISSED_EFFECT = register(MissedEffect.class);
   public static final BaseAttackEffect DAMAGE_BONUS_EFFECT = register(DamageBonusEffect.class);
   public static final BaseAttackEffect DAMAGE_REDUCE_EFFECT = register(ReduceDamageEffect.class);
   public static final BaseAttackEffect FLIPS_MULTIPLY_EFFECT = register(FlipsMultiplyEffect.class);
   public static final BaseAttackEffect SWITCH_POKEMON_EFFECT = register(SwitchPokemonEffect.class);
   public static final BaseAttackEffect CHANGE_WEAKNESS_EFFECT = register(ChangeWeaknessEffect.class);
   public static final BaseAttackEffect CHANGE_RESISTANCE_EFFECT = register(ChangeResistanceEffect.class);
   public static final BaseAttackEffect DISABLE_ATTACK_EFFECT = register(DisableAttackEffect.class);
   public static final BaseAttackEffect DRAW_CARD_EFFECT = register(DrawCardEffect.class);
   public static final BaseAttackEffect FIND_POKEMON_EFFECT = register(FindPokemonEffect.class);
   public static final BaseAttackEffect DREAM_EATER_EFFECT = register(DreamEaterEffect.class);
   public static final BaseAttackEffect SUPER_FANG_EFFECT = register(SuperFangEffect.class);
   public static final BaseAttackEffect LEACH_SEED_EFFECT = register(LeechSeedEffect.class);
   public static final BaseAttackEffect MIRROR_MOVE_EFFECT = register(MirrorMoveEffect.class);
   public static final BaseAttackEffect HARDEN_EFFECT = register(HardenEffect.class);
   public static final BaseAttackEffect HURRICANE_EFFECT = register(HurricaneEffect.class);
   public static final BaseAttackEffect DESTINY_BOND_EFFECT = register(DestinyBondEffect.class);
   public static final BaseAttackEffect ACID_EFFECT = register(AcidEffect.class);
   public static final BaseAttackEffect CHAIN_LIGHTNING_EFFECT = register(ChainLightningEffect.class);
   public static final BaseAttackEffect SWORDS_DANCE_EFFECT = register(SwordsDanceEffect.class);
   public static final BaseAttackEffect METRONOME_EFFECT = register(MetronomeEffect.class);
   public static final BaseAttackEffect SAND_ATTACK_EFFECT = register(SandAttackEffect.class);
   public static final BaseAttackEffect IGNORE_STATS_EFFECT = register(IgnoreStatsEffect.class);
   public static final BaseAttackEffect PROPHECY_EFFECT = register(ProphecyEffect.class);
   public static final BaseAttackEffect WILD_FIRE_EFFECT = register(WildfireEffect.class);
   public static final BaseAttackEffect THUNDER_STORM_EFFECT = register(ThunderstormEffect.class);
   public static final BaseAttackEffect SEARCH_DISCARD_EFFECT = register(SearchDiscardEffect.class);
   public static final BaseAttackEffect SEARCH_DISCARD_COST_EFFECT = register(SearchDiscardWithEnergyCostEffect.class);
   public static final BaseAttackEffect DISABLE_CARD_EFFECT = register(DisableCardEffect.class);

   public static BaseAttackEffect register(Class clazz) {
      Supplier constructor = getConstructor(clazz);
      BaseAttackEffect defaultInstance = (BaseAttackEffect)constructor.get();
      String[] var3 = defaultInstance.getCodes();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String code = var3[var5];
         REGISTRY.put(code.toLowerCase(), constructor);
      }

      return defaultInstance;
   }

   private static Supplier getConstructor(Class clazz) {
      try {
         Constructor constructor = clazz.getConstructor();
         return () -> {
            try {
               return (BaseAttackEffect)constructor.newInstance();
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException var3) {
               TCG.logger.error("No default constructor for: " + clazz.getSimpleName());
               return null;
            }
         };
      } catch (NoSuchMethodException var2) {
         TCG.logger.error("No default constructor for: " + clazz.getSimpleName());
         return null;
      }
   }

   public static BaseAttackEffect get(String code) {
      Supplier baseAttackEffectSupplier = (Supplier)REGISTRY.get(code.toLowerCase());
      return baseAttackEffectSupplier == null ? null : (BaseAttackEffect)baseAttackEffectSupplier.get();
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.HashMap;

public abstract class EvoCondition {
   public static final HashMap evoConditionTypes = new HashMap();
   public String evoConditionType = null;

   public EvoCondition(String evoConditionType) {
      this.evoConditionType = evoConditionType;
   }

   public abstract boolean passes(EntityPixelmon var1);

   static {
      evoConditionTypes.put("biome", BiomeCondition.class);
      evoConditionTypes.put("chance", ChanceCondition.class);
      evoConditionTypes.put("status", StatusPersistCondition.class);
      evoConditionTypes.put("evolutionRock", EvoRockCondition.class);
      evoConditionTypes.put("evolutionScroll", EvoScrollCondition.class);
      evoConditionTypes.put("friendship", FriendshipCondition.class);
      evoConditionTypes.put("nature", NatureCondition.class);
      evoConditionTypes.put("gender", GenderCondition.class);
      evoConditionTypes.put("heldItem", HeldItemCondition.class);
      evoConditionTypes.put("highAltitude", HighAltitudeCondition.class);
      evoConditionTypes.put("level", LevelCondition.class);
      evoConditionTypes.put("move", MoveCondition.class);
      evoConditionTypes.put("moveType", MoveTypeCondition.class);
      evoConditionTypes.put("party", PartyCondition.class);
      evoConditionTypes.put("statRatio", StatRatioCondition.class);
      evoConditionTypes.put("time", TimeCondition.class);
      evoConditionTypes.put("weather", WeatherCondition.class);
      evoConditionTypes.put("moonPhase", MoonPhaseCondition.class);
      evoConditionTypes.put("withinStructure", WithinStructureCondition.class);
      evoConditionTypes.put("healthAbsence", AbsenceOfHealthCondition.class);
      evoConditionTypes.put("potionEffect", PotionEffectCondition.class);
      evoConditionTypes.put("isBurning", IsBurningCondition.class);
      evoConditionTypes.put("ores", OreCondition.class);
      evoConditionTypes.put("critical", BattleCriticalCondition.class);
      evoConditionTypes.put("invert", InvertCondition.class);
      evoConditionTypes.put("recoil", RecoilDamageCondition.class);
   }
}

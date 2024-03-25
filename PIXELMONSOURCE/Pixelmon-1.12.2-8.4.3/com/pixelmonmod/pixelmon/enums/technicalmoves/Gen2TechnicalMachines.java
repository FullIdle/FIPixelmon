package com.pixelmonmod.pixelmon.enums.technicalmoves;

import com.pixelmonmod.pixelmon.enums.EnumType;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.HashMap;
import java.util.Map;

public enum Gen2TechnicalMachines implements ITechnicalMove {
   DynamicPunch(1, "Dynamic Punch"),
   Headbutt(2, "Headbutt"),
   Curse(3, "Curse"),
   Rollout(4, "Rollout"),
   Roar(5, "Roar"),
   Toxic(6, "Toxic"),
   Zap_Cannon(7, "Zap Cannon"),
   Rock_Smash(8, "Rock Smash"),
   Psych_Up(9, "Psych Up"),
   Hidden_Power(10, "Hidden Power"),
   Sunny_Day(11, "Sunny Day"),
   Sweet_Scent(12, "Sweet Scent"),
   Snore(13, "Snore"),
   Blizzard(14, "Blizzard"),
   Hyper_Beam(15, "Hyper Beam"),
   Icy_Wind(16, "Icy Wind"),
   Protect(17, "Protect"),
   Rain_Dance(18, "Rain Dance"),
   Giga_Drain(19, "Giga Drain"),
   Endure(20, "Endure"),
   Frustration(21, "Frustration"),
   Solar_Beam(22, "Solar Beam"),
   Iron_Tail(23, "Iron Tail"),
   DragonBreath(24, "Dragon Breath"),
   Thunder(25, "Thunder"),
   Earthquake(26, "Earthquake"),
   Return(27, "Return"),
   Dig(28, "Dig"),
   Psychic(29, "Psychic"),
   Shadow_Ball(30, "Shadow Ball"),
   Mud_Slap(31, "Mud-Slap"),
   Double_Team(32, "Double Team"),
   Ice_Punch(33, "Ice Punch"),
   Swagger(34, "Swagger"),
   Sleep_Talk(35, "Sleep Talk"),
   Sludge_Bomb(36, "Sludge Bomb"),
   Sandstorm(37, "Sandstorm"),
   Fire_Blast(38, "Fire Blast"),
   Swift(39, "Swift"),
   Defense_Curl(40, "Defense Curl"),
   Thunder_Punch(41, "Thunder Punch"),
   Dream_Eater(42, "Dream Eater"),
   Detect(43, "Detect"),
   Rest(44, "Rest"),
   Attract(45, "Attract"),
   Thief(46, "Thief"),
   Steel_Wing(47, "Steel Wing"),
   Fire_Punch(48, "Fire Punch"),
   Fury_Cutter(49, "Fury Cutter"),
   Nightmare(50, "Nightmare");

   private final int id;
   private final String attackName;
   private static final Gen2TechnicalMachines[] VALUES = values();
   protected static final Map idMap = new Int2ObjectArrayMap(VALUES.length);
   protected static final Map nameMap = new HashMap(VALUES.length);
   protected static final Map typeMap = new HashMap(EnumType.values().length);

   private Gen2TechnicalMachines(int id, String attackName) {
      this.id = id;
      this.attackName = attackName;
   }

   public String prefix() {
      return "tm_gen2";
   }

   public int getId() {
      return this.id;
   }

   public String getAttackName() {
      return this.attackName;
   }

   public Map getTypeMap() {
      return typeMap;
   }

   public static ITechnicalMove getTm(int id) {
      return (ITechnicalMove)idMap.get(id);
   }

   public static ITechnicalMove getTm(String attackName) {
      return (ITechnicalMove)nameMap.get(attackName);
   }

   static {
      Gen2TechnicalMachines[] var0 = VALUES;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Gen2TechnicalMachines tm = var0[var2];
         idMap.put(tm.id, tm);
         nameMap.put(tm.attackName, tm);
      }

   }
}

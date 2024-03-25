package com.pixelmonmod.pixelmon.enums.technicalmoves;

import com.pixelmonmod.pixelmon.enums.EnumType;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.HashMap;
import java.util.Map;

public enum Gen3TechnicalMachines implements ITechnicalMove {
   Focus_Punch(1, "Focus Punch"),
   Dragon_Claw(2, "Dragon Claw"),
   Water_Pulse(3, "Water Pulse"),
   Calm_Mind(4, "Calm Mind"),
   Roar(5, "Roar"),
   Toxic(6, "Toxic"),
   Hail(7, "Hail"),
   Bulk_Up(8, "Bulk Up"),
   Bullet_Seed(9, "Bullet Seed"),
   Hidden_Power(10, "Hidden Power"),
   Sunny_Day(11, "Sunny Day"),
   Taunt(12, "Taunt"),
   Ice_Beam(13, "Ice Beam"),
   Blizzard(14, "Blizzard"),
   Hyper_Beam(15, "Hyper Beam"),
   Light_Screen(16, "Light Screen"),
   Protect(17, "Protect"),
   Rain_Dance(18, "Rain Dance"),
   Giga_Drain(19, "Giga Drain"),
   Safeguard(20, "Safeguard"),
   Frustration(21, "Frustration"),
   Solar_Beam(22, "Solar Beam"),
   Iron_Tail(23, "Iron Tail"),
   Thunderbolt(24, "Thunderbolt"),
   Thunder(25, "Thunder"),
   Earthquake(26, "Earthquake"),
   Return(27, "Return"),
   Dig(28, "Dig"),
   Psychic(29, "Psychic"),
   Shadow_Ball(30, "Shadow Ball"),
   Brick_Break(31, "Brick Break"),
   Double_Team(32, "Double Team"),
   Reflect(33, "Reflect"),
   Shock_Wave(34, "Shock Wave"),
   Flamethrower(35, "Flamethrower"),
   Sludge_Bomb(36, "Sludge Bomb"),
   Sandstorm(37, "Sandstorm"),
   Fire_Blast(38, "Fire Blast"),
   Rock_Tomb(39, "Rock Tomb"),
   Aerial_Ace(40, "Aerial Ace"),
   Torment(41, "Torment"),
   Facade(42, "Facade"),
   Secret_Power(43, "Secret Power"),
   Rest(44, "Rest"),
   Attract(45, "Attract"),
   Thief(46, "Thief"),
   Steel_Wing(47, "Steel Wing"),
   Skill_Swap(48, "Skill Swap"),
   Snatch(49, "Snatch"),
   Overheat(50, "Overheat");

   private final int id;
   private final String attackName;
   private static final Gen3TechnicalMachines[] VALUES = values();
   protected static final Map idMap = new Int2ObjectArrayMap(VALUES.length);
   protected static final Map nameMap = new HashMap(VALUES.length);
   protected static final Map typeMap = new HashMap(EnumType.values().length);

   private Gen3TechnicalMachines(int id, String attackName) {
      this.id = id;
      this.attackName = attackName;
   }

   public String prefix() {
      return "tm_gen3";
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
      Gen3TechnicalMachines[] var0 = VALUES;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Gen3TechnicalMachines tm = var0[var2];
         idMap.put(tm.id, tm);
         nameMap.put(tm.attackName, tm);
      }

   }
}

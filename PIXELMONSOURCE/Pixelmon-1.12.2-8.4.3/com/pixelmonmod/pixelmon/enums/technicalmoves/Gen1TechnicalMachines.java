package com.pixelmonmod.pixelmon.enums.technicalmoves;

import com.pixelmonmod.pixelmon.enums.EnumType;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.HashMap;
import java.util.Map;

public enum Gen1TechnicalMachines implements ITechnicalMove {
   Mega_Punch(1, "Mega Punch"),
   Razor_Wind(2, "Razor Wind"),
   Swords_Dance(3, "Swords Dance"),
   Whirlwind(4, "Whirlwind"),
   Mega_Kick(5, "Mega Kick"),
   Toxic(6, "Toxic"),
   Horn_Drill(7, "Horn Drill"),
   Body_Slam(8, "Body Slam"),
   Take_Down(9, "Take Down"),
   Double_Edge(10, "Double-Edge"),
   BubbleBeam(11, "Bubble Beam"),
   Water_Gun(12, "Water Gun"),
   Ice_Beam(13, "Ice Beam"),
   Blizzard(14, "Blizzard"),
   Hyper_Beam(15, "Hyper Beam"),
   Pay_Day(16, "Pay Day"),
   Submission(17, "Submission"),
   Counter(18, "Counter"),
   Seismic_Toss(19, "Seismic Toss"),
   Rage(20, "Rage"),
   Mega_Drain(21, "Mega Drain"),
   SolarBeam(22, "Solar Beam"),
   Dragon_Rage(23, "Dragon Rage"),
   Thunderbolt(24, "Thunderbolt"),
   Thunder(25, "Thunder"),
   Earthquake(26, "Earthquake"),
   Fissure(27, "Fissure"),
   Dig(28, "Dig"),
   Psychic(29, "Psychic"),
   Teleport(30, "Teleport"),
   Mimic(31, "Mimic"),
   Double_Team(32, "Double Team"),
   Reflect(33, "Reflect"),
   Bide(34, "Bide"),
   Metronome(35, "Metronome"),
   SelfDestruct(36, "Self-Destruct"),
   Egg_Bomb(37, "Egg Bomb"),
   Fire_Blast(38, "Fire Blast"),
   Swift(39, "Swift"),
   Skull_Bash(40, "Skull Bash"),
   SoftBoiled(41, "Soft-Boiled"),
   Dream_Eater(42, "Dream Eater"),
   Sky_Attack(43, "Sky Attack"),
   Rest(44, "Rest"),
   Thunder_Wave(45, "Thunder Wave"),
   Psywave(46, "Psywave"),
   Explosion(47, "Explosion"),
   Rock_Slide(48, "Rock Slide"),
   Tri_Attack(49, "Tri Attack"),
   Substitute(50, "Substitute");

   private final int id;
   private final String attackName;
   private static final Gen1TechnicalMachines[] VALUES = values();
   protected static final Map idMap = new Int2ObjectArrayMap(VALUES.length);
   protected static final Map nameMap = new HashMap(VALUES.length);
   protected static final Map typeMap = new HashMap(EnumType.values().length);

   private Gen1TechnicalMachines(int id, String attackName) {
      this.id = id;
      this.attackName = attackName;
   }

   public String prefix() {
      return "tm_gen1";
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
      Gen1TechnicalMachines[] var0 = VALUES;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Gen1TechnicalMachines tm = var0[var2];
         idMap.put(tm.id, tm);
         nameMap.put(tm.attackName, tm);
      }

   }
}

package com.pixelmonmod.pixelmon.enums.technicalmoves;

import com.pixelmonmod.pixelmon.enums.EnumType;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.HashMap;
import java.util.Map;

public enum Gen4TechnicalMachines implements ITechnicalMove {
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
   Overheat(50, "Overheat"),
   Roost(51, "Roost"),
   Focus_Blast(52, "Focus Blast"),
   Energy_Ball(53, "Energy Ball"),
   False_Swipe(54, "False Swipe"),
   Brine(55, "Brine"),
   Fling(56, "Fling"),
   Charge_Beam(57, "Charge Beam"),
   Endure(58, "Endure"),
   Dragon_Pulse(59, "Dragon Pulse"),
   Drain_Punch(60, "Drain Punch"),
   Will_O_Wisp(61, "Will-O-Wisp"),
   Silver_Wind(62, "Silver Wind"),
   Embargo(63, "Embargo"),
   Explosion(64, "Explosion"),
   Shadow_Claw(65, "Shadow Claw"),
   Payback(66, "Payback"),
   Recycle(67, "Recycle"),
   Giga_Impact(68, "Giga Impact"),
   Rock_Polish(69, "Rock Polish"),
   Flash(70, "Flash"),
   Stone_Edge(71, "Stone Edge"),
   Avalanche(72, "Avalanche"),
   Thunder_Wave(73, "Thunder Wave"),
   Gyro_Ball(74, "Gyro Ball"),
   Swords_Dance(75, "Swords Dance"),
   Stealth_Rock(76, "Stealth Rock"),
   Psych_Up(77, "Psych Up"),
   Captivate(78, "Captivate"),
   Dark_Pulse(79, "Dark Pulse"),
   Rock_Slide(80, "Rock Slide"),
   X_Scissor(81, "X-Scissor"),
   Sleep_Talk(82, "Sleep Talk"),
   Natural_Gift(83, "Natural Gift"),
   Poison_Jab(84, "Poison Jab"),
   Dream_Eater(85, "Dream Eater"),
   Grass_Knot(86, "Grass Knot"),
   Swagger(87, "Swagger"),
   Pluck(88, "Pluck"),
   U_turn(89, "U-turn"),
   Substitute(90, "Substitute"),
   Flash_Cannon(91, "Flash Cannon"),
   Trick_Room(92, "Trick Room");

   private final int id;
   private final String attackName;
   private static final Gen4TechnicalMachines[] VALUES = values();
   protected static final Map idMap = new Int2ObjectArrayMap(VALUES.length);
   protected static final Map nameMap = new HashMap(VALUES.length);
   protected static final Map typeMap = new HashMap(EnumType.values().length);

   private Gen4TechnicalMachines(int id, String attackName) {
      this.id = id;
      this.attackName = attackName;
   }

   public String prefix() {
      return "tm_gen4";
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
      Gen4TechnicalMachines[] var0 = VALUES;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Gen4TechnicalMachines tm = var0[var2];
         idMap.put(tm.id, tm);
         nameMap.put(tm.attackName, tm);
      }

   }
}

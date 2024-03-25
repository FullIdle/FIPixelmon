package com.pixelmonmod.pixelmon.enums.technicalmoves;

import com.pixelmonmod.pixelmon.enums.EnumType;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.HashMap;
import java.util.Map;

public enum Gen7TechnicalMachines implements ITechnicalMove {
   Work_Up(1, "Work Up"),
   Dragon_Claw(2, "Dragon Claw"),
   Psyshock(3, "Psyshock"),
   Calm_Mind(4, "Calm Mind"),
   Roar(5, "Roar"),
   Toxic(6, "Toxic"),
   Hail(7, "Hail"),
   Bulk_Up(8, "Bulk Up"),
   Venoshock(9, "Venoshock"),
   Hidden_Power(10, "Hidden Power"),
   Sunny_Day(11, "Sunny Day"),
   Taunt(12, "Taunt"),
   Ice_Beam(13, "Ice Beam"),
   Blizzard(14, "Blizzard"),
   Hyper_Beam(15, "Hyper Beam"),
   Light_Screen(16, "Light Screen"),
   Protect(17, "Protect"),
   Rain_Dance(18, "Rain Dance"),
   Roost(19, "Roost"),
   Safeguard(20, "Safeguard"),
   Frustration(21, "Frustration"),
   Solar_Beam(22, "Solar Beam"),
   Smack_Down(23, "Smack Down"),
   Thunderbolt(24, "Thunderbolt"),
   Thunder(25, "Thunder"),
   Earthquake(26, "Earthquake"),
   Return(27, "Return"),
   Leech_Life(28, "Leech Life"),
   Psychic(29, "Psychic"),
   Shadow_Ball(30, "Shadow Ball"),
   Brick_Break(31, "Brick Break"),
   Double_Team(32, "Double Team"),
   Reflect(33, "Reflect"),
   Sludge_Wave(34, "Sludge Wave"),
   Flamethrower(35, "Flamethrower"),
   Sludge_Bomb(36, "Sludge Bomb"),
   Sandstorm(37, "Sandstorm"),
   Fire_Blast(38, "Fire Blast"),
   Rock_Tomb(39, "Rock Tomb"),
   Aerial_Ace(40, "Aerial Ace"),
   Torment(41, "Torment"),
   Facade(42, "Facade"),
   Flame_Charge(43, "Flame Charge"),
   Rest(44, "Rest"),
   Attract(45, "Attract"),
   Thief(46, "Thief"),
   Low_Sweep(47, "Low Sweep"),
   Round(48, "Round"),
   Echoed_Voice(49, "Echoed Voice"),
   Overheat(50, "Overheat"),
   Steel_Wing(51, "Steel Wing"),
   Focus_Blast(52, "Focus Blast"),
   Energy_Ball(53, "Energy Ball"),
   False_Swipe(54, "False Swipe"),
   Scald(55, "Scald"),
   Fling(56, "Fling"),
   Charge_Beam(57, "Charge Beam"),
   Sky_Drop(58, "Sky Drop"),
   Brutal_Swing(59, "Brutal Swing"),
   Quash(60, "Quash"),
   Will_O_Wisp(61, "Will-O-Wisp"),
   Acrobatics(62, "Acrobatics"),
   Embargo(63, "Embargo"),
   Explosion(64, "Explosion"),
   Shadow_Claw(65, "Shadow Claw"),
   Payback(66, "Payback"),
   Smart_Strike(67, "Smart Strike"),
   Giga_Impact(68, "Giga Impact"),
   Rock_Polish(69, "Rock Polish"),
   Aurora_Veil(70, "Aurora Veil"),
   Stone_Edge(71, "Stone Edge"),
   Volt_Switch(72, "Volt Switch"),
   Thunder_Wave(73, "Thunder Wave"),
   Gyro_Ball(74, "Gyro Ball"),
   Swords_Dance(75, "Swords Dance"),
   Fly(76, "Fly"),
   Psych_Up(77, "Psych Up"),
   Bulldoze(78, "Bulldoze"),
   Frost_Breath(79, "Frost Breath"),
   Rock_Slide(80, "Rock Slide"),
   X_Scissor(81, "X-Scissor"),
   Dragon_Tail(82, "Dragon Tail"),
   Infestation(83, "Infestation"),
   Poison_Jab(84, "Poison Jab"),
   Dream_Eater(85, "Dream Eater"),
   Grass_Knot(86, "Grass Knot"),
   Swagger(87, "Swagger"),
   Sleep_Talk(88, "Sleep Talk"),
   U_turn(89, "U-turn"),
   Substitute(90, "Substitute"),
   Flash_Cannon(91, "Flash Cannon"),
   Trick_Room(92, "Trick Room"),
   Wild_Charge(93, "Wild Charge"),
   Surf(94, "Surf"),
   Snarl(95, "Snarl"),
   Nature_Power(96, "Nature Power"),
   Dark_Pulse(97, "Dark Pulse"),
   Waterfall(98, "Waterfall"),
   Dazzling_Gleam(99, "Dazzling Gleam"),
   Confide(100, "Confide");

   private final int id;
   private final String attackName;
   private static final Gen7TechnicalMachines[] VALUES = values();
   protected static final Map idMap = new Int2ObjectArrayMap(VALUES.length);
   protected static final Map nameMap = new HashMap(VALUES.length);
   protected static final Map typeMap = new HashMap(EnumType.values().length);

   private Gen7TechnicalMachines(int id, String attackName) {
      this.id = id;
      this.attackName = attackName;
   }

   public String prefix() {
      return "tm_gen7";
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
      Gen7TechnicalMachines[] var0 = VALUES;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Gen7TechnicalMachines tm = var0[var2];
         idMap.put(tm.id, tm);
         nameMap.put(tm.attackName, tm);
      }

   }
}

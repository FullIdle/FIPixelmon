package com.pixelmonmod.pixelmon.enums.technicalmoves;

import com.pixelmonmod.pixelmon.enums.EnumType;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.HashMap;
import java.util.Map;

public enum Gen8TechnicalMachines implements ITechnicalMove {
   MegaPunch(0, "Mega Punch"),
   MegaKick(1, "Mega Kick"),
   PayDay(2, "Pay Day"),
   FirePunch(3, "Fire Punch"),
   IcePunch(4, "Ice Punch"),
   ThunderPunch(5, "Thunder Punch"),
   Fly(6, "Fly"),
   PinMissile(7, "Pin Missile"),
   HyperBeam(8, "Hyper Beam"),
   GigaImpact(9, "Giga Impact"),
   MagicalLeaf(10, "Magical Leaf"),
   SolarBeam(11, "Solar Beam"),
   SolarBlade(12, "Solar Blade"),
   FireSpin(13, "Fire Spin"),
   ThunderWave(14, "Thunder Wave"),
   Dig(15, "Dig"),
   Screech(16, "Screech"),
   LightScreen(17, "Light Screen"),
   Reflect(18, "Reflect"),
   Safeguard(19, "Safeguard"),
   Self_Destruct(20, "Self-Destruct"),
   Rest(21, "Rest"),
   RockSlide(22, "Rock Slide"),
   Thief(23, "Thief"),
   Snore(24, "Snore"),
   Protect(25, "Protect"),
   ScaryFace(26, "Scary Face"),
   IcyWind(27, "Icy Wind"),
   GigaDrain(28, "Giga Drain"),
   Charm(29, "Charm"),
   SteelWing(30, "Steel Wing"),
   Attract(31, "Attract"),
   Sandstorm(32, "Sandstorm"),
   RainDance(33, "Rain Dance"),
   SunnyDay(34, "Sunny Day"),
   Hail(35, "Hail"),
   Whirlpool(36, "Whirlpool"),
   BeatUp(37, "Beat Up"),
   Will_O_Wisp(38, "Will-O-Wisp"),
   Facade(39, "Facade"),
   Swift(40, "Swift"),
   HelpingHand(41, "Helping Hand"),
   Revenge(42, "Revenge"),
   BrickBreak(43, "Brick Break"),
   Imprison(44, "Imprison"),
   Dive(45, "Dive"),
   WeatherBall(46, "Weather Ball"),
   FakeTears(47, "Fake Tears"),
   RockTomb(48, "Rock Tomb"),
   SandTomb(49, "Sand Tomb"),
   BulletSeed(50, "Bullet Seed"),
   IcicleSpear(51, "Icicle Spear"),
   Bounce(52, "Bounce"),
   MudShot(53, "Mud Shot"),
   RockBlast(54, "Rock Blast"),
   Brine(55, "Brine"),
   U_turn(56, "U-turn"),
   Payback(57, "Payback"),
   Assurance(58, "Assurance"),
   Fling(59, "Fling"),
   PowerSwap(60, "Power Swap"),
   GuardSwap(61, "Guard Swap"),
   SpeedSwap(62, "Speed Swap"),
   DrainPunch(63, "Drain Punch"),
   Avalanche(64, "Avalanche"),
   ShadowClaw(65, "Shadow Claw"),
   ThunderFang(66, "Thunder Fang"),
   IceFang(67, "Ice Fang"),
   FireFang(68, "Fire Fang"),
   PsychoCut(69, "Psycho Cut"),
   TrickRoom(70, "Trick Room"),
   WonderRoom(71, "Wonder Room"),
   MagicRoom(72, "Magic Room"),
   CrossPoison(73, "Cross Poison"),
   Venoshock(74, "Venoshock"),
   LowSweep(75, "Low Sweep"),
   Round(76, "Round"),
   Hex(77, "Hex"),
   Acrobatics(78, "Acrobatics"),
   Retaliate(79, "Retaliate"),
   VoltSwitch(80, "Volt Switch"),
   Bulldoze(81, "Bulldoze"),
   Electroweb(82, "Electroweb"),
   RazorShell(83, "Razor Shell"),
   TailSlap(84, "Tail Slap"),
   Snarl(85, "Snarl"),
   PhantomForce(86, "Phantom Force"),
   DrainingKiss(87, "Draining Kiss"),
   GrassyTerrain(88, "Grassy Terrain"),
   MistyTerrain(89, "Misty Terrain"),
   ElectricTerrain(90, "Electric Terrain"),
   PsychicTerrain(91, "Psychic Terrain"),
   MysticalFire(92, "Mystical Fire"),
   EerieImpulse(93, "Eerie Impulse"),
   FalseSwipe(94, "False Swipe"),
   AirSlash(95, "Air Slash"),
   SmartStrike(96, "Smart Strike"),
   BrutalSwing(97, "Brutal Swing"),
   StompingTantrum(98, "Stomping Tantrum"),
   BreakingSwipe(99, "Breaking Swipe");

   private final int id;
   private final String attackName;
   private static final Gen8TechnicalMachines[] VALUES = values();
   protected static final Map idMap = new Int2ObjectArrayMap(VALUES.length);
   protected static final Map nameMap = new HashMap(VALUES.length);
   protected static final Map typeMap = new HashMap(EnumType.values().length);

   private Gen8TechnicalMachines(int id, String attackName) {
      this.id = id;
      this.attackName = attackName;
   }

   public String prefix() {
      return "tm_gen8";
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
      Gen8TechnicalMachines[] var0 = VALUES;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Gen8TechnicalMachines tm = var0[var2];
         idMap.put(tm.id, tm);
         nameMap.put(tm.attackName, tm);
      }

   }
}

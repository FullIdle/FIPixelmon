package com.pixelmonmod.pixelmon.enums.technicalmoves;

import com.pixelmonmod.pixelmon.enums.EnumType;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.HashMap;
import java.util.Map;

public enum Gen8TechnicalRecords implements ITechnicalMove {
   SwordsDance(0, "Swords Dance"),
   BodySlam(1, "Body Slam"),
   Flamethrower(2, "Flamethrower"),
   HydroPump(3, "Hydro Pump"),
   Surf(4, "Surf"),
   IceBeam(5, "Ice Beam"),
   Blizzard(6, "Blizzard"),
   LowKick(7, "Low Kick"),
   Thunderbolt(8, "Thunderbolt"),
   Thunder(9, "Thunder"),
   Earthquake(10, "Earthquake"),
   Psychic(11, "Psychic"),
   Agility(12, "Agility"),
   FocusEnergy(13, "Focus Energy"),
   Metronome(14, "Metronome"),
   FireBlast(15, "Fire Blast"),
   Waterfall(16, "Waterfall"),
   Amnesia(17, "Amnesia"),
   LeechLife(18, "Leech Life"),
   TriAttack(19, "Tri Attack"),
   Substitute(20, "Substitute"),
   Reversal(21, "Reversal"),
   SludgeBomb(22, "Sludge Bomb"),
   Spikes(23, "Spikes"),
   Outrage(24, "Outrage"),
   Psyshock(25, "Psyshock"),
   Endure(26, "Endure"),
   SleepTalk(27, "Sleep Talk"),
   Megahorn(28, "Megahorn"),
   BatonPass(29, "Baton Pass"),
   Encore(30, "Encore"),
   IronTail(31, "Iron Tail"),
   Crunch(32, "Crunch"),
   ShadowBall(33, "Shadow Ball"),
   FutureSight(34, "Future Sight"),
   Uproar(35, "Uproar"),
   HeatWave(36, "Heat Wave"),
   Taunt(37, "Taunt"),
   Trick(38, "Trick"),
   Superpower(39, "Superpower"),
   SkillSwap(40, "Skill Swap"),
   BlazeKick(41, "Blaze Kick"),
   HyperVoice(42, "Hyper Voice"),
   Overheat(43, "Overheat"),
   CosmicPower(44, "Cosmic Power"),
   MuddyWater(45, "Muddy Water"),
   IronDefense(46, "Iron Defense"),
   DragonClaw(47, "Dragon Claw"),
   BulkUp(48, "Bulk Up"),
   CalmMind(49, "Calm Mind"),
   LeafBlade(50, "Leaf Blade"),
   DragonDance(51, "Dragon Dance"),
   GyroBall(52, "Gyro Ball"),
   CloseCombat(53, "Close Combat"),
   ToxicSpikes(54, "Toxic Spikes"),
   FlareBlitz(55, "Flare Blitz"),
   AuraSphere(56, "Aura Sphere"),
   PoisonJab(57, "Poison Jab"),
   DarkPulse(58, "Dark Pulse"),
   SeedBomb(59, "Seed Bomb"),
   X_Scissor(60, "X-Scissor"),
   BugBuzz(61, "Bug Buzz"),
   DragonPulse(62, "Dragon Pulse"),
   PowerGem(63, "Power Gem"),
   FocusBlast(64, "Focus Blast"),
   EnergyBall(65, "Energy Ball"),
   BraveBird(66, "Brave Bird"),
   EarthPower(67, "Earth Power"),
   NastyPlot(68, "Nasty Plot"),
   ZenHeadbutt(69, "Zen Headbutt"),
   FlashCannon(70, "Flash Cannon"),
   LeafStorm(71, "Leaf Storm"),
   PowerWhip(72, "Power Whip"),
   GunkShot(73, "Gunk Shot"),
   IronHead(74, "Iron Head"),
   StoneEdge(75, "Stone Edge"),
   StealthRock(76, "Stealth Rock"),
   GrassKnot(77, "Grass Knot"),
   SludgeWave(78, "Sludge Wave"),
   HeavySlam(79, "Heavy Slam"),
   ElectroBall(80, "Electro Ball"),
   FoulPlay(81, "Foul Play"),
   StoredPower(82, "Stored Power"),
   AllySwitch(83, "Ally Switch"),
   Scald(84, "Scald"),
   WorkUp(85, "Work Up"),
   WildCharge(86, "Wild Charge"),
   DrillRun(87, "Drill Run"),
   HeatCrash(88, "Heat Crash"),
   Hurricane(89, "Hurricane"),
   PlayRough(90, "Play Rough"),
   VenomDrench(91, "Venom Drench"),
   DazzlingGleam(92, "Dazzling Gleam"),
   DarkestLariat(93, "Darkest Lariat"),
   HighHorsepower(94, "High Horsepower"),
   ThroatChop(95, "Throat Chop"),
   PollenPuff(96, "Pollen Puff"),
   PsychicFangs(97, "Psychic Fangs"),
   Liquidation(98, "Liquidation"),
   BodyPress(99, "Body Press");

   private final int id;
   private final String attackName;
   private static final Gen8TechnicalRecords[] VALUES = values();
   protected static final Map idMap = new Int2ObjectArrayMap(VALUES.length);
   protected static final Map nameMap = new HashMap(VALUES.length);
   protected static final Map typeMap = new HashMap(EnumType.values().length);

   private Gen8TechnicalRecords(int id, String attackName) {
      this.id = id;
      this.attackName = attackName;
   }

   public String prefix() {
      return "tr_gen8";
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

   public static ITechnicalMove getTr(int id) {
      return (ITechnicalMove)idMap.get(id);
   }

   public static ITechnicalMove getTr(String attackName) {
      return (ITechnicalMove)nameMap.get(attackName);
   }

   static {
      Gen8TechnicalRecords[] var0 = VALUES;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Gen8TechnicalRecords tr = var0[var2];
         idMap.put(tr.id, tr);
         nameMap.put(tr.attackName, tr);
      }

   }
}

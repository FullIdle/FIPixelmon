package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import com.pixelmonmod.pixelmon.enums.items.EnumSymbols;
import com.pixelmonmod.pixelmon.items.ItemBadge;
import com.pixelmonmod.pixelmon.items.ItemSymbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.Item;

public class PixelmonItemsBadges {
   public static Item balanceBadge;
   public static Item basicBadge;
   public static Item beaconBadge;
   public static Item boulderBadge;
   public static Item cascadeBadge;
   public static Item coalBadge;
   public static Item cobbleBadge;
   public static Item dynamoBadge;
   public static Item earthBadge;
   public static Item featherBadge;
   public static Item fenBadge;
   public static Item fogBadge;
   public static Item forestBadge;
   public static Item glacierBadge;
   public static Item heatBadge;
   public static Item hiveBadge;
   public static Item icicleBadge;
   public static Item knuckleBadge;
   public static Item marshBadge;
   public static Item mindBadge;
   public static Item mineBadge;
   public static Item mineralBadge;
   public static Item plainBadge;
   public static Item rainBadge;
   public static Item rainbowBadge;
   public static Item relicBadge;
   public static Item risingBadge;
   public static Item soulBadge;
   public static Item stoneBadge;
   public static Item stormBadge;
   public static Item thunderBadge;
   public static Item volcanoBadge;
   public static Item zephyrBadge;
   public static Item boltBadge;
   public static Item freezeBadge;
   public static Item insectBadge;
   public static Item jetBadge;
   public static Item legendBadge;
   public static Item quakeBadge;
   public static Item toxicBadge;
   public static Item trioBadge;
   public static Item waveBadge;
   public static Item bugBadge;
   public static Item cliffBadge;
   public static Item rumbleBadge;
   public static Item plantBadge;
   public static Item voltageBadge;
   public static Item fairyBadge;
   public static Item psychicBadge;
   public static Item icebergBadge;
   public static Item goldKnowledgeSymbol;
   public static Item goldGutsSymbol;
   public static Item goldTacticsSymbol;
   public static Item goldLuckSymbol;
   public static Item goldSpiritsSymbol;
   public static Item goldBraveSymbol;
   public static Item goldAbilitySymbol;
   public static Item silverKnowledgeSymbol;
   public static Item silverGutsSymbol;
   public static Item silverTacticsSymbol;
   public static Item silverLuckSymbol;
   public static Item silverSpiritsSymbol;
   public static Item silverBraveSymbol;
   public static Item silverAbilitySymbol;
   private static HashMap badgeList;

   public static void load() {
      balanceBadge = new ItemBadge(EnumBadges.BalanceBadge);
      beaconBadge = new ItemBadge(EnumBadges.BeaconBadge);
      boulderBadge = new ItemBadge(EnumBadges.BoulderBadge);
      cascadeBadge = new ItemBadge(EnumBadges.CascadeBadge);
      coalBadge = new ItemBadge(EnumBadges.CoalBadge);
      cobbleBadge = new ItemBadge(EnumBadges.CobbleBadge);
      dynamoBadge = new ItemBadge(EnumBadges.DynamoBadge);
      earthBadge = new ItemBadge(EnumBadges.EarthBadge);
      featherBadge = new ItemBadge(EnumBadges.FeatherBadge);
      fenBadge = new ItemBadge(EnumBadges.FenBadge);
      fogBadge = new ItemBadge(EnumBadges.FogBadge);
      forestBadge = new ItemBadge(EnumBadges.ForestBadge);
      glacierBadge = new ItemBadge(EnumBadges.GlacierBadge);
      heatBadge = new ItemBadge(EnumBadges.HeatBadge);
      hiveBadge = new ItemBadge(EnumBadges.HiveBadge);
      icicleBadge = new ItemBadge(EnumBadges.IcicleBadge);
      knuckleBadge = new ItemBadge(EnumBadges.KnuckleBadge);
      marshBadge = new ItemBadge(EnumBadges.MarshBadge);
      mindBadge = new ItemBadge(EnumBadges.MindBadge);
      mineBadge = new ItemBadge(EnumBadges.MineBadge);
      mineralBadge = new ItemBadge(EnumBadges.MineralBadge);
      plainBadge = new ItemBadge(EnumBadges.PlainBadge);
      rainbowBadge = new ItemBadge(EnumBadges.RainbowBadge);
      rainBadge = new ItemBadge(EnumBadges.RainBadge);
      relicBadge = new ItemBadge(EnumBadges.RelicBadge);
      risingBadge = new ItemBadge(EnumBadges.RisingBadge);
      soulBadge = new ItemBadge(EnumBadges.SoulBadge);
      stoneBadge = new ItemBadge(EnumBadges.StoneBadge);
      stormBadge = new ItemBadge(EnumBadges.StormBadge);
      thunderBadge = new ItemBadge(EnumBadges.ThunderBadge);
      volcanoBadge = new ItemBadge(EnumBadges.VolcanoBadge);
      zephyrBadge = new ItemBadge(EnumBadges.ZephyrBadge);
      basicBadge = new ItemBadge(EnumBadges.BasicBadge);
      boltBadge = new ItemBadge(EnumBadges.BoltBadge);
      freezeBadge = new ItemBadge(EnumBadges.FreezeBadge);
      insectBadge = new ItemBadge(EnumBadges.InsectBadge);
      jetBadge = new ItemBadge(EnumBadges.JetBadge);
      legendBadge = new ItemBadge(EnumBadges.LegendBadge);
      quakeBadge = new ItemBadge(EnumBadges.QuakeBadge);
      toxicBadge = new ItemBadge(EnumBadges.ToxicBadge);
      trioBadge = new ItemBadge(EnumBadges.TrioBadge);
      waveBadge = new ItemBadge(EnumBadges.WaveBadge);
      bugBadge = new ItemBadge(EnumBadges.BugBadge);
      cliffBadge = new ItemBadge(EnumBadges.CliffBadge);
      rumbleBadge = new ItemBadge(EnumBadges.RumbleBadge);
      plantBadge = new ItemBadge(EnumBadges.PlantBadge);
      voltageBadge = new ItemBadge(EnumBadges.VoltageBadge);
      fairyBadge = new ItemBadge(EnumBadges.FairyBadge);
      psychicBadge = new ItemBadge(EnumBadges.PsychicBadge);
      icebergBadge = new ItemBadge(EnumBadges.IcebergBadge);
      goldKnowledgeSymbol = new ItemSymbol(EnumSymbols.Gold_Knowledge);
      goldGutsSymbol = new ItemSymbol(EnumSymbols.Gold_Guts);
      goldTacticsSymbol = new ItemSymbol(EnumSymbols.Gold_Tactics);
      goldLuckSymbol = new ItemSymbol(EnumSymbols.Gold_Luck);
      goldSpiritsSymbol = new ItemSymbol(EnumSymbols.Gold_Spirits);
      goldBraveSymbol = new ItemSymbol(EnumSymbols.Gold_Brave);
      goldAbilitySymbol = new ItemSymbol(EnumSymbols.Gold_Ability);
      silverKnowledgeSymbol = new ItemSymbol(EnumSymbols.Silver_Knowledge);
      silverGutsSymbol = new ItemSymbol(EnumSymbols.Silver_Guts);
      silverTacticsSymbol = new ItemSymbol(EnumSymbols.Silver_Tactics);
      silverLuckSymbol = new ItemSymbol(EnumSymbols.Silver_Luck);
      silverSpiritsSymbol = new ItemSymbol(EnumSymbols.Silver_Spirits);
      silverBraveSymbol = new ItemSymbol(EnumSymbols.Silver_Brave);
      silverAbilitySymbol = new ItemSymbol(EnumSymbols.Silver_Ability);
   }

   public static List getBadgeList(EnumType type) {
      if (badgeList == null) {
         badgeList = new HashMap();
         addToTypeList(boulderBadge, EnumType.Rock);
         addToTypeList(cascadeBadge, EnumType.Water);
         addToTypeList(thunderBadge, EnumType.Electric);
         addToTypeList(rainbowBadge, EnumType.Grass);
         addToTypeList(soulBadge, EnumType.Poison);
         addToTypeList(marshBadge, EnumType.Psychic);
         addToTypeList(volcanoBadge, EnumType.Fire);
         addToTypeList(earthBadge, EnumType.Ground);
         addToTypeList(zephyrBadge, EnumType.Flying);
         addToTypeList(hiveBadge, EnumType.Bug);
         addToTypeList(plainBadge, EnumType.Normal);
         addToTypeList(fogBadge, EnumType.Ghost);
         addToTypeList(stormBadge, EnumType.Fighting);
         addToTypeList(mineralBadge, EnumType.Steel);
         addToTypeList(glacierBadge, EnumType.Ice);
         addToTypeList(risingBadge, EnumType.Dragon);
         addToTypeList(stoneBadge, EnumType.Rock);
         addToTypeList(knuckleBadge, EnumType.Fighting);
         addToTypeList(dynamoBadge, EnumType.Electric);
         addToTypeList(heatBadge, EnumType.Fire);
         addToTypeList(balanceBadge, EnumType.Normal);
         addToTypeList(featherBadge, EnumType.Flying);
         addToTypeList(mindBadge, EnumType.Psychic);
         addToTypeList(rainBadge, EnumType.Water);
         addToTypeList(coalBadge, EnumType.Rock);
         addToTypeList(forestBadge, EnumType.Grass);
         addToTypeList(cobbleBadge, EnumType.Fighting);
         addToTypeList(fenBadge, EnumType.Water);
         addToTypeList(relicBadge, EnumType.Ghost);
         addToTypeList(mineBadge, EnumType.Steel);
         addToTypeList(icicleBadge, EnumType.Ice);
         addToTypeList(beaconBadge, EnumType.Electric);
         addToTypeList(trioBadge, EnumType.Normal);
         addToTypeList(basicBadge, EnumType.Normal);
         addToTypeList(insectBadge, EnumType.Bug);
         addToTypeList(boltBadge, EnumType.Electric);
         addToTypeList(quakeBadge, EnumType.Ground);
         addToTypeList(jetBadge, EnumType.Flying);
         addToTypeList(freezeBadge, EnumType.Ice);
         addToTypeList(legendBadge, EnumType.Dragon);
         addToTypeList(toxicBadge, EnumType.Poison);
         addToTypeList(waveBadge, EnumType.Water);
         addToTypeList(bugBadge, EnumType.Bug);
         addToTypeList(cliffBadge, EnumType.Rock);
         addToTypeList(rumbleBadge, EnumType.Fighting);
         addToTypeList(plantBadge, EnumType.Grass);
         addToTypeList(voltageBadge, EnumType.Electric);
         addToTypeList(fairyBadge, EnumType.Fairy);
         addToTypeList(psychicBadge, EnumType.Psychic);
         addToTypeList(icebergBadge, EnumType.Ice);
      }

      return (List)badgeList.get(type);
   }

   public static List getBadgeList(EnumType[] types) {
      List totalList = new ArrayList();
      EnumType[] var2 = types;
      int var3 = types.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumType type = var2[var4];
         List badgeList = getBadgeList(type);
         totalList.addAll(badgeList);
      }

      return totalList;
   }

   public static ItemBadge getRandomBadge(EnumType... types) {
      List badgeList = getBadgeList(types);
      return (ItemBadge)RandomHelper.getRandomElementFromList(badgeList);
   }

   private static void addToTypeList(Item badge, EnumType type) {
      List list = null;
      if (badgeList.containsKey(type)) {
         list = (List)badgeList.get(type);
      } else {
         list = new ArrayList();
      }

      ((List)list).add((ItemBadge)badge);
      badgeList.put(type, list);
   }
}

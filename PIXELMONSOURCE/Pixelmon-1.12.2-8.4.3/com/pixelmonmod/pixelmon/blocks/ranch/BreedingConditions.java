package com.pixelmonmod.pixelmon.blocks.ranch;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BreedingConditions {
   static EnumMap typeBlockList = new EnumMap(EnumType.class);
   ArrayList blockList;

   public BreedingConditions(ArrayList blockList) {
      if (typeBlockList.isEmpty()) {
         this.fillBlockList();
      }

      this.blockList = blockList;
   }

   private void fillBlockList() {
      HashMap weightedBugList = new HashMap(11);
      weightedBugList.put(Blocks.field_150349_c, 1);
      weightedBugList.put(Blocks.field_150364_r, 1);
      weightedBugList.put(Blocks.field_150363_s, 1);
      weightedBugList.put(Blocks.field_150362_t, 1);
      weightedBugList.put(Blocks.field_150361_u, 1);
      weightedBugList.put(Blocks.field_150328_O, 2);
      weightedBugList.put(Blocks.field_150327_N, 2);
      weightedBugList.put(Blocks.field_150420_aW, 2);
      weightedBugList.put(Blocks.field_150419_aX, 3);
      weightedBugList.put(Blocks.field_150457_bL, 3);
      weightedBugList.put(Blocks.field_180399_cE, 3);
      typeBlockList.put(EnumType.Bug, weightedBugList);
      HashMap weightedDarkList = new HashMap(7);
      weightedDarkList.put(Blocks.field_150354_m, 1);
      weightedDarkList.put(Blocks.field_150425_aM, 2);
      weightedDarkList.put(Blocks.field_150321_G, 2);
      weightedDarkList.put(Blocks.field_150343_Z, 2);
      weightedDarkList.put(Blocks.field_150385_bj, 2);
      weightedDarkList.put(Blocks.field_150402_ci, 3);
      weightedDarkList.put(Blocks.field_150465_bP, 4);
      typeBlockList.put(EnumType.Dark, weightedDarkList);
      HashMap weightedDragonList = new HashMap(8);
      weightedDragonList.put(Blocks.field_150348_b, 1);
      weightedDragonList.put(Blocks.field_150426_aN, 2);
      weightedDragonList.put(Blocks.field_150340_R, 2);
      weightedDragonList.put(Blocks.field_150377_bs, 3);
      weightedDragonList.put(Blocks.field_150484_ah, 3);
      weightedDragonList.put(Blocks.field_150475_bE, 3);
      weightedDragonList.put(Blocks.field_150381_bn, 3);
      weightedDragonList.put(Blocks.field_150380_bt, 4);
      typeBlockList.put(EnumType.Dragon, weightedDragonList);
      HashMap weightedElectricList = new HashMap(7);
      weightedElectricList.put(Blocks.field_150379_bu, 1);
      weightedElectricList.put(Blocks.field_150448_aq, 1);
      weightedElectricList.put(Blocks.field_150451_bX, 2);
      weightedElectricList.put(Blocks.field_150408_cc, 2);
      weightedElectricList.put(Blocks.field_150439_ay, 2);
      weightedElectricList.put(Blocks.field_150374_bv, 3);
      weightedElectricList.put(PixelmonBlocks.pc, 3);
      typeBlockList.put(EnumType.Electric, weightedElectricList);
      HashMap weightedFightingList = new HashMap(8);
      weightedFightingList.put(Blocks.field_150351_n, 1);
      weightedFightingList.put(Blocks.field_150467_bQ, 2);
      weightedFightingList.put(PixelmonBlocks.anvil, 2);
      weightedFightingList.put(Blocks.field_150336_V, 2);
      weightedFightingList.put(Blocks.field_150389_bf, 2);
      weightedFightingList.put(PixelmonBlocks.templeBrick, 2);
      weightedFightingList.put(PixelmonBlocks.templeBrickStairs, 2);
      weightedFightingList.put(Blocks.field_150417_aV, 3);
      typeBlockList.put(EnumType.Fighting, weightedFightingList);
      HashMap weightedFireList = new HashMap(8);
      weightedFireList.put(Blocks.field_150348_b, 1);
      weightedFireList.put(Blocks.field_150364_r, 1);
      weightedFireList.put(Blocks.field_150363_s, 1);
      weightedFireList.put(Blocks.field_150424_aL, 2);
      weightedFireList.put(Blocks.field_150353_l, 2);
      weightedFireList.put(Blocks.field_150478_aa, 2);
      weightedFireList.put(Blocks.field_150356_k, 2);
      weightedFireList.put(Blocks.field_150480_ab, 3);
      typeBlockList.put(EnumType.Fire, weightedFireList);
      HashMap weightedFlyingList = new HashMap(9);
      weightedFlyingList.put(Blocks.field_150349_c, 1);
      weightedFlyingList.put(Blocks.field_150421_aI, 2);
      weightedFlyingList.put(Blocks.field_150323_B, 2);
      weightedFlyingList.put(Blocks.field_150359_w, 2);
      weightedFlyingList.put(Blocks.field_150399_cn, 2);
      weightedFlyingList.put(Blocks.field_150410_aZ, 2);
      weightedFlyingList.put(Blocks.field_150397_co, 2);
      weightedFlyingList.put(Blocks.field_150367_z, 3);
      weightedFlyingList.put(Blocks.field_150461_bJ, 3);
      typeBlockList.put(EnumType.Flying, weightedFlyingList);
      HashMap weightedGhostList = new HashMap(10);
      weightedGhostList.put(Blocks.field_150341_Y, 1);
      weightedGhostList.put(Blocks.field_150321_G, 2);
      weightedGhostList.put(Blocks.field_150391_bh, 2);
      weightedGhostList.put(Blocks.field_150324_C, 2);
      weightedGhostList.put(Blocks.field_150404_cg, 3);
      weightedGhostList.put(Blocks.field_150475_bE, 3);
      weightedGhostList.put(Blocks.field_150342_X, 3);
      weightedGhostList.put(Blocks.field_150421_aI, 3);
      weightedGhostList.put(Blocks.field_150411_aY, 3);
      weightedGhostList.put(Blocks.field_150465_bP, 3);
      typeBlockList.put(EnumType.Ghost, weightedGhostList);
      HashMap weightedGrassList = new HashMap(11);
      weightedGrassList.put(Blocks.field_150349_c, 1);
      weightedGrassList.put(Blocks.field_150329_H, 1);
      weightedGrassList.put(Blocks.field_150393_bb, 2);
      weightedGrassList.put(Blocks.field_150394_bc, 2);
      weightedGrassList.put(PixelmonBlocks.mossyRock, 2);
      weightedGrassList.put(Blocks.field_150464_aj, 3);
      weightedGrassList.put(Blocks.field_150459_bM, 3);
      weightedGrassList.put(Blocks.field_150440_ba, 3);
      weightedGrassList.put(Blocks.field_150423_aK, 3);
      weightedGrassList.put(Blocks.field_150428_aP, 3);
      weightedGrassList.put(Blocks.field_150407_cf, 3);
      typeBlockList.put(EnumType.Grass, weightedGrassList);
      HashMap weightedGroundList = new HashMap(9);
      weightedGroundList.put(Blocks.field_150354_m, 1);
      weightedGroundList.put(Blocks.field_150425_aM, 1);
      weightedGroundList.put(Blocks.field_150434_aF, 2);
      weightedGroundList.put(Blocks.field_150322_A, 2);
      weightedGroundList.put(Blocks.field_150435_aG, 2);
      weightedGroundList.put(Blocks.field_150405_ch, 2);
      weightedGroundList.put(Blocks.field_150406_ce, 3);
      weightedGroundList.put(Blocks.field_150357_h, 4);
      weightedGroundList.put(Blocks.field_180395_cM, 2);
      typeBlockList.put(EnumType.Ground, weightedGroundList);
      HashMap weightedIceList = new HashMap(6);
      weightedIceList.put(Blocks.field_150431_aC, 1);
      weightedIceList.put(Blocks.field_150432_aD, 2);
      weightedIceList.put(Blocks.field_150433_aE, 2);
      weightedIceList.put(PixelmonBlocks.icyRock, 2);
      weightedIceList.put(Blocks.field_150403_cj, 3);
      weightedIceList.put(Blocks.field_180397_cI, 3);
      typeBlockList.put(EnumType.Ice, weightedIceList);
      HashMap weightedNormalList = new HashMap(12);
      weightedNormalList.put(Blocks.field_150462_ai, 1);
      weightedNormalList.put(Blocks.field_150346_d, 1);
      weightedNormalList.put(Blocks.field_150349_c, 1);
      weightedNormalList.put(Blocks.field_150348_b, 1);
      weightedNormalList.put(Blocks.field_150325_L, 2);
      weightedNormalList.put(Blocks.field_180407_aO, 2);
      weightedNormalList.put(Blocks.field_180408_aP, 2);
      weightedNormalList.put(Blocks.field_180404_aQ, 2);
      weightedNormalList.put(Blocks.field_180403_aR, 2);
      weightedNormalList.put(Blocks.field_180406_aS, 2);
      weightedNormalList.put(Blocks.field_180405_aT, 2);
      weightedNormalList.put(Blocks.field_150414_aQ, 3);
      typeBlockList.put(EnumType.Normal, weightedNormalList);
      HashMap weightedPoisonList = new HashMap(9);
      weightedPoisonList.put(Blocks.field_150349_c, 1);
      weightedPoisonList.put(Blocks.field_150338_P, 1);
      weightedPoisonList.put(Blocks.field_150360_v, 2);
      weightedPoisonList.put(Blocks.field_150337_Q, 2);
      weightedPoisonList.put(Blocks.field_150321_G, 2);
      weightedPoisonList.put(Blocks.field_150391_bh, 2);
      weightedPoisonList.put(Blocks.field_150420_aW, 2);
      weightedPoisonList.put(Blocks.field_150419_aX, 3);
      weightedPoisonList.put(Blocks.field_150382_bo, 3);
      typeBlockList.put(EnumType.Poison, weightedPoisonList);
      HashMap weightedPsychicList = new HashMap(7);
      weightedPsychicList.put(Blocks.field_150404_cg, 1);
      weightedPsychicList.put(Blocks.field_150472_an, 2);
      weightedPsychicList.put(Blocks.field_150342_X, 2);
      weightedPsychicList.put(Blocks.field_150371_ca, 3);
      weightedPsychicList.put(Blocks.field_150475_bE, 3);
      weightedPsychicList.put(Blocks.field_150484_ah, 3);
      weightedPsychicList.put(Blocks.field_150381_bn, 3);
      typeBlockList.put(EnumType.Psychic, weightedPsychicList);
      HashMap weightedRockList = new HashMap(8);
      weightedRockList.put(Blocks.field_150348_b, 1);
      weightedRockList.put(Blocks.field_150347_e, 1);
      weightedRockList.put(PixelmonBlocks.fossil, 2);
      weightedRockList.put(Blocks.field_150460_al, 2);
      weightedRockList.put(Blocks.field_150478_aa, 2);
      weightedRockList.put(Blocks.field_150357_h, 3);
      weightedRockList.put(Blocks.field_150402_ci, 3);
      weightedRockList.put(Blocks.field_150377_bs, 3);
      typeBlockList.put(EnumType.Rock, weightedRockList);
      HashMap weightedSteelList = new HashMap(10);
      weightedSteelList.put(Blocks.field_150348_b, 1);
      weightedSteelList.put(Blocks.field_150448_aq, 1);
      weightedSteelList.put(Blocks.field_180384_M, 1);
      weightedSteelList.put(Blocks.field_150411_aY, 2);
      weightedSteelList.put(Blocks.field_150454_av, 2);
      weightedSteelList.put(Blocks.field_150319_E, 2);
      weightedSteelList.put(Blocks.field_150331_J, 2);
      weightedSteelList.put(Blocks.field_150332_K, 2);
      weightedSteelList.put(Blocks.field_150339_S, 3);
      weightedSteelList.put(Blocks.field_150340_R, 3);
      typeBlockList.put(EnumType.Steel, weightedSteelList);
      HashMap weightedWaterList = new HashMap(7);
      weightedWaterList.put(Blocks.field_150355_j, 1);
      weightedWaterList.put(Blocks.field_150436_aH, 2);
      weightedWaterList.put(Blocks.field_150358_i, 1);
      weightedWaterList.put(Blocks.field_150360_v, 2);
      weightedWaterList.put(PixelmonBlocks.waterStoneOre, 2);
      weightedWaterList.put(Blocks.field_150392_bi, 3);
      weightedWaterList.put(Blocks.field_180398_cJ, 3);
      typeBlockList.put(EnumType.Water, weightedWaterList);
      HashMap weightedFairyList = new HashMap(4);
      weightedFairyList.put(Blocks.field_150404_cg, 1);
      weightedFairyList.put(Blocks.field_150426_aN, 2);
      weightedFairyList.put(Blocks.field_150325_L, 2);
      weightedFairyList.put(Blocks.field_150414_aQ, 3);
      typeBlockList.put(EnumType.Fairy, weightedFairyList);
   }

   public float getBreedingStrength(ArrayList types) {
      int sum = 0;
      int nonNullSize = 0;
      if (types != null && !types.isEmpty()) {
         Iterator var4 = types.iterator();

         while(true) {
            while(true) {
               EnumType type;
               do {
                  if (!var4.hasNext()) {
                     sum /= nonNullSize;
                     if (sum < 35) {
                        return 0.0F;
                     }

                     if (sum < 70) {
                        return 0.5F;
                     }

                     if (sum < 105) {
                        return 1.0F;
                     }

                     if (sum < 140) {
                        return 1.5F;
                     }

                     return 2.0F;
                  }

                  type = (EnumType)var4.next();
               } while(type == null);

               ++nonNullSize;
               HashMap weightedList = (HashMap)typeBlockList.get(type);
               if (weightedList == null) {
                  sum = (int)((float)sum + 80.0F);
               } else {
                  Iterator var7 = this.blockList.iterator();

                  while(var7.hasNext()) {
                     Block b = (Block)var7.next();
                     if (weightedList.containsKey(b)) {
                        sum += (Integer)weightedList.get(b);
                     }
                  }
               }
            }
         }
      } else {
         return 0.0F;
      }
   }
}

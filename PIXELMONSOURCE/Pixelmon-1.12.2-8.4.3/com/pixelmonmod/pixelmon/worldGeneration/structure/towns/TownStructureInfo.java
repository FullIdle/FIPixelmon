package com.pixelmonmod.pixelmon.worldGeneration.structure.towns;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.worldGeneration.loot.PixelmonLootTableList;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureInfo;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TownStructureInfo extends StructureInfo {
   int weighting = 0;
   int maxNum = 0;
   HashMap count = new HashMap();

   public void setWeighting(int weighting) {
      this.weighting = weighting;
   }

   public int getWeight() {
      return this.weighting;
   }

   public void setMaxNum(int maxNum) {
      this.maxNum = maxNum;
   }

   public boolean canPlaceMore(List pieces) {
      return this.getCount(pieces) < this.maxNum;
   }

   private int getCount(List pieces) {
      int count = 0;
      Iterator it = pieces.iterator();

      while(it.hasNext()) {
         Object obj = it.next();
         if (obj instanceof ComponentTownPart && this == ((ComponentTownPart)obj).info) {
            ++count;
         }
      }

      return count;
   }

   public void populateChest(World world, BlockPos pos) {
      TileEntity te = world.func_175625_s(pos);
      if (te instanceof TileEntityChest) {
         TileEntityChest chest = (TileEntityChest)te;
         if (this.id.contains("towncenter")) {
            chest.func_189404_a(PixelmonLootTableList.POKEMON_CENTER, RandomHelper.rand.nextLong());
         } else if (this.id.contains("boat_magikarp")) {
            chest.func_189404_a(PixelmonLootTableList.BOAT_MAGIKARP, RandomHelper.rand.nextLong());
         } else if (this.id.contains("boat_quartz")) {
            chest.func_189404_a(PixelmonLootTableList.BOAT_QUARTZ, RandomHelper.rand.nextLong());
         } else if (this.id.contains("boat_wood")) {
            chest.func_189404_a(PixelmonLootTableList.BOAT_WOOD, RandomHelper.rand.nextLong());
         } else if (this.id.contains("watertower")) {
            chest.func_189404_a(PixelmonLootTableList.TOWER_OF_WATERS, RandomHelper.rand.nextLong());
         } else if (this.id.contains("darknesstower")) {
            chest.func_189404_a(PixelmonLootTableList.TOWER_OF_DARKNESS, RandomHelper.rand.nextLong());
         }
      }

   }
}

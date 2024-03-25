package com.pixelmonmod.pixelmon.worldGeneration.structure;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.worldGeneration.loot.PixelmonLootTableList;
import com.pixelmonmod.pixelmon.worldGeneration.structure.towns.NPCPlacementInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.GeneralScattered;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.StructureScattered;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.StructureSnapshot;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class StructureInfo {
   public String id;
   protected int depth = 0;
   protected ArrayList npcs = new ArrayList();
   protected StructureSnapshot snapshot;

   public void addNPC(NPCPlacementInfo info) {
      this.npcs.add(info);
   }

   public void setDepth(int depth) {
      this.depth = depth;
   }

   public void setSnapshot(NBTTagCompound nbt) {
      this.snapshot = StructureSnapshot.readFromNBT(nbt);
   }

   public void setId(String id) {
      this.id = id;
   }

   public int getDepth() {
      return this.depth;
   }

   public StructureScattered createStructure(Random random, BlockPos pos, boolean doRotation, boolean forceGeneration, EnumFacing facing, boolean recreateTechnicalBlocks) {
      return new GeneralScattered(random, pos, this.snapshot, this, doRotation, forceGeneration, facing, recreateTechnicalBlocks);
   }

   public ArrayList getNPCS() {
      return this.npcs;
   }

   public void populateChest(World world, BlockPos pos) {
      TileEntity te = world.func_175625_s(pos);
      if (te != null && te instanceof TileEntityChest) {
         TileEntityChest chest = (TileEntityChest)te;
         if (this.id.contains("boat_magikarp")) {
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

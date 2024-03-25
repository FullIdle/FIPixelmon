package com.pixelmonmod.pixelmon.api.events.raids;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.battles.raids.RaidGovernor;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RaidDropsEvent extends Event {
   private final RaidData raid;
   private final RaidData.RaidPlayer player;
   private final RaidGovernor governor;
   private Pokemon possibleCatch;
   private ArrayList drops;
   private boolean canCatch;

   public RaidDropsEvent(RaidData raid, RaidData.RaidPlayer player, RaidGovernor governor, Pokemon possibleCatch, ArrayList drops, boolean canCatch) {
      this.raid = raid;
      this.player = player;
      this.governor = governor;
      this.possibleCatch = possibleCatch;
      this.drops = drops;
      this.canCatch = canCatch;
   }

   public RaidData getRaid() {
      return this.raid;
   }

   public RaidGovernor getGovernor() {
      return this.governor;
   }

   public RaidData.RaidPlayer getPlayer() {
      return this.player;
   }

   public Pokemon getPossibleCatch() {
      return this.possibleCatch;
   }

   public ArrayList getDrops() {
      return this.drops;
   }

   public void setDrops(ArrayList drops) {
      this.drops = drops;
   }

   public void addDrop(ItemStack stack) {
      this.drops.add(stack);
   }

   public void clearDrops() {
      this.drops.clear();
   }

   public void setPossibleCatch(Pokemon possibleCatch) {
      this.possibleCatch = possibleCatch;
   }

   public boolean canCatch() {
      return this.canCatch;
   }

   public void setCanCatch(boolean canCatch) {
      this.canCatch = canCatch;
   }
}

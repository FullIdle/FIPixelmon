package com.pixelmonmod.pixelmon.api.events;

import com.google.common.collect.ImmutableList;
import com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops.ItemDropMode;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DroppedItem;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class DropEvent extends Event {
   public final EntityPlayerMP player;
   public final EntityCreature entity;
   public final ItemDropMode dropMode;
   private final ArrayList drops;
   private int id;

   public DropEvent(EntityPlayerMP player, EntityCreature entity, ItemDropMode dropMode, ArrayList items) {
      this.player = player;
      this.entity = entity;
      this.dropMode = dropMode;
      this.drops = items;
      Iterator var5 = items.iterator();

      while(var5.hasNext()) {
         DroppedItem item = (DroppedItem)var5.next();
         if (this.id < item.id) {
            this.id = item.id;
         }
      }

   }

   public ImmutableList getDrops() {
      return ImmutableList.builder().addAll(this.drops).build();
   }

   public void addDrop(ItemStack drop) {
      if (drop != null) {
         this.drops.add(new DroppedItem(drop, ++this.id));
      }

   }

   public void removeDrop(DroppedItem drop) {
      boolean droppingID = false;

      for(int i = 0; i < this.drops.size(); ++i) {
         if (droppingID) {
            --((DroppedItem)this.drops.get(i)).id;
         } else if (this.drops.get(i) == drop) {
            this.drops.remove(i);
            --i;
            droppingID = true;
         }
      }

   }

   public boolean isPokemon() {
      return this.entity instanceof EntityPixelmon;
   }

   public boolean isTrainer() {
      return this.entity instanceof NPCTrainer;
   }
}

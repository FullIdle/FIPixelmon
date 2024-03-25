package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.ItemExpCandy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RareCandyEvent extends Event {
   /** @deprecated */
   @Deprecated
   public EntityPixelmon pixelmon;
   /** @deprecated */
   @Deprecated
   public EntityPlayerMP player;
   private ItemStack usedItem;
   private ItemExpCandy usedCandy;

   public RareCandyEvent(EntityPlayerMP player, EntityPixelmon pixelmon, ItemStack usedItem, ItemExpCandy usedCandy) {
      this.player = player;
      this.pixelmon = pixelmon;
      this.usedItem = usedItem;
      this.usedCandy = usedCandy;
   }

   public EntityPixelmon getPixelmon() {
      return this.pixelmon;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public ItemExpCandy getUsedCandy() {
      return this.usedCandy;
   }

   public ItemStack getUsedItem() {
      return this.usedItem;
   }
}

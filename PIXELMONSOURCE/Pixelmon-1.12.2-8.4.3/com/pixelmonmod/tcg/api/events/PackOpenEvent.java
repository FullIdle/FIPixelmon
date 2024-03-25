package com.pixelmonmod.tcg.api.events;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class PackOpenEvent extends Event {
   private final EntityPlayerMP player;
   private final ItemStack packItem;

   public PackOpenEvent(EntityPlayerMP player, ItemStack packItem) {
      this.player = player;
      this.packItem = packItem;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public ItemStack getPackItem() {
      return this.packItem;
   }

   public static class Post extends PackOpenEvent {
      private final List cards;

      public Post(EntityPlayerMP player, ItemStack packItem, List cards) {
         super(player, packItem);
         this.cards = cards;
      }

      public List getCards() {
         return this.cards;
      }
   }

   @Cancelable
   public static class Pre extends PackOpenEvent {
      public Pre(EntityPlayerMP player, ItemStack packItem) {
         super(player, packItem);
      }
   }
}

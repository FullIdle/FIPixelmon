package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.registry.EnumBuySell;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public abstract class ShopkeeperEvent extends Event {
   protected final EntityPlayerMP player;
   protected final EntityNPC npc;

   protected ShopkeeperEvent(EntityPlayerMP player, EntityNPC npc) {
      this.player = player;
      this.npc = npc;
   }

   public EntityPlayerMP getEntityPlayer() {
      return this.player;
   }

   public EntityNPC getNpc() {
      return this.npc;
   }

   @Cancelable
   public static class Sell extends ShopkeeperEvent {
      private final EnumBuySell type;
      private final ItemStack item;

      public Sell(EntityPlayerMP player, EntityNPC npc, EnumBuySell type, ItemStack item) {
         super(player, npc);
         this.type = type;
         this.item = item;
      }

      /** @deprecated */
      @Deprecated
      public EntityPlayerMP getPlayer() {
         return this.player;
      }

      public EnumBuySell getType() {
         return this.type;
      }

      public ItemStack getItem() {
         return this.item;
      }
   }

   @Cancelable
   public static class Purchase extends ShopkeeperEvent {
      private ItemStack item;
      private final EnumBuySell type;

      public Purchase(EntityPlayerMP player, EntityNPC npc, ItemStack item, EnumBuySell type) {
         super(player, npc);
         this.type = type;
         this.item = item;
      }

      public void setItem(ItemStack item) {
         this.item = item;
      }

      public ItemStack getItem() {
         return this.item;
      }

      /** @deprecated */
      @Deprecated
      public EntityPlayerMP getPlayer() {
         return this.player;
      }

      public EnumBuySell getType() {
         return this.type;
      }
   }
}

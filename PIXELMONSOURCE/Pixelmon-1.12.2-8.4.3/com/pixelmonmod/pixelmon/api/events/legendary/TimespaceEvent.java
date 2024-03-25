package com.pixelmonmod.pixelmon.api.events.legendary;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTimespaceAltar;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.heldItems.ItemTimespaceOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class TimespaceEvent extends Event {
   private final EntityPlayer player;

   TimespaceEvent(EntityPlayer player) {
      this.player = player;
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }

   public abstract static class Summon extends TimespaceEvent {
      private final TileEntityTimespaceAltar altar;

      public Summon(EntityPlayer player, TileEntityTimespaceAltar altar) {
         super(player);
         this.altar = altar;
      }

      public TileEntityTimespaceAltar getAltar() {
         return this.altar;
      }

      public static class Post extends Summon {
         private final EntityPixelmon pixelmon;

         public Post(EntityPlayer player, TileEntityTimespaceAltar altar, EntityPixelmon pixelmon) {
            super(player, altar);
            this.pixelmon = pixelmon;
         }

         public EntityPixelmon getPixelmon() {
            return this.pixelmon;
         }
      }

      public static class Pre extends Summon {
         public Pre(EntityPlayer player, TileEntityTimespaceAltar altar) {
            super(player, altar);
         }
      }
   }

   @Cancelable
   public static class TakeOrb extends TimespaceEvent {
      private final TileEntityTimespaceAltar altar;
      private final ItemStack stack;

      public TakeOrb(EntityPlayer player, TileEntityTimespaceAltar altar, ItemStack stack) {
         super(player);
         this.altar = altar;
         this.stack = stack;
      }

      public ItemTimespaceOrb getOrb() {
         return (ItemTimespaceOrb)this.stack.func_77973_b();
      }

      public TileEntityTimespaceAltar getAltar() {
         return this.altar;
      }

      public ItemStack getStack() {
         return this.stack;
      }
   }

   @Cancelable
   public static class PlaceOrb extends TimespaceEvent {
      private final TileEntityTimespaceAltar altar;
      private final ItemStack stack;

      public PlaceOrb(EntityPlayer player, TileEntityTimespaceAltar altar, ItemStack stack) {
         super(player);
         this.altar = altar;
         this.stack = stack;
      }

      public ItemTimespaceOrb getOrb() {
         return (ItemTimespaceOrb)this.stack.func_77973_b();
      }

      public TileEntityTimespaceAltar getAltar() {
         return this.altar;
      }

      public ItemStack getStack() {
         return this.stack;
      }
   }

   @Cancelable
   public static class TakeChain extends TimespaceEvent {
      private final TileEntityTimespaceAltar altar;
      private final ItemStack stack;

      public TakeChain(EntityPlayer player, TileEntityTimespaceAltar altar, ItemStack stack) {
         super(player);
         this.altar = altar;
         this.stack = stack;
      }

      public TileEntityTimespaceAltar getAltar() {
         return this.altar;
      }

      public ItemStack getStack() {
         return this.stack;
      }
   }

   @Cancelable
   public static class PlaceChain extends TimespaceEvent {
      private final TileEntityTimespaceAltar altar;
      private final ItemStack stack;

      public PlaceChain(EntityPlayer player, TileEntityTimespaceAltar altar, ItemStack stack) {
         super(player);
         this.altar = altar;
         this.stack = stack;
      }

      public TileEntityTimespaceAltar getAltar() {
         return this.altar;
      }

      public ItemStack getStack() {
         return this.stack;
      }
   }
}

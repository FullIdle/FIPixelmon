package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityMechanicalAnvil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class MechanicalAnvilEvent extends Event {
   public final TileEntityMechanicalAnvil anvil;

   private MechanicalAnvilEvent(TileEntityMechanicalAnvil anvil) {
      this.anvil = anvil;
   }

   // $FF: synthetic method
   MechanicalAnvilEvent(TileEntityMechanicalAnvil x0, Object x1) {
      this(x0);
   }

   public static class HammerResult extends MechanicalAnvilEvent {
      public ItemStack result;

      public HammerResult(TileEntityMechanicalAnvil anvil, ItemStack result) {
         super(anvil, null);
         this.result = result;
      }
   }

   @Cancelable
   public static class Hammer extends MechanicalAnvilEvent {
      public final ItemStack result;

      public Hammer(TileEntityMechanicalAnvil anvil, ItemStack result) {
         super(anvil, null);
         this.result = result;
      }
   }

   public abstract static class Tick extends MechanicalAnvilEvent {
      public Tick(TileEntityMechanicalAnvil anvil) {
         super(anvil, null);
      }

      public static class Post extends Tick {
         public Post(TileEntityMechanicalAnvil anvil) {
            super(anvil);
         }
      }

      @Cancelable
      public static class Pre extends Tick {
         public Pre(TileEntityMechanicalAnvil anvil) {
            super(anvil);
         }
      }
   }

   public static class SetStack extends MechanicalAnvilEvent {
      private final ItemStack item;
      private final int slotIndex;

      public SetStack(TileEntityMechanicalAnvil anvil, ItemStack itemstack, int slotIndex) {
         super(anvil, null);
         this.item = itemstack;
         this.slotIndex = slotIndex;
      }

      public ItemStack getItem() {
         return this.item;
      }

      public int getSlotIndex() {
         return this.slotIndex;
      }
   }

   public static class RemoveStack extends MechanicalAnvilEvent {
      private final ItemStack item;
      private final int slotIndex;

      public RemoveStack(TileEntityMechanicalAnvil anvil, ItemStack itemstack, int slotIndex) {
         super(anvil, null);
         this.item = itemstack;
         this.slotIndex = slotIndex;
      }

      public ItemStack getItem() {
         return this.item;
      }

      public int getSlotIndex() {
         return this.slotIndex;
      }
   }
}

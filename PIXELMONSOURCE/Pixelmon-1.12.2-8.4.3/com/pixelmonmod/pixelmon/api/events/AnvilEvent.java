package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityAnvil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class AnvilEvent extends Event {
   public final EntityPlayerMP player;
   public final TileEntityAnvil anvil;

   private AnvilEvent(EntityPlayerMP player, TileEntityAnvil anvil) {
      this.player = player;
      this.anvil = anvil;
   }

   // $FF: synthetic method
   AnvilEvent(EntityPlayerMP x0, TileEntityAnvil x1, Object x2) {
      this(x0, x1);
   }

   public static class FinishedSmith extends AnvilEvent {
      private final ItemStack item;

      public FinishedSmith(EntityPlayerMP player, TileEntityAnvil anvil, ItemStack itemStack) {
         super(player, anvil, null);
         this.item = itemStack;
      }

      public ItemStack getItem() {
         return this.item;
      }
   }

   @Cancelable
   public static class MaterialChanged extends AnvilEvent {
      public final ItemStack material;
      public final boolean isCollecting;

      public MaterialChanged(EntityPlayerMP player, TileEntityAnvil anvil, ItemStack material, boolean isCollecting) {
         super(player, anvil, null);
         this.material = material;
         this.isCollecting = isCollecting;
      }
   }

   @Cancelable
   public static class HammerDamage extends AnvilEvent {
      public final ItemStack hammer;
      public int damage;

      public HammerDamage(EntityPlayerMP player, TileEntityAnvil anvil, ItemStack hammer, int damage) {
         super(player, anvil, null);
         this.hammer = hammer;
         this.damage = damage;
      }
   }

   @Cancelable
   public static class BeatAnvil extends AnvilEvent {
      private final ItemStack item;
      private int neededHits;
      private int force;

      public BeatAnvil(EntityPlayerMP player, TileEntityAnvil anvil, ItemStack itemstack, int neededHits, int force) {
         super(player, anvil, null);
         this.item = itemstack;
         this.neededHits = neededHits;
         this.force = force;
      }

      public ItemStack getItem() {
         return this.item;
      }

      public int getNeededHits() {
         return this.neededHits;
      }

      public void setNeededHits(int neededHits) {
         if (neededHits < 0) {
            neededHits = 0;
         }

         this.neededHits = neededHits;
      }

      public int getForce() {
         return this.force;
      }

      public void setForce(int force) {
         if (force < 0) {
            force = 0;
         }

         this.force = force;
      }
   }
}

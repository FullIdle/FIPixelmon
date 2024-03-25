package com.pixelmonmod.pixelmon.api.events.screens;

import com.pixelmonmod.pixelmon.items.ItemBadgeCase;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class BadgeCaseEvent extends Event {
   private final EntityPlayerMP player;
   private final boolean wasCustom;

   public BadgeCaseEvent(EntityPlayerMP player, boolean custom) {
      this.player = player;
      this.wasCustom = custom;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public boolean isCustom() {
      return this.wasCustom;
   }

   @Cancelable
   public static class Swap extends BadgeCaseEvent {
      private final int index1;
      private final int index2;

      public Swap(EntityPlayerMP player, boolean custom, int index1, int index2) {
         super(player, custom);
         this.index1 = index1;
         this.index2 = index2;
      }

      public int getIndex1() {
         return this.index1;
      }

      public int getIndex2() {
         return this.index2;
      }
   }

   @Cancelable
   public static class Remove extends BadgeCaseEvent {
      private final int index;

      public Remove(EntityPlayerMP player, boolean custom, int index) {
         super(player, custom);
         this.index = index;
      }

      public int getIndex() {
         return this.index;
      }

      @Nullable
      public ItemStack getStack() {
         if (this.isCustom() && this.getPlayer().func_184614_ca().func_77973_b() instanceof ItemBadgeCase) {
            ItemStack hand = this.getPlayer().func_184614_ca();
            ItemBadgeCase.BadgeCase badgeCase = ItemBadgeCase.BadgeCase.readFromItemStack(hand);
            if (badgeCase != null && this.index >= 0 && this.index < badgeCase.badges.size()) {
               ItemStack removed = (ItemStack)badgeCase.badges.get(this.index);
               return removed != null ? removed : ItemStack.field_190927_a;
            }
         }

         return null;
      }
   }

   @Cancelable
   public static class Register extends BadgeCaseEvent {
      public Register(EntityPlayerMP player, boolean custom) {
         super(player, custom);
      }
   }
}

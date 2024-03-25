package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBerryTree;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class BerryEvent extends Event {
   public final EnumBerry berry;
   public final BlockPos pos;

   protected BerryEvent(EnumBerry berry, BlockPos pos) {
      this.berry = berry;
      this.pos = pos;
   }

   @Cancelable
   public static class PickBerry extends BerryEvent {
      public final EntityPlayerMP player;
      public final TileEntityBerryTree tree;
      private ItemStack pickedStack;

      public PickBerry(EnumBerry berry, BlockPos pos, EntityPlayerMP player, TileEntityBerryTree tree, ItemStack pickedStack) {
         super(berry, pos);
         this.player = player;
         this.tree = tree;
         this.pickedStack = pickedStack;
      }

      public ItemStack getPickedStack() {
         return this.pickedStack;
      }

      public void setPickedStack(ItemStack pickedStack) {
         if (pickedStack != null) {
            this.pickedStack = pickedStack;
         }

      }
   }

   @Cancelable
   public static class BerryWatered extends BerryEvent {
      public final EntityPlayer player;

      public BerryWatered(BlockPos pos, EntityPlayer player, EnumBerry type) {
         super(type, pos);
         this.player = player;
      }
   }

   public static class BerryReady extends BerryEvent {
      public final TileEntityBerryTree tree;

      public BerryReady(EnumBerry berry, BlockPos pos, TileEntityBerryTree tree) {
         super(berry, pos);
         this.tree = tree;
      }
   }

   @Cancelable
   public static class BerryPlanted extends BerryEvent {
      public final EntityPlayerMP player;

      public BerryPlanted(EnumBerry berry, BlockPos pos, EntityPlayerMP player) {
         super(berry, pos);
         this.player = player;
      }
   }
}

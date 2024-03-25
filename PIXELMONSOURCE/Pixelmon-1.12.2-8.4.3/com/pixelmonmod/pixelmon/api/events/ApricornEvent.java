package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import com.pixelmonmod.pixelmon.enums.items.EnumApricorns;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class ApricornEvent extends Event {
   public final EnumApricorns apricorn;
   public final BlockPos pos;

   protected ApricornEvent(EnumApricorns apricorn, BlockPos pos) {
      this.apricorn = apricorn;
      this.pos = pos;
   }

   @Cancelable
   public static class PickApricorn extends ApricornEvent {
      public final EntityPlayerMP player;
      public final TileEntityApricornTree tree;
      private ItemStack pickedStack;

      public PickApricorn(EnumApricorns apricorn, BlockPos pos, EntityPlayerMP player, TileEntityApricornTree tree, ItemStack pickedStack) {
         super(apricorn, pos);
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

   public static class ApricornReady extends ApricornEvent {
      public final TileEntityApricornTree tree;

      public ApricornReady(EnumApricorns apricorn, BlockPos pos, TileEntityApricornTree tree) {
         super(apricorn, pos);
         this.tree = tree;
      }
   }

   @Cancelable
   public static class GrowthChance extends ApricornEvent {
      public final TileEntityApricornTree tree;
      private float growthChance;

      public GrowthChance(EnumApricorns apricorn, BlockPos pos, TileEntityApricornTree tree, float growthChance) {
         super(apricorn, pos);
         this.tree = tree;
         this.growthChance = growthChance;
      }

      public float getGrowthChance() {
         return this.growthChance;
      }

      public void setGrowthChance(float growthChance) {
         growthChance = Math.max(0.0F, Math.min(1.0F, growthChance));
      }
   }

   @Cancelable
   public static class ApricornWatered extends ApricornEvent {
      public final EntityPlayer player;
      public final TileEntityApricornTree tree;

      public ApricornWatered(EnumApricorns apricorn, BlockPos pos, EntityPlayer player, TileEntityApricornTree tree) {
         super(apricorn, pos);
         this.player = player;
         this.tree = tree;
      }
   }

   @Cancelable
   public static class ApricornPlanted extends ApricornEvent {
      public final EntityPlayerMP player;

      public ApricornPlanted(EnumApricorns apricorn, BlockPos pos, EntityPlayerMP player) {
         super(apricorn, pos);
         this.player = player;
      }
   }
}

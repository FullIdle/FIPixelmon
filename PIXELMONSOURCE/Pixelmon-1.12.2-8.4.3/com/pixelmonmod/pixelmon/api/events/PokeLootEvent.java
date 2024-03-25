package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class PokeLootEvent extends Event {
   public TileEntityPokeChest chest;
   public EntityPlayerMP player;

   public PokeLootEvent(EntityPlayerMP player, TileEntityPokeChest tile) {
      this.player = player;
      this.chest = tile;
   }

   @Cancelable
   public static class Drop extends PokeLootEvent {
      private ItemStack drop;

      public Drop(EntityPlayerMP player, TileEntityPokeChest tile, ItemStack drop) {
         super(player, tile);
         this.drop = drop;
      }

      public ItemStack getDrop() {
         return this.drop;
      }

      public void setDrop(ItemStack drop) {
         this.drop = drop;
      }
   }

   public static class GetDrops extends PokeLootEvent {
      private ItemStack[] drops;

      public GetDrops(EntityPlayerMP player, TileEntityPokeChest tile, ItemStack[] drops) {
         super(player, tile);
         this.drops = drops;
      }

      public ItemStack[] getDrops() {
         return this.drops;
      }

      public void setDrops(ItemStack... drops) {
         this.drops = drops;
      }
   }

   @Cancelable
   public static class Claim extends PokeLootEvent {
      public Claim(EntityPlayerMP player, TileEntityPokeChest tile) {
         super(player, tile);
      }
   }
}

package com.pixelmonmod.pixelmon.api.events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class FossilCleanerEvent extends Event {
   private final EntityPlayerMP player;
   private final TileEntity tileEntity;
   private Item fossil;

   protected FossilCleanerEvent(EntityPlayerMP player, TileEntity tileEntity, Item fossil) {
      this.player = player;
      this.tileEntity = tileEntity;
      this.fossil = fossil;
   }

   public Item getFossil() {
      return this.fossil;
   }

   public void setFossil(Item fossil) {
      this.fossil = fossil;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public TileEntity getTileEntity() {
      return this.tileEntity;
   }

   @Cancelable
   public static class ObtainingCleanFossil extends FossilCleanerEvent {
      public ObtainingCleanFossil(EntityPlayerMP player, TileEntity tileEntity, Item fossil) {
         super(player, tileEntity, fossil);
      }
   }

   @Cancelable
   public static class PutFossil extends FossilCleanerEvent {
      public PutFossil(EntityPlayerMP player, TileEntity tileEntity, Item fossil) {
         super(player, tileEntity, fossil);
      }
   }
}

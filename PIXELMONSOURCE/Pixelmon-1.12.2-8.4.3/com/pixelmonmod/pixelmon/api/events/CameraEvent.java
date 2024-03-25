package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class CameraEvent extends Event {
   public final EntityPlayerMP player;
   public final EntityPixelmon pixelmon;

   public CameraEvent(EntityPlayerMP player, EntityPixelmon pixelmon) {
      this.player = player;
      this.pixelmon = pixelmon;
   }

   @Cancelable
   public static class TakePhoto extends CameraEvent {
      public ItemStack photo;

      public TakePhoto(EntityPlayerMP player, EntityPixelmon pixelmon, ItemStack photo) {
         super(player, pixelmon);
         this.photo = photo;
      }
   }

   @Cancelable
   public static class ConsumeFilm extends CameraEvent {
      public ConsumeFilm(EntityPlayerMP player, EntityPixelmon pixelmon) {
         super(player, pixelmon);
      }
   }

   @Cancelable
   public static class DuplicatePhoto extends CameraEvent {
      public DuplicatePhoto(EntityPlayerMP player, EntityPixelmon pixelmon) {
         super(player, pixelmon);
      }
   }
}

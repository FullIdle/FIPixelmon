package com.pixelmonmod.pixelmon.api.events.drops;

import com.pixelmonmod.pixelmon.api.enums.EnumPositionTriState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CustomDropsEvent extends Event {
   private final EntityPlayerMP player;

   public CustomDropsEvent(EntityPlayerMP player) {
      this.player = player;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public static class ClickDrop extends CustomDropsEvent {
      private final int index;

      public ClickDrop(EntityPlayerMP player, int index) {
         super(player);
         this.index = index;
      }

      public int getIndex() {
         return this.index;
      }
   }

   public static class ClickButton extends CustomDropsEvent {
      private final EnumPositionTriState position;

      public ClickButton(EntityPlayerMP player, EnumPositionTriState position) {
         super(player);
         this.position = position;
      }

      public EnumPositionTriState getPosition() {
         return this.position;
      }
   }
}

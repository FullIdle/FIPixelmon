package com.pixelmonmod.pixelmon.api.events.dialogue;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DialogueInputEvent extends Event {
   private final EntityPlayerMP player;

   public DialogueInputEvent(EntityPlayerMP player) {
      this.player = player;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public static class Submitted extends DialogueInputEvent {
      private final String input;

      public Submitted(EntityPlayerMP player, String input) {
         super(player);
         this.input = input;
      }

      public String getInput() {
         return this.input;
      }
   }

   public static class ClosedScreen extends DialogueInputEvent {
      public ClosedScreen(EntityPlayerMP player) {
         super(player);
      }
   }
}

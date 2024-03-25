package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class NPCChatEvent extends Event {
   public final EntityNPC npc;
   public final EntityPlayer player;
   private ArrayList chatlines;

   public NPCChatEvent(EntityNPC npc, EntityPlayer player, ArrayList chatlines) {
      this.npc = npc;
      this.player = player;
      this.chatlines = chatlines;
   }

   public ArrayList getChat() {
      return this.chatlines;
   }

   public void setChat(ArrayList chatlines) {
      if (!chatlines.isEmpty()) {
         this.chatlines = chatlines;
      }
   }
}

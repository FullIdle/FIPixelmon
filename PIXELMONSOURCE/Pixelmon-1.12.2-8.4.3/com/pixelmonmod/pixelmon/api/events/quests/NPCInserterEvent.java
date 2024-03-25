package com.pixelmonmod.pixelmon.api.events.quests;

import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class NPCInserterEvent extends Event {
   public final EntityPlayerMP player;
   public final QuestProgress progress;
   public final NPCQuestGiver npc;

   public NPCInserterEvent(EntityPlayerMP player, QuestProgress progress, NPCQuestGiver npc) {
      this.player = player;
      this.progress = progress;
      this.npc = npc;
   }
}

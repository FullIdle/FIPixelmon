package com.pixelmonmod.pixelmon.api.events.quests;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.actions.Action;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class QuestActionEvent extends Event {
   public final EntityPlayerMP player;
   public final QuestProgress progress;
   public final Stage stage;
   public final Action action;

   public QuestActionEvent(EntityPlayerMP player, QuestProgress progress, Stage stage, Action action) {
      this.player = player;
      this.progress = progress;
      this.stage = stage;
      this.action = action;
   }
}

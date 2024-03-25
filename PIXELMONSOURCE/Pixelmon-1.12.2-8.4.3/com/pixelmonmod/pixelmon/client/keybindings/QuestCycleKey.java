package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.quests.client.QuestDataClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class QuestCycleKey extends KeyBinding {
   public QuestCycleKey() {
      super("key.questcycle", 50, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         QuestDataClient.getInstance().cycleDisplayQuest();
      }

   }
}

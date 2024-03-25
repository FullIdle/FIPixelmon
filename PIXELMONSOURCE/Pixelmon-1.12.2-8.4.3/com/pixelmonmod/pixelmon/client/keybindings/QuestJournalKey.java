package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.quests.client.ui.GuiQuests;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class QuestJournalKey extends KeyBinding {
   public QuestJournalKey() {
      super("key.questjournal", 49, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         Minecraft.func_71410_x().func_147108_a(new GuiQuests());
      }

   }
}

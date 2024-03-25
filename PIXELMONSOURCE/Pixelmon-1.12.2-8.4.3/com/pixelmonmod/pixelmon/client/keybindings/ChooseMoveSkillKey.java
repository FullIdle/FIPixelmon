package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.moveskills.GuiMoveSkillSelect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ChooseMoveSkillKey extends KeyBinding {
   public ChooseMoveSkillKey() {
      super("key.nextexternalmove", 48, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         if (Minecraft.func_71410_x().field_71441_e == null) {
            return;
         }

         Minecraft.func_71410_x().func_147108_a(new GuiMoveSkillSelect(GuiPixelmonOverlay.selectedPixelmon));
      }

   }
}

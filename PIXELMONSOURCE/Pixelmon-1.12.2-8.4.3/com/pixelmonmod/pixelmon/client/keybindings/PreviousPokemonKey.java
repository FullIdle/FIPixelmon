package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class PreviousPokemonKey extends KeyBinding {
   public PreviousPokemonKey() {
      super("key.previouspokemon", 200, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         if (Minecraft.func_71410_x().field_71441_e == null) {
            return;
         }

         GuiPixelmonOverlay.selectPreviousPixelmon();
      }

   }
}

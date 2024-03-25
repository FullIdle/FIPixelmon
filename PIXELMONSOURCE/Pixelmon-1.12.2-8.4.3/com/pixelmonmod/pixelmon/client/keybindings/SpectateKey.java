package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.RequestSpectate;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class SpectateKey extends KeyBinding {
   public SpectateKey() {
      super("key.spectateBattle", 21, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         if (Minecraft.func_71410_x().field_71441_e == null) {
            return;
         }

         UUID uuid = GuiPixelmonOverlay.getCurrentSpectatingUUID();
         if (uuid != null) {
            RequestSpectate message = new RequestSpectate(uuid);
            Pixelmon.network.sendToServer(message);
         }
      }

   }
}

package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.KeyPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class SendPokemonKey extends KeyBinding {
   public SendPokemonKey() {
      super("key.sendpokemon", 19, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71462_r != null || mc.field_71441_e == null) {
            return;
         }

         Pokemon pokemon = ClientStorageManager.party.get(GuiPixelmonOverlay.selectedPixelmon);
         if (pokemon != null && !pokemon.isEgg()) {
            PixelmonModelRegistry.getModel(pokemon.getSpecies(), pokemon.getFormEnum());
            Pixelmon.network.sendToServer(new KeyPacket(GuiPixelmonOverlay.selectedPixelmon));
         }
      }

   }
}

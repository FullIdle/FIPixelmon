package com.pixelmonmod.pixelmon.client.gui.pc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.GuiWarning;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ServerTrash;
import net.minecraft.client.resources.I18n;

public class GuiReleaseWarning extends GuiWarning {
   GuiReleaseWarning(GuiPC previousScreen) {
      super(previousScreen);
   }

   protected void confirmAction() {
      if (((GuiPC)this.previousScreen).selected != null && ((GuiPC)this.previousScreen).selectedPokemon != null) {
         Pixelmon.network.sendToServer(new ServerTrash(((GuiPC)this.previousScreen).selectedPokemon.getPosition(), ((GuiPC)this.previousScreen).selectedPokemon));
         ((GuiPC)this.previousScreen).getStorage(((GuiPC)this.previousScreen).selected).set(((GuiPC)this.previousScreen).selected, (Pokemon)null);
         ((GuiPC)this.previousScreen).updateSelected((StoragePosition)null);
      }

   }

   protected void drawWarningText() {
      if (((GuiPC)this.previousScreen).selectedPokemon != null) {
         this.drawCenteredSplitText(I18n.func_135052_a("gui.pc.releasewarning", new Object[]{((GuiPC)this.previousScreen).selectedPokemon.getDisplayName()}));
      } else {
         this.field_146297_k.field_71439_g.func_71053_j();
      }

   }
}

package com.pixelmonmod.pixelmon.client.gui.ranchblock;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.pc.GuiPC;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.EnumRanchServerPacketMode;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ranch.RanchBlockServerPacket;
import java.io.IOException;

public class GuiPCRanch extends GuiPC {
   private TileEntityRanchBlock ranch;

   public GuiPCRanch(TileEntityRanchBlock ranch) {
      this.ranch = ranch;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.footerLeft = this.field_146294_l / 2 - 30;
      this.footerRight = this.footerLeft + 60;
      this.footerTop = this.field_146295_m / 6 + 157;
      this.footerBottom = this.footerTop + 28;
      this.renameIcon.setEnabled(false);
      this.wallpaperIcon.setEnabled(false);
   }

   protected int getFooterNumWidth() {
      return this.ranch.getPokemonData().size();
   }

   protected StoragePosition getFooterPosAt(int x, int y) {
      if (x >= this.footerLeft && x < this.footerRight && y >= this.footerTop + 3 && y <= this.footerBottom + 3) {
         double xInd = (double)(x - this.footerLeft) / 30.0;
         Pokemon pokemon = this.getFooterAt((int)Math.floor(xInd));
         if (pokemon == null) {
            return null;
         } else {
            return search != null ? null : pokemon.getPosition();
         }
      } else {
         return null;
      }
   }

   protected Pokemon getFooterAt(int index) {
      index = Math.min(index, this.ranch.getPokemonData().size());
      return index < this.ranch.getPokemonData().size() ? ((TileEntityRanchBlock.RanchPoke)this.ranch.getPokemonData().get(index)).getPokemon(ClientStorageManager.openPC) : null;
   }

   protected void updateSelected(StoragePosition position) {
      if (position != null) {
         Pokemon pokemon = this.getPokemon(position);
         if (pokemon == null) {
            return;
         }

         if (pokemon.isInRanch()) {
            if (this.ranch.getPokemonData().removeIf((ranchPoke) -> {
               return ranchPoke.matches(pokemon);
            })) {
               Pixelmon.network.sendToServer(new RanchBlockServerPacket(this.ranch.func_174877_v(), pokemon.getUUID(), pokemon.getPosition(), EnumRanchServerPacketMode.RemovePokemon));
            }
         } else if (position.box != -1 && !pokemon.isEgg() && this.ranch.getPokemonData().size() < 2) {
            this.ranch.getPokemonData().add(new TileEntityRanchBlock.RanchPoke(pokemon.getUUID(), pokemon.getPosition()));
            Pixelmon.network.sendToServer(new RanchBlockServerPacket(this.ranch.func_174877_v(), pokemon.getUUID(), pokemon.getPosition(), EnumRanchServerPacketMode.AddPokemon));
         }
      }

   }

   protected boolean tryToSwap(PokemonStorage from, StoragePosition fromPosition, PokemonStorage to, StoragePosition toPosition) {
      return false;
   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      if (this.searchField.func_146176_q() || keyCode == 1 || keyCode == ClientProxy.pcSearchKeyBind.func_151463_i() || this.field_146297_k.field_71474_y.field_151445_Q.isActiveAndMatches(keyCode)) {
         super.func_73869_a(typedChar, keyCode);
      }

   }

   protected void drawFooterBackground() {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pcResources);
      this.func_73729_b(this.field_146294_l / 2 - 31, this.field_146295_m / 6 + 160, 183, 0, 62, 29);
   }
}

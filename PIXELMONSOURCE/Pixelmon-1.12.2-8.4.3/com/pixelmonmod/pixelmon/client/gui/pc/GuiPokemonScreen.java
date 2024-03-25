package com.pixelmonmod.pixelmon.client.gui.pc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.IGuiHideMouse;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiButtonPokeChecker;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ServerSwap;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public abstract class GuiPokemonScreen extends GuiScreen implements IGuiHideMouse {
   protected static final int SLOT_WIDTH = 30;
   protected static final int SLOT_HEIGHT = 28;
   protected static final int PC_NUM_WIDTH = 6;
   protected static final int PC_NUM_HEIGHT = 5;
   protected int pcLeft;
   protected int pcRight;
   protected int pcTop;
   protected int pcBottom;
   protected PCStorage storage;
   protected int boxNumber;
   protected StoragePosition selected;
   protected Pokemon selectedPokemon = null;
   protected int footerLeft;
   protected int footerRight;
   protected int footerTop;
   protected int footerBottom;
   private GuiButtonPokeChecker pokeChecker = new GuiButtonPokeChecker(this);

   public GuiPokemonScreen(@Nullable StoragePosition selected) {
      this.updateStorage(ClientStorageManager.openPC, selected);
   }

   protected void updateSelected(StoragePosition selected) {
      this.selected = selected;
      this.selectedPokemon = this.getPokemon(selected);
      if (this.selectedPokemon != null && this.selectedPokemon.isInRanch()) {
         this.selected = null;
         this.selectedPokemon = null;
      }

   }

   protected void updateStorage(PCStorage storage, @Nullable StoragePosition selected) {
      this.storage = storage;
      this.updateSelected(selected);
      this.boxNumber = this.selected == null ? storage.getLastBox() : this.selected.box;
   }

   protected PokemonStorage getStorage(StoragePosition position) {
      if (position == null) {
         return null;
      } else {
         return (PokemonStorage)(position.box == -1 ? ClientStorageManager.party : this.storage);
      }
   }

   protected Pokemon getPokemon(StoragePosition position) {
      if (position == null) {
         return null;
      } else {
         return position.box == -1 ? ClientStorageManager.party.get(position) : this.storage.get(position);
      }
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.pcLeft = this.field_146294_l / 2 - 90;
      this.pcRight = this.pcLeft + 180;
      this.pcTop = this.field_146295_m / 6 - 5;
      this.pcBottom = this.pcTop + 140;
      this.footerLeft = this.field_146294_l / 2 - 90;
      this.footerRight = this.footerLeft + this.getFooterNumWidth() * 30;
      this.footerTop = this.field_146295_m / 6 + 157;
      this.footerBottom = this.footerTop + 28;
   }

   protected int getFooterNumWidth() {
      return 6;
   }

   public Pokemon getSlotAt(int x, int y) {
      Pokemon pokemon = this.getPCAt(x, y);
      return pokemon == null ? this.getFooterAt(x, y) : pokemon;
   }

   public StoragePosition getPosAt(int x, int y) {
      StoragePosition pos = this.getPCPosAt(x, y);
      return pos == null ? this.getFooterPosAt(x, y) : pos;
   }

   protected StoragePosition getFooterPosAt(int x, int y) {
      if (x >= this.footerLeft && x < this.footerRight && y >= this.footerTop + 3 && y <= this.footerBottom + 3) {
         double xInd = ((double)x - (double)this.footerLeft) / 30.0;
         return new StoragePosition(-1, Math.min((int)Math.floor(xInd), 5));
      } else {
         return null;
      }
   }

   protected StoragePosition getPCPosAt(int x, int y) {
      if (x >= this.pcLeft && x < this.pcRight && y >= this.pcTop + 5 && y <= this.pcBottom + 5) {
         double xInd = ((double)x - (double)this.pcLeft) / 30.0;
         double yInd = ((double)y - ((double)this.pcTop + 5.0)) / 28.0;
         int ind = (int)Math.floor(yInd) * 6 + (int)Math.floor(xInd);
         return new StoragePosition(this.boxNumber, ind > 29 ? 29 : ind);
      } else {
         return null;
      }
   }

   protected Pokemon getFooterAt(int x, int y) {
      if (x >= this.footerLeft && x < this.footerRight && y >= this.footerTop + 3 && y <= this.footerBottom + 3) {
         double xInd = ((double)x - (double)this.footerLeft) / 30.0;
         return this.getFooterAt((int)Math.floor(xInd));
      } else {
         return null;
      }
   }

   protected Pokemon getFooterAt(int index) {
      return ClientStorageManager.party.get(index);
   }

   protected Pokemon getPCAt(int x, int y) {
      if (x >= this.pcLeft && x < this.pcRight && y >= this.pcTop + 5 && y <= this.pcBottom + 5) {
         double xInd = ((double)x - (double)this.pcLeft) / 30.0;
         double yInd = ((double)y - ((double)this.pcTop + 5.0)) / 28.0;
         int ind = (int)Math.floor(yInd) * 6 + (int)Math.floor(xInd);
         return ind > 29 ? null : this.storage.get(this.boxNumber, ind);
      } else {
         return null;
      }
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      StoragePosition position = this.getPosAt(mouseX, mouseY);
      Pokemon pokemon = this.getPokemon(position);
      if (this.pokeChecker.func_146115_a()) {
         if (mouseButton == 0) {
            this.pokeChecker.mouseClicked(mouseX, mouseY);
         }

      } else {
         if (this.selected == null) {
            if (mouseButton == 1) {
               this.pokeChecker.setPokemon(this.getStorage(position), position, pokemon, mouseX, mouseY);
            } else {
               this.pokeChecker.setPokemon((PokemonStorage)null, (StoragePosition)null, (Pokemon)null, mouseX, mouseY);
            }
         }

         if (mouseButton == 0 && position != null) {
            if (pokemon != null && position.equals(this.selected)) {
               this.updateSelected((StoragePosition)null);
            } else if (pokemon != null && this.selected == null) {
               if (func_146272_n()) {
                  if (position.box != -1 || pokemon.isEgg() || ClientStorageManager.party.countPokemon() > 1) {
                     PokemonStorage destination = position.box == -1 ? ClientStorageManager.openPC : ClientStorageManager.party;
                     StoragePosition slot = ((PokemonStorage)(position.box == -1 ? this.storage.getBox(this.boxNumber) : ClientStorageManager.party)).getFirstEmptyPosition();
                     if (slot != null) {
                        this.tryToSwap(this.getStorage(position), position, (PokemonStorage)destination, slot);
                     }
                  }
               } else {
                  this.updateSelected(position);
               }
            } else if (this.selected != null && this.selectedPokemon == null) {
               this.updateSelected(this.selected);
               if (this.selectedPokemon == null) {
                  this.updateSelected((StoragePosition)null);
               }
            } else if (this.selected != null) {
               if (this.selected.box == -1 && position.box != -1 || this.selected.box != -1 && position.box == -1) {
                  boolean selectedIsEgg = this.selectedPokemon.isEgg();
                  boolean targetIsEggOrBlank = this.getPokemon(position) == null || this.getPokemon(position).isEgg();
                  if (selectedIsEgg != targetIsEggOrBlank && (selectedIsEgg && position.box == -1 || targetIsEggOrBlank && this.selected.box == -1) && ClientStorageManager.party.countPokemon() <= 1) {
                     return;
                  }
               }

               if (this.tryToSwap(this.getStorage(this.selected), this.selected, this.getStorage(position), position)) {
                  this.updateSelected((StoragePosition)null);
               }
            }
         }

      }
   }

   protected boolean tryToSwap(PokemonStorage from, StoragePosition fromPosition, PokemonStorage to, StoragePosition toPosition) {
      Pokemon fromPokemon = from.get(fromPosition);
      Pokemon toPokemon = to.get(toPosition);
      if ((fromPokemon == null || !fromPokemon.isInRanch()) && (toPokemon == null || !toPokemon.isInRanch())) {
         StoragePosition fromOriginalPosition = fromPokemon == null ? fromPosition : fromPokemon.getPosition();
         StoragePosition toOriginalPosition = toPokemon == null ? toPosition : toPokemon.getPosition();
         if (to.transfer(from, fromPosition, toPosition)) {
            Pixelmon.network.sendToServer(new ServerSwap(fromOriginalPosition, fromPokemon, toOriginalPosition, toPokemon));
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      if (this.field_146297_k.field_71474_y.field_151445_Q.isActiveAndMatches(keyCode)) {
         this.field_146297_k.func_147108_a((GuiScreen)null);
         if (this.field_146297_k.field_71462_r == null) {
            this.field_146297_k.func_71381_h();
         }
      } else {
         super.func_73869_a(typedChar, keyCode);
      }

   }

   public void func_146281_b() {
      super.func_146281_b();
      GuiPixelmonOverlay.checkSelection();
   }

   protected void drawBox() {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);

      for(int x = 0; x < 6; ++x) {
         for(int y = 0; y < 5; ++y) {
            int slot = y * 6 + x;
            Pokemon pokemon = this.storage.get(this.boxNumber, slot);
            if (pokemon != null && (this.selected == null || this.selected.box != this.boxNumber || this.selected.order != slot)) {
               GuiHelper.bindPokemonSprite(pokemon, this.field_146297_k);
               int xPos = this.pcLeft + x * 30;
               int yPos = this.pcTop + y * 28;
               GuiHelper.drawImageQuad((double)(xPos + 2), (double)(yPos + 6), 26.0, 26.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
               if (!pokemon.getHeldItem().func_190926_b()) {
                  this.field_146297_k.field_71446_o.func_110577_a(GuiResources.heldItem);
                  GuiHelper.drawImageQuad((double)(xPos + 21), (double)(yPos + 25), 8.0, 8.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
               }

               if (pokemon.isInRanch()) {
                  this.field_146297_k.field_71446_o.func_110577_a(GuiResources.padlock);
                  GuiHelper.drawImageQuad((double)xPos, (double)(yPos + 25), 8.0, 8.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
               }
            }
         }
      }

   }

   protected void drawFooter() {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.drawFooterBackground();

      for(int x = 0; x < this.getFooterNumWidth(); ++x) {
         Pokemon pokemon = this.getFooterAt(x);
         if (pokemon != null && (this.selected == null || this.selected.box != -1 || this.selected.order != x)) {
            int xPos = this.footerLeft + x * 30;
            int yPos = this.footerTop;
            GuiHelper.bindPokemonSprite(pokemon, this.field_146297_k);
            GuiHelper.drawImageQuad((double)(xPos + 2), (double)(yPos + 3), 26.0, 26.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
            if (!pokemon.getHeldItem().func_190926_b()) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.heldItem);
               GuiHelper.drawImageQuad((double)(xPos + 18), (double)(yPos + 22), 8.0, 8.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            }
         }
      }

   }

   protected void drawFooterBackground() {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pcResources);
      this.func_73729_b(this.field_146294_l / 2 - 91, this.field_146295_m / 6 + 160, 0, 0, 182, 29);
   }

   protected void drawPokeChecker(int mouseX, int mouseY, float partialTicks) {
      this.pokeChecker.func_191745_a(this.field_146297_k, mouseX, mouseY, partialTicks);
   }

   protected void drawHover(int mouseX, int mouseY) {
      if (!this.pokeChecker.func_146115_a()) {
         Pokemon pokemon = this.getSlotAt(mouseX, mouseY);
         if (pokemon != null && !pokemon.equals(this.selectedPokemon)) {
            GuiHelper.drawPokemonHoverInfo(pokemon, mouseX, mouseY);
         }
      }

   }

   protected void drawCursor(int mouseX, int mouseY) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.selectedPokemon == null) {
         this.field_146297_k.func_110434_K().func_110577_a(GuiResources.pcPointer);
         GuiHelper.drawImageQuad((double)mouseX, (double)(mouseY - 19), 19.0, 21.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      } else {
         GuiHelper.bindPokemonSprite(this.selectedPokemon, this.field_146297_k);
         GuiHelper.drawImageQuad((double)(mouseX - 13), (double)(mouseY - 13 - (this.selectedPokemon.getSpecies().getGeneration() == 6 ? -3 : 0)), 26.0, 26.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         if (!this.selectedPokemon.getHeldItem().func_190926_b()) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.heldItem);
            GuiHelper.drawImageQuad((double)(mouseX + 6), (double)(mouseY + 6), 8.0, 8.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         this.field_146297_k.func_110434_K().func_110577_a(GuiResources.pcPointerGrabbed);
         GuiHelper.drawImageQuad((double)mouseX, (double)(mouseY - 18), 17.0, 18.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      }

   }
}

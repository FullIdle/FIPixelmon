package com.pixelmonmod.pixelmon.client.gui.custom.selection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.custom.selection.elements.SelectionPokemonUI;
import com.pixelmonmod.pixelmon.comm.packetHandlers.selection.SelectionResponsePacket;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.input.Keyboard;

public class GuiSelection extends GuiScreen {
   private final ITextComponent title;
   private final ITextComponent text;
   private final int selections;
   private final Pokemon[] options;
   private final Set selected;
   private final boolean allowExit;
   private List pokemonInUI = Lists.newArrayList();
   private int centerW;
   private int centerH;
   private int buttonOffset = 30;
   private boolean closing = false;

   public GuiSelection(ITextComponent title, ITextComponent text, int selections, boolean allowExit) {
      this.title = title;
      this.text = text;
      this.selections = selections;
      this.allowExit = allowExit;
      this.options = new Pokemon[6];
      this.selected = Sets.newHashSet();
      if (this.allowExit) {
         this.closing = true;
      }

   }

   public GuiSelection(ITextComponent title, ITextComponent text, int selections, Pokemon[] options, Set selected, boolean allowExit) {
      this.title = title;
      this.text = text;
      this.selections = selections;
      this.options = options;
      this.selected = selected;
      this.allowExit = allowExit;
      if (this.allowExit) {
         this.closing = true;
      }

   }

   public void setPokemon(int slot, Pokemon pokemon) {
      this.options[slot] = pokemon;
   }

   public void func_73866_w_() {
      this.centerW = this.field_146294_l / 2;
      this.centerH = this.field_146295_m / 2;
      this.pokemonInUI.clear();

      for(int i = 0; i < this.options.length; ++i) {
         SelectionPokemonUI selection = new SelectionPokemonUI(0, 0, this.options[i]);
         selection.setSelected(this.selected.contains(i));
         this.pokemonInUI.add(selection);
      }

   }

   public void func_146281_b() {
      super.func_146281_b();
      if (this.allowExit && this.selections != this.selected.size()) {
         Pixelmon.network.sendToServer(new SelectionResponsePacket((List)null));
      } else {
         Pixelmon.network.sendToServer(new SelectionResponsePacket(Lists.newArrayList(this.selected)));
      }

   }

   public void func_146282_l() throws IOException {
      if (this.closing || Keyboard.getEventKey() != 1) {
         super.func_146282_l();
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GuiHelper.drawDialogueBox(this, (ITextComponent)this.title, (ITextComponent)this.text, 0.0F);

      for(int i = 0; i < this.pokemonInUI.size(); ++i) {
         SelectionPokemonUI pokemon = (SelectionPokemonUI)this.pokemonInUI.get(i);
         boolean highlight = pokemon.isMouseOver(i < 3 ? this.centerW - 110 : this.centerW + 5, this.field_146295_m / 4 + 20 + this.buttonOffset * (i % 3 + 1), mouseX, mouseY) || pokemon.isSelected();
         pokemon.drawPokemon(pokemon.getPokemon(), i < 3 ? this.centerW - 110 : this.centerW + 5, this.field_146295_m / 4 + 20 + this.buttonOffset * (i % 3 + 1), mouseX, mouseY, 0.0F, highlight);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      for(int i = 0; i < this.pokemonInUI.size(); ++i) {
         SelectionPokemonUI pokemonUI = (SelectionPokemonUI)this.pokemonInUI.get(i);
         if (pokemonUI.isMouseOver(i < 3 ? this.centerW - 110 : this.centerW + 5, this.field_146295_m / 4 + 20 + this.buttonOffset * (i % 3 + 1), mouseX, mouseY)) {
            if (pokemonUI.isSelected()) {
               this.selected.remove(i);
               this.func_73866_w_();
               this.closing = this.allowExit;
            } else if (this.selected.size() < this.selections) {
               this.selected.add(i);
               this.func_73866_w_();
               if (!this.allowExit) {
                  this.closing = this.selected.size() == this.selections;
               }
            }
         }
      }

   }
}

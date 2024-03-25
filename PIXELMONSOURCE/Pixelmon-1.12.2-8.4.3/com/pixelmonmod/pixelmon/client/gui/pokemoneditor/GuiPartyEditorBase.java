package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.exceptions.ShowdownImportException;
import com.pixelmonmod.pixelmon.api.pokemon.ImportExportConverter;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class GuiPartyEditorBase extends GuiScreen implements IImportableContainer {
   private static final int BUTTON_OKAY = 1;
   private static final int BUTTON_RANDOM = 10;
   private static final int BUTTON_IMPORT_EXPORT = 11;
   public List pokemonList;
   private List pokemonButtons = new ArrayList();

   protected GuiPartyEditorBase(List pokemonList) {
      this.pokemonList = pokemonList;
   }

   public void func_73866_w_() {
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
      this.field_146292_n.add(new GuiButton(10, this.field_146294_l / 2 - 160, this.field_146295_m / 2 + 90, 100, 20, I18n.func_135052_a("gui.trainereditor.randomise", new Object[0])));
      this.field_146292_n.add(new GuiButton(11, this.field_146294_l / 2 + 20, this.field_146295_m / 2 + 90, 100, 20, I18n.func_135052_a("gui.pokemoneditor.importexport", new Object[0])));
      super.func_73866_w_();

      for(int i = 0; i < 6; ++i) {
         GuiButton pokeButton = new GuiButton(i + 2, this.field_146294_l / 2 + 40, this.field_146295_m / 2 - 50 + i * 20, 80, 20, I18n.func_135052_a("gui.trainereditor.edit", new Object[0]));
         this.field_146292_n.add(pokeButton);
         this.pokemonButtons.add(pokeButton);
      }

   }

   public void func_73863_a(int i, int j, float f) {
      for(int z = 0; z < 6; ++z) {
         GuiButton pokemonButton = (GuiButton)this.pokemonButtons.get(z);
         if (z < this.pokemonList.size() && this.pokemonList.get(z) != null) {
            pokemonButton.field_146126_j = I18n.func_135052_a("gui.trainereditor.edit", new Object[0]);
         } else {
            pokemonButton.field_146126_j = I18n.func_135052_a("gui.trainereditor.add", new Object[0]);
         }
      }

      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 200), (double)(this.field_146295_m / 2 - 120), 400.0, 240.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      String title = this.getTitle();
      this.field_146297_k.field_71466_p.func_78276_b(title, this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(title) / 2, this.field_146295_m / 2 - 90, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.name", new Object[0]), this.field_146294_l / 2 - 130, this.field_146295_m / 2 - 65, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.lvl", new Object[0]), this.field_146294_l / 2 - 40, this.field_146295_m / 2 - 65, 0);

      int k;
      for(k = 0; k < 6; ++k) {
         if (k % 2 == 1) {
            func_73734_a(this.field_146294_l / 2 - 160, this.field_146295_m / 2 - 50 + k * 20, this.field_146294_l / 2 + 40, this.field_146295_m / 2 - 30 + k * 20, -1777215);
         }
      }

      for(k = 0; k < this.pokemonList.size(); ++k) {
         Pokemon p = (Pokemon)this.pokemonList.get(k);
         if (p != null) {
            this.field_146297_k.field_71466_p.func_78276_b(p.getDisplayName(), this.field_146294_l / 2 - 130, this.field_146295_m / 2 - 44 + k * 20, 0);
            this.field_146297_k.field_71466_p.func_78276_b("" + p.getLevel(), this.field_146294_l / 2 - 40, this.field_146295_m / 2 - 44 + k * 20, 0);
            GuiHelper.bindPokemonSprite(p, this.field_146297_k);
            GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 157), (double)(this.field_146295_m / 2 - 53 + k * 20), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, 1.0F);
         }
      }

      super.func_73863_a(i, j, f);
   }

   public abstract String getTitle();

   protected void func_73869_a(char key, int par2) throws IOException {
      if (par2 == 1 || par2 == 28) {
         this.exitScreen();
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         if (button.field_146127_k == 1) {
            this.exitScreen();
         } else if (button.field_146127_k == 10) {
            this.randomizeParty();
         } else if (button.field_146127_k >= 2 && button.field_146127_k < 8) {
            int partySlot = button.field_146127_k - 2;
            if (((GuiButton)this.pokemonButtons.get(partySlot)).field_146126_j.equalsIgnoreCase(I18n.func_135052_a("gui.trainereditor.add", new Object[0]))) {
               this.addPokemon(partySlot);
            } else {
               this.editPokemon(partySlot);
            }
         } else if (button.field_146127_k == 11) {
            this.field_146297_k.func_147108_a(new GuiImportExport(this, this.getTitle()));
         }
      }

   }

   protected abstract void exitScreen();

   protected abstract void randomizeParty();

   protected abstract void addPokemon(int var1);

   protected abstract void editPokemon(int var1);

   public static void editPokemonPacket(int partySlot) {
      Minecraft mc = Minecraft.func_71410_x();
      if (mc.field_71462_r instanceof GuiPartyEditorBase) {
         GuiPartyEditorBase partyEditor = (GuiPartyEditorBase)mc.field_71462_r;
         partyEditor.editPokemon(partySlot);
      } else if (mc.field_71462_r instanceof GuiIndividualEditorBase) {
         GuiIndividualEditorBase individualEditor = (GuiIndividualEditorBase)mc.field_71462_r;
         Pokemon currentPokemon = (Pokemon)individualEditor.getPokemonList().get(partySlot);
         if (currentPokemon != null) {
            individualEditor.p = currentPokemon;
            individualEditor.func_73866_w_();
         }
      }

   }

   public String getExportText() {
      StringBuilder exportText = new StringBuilder();

      for(int i = 0; i < this.pokemonList.size(); ++i) {
         Pokemon data = (Pokemon)this.pokemonList.get(i);
         if (data != null) {
            if (i > 0) {
               exportText.append("\n");
            }

            exportText.append(ImportExportConverter.getExportText(data));
         }
      }

      return exportText.toString();
   }

   public String importText(String importText) {
      importText = importText.trim();
      String[] pokemonSplit = importText.split("\n\n");
      if (pokemonSplit.length >= 1 && pokemonSplit.length <= 6) {
         Pokemon[] importData = new Pokemon[6];

         int i;
         for(i = 0; i < importData.length; ++i) {
            if (i < pokemonSplit.length) {
               try {
                  importData[i] = ImportExportConverter.importText(pokemonSplit[i]);
               } catch (ShowdownImportException var8) {
                  String errorText = var8.field.errorCode;
                  if (errorText != null) {
                     return i + 1 + ", " + errorText;
                  }
               }
            }
         }

         try {
            this.pokemonList.clear();
         } catch (UnsupportedOperationException var7) {
         }

         for(i = 0; i < importData.length; ++i) {
            Pokemon oldPokemon = null;
            int pokemonListSize = this.pokemonList.size();
            if (pokemonListSize > i) {
               oldPokemon = (Pokemon)this.pokemonList.get(i);
            }

            if (importData[i] != null) {
               importData[i].setOriginalTrainer((UUID)null, GuiPokemonEditorParty.editedPlayerName);
            }

            if (pokemonListSize > i) {
               this.pokemonList.set(i, importData[i]);
            } else {
               this.pokemonList.add(importData[i]);
            }
         }

         Pixelmon.network.sendToServer(this.getImportSavePacket());
         return null;
      } else {
         return "";
      }
   }

   protected abstract IMessage getImportSavePacket();

   public GuiScreen getScreen() {
      return this;
   }
}

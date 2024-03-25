package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.ChangePokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.DeletePokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.UpdatePlayerPokemon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.LakeTrioStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MeltanStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MewStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GuiPokemonEditorIndividual extends GuiIndividualEditorBase {
   private GuiTextField pokeBallText;
   private GuiTextField cloneText;
   private GuiTextField lakeText;
   private GuiTextField smeltText;
   private ItemStack pokeBall;

   GuiPokemonEditorIndividual(Pokemon p, String titleText) {
      super(p, titleText);
   }

   public void func_73866_w_() {
      this.textFields.clear();
      this.cloneText = null;
      this.lakeText = null;
      this.smeltText = null;
      super.func_73866_w_();
      this.pokeBallText = (new GuiTabCompleteTextField(16, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 120, this.field_146295_m / 2 + 72, 90, 17)).setCompletions((Collection)Arrays.stream(EnumPokeballs.values()).map(ITranslatable::getLocalizedName).collect(Collectors.toSet()));
      this.pokeBall = new ItemStack(this.p.getCaughtBall().getItem());
      this.pokeBallText.func_146180_a(this.pokeBall.func_82833_r());
      this.textFields.add(this.pokeBallText);
      int formOffset = 20;
      GuiTextField var10000;
      if (this.p.getSpecies() == EnumSpecies.Mew) {
         this.cloneText = this.createExtraTextField(17);
         var10000 = this.cloneText;
         var10000.field_146210_g += formOffset;
         this.cloneText.func_146180_a(Integer.toString(((MewStats)this.p.getExtraStats(MewStats.class)).numCloned));
         this.textFields.add(this.cloneText);
      } else if (this.p.getSpecies() != EnumSpecies.Azelf && this.p.getSpecies() != EnumSpecies.Mesprit && this.p.getSpecies() != EnumSpecies.Uxie) {
         if (this.p.getSpecies() == EnumSpecies.Meltan) {
            this.smeltText = this.createExtraTextField(17);
            var10000 = this.smeltText;
            var10000.field_146210_g += formOffset;
            this.smeltText.func_146180_a(Integer.toString(((MeltanStats)this.p.getExtraStats(MeltanStats.class)).oresSmelted));
            this.textFields.add(this.smeltText);
         }
      } else {
         this.lakeText = this.createExtraTextField(17);
         var10000 = this.lakeText;
         var10000.field_146210_g += formOffset;
         this.lakeText.func_146180_a(Integer.toString(((LakeTrioStats)this.p.getExtraStats(LakeTrioStats.class)).numEnchanted));
         this.textFields.add(this.lakeText);
      }

   }

   protected void drawBackgroundUnderMenus(float f, int i, int j) {
      super.drawBackgroundUnderMenus(f, i, j);
      if (this.pokeBallText != null) {
         this.pokeBallText.func_146194_f();
         this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pokemoneditor.pokeball", new Object[0]), this.field_146294_l / 2 - 180, this.field_146295_m / 2 + 75, 0);
         this.field_146296_j.func_180450_b(this.pokeBall, this.field_146294_l / 2 - 28, this.field_146295_m / 2 + 72);
      }

      this.drawExtraText(this.cloneText, "gui.pokemoneditor.clones");
      this.drawExtraText(this.lakeText, "gui.pokemoneditor.lake_enchants");
      this.drawExtraText(this.smeltText, "gui.pokemoneditor.smelts");
   }

   protected void func_73869_a(char key, int keyCode) throws IOException {
      super.func_73869_a(key, keyCode);
      if (this.pokeBallText != null && this.pokeBallText.func_146206_l()) {
         this.updatePokeBall();
      }

   }

   private void updatePokeBall() {
      Item newItem = PixelmonItems.getItemFromName(this.pokeBallText.func_146179_b());
      if (newItem instanceof ItemPokeball) {
         this.pokeBall = new ItemStack(newItem);
      } else if (!"en_us".equals(Minecraft.func_71410_x().func_135016_M().func_135041_c().func_135034_a())) {
         for(Iterator var2 = PixelmonItemsPokeballs.getPokeballListWithMaster().iterator(); var2.hasNext(); this.pokeBall = new ItemStack(PixelmonItemsPokeballs.pokeBall)) {
            Item item = (Item)var2.next();
            if (item instanceof ItemPokeball && (new ItemStack(item)).func_82833_r().equalsIgnoreCase(this.pokeBallText.func_146179_b())) {
               this.pokeBall = new ItemStack(item);
               break;
            }
         }
      } else {
         this.pokeBall = new ItemStack(PixelmonItemsPokeballs.pokeBall);
      }

      this.p.setCaughtBall(((ItemPokeball)this.pokeBall.func_77973_b()).type);
   }

   protected void mouseClickedUnderMenus(int x, int y, int mouseButton) throws IOException {
      super.mouseClickedUnderMenus(x, y, mouseButton);
      GuiTextField[] var4 = new GuiTextField[]{this.pokeBallText, this.cloneText, this.lakeText};
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         GuiTextField textField = var4[var6];
         if (textField != null) {
            textField.func_146192_a(x, y, mouseButton);
         }
      }

   }

   protected void changePokemon(EnumSpecies newPokemon) {
      Pixelmon.network.sendToServer(new ChangePokemon(this.p.getUUID(), newPokemon));
   }

   protected void deletePokemon() {
      Pixelmon.network.sendToServer(new DeletePokemon(this.p.getUUID()));
      this.field_146297_k.func_147108_a(new GuiPokemonEditorParty());
   }

   protected void saveAndClose() {
      Pixelmon.network.sendToServer(new UpdatePlayerPokemon(this.p));
      this.field_146297_k.func_147108_a(new GuiPokemonEditorParty());
   }

   public List getPokemonList() {
      return ServerStorageDisplay.editedPokemon;
   }

   protected boolean checkFields() {
      boolean valid = super.checkFields();
      int smelts;
      if (this.cloneText != null) {
         try {
            smelts = Integer.parseInt(this.cloneText.func_146179_b());
            if (smelts < 0) {
               this.cloneText.func_146180_a("0");
               valid = false;
            } else if (smelts > 3) {
               this.cloneText.func_146180_a(Integer.toString(3));
               valid = false;
            } else {
               ((MewStats)this.p.getExtraStats(MewStats.class)).numCloned = smelts;
            }
         } catch (NumberFormatException var5) {
            this.cloneText.func_146180_a("");
            valid = false;
         }
      }

      if (this.lakeText != null) {
         try {
            smelts = Integer.parseInt(this.lakeText.func_146179_b());
            if (smelts < 0) {
               this.lakeText.func_146180_a("0");
               valid = false;
            } else if (smelts > PixelmonConfig.lakeTrioMaxEnchants) {
               this.lakeText.func_146180_a(Integer.toString(PixelmonConfig.lakeTrioMaxEnchants));
               valid = false;
            } else {
               ((LakeTrioStats)this.p.getExtraStats(LakeTrioStats.class)).numEnchanted = smelts;
            }
         } catch (NumberFormatException var4) {
            this.lakeText.func_146180_a("");
            valid = false;
         }
      }

      if (this.smeltText != null) {
         try {
            smelts = Integer.parseInt(this.smeltText.func_146179_b());
            if (smelts < 0) {
               this.smeltText.func_146180_a("0");
               valid = false;
            } else {
               ((MeltanStats)this.p.getExtraStats(MeltanStats.class)).oresSmelted = smelts;
            }
         } catch (NumberFormatException var3) {
            this.smeltText.func_146180_a("");
            valid = false;
         }
      }

      return valid;
   }
}

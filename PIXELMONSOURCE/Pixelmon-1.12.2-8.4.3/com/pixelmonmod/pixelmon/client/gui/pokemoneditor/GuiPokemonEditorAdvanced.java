package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GuiPokemonEditorAdvanced extends GuiScreenDropDown {
   private GuiIndividualEditorBase previousScreen;
   private Pokemon data;
   private GuiTextField[] evText = new GuiTextField[6];
   private GuiTextField[] ivText = new GuiTextField[6];
   private StatsType[] stats;
   private GuiTextField heldText;
   private ItemStack heldItem;
   private GuiTextField natureText;
   private GuiTextField friendshipText;
   private GuiDropDown abilityDropDown;
   private GuiTextField[] allText;
   private static final int BUTTON_OKAY = 1;
   private static final int BUTTON_MAX_IVS = 3;
   private static final int BUTTON_MIN_IVS = 4;
   private static final int BUTTON_RANDOM_IVS = 5;
   private static final int BUTTON_RESET_EVS = 6;

   public GuiPokemonEditorAdvanced(GuiIndividualEditorBase previousScreen) {
      this.stats = new StatsType[]{StatsType.HP, StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed};
      this.heldItem = ItemStack.field_190927_a;
      this.previousScreen = previousScreen;
      this.data = this.previousScreen.p;
   }

   public void func_73866_w_() {
      if (this.abilityDropDown != null) {
         this.checkFields();
      }

      super.func_73866_w_();
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
      String[] allAbilities = this.data.getBaseStats().getAbilitiesArray();
      List abilityList = (List)Arrays.stream(allAbilities).filter(Objects::nonNull).collect(Collectors.toList());
      int abilityWidth = this.getLongestString(abilityList);
      this.abilityDropDown = (new GuiDropDown(abilityList, this.data.getAbilityName(), this.field_146294_l / 2 - 130, this.field_146295_m / 2 - 40, 80, 50, Math.max(80, abilityWidth))).setGetOptionString((ability) -> {
         return I18n.func_135052_a("ability." + ability + ".name", new Object[0]);
      });
      this.addDropDown(this.abilityDropDown);
      this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 125, this.field_146295_m / 2 - 50, 60, 20, I18n.func_135052_a("gui.trainereditor.maxivs", new Object[0])));
      this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 + 125, this.field_146295_m / 2 - 25, 60, 20, I18n.func_135052_a("gui.trainereditor.minivs", new Object[0])));
      this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 + 125, this.field_146295_m / 2, 60, 20, I18n.func_135052_a("gui.trainereditor.randomivs", new Object[0])));
      this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2 + 125, this.field_146295_m / 2 + 35, 60, 20, I18n.func_135052_a("gui.trainereditor.resetevs", new Object[0])));
      this.allText = new GuiTextField[15];

      for(int i = 0; i < this.stats.length; ++i) {
         this.evText[i] = new GuiTextField(i, this.field_146297_k.field_71466_p, this.field_146294_l / 2 + 45, this.field_146295_m / 2 - 55 + i * 25, 30, 20);
         this.evText[i].func_146180_a(String.valueOf(this.data.getEVs().getArray()[i]));
         this.ivText[i] = new GuiTextField(i + this.evText.length, this.field_146297_k.field_71466_p, this.field_146294_l / 2 + 85, this.field_146295_m / 2 - 55 + i * 25, 30, 20);
         this.ivText[i].func_146180_a(String.valueOf(this.data.getIVs().getArray()[i]));
         this.allText[i] = this.evText[i];
         this.allText[i + this.evText.length] = this.ivText[i];
      }

      this.heldText = (new GuiTabCompleteTextField(13, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 130, this.field_146295_m / 2 - 4, 80, 20)).setCompletions((Collection)PixelmonItems.getAllItems().stream().filter((item) -> {
         return item instanceof ItemHeld;
      }).map(ItemStack::new).map(ItemStack::func_82833_r).collect(Collectors.toSet()));
      if (!this.data.getHeldItem().func_190926_b()) {
         this.heldText.func_146180_a(I18n.func_135052_a(this.data.getHeldItem().func_77977_a(), new Object[0]));
         this.updateHeldItem();
      }

      this.natureText = (new GuiTabCompleteTextField(14, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 130, this.field_146295_m / 2 + 34, 80, 20)).setCompletions((Collection)Arrays.stream(EnumNature.values()).map(ITranslatable::getLocalizedName).collect(Collectors.toSet()));
      this.natureText.func_146180_a(this.data.getBaseNature().getLocalizedName());
      this.friendshipText = new GuiTextField(14, this.field_146297_k.field_71466_p, this.field_146294_l / 2 - 130, this.field_146295_m / 2 + 70, 80, 20);
      this.friendshipText.func_146180_a(String.valueOf(this.data.getFriendship()));
      this.allText[12] = this.heldText;
      this.allText[13] = this.natureText;
      this.allText[14] = this.friendshipText;
   }

   private int getLongestString(List items) {
      return GuiHelper.getLongestStringWidth((Collection)items.stream().map((s) -> {
         return I18n.func_135052_a("ability." + s + ".name", new Object[0]);
      }).collect(Collectors.toList())) + 2;
   }

   protected void drawBackgroundUnderMenus(float renderPartialTicks, int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 200), (double)(this.field_146295_m / 2 - 120), 400.0, 240.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      GuiHelper.drawCenteredString(this.previousScreen.titleText, (float)(this.field_146294_l / 2), (float)(this.field_146295_m / 2 - 90), 0, false);
      GuiTextField[] var4 = this.allText;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         GuiTextField textField = var4[var6];
         textField.func_146194_f();
      }

      for(int i = 0; i < this.stats.length; ++i) {
         GuiHelper.drawStringRightAligned(this.stats[i].getLocalizedName(), (float)(this.field_146294_l / 2 + 40), (float)(this.field_146295_m / 2 - 50 + i * 25), 0, false);
      }

      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.evs", new Object[0]), this.field_146294_l / 2 + 50, this.field_146295_m / 2 - 70, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.trainereditor.ivs", new Object[0]), this.field_146294_l / 2 + 90, this.field_146295_m / 2 - 70, 0);
      GuiHelper.drawCenteredString(I18n.func_135052_a("gui.screenpokechecker.ability", new Object[0]), (float)(this.field_146294_l / 2 - 90), (float)(this.abilityDropDown.getTop() - 10), 0, false);
      GuiHelper.drawCenteredString(I18n.func_135052_a("gui.trainereditor.helditem", new Object[0]), (float)(this.field_146294_l / 2 - 90), (float)(this.heldText.field_146210_g - 10), 0, false);
      GuiHelper.drawCenteredString(I18n.func_135052_a("gui.screenpokechecker.nature", new Object[0]), (float)(this.field_146294_l / 2 - 90), (float)(this.natureText.field_146210_g - 10), 0, false);
      GuiHelper.drawCenteredString(I18n.func_135052_a("gui.screenpokechecker.happiness", new Object[0]), (float)(this.field_146294_l / 2 - 90), (float)(this.friendshipText.field_146210_g - 10), 0, false);
      if (!this.heldItem.func_190926_b()) {
         this.field_146296_j.func_180450_b(this.heldItem, this.field_146294_l / 2 - 150, this.heldText.field_146210_g);
      }

      GuiHelper.bindPokemonSprite(this.data, this.field_146297_k);
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 157), (double)(this.field_146295_m / 2 - 73), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, 1.0F);
   }

   protected void func_73869_a(char key, int keyCode) throws IOException {
      GuiTextField[] var3 = this.allText;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         GuiTextField textField = var3[var5];
         textField.func_146201_a(key, keyCode);
      }

      if (this.heldText.func_146206_l()) {
         this.updateHeldItem();
      }

      if (keyCode == 1 || keyCode == 28) {
         this.saveFields();
      }

   }

   protected void mouseClickedUnderMenus(int mouseX, int mouseY, int clickedButton) throws IOException {
      GuiTextField[] var4 = this.allText;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         GuiTextField textField = var4[var6];
         textField.func_146192_a(mouseX, mouseY, clickedButton);
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146127_k == 1) {
         this.saveFields();
      } else {
         GuiTextField[] var2;
         int var3;
         int var4;
         GuiTextField ev;
         if (button.field_146127_k == 3) {
            var2 = this.ivText;
            var3 = var2.length;

            for(var4 = 0; var4 < var3; ++var4) {
               ev = var2[var4];
               ev.func_146180_a("31");
            }
         } else if (button.field_146127_k == 4) {
            var2 = this.ivText;
            var3 = var2.length;

            for(var4 = 0; var4 < var3; ++var4) {
               ev = var2[var4];
               ev.func_146180_a("0");
            }
         } else if (button.field_146127_k == 5) {
            var2 = this.ivText;
            var3 = var2.length;

            for(var4 = 0; var4 < var3; ++var4) {
               ev = var2[var4];
               ev.func_146180_a(String.valueOf(RandomHelper.getRandomNumberBetween(0, 31)));
            }
         } else if (button.field_146127_k == 6) {
            var2 = this.evText;
            var3 = var2.length;

            for(var4 = 0; var4 < var3; ++var4) {
               ev = var2[var4];
               ev.func_146180_a("0");
            }
         }
      }

   }

   private void saveFields() {
      if (this.checkFields()) {
         this.field_146297_k.func_147108_a(this.previousScreen);
      }

   }

   private void updateHeldItem() {
      try {
         Item newItem = PixelmonItems.getItemFromName(this.heldText.func_146179_b());
         if (newItem instanceof ItemHeld) {
            this.heldItem = new ItemStack(newItem);
         } else if (!"en_us".equals(Minecraft.func_71410_x().func_135016_M().func_135041_c().func_135034_a())) {
            Iterator var2 = PixelmonItems.getAllItems().iterator();

            while(var2.hasNext()) {
               Item item = (Item)var2.next();
               if (item instanceof ItemHeld && (new ItemStack(item)).func_82833_r().equalsIgnoreCase(this.heldText.func_146179_b())) {
                  this.heldItem = new ItemStack(item);
                  return;
               }
            }

            this.heldItem = ItemStack.field_190927_a;
         } else {
            this.heldItem = ItemStack.field_190927_a;
         }
      } catch (Exception var4) {
         Pixelmon.LOGGER.warn("Failed to update held item!");
         var4.printStackTrace();
      }

   }

   private boolean checkFields() {
      boolean isValid = true;
      GuiTextField[] var2 = this.ivText;
      int var3 = var2.length;

      int newFriendship;
      for(newFriendship = 0; newFriendship < var3; ++newFriendship) {
         GuiTextField iv = var2[newFriendship];

         try {
            int ivNumber = Integer.parseInt(iv.func_146179_b());
            if (ivNumber < 0) {
               isValid = false;
               iv.func_146180_a("0");
            } else if (ivNumber > 31) {
               isValid = false;
               iv.func_146180_a("31");
            }
         } catch (NumberFormatException var11) {
            isValid = false;
            iv.func_146180_a("0");
         }
      }

      int evTotal = 0;
      GuiTextField[] var14 = this.evText;
      newFriendship = var14.length;

      int i;
      for(i = 0; i < newFriendship; ++i) {
         GuiTextField ev = var14[i];

         int evNumber;
         try {
            evNumber = Integer.parseInt(ev.func_146179_b());
            if (evNumber < 0) {
               isValid = false;
               ev.func_146180_a("0");
               evNumber = 0;
            } else if (evNumber > EVStore.MAX_EVS) {
               isValid = false;
               ev.func_146180_a(EVStore.MAX_EVS + "");
               evNumber = EVStore.MAX_EVS;
            }
         } catch (NumberFormatException var10) {
            isValid = false;
            ev.func_146180_a("0");
            evNumber = 0;
         }

         evTotal += evNumber;
         if (evTotal > EVStore.MAX_TOTAL_EVS) {
            isValid = false;
            ev.func_146180_a(String.valueOf(evNumber - (evTotal - EVStore.MAX_TOTAL_EVS)));
            evTotal = EVStore.MAX_TOTAL_EVS;
         }
      }

      EnumNature newNature = EnumNature.natureFromString(this.natureText.func_146179_b());
      if (newNature == null) {
         isValid = false;
         this.natureText.func_146180_a(this.data.getBaseNature().getLocalizedName());
      }

      this.updateHeldItem();
      if (this.heldItem.func_190926_b() && !this.heldText.func_146179_b().equals("")) {
         this.heldText.func_146180_a("");
         isValid = false;
      }

      newFriendship = this.data.getFriendship();

      try {
         newFriendship = Integer.parseInt(this.friendshipText.func_146179_b());
         if (newFriendship > 255) {
            this.friendshipText.func_146180_a(String.valueOf(255));
            isValid = false;
         } else if (newFriendship < 0) {
            this.friendshipText.func_146180_a("0");
            isValid = false;
         }
      } catch (NumberFormatException var9) {
      }

      if (isValid) {
         try {
            for(i = 0; i < this.stats.length; ++i) {
               this.data.getEVs().setStat(this.stats[i], Integer.parseInt(this.evText[i].func_146179_b()));
               this.data.getIVs().setStat(this.stats[i], Integer.parseInt(this.ivText[i].func_146179_b()));
            }

            this.data.setNature(newNature);
            this.data.setHeldItem(this.heldItem);
            this.data.setAbility((String)this.abilityDropDown.getSelected());
            this.data.setFriendship(newFriendship);
         } catch (NumberFormatException var12) {
            isValid = false;
         }
      }

      return isValid;
   }
}

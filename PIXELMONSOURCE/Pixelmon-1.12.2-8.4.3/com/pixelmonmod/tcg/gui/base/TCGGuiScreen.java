package com.pixelmonmod.tcg.gui.base;

import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.helper.CardHelper;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.input.Keyboard;

public abstract class TCGGuiScreen extends GuiScreen {
   public boolean visible = true;
   protected List textFieldList = new ArrayList();
   protected List slotList = new ArrayList();

   public TCGGuiScreen() {
      if (this.field_146297_k == null) {
         this.field_146297_k = Minecraft.func_71410_x();
      }

   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.slotList.forEach(GuiSlotCompact::initGui);
   }

   public void func_73876_c() {
      if (this.visible) {
         super.func_73876_c();
         this.textFieldList.forEach(GuiTextField::func_146178_a);
         this.slotList.forEach(GuiSlotCompact::updateScreen);
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         Iterator var4 = this.slotList.iterator();

         while(var4.hasNext()) {
            GuiSlotCompact slot = (GuiSlotCompact)var4.next();
            slot.func_148128_a(mouseX, mouseY, partialTicks);
         }

         this.textFieldList.forEach(GuiTextField::func_146194_f);
         super.func_73863_a(mouseX, mouseY, partialTicks);
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      super.func_73869_a(typedChar, keyCode);
      Iterator var3 = this.textFieldList.iterator();

      while(var3.hasNext()) {
         GuiTextField textField = (GuiTextField)var3.next();
         textField.func_146201_a(typedChar, keyCode);
      }

   }

   public void func_146281_b() {
      Keyboard.enableRepeatEvents(false);
   }

   public void func_146274_d() throws IOException {
      if (this.visible) {
         super.func_146274_d();
         this.slotList.forEach(GuiSlotCompact::func_178039_p);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (this.visible) {
         super.func_73864_a(mouseX, mouseY, mouseButton);
         Iterator var4 = this.textFieldList.iterator();

         while(var4.hasNext()) {
            GuiTextField textField = (GuiTextField)var4.next();
            textField.func_146192_a(mouseX, mouseY, mouseButton);
         }
      }

   }

   public void drawEffectTooltip(CommonCardState card, int mouseX, int mouseY, FontRenderer f, int xOffset, int yOffset) {
      int lines = 0;
      int x0 = this.field_146294_l / 2 + xOffset;
      int y0 = this.field_146295_m / 2 + yOffset;
      PokemonAttackStatus[] attacks = null;
      if (card instanceof PokemonCardState) {
         attacks = ((PokemonCardState)card).getAttacksStatus();
      } else if (card.getData().getAttacks() != null) {
         attacks = (PokemonAttackStatus[])Arrays.stream(card.getData().getAttacks()).map(PokemonAttackStatus::new).toArray((x$0) -> {
            return new PokemonAttackStatus[x$0];
         });
      }

      AbilityCard cardAbility = card.getData().getAbility();
      int i;
      int x1;
      int x2;
      int y1;
      String description;
      String[] desc;
      List list;
      if (cardAbility != null) {
         int linesForAbility = attacks != null && attacks.length > 0 ? 2 : 5;
         if (attacks.length == 1 && !attacks[0].hasDescription()) {
            linesForAbility = 4;
         }

         i = x0 - 46;
         x1 = x0 + 46;
         x2 = y0 + 9;
         y1 = y0 - 1 + (linesForAbility + 1) * 9;
         if (mouseX > i && mouseX < x1 && mouseY > x2 && mouseY < y1) {
            String name = "§l" + LanguageMapTCG.translateKey(cardAbility.getName());
            description = LanguageMapTCG.translateKey(cardAbility.getDescription());
            desc = new String[]{name, description};
            list = Arrays.asList(desc);
            this.drawHoveringText(list, mouseX, mouseY, f, card.getMainEnergy());
         }

         lines += linesForAbility;
      }

      if (attacks != null && attacks.length > 0) {
         int[] descriptionLines = CardHelper.calculateAttackDescriptionLines(attacks, cardAbility != null, 94, f);

         for(i = 0; i < attacks.length; ++i) {
            if (attacks[i] != null) {
               if (attacks[i].hasDescription()) {
                  x1 = x0 - 46;
                  x2 = x0 + 46;
                  y1 = y0 - 1 + (lines + 1 + (cardAbility != null ? 1 : 0)) * 9;
                  int y2 = y0 - 1 + (lines + 1 + (cardAbility != null ? 1 : 0) + descriptionLines[i]) * 9;
                  if (mouseX > x1 && mouseX < x2 && mouseY > y1 && mouseY < y2) {
                     description = attacks[i].getLocalizedDescription();
                     desc = new String[]{"§l" + LanguageMapTCG.translateKey(attacks[i].getData().getName()), description};
                     list = Arrays.asList(desc);
                     this.drawHoveringText(list, mouseX, mouseY, f, card.getMainEnergy());
                  }

                  lines += 1 + descriptionLines[i];
               } else {
                  ++lines;
               }
            }
         }
      }

   }

   public void drawHoveringText(List textLines, int x, int y, FontRenderer font, Energy type) {
      if (!textLines.isEmpty()) {
         GlStateManager.func_179101_C();
         RenderHelper.func_74518_a();
         GlStateManager.func_179140_f();
         GlStateManager.func_179097_i();
         int popupWidth = 130;
         Iterator iterator = textLines.iterator();

         int lineCount;
         List lines;
         for(lineCount = 0; iterator.hasNext(); lineCount += lines.size()) {
            String s = (String)iterator.next();
            lines = font.func_78271_c(s, popupWidth);
         }

         int startX = x + 12;
         int startY = y - 12;
         int popupHeight = 2 + lineCount * 9;
         if (startX + popupWidth > this.field_146294_l) {
            startX -= 20 + popupWidth;
         }

         if (startY + popupHeight + 6 > this.field_146295_m) {
            startY = this.field_146295_m - popupHeight - 6;
         }

         this.field_73735_i = 300.0F;
         this.field_146296_j.field_77023_b = 300.0F;
         int j1 = CardHelper.getBackgroundColor(type);
         this.func_73733_a(startX - 4, startY - 4, startX + popupWidth + 3, startY - 3, j1, j1);
         this.func_73733_a(startX - 4, startY + popupHeight + 3, startX + popupWidth + 3, startY + popupHeight + 4, j1, j1);
         this.func_73733_a(startX - 4, startY - 3, startX + popupWidth + 3, startY + popupHeight + 3, j1, j1);
         this.func_73733_a(startX - 5, startY - 3, startX - 3, startY + popupHeight + 3, j1, j1);
         this.func_73733_a(startX + popupWidth + 3, startY - 3, startX + popupWidth + 4, startY + popupHeight + 3, j1, j1);
         int k1 = 1347420415;
         int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
         this.func_73733_a(startX - 4, startY - 3 + 1, startX - 4 + 1, startY + popupHeight + 3 - 1, k1, l1);
         this.func_73733_a(startX + popupWidth + 2, startY - 3 + 1, startX + popupWidth + 3, startY + popupHeight + 3 - 1, k1, l1);
         this.func_73733_a(startX - 4, startY - 3, startX + popupWidth + 3, startY - 3 + 1, k1, k1);
         this.func_73733_a(startX - 4, startY + popupHeight + 2, startX + popupWidth + 3, startY + popupHeight + 3, l1, l1);

         for(int i = 0; i < textLines.size(); ++i) {
            font.func_78279_b((String)textLines.get(i), startX, startY + i * 12, popupWidth, CardHelper.getForegroundColor(type));
         }

         this.field_73735_i = 0.0F;
         this.field_146296_j.field_77023_b = 0.0F;
         GlStateManager.func_179145_e();
         GlStateManager.func_179126_j();
         RenderHelper.func_74519_b();
         GlStateManager.func_179091_B();
      }

   }

   public void drawRectWithBorder(int x1, int y1, int x2, int y2, int backgroundColor, int borderColor) {
      func_73734_a(x1, y1, x2, y2, backgroundColor);
      this.func_73730_a(x1, x2, y1, borderColor);
      this.func_73730_a(x1, x2, y2, borderColor);
      this.func_73728_b(x1, y1, y2, borderColor);
      this.func_73728_b(x2, y1, y2, borderColor);
   }
}

package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.tcg.gui.base.GuiSlotCompact;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class GuiCommandSlot extends GuiSlotCompact {
   protected final FontRenderer fontRenderer;
   protected final GuiTCGRuleEditor guiScreen;
   protected List commands = new ArrayList();

   public GuiCommandSlot(Minecraft mc, GuiTCGRuleEditor guiScreen, FontRenderer fontRenderer, int width, int height, int posX, int posY) {
      super(mc, width, height, posX, posY, 20);
      this.guiScreen = guiScreen;
      this.fontRenderer = fontRenderer;
   }

   public void initGui() {
      this.itemSlotContextMenu = new GuiCommandContextMenuSlotItem(this.field_148161_k, this.guiScreen, this);
   }

   protected void func_192637_a(int slotIndex, int x, int y, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
      if (this.commands != null && this.commands.size() > slotIndex) {
         String command = (String)this.commands.get(slotIndex);
         this.fontRenderer.func_175063_a(this.fontRenderer.func_78269_a(command, this.field_148155_a - 8), (float)(x + 5), (float)(y + 5), 16777215);
      }

   }

   public void func_148128_a(int mouseXIn, int mouseYIn, float partialTicks) {
      if (this.func_148127_b() == 0) {
         this.drawEmptyList(mouseXIn, mouseYIn);
      } else {
         super.func_148128_a(mouseXIn, mouseYIn, partialTicks);
      }

   }

   protected int func_148127_b() {
      return this.commands == null ? 0 : this.commands.size();
   }

   protected void func_148144_a(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
   }

   public int func_148139_c() {
      return this.field_148155_a;
   }

   protected boolean func_148131_a(int index) {
      return this.field_148168_r == index;
   }

   protected void func_148123_a() {
   }

   protected void func_148136_c(int top, int bottom, int alpha1, int alpha2) {
      this.field_148161_k.field_71446_o.func_110577_a(GuiTCGRuleEditor.background);
      GuiTCGRuleEditor var10000 = this.guiScreen;
      GuiTCGRuleEditor.func_146110_a(this.field_148152_e, top, 10.0F, 10.0F, this.field_148155_a, bottom - top, 2.0F, 50.0F);
   }

   protected void drawEmptyList(int mouseX, int mouseY) {
      this.guiScreen.func_73732_a(this.fontRenderer, I18n.func_135052_a("battleRule.emptyCommands", new Object[0]), this.field_148152_e + this.field_148155_a / 2, this.field_148153_b + this.field_148158_l / 2 - this.fontRenderer.field_78288_b / 2, 15658734);
   }

   public void deleteClicked() {
      if (this.field_148168_r > -1) {
         if (this.itemSlotContextMenu != null) {
            this.itemSlotContextMenu.setVisible(false);
         }

         this.commands.remove(this.field_148168_r);
      }

   }

   public List getCommands() {
      return this.commands;
   }

   public void setCommands(List commands) {
      this.commands = commands;
   }
}

package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.bag;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleMenuElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ItemMenuButton extends BattleMenuElement.MenuListButton {
   private final ItemStack stack;
   private String title;
   private int count;

   public ItemMenuButton(int buttonId, ItemStack stack, GuiScreen parent) {
      super(buttonId, parent);
      this.title = "";
      this.count = -1;
      this.stack = stack;
   }

   public ItemMenuButton(int buttonId, ItemStack stack, String title, GuiScreen parent) {
      this(buttonId, stack, parent);
      this.title = title;
   }

   public ItemMenuButton(int buttonId, ItemStack stack, int count, GuiScreen parent) {
      this(buttonId, stack, parent);
      this.count = count;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      super.func_191745_a(mc, mouseX, mouseY, partialTicks);
      RenderItem ri = Minecraft.func_71410_x().func_175599_af();
      ri.func_175042_a(this.stack, this.field_146128_h + 6, this.field_146129_i + 5);
      String displayName = this.title.isEmpty() ? this.stack.func_82833_r() : this.title;
      GuiHelper.drawScaledString(displayName, (float)(this.field_146128_h + 36), (float)(this.field_146129_i + 4), -16777216, 16.0F);
      if (this.count >= 0) {
         int countX = Math.max(122, this.field_146128_h + 36 + GuiHelper.getStringWidth(displayName) + 4);
         GuiHelper.drawScaledString("X " + this.count, (float)countX, (float)(this.field_146129_i + 4), -16777216, 16.0F);
      }

   }
}

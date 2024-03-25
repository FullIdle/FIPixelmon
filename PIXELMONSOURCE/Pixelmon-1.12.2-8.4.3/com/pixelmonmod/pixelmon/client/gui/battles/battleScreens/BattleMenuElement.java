package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.gui.GuiElement;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class BattleMenuElement extends GuiElement {
   protected static final ResourceLocation MENU_BACKGROUND = new ResourceLocation("pixelmon", "textures/gui/battle/menu_background.png");
   protected static final ResourceLocation MENU_LIST_ITEM = new ResourceLocation("pixelmon", "textures/gui/battle/menu_list_item.png");
   protected static final ResourceLocation MENU_LIST_LEFT = new ResourceLocation("pixelmon", "textures/gui/battle/menu_left.png");
   protected static final ResourceLocation MENU_LIST_RIGHT = new ResourceLocation("pixelmon", "textures/gui/battle/menu_right.png");
   private int page = 1;
   private final GuiScreen parent;
   private String title;
   private List buttons;
   private MenuReuseElement reuseElement = null;

   public BattleMenuElement(GuiScreen parent) {
      this.parent = parent;
      this.setTitle("");
      this.setButtons(Collections.emptyList());
   }

   public BattleMenuElement(GuiScreen parent, String title, List buttons) {
      this.parent = parent;
      this.setTitle(title);
      this.setButtons(buttons);
   }

   public String getTitle() {
      return I18n.func_135052_a(this.title, new Object[0]);
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public List getAllButtons() {
      return this.buttons;
   }

   public void setButtons(List buttons) {
      this.buttons = buttons;
   }

   public List getPageButtons() {
      List list = Lists.newArrayList();
      int maxPages = this.buttons.size() < 6 ? 1 : this.buttons.size() / 6 + (this.buttons.size() % 6 > 0 ? 1 : 0);
      int page = Math.min(this.page, maxPages);

      for(int i = 0; i < 6; ++i) {
         if (i + 6 * (page - 1) < this.buttons.size()) {
            list.add(this.buttons.get(i + 6 * (page - 1)));
         }
      }

      return list;
   }

   public void setReuseMessage(String key, @Nullable ItemStack stack) {
      if (key != null && !key.isEmpty()) {
         this.reuseElement = new MenuReuseElement(key, stack);
      } else {
         this.reuseElement = null;
      }

   }

   public void drawElement(float scale) {
      GuiHelper.drawImage(MENU_BACKGROUND, (double)this.x, (double)this.y, 180.0, 240.0, this.zLevel);
      GuiHelper.drawScaledCenteredString(this.getTitle(), (float)this.x + 85.0F, (float)(this.y + 6), -986896, 14.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.reuseElement != null) {
         this.reuseElement.drawElement(scale);
      }

      int mouseX = Mouse.getX() * this.width / this.parent.field_146294_l;
      int mouseZ = this.height - Mouse.getY() * this.height / this.parent.field_146295_m - 1;
      this.drawMenuButtons(mouseX, mouseZ);
   }

   public GuiElement setPosition(int x, int y, int width, int height) {
      GuiElement t = super.setPosition(x, y, width, height);
      if (this.reuseElement != null) {
         this.reuseElement.setPosition(x + 45, y + height - 22, 110, 18);
      }

      Iterator var6 = this.getAllButtons().iterator();

      while(var6.hasNext()) {
         MenuListButton button = (MenuListButton)var6.next();
         button.setPosition(x, y, width, height);
      }

      return t;
   }

   public void drawMenuButtons(int mouseX, int mouseY) {
      int maxPages = this.buttons.size() < 6 ? 1 : this.buttons.size() / 6 + (this.buttons.size() % 6 > 0 ? 1 : 0);
      this.page = Math.min(this.page, maxPages);

      for(int i = 0; i < 6; ++i) {
         if (i + 6 * (this.page - 1) < this.buttons.size()) {
            ((MenuListButton)this.buttons.get(i + 6 * (this.page - 1))).func_191745_a(this.parent.field_146297_k, mouseX, mouseY, this.zLevel);
         }
      }

      if (maxPages > 1) {
         GuiHelper.drawScaledCenteredString(this.page + "/" + maxPages, 85.0F, (float)(this.y + this.height - 38), -986896, 14.0F);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.page < maxPages) {
            GuiHelper.drawImage(MENU_LIST_RIGHT, 100.0, (double)(this.y + this.height - 39), 8.0, 8.0, this.zLevel);
         }

         if (this.page > 1) {
            GuiHelper.drawImage(MENU_LIST_LEFT, 62.0, (double)(this.y + this.height - 39), 8.0, 8.0, this.zLevel);
         }
      }

   }

   public boolean isOverReturnButton(int mouseX, int mouseY) {
      return mouseX >= this.x && mouseX < this.x + 30 && mouseY >= this.y + this.height - 30 && mouseY < this.y + this.height;
   }

   public boolean isOverReuseButton(int mouseX, int mouseY) {
      return this.reuseElement != null && this.reuseElement.isMouseOver(mouseX, mouseY);
   }

   public boolean handleClickPageTurn(int mouseX, int mouseY) {
      int maxPages = this.buttons.size() < 6 ? 1 : this.buttons.size() / 6 + (this.buttons.size() % 6 > 0 ? 1 : 0);
      if (maxPages > 1) {
         if (this.page > 1 && (float)mouseX >= 62.0F && mouseY >= this.y + this.height - 39 && (float)mouseX < 70.0F && mouseY < this.y + this.height - 39 + 8) {
            --this.page;
            return true;
         }

         if (this.page < maxPages && (float)mouseX >= 100.0F && mouseY >= this.y + this.height - 39 && (float)mouseX < 108.0F && mouseY < this.y + this.height - 39 + 8) {
            ++this.page;
            return true;
         }
      }

      return false;
   }

   public static class MenuListButton extends GuiButton {
      private boolean isPressed;

      public MenuListButton(int buttonId, GuiScreen parent) {
         super(buttonId, 0, 0, "");
         this.setPosition(0, 0, parent.field_146294_l, parent.field_146295_m);
      }

      public MenuListButton setPosition(int x, int y, int width, int height) {
         this.field_146128_h = x + 5;
         this.field_146129_i = y + 20 + this.field_146127_k % 6 * 30;
         this.field_146120_f = 155;
         this.field_146121_g = 26;
         return this;
      }

      public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
         if (this.field_146125_m) {
            GuiHelper.drawImage(BattleMenuElement.MENU_LIST_ITEM, (double)this.field_146128_h, (double)this.field_146129_i, (double)this.field_146120_f, (double)this.field_146121_g, this.field_73735_i);
         }

      }

      public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
         return this.isPressed = super.func_146116_c(mc, mouseX, mouseY);
      }

      public void func_146118_a(int mouseX, int mouseY) {
         this.isPressed = false;
      }
   }
}

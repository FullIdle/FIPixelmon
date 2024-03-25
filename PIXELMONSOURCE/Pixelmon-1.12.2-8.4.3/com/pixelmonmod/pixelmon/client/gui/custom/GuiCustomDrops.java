package com.pixelmonmod.pixelmon.client.gui.custom;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.EnumPositionTriState;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.packetHandlers.custom.drops.CustomDropsAction;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.util.vector.Vector4f;

public class GuiCustomDrops extends GuiScreen {
   ITextComponent title;
   ItemStack[] items;
   String[] buttons;
   boolean action = false;

   public GuiCustomDrops(ITextComponent title, ItemStack[] items, String[] buttons) {
      this.title = title;
      this.items = items;
      this.buttons = buttons;
   }

   public void func_73866_w_() {
      this.field_146292_n.clear();
      int buttonHeight = (this.field_146295_m - 182) / 2 + 150;
      this.func_189646_b(new Button(-1, (this.field_146294_l - 280) / 2 + 10, buttonHeight, 80, 20, this.buttons[EnumPositionTriState.LEFT.ordinal()]));
      this.func_189646_b(new Button(-2, (this.field_146294_l - 280) / 2 + 190, buttonHeight, 80, 20, this.buttons[EnumPositionTriState.RIGHT.ordinal()]));
      this.func_189646_b(new Button(-3, (this.field_146294_l - 280) / 2 + 100, buttonHeight, 80, 20, this.buttons[EnumPositionTriState.CENTER.ordinal()]));
      Iterator var2 = this.field_146292_n.iterator();

      while(var2.hasNext()) {
         GuiButton button = (GuiButton)var2.next();
         if (button.field_146126_j.isEmpty()) {
            button.field_146125_m = false;
         }
      }

      int x = 0;
      int y = 0;

      for(int i = 0; i < this.items.length; ++i) {
         ItemButton button = (ItemButton)this.func_189646_b(new ItemButton(i, 0, 0, 24, 24, this.items[i]));
         if (button.stack != null && !button.stack.func_190926_b()) {
            button.field_146128_h = (this.field_146294_l - 280) / 2 + 15 + x * 40;
            button.field_146129_i = (this.field_146295_m - 182) / 2 + 45 + y * 30;
            ++x;
            if (x > 3) {
               x = 0;
               ++y;
            }
         } else {
            button.field_146125_m = false;
         }
      }

   }

   public void func_73876_c() {
      this.action = false;
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (!this.action) {
         if (button.field_146127_k < 0) {
            EnumPositionTriState position = button.field_146127_k == -1 ? EnumPositionTriState.LEFT : (button.field_146127_k == -2 ? EnumPositionTriState.RIGHT : EnumPositionTriState.CENTER);
            Pixelmon.network.sendToServer(new CustomDropsAction(position));
            this.closeScreen();
         } else if (button instanceof ItemButton) {
            ItemButton item = (ItemButton)button;
            item.stack.func_190920_e(0);
            Pixelmon.network.sendToServer(new CustomDropsAction(item.field_146127_k));
            if (this.field_146292_n.stream().filter((b) -> {
               return b instanceof ItemButton;
            }).map((b) -> {
               return (ItemButton)b;
            }).allMatch((b) -> {
               return b.stack.func_190926_b();
            })) {
               this.closeScreen();
            } else {
               this.func_73866_w_();
            }
         }

         this.action = true;
      }
   }

   private void closeScreen() {
      GuiHelper.closeScreen();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179147_l();
      Vector4f colour = new Vector4f(0.1F, 0.1F, 0.1F, 0.6F);
      GuiHelper.drawGradientRect((this.field_146294_l - 280) / 2, (this.field_146295_m - 182) / 2, this.field_73735_i, (this.field_146294_l - 280) / 2 + 280, (this.field_146295_m - 182) / 2 + 182, colour, colour, false);
      colour = new Vector4f(0.1F, 0.2F, 0.23F, 0.6F);
      Vector4f colour2 = new Vector4f(0.1F, 0.14F, 0.2F, 0.6F);
      GuiHelper.drawGradientRect((this.field_146294_l - 280) / 2, (this.field_146295_m - 182) / 2, this.field_73735_i, (this.field_146294_l - 280) / 2 + 280, (this.field_146295_m - 182) / 2 + 30, colour, colour2, false);
      this.field_146297_k.field_71466_p.func_78276_b(this.title.func_150254_d(), (this.field_146294_l - 280) / 2 + 10, (this.field_146295_m - 182) / 2 + 15, 16777215);
      int x = false;
      int y = false;
      colour = new Vector4f(0.1F, 0.1F, 0.1F, 0.3F);
      int x = (this.field_146294_l - 280) / 2 + 5;
      int y = (this.field_146295_m - 182) / 2 + 35;
      GuiHelper.drawGradientRect(x, y, this.field_73735_i, x + 165, y + 105, colour, colour, false);
      RenderHelper.func_74520_c();
      colour = new Vector4f(0.1F, 0.1F, 0.1F, 0.7F);
      int itemXPos = (this.field_146294_l - 280) / 2 + 175;
      int itemYPos = (this.field_146295_m - 182) / 2 + 35;
      GuiHelper.drawGradientRect(itemXPos, itemYPos, this.field_73735_i, itemXPos + 100, itemYPos + 105, colour, colour, false);
      super.func_73863_a(mouseX, mouseY, partialTicks);
      boolean hoverItem = false;
      Iterator var12 = this.field_146292_n.iterator();

      String s;
      while(var12.hasNext()) {
         GuiButton button = (GuiButton)var12.next();
         if (button instanceof ItemButton && button.func_146115_a()) {
            ItemButton item = (ItemButton)button;
            GlStateManager.func_179094_E();
            s = item.stack.func_82833_r();
            GuiHelper.drawCenteredString(s, (float)((this.field_146294_l - 280) / 2 + 225), (float)(itemYPos + 5), 16777215);
            GuiHelper.drawCenteredString(I18n.func_135052_a("gui.guiItemDrops.click", new Object[0]), (float)((this.field_146294_l - 280) / 2 + 225), (float)(itemYPos + 90), 16777215);
            itemXPos = (this.field_146294_l / 3 - 93) / 2 + 69;
            itemYPos = (this.field_146295_m / 3 - 60) / 2 + 21;
            GlStateManager.func_179152_a(3.0F, 3.0F, 3.0F);
            this.field_146296_j.func_180450_b(item.stack, itemXPos, itemYPos);
            this.field_146296_j.func_180453_a(this.field_146297_k.field_71466_p, item.stack, itemXPos, itemYPos, (String)null);
            GlStateManager.func_179152_a(0.33333334F, 0.33333334F, 0.33333334F);
            GlStateManager.func_179121_F();
            hoverItem = true;
         }
      }

      if (!hoverItem) {
         s = I18n.func_135052_a("gui.guiItemDrops.mouse", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(s, (this.field_146294_l - 280) / 2 + 228 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, (this.field_146295_m - 182) / 2 + 100, 8947848);
         s = I18n.func_135052_a("gui.guiItemDrops.see", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(s, (this.field_146294_l - 280) / 2 + 228 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, (this.field_146295_m - 182) / 2 + 114, 8947848);
         s = I18n.func_135052_a("gui.guiItemDrops.details", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(s, (this.field_146294_l - 280) / 2 + 228 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, (this.field_146295_m - 182) / 2 + 128, 8947848);
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public static class ItemButton extends Button {
      ItemStack stack;

      public ItemButton(int buttonId, int x, int y, int widthIn, int heightIn, ItemStack stack) {
         super(buttonId, x, y, widthIn, heightIn, "");
         this.stack = stack;
      }

      public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
         if (this.field_146125_m) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Vector4f color = this.field_146123_n ? new Vector4f(1.0F, 1.0F, 1.0F, 1.4F) : new Vector4f(0.0F, 0.0F, 0.0F, 0.4F);
            GuiHelper.drawGradientRect(this.field_146128_h, this.field_146129_i, this.field_73735_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, color, color, false);
            RenderHelper.func_74520_c();
            Minecraft.func_71410_x().func_175599_af().func_180450_b(this.stack, this.field_146128_h + 4, this.field_146129_i + 3);
            RenderItem var10000 = Minecraft.func_71410_x().func_175599_af();
            int var10003 = this.field_146128_h + 4;
            int var10004 = this.field_146129_i + 3;
            var10000.func_180453_a(Minecraft.func_71410_x().field_71466_p, this.stack, var10003, var10004, (String)null);
            this.func_146119_b(mc, mouseX, mouseY);
         }

      }
   }

   public static class Button extends GuiButton {
      public Button(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
         super(buttonId, x, y, widthIn, heightIn, buttonText);
      }

      public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
         if (this.field_146125_m) {
            FontRenderer fontrenderer = mc.field_71466_p;
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            int i = this.func_146114_a(this.field_146123_n);
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Vector4f color = this.field_146123_n ? new Vector4f(1.0F, 1.0F, 1.0F, 1.4F) : new Vector4f(0.0F, 0.0F, 0.0F, 0.4F);
            GuiHelper.drawGradientRect(this.field_146128_h, this.field_146129_i, this.field_73735_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, color, color, false);
            this.func_146119_b(mc, mouseX, mouseY);
            int j = 14737632;
            if (this.packedFGColour != 0) {
               j = this.packedFGColour;
            } else if (!this.field_146124_l) {
               j = 10526880;
            } else if (this.field_146123_n) {
               j = 16777120;
            }

            this.func_73732_a(fontrenderer, this.field_146126_j, this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + (this.field_146121_g - 8) / 2, j);
         }

      }
   }
}

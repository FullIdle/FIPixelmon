package com.pixelmonmod.pixelmon.client.gui.mail;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.comm.packetHandlers.mail.MailPacket;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiMail extends GuiScreen {
   private boolean editable = true;
   private ResourceLocation backgroundTexture;
   private final ResourceLocation sealButtonTexture;
   private final ResourceLocation abandonButtonTexture;
   private GuiButton sealButton;
   private final int sealX;
   private final int sealY;
   private GuiButton exitButton;
   private final int exitX;
   private final int exitY;
   private String bookContents;
   private String author;

   public GuiMail(ItemStack item) {
      this.sealButtonTexture = new ResourceLocation(GuiResources.prefix + "gui/mail/sealButton.png");
      this.abandonButtonTexture = new ResourceLocation(GuiResources.prefix + "gui/mail/closeButton.png");
      this.sealX = 60;
      this.sealY = 185;
      this.exitX = 100;
      this.exitY = 185;
      this.bookContents = "";
      this.author = "";
      String mailType = item.func_77977_a().split("_")[1];
      this.backgroundTexture = new ResourceLocation(GuiResources.prefix + "gui/mail/" + mailType + "mail.png");
      if (item.func_77942_o()) {
         NBTTagCompound nbtdata = item.func_77978_p();
         this.editable = nbtdata.func_74767_n("editable");
         this.author = nbtdata.func_74779_i("author");
         this.bookContents = nbtdata.func_74779_i("contents");
      }

   }

   private void writeLetterData(boolean shouldSeal) {
      MailPacket packetMail = new MailPacket(shouldSeal, this.bookContents);
      Pixelmon.network.sendToServer(packetMail);
      Minecraft.func_71410_x().func_147108_a((GuiScreen)null);
   }

   public void func_73866_w_() {
      Keyboard.enableRepeatEvents(true);
      int buttonID = 0;
      int xOffset = (this.field_146294_l - 252) / 2;
      int yOffset = 2;
      int var10003 = buttonID++;
      this.getClass();
      int var10004 = xOffset + 60;
      this.getClass();
      this.sealButton = new GuiButton(var10003, var10004, yOffset + 185, 30, 20, I18n.func_135052_a("gui.mail.seal", new Object[0]));
      this.getClass();
      var10004 = xOffset + 100;
      this.getClass();
      this.exitButton = new GuiButton(buttonID, var10004, yOffset + 185, 30, 20, I18n.func_135052_a("gui.mail.close", new Object[0]));
      if (!this.editable) {
         this.sealButton.field_146124_l = false;
      }

      this.field_146292_n.add(this.sealButton);
      this.field_146292_n.add(this.exitButton);
   }

   protected void func_146284_a(GuiButton button) {
      switch (button.field_146127_k) {
         case 0:
            this.writeLetterData(true);
            break;
         case 1:
            if (this.editable) {
               this.writeLetterData(false);
            } else {
               Minecraft.func_71410_x().func_147108_a((GuiScreen)null);
            }
      }

   }

   protected void func_73869_a(char keyChar, int keyCode) throws IOException {
      if (!this.editable) {
         super.func_73869_a(keyChar, keyCode);
      } else {
         label28:
         switch (keyChar) {
            case '\u0016':
               char[] var3 = GuiScreen.func_146277_j().toCharArray();
               int var4 = var3.length;
               int var5 = 0;

               while(true) {
                  if (var5 >= var4) {
                     break label28;
                  }

                  char character = var3[var5];
                  this.addNormalCharacter(character);
                  ++var5;
               }
            default:
               switch (keyCode) {
                  case 14:
                     if (!this.bookContents.isEmpty()) {
                        this.bookContents = this.bookContents.substring(0, this.bookContents.length() - 1);
                     }
                     break;
                  case 28:
                  case 156:
                     String[] strings2 = this.bookContents.split("\n");
                     if (strings2.length != 14) {
                        this.bookContents = this.bookContents + "\n";
                     }
                     break;
                  default:
                     this.addNormalCharacter(keyChar);
               }
         }

         super.func_73869_a(keyChar, keyCode);
      }
   }

   private void addNormalCharacter(char keyChar) {
      if (ChatAllowedCharacters.func_71566_a(keyChar)) {
         String[] strings = this.bookContents.split("\n");
         if (strings.length > 0) {
            int widthInPixels = this.field_146289_q.func_78256_a(strings[strings.length - 1]);
            if (widthInPixels >= 200) {
               if (strings.length == 14) {
                  return;
               }

               this.bookContents = this.bookContents + "\n";
            }
         }

         this.bookContents = this.bookContents + keyChar;
      }
   }

   public void func_146281_b() {
      Keyboard.enableRepeatEvents(false);
   }

   public void func_73863_a(int mouseX, int mouseY, float wut) {
      int guiWidth = true;
      int guiHeight = true;
      this.field_146297_k.field_71446_o.func_110577_a(this.backgroundTexture);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      int xOffset = (this.field_146294_l - 252) / 2;
      int yOffset = 2;
      Gui.func_146110_a(xOffset, yOffset, 0.0F, 0.0F, 252, 188, 252.0F, 188.0F);
      int buttonWidth = 32;
      int buttonHeight = 32;
      int var10000;
      if (this.editable) {
         this.field_146297_k.field_71446_o.func_110577_a(this.sealButtonTexture);
         this.getClass();
         var10000 = xOffset + 60;
         this.getClass();
         Gui.func_146110_a(var10000, yOffset + 185, 0.0F, 0.0F, buttonWidth, buttonHeight, (float)buttonWidth, (float)buttonHeight);
      }

      this.field_146297_k.field_71446_o.func_110577_a(this.abandonButtonTexture);
      this.getClass();
      var10000 = xOffset + 100;
      this.getClass();
      Gui.func_146110_a(var10000, yOffset + 185, 0.0F, 0.0F, buttonWidth, buttonHeight, (float)buttonWidth, (float)buttonHeight);
      String[] lines = this.bookContents.split("\n");

      for(int lineNumber = 0; lineNumber < lines.length; ++lineNumber) {
         this.field_146289_q.func_78276_b(lines[lineNumber], xOffset + 27, yOffset + 32 + lineNumber * 9 - this.field_146289_q.field_78288_b, 0);
      }

      this.field_146289_q.func_78276_b(this.author, xOffset + 164, yOffset + 175 - this.field_146289_q.field_78288_b, 0);
      this.drawButtonTooltips(mouseX, mouseY);
   }

   private void drawButtonTooltips(int mouseX, int mouseY) {
      ArrayList tooltipData;
      if (this.mouseOverButton(mouseX, mouseY, this.sealButton)) {
         tooltipData = new ArrayList(1);
         tooltipData.add(I18n.func_135052_a("gui.mail.seal", new Object[0]));
         GuiHelper.renderTooltip(mouseX, mouseY, tooltipData, Color.BLUE.getRGB(), Color.BLACK.getRGB(), 100, false, false);
      } else if (this.mouseOverButton(mouseX, mouseY, this.exitButton)) {
         tooltipData = new ArrayList(1);
         tooltipData.add(I18n.func_135052_a("gui.mail.close", new Object[0]));
         GuiHelper.renderTooltip(mouseX, mouseY, tooltipData, Color.BLUE.getRGB(), Color.BLACK.getRGB(), 100, false, false);
      }

   }

   private boolean mouseOverButton(int mouseX, int mouseY, GuiButton button) {
      return button.field_146124_l && mouseX >= button.field_146128_h && mouseX <= button.field_146128_h + button.field_146120_f && mouseY >= button.field_146129_i && mouseY <= button.field_146129_i + button.field_146120_f;
   }

   public boolean func_73868_f() {
      return false;
   }
}

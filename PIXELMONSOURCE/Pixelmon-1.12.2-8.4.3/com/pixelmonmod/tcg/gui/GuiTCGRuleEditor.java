package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.RulebookUpdatePacket;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class GuiTCGRuleEditor extends TCGGuiScreen {
   public static ResourceLocation background = new ResourceLocation("tcg", "gui/background.png");
   private int deckSize;
   private int prizeCount;
   private int timeLimit;
   private String startingCommand;
   private String endingCommand;
   private String startingMessage;
   private String endingMessage;
   private int eloMinimum;
   private GuiTextField deckSizeTextField;
   private GuiTextField prizeCountTextField;
   private GuiTextField startingCommandTextField;
   private GuiTextField endingCommandTextField;
   private GuiTextField timeLimitTextField;
   private GuiTextField startingMessageTextField;
   private GuiTextField endingMessageTextField;
   private GuiTextField eloMinimumTextField;
   private final ItemStack book;
   private int hotbarSlot;
   private GuiButton doneBtn;
   private GuiButton cancelBtn;
   private static final int DONE_BUTTON_ID = 0;
   private static final int CANCEL_BUTTON_ID = 1;

   public GuiTCGRuleEditor(EntityPlayer player, ItemStack book) {
      this.book = book;
      this.deckSize = book.func_77978_p() != null ? book.func_77978_p().func_74762_e("DeckSize") : 60;
      this.timeLimit = book.func_77978_p() != null ? book.func_77978_p().func_74762_e("TimeLimit") : 0;
      this.prizeCount = book.func_77978_p() != null ? book.func_77978_p().func_74762_e("PrizeCount") : 6;
      this.startingCommand = book.func_77978_p() != null ? book.func_77978_p().func_74779_i("StartingCommand") : "";
      this.endingCommand = book.func_77978_p() != null ? book.func_77978_p().func_74779_i("EndingCommand") : "";
      this.startingMessage = book.func_77978_p() != null ? book.func_77978_p().func_74779_i("StartingMessage") : "";
      this.endingMessage = book.func_77978_p() != null ? book.func_77978_p().func_74779_i("EndingMessage") : "";
      this.eloMinimum = book.func_77978_p() != null ? book.func_77978_p().func_74762_e("EloMinimum") : 0;
      this.hotbarSlot = player.field_71071_by.field_70461_c;
   }

   public void func_73866_w_() {
      Keyboard.enableRepeatEvents(true);
      if (!this.book.func_77942_o()) {
         this.book.func_77982_d(new NBTTagCompound());
      }

      this.field_146292_n.clear();
      this.textFieldList.clear();
      this.slotList.clear();
      this.field_146292_n.add(this.doneBtn = new GuiButton(0, this.field_146294_l / 2 - 4 - 150, 210, 150, 20, I18n.func_135052_a("gui.done", new Object[0])));
      this.field_146292_n.add(this.cancelBtn = new GuiButton(1, this.field_146294_l / 2 + 4, 210, 150, 20, I18n.func_135052_a("gui.cancel", new Object[0])));
      this.textFieldList.add(this.deckSizeTextField = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 150, 45, 23, 20));
      this.deckSizeTextField.func_146203_f(32767);
      this.deckSizeTextField.func_146195_b(false);
      this.deckSizeTextField.func_146180_a(Integer.toString(this.deckSize));
      this.textFieldList.add(this.prizeCountTextField = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - 80, 45, 23, 20));
      this.prizeCountTextField.func_146203_f(32767);
      this.prizeCountTextField.func_146195_b(false);
      this.prizeCountTextField.func_146180_a(Integer.toString(this.prizeCount));
      this.textFieldList.add(this.timeLimitTextField = new GuiTextField(4, this.field_146289_q, this.field_146294_l / 2 - 10, 45, 23, 20));
      this.timeLimitTextField.func_146203_f(32767);
      this.timeLimitTextField.func_146195_b(false);
      this.timeLimitTextField.func_146180_a(Integer.toString(this.timeLimit));
      this.textFieldList.add(this.eloMinimumTextField = new GuiTextField(5, this.field_146289_q, this.field_146294_l / 2 + 60, 45, 23, 20));
      this.eloMinimumTextField.func_146203_f(32767);
      this.eloMinimumTextField.func_146195_b(false);
      this.eloMinimumTextField.func_146180_a(Integer.toString(this.eloMinimum));
      this.textFieldList.add(this.startingCommandTextField = new GuiTextField(6, this.field_146289_q, this.field_146294_l / 2 - 150, 115, 276, 20));
      this.startingCommandTextField.func_146203_f(32767);
      this.startingCommandTextField.func_146195_b(false);
      this.startingCommandTextField.func_146180_a(this.startingCommand);
      this.textFieldList.add(this.endingCommandTextField = new GuiTextField(7, this.field_146289_q, this.field_146294_l / 2 - 150, 180, 276, 20));
      this.endingCommandTextField.func_146203_f(32767);
      this.endingCommandTextField.func_146195_b(false);
      this.endingCommandTextField.func_146180_a(this.endingCommand);
      this.doneBtn.field_146124_l = this.deckSizeTextField.func_146179_b().trim().length() > 0;
      super.func_73866_w_();
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146124_l) {
         if (!this.book.func_77942_o()) {
            this.book.func_77982_d(new NBTTagCompound());
         }

         NBTTagCompound tag = this.book.func_77978_p();
         switch (button.field_146127_k) {
            case 0:
               int eloMinimum;
               try {
                  eloMinimum = Integer.parseInt(this.deckSizeTextField.func_146179_b());
                  if (eloMinimum != 30 && eloMinimum != 60) {
                     this.field_146297_k.field_71439_g.func_146105_b(new TextComponentString(TextFormatting.RED + "Deck size has to be 30 or 60!"), false);
                  } else {
                     tag.func_74768_a("DeckSize", eloMinimum);
                  }
               } catch (Exception var7) {
                  this.field_146297_k.field_71439_g.func_146105_b(new TextComponentString(TextFormatting.RED + "Invalid deck size!"), false);
               }

               try {
                  eloMinimum = Integer.parseInt(this.prizeCountTextField.func_146179_b());
                  if (eloMinimum >= 1 && eloMinimum <= 6) {
                     tag.func_74768_a("PrizeCount", eloMinimum);
                  } else {
                     this.field_146297_k.field_71439_g.func_146105_b(new TextComponentString(TextFormatting.RED + "Prize count has to be 1 to 6!"), false);
                  }
               } catch (Exception var6) {
                  this.field_146297_k.field_71439_g.func_146105_b(new TextComponentString(TextFormatting.RED + "Invalid prize count!"), false);
               }

               try {
                  eloMinimum = Integer.parseInt(this.timeLimitTextField.func_146179_b());
                  if (eloMinimum >= 0) {
                     tag.func_74768_a("TimeLimit", eloMinimum);
                  } else {
                     this.field_146297_k.field_71439_g.func_146105_b(new TextComponentString(TextFormatting.RED + "Time limit has to be larger or equal to 0!"), false);
                  }
               } catch (Exception var5) {
                  this.field_146297_k.field_71439_g.func_146105_b(new TextComponentString(TextFormatting.RED + "Invalid time limit!"), false);
               }

               try {
                  eloMinimum = Integer.parseInt(this.eloMinimumTextField.func_146179_b());
                  if (eloMinimum >= 0) {
                     tag.func_74768_a("EloMinimum", eloMinimum);
                  } else {
                     this.field_146297_k.field_71439_g.func_146105_b(new TextComponentString(TextFormatting.RED + "Elo requirement has to be larger or equal to 0!"), false);
                  }
               } catch (Exception var4) {
                  this.field_146297_k.field_71439_g.func_146105_b(new TextComponentString(TextFormatting.RED + "Invalid Elo requirement!"), false);
               }

               tag.func_74778_a("StartingCommand", this.startingCommandTextField.func_146179_b());
               tag.func_74778_a("EndingCommand", this.endingCommandTextField == null ? "" : this.endingCommandTextField.func_146179_b());
               tag.func_74778_a("StartingMessage", this.startingMessageTextField == null ? "" : this.startingMessageTextField.func_146179_b());
               tag.func_74778_a("EndingMessage", this.endingMessageTextField == null ? "" : this.endingMessageTextField.func_146179_b());
               PacketHandler.net.sendToServer(new RulebookUpdatePacket(tag, this.hotbarSlot));
               this.field_146297_k.func_147108_a((GuiScreen)null);
               break;
            case 1:
               this.field_146297_k.func_147108_a((GuiScreen)null);
         }
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      super.func_73869_a(typedChar, keyCode);
      this.doneBtn.field_146124_l = this.deckSizeTextField.func_146179_b().trim().length() > 0;
      this.deckSizeTextField.func_146180_a(this.deckSizeTextField.func_146179_b().trim());
      this.prizeCountTextField.func_146180_a(this.prizeCountTextField.func_146179_b().trim());
      this.timeLimitTextField.func_146180_a(this.timeLimitTextField.func_146179_b().trim());
      int number;
      if (!this.deckSizeTextField.func_146179_b().equals("")) {
         try {
            number = Integer.parseInt(this.deckSizeTextField.func_146179_b());
            if (number > 60) {
               number = 60;
            }

            this.deckSize = number;
         } catch (Exception var6) {
         }

         this.deckSizeTextField.func_146180_a(Integer.toString(this.deckSize));
      }

      if (!this.prizeCountTextField.func_146179_b().equals("")) {
         try {
            number = Integer.parseInt(this.prizeCountTextField.func_146179_b());
            if (number > 6) {
               number = 6;
            }

            if (number < 1) {
               number = 1;
            }

            this.prizeCount = number;
         } catch (Exception var5) {
         }

         this.prizeCountTextField.func_146180_a(Integer.toString(this.prizeCount));
      }

      if (!this.timeLimitTextField.func_146179_b().equals("")) {
         try {
            number = Integer.parseInt(this.timeLimitTextField.func_146179_b());
            if (number < 0) {
               number = 0;
            }

            this.timeLimit = number;
         } catch (Exception var4) {
         }

         this.timeLimitTextField.func_146180_a(Integer.toString(this.timeLimit));
      }

      if (keyCode != 28 && keyCode != 156) {
         if (keyCode == 1) {
            this.func_146284_a(this.cancelBtn);
         }
      } else {
         this.func_146284_a(this.doneBtn);
      }

   }

   public void func_73876_c() {
      super.func_73876_c();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.field_146297_k.field_71446_o.func_110577_a(background);
      func_146110_a(this.field_146294_l / 2 - 175, 0, 0.0F, 0.0F, 350, 350, 350.0F, 350.0F);
      super.func_73863_a(mouseX, mouseY, partialTicks);
      this.func_73732_a(this.field_146289_q, TextFormatting.BOLD + I18n.func_135052_a("battleRule.setRule", new Object[0]), this.field_146294_l / 2, 10, 16777215);
      this.func_73731_b(this.field_146289_q, I18n.func_135052_a("battleRule.deckSize", new Object[0]), this.field_146294_l / 2 - 150, 30, 16777215);
      this.func_73731_b(this.field_146289_q, I18n.func_135052_a("battleRule.prizeCount", new Object[0]), this.field_146294_l / 2 - 80, 30, 16777215);
      this.func_73731_b(this.field_146289_q, I18n.func_135052_a("battleRule.timeLimit", new Object[0]), this.field_146294_l / 2 - 10, 30, 16777215);
      this.func_73731_b(this.field_146289_q, I18n.func_135052_a("battlerule.setstartingcommands", new Object[0]), this.field_146294_l / 2 - 150, 76, 16777215);
      this.func_73731_b(this.field_146289_q, I18n.func_135052_a("battlerule.setendingcommands", new Object[0]), this.field_146294_l / 2 - 150, 141, 16777215);
   }
}

package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.BattleSpectatorUpdatePacket;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleSpectator;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class GuiBattleSpectator extends TCGGuiScreen {
   public static ResourceLocation background = new ResourceLocation("tcg", "gui/background.png");
   private int x;
   private int y;
   private int z;
   private int playerIndex;
   private GuiTextField xTextField;
   private GuiTextField yTextField;
   private GuiTextField zTextField;
   private GuiTextField playerIndexTextField;
   private final TileEntityBattleSpectator spectator;
   private TileEntityBattleController foundController;
   private GuiButton doneBtn;
   private GuiButton cancelBtn;
   private static final int DONE_BUTTON_ID = 0;
   private static final int CANCEL_BUTTON_ID = 1;

   public GuiBattleSpectator(TileEntityBattleSpectator spectator) {
      this.spectator = spectator;
      if (spectator.getControllerPosition() != null) {
         this.x = spectator.getControllerPosition().func_177958_n();
         this.y = spectator.getControllerPosition().func_177956_o();
         this.z = spectator.getControllerPosition().func_177952_p();
      }

      this.playerIndex = spectator.getPlayerIndex();
   }

   public void func_73866_w_() {
      Keyboard.enableRepeatEvents(true);
      this.field_146292_n.clear();
      this.textFieldList.clear();
      this.field_146292_n.add(this.doneBtn = new GuiButton(0, this.field_146294_l / 2 - 4 - 150, 210, 150, 20, I18n.func_135052_a("gui.done", new Object[0])));
      this.field_146292_n.add(this.cancelBtn = new GuiButton(1, this.field_146294_l / 2 + 4, 210, 150, 20, I18n.func_135052_a("gui.cancel", new Object[0])));
      this.textFieldList.add(this.xTextField = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - 150, 45, 45, 20));
      this.xTextField.func_146203_f(32767);
      this.xTextField.func_146195_b(false);
      this.xTextField.func_146180_a(Integer.toString(this.x));
      this.textFieldList.add(this.yTextField = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - 105, 45, 45, 20));
      this.yTextField.func_146203_f(32767);
      this.yTextField.func_146195_b(false);
      this.yTextField.func_146180_a(Integer.toString(this.y));
      this.textFieldList.add(this.zTextField = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - 60, 45, 45, 20));
      this.zTextField.func_146203_f(32767);
      this.zTextField.func_146195_b(false);
      this.zTextField.func_146180_a(Integer.toString(this.z));
      this.textFieldList.add(this.playerIndexTextField = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - 150, 95, 23, 20));
      this.playerIndexTextField.func_146203_f(32767);
      this.playerIndexTextField.func_146195_b(false);
      this.playerIndexTextField.func_146180_a(Integer.toString(this.playerIndex));
      super.func_73866_w_();
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146124_l) {
         switch (button.field_146127_k) {
            case 0:
               if (this.foundController != null) {
                  this.spectator.setControllerPosition(new BlockPos(this.x, this.y, this.z));
                  this.spectator.setPlayerIndex(this.playerIndex);
                  PacketHandler.net.sendToServer(new BattleSpectatorUpdatePacket(this.spectator));
                  this.field_146297_k.func_147108_a((GuiScreen)null);
               }
               break;
            case 1:
               this.field_146297_k.func_147108_a((GuiScreen)null);
         }
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      super.func_73869_a(typedChar, keyCode);
      this.xTextField.func_146180_a(this.xTextField.func_146179_b().trim());
      this.yTextField.func_146180_a(this.yTextField.func_146179_b().trim());
      this.zTextField.func_146180_a(this.zTextField.func_146179_b().trim());
      this.playerIndexTextField.func_146180_a(this.playerIndexTextField.func_146179_b().trim());
      if (!this.xTextField.func_146179_b().equals("")) {
         try {
            this.x = Integer.parseInt(this.xTextField.func_146179_b());
         } catch (Exception var7) {
         }
      }

      if (!this.yTextField.func_146179_b().equals("")) {
         try {
            this.y = Integer.parseInt(this.yTextField.func_146179_b());
         } catch (Exception var6) {
         }
      }

      if (!this.zTextField.func_146179_b().equals("")) {
         try {
            this.z = Integer.parseInt(this.zTextField.func_146179_b());
         } catch (Exception var5) {
         }
      }

      if (!this.playerIndexTextField.func_146179_b().equals("")) {
         try {
            int number = Integer.parseInt(this.playerIndexTextField.func_146179_b());
            if (number > 1) {
               number = 1;
            }

            if (number < 0) {
               number = 0;
            }

            this.playerIndex = number;
         } catch (Exception var4) {
         }

         this.playerIndexTextField.func_146180_a(Integer.toString(this.playerIndex));
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
      this.foundController = null;
      TileEntity te = this.spectator.func_145831_w().func_175625_s(new BlockPos(this.x, this.y, this.z));
      if (te != null && te instanceof TileEntityBattleController) {
         this.foundController = (TileEntityBattleController)te;
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.field_146297_k.field_71446_o.func_110577_a(background);
      func_146110_a(this.field_146294_l / 2 - 170, 0, 0.0F, 0.0F, 350, 350, 350.0F, 350.0F);
      super.func_73863_a(mouseX, mouseY, partialTicks);
      this.func_73732_a(this.field_146289_q, TextFormatting.BOLD + I18n.func_135052_a("battleSpectator.title", new Object[0]), this.field_146294_l / 2, 10, 16777215);
      this.func_73731_b(this.field_146289_q, I18n.func_135052_a("battleSpectator.controllerLocation", new Object[0]), this.field_146294_l / 2 - 150, 30, 16777215);
      this.func_73731_b(this.field_146289_q, I18n.func_135052_a("battleSpectator.playerIndex", new Object[0]), this.field_146294_l / 2 - 150, 80, 16777215);
      if (this.foundController != null) {
         this.func_73731_b(this.field_146289_q, I18n.func_135052_a("battleSpectator.foundController", new Object[0]), this.field_146294_l / 2 - 10, 50, 16777215);
      }

      if (this.spectator != null && this.spectator.getBattleController() != null) {
         TileEntityBattleController var4 = this.spectator.getBattleController();
      }

   }
}

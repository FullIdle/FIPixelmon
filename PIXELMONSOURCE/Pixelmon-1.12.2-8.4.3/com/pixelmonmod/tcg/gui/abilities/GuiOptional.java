package com.pixelmonmod.tcg.gui.abilities;

import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.gui.duel.GuiTCG;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.battles.CustomGUIChoiceToServerPacket;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;

public class GuiOptional extends TCGGuiScreen {
   private final TileEntityBattleController controller;
   public static final int BUTTON_YES = 1;
   public static final int BUTTON_NO = 2;
   private final GuiTCG parent;
   private GuiButton buttonYes = new GuiButton(1, 0, 30, 150, 20, "Yes, use effect");
   private GuiButton buttonNo = new GuiButton(2, 0, 50, 150, 20, "No, do nothing");

   public GuiOptional(GuiTCG parent, TileEntityBattleController controller) {
      this.parent = parent;
      this.controller = controller;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.add(this.buttonYes);
      this.field_146292_n.add(this.buttonNo);
      this.buttonYes.field_146128_h = this.field_146297_k.field_71443_c / 2 / this.parent.scaledFactor - 75;
      this.buttonNo.field_146128_h = this.field_146297_k.field_71443_c / 2 / this.parent.scaledFactor - 75;
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      PacketHandler.net.sendToServer(new CustomGUIChoiceToServerPacket(this.controller.func_174877_v(), true, new int[]{button.field_146127_k}));
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (this.visible) {
         String text = "§lActivate your attack's optional effect?";
         this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146297_k.field_71443_c / 2 / this.parent.scaledFactor - this.field_146297_k.field_71466_p.func_78256_a(text) / 2, 15, 16777215);
      }

   }
}

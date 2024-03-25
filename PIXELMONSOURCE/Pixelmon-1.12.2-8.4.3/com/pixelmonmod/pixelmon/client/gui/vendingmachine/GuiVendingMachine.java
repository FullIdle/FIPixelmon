package com.pixelmonmod.pixelmon.client.gui.vendingmachine;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.npc.ClientShopItem;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiShopScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.vendingMachine.VendingMachinePacket;
import java.io.IOException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;

public class GuiVendingMachine extends GuiShopScreen {
   BlockPos vendingMachineLocation;

   public GuiVendingMachine(BlockPos pos) {
      this.vendingMachineLocation = pos;
      this.allowMultiple = false;
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      this.handleMouseScroll();
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      this.clickBuyScreen(mouseX, mouseY);
   }

   protected void sendBuyPacket() {
      Pixelmon.network.sendToServer(new VendingMachinePacket(this.vendingMachineLocation, ((ClientShopItem)buyItems.get(this.selectedItem)).getItemID()));
   }

   public void func_73863_a(int mouseX, int mouseY, float f) {
      this.renderBuyScreen(mouseX, mouseY);
      int colour = 16777215;
      String buyLabel = I18n.func_74838_a("gui.shopkeeper.buy");
      this.func_73731_b(this.field_146297_k.field_71466_p, buyLabel, this.field_146294_l / 2 - 2 - this.field_146297_k.field_71466_p.func_78256_a(buyLabel) / 2, this.field_146295_m / 2 - 82, colour);
      colour = 16777215;
      if (mouseX > this.field_146294_l / 2 + 28 && mouseX < this.field_146294_l / 2 + 28 + 58 && mouseY > this.field_146295_m / 2 - 93 && mouseY < this.field_146295_m / 2 - 93 + 30) {
         colour = 13421772;
      }

      super.func_73863_a(mouseX, mouseY, f);
   }

   public boolean func_73868_f() {
      return false;
   }
}

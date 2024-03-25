package com.pixelmonmod.pixelmon.client.gui.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.trading.TradePair;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.comm.packetHandlers.AcceptNPCTradePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiTradeYesNo extends GuiScreen {
   public int top;
   public int left;
   int traderID = -1;
   TradePair pair = null;

   public GuiTradeYesNo(int traderId) {
      this.traderID = traderId;
      this.pair = ClientProxy.currentTradePair;
   }

   public void func_73863_a(int mouseX, int mouseY, float par3) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.yesNo);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 128), (double)(this.field_146295_m / 2 - 50), 256.0, 100.0F, 0.0, 0.0, 1.0, 0.78125, this.field_73735_i);
      String offer = I18n.func_135052_a("pixelmon." + this.pair.offer.name.toLowerCase() + ".name", new Object[0]);
      String e = I18n.func_135052_a("pixelmon." + this.pair.exchange.name.toLowerCase() + ".name", new Object[0]);
      String s = I18n.func_135052_a("gui.yesno.trade", new Object[]{e, offer});
      float textAreaWidth = 170.0F;
      float textWidth = (float)this.field_146297_k.field_71466_p.func_78256_a(s);
      int numLines = (int)(textWidth / textAreaWidth) + 1;
      this.field_146297_k.field_71466_p.func_78279_b(s, this.field_146294_l / 2 - 109, this.field_146295_m / 2 + 1 - numLines * 10 / 2, (int)textAreaWidth, 0);
      this.field_146297_k.field_71466_p.func_78279_b(s, this.field_146294_l / 2 - 110, this.field_146295_m / 2 - numLines * 10 / 2, (int)textAreaWidth, 16777215);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.yesNo);
      if (mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 - 33 && mouseY < this.field_146295_m / 2 - 7) {
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 63), (double)(this.field_146295_m / 2 - 33), 45.0, 26.0F, 0.6015625, 0.7890625, 0.77734375, 0.9921875, this.field_73735_i);
      }

      if (mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 + 5 && mouseY < this.field_146295_m / 2 + 31) {
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 63), (double)(this.field_146295_m / 2 + 5), 45.0, 26.0F, 0.6015625, 0.7890625, 0.77734375, 0.9921875, this.field_73735_i);
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.yesno.yes", new Object[0]), this.field_146294_l / 2 + 76, this.field_146295_m / 2 - 23, 16777215);
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.yesno.no", new Object[0]), this.field_146294_l / 2 + 80, this.field_146295_m / 2 + 15, 16777215);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      if (mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 - 33 && mouseY < this.field_146295_m / 2 - 7) {
         Pixelmon.network.sendToServer(new AcceptNPCTradePacket(this.traderID));
         Minecraft.func_71410_x().field_71439_g.func_71053_j();
      }

      if (mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 + 5 && mouseY < this.field_146295_m / 2 + 31) {
         Minecraft.func_71410_x().field_71439_g.func_71053_j();
      }

   }

   public boolean func_73868_f() {
      return true;
   }
}

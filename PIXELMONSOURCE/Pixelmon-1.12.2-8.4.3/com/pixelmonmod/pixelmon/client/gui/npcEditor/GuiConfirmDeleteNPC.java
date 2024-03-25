package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.DeleteNPCPacket;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiConfirmDeleteNPC extends GuiScreen {
   int entityId;

   public GuiConfirmDeleteNPC(int entityId) {
      this.entityId = entityId;
      this.field_146297_k = Minecraft.func_71410_x();
   }

   public void func_73866_w_() {
      int centerW = this.field_146294_l / 2;
      int centerH = this.field_146295_m / 2;
      this.func_189646_b(new GuiButton(1, centerW - 65, centerH - 10, I18n.func_135052_a("gui.yes", new Object[0]))).func_175211_a(60);
      this.func_189646_b(new GuiButton(2, centerW + 5, centerH - 10, I18n.func_135052_a("gui.no", new Object[0]))).func_175211_a(60);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      int centerW = this.field_146294_l / 2;
      int centerH = this.field_146295_m / 2;
      GuiHelper.drawCenteredString(I18n.func_135052_a("gui.confirmdeletenpc.delete", new Object[0]), (float)centerW, (float)(centerH - 60), 16777215, true);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146127_k == 1) {
         Pixelmon.network.sendToServer(new DeleteNPCPacket(this.entityId));
      }

      this.field_146297_k.func_147108_a((GuiScreen)null);
   }
}

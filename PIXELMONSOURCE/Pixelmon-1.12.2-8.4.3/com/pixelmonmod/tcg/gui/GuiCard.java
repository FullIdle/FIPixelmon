package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.helper.CardHelper;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.DisenchantPacket;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;

public class GuiCard extends TCGGuiScreen {
   static float spinCount = 0.0F;
   private CommonCardState card;
   private GuiButton disenchant;

   public GuiCard(ImmutableCard card) {
      this.card = new CommonCardState(card);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      this.disenchant = new GuiButton(1, this.field_146294_l / 2 - 30, 10, 60, 20, "Disenchant");
      this.field_146292_n.add(this.disenchant);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      CardHelper.drawCard(this.card, this.field_146297_k, this.field_146294_l, this.field_146295_m, this.field_73735_i, 1.0F, 0.0, (PlayerClientMyState)null, (PlayerClientOpponentState)null);
      this.drawEffectTooltip(this.card, mouseX, mouseY, this.field_146297_k.field_71466_p, 0, 0);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      switch (button.field_146127_k) {
         case 1:
            PacketHandler.net.sendToServer(new DisenchantPacket());
            if (this.field_146297_k.field_71439_g.func_184614_ca().func_190916_E() <= 1) {
               this.field_146297_k.field_71439_g.func_71053_j();
            }
         default:
            super.func_146284_a(button);
      }
   }

   public boolean func_73868_f() {
      return false;
   }
}

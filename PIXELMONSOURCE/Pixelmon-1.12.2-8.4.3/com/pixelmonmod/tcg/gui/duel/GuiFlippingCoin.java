package com.pixelmonmod.tcg.gui.duel;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.api.card.personalization.Coin;
import com.pixelmonmod.tcg.api.registries.CoinRegistry;
import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.battles.CoinFlipAckPacket;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class GuiFlippingCoin extends TCGGuiScreen {
   private static final int OK_BUTTON_ID = 1000;
   private final GuiTCG parent;
   private final TileEntityBattleController controller;
   private GuiButton okButton = new GuiButton(1000, 0, 0, 40, 20, "OK");

   public GuiFlippingCoin(GuiTCG parent, TileEntityBattleController controller) {
      this.parent = parent;
      this.controller = controller;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.add(this.okButton);
   }

   public void func_73876_c() {
      if (this.controller != null && this.controller.getClient() != null && this.controller.getClient().getCoinFlip() != null) {
         super.func_73876_c();
         ScaledResolution res = new ScaledResolution(this.field_146297_k);
         this.okButton.field_146125_m = !this.parent.isSpectating && this.controller.getClient().getPlayerIndex() == this.controller.getClient().getCoinFlip().getPlayerIndex();
         this.okButton.field_146128_h = res.func_78326_a() / 2 - 20;
         this.okButton.field_146129_i = res.func_78328_b() / 2 + 10;
      }
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      if (this.controller != null && this.controller.getClient() != null && this.controller.getClient().getCoinFlip() != null) {
         GameClientState client = this.controller.getClient();
         List results = client.getCoinFlip().getResults();
         if (results != null && !results.isEmpty()) {
            super.func_73863_a(mouseX, mouseY, partialTicks);
            ScaledResolution res = new ScaledResolution(this.field_146297_k);
            int size = 40;
            int space = 5;
            int start = res.func_78326_a() / 2 - (size * results.size() + space * (results.size() - 1)) / 2;
            Coin coin = CoinRegistry.PIKACHU;
            if (client.getPlayerIndex() == client.getCoinFlip().getPlayerIndex()) {
               coin = CoinRegistry.get(client.getMe().getCoinSetID());
            } else {
               CoinRegistry.get(client.getOpponent().getCoinSetID());
            }

            for(int i = 0; i < results.size(); ++i) {
               this.field_146297_k.field_71446_o.func_110577_a(results.get(i) == CoinSide.Head ? coin.getHeads() : coin.getTails());
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179141_d();
               GuiHelper.drawImageQuad((double)(start + i * (size + space)), (double)(res.func_78328_b() / 2 - size / 2 - 20), (double)size, (float)size, 0.0, 0.0, 1.0, 1.0, 0.0F);
            }

            if (this.controller.getClient().getGamePhase().after(GamePhase.PreMatch)) {
               String title = String.format("%s is flipping coins", this.controller.getClient().isMyTurn() ? this.controller.getClient().getMe().getPlayerName() : this.controller.getClient().getOpponent().getPlayerName());
               if (!this.parent.isSpectating && this.controller.getClient().isMyTurn()) {
                  title = "Flipping coins";
               }

               this.field_146289_q.func_175065_a(title, (float)(res.func_78326_a() / 2 - this.field_146289_q.func_78256_a(title) / 2), (float)(res.func_78328_b() / 2 - 55), 16777215, true);
            }
         }

      }
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      switch (button.field_146127_k) {
         case 1000:
            PacketHandler.net.sendToServer(new CoinFlipAckPacket(this.controller.func_174877_v()));
         default:
      }
   }
}

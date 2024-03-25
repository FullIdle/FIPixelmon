package com.pixelmonmod.pixelmon.client.gui.npc;

import com.pixelmonmod.pixelmon.api.trading.TradePair;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrader;
import java.io.IOException;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class GuiNPCTrader extends GuiScreen {
   protected int xSize = 176;
   protected int ySize = 166;
   NPCTrader trader = null;
   private int left;
   private int top;
   private TradePair pair;

   public GuiNPCTrader(int traderId) {
      Optional entityNPCOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, traderId, NPCTrader.class);
      if (entityNPCOptional.isPresent()) {
         this.trader = (NPCTrader)entityNPCOptional.get();
      }

      this.pair = ClientProxy.currentTradePair;
      this.field_146297_k = Minecraft.func_71410_x();
   }

   public void func_73863_a(int var2, int var3, float var1) {
      this.func_146276_q_();
      super.func_73863_a(var2, var3, var1);
      GuiHelper.bindPokemonSprite(this.pair.offer, this.field_146297_k);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 120), (double)(this.field_146295_m / 2 - 100), 86.0, 86.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      GuiHelper.bindPokemonSprite(this.pair.exchange, this.field_146297_k);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 40), (double)(this.field_146295_m / 2 - 100), 86.0, 86.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.npctrader.yours", new Object[0]), this.left + 145, this.top, 16777215);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.npctrader.their", new Object[0]), this.left - 20, this.top, 16777215);
      String s3 = "pixelmon." + this.pair.offer.name.toLowerCase() + ".name";
      if (this.pair.offer.shiny == Boolean.TRUE) {
         this.field_146297_k.field_71466_p.func_78276_b(TextFormatting.YELLOW + I18n.func_135052_a(s3, new Object[0]), this.left - 10, this.top + 70, 16777215);
      } else {
         this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a(s3, new Object[0]), this.left - 10, this.top + 70, 16777215);
      }

      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("pixelmon." + this.pair.exchange.name.toLowerCase() + ".name", new Object[0]), this.left + 155, this.top + 70, 16777215);
      if (this.pair.description != null) {
         this.field_146297_k.field_71466_p.func_78279_b(I18n.func_135052_a(this.pair.description, new Object[0]), this.left + 20, this.top + 110, 150, 16777215);
      }

   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.pair = ClientProxy.currentTradePair;
      this.trader.updateTradePair();
      this.left = (this.field_146294_l - this.xSize) / 2;
      this.top = (this.field_146295_m - this.ySize) / 2;
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
      if (ClientProxy.playerHasTradeRequestPokemon) {
         this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 25, this.field_146295_m / 2 - 50, 60, 20, I18n.func_135052_a("gui.trading.trade", new Object[0])));
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         Minecraft mc = Minecraft.func_71410_x();
         if (button.field_146127_k == 1) {
            mc.field_71439_g.func_71053_j();
            mc.func_175607_a(Minecraft.func_71410_x().field_71439_g);
         } else if (button.field_146127_k == 2) {
            mc.func_147108_a(new GuiTradeYesNo(this.trader.getId()));
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}

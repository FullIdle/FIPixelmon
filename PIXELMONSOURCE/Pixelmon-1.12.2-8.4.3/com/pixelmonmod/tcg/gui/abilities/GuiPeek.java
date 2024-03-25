package com.pixelmonmod.tcg.gui.abilities;

import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.gui.duel.GuiTCG;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.battles.CustomGUIChoiceToServerPacket;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;

public class GuiPeek extends TCGGuiScreen {
   private final TileEntityBattleController controller;
   public static final int BUTTON_TOP_MY_DECK = 1;
   public static final int BUTTON_TOP_OPP_DECK = 2;
   public static final int BUTTON_RANDOM_OPP_HAND = 3;
   public static final int BUTTON_ONE_MY_PRIZES = 4;
   public static final int BUTTON_ONE_OPP_PRIZES = 5;
   private final GuiTCG parent;
   private GuiButton buttonTopInMyDeck = new GuiButton(1, 0, 30, 150, 20, "Top of my deck");
   private GuiButton buttonTopInOppDeck = new GuiButton(2, 0, 50, 150, 20, "Top of opponent's deck");
   private GuiButton buttonRandomInOppHand = new GuiButton(3, 0, 70, 150, 20, "Random in opponent's hand");
   private GuiButton buttonOneInMyPrizes = new GuiButton(4, 0, 90, 150, 20, "One of my prizes");
   private GuiButton buttonOneInOppPrizes = new GuiButton(5, 0, 110, 150, 20, "One of opponent's prizes");

   public GuiPeek(GuiTCG parent, TileEntityBattleController controller) {
      this.parent = parent;
      this.controller = controller;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.add(this.buttonTopInMyDeck);
      this.field_146292_n.add(this.buttonTopInOppDeck);
      this.field_146292_n.add(this.buttonRandomInOppHand);
      this.field_146292_n.add(this.buttonOneInMyPrizes);
      this.field_146292_n.add(this.buttonOneInOppPrizes);
      this.buttonTopInMyDeck.field_146128_h = this.field_146297_k.field_71443_c / 2 / this.parent.scaledFactor - 75;
      this.buttonTopInOppDeck.field_146128_h = this.field_146297_k.field_71443_c / 2 / this.parent.scaledFactor - 75;
      this.buttonRandomInOppHand.field_146128_h = this.field_146297_k.field_71443_c / 2 / this.parent.scaledFactor - 75;
      this.buttonOneInMyPrizes.field_146128_h = this.field_146297_k.field_71443_c / 2 / this.parent.scaledFactor - 75;
      this.buttonOneInOppPrizes.field_146128_h = this.field_146297_k.field_71443_c / 2 / this.parent.scaledFactor - 75;
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      PacketHandler.net.sendToServer(new CustomGUIChoiceToServerPacket(this.controller.func_174877_v(), true, new int[]{button.field_146127_k}));
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (this.visible) {
         String text = "§l" + LanguageMapTCG.translateKey("ability.jg55.name");
         this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146297_k.field_71443_c / 2 / this.parent.scaledFactor - this.field_146297_k.field_71466_p.func_78256_a(text) / 2, 15, 16777215);
      }

   }
}

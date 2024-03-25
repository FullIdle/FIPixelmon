package com.pixelmonmod.tcg.gui.duel;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardBackRegistry;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.helper.CardHelper;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.battles.PrizeSelectorToServerPacket;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPrizeSelector extends TCGGuiScreen {
   public static final int TEXTURE_WIDTH = 108;
   public static final int TEXTURE_HEIGHT = 167;
   private final TileEntityBattleController controller;
   private final GuiTCG parent;
   private ImmutableCard[] prizes;

   public GuiPrizeSelector(GuiTCG parent, TileEntityBattleController controller) {
      this.parent = parent;
      this.controller = controller;
   }

   public ImmutableCard[] getPrizes() {
      return this.prizes;
   }

   public void setPrizes(ImmutableCard[] prizes) {
      this.prizes = prizes;
   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.prizes != null && !this.parent.isSpectating && Mouse.isButtonDown(0)) {
         if (this.controller.getClient().getMe().getPendingPrizeCount() > 0) {
            for(int index = 0; index < 6; ++index) {
               int row = index / 3;
               int col = index % 3 - 1;
               if (this.isHovering(this.field_146297_k.field_71443_c, this.field_146297_k.field_71440_d, row, col)) {
                  PacketHandler.net.sendToServer(new PrizeSelectorToServerPacket(this.controller.func_174877_v(), index));
               }
            }
         } else {
            this.controller.getShowingPrizes().clear();
         }
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      GlStateManager.func_179140_f();
      if (this.prizes != null) {
         Map showing = this.controller.getShowingPrizes();
         int w = this.field_146297_k.field_71443_c;
         int h = this.field_146297_k.field_71440_d;

         for(int i = 0; i < 6; ++i) {
            int row = i / 3;
            int col = i % 3 - 1;
            if (i < this.prizes.length) {
               if (showing.containsKey(i)) {
                  double factor = 0.5;
                  if (this.isHovering(w, h, row, col)) {
                     factor = 1.0;
                  }

                  factor = Math.min(1.2, factor * (double)(2.0F / (float)this.parent.scaledFactor));
                  GL11.glPushMatrix();
                  GL11.glScaled(factor, factor, 1.0);
                  GL11.glTranslated((double)(w / 2 - 54 + col * 118 + 54) / factor / (double)this.parent.scaledFactor, (double)(h / 2 - 167 - 5 + row * 177 + 83) / factor / (double)this.parent.scaledFactor, 1.0);
                  CardHelper.drawCard(new CommonCardState((ImmutableCard)showing.get(i)), this.field_146297_k, 0, 0, 0.0F, 1.0F, 0.0, this.controller.getClient().getMe(), this.controller.getClient().getOpponent());
                  GlStateManager.func_179140_f();
                  GL11.glPopMatrix();
               } else if (this.prizes[i] != null) {
                  this.field_146297_k.field_71446_o.func_110577_a(CardBackRegistry.STANDARD.getFile());
                  if (this.isHovering(w, h, row, col)) {
                     GuiHelper.drawImageQuad((double)((w / 2 - 54 + col * 118 - 2) / this.parent.scaledFactor), (double)((h / 2 - 167 - 5 + row * 177 - 2) / this.parent.scaledFactor), (double)(112 / this.parent.scaledFactor), (float)(171 / this.parent.scaledFactor), 0.0, 0.0, 1.0, 1.0, 0.0F);
                  } else {
                     GuiHelper.drawImageQuad((double)((w / 2 - 54 + col * 118) / this.parent.scaledFactor), (double)((h / 2 - 167 - 5 + row * 177) / this.parent.scaledFactor), (double)(108 / this.parent.scaledFactor), (float)(167 / this.parent.scaledFactor), 0.0, 0.0, 1.0, 1.0, 0.0F);
                  }
               }
            }
         }

         String text = "";
         if (this.controller.getClient().getMe().getPendingPrizeCount() > 0) {
            text = "§l" + I18n.func_135052_a("battle.selector.prize", new Object[0]);
         } else {
            text = "§l" + I18n.func_135052_a("battle.selector.prize.close", new Object[0]);
         }

         this.field_146297_k.field_71466_p.func_175065_a(text, (float)(w / 2 / this.parent.scaledFactor - this.field_146297_k.field_71466_p.func_78256_a(text) / 2), (float)((h / 2 - 167 - 40) / this.parent.scaledFactor), 16777215, true);
      }

   }

   boolean isHovering(int w, int h, int row, int col) {
      int minX = w / 2 - 54 + col * 118;
      int minY = h / 2 - 167 - 5 + row * 177;
      return Mouse.getX() >= minX && Mouse.getX() <= minX + 108 && h - Mouse.getY() >= minY && h - Mouse.getY() <= minY + 167;
   }
}

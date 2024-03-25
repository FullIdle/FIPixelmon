package com.pixelmonmod.tcg.gui.duel;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.gui.GuiTarget;
import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.gui.enums.CardSelectorPurpose;
import com.pixelmonmod.tcg.helper.CardHelper;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.battles.CardSelectorToServerPacket;
import com.pixelmonmod.tcg.network.packets.battles.RetreatAndSwitchPacket;
import com.pixelmonmod.tcg.network.packets.battles.SwitchPacket;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class CardSelector extends TCGGuiScreen {
   private final int SCROLL_SPEED = 70;
   private final int BUTTON_ACCEPT_ID = 101;
   private final int BUTTON_CANCEL_ID = 102;
   private final GuiTCG parent;
   private CardSelectorState data = null;
   private CardSelectorPurpose purpose = null;
   private CardWithLocation renderLast = null;
   private int renderLastPos = -1;
   private TileEntityBattleController controller;
   private boolean[] isSelectedAtPosition;
   private int scrollModifier = 0;
   private boolean mousePressed = false;
   private boolean isMouseInGrid = false;
   private boolean tooFarLeft = false;
   private boolean tooFarRight = false;
   private boolean canScroll = true;
   private GuiButton acceptButton = new GuiButton(101, 0, 0, 40, 20, "OK");
   private GuiButton cancelButton = new GuiButton(102, 0, 0, 40, 20, "Cancel");

   public CardSelector(GuiTCG parent, TileEntityBattleController controller) {
      this.parent = parent;
      this.controller = controller;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      if (!this.parent.isSpectating) {
         this.field_146292_n.add(this.acceptButton);
         this.field_146292_n.add(this.cancelButton);
      }

   }

   public void func_73876_c() {
      if (this.data != null) {
         super.func_73876_c();
         ScaledResolution res = new ScaledResolution(this.field_146297_k);
         this.handleMouseWheel();
         this.cancelButton.field_146125_m = this.data.isCancellable();
         this.acceptButton.field_146124_l = this.getSelectionCount() >= this.data.getMinimumCount();
         this.acceptButton.field_146128_h = res.func_78326_a() / 2 - (this.data.isCancellable() ? 45 : 20);
         this.acceptButton.field_146129_i = res.func_78328_b() / 2 + 50;
         this.cancelButton.field_146128_h = res.func_78326_a() / 2 + (this.data.getMaximumCount() > 0 ? 5 : -20);
         this.cancelButton.field_146129_i = res.func_78328_b() / 2 + 50;
      }
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      if (this.data != null) {
         super.func_73863_a(mouseX, mouseY, partialTicks);
         GlStateManager.func_179140_f();
         ScaledResolution res = new ScaledResolution(this.field_146297_k);
         List cardList = this.data.getCardList();
         String hint = this.data.getCustomText() == null ? null : I18n.func_74838_a(this.data.getCustomText());
         if (hint == null) {
            if (this.data.getMaximumCount() > 0) {
               String rangeText = this.data.getMaximumCount() + "";
               if (this.data.getMaximumCount() > this.data.getMinimumCount()) {
                  rangeText = this.data.getMinimumCount() + "-" + this.data.getMaximumCount();
               }

               String suffix = this.data.getMaximumCount() > 1 ? (this.data.isCountEnergy() ? " energies" : " cards") : (this.data.isCountEnergy() ? " energy" : " card");
               hint = this.data.getDisplayType().toString() + " " + rangeText + suffix;
            } else if (this.data.getDisplayType() == CardSelectorDisplay.Reveal) {
               hint = String.format("%s reveals those cards", this.controller.getClient().getOpponent().getPlayerName());
            }
         }

         if (hint != null) {
            this.field_146297_k.field_71466_p.func_175065_a("§l" + hint, (float)(res.func_78326_a() / 2 - this.field_146297_k.field_71466_p.func_78256_a(hint) / 2), (float)(res.func_78328_b() / 2 - 65), 16777215, true);
         }

         GameClientState client = this.controller.getClient();
         PlayerClientMyState me = client.getMe();
         PlayerClientOpponentState opp = client.getOpponent();
         int size = cardList.size();
         boolean isMouseOut = true;

         int cardX;
         for(int cardIndex = 0; cardIndex < size; ++cardIndex) {
            CardWithLocation card = (CardWithLocation)cardList.get(cardIndex);
            GuiTarget target = this.getGuiTarget(cardIndex, size, res);
            this.isMouseInGrid = target.isInside(mouseX, mouseY);
            float scaleFactor = 0.5F;
            if (this.isMouseInGrid) {
               this.renderLast = card;
               this.renderLastPos = cardIndex;
               isMouseOut = false;
               this.canScroll = false;
            } else {
               this.canScroll = true;
               GL11.glPushMatrix();
               GL11.glScaled((double)scaleFactor, (double)scaleFactor, (double)scaleFactor);
               cardX = (int)((float)(res.func_78326_a() * 2 + cardIndex * 250 - (size - 1) * 125 + this.scrollModifier) / (scaleFactor * 2.0F));
               int cardY = (int)((float)(res.func_78328_b() * 2) / (scaleFactor * 2.0F));
               float cardZ = this.isMouseInGrid ? this.field_73735_i + 0.1F : this.field_73735_i;
               float opacity = this.getCardOpacity(cardIndex);
               if (card == null) {
                  CardHelper.drawCardBack(this.field_146297_k, cardX, cardY, cardZ - 0.1F, (PlayerCommonState)me);
               } else {
                  CardHelper.drawCard(card.getCard(), this.field_146297_k, cardX, cardY, cardZ - 0.1F, opacity, 0.0, me, opp);
                  GlStateManager.func_179140_f();
               }

               GlStateManager.func_179140_f();
               GL11.glScaled((double)(-scaleFactor), (double)(-scaleFactor), (double)(-scaleFactor));
               GL11.glPopMatrix();
               String title = "";
               if (card == null) {
                  title = BoardLocation.Deck.toString();
               } else if (card.getLocation() != null) {
                  title = card.getLocation().toString();
               }

               this.field_146297_k.field_71466_p.func_175065_a(title, (float)((int)((float)cardX * scaleFactor / 2.0F) - this.field_146297_k.field_71466_p.func_78256_a(title) / 2), (float)(res.func_78328_b() / 2 - 51), 16777215, true);
               if (this.isSelectedAtPosition[cardIndex]) {
                  this.drawSelectionOverlay(target, scaleFactor, 0.9F);
               }

               if (cardIndex == 0) {
                  this.tooFarLeft = cardX > 285;
               }

               if (cardIndex == size - 1) {
                  this.tooFarRight = cardX < this.field_146294_l + 1180;
               }
            }

            if (!this.parent.isSpectating && Mouse.isButtonDown(0)) {
               if (this.isMouseInGrid && !this.mousePressed) {
                  if (this.getSelectionCount() == this.getData().getMaximumCount() && !this.isSelectedAtPosition[cardIndex]) {
                     if (this.getData().getMaximumCount() == 1) {
                        this.isSelectedAtPosition = new boolean[this.isSelectedAtPosition.length];
                        this.isSelectedAtPosition[cardIndex] = true;
                     }
                  } else {
                     this.isSelectedAtPosition[cardIndex] = !this.isSelectedAtPosition[cardIndex];
                  }

                  this.mousePressed = true;
               }
            } else if (!Mouse.isButtonDown(0) && this.mousePressed) {
               this.mousePressed = false;
            }
         }

         if (isMouseOut) {
            this.renderLast = null;
            this.renderLastPos = -1;
         }

         if (this.renderLast != null || this.renderLastPos != -1) {
            GuiTarget target = this.getGuiTarget(this.renderLastPos, size, res);
            this.isMouseInGrid = target.isInside(mouseX, mouseY);
            float scaleFactor = 0.5F;
            if (this.mousePressed && this.isMouseInGrid) {
               scaleFactor = 0.9F;
            } else if (this.isMouseInGrid) {
               scaleFactor = 1.0F;
            }

            float opacity = this.getCardOpacity(this.renderLastPos);
            GL11.glPushMatrix();
            GL11.glScaled((double)scaleFactor, (double)scaleFactor, (double)scaleFactor);
            int cardX = (int)((float)(res.func_78326_a() * 2 + this.renderLastPos * 250 - (size - 1) * 125 + this.scrollModifier) / (scaleFactor * 2.0F));
            cardX = (int)((float)(res.func_78328_b() * 2) / (scaleFactor * 2.0F));
            float cardZ = this.isMouseInGrid ? this.field_73735_i + 0.1F : this.field_73735_i;
            if (this.renderLast == null) {
               CardHelper.drawCardBack(this.field_146297_k, cardX, cardX, cardZ - 0.1F, (PlayerCommonState)me);
            } else {
               CardHelper.drawCard(this.renderLast.getCard(), this.field_146297_k, cardX, cardX, cardZ - 0.1F, opacity, 0.0, me, opp);
               GlStateManager.func_179140_f();
            }

            GlStateManager.func_179140_f();
            GL11.glScaled((double)(-scaleFactor), (double)(-scaleFactor), (double)(-scaleFactor));
            GL11.glPopMatrix();
            if (this.isSelectedAtPosition[this.renderLastPos]) {
               this.drawSelectionOverlay(target, scaleFactor, 0.5F);
            }

            String title = null;
            if (this.renderLast == null) {
               title = "Deck";
            } else if (this.renderLast.getLocation() != null) {
               title = this.renderLast.getLocation().toString();
            }

            this.field_146297_k.field_71466_p.func_175065_a(title, (float)((int)((float)cardX * scaleFactor / 2.0F) - this.field_146297_k.field_71466_p.func_78256_a(title) / 2), (float)(res.func_78328_b() / 2 - 93), 16777215, true);
         }

      }
   }

   private void drawSelectionOverlay(GuiTarget target, float scaleFactor, float opacity) {
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      switch (this.data.getDisplayType()) {
         case Discard:
            this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/discard.png"));
            GlStateManager.func_179131_c(0.0F, 0.0F, 0.0F, opacity / 2.0F);
            GuiHelper.drawImageQuad((double)((float)(target.getX() + target.getWidth() / 2) - 24.0F * scaleFactor) + 0.5, (double)((float)(target.getY() + target.getHeight() / 2) - 24.0F * scaleFactor) + 0.5, (double)(48.0F * scaleFactor), 48.0F * scaleFactor, 0.0, 0.0, 1.0, 1.0, this.field_73735_i + 0.2F);
            GlStateManager.func_179131_c(1.0F, 0.0F, 0.0F, opacity);
            GuiHelper.drawImageQuad((double)((float)(target.getX() + target.getWidth() / 2) - 24.0F * scaleFactor), (double)((float)(target.getY() + target.getHeight() / 2) - 24.0F * scaleFactor), (double)(48.0F * scaleFactor), 48.0F * scaleFactor, 0.0, 0.0, 1.0, 1.0, this.field_73735_i + 0.2F);
            break;
         case Draw:
         case Select:
            this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/cards/select.png"));
            GlStateManager.func_179131_c(0.0F, 0.0F, 0.0F, opacity / 2.0F);
            GuiHelper.drawImageQuad((double)((float)(target.getX() + target.getWidth() / 2) - 24.0F * scaleFactor) + 0.5, (double)((float)(target.getY() + target.getHeight() / 2) - 24.0F * scaleFactor) + 0.5, (double)(48.0F * scaleFactor), 48.0F * scaleFactor, 0.0, 0.0, 1.0, 1.0, this.field_73735_i + 0.2F);
            GlStateManager.func_179131_c(0.2F, 0.3F, 1.0F, opacity);
            GuiHelper.drawImageQuad((double)((float)(target.getX() + target.getWidth() / 2) - 24.0F * scaleFactor), (double)((float)(target.getY() + target.getHeight() / 2) - 24.0F * scaleFactor), (double)(48.0F * scaleFactor), 48.0F * scaleFactor, 0.0, 0.0, 1.0, 1.0, this.field_73735_i + 0.2F);
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179084_k();
      GlStateManager.func_179118_c();
   }

   private GuiTarget getGuiTarget(int cardIndex, int size, ScaledResolution res) {
      int sizeModXA = -26;
      int sizeModXB = 27;
      int sizeModYA = -42;
      int sizeModYB = 40;
      return new GuiTarget(res.func_78326_a() / 2 + cardIndex * 250 / 4 - (size - 1) * 125 / 4 + this.scrollModifier / 4 - sizeModXB, res.func_78328_b() / 2 - sizeModYB, sizeModXB - sizeModXA, sizeModYB - sizeModYA, 0.0F);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (this.data != null) {
         super.func_146284_a(button);
         switch (button.field_146127_k) {
            case 101:
               this.scrollModifier = 0;
               this.acceptSelection();
               break;
            case 102:
               this.scrollModifier = 0;
               this.set((CardSelectorPurpose)null, (CardSelectorState)null);
         }

      }
   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      if (this.data != null) {
         super.func_73869_a(typedChar, keyCode);
      }
   }

   public void set(CardSelectorPurpose purpose, CardSelectorState data) {
      if (data != null || this.data == null || this.data.getDisplayType() != CardSelectorDisplay.Reveal) {
         CardSelectorState oldData = this.data;
         this.purpose = purpose;
         this.data = data;
         if (data == null && oldData != null || data != null && oldData == null || data != null && oldData != null && oldData.getId() != data.getId()) {
            this.initialize();
            if (purpose == CardSelectorPurpose.Trainer) {
               PacketHandler.net.sendToServer(new CardSelectorToServerPacket(this.controller.func_174877_v(), true, (boolean[])null));
            }
         }

      }
   }

   public CardSelectorState getData() {
      return this.data;
   }

   public CardSelectorPurpose getPurpose() {
      return this.purpose;
   }

   public void initialize() {
      this.isSelectedAtPosition = null;
      if (this.data != null) {
         this.isSelectedAtPosition = new boolean[this.data.getCardList().size()];
      }

   }

   public List getSelectedCards() {
      List cards = new ArrayList();

      for(int i = 0; i < this.data.getCardList().size(); ++i) {
         if (this.isSelectedAtPosition[i]) {
            cards.add(((CardWithLocation)this.data.getCardList().get(i)).getCard());
         }
      }

      return cards;
   }

   void acceptSelection() {
      if (this.data.getDisplayType() == CardSelectorDisplay.Reveal) {
         this.data = null;
         this.purpose = null;
      } else {
         int selectionCount = this.getSelectionCount();
         int i;
         switch (this.purpose) {
            case Trainer:
               if (selectionCount <= this.data.getMaximumCount() && selectionCount >= this.data.getMinimumCount()) {
                  PacketHandler.net.sendToServer(new CardSelectorToServerPacket(this.controller.func_174877_v(), true, this.isSelectedAtPosition));
               }
               break;
            case StartRetreating:
               if (selectionCount >= this.data.getMinimumCount()) {
                  this.controller.energySelectionForRetreat = this.isSelectedAtPosition;
               }

               PlayerClientMyState me = this.controller.getClient().getMe();
               CardSelectorState state = SelectorHelper.generateSelectorForBench(me, "battle.selector.newactive");
               state.setCancellable(true);
               this.set(CardSelectorPurpose.FinishRetreating, state);
               break;
            case FinishRetreating:
               for(i = 0; i < this.data.getCardList().size(); ++i) {
                  if (this.isSelectedAtPosition[i]) {
                     PacketHandler.net.sendToServer(new RetreatAndSwitchPacket(this.controller.func_174877_v(), this.controller.energySelectionForRetreat, ((CardWithLocation)this.data.getCardList().get(i)).getLocationSubIndex()));
                  }
               }

               this.controller.energySelectionForRetreat = null;
               this.set((CardSelectorPurpose)null, (CardSelectorState)null);
               break;
            case KnockoutSwitch:
               for(i = 0; i < this.data.getCardList().size(); ++i) {
                  if (this.isSelectedAtPosition[i]) {
                     PacketHandler.net.sendToServer(new SwitchPacket(this.controller.func_174877_v(), ((CardWithLocation)this.data.getCardList().get(i)).getLocationSubIndex()));
                  }
               }

               this.set((CardSelectorPurpose)null, (CardSelectorState)null);
         }

      }
   }

   private int getSelectionCount() {
      int selectionCount = 0;

      for(int i = 0; i < this.isSelectedAtPosition.length; ++i) {
         if (this.isSelectedAtPosition[i]) {
            ++selectionCount;
            if (this.data.isCountEnergy() && ((CardWithLocation)this.data.getCardList().get(i)).getCard().getSecondaryEnergy() != null) {
               ++selectionCount;
            }
         }
      }

      return selectionCount;
   }

   private float getCardOpacity(int pos) {
      return 1.0F;
   }

   private void handleMouseWheel() {
      int mousewheelDirection = Mouse.getDWheel();
      if ((mousewheelDirection >= 120 || mousewheelDirection <= -120) && this.canScroll && (!this.tooFarRight || mousewheelDirection > -120) && (!this.tooFarLeft || mousewheelDirection < 120)) {
         this.scrollModifier += 70 * mousewheelDirection / 120;
      }

   }
}

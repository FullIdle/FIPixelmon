package com.pixelmonmod.tcg.gui.duel;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.camera.EntityCamera;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.api.registries.CoinRegistry;
import com.pixelmonmod.tcg.block.renderers.RenderBattleController;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.duel.RenderDuel;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.trainer.BaseTrainerEffect;
import com.pixelmonmod.tcg.gui.GuiPlayerTargets;
import com.pixelmonmod.tcg.gui.GuiTarget;
import com.pixelmonmod.tcg.gui.abilities.GuiOptional;
import com.pixelmonmod.tcg.gui.abilities.GuiPeek;
import com.pixelmonmod.tcg.gui.base.TCGGuiScreenChattable;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.gui.enums.CardSelectorPurpose;
import com.pixelmonmod.tcg.helper.BattleHelper;
import com.pixelmonmod.tcg.helper.CardHelper;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.GenericGUIPacket;
import com.pixelmonmod.tcg.network.packets.battles.GenericActionRequestPacket;
import com.pixelmonmod.tcg.network.packets.battles.UpdateServerCardRecordPacket;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import com.pixelmonmod.tcg.network.packets.enums.PhaseAction;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiTCG extends TCGGuiScreenChattable {
   public static final String GUI_PEEK_ID = "GUI_PEEK";
   public static final String GUI_OPTIONAL_ID = "GUI_OPTIONAL";
   private final int BUTTON_ENDTURN_ID = 10;
   private final int BUTTON_DISCARD_ID = 11;
   public TileEntityBattleController controller;
   private int playerIndex;
   private boolean isCardInSelectMode = false;
   private CardWithLocation draggingCard = null;
   private InspectingCard inspectingCard;
   private CardSelector cardSelector;
   private GuiFlippingCoin guiFlippingCoin;
   private GuiDuelLog guiDuelLog;
   private GuiPrizeSelector guiPrizeSelector;
   boolean isSpectating = false;
   private int ticks = 0;
   public double guiScaling;
   public int scaledWidth;
   public int scaledHeight;
   public int scaledFactor;
   private Set subGui = new HashSet();
   private GuiPlayerTargets myTargets;
   private GuiPlayerTargets oppTargets;
   private GuiButton buttonEndTurn = new GuiButton(10, 0, 0, 80, 20, "End turn");
   private GuiButton buttonDiscard = new GuiButton(11, 0, 0, 80, 20, "Discard");
   private GuiTarget buttonAbility;
   private GuiTarget[] buttonAttacks;
   private GuiTarget buttonRetreat;
   private GuiPeek guiPeek;
   private GuiOptional guiOptional;
   private Cursor blankCursor;

   public GuiTCG(TileEntityBattleController controller, int playerIndex, boolean isSpectating) {
      this.controller = controller;
      this.playerIndex = playerIndex;
      this.isSpectating = isSpectating;
      this.inspectingCard = new InspectingCard(this);
      this.cardSelector = new CardSelector(this, controller);
      this.guiFlippingCoin = new GuiFlippingCoin(this, controller);
      this.guiDuelLog = new GuiDuelLog(this);
      this.guiPrizeSelector = new GuiPrizeSelector(this, controller);
      this.guiPeek = new GuiPeek(this, controller);
      this.guiPeek.visible = false;
      this.guiOptional = new GuiOptional(this, controller);
      this.guiOptional.visible = false;
      this.myTargets = new GuiPlayerTargets();
      this.oppTargets = new GuiPlayerTargets();
      this.buttonAbility = new GuiTarget(0.0F);
      this.buttonAttacks = new GuiTarget[3];

      for(int i = 0; i < this.buttonAttacks.length; ++i) {
         this.buttonAttacks[i] = new GuiTarget(0.0F);
      }

      this.buttonRetreat = new GuiTarget(0.0F);

      try {
         this.blankCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), (IntBuffer)null);
      } catch (LWJGLException var5) {
      }

   }

   public InspectingCard getInspectingCard() {
      return this.inspectingCard;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.updateLocalDimensions();
      this.field_146292_n.add(this.buttonEndTurn);
      this.field_146292_n.add(this.buttonDiscard);
      this.buttonDiscard.field_146125_m = false;
      this.subGui.clear();
      this.subGui.add(this.cardSelector);
      this.subGui.add(this.guiFlippingCoin);
      this.subGui.add(this.guiPrizeSelector);
      this.subGui.add(this.guiPeek);
      this.subGui.add(this.guiOptional);
      this.subGui.forEach(GuiScreen::func_73866_w_);
      this.updateTargetPositions();
      Minecraft.func_71410_x().field_71474_y.field_74319_N = true;
      GuiIngameForge.renderObjective = false;

      try {
         Mouse.setNativeCursor(this.blankCursor);
      } catch (LWJGLException var2) {
      }

   }

   public void func_146281_b() {
      super.func_146281_b();
      Minecraft.func_71410_x().field_71474_y.field_74319_N = false;
      Minecraft.func_71410_x().field_71474_y.field_74335_Z = TCGConfig.getInstance().savedUIScale;
      GuiIngameForge.renderObjective = true;
      BattleHelper.resetViewEntity();
      if (!this.isSpectating) {
         PacketHandler.net.sendToServer(new GenericGUIPacket(GenericGUIPacket.GUITypes.Battle, false, this.controller.func_174877_v()));
      } else {
         PacketHandler.net.sendToServer(new GenericGUIPacket(GenericGUIPacket.GUITypes.Spectate, false, this.controller.func_174877_v()));
      }

      this.controller = null;

      try {
         Mouse.setNativeCursor((Cursor)null);
      } catch (LWJGLException var2) {
      }

   }

   public void func_73876_c() {
      if (this.controller == null) {
         this.field_146297_k.field_71439_g.func_71053_j();
      }

      super.func_73876_c();
      if (this.ticks == 0) {
         this.updateCameraPositions(RenderBattleController.getFacing(this.controller));
      }

      BattleHelper.setViewEntity(ClientProxy.camera);
      if (this.controller != null && this.controller.getClient() != null && this.controller.getClient().getMe() != null) {
         GameClientState client = this.controller.getClient();
         PlayerClientMyState me = client.getMe();
         PlayerClientOpponentState opp = client.getOpponent();
         if (me.getPendingPrizeCount() <= 0 && this.controller.getShowingPrizes().isEmpty()) {
            this.guiPrizeSelector.setPrizes((ImmutableCard[])null);
         } else if (me.getPendingPrizePlayerIndex() == client.getPlayerIndex()) {
            this.guiPrizeSelector.setPrizes(me.getPrizeCards());
         } else {
            this.guiPrizeSelector.setPrizes(opp.getPrizeCards());
         }

         float width;
         if (this.inspectingCard != null && this.inspectingCard.getCard() != null) {
            CommonCardState card = this.inspectingCard.getCard();
            if (card instanceof PokemonCardState) {
               PokemonCardState pokemon = (PokemonCardState)card;
               int line = 0;
               if (pokemon.getAbility() != null) {
                  line = 3;
                  if (pokemon.getData().getAttacks().length == 1 && pokemon.getData().getAttacks()[0].getText() == null) {
                     line = 5;
                  }
               }

               int[] attackLines = CardHelper.calculateAttackDescriptionLines(pokemon.getAttacksStatus(), pokemon.getAbility() != null, 94, this.field_146289_q);

               for(int i = 0; i < attackLines.length; ++i) {
                  width = 9.0F;
                  int offset = i > 0 && attackLines[i - 1] == 0 ? 2 : 0;
                  int height = 12 - offset;
                  if (attackLines[i] == 0 && i < attackLines.length - 1) {
                     height -= 2;
                  }

                  this.buttonAttacks[i].setHeight(height);
                  this.buttonAttacks[i].setX((int)((double)(this.scaledWidth / 2) + (double)(this.inspectingCard.isPickingAttack() ? 0 : 150) * this.guiScaling - 56.0));
                  this.buttonAttacks[i].setY((int)((float)(this.scaledHeight / 2) + -2.0F + (float)line * width + (float)offset));
                  line += attackLines[i] + 1;
               }
            }
         }

         if (me.getActiveCard() == null && client.getGamePhase().after(GamePhase.PreMatch) && me.hasPrizeLeft() && opp.hasPrizeLeft() && this.guiPrizeSelector.getPrizes() == null) {
            if (this.cardSelector.getPurpose() == null) {
               CardSelectorState state = SelectorHelper.generateSelectorForBench(me, "battle.selector.newactive");
               this.cardSelector.set(CardSelectorPurpose.KnockoutSwitch, state);
            }
         } else if (me.getActiveCard() != null && this.cardSelector.getPurpose() == CardSelectorPurpose.KnockoutSwitch) {
            this.cardSelector.set((CardSelectorPurpose)null, (CardSelectorState)null);
         } else if (me.getCardSelectorState() != null) {
            this.cardSelector.set(CardSelectorPurpose.Trainer, this.controller.getClient().getMe().getCardSelectorState());
            this.inspectingCard.clear();
         } else if (this.cardSelector.getPurpose() == CardSelectorPurpose.Trainer) {
            this.cardSelector.set((CardSelectorPurpose)null, (CardSelectorState)null);
         }

         if ((this.cardSelector.getPurpose() == CardSelectorPurpose.FinishRetreating || this.cardSelector.getPurpose() == CardSelectorPurpose.StartRetreating) && this.cardSelector.getData().isCancellable() && !client.isMyTurn()) {
            this.cardSelector.set((CardSelectorPurpose)null, (CardSelectorState)null);
         }

         if (me.isChoosingOppAttack()) {
            this.inspectingCard.set(opp.getActiveCard(), false, BoardLocation.Active, 0, true, "Choose an attack", true);
         } else if (this.inspectingCard.isPickingAttack()) {
            this.inspectingCard.clear();
         }

         ArrayList handTargets;
         int i;
         if (this.myTargets.handTargets.size() != me.getHand().size()) {
            handTargets = new ArrayList();

            for(i = 0; i < me.getHand().size(); ++i) {
               handTargets.add(new GuiTarget(0.0F));
            }

            this.myTargets.handTargets = handTargets;
         }

         int i;
         double cardOffset;
         double handOffset;
         for(i = 0; i < this.myTargets.handTargets.size(); ++i) {
            cardOffset = 12.6 * (double)i;
            handOffset = -5.5 * (double)this.myTargets.handTargets.size() - 10.0;
            width = i < this.myTargets.handTargets.size() - 1 ? 12.6F : 26.0F;
            ((GuiTarget)this.myTargets.handTargets.get(i)).setWidth((int)((double)width * this.guiScaling));
            ((GuiTarget)this.myTargets.handTargets.get(i)).setHeight((int)(40.0 * this.guiScaling));
            ((GuiTarget)this.myTargets.handTargets.get(i)).setX(this.scaledWidth / 2 + (int)((handOffset + cardOffset) * this.guiScaling));
            ((GuiTarget)this.myTargets.handTargets.get(i)).setY(this.scaledHeight - (int)(42.0 * this.guiScaling));
         }

         if (this.oppTargets.handTargets.size() != opp.getHand().size()) {
            handTargets = new ArrayList();

            for(i = 0; i < opp.getHand().size(); ++i) {
               handTargets.add(new GuiTarget(0.0F));
            }

            this.oppTargets.handTargets = handTargets;
         }

         for(i = 0; i < this.oppTargets.handTargets.size(); ++i) {
            cardOffset = 12.6 * (double)i;
            handOffset = -5.5 * (double)this.oppTargets.handTargets.size() - 10.0;
            width = i < this.oppTargets.handTargets.size() - 1 ? 12.6F : 26.0F;
            ((GuiTarget)this.oppTargets.handTargets.get(i)).setWidth((int)((double)width * this.guiScaling));
            ((GuiTarget)this.oppTargets.handTargets.get(i)).setHeight((int)(40.0 * this.guiScaling));
            ((GuiTarget)this.oppTargets.handTargets.get(i)).setX(this.scaledWidth / 2 + (int)((handOffset + cardOffset) * this.guiScaling));
            ((GuiTarget)this.oppTargets.handTargets.get(i)).setY((int)(-33.0 * this.guiScaling));
         }

         this.buttonEndTurn.field_146125_m = this.cardSelector.getData() == null && client.getCoinFlip() == null && !this.isSpectating;
         this.buttonEndTurn.field_146124_l = client.getGamePhase().after(GamePhase.PreMatch) && client.isMyTurn() || !client.getGamePhase().after(GamePhase.PreMatch) && me.getActiveCard() != null && !me.isReady();
         if (client.getGamePhase() == GamePhase.PreMatch && !this.controller.getClient().getMe().isReady() || client.getGamePhase() != GamePhase.PreMatch && this.controller.getClient().isMyTurn()) {
            if (client.getGamePhase() == GamePhase.PreMatch) {
               this.buttonEndTurn.field_146126_j = "Start Game";
            } else {
               this.buttonEndTurn.field_146126_j = "End Turn";
            }
         } else {
            this.buttonEndTurn.field_146126_j = "Waiting...";
         }

         this.buttonDiscard.field_146125_m = false;
         if (this.inspectingCard != null && this.inspectingCard.getCard() != null && this.inspectingCard.isMine() && !this.isSpectating) {
            BaseTrainerEffect effect = this.inspectingCard.getCard().getData().getEffect();
            if (effect != null && effect.showDiscardButton()) {
               this.buttonDiscard.field_146125_m = true;
            }
         }

         if (me.getCustomGUI() != null) {
            switch (me.getCustomGUI()) {
               case "GUI_PEEK":
                  this.guiPeek.visible = true;
                  this.guiPeek.func_73866_w_();
                  break;
               case "GUI_OPTIONAL":
                  this.guiOptional.visible = true;
                  this.guiOptional.func_73866_w_();
                  break;
               default:
                  this.guiPeek.visible = false;
                  this.guiOptional.visible = false;
            }
         } else {
            this.guiPeek.visible = false;
            this.guiOptional.visible = false;
         }

         this.inspectingCard.update(Mouse.isButtonDown(1));
         this.subGui.forEach(GuiScreen::func_73876_c);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      GameClientState client = this.controller.getClient();
      PlayerClientMyState me = client.getMe();
      PlayerClientOpponentState opp = client.getOpponent();
      List myHand = me.getHand();
      boolean handled = false;
      boolean enabled = this.cardSelector.getData() == null && (client.getCoinFlip() == null || client.getCoinFlip().getResults().isEmpty()) && this.guiPrizeSelector.getPrizes() == null && !this.guiPeek.visible && !this.guiOptional.visible;
      if (enabled && !this.isSpectating) {
         this.handleAbilityButton(this.controller.getClient().getPlayerIndex(), this.controller.getClient().isMyTurn(), mouseX, mouseY, me);
         this.handleAttackButtons(this.controller.getClient().getPlayerIndex(), this.controller.getClient().isMyTurn(), mouseX, mouseY, me);
         handled = this.handleRetreatButton(this.controller.getClient().isMyTurn(), mouseX, mouseY, me, opp) || handled;

         int pos;
         for(pos = 0; pos < myHand.size(); ++pos) {
            handled = this.handleCardInHand(mouseX, mouseY, myHand, opp.getHand(), pos, true) || handled;
         }

         for(pos = 0; pos < opp.getHand().size(); ++pos) {
            handled = this.handleCardInHand(mouseX, mouseY, myHand, opp.getHand(), pos, false) || handled;
         }
      }

      if (this.draggingCard == null) {
         this.isCardInSelectMode = false;
      }

      if (enabled) {
         handled = this.handleActiveCards(mouseX, mouseY, me, opp) || handled;
         handled = this.handleBenchCards(mouseX, mouseY, me, opp) || handled;
         handled = this.handleTrainerCards(mouseX, mouseY, me, opp) || handled;
      }

      if (!handled) {
         this.inspectingCard.clear();
      }

      if (this.draggingCard == null && me.getCardSelectorState() == null && Mouse.isButtonDown(0)) {
         Iterator var11;
         ImmutableCard c;
         CardSelectorState discard;
         if ((this.playerIndex == 0 && mouseX > this.scaledWidth / 2 - 106 && mouseX < this.scaledWidth / 2 - 78 && mouseY < this.scaledHeight / 2 + 72 && mouseY > this.scaledHeight / 2 + 51 || this.playerIndex == 1 && mouseX > this.scaledWidth / 2 + 79 && mouseX < this.scaledWidth / 2 + 107 && mouseY < this.scaledHeight / 2 + 72 && mouseY > this.scaledHeight / 2 + 51) && !me.getDiscardPile().isEmpty()) {
            discard = new CardSelectorState(0, 0, CardSelectorDisplay.Reveal, false, "selector.mydiscard");
            var11 = me.getDiscardPile().iterator();

            while(var11.hasNext()) {
               c = (ImmutableCard)var11.next();
               discard.getCardList().add(new CardWithLocation(new CommonCardState(c), true, BoardLocation.DiscardPile, 0));
            }

            me.setCardSelectorState(discard);
         }

         if ((this.playerIndex == 0 && mouseX > this.scaledWidth / 2 - 106 && mouseX < this.scaledWidth / 2 - 78 && mouseY < this.scaledHeight / 2 - 94 && mouseY > this.scaledHeight / 2 - 115 || this.playerIndex == 1 && mouseX > this.scaledWidth / 2 + 79 && mouseX < this.scaledWidth / 2 + 107 && mouseY < this.scaledHeight / 2 - 94 && mouseY > this.scaledHeight / 2 - 115) && !opp.getDiscardPile().isEmpty()) {
            discard = new CardSelectorState(0, 0, CardSelectorDisplay.Reveal, false, "selector.oppdiscard");
            var11 = opp.getDiscardPile().iterator();

            while(var11.hasNext()) {
               c = (ImmutableCard)var11.next();
               discard.getCardList().add(new CardWithLocation(new CommonCardState(c), false, BoardLocation.DiscardPile, 0));
            }

            me.setCardSelectorState(discard);
         }
      }

   }

   protected void func_146286_b(int mouseX, int mouseY, int state) {
      super.func_146286_b(mouseX, mouseY, state);
      GameClientState client = this.controller.getClient();
      PlayerClientMyState me = client.getMe();
      List myHand = me.getHand();
      if (this.draggingCard != null) {
         this.releaseDraggingCard(mouseX, mouseY, myHand, this.controller.getClient().getPlayerIndex());
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      if (this.field_146297_k != null && this.controller != null && this.controller.getClient() != null) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.controller.getClient().getMe() != null && this.controller.getClient().getOpponent() != null) {
            this.myTargets.draw(mouseX, mouseY);
            this.oppTargets.draw(mouseX, mouseY);
            GameClientState client = this.controller.getClient();
            PlayerClientMyState me = client.getMe();
            PlayerClientOpponentState opp = client.getOpponent();
            List myHand = me.getHand();
            List oppHand = opp.getHand();
            int color;
            if (client.getGamePhase() == GamePhase.PreMatch && !this.isSpectating) {
               if (client.isMyTurn()) {
                  this.field_146297_k.field_71446_o.func_110577_a(CoinRegistry.get(this.controller.getClient().getMe().getCoinSetID()).getHeads());
               } else {
                  this.field_146297_k.field_71446_o.func_110577_a(CoinRegistry.get(this.controller.getClient().getMe().getCoinSetID()).getTails());
               }

               GuiHelper.drawImageQuad((double)(this.field_146297_k.field_71443_c / this.scaledFactor - 50), 33.0, 40.0, 40.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
               String text = client.isMyTurn() ? I18n.func_135052_a("battle.first", new Object[0]) : I18n.func_135052_a("battle.second", new Object[0]);
               this.field_146289_q.func_175063_a(text, (float)(this.scaledWidth - this.field_146289_q.func_78256_a(text) - 50), 40.0F, 16777215);
               int topY = -60;
               color = 12;
               int offset = 80;
               RenderDuel.draw2DRectangle((double)(this.scaledWidth / 2 + offset - 5), (double)this.scaledWidth, (double)(this.scaledHeight / 2 + topY + color * 4 + 8 + 5), (double)(this.scaledHeight / 2 + topY - 5), 0.0F, 0.0F, 0.0F, 0.5F);
               this.field_146289_q.func_175063_a(I18n.func_135052_a("battle.guide.settingup.title", new Object[0]), (float)(this.scaledWidth / 2 + offset), (float)(this.scaledHeight / 2 + topY), 16777215);
               this.field_146289_q.func_175063_a(I18n.func_135052_a("battle.guide.settingup.1", new Object[0]), (float)(this.scaledWidth / 2 + offset), (float)(this.scaledHeight / 2 + topY + color), 16777215);
               this.field_146289_q.func_175063_a(I18n.func_135052_a("battle.guide.settingup.2", new Object[0]), (float)(this.scaledWidth / 2 + offset), (float)(this.scaledHeight / 2 + topY + color * 2), 16777215);
               this.field_146289_q.func_175063_a(I18n.func_135052_a("battle.guide.settingup.3", new Object[0]), (float)(this.scaledWidth / 2 + offset), (float)(this.scaledHeight / 2 + topY + color * 3), 16777215);
               this.field_146289_q.func_175063_a(I18n.func_135052_a("battle.guide.settingup.4", new Object[0]), (float)(this.scaledWidth / 2 + offset), (float)(this.scaledHeight / 2 + topY + color * 4), 16777215);
            }

            int secondsLeft;
            String text;
            if (this.isSpectating) {
               secondsLeft = 10;
               text = I18n.func_135052_a("battleSpectator.title", new Object[0]);
               color = this.field_146289_q.func_78256_a(text);
               String player1 = this.controller.getClient().getMe().getPlayerName();
               if (this.controller.getClient().isMyTurn()) {
                  player1 = TextFormatting.AQUA + "> " + player1;
               }

               int player1Width = this.field_146289_q.func_78256_a(player1);
               String player2 = this.controller.getClient().getOpponent().getPlayerName();
               if (!this.controller.getClient().isMyTurn()) {
                  player2 = TextFormatting.AQUA + "> " + player2;
               }

               int player2Width = this.field_146289_q.func_78256_a(player2);
               int maxWidth = Math.max(color, Math.max(player1Width, player2Width));
               RenderDuel.draw2DRectangle((double)this.scaledWidth, (double)(this.scaledWidth - 20 - maxWidth), (double)(secondsLeft - 5), (double)(secondsLeft + 32 + 5), 0.0F, 0.0F, 0.0F, 0.5F);
               this.field_146289_q.func_175063_a(text, (float)(this.scaledWidth - 10 - color), (float)secondsLeft, 16777215);
               this.field_146289_q.func_175063_a(player1, (float)(this.scaledWidth - 10 - player1Width), (float)(secondsLeft + 12), 16777215);
               this.field_146289_q.func_175063_a(player2, (float)(this.scaledWidth - 10 - player2Width), (float)(secondsLeft + 24), 16777215);
               CardHelper.resetColour(1.0F);
            }

            this.inspectingCard.draw(me, opp, this.field_146289_q, this.field_73735_i);
            if (this.guiPrizeSelector.getPrizes() == null && !this.guiPeek.visible && !this.guiOptional.visible) {
               this.drawHand(mouseX, mouseY, myHand, oppHand);
               this.drawAbilityButtons(mouseX, mouseY, me);
               this.drawAttackButtons(mouseX, mouseY, me);
               this.drawRetreatButton(mouseX, mouseY, me, opp, client.isMyTurn());
               if (this.draggingCard != null) {
                  this.drawDraggingCard(mouseX, mouseY, this.draggingCard.getCard(), me, opp);
                  if (Mouse.isButtonDown(0)) {
                     this.checkAndDrawActionHint(this.draggingCard.getCard().getData(), this.controller.getClient().isMyTurn(), mouseX, mouseY, me, this.controller.getClient().getPlayerIndex());
                  }
               }

               this.inspectingCard.drawTooltip(mouseX, mouseY, this.field_146289_q, this.field_73735_i);
            }

            this.guiDuelLog.draw(client.getLog(), mouseX, mouseY, this.field_73735_i);
            Iterator var19 = this.subGui.iterator();

            while(var19.hasNext()) {
               GuiScreen gui = (GuiScreen)var19.next();
               gui.func_73863_a(mouseX, mouseY, partialTicks);
            }

            if (me.getCounterEndTime() != null) {
               secondsLeft = (int)((me.getCounterEndTime().getTime() - Calendar.getInstance().getTime().getTime()) / 1000L);
               if (secondsLeft < 0) {
                  secondsLeft = 0;
               }

               text = secondsLeft + "s";
               color = 16777215;
               if (secondsLeft < 10) {
                  color = 16711680;
               }

               this.field_146297_k.field_71466_p.func_78276_b(text, 25 - this.field_146297_k.field_71466_p.func_78256_a(text) / 2, this.scaledHeight / 2 - 97, color);
            } else if (opp.getCounterEndTime() != null) {
               secondsLeft = (int)((opp.getCounterEndTime().getTime() - Calendar.getInstance().getTime().getTime()) / 1000L);
               if (secondsLeft < 0) {
                  secondsLeft = 0;
               }

               text = secondsLeft + "s";
               this.field_146297_k.field_71466_p.func_78276_b(text, 25 - this.field_146297_k.field_71466_p.func_78256_a(text) / 2, this.scaledHeight / 2 - 97, 12303291);
            }
         } else {
            String s = "Waiting for another player...";
            this.field_146297_k.field_71466_p.func_175065_a(s, (float)(this.scaledWidth / 2 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2), (float)(this.scaledHeight / 2), 16777215, true);
            this.buttonEndTurn.field_146125_m = false;
         }
      }

      if (Mouse.isButtonDown(0)) {
         this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/game/cursorPressed.png"));
      } else if (Mouse.isButtonDown(1)) {
         this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/game/cursorInspect.png"));
      } else {
         this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg:gui/game/cursor.png"));
      }

      GlStateManager.func_179141_d();
      GuiHelper.drawImageQuad((double)(mouseX - 8), (double)(mouseY - 8), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      GlStateManager.func_179118_c();
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (!this.isSpectating) {
         switch (button.field_146127_k) {
            case 10:
               PacketHandler.net.sendToServer(new GenericActionRequestPacket(this.controller.func_174877_v(), PhaseAction.EndTurn, this.playerIndex, 0, 0));
               break;
            case 11:
               PacketHandler.net.sendToServer(new GenericActionRequestPacket(this.controller.func_174877_v(), PhaseAction.Discard, this.playerIndex, this.inspectingCard.getLocation().ordinal(), this.inspectingCard.getLocationSubIndex()));
         }

         super.func_146284_a(button);
      }
   }

   public void func_146280_a(Minecraft mc, int width, int height) {
      super.func_146280_a(mc, width, height);
      Iterator var4 = this.subGui.iterator();

      while(var4.hasNext()) {
         GuiScreen gui = (GuiScreen)var4.next();
         gui.func_146280_a(mc, width, height);
      }

   }

   public void func_146274_d() throws IOException {
      if (this.controller != null && this.controller.getClient() != null && this.controller.getClient().getMe() != null) {
         super.func_146274_d();
         Iterator var1 = this.subGui.iterator();

         while(var1.hasNext()) {
            GuiScreen gui = (GuiScreen)var1.next();
            gui.func_146274_d();
         }

      }
   }

   public void func_146282_l() throws IOException {
      super.func_146282_l();
   }

   private void updateLocalDimensions() {
      ScaledResolution res = new ScaledResolution(this.field_146297_k);
      this.guiScaling = (double)((float)this.field_146297_k.field_71440_d / 240.0F / (float)res.func_78325_e());
      this.scaledWidth = res.func_78326_a();
      this.scaledHeight = res.func_78328_b();
      this.scaledFactor = res.func_78325_e();
   }

   private void updateTargetPositions() {
      int centerY = this.scaledHeight / 2 - (int)(21.0 * this.guiScaling);
      int cardWidth = (int)(19.0 * this.guiScaling);
      int cardHeight = (int)(25.0 * this.guiScaling);
      this.myTargets.activeTarget.setWidth(cardWidth);
      this.myTargets.activeTarget.setHeight(cardHeight);
      this.myTargets.activeTarget.setX(this.scaledWidth / 2 - (int)(9.0 * this.guiScaling));
      this.myTargets.activeTarget.setY(centerY + (int)(13.0 * this.guiScaling));
      this.oppTargets.activeTarget.setWidth(cardWidth);
      this.oppTargets.activeTarget.setHeight(cardHeight);
      this.oppTargets.activeTarget.setX(this.scaledWidth / 2 - (int)(9.0 * this.guiScaling));
      this.oppTargets.activeTarget.setY(centerY - (int)(13.0 * this.guiScaling) - cardHeight);
      float cX = this.playerIndex == 0 ? -48.0F : 29.0F;
      this.myTargets.trainerTarget.setWidth(cardWidth);
      this.myTargets.trainerTarget.setHeight(cardHeight);
      this.myTargets.trainerTarget.setX(this.scaledWidth / 2 + (int)((double)cX * this.guiScaling));
      this.myTargets.trainerTarget.setY(centerY + (int)(13.0 * this.guiScaling));
      this.oppTargets.trainerTarget.setWidth(cardWidth);
      this.oppTargets.trainerTarget.setHeight(cardHeight);
      this.oppTargets.trainerTarget.setX(this.scaledWidth / 2 + (int)((double)cX * this.guiScaling));
      this.oppTargets.trainerTarget.setY(centerY - (int)(13.0 * this.guiScaling) - cardHeight);

      int i;
      for(i = 0; i < 5; ++i) {
         cX = 60.0F - (float)i * 25.5F;
         this.myTargets.benchTargets[i].setWidth(cardWidth);
         this.myTargets.benchTargets[i].setHeight(cardHeight);
         this.myTargets.benchTargets[i].setX(this.scaledWidth / 2 - (int)((double)cX * this.guiScaling));
         this.myTargets.benchTargets[i].setY(centerY + (int)(64.0 * this.guiScaling));
         cX = 60.0F - (float)(4 - i) * 25.5F;
         this.oppTargets.benchTargets[i].setWidth(cardWidth);
         this.oppTargets.benchTargets[i].setHeight(cardHeight);
         this.oppTargets.benchTargets[i].setX(this.scaledWidth / 2 - (int)((double)cX * this.guiScaling));
         this.oppTargets.benchTargets[i].setY(centerY - (int)(64.0 * this.guiScaling) - cardHeight);
      }

      this.buttonAbility.setWidth(111);
      this.buttonAbility.setHeight(12);
      this.buttonAbility.setX((int)((double)(this.scaledWidth / 2) + 150.0 * this.guiScaling - 56.0));
      this.buttonAbility.setY((int)((double)(this.scaledHeight / 2) + -1.0 * this.guiScaling));

      for(i = 0; i < 3; ++i) {
         this.buttonAttacks[i].setWidth(111);
      }

      this.buttonEndTurn.field_146128_h = this.scaledWidth - 10 - this.buttonEndTurn.field_146120_f;
      this.buttonEndTurn.field_146129_i = 10;
      this.buttonDiscard.field_146128_h = this.scaledWidth - 10 - this.buttonDiscard.field_146120_f - 10 - this.buttonEndTurn.field_146120_f;
      this.buttonDiscard.field_146129_i = 10;
      this.buttonRetreat.setWidth(43);
      this.buttonRetreat.setHeight(20);
      this.buttonRetreat.setX((int)((double)(this.scaledWidth / 2) + 150.0 * this.guiScaling + 12.0));
      this.buttonRetreat.setY((int)((double)(this.scaledHeight / 2) + 0.0 * this.guiScaling + 56.0));
   }

   private void updateCameraPositions(EnumFacing facing) {
      EntityCamera cam = new EntityCamera(this.field_146297_k.field_71441_e);
      this.field_146297_k.func_152344_a(() -> {
         ClientProxy.camera = cam;
         int rotation = 90;
         double x0 = 0.0;
         double x1 = 0.0;
         double z0 = 0.0;
         double z1 = 0.0;
         float scale = this.controller.getScale();
         double scaleX = 0.0;
         double scaleZ = 0.0;
         switch (facing) {
            case EAST:
               rotation = 0;
               x1 = -5.4;
               x0 = -5.4;
               scaleX = scale > 1.0F ? (double)scale * 0.25 : 0.0;
               scaleZ = scale > 1.0F ? (double)(-scale) : 0.0;
               z0 = -1.2;
               z1 = 2.2;
               break;
            case NORTH:
               rotation = -90;
               x0 = -1.15;
               x1 = 2.15;
               z1 = 6.0;
               z0 = 6.0;
               scaleX = scale > 1.0F ? (double)(-scale) * 0.5 : 0.0;
               scaleZ = scale > 1.0F ? (double)(-scale) * 0.5 : 0.0;
               break;
            case SOUTH:
               rotation = 90;
               x0 = 2.15;
               x1 = -1.15;
               z1 = -5.25;
               z0 = -5.25;
               scaleX = scale > 1.0F ? (double)(-scale) * 0.25 : 0.0;
               scaleZ = scale > 1.0F ? (double)(-scale) * 0.25 : 0.0;
               break;
            case WEST:
               rotation = 180;
               x1 = 6.5;
               x0 = 6.5;
               z0 = 2.1;
               z1 = -1.2;
               scaleX = scale > 1.0F ? (double)(-scale) : 0.0;
               scaleZ = scale > 1.0F ? (double)scale * 0.25 : 0.0;
         }

         double y = (double)this.controller.func_174877_v().func_177956_o() + 13.5 * (double)scale;
         if (this.playerIndex == 0) {
            ClientProxy.camera.func_70080_a((double)this.controller.func_174877_v().func_177958_n() + x0 * (double)scale + scaleX, y, (double)this.controller.func_174877_v().func_177952_p() + z0 * (double)scale + scaleZ, (float)rotation, 90.0F);
         } else if (this.playerIndex == 1) {
            ClientProxy.camera.func_70080_a((double)this.controller.func_174877_v().func_177958_n() + x1 * (double)scale + scaleX, y, (double)this.controller.func_174877_v().func_177952_p() + z1 * (double)scale + scaleZ, (float)(rotation + 180), 90.0F);
         }

         this.field_146297_k.field_71441_e.func_73027_a(ClientProxy.camera.func_145782_y(), ClientProxy.camera);
      });
      this.ticks = 1;
   }

   private boolean handleActiveCards(int mouseX, int mouseY, PlayerClientMyState me, PlayerClientOpponentState opp) {
      boolean handled = false;
      if (this.draggingCard == null && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1))) {
         if (me.getActiveCard() != null && this.myTargets.activeTarget.isInside(mouseX, mouseY)) {
            this.isCardInSelectMode = true;
            this.inspectingCard.set(me.getActiveCard(), true, BoardLocation.Active, 0, true);
            handled = true;
         }

         if (opp.getActiveCard() != null && opp.getActiveCard().getData().getID() != ImmutableCard.FACE_DOWN_ID && this.oppTargets.activeTarget.isInside(mouseX, mouseY)) {
            this.isCardInSelectMode = true;
            this.inspectingCard.set(opp.getActiveCard(), false, BoardLocation.Active, 0, true);
            handled = true;
         }
      }

      return handled;
   }

   private boolean handleBenchCards(int mouseX, int mouseY, PlayerClientMyState me, PlayerClientOpponentState opp) {
      boolean handled = false;
      if (this.draggingCard == null && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1))) {
         for(int bench = 0; bench < 5; ++bench) {
            if (me.getBenchCards()[bench] != null && this.myTargets.benchTargets[bench].isInside(mouseX, mouseY)) {
               this.isCardInSelectMode = true;
               this.inspectingCard.set(me.getBenchCards()[bench], true, BoardLocation.Bench, bench, true);
               handled = true;
            }

            if (opp.getBenchCards()[bench] != null && opp.getBenchCards()[bench].getData().getID() != ImmutableCard.FACE_DOWN_ID && this.oppTargets.benchTargets[bench].isInside(mouseX, mouseY)) {
               this.isCardInSelectMode = true;
               this.inspectingCard.set(opp.getBenchCards()[bench], false, BoardLocation.Bench, bench, true);
               handled = true;
            }
         }
      }

      return handled;
   }

   private boolean handleTrainerCards(int mouseX, int mouseY, PlayerClientMyState me, PlayerClientOpponentState opp) {
      boolean handled = false;
      if (this.draggingCard == null && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1))) {
         if (me.getTrainerCard() != null && this.myTargets.trainerTarget.isInside(mouseX, mouseY)) {
            this.isCardInSelectMode = true;
            this.inspectingCard.set(me.getTrainerCard(), true, BoardLocation.Trainer, 0, true);
            handled = true;
         }

         if (opp.getTrainerCard() != null && this.oppTargets.trainerTarget.isInside(mouseX, mouseY)) {
            this.isCardInSelectMode = true;
            this.inspectingCard.set(opp.getTrainerCard(), false, BoardLocation.Trainer, 0, true);
            handled = true;
         }
      }

      return handled;
   }

   private void drawAbilityButtons(int mouseX, int mouseY, PlayerClientMyState me) {
      if (this.inspectingCard.isMine() && (this.inspectingCard.getLocation() == BoardLocation.Active || this.inspectingCard.getLocation() == BoardLocation.Bench) && me.getActiveCard() != null && this.controller.getClient().isMyTurn() && this.isCardInSelectMode && this.inspectingCard.getCard() instanceof PokemonCardState) {
         PokemonCardState card = (PokemonCardState)this.inspectingCard.getCard();
         if (card.getAbility() != null && card.getAbility().getEffect() != null && !card.getAbility().getEffect().isPassive()) {
            boolean isEnabled = card.getAbility().getEffect().isEnabled(card, this.controller.getClient());
            GlStateManager.func_179140_f();
            GlStateManager.func_179118_c();
            int color = this.buttonAbility.isInside(mouseX, mouseY) ? 587202304 : 301989887;
            int stroke = this.buttonAbility.isInside(mouseX, mouseY) ? -256 : -570425345;
            if (isEnabled && this.buttonAbility.isInside(mouseX, mouseY)) {
               this.drawRectWithBorder(this.buttonAbility.getX() - 1, this.buttonAbility.getY() - 1, this.buttonAbility.getX() + this.buttonAbility.getWidth() + 1, this.buttonAbility.getY() + this.buttonAbility.getHeight() + 1, color, stroke);
            } else if (isEnabled) {
               this.drawRectWithBorder(this.buttonAbility.getX(), this.buttonAbility.getY(), this.buttonAbility.getX() + this.buttonAbility.getWidth(), this.buttonAbility.getY() + this.buttonAbility.getHeight(), color, stroke);
            }

            GlStateManager.func_179145_e();
            GlStateManager.func_179141_d();
            if (this.buttonAbility.isInside(mouseX, mouseY)) {
               String error = null;
               if (!isEnabled) {
                  error = "Can't activate ability";
               }

               if (error != null) {
                  this.field_146289_q.func_78276_b(error, mouseX + 4, mouseY + 4, 16711680);
               }
            }
         }
      }

   }

   private boolean handleAbilityButton(int playerIndex, boolean isMyTurn, int mouseX, int mouseY, PlayerClientMyState me) {
      boolean handled = false;
      if (this.draggingCard == null && this.inspectingCard.isMine() && (this.inspectingCard.getLocation() == BoardLocation.Active || this.inspectingCard.getLocation() == BoardLocation.Bench) && me.getActiveCard() != null && isMyTurn && this.isCardInSelectMode && this.inspectingCard.getCard() instanceof PokemonCardState) {
         PokemonCardState card = (PokemonCardState)this.inspectingCard.getCard();
         if (card.getAbility() != null && card.getAbility().getEffect() != null && !card.getAbility().getEffect().isPassive() && card.getAbility().getEffect().isEnabled(card, this.controller.getClient()) && this.buttonAbility.isInside(mouseX, mouseY)) {
            PacketHandler.net.sendToServer(new GenericActionRequestPacket(this.controller.func_174877_v(), PhaseAction.UseAbility, playerIndex, this.inspectingCard.getLocation().ordinal(), this.inspectingCard.getLocationSubIndex()));
            handled = true;
         }
      }

      return handled;
   }

   private void drawAttackButtons(int mouseX, int mouseY, PlayerClientMyState me) {
      GlStateManager.func_179140_f();
      GlStateManager.func_179118_c();
      if (me != null && this.controller != null && this.controller.getClient() != null && this.controller.getClient().isMyTurn() && this.inspectingCard != null && (this.inspectingCard.isPickingAttack() || this.inspectingCard.isMine() && this.inspectingCard.getLocation() == BoardLocation.Active && me.getActiveCard() != null && this.isCardInSelectMode)) {
         PokemonCardState pokemon = (PokemonCardState)this.inspectingCard.getCard();
         boolean isParalyzed = false;
         boolean isAsleep = false;
         Iterator var7 = pokemon.getStatus().getConditions().iterator();

         while(var7.hasNext()) {
            Pair condition = (Pair)var7.next();
            switch ((CardCondition)condition.getLeft()) {
               case ASLEEP:
                  isAsleep = true;
                  break;
               case PARALYZED:
                  isParalyzed = true;
            }
         }

         for(int i = 0; i < pokemon.getAttacksStatus().length; ++i) {
            PokemonAttackStatus attack = pokemon.getAttacksStatus()[i];
            AttackCard data = attack.getData();
            boolean enoughEnergy = LogicHelper.isEnoughEnergy(data, pokemon.getAttachments(), pokemon);
            boolean isEnabled = this.inspectingCard.isPickingAttack() || !attack.isDisabled() && enoughEnergy && !isAsleep && !isParalyzed && me.getAvailableActions().isCanAttack();
            GuiTarget button = this.buttonAttacks[i];
            int color = button.isInside(mouseX, mouseY) ? 587202304 : 301989887;
            int stroke = button.isInside(mouseX, mouseY) ? -256 : -570425345;
            if (isEnabled && button.isInside(mouseX, mouseY)) {
               this.drawRectWithBorder(button.getX() - 1, button.getY() - 1, button.getX() + button.getWidth() + 1, button.getY() + button.getHeight() + 1, color, stroke);
            } else if (isEnabled) {
               this.drawRectWithBorder(button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), color, stroke);
            }

            if (button.isInside(mouseX, mouseY)) {
               if (this.inspectingCard.isPickingAttack()) {
                  this.field_146289_q.func_78276_b("Choose this attack!", mouseX + 4, mouseY + 4, 16777215);
               } else {
                  String error = null;
                  if (attack.isDisabled()) {
                     error = "This attack is disabled!";
                  } else if (!me.getAvailableActions().isCanAttack()) {
                     error = "You can't attack this turn.";
                  } else if (isAsleep) {
                     error = LanguageMapTCG.translateKey(pokemon.getData().getName().toLowerCase()) + " is asleep!";
                  } else if (isParalyzed) {
                     error = LanguageMapTCG.translateKey(pokemon.getData().getName().toLowerCase()) + " is paralyzed!";
                  } else if (!enoughEnergy) {
                     error = "Not enough energy!";
                  }

                  if (error != null) {
                     this.field_146289_q.func_78276_b(error, mouseX + 4, mouseY + 4, 16711680);
                  }
               }
            }
         }
      }

   }

   private boolean handleAttackButtons(int playerIndex, boolean isMyTurn, int mouseX, int mouseY, PlayerClientMyState me) {
      boolean handled = false;
      if (isMyTurn && this.draggingCard == null && (this.inspectingCard.isPickingAttack() || this.inspectingCard.isMine() && this.inspectingCard.getLocation() == BoardLocation.Active && me.getActiveCard() != null && this.isCardInSelectMode && me.getAvailableActions().isCanAttack())) {
         PokemonCardState pokemon = (PokemonCardState)this.inspectingCard.getCard();
         boolean isParalyzed = false;
         boolean isAsleep = false;
         Iterator var10 = pokemon.getStatus().getConditions().iterator();

         while(var10.hasNext()) {
            Pair condition = (Pair)var10.next();
            switch ((CardCondition)condition.getLeft()) {
               case ASLEEP:
                  isAsleep = true;
                  break;
               case PARALYZED:
                  isParalyzed = true;
            }
         }

         boolean buttonDown = Mouse.isButtonDown(0);
         if (buttonDown && (this.inspectingCard.isPickingAttack() || !isAsleep && !isParalyzed)) {
            for(int i = 0; i < pokemon.getAttacksStatus().length; ++i) {
               PokemonAttackStatus attackStatus = pokemon.getAttacksStatus()[i];
               AttackCard cardAttack = attackStatus.getData();
               if (cardAttack != null && !attackStatus.isDisabled() && this.buttonAttacks[i].isInside(mouseX, mouseY)) {
                  boolean enoughEnergy = this.inspectingCard.isPickingAttack() || LogicHelper.isEnoughEnergy(cardAttack, pokemon.getAttachments(), pokemon);
                  if (enoughEnergy) {
                     PacketHandler.net.sendToServer(new GenericActionRequestPacket(this.controller.func_174877_v(), PhaseAction.UseAttack, playerIndex, i, 0));
                     handled = true;
                  }
               }
            }
         }
      }

      return handled;
   }

   private boolean drawRetreatButton(int mouseX, int mouseY, PlayerClientMyState me, PlayerClientOpponentState opp, boolean isMyTurn) {
      boolean clickedSomething = false;
      if (this.inspectingCard.isMine() && this.inspectingCard.getLocation() == BoardLocation.Active && me.getActiveCard() != null && isMyTurn && this.isCardInSelectMode) {
         PokemonCardState pokemon = (PokemonCardState)this.inspectingCard.getCard();
         boolean isParalyzed = false;
         boolean isAsleep = false;
         Iterator var10 = pokemon.getStatus().getConditions().iterator();

         while(var10.hasNext()) {
            Pair condition = (Pair)var10.next();
            switch ((CardCondition)condition.getLeft()) {
               case ASLEEP:
                  isAsleep = true;
                  break;
               case PARALYZED:
                  isParalyzed = true;
            }
         }

         int retreatCost = Math.max(0, me.getActiveCard().getRetreatCost() + LogicHelper.getCostModifier(me, opp));
         boolean enoughEnergy = LogicHelper.isEnoughEnergy(retreatCost, pokemon.getAttachments(), pokemon);
         boolean hasBench = LogicHelper.hasBench(me);
         boolean canRetreat = me.getActiveCard().canRetreat();
         boolean enabled = hasBench && enoughEnergy && !isAsleep && !isParalyzed && me.getAvailableActions().isCanRetreatActive() && canRetreat;
         int color = this.buttonRetreat.isInside(mouseX, mouseY) ? 587202304 : 301989887;
         int stroke = this.buttonRetreat.isInside(mouseX, mouseY) ? -256 : -570425345;
         if (enabled && this.buttonRetreat.isInside(mouseX, mouseY)) {
            this.drawRectWithBorder(this.buttonRetreat.getX() - 1, this.buttonRetreat.getY() - 1, this.buttonRetreat.getX() + this.buttonRetreat.getWidth() + 1, this.buttonRetreat.getY() + this.buttonRetreat.getHeight() + 1, color, stroke);
         } else if (enabled) {
            this.drawRectWithBorder(this.buttonRetreat.getX(), this.buttonRetreat.getY(), this.buttonRetreat.getX() + this.buttonRetreat.getWidth(), this.buttonRetreat.getY() + this.buttonRetreat.getHeight(), color, stroke);
         }

         if (this.buttonRetreat.isInside(mouseX, mouseY)) {
            String error = null;
            if (!me.getAvailableActions().isCanRetreatActive()) {
               error = "You can't retreat this turn";
            } else if (isAsleep) {
               error = LanguageMapTCG.translateKey(pokemon.getData().getName().toLowerCase()) + " is asleep";
            } else if (isParalyzed) {
               error = LanguageMapTCG.translateKey(pokemon.getData().getName().toLowerCase()) + " is paralyzed";
            } else if (!hasBench) {
               error = "You have no card in bench";
            } else if (!enoughEnergy) {
               error = "You don't have enough energy";
            } else if (!canRetreat) {
               error = "This can't retreat";
            }

            if (error != null) {
               this.field_146289_q.func_78276_b(error, mouseX - this.field_146289_q.func_78256_a(error), mouseY, 16711680);
            }
         }
      }

      return clickedSomething;
   }

   private boolean handleRetreatButton(boolean isMyTurn, int mouseX, int mouseY, PlayerClientMyState me, PlayerClientOpponentState opp) {
      boolean clickedSomething = false;
      if (this.inspectingCard.isMine() && this.inspectingCard.getLocation() == BoardLocation.Active && Mouse.isButtonDown(0) && this.draggingCard == null && this.buttonRetreat.isInside(mouseX, mouseY) && isMyTurn && this.isCardInSelectMode) {
         PokemonCardState pokemon = (PokemonCardState)this.inspectingCard.getCard();
         boolean isParalyzed = false;
         boolean isAsleep = false;
         Iterator var10 = pokemon.getStatus().getConditions().iterator();

         while(var10.hasNext()) {
            Pair condition = (Pair)var10.next();
            switch ((CardCondition)condition.getLeft()) {
               case ASLEEP:
                  isAsleep = true;
                  break;
               case PARALYZED:
                  isParalyzed = true;
            }
         }

         int cost = me.getActiveCard().getRetreatCost();
         cost = Math.max(0, cost + LogicHelper.getCostModifier(me, opp));
         boolean enoughEnergy = LogicHelper.isEnoughEnergy(cost, pokemon.getAttachments(), pokemon);
         boolean hasBench = LogicHelper.hasBench(me);
         boolean canRetreat = me.getActiveCard().canRetreat();
         boolean enabled = hasBench && enoughEnergy && !isAsleep && !isParalyzed && me.getAvailableActions().isCanRetreatActive() && canRetreat;
         if (enabled) {
            CardSelectorState state = new CardSelectorState(cost, cost, true, CardSelectorDisplay.Discard, true);
            state.setCardList(LogicHelper.getEnergiesFromList(me.getActiveCard().getAttachments()));
            this.cardSelector.set(CardSelectorPurpose.StartRetreating, state);
            if (cost == 0) {
               this.cardSelector.acceptSelection();
            }

            clickedSomething = true;
         }
      }

      return clickedSomething;
   }

   private void checkAndDrawActionHint(ImmutableCard card, boolean isMyTurn, int mouseX, int mouseY, PlayerClientMyState me, int playerIndex) {
      CardType ct = card.getCardType();
      GlStateManager.func_179140_f();
      GlStateManager.func_179141_d();
      int c = 8;
      int color = '\uff00';
      int errorColor = 16711680;
      if (this.controller.getClient().getGamePhase() != GamePhase.PreMatch && !isMyTurn) {
         this.field_146289_q.func_175065_a("Not your turn yet!", (float)mouseX, (float)mouseY, errorColor, true);
      } else {
         for(int bench = 0; bench < 5; ++bench) {
            if (this.myTargets.benchTargets[bench].isInside(mouseX, mouseY)) {
               PokemonCardState benchCard = me.getBenchCards()[bench];
               this.showPokemonCardActionHint(mouseX, mouseY, ct, c, color, errorColor, me, benchCard, false);
            }
         }

         if (this.myTargets.activeTarget.isInside(mouseX, mouseY)) {
            PokemonCardState activeCard = me.getActiveCard();
            this.showPokemonCardActionHint(mouseX, mouseY, ct, c, color, errorColor, me, activeCard, true);
         } else if ((double)mouseY < (double)this.scaledHeight / 1.5 && ct == CardType.TRAINER && card.getEffect() != null) {
            if (me.getAvailableActions().isCanPlayTrainer() && card.getEffect().canPlay(this.controller.getClient())) {
               this.field_146289_q.func_175065_a("Play Trainer card", (float)(mouseX + c), (float)(mouseY + c), color, true);
            } else {
               this.field_146289_q.func_175065_a("Cannot play Trainer card now", (float)(mouseX + c), (float)(mouseY + c), errorColor, true);
            }
         }
      }

   }

   private void releaseDraggingCard(int mouseX, int mouseY, List myHand, int playerIndex) {
      ImmutableCard card = this.draggingCard.getCard().getData();

      for(int benchIndex = 0; benchIndex < 5; ++benchIndex) {
         if (this.myTargets.benchTargets[benchIndex].isInside(mouseX, mouseY)) {
            PacketHandler.net.sendToServer(new UpdateServerCardRecordPacket(this.controller.func_174877_v(), BoardLocation.Bench, benchIndex, card, this.draggingCard.locationSubIndex));
         }
      }

      if (this.myTargets.activeTarget.isInside(mouseX, mouseY)) {
         PacketHandler.net.sendToServer(new UpdateServerCardRecordPacket(this.controller.func_174877_v(), BoardLocation.Active, 0, card, this.draggingCard.locationSubIndex));
      } else if ((double)mouseY < (double)this.scaledHeight / 1.5) {
         PacketHandler.net.sendToServer(new UpdateServerCardRecordPacket(this.controller.func_174877_v(), BoardLocation.Trainer, 0, card, this.draggingCard.locationSubIndex));
      }

      this.draggingCard = null;
   }

   private void drawDraggingCard(int mouseX, int mouseY, CommonCardState card, PlayerClientMyState me, PlayerClientOpponentState opp) {
      GL11.glPushMatrix();
      double scaleFactor = 0.35;
      GL11.glTranslated((double)mouseX, (double)mouseY, 0.0);
      GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
      CardHelper.drawCard(card, this.field_146297_k, 0, 0, this.field_73735_i, 1.0F, 0.0, me, opp);
      GL11.glScaled(-scaleFactor, -scaleFactor, -scaleFactor);
      GL11.glPopMatrix();
   }

   private boolean handleCardInHand(int mouseX, int mouseY, List myHand, List oppHand, int handIndex, boolean isMe) {
      boolean handled = false;
      ImmutableCard card;
      Object cardState;
      if (isMe) {
         card = (ImmutableCard)myHand.get(handIndex);
         if ((this.draggingCard == null || this.isCardInSelectMode) && this.myTargets.handTargets.size() > handIndex && ((GuiTarget)this.myTargets.handTargets.get(handIndex)).isInside(mouseX, mouseY) && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1))) {
            cardState = card.isPokemonCard() ? new PokemonCardState(card, this.controller.getClient().getTurnCount()) : new CommonCardState(card);
            if (Mouse.isButtonDown(1)) {
               this.inspectingCard.set((CommonCardState)cardState, true, BoardLocation.Hand, handIndex, true);
               handled = true;
            }

            if (Mouse.isButtonDown(0)) {
               this.draggingCard = new CardWithLocation((CommonCardState)cardState, true, BoardLocation.Hand, handIndex);
            }

            this.isCardInSelectMode = false;
         }
      } else {
         card = (ImmutableCard)oppHand.get(handIndex);
         if ((this.draggingCard == null || this.isCardInSelectMode) && this.oppTargets.handTargets.size() > handIndex && ((GuiTarget)this.oppTargets.handTargets.get(handIndex)).isInside(mouseX, mouseY) && (Mouse.isButtonDown(1) || Mouse.isButtonDown(0)) && card != null && card.getID() != ImmutableCard.FACE_DOWN_ID) {
            cardState = card.isPokemonCard() ? new PokemonCardState(card, this.controller.getClient().getTurnCount()) : new CommonCardState(card);
            this.inspectingCard.set((CommonCardState)cardState, true, BoardLocation.Hand, handIndex, true);
            handled = true;
            this.isCardInSelectMode = false;
         }
      }

      return handled;
   }

   private void drawHand(int mouseX, int mouseY, List myHand, List oppHand) {
      int i;
      ImmutableCard card;
      GuiTarget handTarget;
      for(i = 0; i < myHand.size(); ++i) {
         if (i >= myHand.size()) {
            return;
         }

         if (i >= this.myTargets.handTargets.size()) {
            return;
         }

         card = (ImmutableCard)myHand.get(i);
         handTarget = (GuiTarget)this.myTargets.handTargets.get(i);
         int offsetY = 0;
         float opacity = 1.0F;
         if ((this.draggingCard == null || this.isCardInSelectMode) && handTarget.isInside(mouseX, mouseY)) {
            if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
               opacity = 0.5F;
            }

            offsetY = -15;
         }

         if (this.draggingCard != null && this.draggingCard.getLocation() == BoardLocation.Hand && this.draggingCard.getLocationSubIndex() == i) {
            opacity = 0.3F;
         }

         GL11.glPushMatrix();
         double scaleFactor = 0.25 * this.guiScaling;
         GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
         if (card != null && card.getID() != ImmutableCard.FACE_DOWN_ID) {
            CardHelper.drawCard(new CommonCardState(card), this.field_146297_k, (int)((double)(handTarget.getX() * 2) / scaleFactor) + 100, (int)((double)(handTarget.getY() * 2) / scaleFactor) + offsetY + 150, this.field_73735_i, opacity, 0.0, (PlayerClientMyState)null, (PlayerClientOpponentState)null);
         } else {
            CardHelper.drawCardBack(this.field_146297_k, (int)((double)(handTarget.getX() * 2) / scaleFactor) + 100, (int)((double)(handTarget.getY() * 2) / scaleFactor) + offsetY + 150, this.field_73735_i, (PlayerCommonState)this.controller.getClient().getMe());
         }

         GL11.glScaled(-scaleFactor, -scaleFactor, -scaleFactor);
         GL11.glPopMatrix();
      }

      for(i = 0; i < oppHand.size(); ++i) {
         if (i >= oppHand.size()) {
            return;
         }

         if (i >= this.oppTargets.handTargets.size()) {
            break;
         }

         card = (ImmutableCard)oppHand.get(i);
         handTarget = (GuiTarget)this.oppTargets.handTargets.get(i);
         GL11.glPushMatrix();
         double scaleFactor = 0.25 * this.guiScaling;
         GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
         int offsetY = 0;
         float opacity = 1.0F;
         if ((this.draggingCard == null || this.isCardInSelectMode) && handTarget.isInside(mouseX, mouseY)) {
            if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
               opacity = 0.5F;
            }

            offsetY = 15;
         }

         if (card != null && card.getID() != ImmutableCard.FACE_DOWN_ID) {
            CardHelper.drawCard(new CommonCardState(card), this.field_146297_k, (int)((double)(handTarget.getX() * 2) / scaleFactor) + 100, (int)((double)(handTarget.getY() * 2) / scaleFactor) + offsetY + 150, this.field_73735_i, opacity, 180.0, (PlayerClientMyState)null, (PlayerClientOpponentState)null);
         } else {
            CardHelper.drawCardBack(this.field_146297_k, (int)((double)(handTarget.getX() * 2) / scaleFactor) + 100, (int)((double)(handTarget.getY() * 2) / scaleFactor) + offsetY + 150, this.field_73735_i, (PlayerCommonState)this.controller.getClient().getOpponent());
         }

         GL11.glScaled(-scaleFactor, -scaleFactor, -scaleFactor);
         GL11.glPopMatrix();
      }

   }

   private void showPokemonCardActionHint(int mouseX, int mouseY, CardType ct, int c, int color, int errorColor, PlayerClientMyState me, PokemonCardState card, boolean isActive) {
      if (ct == CardType.BASIC && card == null) {
         if (isActive) {
            this.field_146289_q.func_175065_a("Play Active Pokemon", (float)(mouseX + c), (float)(mouseY + c), color, true);
         } else if (me.getActiveCard() != null) {
            this.field_146289_q.func_175065_a("Play Benched Pokemon", (float)(mouseX + c), (float)(mouseY + c), color, true);
         } else {
            this.field_146289_q.func_175065_a("Must play Active Pokemon first", (float)(mouseX + c), (float)(mouseY + c), errorColor, true);
         }
      } else if (ct == CardType.ENERGY && card != null) {
         if (me.getAvailableActions().isCanPlayEnergy()) {
            this.field_146289_q.func_175065_a("Attach Energy", (float)(mouseX + c), (float)(mouseY + c), color, true);
         } else {
            this.field_146289_q.func_175065_a("Cannot attach Energy this turn", (float)(mouseX + c), (float)(mouseY + c), errorColor, true);
         }
      } else if (ct == CardType.TOOL && card != null) {
         this.field_146289_q.func_175065_a("Attach Tool", (float)(mouseX + c), (float)(mouseY + c), color, true);
      } else if ((ct == CardType.STAGE1 || ct == CardType.STAGE2) && card != null && this.draggingCard.getCard().getData().getPrevEvoID() == card.getData().getPokemonID()) {
         if (LogicHelper.canEvolve(card.getTurn(), this.controller.getClient().getTurnCount())) {
            if (this.controller.getClient().isDisablingEvolution(card)) {
               this.field_146289_q.func_175065_a("Disabled by an ability!", (float)(mouseX + c), (float)(mouseY + c), errorColor, true);
            } else {
               this.field_146289_q.func_175065_a("Evolve", (float)(mouseX + c), (float)(mouseY + c), color, true);
            }
         } else {
            this.field_146289_q.func_175065_a("Cannot Evolve this turn!", (float)(mouseX + c), (float)(mouseY + c), errorColor, true);
         }
      } else if (ct == CardType.LVLX && this.draggingCard.getCard().getData().getPokemonID() == card.getData().getPokemonID()) {
         if (LogicHelper.canEvolve(card.getTurn(), this.controller.getClient().getTurnCount())) {
            this.field_146289_q.func_175065_a("Level Up", (float)(mouseX + c), (float)(mouseY + c), color, true);
         } else {
            this.field_146289_q.func_175065_a("Cannot Level Up this turn!", (float)(mouseX + c), (float)(mouseY + c), errorColor, true);
         }
      } else if (ct == CardType.MEGA && this.draggingCard.getCard().getData().getPokemonID() == card.getData().getPokemonID()) {
         if (LogicHelper.canEvolve(card.getTurn(), this.controller.getClient().getTurnCount())) {
            this.field_146289_q.func_175065_a("Mega Evolve", (float)(mouseX + c), (float)(mouseY + c), color, true);
         } else {
            this.field_146289_q.func_175065_a("Cannot Mega Evolve this turn!", (float)(mouseX + c), (float)(mouseY + c), errorColor, true);
         }
      } else {
         this.field_146289_q.func_175065_a("Can't put that here", (float)(mouseX + c), (float)(mouseY + c), errorColor, true);
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}

package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.dto.CustomGUI;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.item.ItemDeck;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerServerState extends PlayerCommonState {
   private List deck = new ArrayList();
   private List hand = new ArrayList();
   private AvailableActions availableActions = new AvailableActions();
   private CardSelectorState cardSelectorState = null;
   private CardSelectorResult cardSelectorResult = null;
   private CustomGUI customGUI;
   private CustomGUIResult customGUIResult;
   protected EntityPlayer entityPlayer;
   private int openingPrizeIndex = -1;

   public PlayerServerState(EntityPlayer entityPlayer) {
      this.entityPlayer = entityPlayer;
      this.playerName = entityPlayer.getDisplayNameString();
   }

   public int drawCards(int count, GameServerState server) {
      int cardDrew = 0;

      for(int i = 0; i < count && this.deck.size() != 0; ++i) {
         this.hand.add(this.deck.get(0));
         if (this.entityPlayer.field_71071_by.func_70431_c(new ItemStack(TCG.itemNecklace))) {
            this.hand.add(this.deck.get(0));
         }

         this.deck.remove(0);
         ++cardDrew;
      }

      if (this.getEntityPlayer() == server.getPlayer(0).getEntityPlayer()) {
         server.getLog().trackDrawCard(cardDrew, 0, server);
      } else {
         server.getLog().trackDrawCard(cardDrew, 1, server);
      }

      return cardDrew;
   }

   public List getDeck() {
      return this.deck;
   }

   public List getHand() {
      return this.hand;
   }

   public void setDeck(List deck) {
      this.deck = deck;
   }

   public void setCardBack(String id) {
      this.cardBackID = id;
   }

   public void setCoinSet(String id) {
      this.coinSetID = id;
   }

   public AvailableActions getAvailableActions() {
      return this.availableActions;
   }

   public CardSelectorState getCardSelectorState() {
      return this.cardSelectorState;
   }

   public void setCardSelectorState(CardSelectorState cardSelectorState) {
      this.cardSelectorState = cardSelectorState;
   }

   public CardSelectorResult getCardSelectorResult() {
      return this.cardSelectorResult;
   }

   public void setCardSelectorResult(CardSelectorResult cardSelectorResult) {
      this.cardSelectorResult = cardSelectorResult;
   }

   public EntityPlayer getEntityPlayer() {
      return this.entityPlayer;
   }

   public void setEntityPlayer(EntityPlayer entityPlayer) {
      this.entityPlayer = entityPlayer;
   }

   public void setCounterEndTime(Date counterEndTime) {
      this.counterEndTime = counterEndTime;
   }

   public boolean hasPokemonLeft() {
      if (this.activeCard != null) {
         return true;
      } else {
         if (this.benchCards != null) {
            PokemonCardState[] var1 = this.benchCards;
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               PokemonCardState card = var1[var3];
               if (card != null) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean hasDeck() {
      return this.entityPlayer != null && this.entityPlayer.func_184614_ca() != null && this.entityPlayer.func_184614_ca().func_77973_b() instanceof ItemDeck;
   }

   public void prepareDeck() {
      if (this.hasDeck()) {
         List cards = LogicHelper.getCardsAndCosmetics(this.entityPlayer.func_184614_ca(), this);
         this.deck.clear();
         this.deck.addAll(cards);
         if (!this.entityPlayer.field_71071_by.func_70431_c(new ItemStack(TCG.itemPendant))) {
            LogicHelper.shuffleCardList(this.deck);
         }
      }

   }

   public void switchActive(PokemonCardState benchCard, GameServerState server) {
      PokemonCardState oldActive = null;
      int benchIndex = false;

      for(int i = 0; i < this.benchCards.length; ++i) {
         if (this.benchCards[i] == benchCard) {
            if (this.activeCard != null) {
               this.activeCard.getStatus().removeAllConditions();
            }

            oldActive = this.activeCard;
            this.benchCards[i] = this.activeCard;
            this.activeCard = benchCard;
            break;
         }
      }

      PlayerServerState[] var11 = server.getPlayers();
      int var6 = var11.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         PlayerServerState player = var11[var7];
         Iterator var9 = player.getActiveAndBenchCards().iterator();

         while(var9.hasNext()) {
            PokemonCardState pokemon = (PokemonCardState)var9.next();
            if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null) {
               pokemon.getAbility().getEffect().onSwitchActiveCard(this.activeCard, oldActive, this, pokemon, player, server);
            }

            if (pokemon.getHiddenAbility() != null && pokemon.getHiddenAbility().getEffect() != null) {
               pokemon.getHiddenAbility().getEffect().onSwitchActiveCard(this.activeCard, oldActive, this, pokemon, player, server);
            }
         }
      }

      server.getLog().trackSwitch(this.activeCard, server.getTurn(this), server);
   }

   public CustomGUI getCustomGUI() {
      return this.customGUI;
   }

   public void setCustomGUI(CustomGUI customGUI) {
      this.customGUI = customGUI;
   }

   public CustomGUIResult getCustomGUIResult() {
      return this.customGUIResult;
   }

   public void useCustomGUIDefaultResult() {
      this.customGUIResult = this.customGUI.defaultResult;
   }

   public void setCustomGUIResult(CustomGUIResult customGUIResult) {
      this.customGUIResult = customGUIResult;
   }

   public void setPendingPrizeCount(int playerIndex, int pendingCount) {
      this.pendingPrizePlayerIndex = playerIndex;
      this.pendingPrizeCount = pendingCount;
      int count = 0;
      ImmutableCard[] var4 = this.prizeCards;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ImmutableCard card = var4[var6];
         if (card != null) {
            ++count;
         }
      }

      if (this.pendingPrizeCount > count) {
         this.pendingPrizeCount = count;
      }

      if (this.pendingPrizeCount < 0) {
         this.pendingPrizeCount = 0;
      }

   }

   public void addPendingPrizeCount(int playerIndex, int pendingPrizeCount) {
      this.setPendingPrizeCount(playerIndex, this.pendingPrizeCount + pendingPrizeCount);
   }

   public int getOpeningPrizeIndex() {
      return this.openingPrizeIndex;
   }

   public void setOpeningPrizeIndex(int openingPrizeIndex) {
      this.openingPrizeIndex = openingPrizeIndex;
   }
}

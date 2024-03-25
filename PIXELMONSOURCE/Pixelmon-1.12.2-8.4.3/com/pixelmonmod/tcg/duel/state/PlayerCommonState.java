package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.personalization.Coin;
import com.pixelmonmod.tcg.api.registries.CardBackRegistry;
import com.pixelmonmod.tcg.api.registries.CoinRegistry;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PlayerCommonState {
   protected boolean isReady;
   protected TrainerCardState trainerCard = null;
   protected PokemonCardState activeCard = null;
   protected PokemonCardState[] benchCards = new PokemonCardState[5];
   protected List graveyard = new ArrayList();
   protected ImmutableCard[] prizeCards = null;
   private List hand = new ArrayList();
   protected boolean showPokemonsInClient = true;
   protected String cardBackID;
   protected String coinSetID;
   protected boolean isChoosingOppAttack;
   protected int pendingPrizeCount;
   protected int pendingPrizePlayerIndex;
   protected boolean isInGUI;
   protected boolean isSpectating;
   protected boolean trainersDisabled;
   protected String playerName;
   protected Integer timeLeft;
   protected Date counterEndTime;
   private boolean canSeeOpponentHand;
   private boolean isMe;
   private GameServerState server;
   private PlayerServerState player;

   public PlayerCommonState() {
      this.cardBackID = CardBackRegistry.STANDARD.getName();
      this.coinSetID = CoinRegistry.CHARMANDER.getName();
      this.isChoosingOppAttack = false;
      this.pendingPrizeCount = 0;
      this.pendingPrizePlayerIndex = 0;
      this.isInGUI = true;
   }

   public PlayerCommonState(PlayerServerState player, GameServerState server, boolean isMe) {
      this.cardBackID = CardBackRegistry.STANDARD.getName();
      this.coinSetID = CoinRegistry.CHARMANDER.getName();
      this.isChoosingOppAttack = false;
      this.pendingPrizeCount = 0;
      this.pendingPrizePlayerIndex = 0;
      this.isInGUI = true;
      this.isReady = player.isReady();
      this.trainerCard = player.getTrainerCard();
      this.activeCard = player.getActiveCard();
      this.benchCards = player.getBenchCards();
      this.graveyard = player.getDiscardPile();
      this.prizeCards = player.getPrizeCards();
      this.hand = player.getHand();
      this.cardBackID = player.getCardBackID();
      this.coinSetID = player.getCoinSetID();
      this.isChoosingOppAttack = player.isChoosingOppAttack;
      this.isInGUI = player.isInGUI;
      this.isSpectating = player.isSpectating;
      this.trainersDisabled = player.trainersDisabled;
      this.canSeeOpponentHand = false;
      this.playerName = player.getEntityPlayer().getDisplayNameString();
      if (player.getCounterEndTime() != null) {
         this.timeLeft = (int)(player.getCounterEndTime().getTime() - Calendar.getInstance().getTime().getTime());
      }

      this.server = server;
      this.player = player;
      this.isMe = isMe;
   }

   @SideOnly(Side.CLIENT)
   public PlayerCommonState(ByteBuf buf, GameServerState server, boolean isMe) {
      this.cardBackID = CardBackRegistry.STANDARD.getName();
      this.coinSetID = CoinRegistry.CHARMANDER.getName();
      this.isChoosingOppAttack = false;
      this.pendingPrizeCount = 0;
      this.pendingPrizePlayerIndex = 0;
      this.isInGUI = true;
      if (buf.readBoolean()) {
         this.timeLeft = buf.readInt();
      }

      this.playerName = ByteBufUtils.readUTF8String(buf);
      this.isSpectating = buf.readBoolean();
      this.isReady = buf.readBoolean();
      this.cardBackID = ByteBufUtils.readUTF8String(buf);
      this.coinSetID = ByteBufUtils.readUTF8String(buf);
      this.isChoosingOppAttack = buf.readBoolean();
      this.isInGUI = buf.readBoolean();
      this.pendingPrizePlayerIndex = buf.readInt();
      this.pendingPrizeCount = buf.readInt();
      this.hand = ByteBufTCG.readCardList(buf);
      this.graveyard = ByteBufTCG.readCardList(buf);
      this.trainersDisabled = buf.readBoolean();

      while(true) {
         label47:
         while(true) {
            int locationOrdinal = buf.readInt();
            if (locationOrdinal < 0) {
               if (this.timeLeft != null) {
                  Calendar calendar = Calendar.getInstance();
                  calendar.add(14, this.timeLeft);
                  this.counterEndTime = calendar.getTime();
               }

               return;
            }

            BoardLocation location = BoardLocation.values()[locationOrdinal];
            int i;
            switch (location) {
               case Trainer:
                  this.trainerCard = new TrainerCardState(ByteBufTCG.readCard(buf));
                  break;
               case Active:
                  this.activeCard = new PokemonCardState(buf);
                  break;
               case Bench:
                  this.benchCards = new PokemonCardState[5];
                  i = 0;

                  while(true) {
                     if (i >= this.benchCards.length) {
                        continue label47;
                     }

                     if (buf.readBoolean()) {
                        this.benchCards[i] = new PokemonCardState(buf);
                     }

                     ++i;
                  }
               case Prize:
                  this.prizeCards = new ImmutableCard[buf.readInt()];

                  for(i = 0; i < this.prizeCards.length; ++i) {
                     if (buf.readBoolean()) {
                        this.prizeCards[i] = new ImmutableCard();
                     }
                  }
            }
         }
      }
   }

   public void write(ByteBuf buf) {
      buf.writeBoolean(this.timeLeft != null);
      if (this.timeLeft != null) {
         buf.writeInt(this.timeLeft);
      }

      ByteBufUtils.writeUTF8String(buf, this.playerName);
      buf.writeBoolean(this.isSpectating);
      buf.writeBoolean(this.isReady);
      ByteBufUtils.writeUTF8String(buf, this.cardBackID);
      ByteBufUtils.writeUTF8String(buf, this.coinSetID);
      buf.writeBoolean(this.isChoosingOppAttack);
      buf.writeBoolean(this.isInGUI);
      buf.writeInt(this.pendingPrizePlayerIndex);
      buf.writeInt(this.pendingPrizeCount);
      if (this.server.getOpponent(this.player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemEye))) {
         this.canSeeOpponentHand = true;
         ByteBufTCG.writeCardList(buf, this.hand);
      } else {
         Iterator var2 = this.server.getOpponent(this.player).getActiveAndBenchCards().iterator();

         while(var2.hasNext()) {
            PokemonCardState card = (PokemonCardState)var2.next();
            if (card != null && card.getAbility() != null && card.getAbility().getEffect() != null && card.getAbility().getEffect().revealHand()) {
               this.canSeeOpponentHand = true;
               ByteBufTCG.writeCardList(buf, this.hand);
            }
         }
      }

      if (!this.canSeeOpponentHand) {
         if (!this.isSpectating && this.isMe) {
            ByteBufTCG.writeCardList(buf, this.hand);
         } else {
            ByteBufTCG.writeFaceDownCardList(buf, this.hand);
         }
      }

      ByteBufTCG.writeCardList(buf, this.graveyard);
      buf.writeBoolean(this.trainersDisabled);
      int var4;
      int var8;
      if (this.prizeCards != null) {
         buf.writeInt(BoardLocation.Prize.ordinal());
         buf.writeInt(this.prizeCards.length);
         ImmutableCard[] var6 = this.prizeCards;
         var8 = var6.length;

         for(var4 = 0; var4 < var8; ++var4) {
            ImmutableCard prizeCard = var6[var4];
            buf.writeBoolean(prizeCard != null);
         }
      }

      if (this.trainerCard != null) {
         buf.writeInt(BoardLocation.Trainer.ordinal());
         ByteBufTCG.writeCard(buf, this.trainerCard.getData());
      }

      if (this.activeCard != null) {
         buf.writeInt(BoardLocation.Active.ordinal());
         this.activeCard.write(buf, this.showPokemonsInClient);
      }

      buf.writeInt(BoardLocation.Bench.ordinal());
      PokemonCardState[] var7 = this.benchCards;
      var8 = var7.length;

      for(var4 = 0; var4 < var8; ++var4) {
         PokemonCardState benchCard = var7[var4];
         buf.writeBoolean(benchCard != null);
         if (benchCard != null) {
            benchCard.write(buf, this.showPokemonsInClient);
         }
      }

      buf.writeInt(-1);
   }

   public List getHand() {
      return this.hand;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public boolean isReady() {
      return this.isReady;
   }

   public void setReady(boolean ready) {
      this.isReady = ready;
   }

   public TrainerCardState getTrainerCard() {
      return this.trainerCard;
   }

   public List getActiveAndBenchCards() {
      List cards = new ArrayList();
      if (this.activeCard != null) {
         cards.add(this.activeCard);
      }

      PokemonCardState[] var2 = this.benchCards;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PokemonCardState benchCard = var2[var4];
         if (benchCard != null) {
            cards.add(benchCard);
         }
      }

      return cards;
   }

   public List getActiveAndBenchCards(boolean isMine) {
      List cards = new ArrayList();
      if (this.activeCard != null) {
         cards.add(new CardWithLocation(this.activeCard, isMine, BoardLocation.Active, 0));
      }

      for(int i = 0; i < this.benchCards.length; ++i) {
         if (this.benchCards[i] != null) {
            cards.add(new CardWithLocation(this.benchCards[i], isMine, BoardLocation.Bench, i));
         }
      }

      return cards;
   }

   public PokemonCardState getActiveCard() {
      return this.activeCard;
   }

   public PokemonCardState[] getBenchCards() {
      return this.benchCards;
   }

   public void setTrainerCard(TrainerCardState trainerCard) {
      this.trainerCard = trainerCard;
   }

   public void setActiveCard(PokemonCardState activeCard) {
      this.activeCard = activeCard;
   }

   public void setBenchCards(PokemonCardState[] benchCards) {
      this.benchCards = benchCards;
   }

   public List getDiscardPile() {
      return this.graveyard;
   }

   public void setGraveyard(List graveyard) {
      this.graveyard = graveyard;
   }

   public ImmutableCard[] getPrizeCards() {
      return this.prizeCards;
   }

   public void setPrizeCards(ImmutableCard[] prizeCards) {
      this.prizeCards = prizeCards;
   }

   public boolean hasPrizeLeft() {
      ImmutableCard[] var1 = this.prizeCards;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ImmutableCard card = var1[var3];
         if (card != null) {
            return true;
         }
      }

      return false;
   }

   public boolean isInBench(CommonCardState card) {
      PokemonCardState[] var2 = this.benchCards;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PokemonCardState benchCard = var2[var4];
         if (benchCard == card) {
            return true;
         }
      }

      return false;
   }

   public boolean isChoosingOppAttack() {
      return this.isChoosingOppAttack;
   }

   public void setChoosingOppAttack(boolean choosingOppAttack) {
      this.isChoosingOppAttack = choosingOppAttack;
   }

   public String getCardBackID() {
      return this.cardBackID;
   }

   public String getCoinSetID() {
      return this.coinSetID;
   }

   public Coin getCoin() {
      return CoinRegistry.get(this.getCoinSetID());
   }

   public boolean areTrainersDisabled() {
      return this.trainersDisabled;
   }

   public void setAreTrainersDisabled(boolean y) {
      this.trainersDisabled = y;
   }

   public PokemonCardState getCard(BoardLocation location, int locationSubindex) {
      switch (location) {
         case Active:
            return this.activeCard;
         case Bench:
            return this.benchCards[locationSubindex];
         default:
            return null;
      }
   }

   public int getPendingPrizePlayerIndex() {
      return this.pendingPrizePlayerIndex;
   }

   public int getPendingPrizeCount() {
      return this.pendingPrizeCount;
   }

   public boolean isInGUI() {
      return this.isInGUI;
   }

   public void setInGUI(boolean inGUI) {
      this.isInGUI = inGUI;
   }

   public Date getCounterEndTime() {
      return this.counterEndTime;
   }
}

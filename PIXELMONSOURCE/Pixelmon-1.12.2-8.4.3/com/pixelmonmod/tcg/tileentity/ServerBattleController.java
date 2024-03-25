package com.pixelmonmod.tcg.tileentity;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.api.events.EndGameEvent;
import com.pixelmonmod.tcg.api.events.EndTurnEvent;
import com.pixelmonmod.tcg.api.events.StartGameEvent;
import com.pixelmonmod.tcg.duel.ability.BaseAbilityEffect;
import com.pixelmonmod.tcg.duel.attack.PendingAttack;
import com.pixelmonmod.tcg.duel.attack.effects.BaseAttackEffect;
import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.dto.CustomGUI;
import com.pixelmonmod.tcg.duel.state.AvailableActions;
import com.pixelmonmod.tcg.duel.state.CardSelectorResult;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CoinFlipState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.CustomGUIResult;
import com.pixelmonmod.tcg.duel.state.DelayEffect;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.duel.trainer.BaseTrainerEffect;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.helper.GuiHelper;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.lang.LanguageMapTCG;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.battles.EndGamePacket;
import com.pixelmonmod.tcg.network.packets.battles.GameStateSyncPacket;
import com.pixelmonmod.tcg.network.packets.battles.PrizeSelectorToClientPacket;
import com.pixelmonmod.tcg.network.packets.battles.RenderStatePreBattleSyncPacket;
import com.pixelmonmod.tcg.network.packets.battles.RenderStateSyncPacket;
import com.pixelmonmod.tcg.network.packets.battles.TrainerPlayedPacket;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ServerBattleController extends TileEntityBattleController {
   private GameServerState server;
   private boolean readyToEndTurn = false;
   private static final int MILLIS_EVERY_RENDER_SYNC = 500;
   private static final int MILLIS_EVERY_GAME_SYNC = 100;
   private static final int MILLIS_EVERY_GAME_SYNC_SPECTATOR = 1000;
   private static final int MILLIS_EVERY_AVAILABILITY_SYNC = 2000;
   private long syncRenderTime = 0L;
   private long syncGameTime = 0L;
   private long syncGameSpectatorTime = 0L;
   private long syncAvailabilityTime = 0L;
   private int mulliganDifference = 0;
   private List mulliganStates0 = new ArrayList();
   private List mulliganStates1 = new ArrayList();

   public GameServerState getGameServer() {
      return this.server;
   }

   public void func_73660_a() {
      if (this.field_145850_b.field_72995_K) {
         super.initialize();
      } else {
         if (!this.init) {
            this.newGame();
         }

         int i;
         int var3;
         int i;
         PlayerServerState player;
         if (this.server.isGameInProgress()) {
            i = 0;
            PlayerServerState[] var2 = this.server.getPlayers();
            var3 = var2.length;

            for(i = 0; i < var3; ++i) {
               player = var2[i];
               if (player != null && player.getEntityPlayer() != null) {
                  if (player.getEntityPlayer().func_130014_f_().func_152378_a(player.getEntityPlayer().func_110124_au()) == null) {
                     player.setInGUI(false);
                     ++i;
                  }
               } else {
                  ++i;
               }
            }

            if (i == 2) {
               this.endGame((PlayerServerState)null, (PlayerServerState)null, false);
               return;
            }
         }

         if (!this.server.isGameInProgress()) {
            this.sendRenderPreBattleSyncPackets();
            i = 0;

            label421:
            while(true) {
               if (i < this.server.getPlayers().length) {
                  PlayerServerState player = this.server.getPlayers()[i];
                  if (player == null) {
                     return;
                  }

                  if (player.isInGUI() && player.getEntityPlayer() != null) {
                     if (!player.hasDeck()) {
                        this.server.getPlayers()[i] = null;
                        PacketHandler.net.sendTo(new EndGamePacket(this.field_174879_c), (EntityPlayerMP)player.getEntityPlayer());
                        ChatHandler.sendChat(player.getEntityPlayer(), TextFormatting.RED + "You don't have a valid deck!");
                        return;
                     }

                     if (player.getEntityPlayer().func_130014_f_().func_152378_a(player.getEntityPlayer().func_110124_au()) == null) {
                        this.server.getPlayers()[i] = null;
                        return;
                     }

                     ++i;
                     continue;
                  }

                  this.server.getPlayers()[i] = null;
                  return;
               }

               this.server.setGameInProgress(true);
               if (this.startingCommand != null) {
                  this.trigger(this.field_145850_b, this.startingCommand, this.server.getPlayers()[0].getPlayerName(), this.server.getPlayers()[1].getPlayerName());
               }

               PlayerServerState[] var12 = this.server.getPlayers();
               int var13 = var12.length;
               var3 = 0;

               while(true) {
                  if (var3 >= var13) {
                     break label421;
                  }

                  PlayerServerState player = var12[var3];
                  player.prepareDeck();
                  ++var3;
               }
            }
         }

         if (this.server.isGameInProgress()) {
            long currentTime = System.currentTimeMillis();
            if (this.syncRenderTime < currentTime - 500L) {
               this.syncRenderTime = currentTime;
               this.sendRenderSyncPackets();
            }

            if (this.syncGameTime < currentTime - 100L) {
               this.syncGameTime = currentTime;
               this.sendGameSyncPackets();
            }

            if (this.syncGameSpectatorTime < currentTime - 1000L) {
               this.syncGameSpectatorTime = currentTime;
               this.sendGameSyncPacketsToSpectators();
            }

            PlayerServerState player;
            int playerIndex;
            if (this.syncAvailabilityTime < currentTime - 2000L) {
               this.syncAvailabilityTime = currentTime;
               PlayerServerState[] var16 = this.server.getPlayers();
               i = var16.length;

               for(playerIndex = 0; playerIndex < i; ++playerIndex) {
                  player = var16[playerIndex];
                  if (player != null && player.getEntityPlayer() != null && player.getEntityPlayer().func_130014_f_().func_152378_a(player.getEntityPlayer().func_110124_au()) == null) {
                     player.setInGUI(false);
                  }
               }
            }

            if (this.readyToEndTurn) {
               this.endTurn();
            } else {
               GamePhase nextPhase = this.server.getGamePhase();
               switch (this.server.getGamePhase()) {
                  case FlippingCoin:
                     nextPhase = this.handleFlippingCoin();
                     break;
                  case PreMatch:
                     nextPhase = this.handlePreMatch(nextPhase);
                     break;
                  case BetweenTurns:
                     this.handleBetweenTurn();
                     return;
               }

               int var8;
               int var9;
               int playerIndex;
               if (this.server.getGamePhase() == GamePhase.PreMatch) {
                  for(i = 0; i < 2; ++i) {
                     player = this.server.getPlayer(i);
                     if (player == this.server.getPlayer(0) && !this.mulliganStates0.isEmpty()) {
                        if (player.getCardSelectorState() == null) {
                           player.setCardSelectorState((CardSelectorState)this.mulliganStates0.get(0));
                           return;
                        }

                        if (player.getCardSelectorState().getDisplayType() == CardSelectorDisplay.Select && player.getCardSelectorResult() != null && player.getCardSelectorResult().getSelection() != null) {
                           player.setCardSelectorResult((CardSelectorResult)null);
                           player.setCardSelectorState((CardSelectorState)null);
                           this.mulliganStates0.remove(0);
                           return;
                        }
                     }

                     if (player == this.server.getPlayer(1) && !this.mulliganStates1.isEmpty()) {
                        if (player.getCardSelectorState() == null) {
                           player.setCardSelectorState((CardSelectorState)this.mulliganStates1.get(0));
                           return;
                        }

                        if (player.getCardSelectorState().getDisplayType() == CardSelectorDisplay.Select && player.getCardSelectorResult() != null && player.getCardSelectorResult().getSelection() != null) {
                           player.setCardSelectorResult((CardSelectorResult)null);
                           player.setCardSelectorState((CardSelectorState)null);
                           this.mulliganStates1.remove(0);
                           return;
                        }
                     }

                     if (player.getCardSelectorState() != null && player.getCardSelectorState().getDisplayType() == CardSelectorDisplay.Draw && player.getCardSelectorResult() == null) {
                        return;
                     }

                     if (player.getCardSelectorResult() != null && player.getCardSelectorState().getDisplayType() == CardSelectorDisplay.Draw && player.getCardSelectorResult().getSelection() != null) {
                        playerIndex = 0;
                        boolean[] var7 = player.getCardSelectorResult().getSelection();
                        var8 = var7.length;

                        for(var9 = 0; var9 < var8; ++var9) {
                           boolean b = var7[var9];
                           if (b) {
                              ++playerIndex;
                           }
                        }

                        player.drawCards(playerIndex, this.server);
                        player.setCardSelectorResult((CardSelectorResult)null);
                        player.setCardSelectorState((CardSelectorState)null);
                     }
                  }
               }

               if (this.server.getGamePhase().after(GamePhase.PreMatch)) {
                  boolean hasPending = false;
                  if (this.server.exceedTimeLimit()) {
                     player = this.server.getPlayer(this.server.getCurrentTurn());
                     if (player.isChoosingOppAttack()) {
                        this.requestPickAttack(this.server.getCurrentTurn(), 0);
                     }

                     if (player.getCustomGUI() != null) {
                        player.useCustomGUIDefaultResult();
                     }
                  }

                  if (this.server.getPendingAbility() != null) {
                     if (this.handlePendingAbility()) {
                        return;
                     }

                     hasPending = true;
                  }

                  if (this.server.getPendingAttack() != null) {
                     if (this.server.getCurrentEffectIndex() >= 0 && this.handlePendingEffects()) {
                        return;
                     }

                     if (this.handlePendingAttack()) {
                        return;
                     }

                     hasPending = true;
                  }

                  PlayerServerState[] var22 = this.server.getPlayers();
                  playerIndex = var22.length;

                  int i;
                  PlayerServerState next;
                  for(i = 0; i < playerIndex; ++i) {
                     next = var22[i];
                     if (next.getTrainerCard() != null && next.getTrainerCard().getData().getEffect() != null) {
                        if (this.handlePendingTrainer(next)) {
                           return;
                        }

                        hasPending = true;
                     }
                  }

                  var22 = this.server.getPlayers();
                  playerIndex = var22.length;

                  for(i = 0; i < playerIndex; ++i) {
                     next = var22[i];
                     if (next.getCardSelectorState() != null && next.getCardSelectorState().getDisplayType() == CardSelectorDisplay.Reveal && next.getCardSelectorResult() != null && next.getCardSelectorResult().isOpened()) {
                        next.setCardSelectorState((CardSelectorState)null);
                     }
                  }

                  for(playerIndex = 0; playerIndex < 2; ++playerIndex) {
                     player = this.server.getPlayer(playerIndex);
                     i = player.getOpeningPrizeIndex();
                     if (i >= 0) {
                        if (player.getPrizeCards()[i] != null) {
                           ImmutableCard prize = player.getPrizeCards()[i];
                           player.getHand().add(prize);
                           player.getPrizeCards()[i] = null;
                           player.addPendingPrizeCount(playerIndex, -1);
                           PacketHandler.net.sendTo(new PrizeSelectorToClientPacket(this.field_174879_c, i, prize), (EntityPlayerMP)player.getEntityPlayer());
                        }

                        player.setOpeningPrizeIndex(-1);
                     }
                  }

                  boolean hasFainted = false;

                  PlayerServerState current;
                  int i;
                  for(playerIndex = 0; playerIndex < 2; ++playerIndex) {
                     current = this.server.getPlayer(playerIndex);
                     MutableInt prizeCount = new MutableInt(0);
                     PokemonCardState activeCard = current.getActiveCard();
                     if (activeCard != null && this.checkKnockedOut(current, activeCard, prizeCount)) {
                        hasFainted = true;
                        current.setActiveCard((PokemonCardState)null);
                     }

                     for(i = 0; i < current.getBenchCards().length; ++i) {
                        if (current.getBenchCards()[i] != null && this.checkKnockedOut(current, current.getBenchCards()[i], prizeCount)) {
                           hasFainted = true;
                           current.getBenchCards()[i] = null;
                        }
                     }

                     if (prizeCount.intValue() > 0) {
                        this.server.getOpponent(current).addPendingPrizeCount((playerIndex + 1) % 2, prizeCount.getValue());
                     }
                  }

                  if (hasFainted) {
                     this.requestSyncGame();
                     return;
                  }

                  int[] winningScores = new int[this.server.getPlayers().length];

                  for(i = 0; i < this.server.getPlayers().length; ++i) {
                     next = this.server.getPlayer(i);
                     if (!next.hasPokemonLeft()) {
                        ChatHandler.sendFormattedChat(next.getEntityPlayer(), TextFormatting.RED, "You have no Pokemon cards left on the board!");
                        ChatHandler.sendFormattedChat(this.server.getOpponent(next).getEntityPlayer(), TextFormatting.YELLOW, next.getPlayerName() + " has no Pokemon cards left on the board!");
                        ++winningScores[(i + 1) % 2];
                     }

                     if (next.getPrizeCards() != null && !next.hasPrizeLeft()) {
                        ChatHandler.sendFormattedChat(next.getEntityPlayer(), TextFormatting.YELLOW, "You have won all prize cards!");
                        ChatHandler.sendFormattedChat(this.server.getOpponent(next).getEntityPlayer(), TextFormatting.RED, next.getPlayerName() + " has won all prize cards!");
                        int var10002 = winningScores[i]++;
                     }
                  }

                  if (winningScores[0] > 0 || winningScores[1] > 0) {
                     current = null;
                     next = null;
                     boolean tiedGame = false;
                     if (winningScores[0] > winningScores[1]) {
                        current = this.server.getPlayer(0);
                        next = this.server.getPlayer(1);
                     } else if (winningScores[0] < winningScores[1]) {
                        current = this.server.getPlayer(1);
                        next = this.server.getPlayer(0);
                     } else {
                        current = this.server.getPlayer(0);
                        next = this.server.getPlayer(1);
                        tiedGame = true;
                     }

                     if (current != null && next != null) {
                        this.endGame(current, next, tiedGame);
                        return;
                     }
                  }

                  PlayerServerState[] var35 = this.server.getPlayers();
                  var8 = var35.length;

                  for(var9 = 0; var9 < var8; ++var9) {
                     PlayerServerState player = var35[var9];
                     if (player.getActiveCard() == null && player.hasPokemonLeft() && this.server.getOpponent(player).hasPrizeLeft()) {
                        if (this.server.exceedTimeLimit()) {
                           player.setActiveCard((PokemonCardState)player.getActiveAndBenchCards().get(0));
                        }

                        return;
                     }
                  }

                  if (this.server.exceedTimeLimit()) {
                     current = this.server.getPlayer(this.server.getCurrentTurn());
                     next = this.server.getPlayer(this.server.getNextTurn());
                     CardSelectorState selector = current.getCardSelectorState();
                     if (selector != null && selector.getMaximumCount() > 0) {
                        if (selector.isCancellable()) {
                           current.setCardSelectorState((CardSelectorState)null);
                        } else {
                           CardSelectorResult result = new CardSelectorResult();
                           result.setSelection(new boolean[selector.getCardList().size()]);
                           result.setOpened(true);

                           for(int i = 0; i < selector.getMinimumCount(); ++i) {
                              result.getSelection()[i] = true;
                           }

                           current.setCardSelectorResult(result);
                        }

                        return;
                     }

                     if (current.getPendingPrizeCount() > 0) {
                        for(i = 0; i < current.getPendingPrizeCount(); ++i) {
                           if (current.getPrizeCards()[i] != null) {
                              this.setPrizeSelection(current.getEntityPlayer(), i);
                           }
                        }

                        return;
                     }

                     if (!hasPending) {
                        this.requestEndTurn(this.server.getPlayer(this.server.getCurrentTurn()));
                     }
                  }
               }

               if (this.server.isGameInProgress()) {
                  this.server.setGamePhase(nextPhase);
               }

            }
         }
      }
   }

   private void handleBetweenTurn() {
      if (!this.handleConditionEffects()) {
         PlayerServerState lastPlayer = this.server.getPlayer(this.server.getCurrentTurn());
         PlayerServerState nextPlayer = this.server.getPlayer(this.server.getNextTurn());
         if (nextPlayer.getActiveCard() != null) {
            nextPlayer.getActiveCard().getStatus().setDamageImmune(false);
            nextPlayer.getActiveCard().getStatus().setConditionImmune(false);
         }

         PokemonCardState[] var3 = nextPlayer.getBenchCards();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PokemonCardState benchCard = var3[var5];
            if (benchCard != null) {
               benchCard.getStatus().setDamageImmune(false);
               benchCard.getStatus().setConditionImmune(false);
            }
         }

         Iterator var9 = lastPlayer.getActiveAndBenchCards().iterator();

         while(var9.hasNext()) {
            PokemonCardState card = (PokemonCardState)var9.next();
            PokemonAttackStatus[] var13 = card.getAttacksStatus();
            int var15 = var13.length;

            for(int var7 = 0; var7 < var15; ++var7) {
               PokemonAttackStatus attackStatus = var13[var7];
               attackStatus.reduceTurnCount();
            }
         }

         this.server.setCurrentTurn(this.server.getNextTurn());
         PlayerServerState currentPlayer = this.server.getPlayer(this.server.getCurrentTurn());
         if (currentPlayer.getDeck().size() == 0) {
            if (currentPlayer.getEntityPlayer() != null) {
               ChatHandler.sendFormattedChat(currentPlayer.getEntityPlayer(), TextFormatting.RED, "You ran out of card to draw!");
            }

            if (lastPlayer.getEntityPlayer() != null) {
               ChatHandler.sendFormattedChat(lastPlayer.getEntityPlayer(), TextFormatting.RED, (currentPlayer.getEntityPlayer() != null ? currentPlayer.getEntityPlayer().getDisplayNameString() : "Your opponent") + " ran out of card to draw!");
            }

            this.endGame(lastPlayer, currentPlayer, false);
         } else {
            this.server.setTurnCount(this.server.getTurnCount() + 1);
            GuiHelper.showTitle(new TextComponentString("Your turn started"), (EntityPlayerMP)currentPlayer.getEntityPlayer(), 10, 30, 10);
            this.server.getLog().trackStartTurn(this.server.getTurn(nextPlayer), this.server);
            this.setTurnTimeLimit();
            currentPlayer.drawCards(1, this.server);
            this.server.setGamePhase(GamePhase.NormalTurn);
            currentPlayer.getAvailableActions().setForNormalTurn();
            this.server.setHandledPoisoned(false);
            this.server.setHandledBurned(false);
            this.server.setHandledAsleep(false);
            this.server.setHandledParalyzed(false);
            Iterator var12 = this.server.getDelayEffects().iterator();

            while(var12.hasNext()) {
               DelayEffect effect = (DelayEffect)var12.next();
               if (effect.getTurn() == this.server.getTurnCount()) {
                  effect.getEffect().modifyTurn(effect.getPokemon(), this.server);
               }
            }

         }
      }
   }

   private void setTurnTimeLimit() {
      if (this.timeLimit > 0) {
         Calendar calendar = Calendar.getInstance();
         calendar.add(13, this.timeLimit);
         this.server.getPlayer(this.server.getCurrentTurn()).setCounterEndTime(calendar.getTime());
         this.server.getPlayer(this.server.getNextTurn()).setCounterEndTime((Date)null);
      }

   }

   private boolean handleConditionEffects() {
      PlayerServerState[] var1;
      int var2;
      int playerIndex;
      PlayerServerState player;
      Iterator var5;
      PokemonCardState card;
      Optional status;
      if (!this.server.isHandledPoisoned()) {
         var1 = this.server.getPlayers();
         var2 = var1.length;

         label119:
         for(playerIndex = 0; playerIndex < var2; ++playerIndex) {
            player = var1[playerIndex];
            var5 = player.getActiveAndBenchCards().iterator();

            while(true) {
               do {
                  if (!var5.hasNext()) {
                     continue label119;
                  }

                  card = (PokemonCardState)var5.next();
                  status = card.getStatus().getConditions().stream().filter((s) -> {
                     return s.getLeft() == CardCondition.POISONED;
                  }).findFirst();
               } while(!status.isPresent());

               Integer modifier = (Integer)((Pair)status.get()).getRight();
               if (modifier == null || modifier < 2) {
                  modifier = 1;
               }

               card.addDamageFromCondition(10 * modifier);
               this.server.getLog().trackCondition(CardCondition.POISONED, card, 10 * modifier, false, this.server.getTurn(player), this.server);
            }
         }

         this.server.setHandledPoisoned(true);
      }

      Pair pair;
      PokemonCardState card;
      ArrayList coinFlips;
      if (!this.server.isHandledBurned()) {
         if (this.server.getBurningCard() != null) {
            if (this.server.getRevealedCoinFlipResults() <= 1) {
               return true;
            }

            if (this.server.getCoinFlip().getResults().get(0) == CoinSide.Tail) {
               this.server.getBurningCard().addDamageFromCondition(20);
            }

            this.server.setBurningCard((PokemonCardState)null);
            this.server.setCoinFlip((CoinFlipState)null);
         }

         if (this.server.getResolvingConditionCards() == null) {
            this.server.setResolvingConditionCards(this.getCardsWithCondition(CardCondition.BURNT));
         }

         if (!this.server.getResolvingConditionCards().isEmpty()) {
            pair = (Pair)this.server.getResolvingConditionCards().remove();
            card = (PokemonCardState)pair.getLeft();
            playerIndex = (Integer)pair.getRight();
            this.server.setBurningCard(card);
            coinFlips = new ArrayList();
            coinFlips.add(CoinSide.getRandom());
            this.server.setCoinFlip(new CoinFlipState(coinFlips, playerIndex));
            this.server.setRevealedCoinFlipResults(1);
            return true;
         }

         this.server.setResolvingConditionCards((Queue)null);
         this.server.setHandledBurned(true);
      }

      if (!this.server.isHandledAsleep()) {
         if (this.server.getSleepingCard() != null) {
            if (this.server.getRevealedCoinFlipResults() <= 1) {
               return true;
            }

            boolean healed = this.server.getCoinFlip().getResults().get(0) == CoinSide.Head;
            if (healed) {
               this.server.getSleepingCard().getStatus().removeCondition(CardCondition.ASLEEP);
            }

            this.server.getLog().trackCondition(CardCondition.ASLEEP, this.server.getSleepingCard(), 0, healed, this.server.getNextTurn(), this.server);
            this.server.setSleepingCard((PokemonCardState)null);
            this.server.setCoinFlip((CoinFlipState)null);
         }

         if (this.server.getResolvingConditionCards() == null) {
            this.server.setResolvingConditionCards(this.getCardsWithCondition(CardCondition.ASLEEP));
         }

         if (!this.server.getResolvingConditionCards().isEmpty()) {
            pair = (Pair)this.server.getResolvingConditionCards().remove();
            card = (PokemonCardState)pair.getLeft();
            playerIndex = (Integer)pair.getRight();
            this.server.setSleepingCard(card);
            coinFlips = new ArrayList();
            coinFlips.add(CoinSide.getRandom());
            this.server.setCoinFlip(new CoinFlipState(coinFlips, playerIndex));
            this.server.setRevealedCoinFlipResults(1);
            return true;
         }

         this.server.setResolvingConditionCards((Queue)null);
         this.server.setHandledAsleep(true);
      }

      if (!this.server.isHandledParalyzed()) {
         var1 = this.server.getPlayers();
         var2 = var1.length;

         for(playerIndex = 0; playerIndex < var2; ++playerIndex) {
            player = var1[playerIndex];
            var5 = player.getActiveAndBenchCards().iterator();

            while(var5.hasNext()) {
               card = (PokemonCardState)var5.next();
               status = card.getStatus().getConditions().stream().filter((s) -> {
                  return s.getLeft() == CardCondition.PARALYZED;
               }).findFirst();
               if (status.isPresent()) {
                  if (card.getStatus().isStartTurnParalyzed()) {
                     card.getStatus().removeCondition(CardCondition.PARALYZED);
                     this.server.getLog().trackCondition(CardCondition.PARALYZED, card, 0, true, this.server.getTurn(player), this.server);
                  } else if (player != this.server.getPlayer(this.server.getCurrentTurn())) {
                     card.getStatus().setStartTurnParalyzed(true);
                     this.server.getLog().trackCondition(CardCondition.PARALYZED, card, 0, false, this.server.getTurn(player), this.server);
                  }
               }
            }
         }

         this.server.setHandledParalyzed(true);
      }

      return false;
   }

   private boolean handlePendingAbility() {
      PlayerServerState player = this.server.getPlayer(this.server.getCurrentTurn());
      PokemonCardState pokemon = this.server.getPendingAbility();
      BaseAbilityEffect effect = pokemon.getAbility().getEffect();
      if (effect != null) {
         if (this.server.getCoinFlip() == null) {
            List flips = effect.flipCoin();
            this.server.setCoinFlip(new CoinFlipState(flips, this.server.getCurrentTurn()));
            if (flips.isEmpty()) {
               this.server.setRevealedCoinFlipResults(0);
            } else {
               this.server.setRevealedCoinFlipResults(1);
            }
         }

         if (this.server.getCoinFlip() != null && !this.server.getCoinFlip().getResults().isEmpty() && this.server.getRevealedCoinFlipResults() <= this.server.getCoinFlip().getResults().size()) {
            this.forceRevealingCoinFlips();
            return true;
         }

         CustomGUI customGUI = player.getCustomGUI() == null ? effect.getCustomGUI(pokemon, this.server) : null;
         if (customGUI != null) {
            player.setCustomGUI(customGUI);
            this.requestSyncGame();
            return true;
         }

         this.server.getOpponent(player);
         CardSelectorState mySelector = player.getCardSelectorState() == null ? effect.getSelectorState(pokemon, this.server) : null;
         if (mySelector != null) {
            player.setCardSelectorState(mySelector);
            this.requestSyncGame();
            return true;
         }

         this.saveSelectorResult(pokemon.getParameters(), player, player.getCardSelectorResult());
         if (effect.canActivate(pokemon, this.server)) {
            effect.activate(pokemon, this.server, player);
            this.server.setPendingAbility((PokemonCardState)null);
            player.setCardSelectorState((CardSelectorState)null);
            player.setCardSelectorResult((CardSelectorResult)null);
            effect.cleanUp(pokemon, this.server);
            this.server.getLog().trackAbility(pokemon, this.server.getTurn(player), this.server);
            this.server.setCoinFlip((CoinFlipState)null);
            return true;
         }
      }

      return false;
   }

   private boolean handlePendingAttack() {
      PokemonAttackStatus attack = this.server.getPendingAttack().getAttack();
      PlayerServerState player = this.server.getPendingAttack().getPlayer();
      PokemonCardState active = this.server.getPendingAttack().getPokemon();
      if (!this.server.isHandledConfusedFlip() && active.getStatus().getConditions().stream().anyMatch((c) -> {
         return c.getLeft() == CardCondition.CONFUSED;
      })) {
         if (this.server.getCoinFlip() == null) {
            List coinFlips = new ArrayList();
            coinFlips.add(CoinSide.getRandom());
            this.server.setCoinFlip(new CoinFlipState(coinFlips, this.server.getCurrentTurn()));
            this.server.setRevealedCoinFlipResults(1);
            return true;
         }

         if (this.server.getRevealedCoinFlipResults() <= this.server.getCoinFlip().getResults().size()) {
            this.forceRevealingCoinFlips();
            return true;
         }

         CoinSide flipResult = (CoinSide)this.server.getCoinFlip().getResults().get(0);
         this.server.setCoinFlip((CoinFlipState)null);
         this.server.setRevealedCoinFlipResults(0);
         if (flipResult == CoinSide.Tail) {
            active.addDamage(active, 30, this.server);
            this.server.setHandledConfusedFlip(false);
            this.server.setPendingAttack((PendingAttack)null);
            this.requestEndTurn(player);
            return true;
         }

         this.server.setHandledConfusedFlip(true);
      }

      AttackCard data = attack.getData();
      if (!attack.isMissed()) {
         if (attack.getTemporaryEffect() != null && this.server.getCoinFlip() == null) {
            List flips = attack.getTemporaryEffect().flipCoin(new ArrayList(), active, this.server);
            this.server.setCoinFlip(new CoinFlipState(flips, this.server.getCurrentTurn()));
            if (flips.isEmpty()) {
               this.server.setRevealedCoinFlipResults(0);
            } else {
               this.server.setRevealedCoinFlipResults(1);
            }
         }

         if (data.getEffects() != null && this.server.getCoinFlip() == null) {
            List flips = new ArrayList();

            BaseAttackEffect effect;
            for(Iterator var6 = data.getEffects().iterator(); var6.hasNext(); flips = effect.flipCoin((List)flips, active, this.server)) {
               effect = (BaseAttackEffect)var6.next();
            }

            this.server.setCoinFlip(new CoinFlipState((List)flips, this.server.getCurrentTurn()));
            if (((List)flips).isEmpty()) {
               this.server.setRevealedCoinFlipResults(0);
            } else {
               this.server.setRevealedCoinFlipResults(1);
            }
         }
      }

      if (this.server.getCoinFlip() != null && !this.server.getCoinFlip().getResults().isEmpty() && this.server.getRevealedCoinFlipResults() <= this.server.getCoinFlip().getResults().size()) {
         this.forceRevealingCoinFlips();
         return true;
      } else {
         if (!attack.isMissed()) {
            ArrayList parameters;
            if (attack.getTemporaryEffect() != null) {
               parameters = new ArrayList();
               parameters.add(new ArrayList());
               this.server.setEffectsParameters(parameters);
            } else if (data.getEffects() != null) {
               parameters = new ArrayList();

               for(int i = 0; i < data.getEffects().size(); ++i) {
                  parameters.add(new ArrayList());
               }

               this.server.setEffectsParameters(parameters);
            }
         }

         this.server.setCurrentEffectIndex(0);
         this.server.setRevealedCoinFlipResults(0);
         this.server.setHandledConfusedFlip(false);
         return true;
      }
   }

   private boolean handlePendingEffects() {
      PokemonAttackStatus attack = this.server.getPendingAttack().getAttack();
      List effects = attack.getData().getEffects();
      PlayerServerState player = this.server.getPendingAttack().getPlayer();
      PokemonCardState active = this.server.getPendingAttack().getPokemon();
      PlayerServerState opp = this.server.getOpponent(player);
      PokemonCardState oppPokemon = opp.getActiveCard();
      oppPokemon.getStatus().cloneConditions();
      if (oppPokemon.getAbility() != null && oppPokemon.getAbility().getEffect() != null) {
         if (oppPokemon.getAbility().getEffect().onAttacked(oppPokemon, active, this.server) == 0) {
            attack.setMissed(true);
         }

         if (oppPokemon.getAbility().getEffect().onAttacked(oppPokemon, active, this.server) == 1) {
            attack.setMissed(false);
         }
      }

      if (!attack.isMissed()) {
         int effectIndex = this.server.getCurrentEffectIndex();
         List parameters = this.server.getEffectsParameters();
         BaseAttackEffect currentEffect = null;
         if (attack.getTemporaryEffect() != null) {
            currentEffect = attack.getTemporaryEffect();
         } else if (effects != null && effectIndex >= 0 && effectIndex < effects.size()) {
            currentEffect = (BaseAttackEffect)effects.get(effectIndex);
         }

         if (currentEffect != null) {
            CustomGUI customGUI = player.getCustomGUI() == null ? currentEffect.getCustomGUI(active, this.server) : null;
            if (currentEffect.isOptional()) {
               if (customGUI != null) {
                  player.setCustomGUI(customGUI);
                  this.requestSyncGame();
                  return true;
               }

               if (player.getCustomGUI() != null && (player.getCustomGUIResult() == null || player.getCustomGUIResult().getResult() == null || player.getCustomGUIResult().getResult().length == 0)) {
                  return true;
               }
            }

            if (currentEffect.isOptional() && this.server.getPlayer(this.server.getCurrentTurn()).getCustomGUIResult().getResult()[0] == 2) {
               player.setCardSelectorState((CardSelectorState)null);
               player.setCardSelectorResult((CardSelectorResult)null);
            } else {
               CardSelectorState mySelector = player.getCardSelectorState() == null ? currentEffect.getSelectorState((List)parameters.get(effectIndex), this.server) : null;
               CardSelectorState oppSelector = opp.getCardSelectorState() == null ? currentEffect.getOpponentSelectorState(this.server) : null;
               if (mySelector != null) {
                  player.setCardSelectorState(mySelector);
               }

               if (oppSelector != null) {
                  opp.setCardSelectorState(oppSelector);
               }

               if (mySelector != null || oppSelector != null) {
                  this.requestSyncGame();
                  return true;
               }

               this.saveSelectorResult((List)parameters.get(effectIndex), player, player.getCardSelectorResult());
               this.saveSelectorResult((List)parameters.get(effectIndex), opp, opp.getCardSelectorResult());
            }

            if (currentEffect.chooseOppAttack()) {
               player.setChoosingOppAttack(true);
            }

            if (currentEffect.canApply((List)parameters.get(effectIndex), attack.getData(), this.server)) {
               currentEffect.applyBeforeDamage((List)parameters.get(effectIndex), attack, active, this.server);
               if (attack.getTemporaryEffect() == null) {
                  this.server.setCurrentEffectIndex(this.server.getCurrentEffectIndex() + 1);
               } else {
                  attack.setTemporaryEffect((BaseAttackEffect)null, (String)null);
                  this.server.setCoinFlip((CoinFlipState)null);
                  this.server.setCurrentEffectIndex(-1);
                  this.server.setEffectsParameters((List)null);
               }

               player.setChoosingOppAttack(false);
               player.setCardSelectorState((CardSelectorState)null);
               opp.setCardSelectorState((CardSelectorState)null);
               player.setCardSelectorResult((CardSelectorResult)null);
               opp.setCardSelectorResult((CardSelectorResult)null);
            }

            return true;
         }

         List conditionsEffect = new ArrayList();
         if (oppPokemon.getStatus().getConditions() != null) {
            conditionsEffect.addAll(oppPokemon.getStatus().getConditions());
         }

         Iterator var11 = oppPokemon.getStatus().getClonedConditions().iterator();

         label210:
         while(true) {
            Optional matched;
            Integer oldCondition;
            Integer newCondition;
            do {
               Pair pair;
               do {
                  if (!var11.hasNext()) {
                     int damage = attack.getDamage();
                     if (oppPokemon.getWeakness() == active.getMainEnergy()) {
                        damage = oppPokemon.getWeaknessModifier().apply(damage, oppPokemon.getWeaknessValue());
                     } else if (oppPokemon.getResistance() == active.getMainEnergy()) {
                        damage = oppPokemon.getResistanceModifier().apply(damage, oppPokemon.getResistanceValue());
                     }

                     Iterator var20 = this.server.getDelayEffects().iterator();

                     while(var20.hasNext()) {
                        DelayEffect effect = (DelayEffect)var20.next();
                        if (effect.getTurn() == this.server.getTurnCount()) {
                           damage = effect.getEffect().modifyDamage(damage, effect.getPokemon(), this.server);
                        }
                     }

                     var20 = oppPokemon.getAttachments().iterator();

                     CommonCardState attachment;
                     TrainerCardState trainer;
                     while(var20.hasNext()) {
                        attachment = (CommonCardState)var20.next();
                        if (attachment instanceof TrainerCardState) {
                           trainer = (TrainerCardState)attachment;
                           damage = trainer.getData().getEffect().modifyDamage(damage, trainer, this.server);
                        }
                     }

                     var20 = active.getAttachments().iterator();

                     while(var20.hasNext()) {
                        attachment = (CommonCardState)var20.next();
                        if (attachment instanceof TrainerCardState) {
                           trainer = (TrainerCardState)attachment;
                           damage = trainer.getData().getEffect().modifyDamage(damage, trainer, this.server);
                        }
                     }

                     if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemScales)) && damage > 10) {
                        damage = 10;
                     }

                     int damageBefore = oppPokemon.getStatus().getDamage();
                     oppPokemon.addDamage(active, damage, this.server);
                     int finalDamage = oppPokemon.getStatus().getDamage() - damageBefore;
                     if (effects != null) {
                        for(int i = 0; i < effects.size(); ++i) {
                           BaseAttackEffect effect = (BaseAttackEffect)effects.get(i);
                           effect.applyAfterDamage((List)parameters.get(i), attack, active, this.server, finalDamage);
                        }
                     }

                     this.server.getLog().trackAttack(attack.getData(), active, oppPokemon, damage, conditionsEffect, this.server.getTurn(player), this.server);
                     break label210;
                  }

                  pair = (Pair)var11.next();
                  matched = conditionsEffect.stream().filter((p) -> {
                     return p.getLeft() == pair.getLeft();
                  }).findFirst();
               } while(!matched.isPresent());

               oldCondition = (Integer)((Pair)matched.get()).getRight();
               newCondition = (Integer)pair.getRight();
            } while((oldCondition != null || newCondition != null) && (oldCondition == null || newCondition == null || !oldCondition.equals(newCondition)));

            conditionsEffect.remove((Pair)matched.get());
         }
      } else {
         this.server.getLog().trackAttack(attack.getData(), active, oppPokemon, 0, new ArrayList(), this.server.getTurn(player), this.server);
      }

      Iterator var16 = this.server.getDelayEffects().iterator();

      while(var16.hasNext()) {
         DelayEffect effect = (DelayEffect)var16.next();
         if (effect.getTurn() == this.server.getTurnCount()) {
            effect.getEffect().applyDelayAfterDamage(effect.getPokemon(), this.server);
         }
      }

      this.requestEndTurn(player);
      this.server.setCoinFlip((CoinFlipState)null);
      this.server.setPendingAttack((PendingAttack)null);
      this.server.setCurrentEffectIndex(-1);
      this.server.setEffectsParameters((List)null);
      return true;
   }

   private boolean handlePendingTrainer(PlayerServerState player) {
      TrainerCardState trainer = player.getTrainerCard();
      BaseTrainerEffect effect = trainer == null ? null : trainer.getData().getEffect();
      if (trainer != null && effect != null) {
         if (this.server.getCoinFlip() == null) {
            List flips = effect.flipCoin();
            this.server.setCoinFlip(new CoinFlipState(flips, this.server.getCurrentTurn()));
            if (flips.isEmpty()) {
               this.server.setRevealedCoinFlipResults(0);
            } else {
               this.server.setRevealedCoinFlipResults(1);
            }
         }

         if (this.server.getCoinFlip() != null && !this.server.getCoinFlip().getResults().isEmpty() && this.server.getRevealedCoinFlipResults() <= this.server.getCoinFlip().getResults().size()) {
            this.forceRevealingCoinFlips();
            return true;
         }

         PlayerServerState opp = this.server.getOpponent(player);
         CardSelectorState mySelector = player.getCardSelectorState() == null ? effect.getSelectorState(trainer, this.server) : null;
         if (mySelector != null) {
            player.setCardSelectorState(mySelector);
         }

         if (mySelector != null) {
            this.requestSyncGame();
            return true;
         }

         CardSelectorResult selectorResult = player.getCardSelectorResult();
         this.saveSelectorResult(trainer.getParameters(), player, selectorResult);
         if (effect.canApply(trainer, this.server)) {
            CardSelectorState oppSelector = opp.getCardSelectorState() == null ? effect.getOpponentRevealingSelectorState(trainer, this.server) : null;
            if (oppSelector != null) {
               opp.setCardSelectorState(oppSelector);
            }

            if (oppSelector != null) {
               this.requestSyncGame();
               return true;
            }

            player.setCardSelectorState((CardSelectorState)null);
            opp.setCardSelectorState((CardSelectorState)null);
            player.setCardSelectorResult((CardSelectorResult)null);
            opp.setCardSelectorResult((CardSelectorResult)null);
            effect.apply(trainer, this.server);
            if (!effect.preventDiscard()) {
               player.getDiscardPile().add(trainer.getData());
            }

            player.setTrainerCard((TrainerCardState)null);
            this.server.setCoinFlip((CoinFlipState)null);
            return true;
         }
      }

      return false;
   }

   private void saveSelectorResult(List parameters, PlayerServerState player, CardSelectorResult selectorResult) {
      if (parameters == null) {
         throw new IllegalArgumentException("parameters cannot be null!");
      } else {
         if (selectorResult != null && selectorResult.getSelection() != null) {
            if (player.getCardSelectorState() == null) {
               throw new IllegalArgumentException("cardSelectorState cannot be null!");
            }

            if (player.getCardSelectorState().getCardList() == null) {
               throw new IllegalArgumentException("cardSelectorState.cardList cannot be null!");
            }

            List cards = player.getCardSelectorState().getCardList();

            for(int i = 0; i < selectorResult.getSelection().length; ++i) {
               if (selectorResult.getSelection()[i]) {
                  parameters.add(((CardWithLocation)cards.get(i)).getCard());
               }
            }

            player.setCardSelectorState((CardSelectorState)null);
            selectorResult.setSelection((boolean[])null);
         }

      }
   }

   private GamePhase handlePreMatch(GamePhase nextPhase) {
      int readyCount = 0;
      PlayerServerState[] var3 = this.server.getPlayers();
      int var4 = var3.length;

      int var5;
      PlayerServerState player0;
      for(var5 = 0; var5 < var4; ++var5) {
         player0 = var3[var5];
         if (player0.isReady()) {
            ++readyCount;
         }
      }

      if (readyCount == 2 && !TCG.EVENT_BUS.post(new StartGameEvent(Arrays.asList(this.server.getPlayers())))) {
         this.server.getPlayer(this.server.getCurrentTurn()).getAvailableActions().setForFirstTurnP1();
         this.setPrizes();
         nextPhase = GamePhase.FirstTurn;
         this.server.getLog().trackStartGame(this.server);
         this.setTurnTimeLimit();
         var3 = this.server.getPlayers();
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            player0 = var3[var5];
            Iterator var7 = player0.getActiveAndBenchCards().iterator();

            while(var7.hasNext()) {
               PokemonCardState pokemon = (PokemonCardState)var7.next();
               if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null) {
                  pokemon.getAbility().getEffect().onStartGame(pokemon, this.server);
               }

               if (pokemon.getHiddenAbility() != null && pokemon.getHiddenAbility().getEffect() != null) {
                  pokemon.getHiddenAbility().getEffect().onStartGame(pokemon, this.server);
               }
            }
         }
      }

      return nextPhase;
   }

   private GamePhase handleFlippingCoin() {
      this.server.setCurrentTurn(this.coinFlip() ? 0 : 1);
      PlayerServerState[] var1 = this.server.getPlayers();
      int var2 = var1.length;

      int i;
      for(i = 0; i < var2; ++i) {
         PlayerServerState player = var1[i];
         player.drawCards(7, this.server);
         this.mulligan(player, this.server);
         player.getAvailableActions().setForPreMatch();
      }

      PlayerServerState player;
      CardSelectorState mulliganBonus;
      if (this.mulliganDifference > 0) {
         player = this.server.getPlayer(1);
         mulliganBonus = new CardSelectorState(0, Math.abs(this.mulliganDifference), CardSelectorDisplay.Draw, false);

         for(i = 0; i < Math.abs(this.mulliganDifference); ++i) {
            mulliganBonus.getCardList().add((Object)null);
         }

         player.setCardSelectorState(mulliganBonus);
      } else if (this.mulliganDifference < 0) {
         player = this.server.getPlayer(0);
         mulliganBonus = new CardSelectorState(0, Math.abs(this.mulliganDifference), CardSelectorDisplay.Draw, false);

         for(i = 0; i < Math.abs(this.mulliganDifference); ++i) {
            mulliganBonus.getCardList().add((Object)null);
         }

         player.setCardSelectorState(mulliganBonus);
      }

      return GamePhase.PreMatch;
   }

   private Queue getCardsWithCondition(CardCondition cardCondition) {
      Queue queue = new LinkedList();

      for(int i = 0; i < this.server.getPlayers().length; ++i) {
         Iterator var4 = this.server.getPlayer(i).getActiveAndBenchCards().iterator();

         while(var4.hasNext()) {
            PokemonCardState card = (PokemonCardState)var4.next();
            if (card.getStatus().getConditions().stream().anyMatch((s) -> {
               return s.getLeft() == cardCondition;
            })) {
               queue.add(new ImmutablePair(card, i));
            }
         }
      }

      return queue;
   }

   public void requestEndTurn(PlayerServerState player) {
      player.setInGUI(true);
      if (this.server.getGamePhase() == GamePhase.PreMatch) {
         player.setReady(true);
      } else {
         this.readyToEndTurn = true;
      }

   }

   private void requestSyncGame() {
      this.syncGameTime = 0L;
   }

   private void newGame() {
      this.server = new GameServerState();
      this.server.setGamePhase(GamePhase.FlippingCoin);
      this.init = true;
      this.readyToEndTurn = false;
   }

   public void endGame(PlayerServerState winner, PlayerServerState loser, boolean tiedGame) {
      if (winner != null && loser != null) {
         TCG.EVENT_BUS.post(new EndGameEvent(winner, loser, tiedGame));
         if (tiedGame) {
            GuiHelper.showTitle(new TextComponentString(TextFormatting.BLUE + "You tied!"), (EntityPlayerMP)winner.getEntityPlayer());
            GuiHelper.showTitle(new TextComponentString(TextFormatting.BLUE + "You tied!"), (EntityPlayerMP)loser.getEntityPlayer());
            this.field_145850_b.func_73046_m().func_184103_al().func_148539_a(new TextComponentString(TextFormatting.LIGHT_PURPLE + "[TCG] " + TextFormatting.GREEN + winner.getEntityPlayer().getDisplayNameString() + " just tied with " + loser.getEntityPlayer().getDisplayNameString() + "!"));
            this.server.getLog().trackStalemate(this.server);
         } else {
            GuiHelper.showTitle(new TextComponentString(TextFormatting.GOLD + "You win!"), (EntityPlayerMP)winner.getEntityPlayer());
            GuiHelper.showTitle(new TextComponentString(TextFormatting.GRAY + "You lose!"), (EntityPlayerMP)loser.getEntityPlayer());
            this.field_145850_b.func_73046_m().func_184103_al().func_148539_a(new TextComponentString(TextFormatting.LIGHT_PURPLE + "[TCG] " + TextFormatting.GREEN + winner.getEntityPlayer().getDisplayNameString() + " just won against " + loser.getEntityPlayer().getDisplayNameString() + "!"));
            this.server.getLog().trackEndGame(this.server.getTurn(winner), this.server);
            if (this.endingCommand != null) {
               this.trigger(this.field_145850_b, this.endingCommand, winner.getPlayerName(), loser.getPlayerName());
            }
         }
      }

      if (this.server != null) {
         PlayerServerState[] var4 = this.server.getPlayers();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PlayerServerState player = var4[var6];
            if (player != null) {
               PacketHandler.net.sendTo(new EndGamePacket(this.field_174879_c), (EntityPlayerMP)player.getEntityPlayer());
            }
         }

         this.server.getSpectators().keySet().stream().filter((entityPlayer) -> {
            return entityPlayer != null;
         }).forEach((entityPlayer) -> {
            PacketHandler.net.sendTo(new EndGamePacket(this.field_174879_c), entityPlayer);
         });
      }

      this.newGame();
      this.sendRenderSyncPackets();
   }

   private void endTurn() {
      if (this.server.getGamePhase().after(GamePhase.PreMatch)) {
         PlayerServerState[] var1 = this.server.getPlayers();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            PlayerServerState player = var1[var3];
            Iterator var5 = player.getActiveAndBenchCards().iterator();

            while(var5.hasNext()) {
               PokemonCardState pokemon = (PokemonCardState)var5.next();
               pokemon.handleEndTurn((PokemonCardState)null, player, this.server);
               CommonCardState[] var7 = (CommonCardState[])pokemon.getAttachments().toArray(new CommonCardState[0]);
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  CommonCardState attachment = var7[var9];
                  attachment.handleEndTurn(pokemon, player, this.server);
               }
            }
         }

         TCG.EVENT_BUS.post(new EndTurnEvent(this.server.getPlayer(this.server.getCurrentTurn()), this.server.getCurrentTurn()));
         this.server.getPlayer(this.server.getCurrentTurn()).setAreTrainersDisabled(false);
         this.server.setGamePhase(GamePhase.BetweenTurns);
      }

      this.readyToEndTurn = false;
   }

   private boolean coinFlip() {
      return RandomHelper.getRandomChance(0.5F);
   }

   private boolean checkKnockedOut(PlayerServerState player, PokemonCardState card, MutableInt prizeCount) {
      if (card.getStatus().getDamage() < card.getHP()) {
         return false;
      } else {
         Iterator var4 = card.getAttachments().iterator();

         while(var4.hasNext()) {
            CommonCardState attachment = (CommonCardState)var4.next();
            player.getDiscardPile().add(attachment.getData());
         }

         boolean isAttached = false;
         Iterator var10 = player.getActiveAndBenchCards().iterator();

         while(true) {
            while(var10.hasNext()) {
               PokemonCardState pokemon = (PokemonCardState)var10.next();
               Iterator var7 = pokemon.getAttachments().iterator();

               while(var7.hasNext()) {
                  CommonCardState attachment = (CommonCardState)var7.next();
                  if (attachment == card) {
                     isAttached = true;
                     break;
                  }
               }
            }

            if (!isAttached) {
               player.getDiscardPile().add(card.getData());
            }

            PlayerServerState winningPlayer = this.server.getOpponent(player);
            ChatHandler.sendFormattedChat(player.getEntityPlayer(), TextFormatting.RED, "Your " + LanguageMapTCG.translateKey(card.getData().getName().toLowerCase()) + " has fainted!");
            ChatHandler.sendFormattedChat(winningPlayer.getEntityPlayer(), TextFormatting.YELLOW, "You have knocked out " + LanguageMapTCG.translateKey(card.getData().getName().toLowerCase()) + "!");
            if (card.getData().getCardType() == CardType.TRAINER) {
               return true;
            }

            prizeCount.increment();
            if (winningPlayer.getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRing))) {
               prizeCount.increment();
            }

            if (card.getCardType() == CardType.EX) {
               prizeCount.increment();
               if (winningPlayer.getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRing))) {
                  prizeCount.increment();
               }
            }

            this.server.getLog().trackKnockout(card, this.server.getTurn(winningPlayer), this.server);
            return true;
         }
      }
   }

   public void sendRenderPreBattleSyncPackets() {
      this.field_145850_b.field_73010_i.stream().filter((p) -> {
         return p.func_180425_c().func_177954_c((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p()) < 2500.0;
      }).forEach((p) -> {
         PacketHandler.net.sendTo(new RenderStatePreBattleSyncPacket(this.field_174879_c, this), (EntityPlayerMP)p);
      });
   }

   public void sendRenderSyncPackets() {
      this.field_145850_b.field_73010_i.stream().filter((p) -> {
         return p.func_180425_c().func_177954_c((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p()) < 2500.0;
      }).forEach((p) -> {
         PacketHandler.net.sendTo(new RenderStateSyncPacket(this.field_174879_c, this.server, p, this), (EntityPlayerMP)p);
      });
   }

   public void setPrizes() {
      PlayerServerState[] var1 = this.server.getPlayers();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PlayerServerState player = var1[var3];
         ImmutableCard[] cards = new ImmutableCard[this.prizeCount];

         for(int x = 0; x < this.prizeCount; ++x) {
            cards[x] = (ImmutableCard)player.getDeck().get(0);
            player.getDeck().remove(0);
         }

         player.setPrizeCards(cards);
      }

   }

   public void mulligan(PlayerServerState player, GameServerState server) {
      while(!this.doesCardListContainCardType(player.getHand(), CardType.BASIC) && !this.doesCardListContainCardType(player.getHand(), CardType.EX)) {
         player.getDeck().addAll(player.getHand());
         CardSelectorState failedHand = new CardSelectorState(0, 0, CardSelectorDisplay.Select, false);
         Iterator var4 = player.getHand().iterator();

         while(var4.hasNext()) {
            ImmutableCard c = (ImmutableCard)var4.next();
            failedHand.getCardList().add(new CardWithLocation(new CommonCardState(c), false, BoardLocation.Hand, 0));
         }

         if (player == server.getPlayer(0)) {
            this.mulliganStates1.add(failedHand);
         } else {
            this.mulliganStates0.add(failedHand);
         }

         player.getHand().clear();
         LogicHelper.shuffleCardList(player.getDeck());
         player.drawCards(7, server);
         if (server.getPlayer(0) == player) {
            ++this.mulliganDifference;
         } else {
            --this.mulliganDifference;
         }
      }

   }

   public void playCardFromHandToActive(PlayerServerState player, int cardIndex, ImmutableCard check) {
      List hand = player.getHand();
      ImmutableCard card = (ImmutableCard)hand.get(cardIndex);
      if (card == check) {
         if (this.server.getGamePhase() == GamePhase.PreMatch || this.server.isCurrentTurn(player)) {
            PokemonCardState newActiveCard = null;
            AvailableActions availableActions = player.getAvailableActions();
            PokemonCardState activeCard = player.getActiveCard();
            switch (card.getCardType()) {
               case EX:
               case BASIC:
                  if (activeCard == null) {
                     newActiveCard = new PokemonCardState(card, this.server.getTurnCount());
                     player.setActiveCard(newActiveCard);
                     if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                        this.server.getOpponent(player).getHand().add(card);
                     }

                     hand.remove(cardIndex);
                     this.server.getLog().trackPlayCard(player.getActiveCard(), this.server.getTurn(player), this.server);
                  }
                  break;
               case ENERGY:
                  if (availableActions.isCanPlayEnergy() && activeCard != null) {
                     CommonCardState cardState = new CommonCardState(card);
                     activeCard.getAttachments().add(cardState);
                     if (player.getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemKey))) {
                        CommonCardState cardState2 = new CommonCardState(card);
                        activeCard.getAttachments().add(cardState2);
                     }

                     if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                        this.server.getOpponent(player).getHand().add(card);
                     }

                     hand.remove(cardIndex);
                     availableActions.setCanPlayEnergy(false);
                     this.server.getLog().trackAttachCard(cardState, activeCard, this.server.getTurn(player), this.server);
                  }
                  break;
               case TOOL:
                  if (activeCard != null) {
                     activeCard.getAttachments().add(new CommonCardState(card));
                     if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                        this.server.getOpponent(player).getHand().add(card);
                     }

                     hand.remove(cardIndex);
                  }
                  break;
               case TRAINER:
                  if (player.getAvailableActions().isCanPlayTrainer() && card.getEffect() != null && card.getEffect().canPlay(this.server)) {
                     TrainerCardState trainerState = new TrainerCardState(card);
                     if (trainerState.getData().getEffect().canSkipSelector() && trainerState.getData().getEffect().canPlaceOn(new CardWithLocation(activeCard, true, BoardLocation.Active, 0))) {
                        trainerState.getData().getEffect().applySkipSelector(trainerState, activeCard, this.server, BoardLocation.Active, 0);
                        this.server.getLog().trackPlayCard(trainerState, this.server.getTurn(player), this.server);
                        if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                           this.server.getOpponent(player).getHand().add(card);
                        }

                        hand.remove(cardIndex);
                        EntityPlayerMP entityPlayer = (EntityPlayerMP)this.server.getOpponent(player).getEntityPlayer();
                        PacketHandler.net.sendTo(new TrainerPlayedPacket(player.getPlayerName(), trainerState), entityPlayer);
                        Iterator var11 = this.server.getSpectators().keySet().iterator();

                        while(var11.hasNext()) {
                           EntityPlayerMP spectator = (EntityPlayerMP)var11.next();
                           PacketHandler.net.sendTo(new TrainerPlayedPacket(player.getPlayerName(), trainerState), spectator);
                        }

                        if (!trainerState.getData().getEffect().preventDiscard()) {
                           player.getDiscardPile().add(trainerState.getData());
                        }
                     }
                  }
               case STAGE1:
               case STAGE2:
               case LVLX:
               case MEGA:
                  if (activeCard != null && activeCard.getPokemonID() == card.getPrevEvoID() && LogicHelper.canEvolve(activeCard.getTurn(), this.server.getTurnCount()) && !(new GameClientState(this.server)).isDisablingEvolution(activeCard)) {
                     newActiveCard = LogicHelper.evolveCard(card, activeCard, this.server.getTurnCount());
                     this.server.getLog().trackEvolve(activeCard, newActiveCard, this.server.getTurn(player), this.server);
                     if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                        this.server.getOpponent(player).getHand().add(card);
                     }

                     player.setActiveCard(newActiveCard);
                     player.getActiveCard().getParameters().clear();
                     hand.remove(cardIndex);
                  }
            }

            if (newActiveCard != null) {
               PlayerServerState[] var16 = this.server.getPlayers();
               int var18 = var16.length;

               for(int var19 = 0; var19 < var18; ++var19) {
                  PlayerServerState player0 = var16[var19];
                  Iterator var13 = player0.getActiveAndBenchCards().iterator();

                  while(var13.hasNext()) {
                     PokemonCardState pokemon = (PokemonCardState)var13.next();
                     if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null) {
                        pokemon.getAbility().getEffect().onPlay(newActiveCard, player, pokemon, player0, this.server);
                        pokemon.getAbility().getEffect().onSwitchActiveCard(newActiveCard, activeCard, player, pokemon, player0, this.server);
                     }

                     if (pokemon.getHiddenAbility() != null && pokemon.getHiddenAbility().getEffect() != null) {
                        pokemon.getHiddenAbility().getEffect().onPlay(newActiveCard, player, pokemon, player0, this.server);
                        pokemon.getHiddenAbility().getEffect().onSwitchActiveCard(newActiveCard, activeCard, player, pokemon, player0, this.server);
                     }
                  }
               }
            }

         }
      }
   }

   public void playPokemonCardToBench(PlayerServerState player, int cardIndex, ImmutableCard check, int benchIndex) {
      List hand = player.getHand();
      ImmutableCard card = (ImmutableCard)hand.get(cardIndex);
      if (card == check) {
         if (this.server.getGamePhase() == GamePhase.PreMatch || this.server.isCurrentTurn(player)) {
            if (player.getActiveCard() != null) {
               AvailableActions availableActions = player.getAvailableActions();
               PokemonCardState[] benchCards = player.getBenchCards();
               PokemonCardState benchCard = benchCards[benchIndex];
               PokemonCardState newBenchCard = null;
               CommonCardState cardState;
               switch (card.getCardType()) {
                  case EX:
                  case BASIC:
                     if (benchCard == null) {
                        newBenchCard = new PokemonCardState(card, this.server.getTurnCount());
                        benchCards[benchIndex] = newBenchCard;
                        if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                           this.server.getOpponent(player).getHand().add(card);
                        }

                        hand.remove(cardIndex);
                        this.server.getLog().trackPlayCard(benchCards[benchIndex], this.server.getTurn(player), this.server);
                     }
                     break;
                  case ENERGY:
                     if (benchCard != null && availableActions.isCanPlayEnergy()) {
                        cardState = new CommonCardState(card);
                        benchCard.getAttachments().add(cardState);
                        if (player.getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemKey))) {
                           CommonCardState cardState2 = new CommonCardState(card);
                           benchCard.getAttachments().add(cardState2);
                        }

                        this.server.getLog().trackAttachCard(cardState, benchCard, this.server.getTurn(player), this.server);
                        if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                           this.server.getOpponent(player).getHand().add(cardState.getData());
                        }

                        hand.remove(cardIndex);
                        availableActions.setCanPlayEnergy(false);
                     }
                  case TOOL:
                  default:
                     break;
                  case TRAINER:
                     if (player.getAvailableActions().isCanPlayTrainer() && card.getEffect() != null && card.getEffect().canPlay(this.server)) {
                        TrainerCardState trainerState = new TrainerCardState(card);
                        if (trainerState.getData().getEffect().canSkipSelector() && trainerState.getData().getEffect().canPlaceOn(new CardWithLocation(benchCard, true, BoardLocation.Bench, benchIndex))) {
                           trainerState.getData().getEffect().applySkipSelector(trainerState, benchCard, this.server, BoardLocation.Bench, benchIndex);
                           this.server.getLog().trackPlayCard(trainerState, this.server.getTurn(player), this.server);
                           if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                              this.server.getOpponent(player).getHand().add(card);
                           }

                           hand.remove(cardIndex);
                           EntityPlayerMP entityPlayer = (EntityPlayerMP)this.server.getOpponent(player).getEntityPlayer();
                           PacketHandler.net.sendTo(new TrainerPlayedPacket(player.getPlayerName(), trainerState), entityPlayer);
                           Iterator var13 = this.server.getSpectators().keySet().iterator();

                           while(var13.hasNext()) {
                              EntityPlayerMP spectator = (EntityPlayerMP)var13.next();
                              PacketHandler.net.sendTo(new TrainerPlayedPacket(player.getPlayerName(), trainerState), spectator);
                           }

                           if (!trainerState.getData().getEffect().preventDiscard()) {
                              player.getDiscardPile().add(trainerState.getData());
                           }
                        }
                     }
                  case STAGE1:
                  case STAGE2:
                  case LVLX:
                  case MEGA:
                     if (benchCard != null && benchCard.getPokemonID() == card.getPrevEvoID() && LogicHelper.canEvolve(benchCard.getTurn(), this.server.getTurnCount()) && !(new GameClientState(this.server)).isDisablingEvolution(benchCard)) {
                        newBenchCard = LogicHelper.evolveCard(card, benchCard, this.server.getTurnCount());
                        this.server.getLog().trackEvolve(benchCard, newBenchCard, this.server.getTurn(player), this.server);
                        if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                           this.server.getOpponent(player).getHand().add(card);
                        }

                        benchCards[benchIndex] = newBenchCard;
                        player.getBenchCards()[benchIndex].getParameters().clear();
                        hand.remove(cardIndex);
                     }
                     break;
                  case ITEM:
                     if (benchCard != null && availableActions.isCanPlayItem()) {
                        cardState = new CommonCardState(card);
                        benchCard.getAttachments().add(cardState);
                        this.server.getLog().trackAttachCard(cardState, benchCard, this.server.getTurn(player), this.server);
                        if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                           this.server.getOpponent(player).getHand().add(card);
                        }

                        hand.remove(cardIndex);
                     }
               }

               if (newBenchCard != null) {
                  PlayerServerState[] var18 = this.server.getPlayers();
                  int var20 = var18.length;

                  for(int var21 = 0; var21 < var20; ++var21) {
                     PlayerServerState player0 = var18[var21];
                     Iterator var15 = player0.getActiveAndBenchCards().iterator();

                     while(var15.hasNext()) {
                        PokemonCardState pokemon = (PokemonCardState)var15.next();
                        if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null) {
                           pokemon.getAbility().getEffect().onPlay(newBenchCard, player, pokemon, player0, this.server);
                        }

                        if (pokemon.getHiddenAbility() != null && pokemon.getHiddenAbility().getEffect() != null) {
                           pokemon.getHiddenAbility().getEffect().onPlay(newBenchCard, player, pokemon, player0, this.server);
                        }
                     }
                  }
               }

            }
         }
      }
   }

   public void playTrainerCard(PlayerServerState player, int cardIndex, ImmutableCard check) {
      List hand = player.getHand();
      ImmutableCard card = (ImmutableCard)hand.get(cardIndex);
      if (card == check) {
         if (this.server.isCurrentTurn(player)) {
            if (player.getAvailableActions().isCanPlayTrainer() && card.getCardType() == CardType.TRAINER && card.getEffect() != null && card.getEffect().canPlay(this.server)) {
               TrainerCardState trainer = new TrainerCardState(card);
               player.setTrainerCard(trainer);
               this.server.getLog().trackPlayCard(trainer, this.server.getTurn(player), this.server);
               if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                  this.server.getOpponent(player).getHand().add(card);
               }

               hand.remove(cardIndex);
               EntityPlayerMP entityPlayer = (EntityPlayerMP)this.server.getOpponent(player).getEntityPlayer();
               PacketHandler.net.sendTo(new TrainerPlayedPacket(player.getPlayerName(), trainer), entityPlayer);
               Iterator var8 = this.server.getSpectators().keySet().iterator();

               while(var8.hasNext()) {
                  EntityPlayerMP spectator = (EntityPlayerMP)var8.next();
                  PacketHandler.net.sendTo(new TrainerPlayedPacket(player.getPlayerName(), trainer), spectator);
               }
            }

         }
      }
   }

   public void playStadiumCard(PlayerServerState player, int cardIndex, ImmutableCard check) {
      List hand = player.getHand();
      ImmutableCard card = (ImmutableCard)hand.get(cardIndex);
      if (card == check) {
         if (this.server.isCurrentTurn(player)) {
            if (card.getCardType() == CardType.STADIUM) {
               this.server.setStadiumCard(card);
               if (this.server.getOpponent(player).getEntityPlayer().field_71071_by.func_70431_c(new ItemStack(TCG.itemRod))) {
                  this.server.getOpponent(player).getHand().add(card);
               }

               hand.remove(cardIndex);
            }

         }
      }
   }

   public void requestAbility(int playerIndex, BoardLocation location, int locationIndex) {
      PlayerServerState[] var4 = this.server.getPlayers();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         PlayerServerState player = var4[var6];
         if (player.getActiveCard() == null) {
            return;
         }
      }

      PlayerServerState player = this.server.getPlayer(playerIndex);
      PokemonCardState pokemon;
      if (location == BoardLocation.Active) {
         pokemon = player.getActiveCard();
      } else {
         if (location != BoardLocation.Bench) {
            return;
         }

         pokemon = player.getBenchCards()[locationIndex];
      }

      if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null && !pokemon.getAbility().getEffect().isPassive() && pokemon.getAbility().getEffect().isEnabled(pokemon, new GameClientState(this.server))) {
         this.getGameServer().setPendingAbility(pokemon);
      }
   }

   public void requestAttack(int playerIndex, int attackIndex) {
      PlayerServerState[] var3 = this.server.getPlayers();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PlayerServerState player = var3[var5];
         if (player.getActiveCard() == null) {
            return;
         }
      }

      PlayerServerState player = this.server.getPlayer(playerIndex);
      PokemonCardState active = player.getActiveCard();
      if (!active.getStatus().getConditions().stream().anyMatch((c) -> {
         return c.getLeft() == CardCondition.ASLEEP || c.getLeft() == CardCondition.PARALYZED;
      })) {
         PokemonAttackStatus attack = active.getAttacksStatus()[attackIndex];
         if (attack == null) {
            throw new IllegalArgumentException("Cannot find attack index " + attackIndex);
         } else if (!attack.isDisabled() && LogicHelper.isEnoughEnergy(attack.getData(), active.getAttachments(), active)) {
            this.server.setPendingAttack(new PendingAttack(player, active, attack));
         }
      }
   }

   public void requestPickAttack(int playIndex, int attackIndex) {
      PlayerServerState player = this.server.getPlayer(this.server.getCurrentTurn());
      PlayerServerState opp = this.server.getPlayer(this.server.getNextTurn());
      ((List)this.server.getEffectsParameters().get(this.server.getCurrentEffectIndex())).add(opp.getActiveCard().getAttacksStatus()[attackIndex]);
      player.setChoosingOppAttack(false);
   }

   private void sendGameSyncPackets() {
      for(int playerIndex = 0; playerIndex < 2; ++playerIndex) {
         PlayerServerState player = this.server.getPlayer(playerIndex);
         PacketHandler.net.sendTo(new GameStateSyncPacket(this.field_174879_c, playerIndex, false, this.server, player.getEntityPlayer()), (EntityPlayerMP)player.getEntityPlayer());
      }

   }

   private void sendGameSyncPacketsToSpectators() {
      Iterator var1 = this.server.getSpectators().keySet().iterator();

      while(var1.hasNext()) {
         EntityPlayerMP spectator = (EntityPlayerMP)var1.next();
         int playerIndex = (Integer)this.server.getSpectators().get(spectator);
         PlayerServerState player = this.server.getPlayer(playerIndex);
         PacketHandler.net.sendTo(new GameStateSyncPacket(this.field_174879_c, playerIndex, true, this.server, player.getEntityPlayer()), spectator);
      }

   }

   public void requestRetreatAndSwitch(PlayerServerState player, List energyPayment, int benchIndex) {
      if (player.getAvailableActions().isCanRetreatActive() && player.getBenchCards()[benchIndex] != null && player.getActiveCard().getAttachments().containsAll(energyPayment) && player.getActiveCard().canRetreat()) {
         Iterator var4 = energyPayment.iterator();

         while(var4.hasNext()) {
            CommonCardState energy = (CommonCardState)var4.next();
            player.getActiveCard().getAttachments().remove(energy);
            player.getDiscardPile().add(energy.getData());
         }

         player.switchActive(player.getBenchCards()[benchIndex], this.server);
         player.getAvailableActions().setCanRetreatActive(false);
      }

   }

   public void requestSwitch(PlayerServerState player, int benchIndex) {
      if (player.getBenchCards()[benchIndex] != null) {
         player.switchActive(player.getBenchCards()[benchIndex], this.server);
      }

   }

   public void setCardSelection(EntityPlayer entityPlayer, boolean isOpened, boolean[] cardSelection) {
      PlayerServerState player = this.server.getPlayer(entityPlayer);
      if (player.getCardSelectorState() != null) {
         if (player.getCardSelectorResult() == null) {
            player.setCardSelectorResult(new CardSelectorResult());
         }

         player.getCardSelectorResult().setOpened(isOpened);
         player.getCardSelectorResult().setSelection(cardSelection);
      }
   }

   public void setPrizeSelection(EntityPlayer entityPlayer, int index) {
      PlayerServerState player = this.server.getPlayer(entityPlayer);
      if (player != null && player.getPendingPrizeCount() > 0) {
         player.setOpeningPrizeIndex(index);
      }

   }

   public void setCustomGUIResult(EntityPlayer entityPlayer, boolean isOpened, int[] result) {
      PlayerServerState player = this.server.getPlayer(entityPlayer);
      if (player.getCustomGUIResult() == null) {
         player.setCustomGUIResult(new CustomGUIResult());
      }

      player.getCustomGUIResult().setOpened(isOpened);
      player.getCustomGUIResult().setResult(result);
   }

   public void requestFlip(PlayerServerState player) {
      this.server.setRevealedCoinFlipResults(this.server.getRevealedCoinFlipResults() + 1);
   }

   public String formatCommand(String command, String winnerName, String loserName) {
      return command.replace(" @player1 ", " " + winnerName + " ").replace(" @player2 ", " " + loserName + " ").replace(" @winner ", " " + winnerName + " ").replace(" @loser ", " " + loserName + " ");
   }

   public void discard(PlayerServerState player, BoardLocation location, int locationSubIndex) {
      PokemonCardState card = player.getCard(location, locationSubIndex);
      Iterator var5 = card.getAttachments().iterator();

      while(var5.hasNext()) {
         CommonCardState attachment = (CommonCardState)var5.next();
         player.getDiscardPile().add(attachment.getData());
      }

      player.getDiscardPile().add(card.getData());
      switch (location) {
         case Active:
            player.setActiveCard((PokemonCardState)null);
            break;
         case Bench:
            player.getBenchCards()[locationSubIndex] = null;
      }

      this.server.getLog().trackDiscard(card, this.server.getCurrentTurn(), this.server);
   }

   private void forceRevealingCoinFlips() {
      if (this.server.exceedTimeLimit()) {
         this.server.setRevealedCoinFlipResults(this.server.getCoinFlip().getResults().size() + 1);
      }

   }
}

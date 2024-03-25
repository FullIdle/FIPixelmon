package com.pixelmonmod.tcg.tileentity;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.block.BlockBattleController;
import com.pixelmonmod.tcg.duel.log.DuelLog;
import com.pixelmonmod.tcg.duel.state.CoinFlipState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.RenderState;
import com.pixelmonmod.tcg.gui.duel.GuiTCG;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBattleController extends TileEntityBattleRule implements ITickable {
   protected GameClientState client;
   protected RenderState render;
   protected GuiTCG gui;
   protected boolean init;
   public boolean[] energySelectionForRetreat;
   private Map showingPrizes = new HashMap();

   public void func_73660_a() {
      this.initialize();
   }

   protected void initialize() {
      if (!this.init) {
         this.client = new GameClientState();
         this.render = new RenderState();
         this.energySelectionForRetreat = null;
         this.showingPrizes = new HashMap();
         this.init = true;
      }

   }

   public float getScale() {
      BlockBattleController controller = (BlockBattleController)this.func_145838_q();
      return controller.getScale();
   }

   @SideOnly(Side.CLIENT)
   public void resetClientGame() {
      this.init = false;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return INFINITE_EXTENT_AABB;
   }

   @SideOnly(Side.CLIENT)
   public void setClientSideValues(GamePhase gamePhase, int whichPlayerAmI, boolean isMyTurn, PlayerClientMyState myState, PlayerClientOpponentState oppState, DuelLog log, int turnCount, CoinFlipState coinFlip) {
      if (this.client != null) {
         this.client.setGamePhase(gamePhase);
         this.client.setPlayerIndex(whichPlayerAmI);
         this.client.setMyTurn(isMyTurn);
         this.client.setMe(myState);
         this.client.setOpponent(oppState);
         this.client.setLog(log);
         this.client.setTurnCount(turnCount);
         this.client.setCoinFlip(coinFlip);
      }

   }

   @SideOnly(Side.CLIENT)
   public void setRenderClientSideValues(GamePhase gamePhase, int currentTurn, PlayerClientOpponentState[] players) {
      if (this.render != null) {
         this.render.setGamePhase(gamePhase);
         this.render.setCurrentTurn(currentTurn);
         this.render.setPlayers(players);
      }

   }

   public GameClientState getClient() {
      return this.client;
   }

   public RenderState getRender() {
      return this.render;
   }

   public void setGui(GuiTCG gui) {
      this.gui = gui;
   }

   public boolean doesCardListContainCardType(List cards, CardType type) {
      Iterator var3 = cards.iterator();

      ImmutableCard card;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         card = (ImmutableCard)var3.next();
      } while(card.getCardType() != type);

      return true;
   }

   public boolean func_183000_F() {
      return false;
   }

   public GameServerState getGameServer() {
      return null;
   }

   public void requestAbility(int playerIndex, BoardLocation location, int locationIndex) {
   }

   public void requestAttack(int playerIndex, int attackIndex) {
   }

   public void requestPickAttack(int playIndex, int attackIndex) {
   }

   public void requestEndTurn(PlayerServerState player) {
   }

   public void playTrainerCard(PlayerServerState player, int cardIndex, ImmutableCard check) {
   }

   public void playPokemonCardToBench(PlayerServerState player, int cardIndex, ImmutableCard check, int bench) {
   }

   public void playCardFromHandToActive(PlayerServerState player, int cardIndex, ImmutableCard check) {
   }

   public void playStadiumCard(PlayerServerState player, int cardIndex, ImmutableCard check) {
   }

   public void setCardSelection(EntityPlayer player, boolean isOpened, boolean[] cardSelection) {
   }

   public void setPrizeSelection(EntityPlayer player, int index) {
   }

   public void requestRetreatAndSwitch(PlayerServerState player, List energyPayment, int newActivePos) {
   }

   public void requestSwitch(PlayerServerState player, int newActivePos) {
   }

   public void requestFlip(PlayerServerState player) {
   }

   public void discard(PlayerServerState player, BoardLocation location, int locationSubIndex) {
   }

   public void setCustomGUIResult(EntityPlayer player, boolean isOpened, int[] result) {
   }

   public void revealPrize(int prizeIndex, ImmutableCard prize) {
      this.showingPrizes.put(prizeIndex, prize);
   }

   public Map getShowingPrizes() {
      return this.showingPrizes;
   }
}

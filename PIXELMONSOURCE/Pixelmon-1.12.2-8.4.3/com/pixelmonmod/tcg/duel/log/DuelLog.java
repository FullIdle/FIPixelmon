package com.pixelmonmod.tcg.duel.log;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogAttachCardParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogAttackParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogCardParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogConditionParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogEndGameParameter;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogEvolveParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogIntParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogKnockoutParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogStalemateParameter;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import io.netty.buffer.ByteBuf;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DuelLog {
   public static final String folderLoc;
   private final List log;

   public DuelLog() {
      this.log = new ArrayList();
   }

   public DuelLog(ByteBuf buf) {
      int count = buf.readInt();
      this.log = new ArrayList();

      for(int i = 0; i < count; ++i) {
         this.log.add(new DuelLogItem(buf));
      }

   }

   public void write(ByteBuf buf, GamePhase gamePhase, int receiverIndex, boolean isMyTurn) {
      buf.writeInt(this.log.size());
      Iterator var5 = this.log.iterator();

      while(var5.hasNext()) {
         DuelLogItem item = (DuelLogItem)var5.next();
         item.write(buf, gamePhase, receiverIndex, isMyTurn);
      }

   }

   public void saveLog(String location) throws IOException {
      File file = new File(location);
      file.createNewFile();
      FileWriter logFile = new FileWriter(file, true);
      Iterator var4 = this.log.iterator();

      while(var4.hasNext()) {
         DuelLogItem item = (DuelLogItem)var4.next();
         logFile.write(item.toString() + "\n");
      }

      logFile.flush();
      logFile.close();
   }

   public void addItem(DuelLogItem item) {
      this.log.add(item);
   }

   public List getItems(int count) {
      if (count > this.log.size()) {
         count = this.log.size();
      }

      List result = new ArrayList();
      int start = this.log.size() - 1;
      int end = this.log.size() - count;

      for(int i = start; i >= end && i >= 0; --i) {
         result.add(this.log.get(i));
      }

      return result;
   }

   public List getItems() {
      return this.log;
   }

   public void trackAttack(AttackCard cardAttack, PokemonCardState attacker, PokemonCardState attacking, int damage, List conditions, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.ATTACK, new DuelLogAttackParameters(cardAttack, attacker, attacking, damage, conditions)));
   }

   public void trackAbility(PokemonCardState activator, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.ABILITY, new DuelLogCardParameters(activator)));
   }

   public void trackCondition(CardCondition cardCondition, PokemonCardState affected, int damage, boolean isHealed, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.CONDITION, new DuelLogConditionParameters(cardCondition, affected, damage, isHealed)));
   }

   public void trackKnockout(PokemonCardState dead, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.KNOCKOUT, new DuelLogKnockoutParameters(dead)));
   }

   public void trackPlayCard(CommonCardState played, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.PLAY, new DuelLogCardParameters(played)));
   }

   public void trackAttachCard(CommonCardState attachment, PokemonCardState host, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.ATTACH, new DuelLogAttachCardParameters(host, attachment)));
   }

   public void trackDrawCard(int count, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.DRAW, new DuelLogIntParameters(count)));
   }

   public void trackDiscard(CommonCardState discarded, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.DISCARD, new DuelLogCardParameters(discarded)));
   }

   public void trackEvolve(PokemonCardState prev, PokemonCardState next, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.EVOLVE, new DuelLogEvolveParameters(prev, next)));
   }

   public void trackSwitch(PokemonCardState in, int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.SWITCH, new DuelLogCardParameters(in)));
   }

   public void trackStartGame(GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), -1, DuelLogType.STARTGAME, (DuelLogParameters)null));
   }

   public void trackEndGame(int winnerIndex, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), -1, DuelLogType.ENDGAME, new DuelLogEndGameParameter(winnerIndex)));
   }

   public void trackStalemate(GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), -1, DuelLogType.ENDGAME, new DuelLogStalemateParameter()));
   }

   public void trackStartTurn(int playerSide, GameServerState state) {
      this.addItem(new DuelLogItem(state.getTurnCount(), playerSide, DuelLogType.PASSTURN, (DuelLogParameters)null));
   }

   static {
      folderLoc = TCG.rootDirectory + "/tcg/";
   }
}

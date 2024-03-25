package com.pixelmonmod.tcg.duel.log;

import com.pixelmonmod.tcg.duel.log.parameters.DuelLogAttachCardParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogAttackParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogCardParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogConditionParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogEndGameParameter;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogEvolveParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogIntParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogKnockoutParameters;
import com.pixelmonmod.tcg.duel.log.parameters.DuelLogParameters;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import io.netty.buffer.ByteBuf;

public class DuelLogItem {
   private int turn;
   private int playerSide;
   private DuelLogType type;
   private DuelLogParameters parameters;

   public DuelLogItem(int turn, int playerSide, DuelLogType type, DuelLogParameters parameters) {
      this.turn = turn;
      this.playerSide = playerSide;
      this.type = type;
      this.parameters = parameters;
   }

   public DuelLogItem(ByteBuf buf) {
      this.turn = buf.readInt();
      this.playerSide = buf.readInt();
      this.type = DuelLogType.values()[buf.readInt()];
      switch (this.type) {
         case ATTACK:
            this.parameters = new DuelLogAttackParameters(buf);
            break;
         case ABILITY:
            this.parameters = new DuelLogCardParameters(buf);
            break;
         case CONDITION:
            this.parameters = new DuelLogConditionParameters(buf);
            break;
         case KNOCKOUT:
            this.parameters = new DuelLogKnockoutParameters(buf);
            break;
         case PLAY:
            this.parameters = new DuelLogCardParameters(buf);
            break;
         case ATTACH:
            this.parameters = new DuelLogAttachCardParameters(buf);
            break;
         case DRAW:
            this.parameters = new DuelLogIntParameters(buf);
            break;
         case DISCARD:
            this.parameters = new DuelLogCardParameters(buf);
            break;
         case EVOLVE:
            this.parameters = new DuelLogEvolveParameters(buf);
            break;
         case SWITCH:
            this.parameters = new DuelLogCardParameters(buf);
            break;
         case ENDGAME:
            this.parameters = new DuelLogEndGameParameter(buf);
      }

   }

   public void write(ByteBuf buf, GamePhase gamePhase, int receiverIndex, boolean isMyTurn) {
      buf.writeInt(this.turn);
      buf.writeInt(this.playerSide);
      buf.writeInt(this.type.ordinal());
      if (this.parameters != null) {
         this.parameters.write(buf, gamePhase, this.playerSide, receiverIndex, isMyTurn);
      }

   }

   public int getTurn() {
      return this.turn;
   }

   public int getPlayerSide() {
      return this.playerSide;
   }

   public DuelLogType getType() {
      return this.type;
   }

   public DuelLogParameters getParameters() {
      return this.parameters;
   }

   public String toString() {
      return this.type.toString();
   }

   public DuelLogAttackParameters getAttackParameters() {
      return (DuelLogAttackParameters)this.parameters;
   }

   public DuelLogCardParameters getAbilityParameters() {
      return (DuelLogCardParameters)this.parameters;
   }

   public DuelLogCardParameters getSwitchParameters() {
      return (DuelLogCardParameters)this.parameters;
   }

   public DuelLogAttachCardParameters getAttachCardParameters() {
      return (DuelLogAttachCardParameters)this.parameters;
   }

   public DuelLogIntParameters getDrawParameters() {
      return (DuelLogIntParameters)this.parameters;
   }

   public DuelLogCardParameters getDiscardParameters() {
      return (DuelLogCardParameters)this.parameters;
   }

   public DuelLogKnockoutParameters getKnockoutParameters() {
      return (DuelLogKnockoutParameters)this.parameters;
   }

   public DuelLogCardParameters getPlayParameters() {
      return (DuelLogCardParameters)this.parameters;
   }

   public DuelLogEvolveParameters getEvolveParameters() {
      return (DuelLogEvolveParameters)this.parameters;
   }
}

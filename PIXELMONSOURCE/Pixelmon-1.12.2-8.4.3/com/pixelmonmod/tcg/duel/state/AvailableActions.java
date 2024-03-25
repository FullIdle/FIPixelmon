package com.pixelmonmod.tcg.duel.state;

import io.netty.buffer.ByteBuf;

public class AvailableActions {
   private boolean canPlayEnergy;
   private boolean canPlayTrainer;
   private boolean canPlaySupporter;
   private boolean canPlayItem;
   private boolean canPlayStadium;
   private boolean canRetreatActive;
   private int mulliganBonusDraws;
   private boolean canAttack;

   public AvailableActions() {
   }

   public AvailableActions(ByteBuf buf) {
      this.canPlayEnergy = buf.readBoolean();
      this.canPlayTrainer = buf.readBoolean();
      this.canPlaySupporter = buf.readBoolean();
      this.canPlayItem = buf.readBoolean();
      this.canPlayStadium = buf.readBoolean();
      this.canRetreatActive = buf.readBoolean();
      this.mulliganBonusDraws = buf.readInt();
      this.canAttack = buf.readBoolean();
   }

   public void write(ByteBuf buf) {
      buf.writeBoolean(this.canPlayEnergy);
      buf.writeBoolean(this.canPlayTrainer);
      buf.writeBoolean(this.canPlaySupporter);
      buf.writeBoolean(this.canPlayItem);
      buf.writeBoolean(this.canPlayStadium);
      buf.writeBoolean(this.canRetreatActive);
      buf.writeInt(this.mulliganBonusDraws);
      buf.writeBoolean(this.canAttack);
   }

   public void setForPreMatch() {
      this.setForNormalTurn();
      this.canPlayEnergy = false;
      this.canPlayTrainer = false;
      this.canPlaySupporter = false;
      this.canPlayItem = false;
      this.canPlayStadium = false;
      this.canRetreatActive = false;
      this.mulliganBonusDraws = 0;
      this.canAttack = false;
   }

   public void setForFirstTurnP1() {
      this.setForNormalTurn();
      this.canAttack = false;
   }

   public void setForNormalTurn() {
      this.canPlayEnergy = true;
      this.canPlayTrainer = true;
      this.canPlaySupporter = true;
      this.canPlayItem = true;
      this.canPlayStadium = true;
      this.canRetreatActive = true;
      this.mulliganBonusDraws = 0;
      this.canAttack = true;
   }

   public boolean isCanPlayEnergy() {
      return this.canPlayEnergy;
   }

   public void setCanPlayEnergy(boolean canPlayEnergy) {
      this.canPlayEnergy = canPlayEnergy;
   }

   public boolean isCanPlayTrainer() {
      return true;
   }

   public void setCanPlayTrainer(boolean canPlayTrainer) {
      this.canPlayTrainer = canPlayTrainer;
   }

   public boolean isCanPlaySupporter() {
      return this.canPlaySupporter;
   }

   public void setCanPlaySupporter(boolean canPlaySupporter) {
      this.canPlaySupporter = canPlaySupporter;
   }

   public boolean isCanPlayItem() {
      return this.canPlayItem;
   }

   public void setCanPlayItem(boolean canPlayItem) {
      this.canPlayItem = canPlayItem;
   }

   public boolean isCanPlayStadium() {
      return this.canPlayStadium;
   }

   public void setCanPlayStadium(boolean canPlayStadium) {
      this.canPlayStadium = canPlayStadium;
   }

   public boolean isCanRetreatActive() {
      return this.canRetreatActive;
   }

   public void setCanRetreatActive(boolean canRetreatActive) {
      this.canRetreatActive = canRetreatActive;
   }

   public int getMulliganBonusDraws() {
      return this.mulliganBonusDraws;
   }

   public void setMulliganBonusDraws(int mulliganBonusDraws) {
      this.mulliganBonusDraws = mulliganBonusDraws;
   }

   public void addMulliganBonusDraws(int cards) {
      this.mulliganBonusDraws += cards;
   }

   public boolean isCanAttack() {
      return this.canAttack;
   }
}

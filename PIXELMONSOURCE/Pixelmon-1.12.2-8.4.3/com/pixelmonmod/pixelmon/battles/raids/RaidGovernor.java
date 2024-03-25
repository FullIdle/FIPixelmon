package com.pixelmonmod.pixelmon.battles.raids;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.RaidPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.tasks.RaidDynamaxTask;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.Entity;

public class RaidGovernor {
   public final RaidSettings settings;
   public int shieldUsesStatic;
   public float shieldHPPercent;
   public float shieldHPBound;
   public int shieldUses;
   public EnumBossMode bossMode;
   public float previousTurnHP = 100.0F;
   public boolean desperate = false;
   public boolean shouldRaiseShields = false;
   public int shields = 0;
   public int turnsAlive = 0;
   public int kills = 0;
   public boolean shockwaved = false;
   public int shockwaveFails = 0;
   public double shockwaveChance;
   public int turnCounter = 0;
   public int[] reviveTimer;
   public int dynamaxIndex = -1;
   public boolean hasDynamaxOccured = false;
   public boolean done = false;

   public RaidGovernor(RaidSettings settings) {
      this.settings = settings;
      this.bossMode = settings.bossMode;
   }

   public void init(EntityPixelmon entity) {
      this.settings.init(entity);
      this.shieldUsesStatic = this.settings.shields;
      this.shieldUses = this.settings.shields;
      this.shieldHPPercent = 1.0F / ((float)this.shieldUses + 1.0F) * 100.0F;
      this.shieldHPBound = this.shieldHPPercent * (float)this.shieldUses;
      this.shockwaveChance = this.settings.shockwaveChance;
      this.shockwaveFails = -2;
      this.reviveTimer = new int[this.settings.participants];
      Arrays.fill(this.reviveTimer, 0);
   }

   public RaidSettings getSettings() {
      return this.settings;
   }

   public ArrayList getMoves() {
      return this.getSettings().moveset;
   }

   public int getParticipantCount() {
      return this.settings.participants;
   }

   public int getLives() {
      return this.settings.lives;
   }

   public boolean isGigantamax() {
      return this.settings.gigantamax;
   }

   public boolean raiseShields() {
      boolean canShield = this.shieldUses > 0 && this.shields <= 0;
      if (canShield && this.shouldRaiseShields) {
         this.shields = this.getShieldStrength();
         this.shouldRaiseShields = false;
         --this.shieldUses;
         this.shieldHPBound = (float)this.shieldUses * this.shieldHPPercent;
         return true;
      } else {
         return false;
      }
   }

   public int getShieldStrength() {
      return this.settings.shieldStrength;
   }

   public int getAttacksPerTurn() {
      return this.settings.attacksPerTurn;
   }

   public boolean tryShockwave(boolean hasStatus) {
      if (this.shockwaveFails < 0) {
         ++this.shockwaveFails;
      } else {
         double chance = this.shockwaveChance * (double)(hasStatus ? 2 : 1);
         if (RandomHelper.rand.nextDouble() < chance) {
            this.shockwaveChance = this.settings.shockwaveChance;
            return true;
         }

         ++this.shockwaveFails;
         this.shockwaveChance += this.settings.shockwaveChance / (double)this.shockwaveFails;
      }

      return false;
   }

   public void knockout(int index) {
      this.reviveTimer[index] = 2;
   }

   public boolean isKnockedOut(int index) {
      return this.reviveTimer[index] > 0;
   }

   public boolean tryRevive(int index) {
      int var10002 = this.reviveTimer[index]--;
      return this.reviveTimer[index] <= 0;
   }

   public boolean canCheer(int index) {
      return this.reviveTimer[index] == 1;
   }

   public boolean tryCheer() {
      return RandomHelper.rand.nextDouble() < this.settings.cheerSuccessChance;
   }

   public int cycleDynamax(RaidPixelmonParticipant rpp) {
      if (!this.hasDynamaxOccured) {
         ++this.dynamaxIndex;
         if (this.dynamaxIndex >= this.getParticipantCount()) {
            this.dynamaxIndex = 0;
         }
      }

      if (this.dynamaxIndex >= 0 && rpp.getOpponents().size() > this.dynamaxIndex) {
         BattleParticipant bp = (BattleParticipant)rpp.getOpponents().get(this.dynamaxIndex);
         rpp.bc.sendToAll("raid.dynamax", bp.getDisplayName());
         Entity entity = bp.getEntity();
         if (entity != null) {
            rpp.bc.sendToPlayers(new RaidDynamaxTask(entity.func_110124_au()));
         }
      }

      return this.dynamaxIndex;
   }

   public void onDynamax() {
      this.hasDynamaxOccured = true;
      this.dynamaxIndex = -1;
   }

   public boolean incrementTurnCounter() {
      ++this.turnCounter;
      return this.turnCounter > this.settings.turns;
   }
}

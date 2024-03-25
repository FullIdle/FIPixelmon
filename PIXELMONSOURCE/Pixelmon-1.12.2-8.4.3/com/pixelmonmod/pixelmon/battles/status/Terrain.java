package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.EnumTerrain;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.Nonnull;

public abstract class Terrain extends GlobalStatusBase {
   protected transient int turnsToGo = 5;
   public String langStart;
   public String langEnd;

   public Terrain(StatusType type, String langStart, String langEnd) {
      super(type);
      this.langStart = langStart;
      this.langEnd = langEnd;
   }

   public void setTurns(int turns) {
      this.turnsToGo = turns;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      Terrain terrain = user.bc.globalStatusController.getTerrain();
      if (terrain != null && terrain.type == this.type) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         if (terrain != null) {
            user.bc.globalStatusController.removeGlobalStatus((GlobalStatusBase)terrain);
         }

         user.bc.sendToAll(this.langStart);
         Terrain newTerrain = this.getNewInstance();
         if (user.getUsableHeldItem().getHeldItemType() == EnumHeldItems.terrainExtender) {
            newTerrain.turnsToGo = 8;
         }

         user.bc.globalStatusController.addGlobalStatus(newTerrain);
      }

   }

   public abstract Terrain getNewInstance();

   @Nonnull
   public abstract EnumTerrain getTerrainType();

   public abstract EnumType getTypingForTerrain();

   public void applyRepeatedEffect(GlobalStatusController global) {
      if (--this.turnsToGo <= 0) {
         global.removeGlobalStatus((GlobalStatusBase)this);
         global.bc.sendToAll(this.langEnd);
      } else {
         this.applyRepeatedEffect(global.bc);
      }
   }

   public int getRemainingTurns() {
      return this.turnsToGo;
   }

   protected void applyRepeatedEffect(BattleControllerBase bc) {
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int allyBenefits = 0;
      int opponentBenefits = 0;
      Terrain currentTerrain = pw.bc.globalStatusController.getTerrain();

      Iterator var10;
      PixelmonWrapper opponent;
      for(var10 = pw.getTeamPokemon().iterator(); var10.hasNext(); allyBenefits -= currentTerrain.countBenefits(pw, opponent)) {
         opponent = (PixelmonWrapper)var10.next();
         allyBenefits += this.countBenefits(pw, opponent);
      }

      for(var10 = pw.getOpponentPokemon().iterator(); var10.hasNext(); opponentBenefits -= currentTerrain.countBenefits(pw, opponent)) {
         opponent = (PixelmonWrapper)var10.next();
         opponentBenefits += this.countBenefits(pw, opponent);
      }

      if (allyBenefits > opponentBenefits) {
         userChoice.raiseWeight((float)(40 * (allyBenefits - opponentBenefits)));
      }

   }

   protected boolean affectsPokemon(PixelmonWrapper pw) {
      return !pw.isAirborne() && !pw.hasStatus(StatusType.Flying, StatusType.UnderGround, StatusType.Submerged, StatusType.Vanish);
   }

   protected abstract int countBenefits(PixelmonWrapper var1, PixelmonWrapper var2);
}

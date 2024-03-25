package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Weather extends GlobalStatusBase {
   protected transient int turnsToGo;
   protected EnumHeldItems weatherRock;
   protected transient String langStart;
   protected transient String langContinue;
   protected transient String langEnd;
   public transient boolean extreme = false;

   public Weather(StatusType type, int turnsToGo, EnumHeldItems weatherRock, String langStart, String langContinue, String langEnd, boolean extreme) {
      super(type);
      this.turnsToGo = turnsToGo;
      this.weatherRock = weatherRock;
      this.langStart = langStart;
      this.langContinue = langContinue;
      this.langEnd = langEnd;
      this.extreme = extreme;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      Weather weather = user.bc.globalStatusController.getWeatherIgnoreAbility();
      if ((weather == null || weather.type != this.type) && user.bc.globalStatusController.canWeatherChange(this)) {
         int turns = this.getStartTurns(user);
         user.bc.sendToAll(this.langStart);
         user.bc.globalStatusController.addGlobalStatus(this.getNewInstance(turns));
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   private int getStartTurns(PixelmonWrapper user) {
      return user.getUsableHeldItem().getHeldItemType() == this.weatherRock ? 8 : 5;
   }

   public void setStartTurns(PixelmonWrapper user) {
      this.turnsToGo = this.getStartTurns(user);
   }

   public int getRemainingTurns() {
      return this.turnsToGo;
   }

   protected abstract Weather getNewInstance(int var1);

   public void applyRepeatedEffect(GlobalStatusController global) {
      if (--this.turnsToGo == 0) {
         global.removeGlobalStatus((GlobalStatusBase)this);
         global.bc.sendToAll(this.langEnd);
      } else {
         global.bc.sendToAll(this.langContinue);
         this.applyRepeatedEffect(global.bc);
      }
   }

   protected void applyRepeatedEffect(BattleControllerBase bc) {
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int allyBenefits = 0;
      int opponentBenefits = 0;
      Weather currentWeather = pw.bc.globalStatusController.getWeather();
      Iterator var10 = pw.getTeamPokemon().iterator();

      PixelmonWrapper opponent;
      while(var10.hasNext()) {
         opponent = (PixelmonWrapper)var10.next();
         allyBenefits += this.countBenefits(pw, opponent);
         if (currentWeather != null) {
            allyBenefits -= currentWeather.countBenefits(pw, opponent);
         }
      }

      var10 = pw.getOpponentPokemon().iterator();

      while(var10.hasNext()) {
         opponent = (PixelmonWrapper)var10.next();
         opponentBenefits += this.countBenefits(pw, opponent);
         if (currentWeather != null) {
            opponentBenefits -= currentWeather.countBenefits(pw, opponent);
         }
      }

      if (allyBenefits > opponentBenefits) {
         userChoice.raiseWeight((float)(40 * (allyBenefits - opponentBenefits)));
      }

   }

   protected abstract int countBenefits(PixelmonWrapper var1, PixelmonWrapper var2);
}

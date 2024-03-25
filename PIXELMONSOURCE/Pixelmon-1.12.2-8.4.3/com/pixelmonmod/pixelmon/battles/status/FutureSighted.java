package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class FutureSighted extends StatusBase {
   protected transient int turnsToGo = 3;
   transient PixelmonWrapper user;
   transient Attack attack;

   public FutureSighted() {
      super(StatusType.FutureSight);
   }

   public FutureSighted(PixelmonWrapper user, Attack attack) {
      super(StatusType.FutureSight);
      this.user = user;
      this.attack = attack;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      --this.turnsToGo;
      if (this.turnsToGo <= 0) {
         if (pw.isAlive()) {
            pw.bc.sendToAll("pixelmon.status.takefuturesight", pw.getNickname(), this.attack.getMove().getTranslatedName());
            this.user.attack = this.attack;
            this.user.targets = new ArrayList();
            this.user.targets.add(pw);
            this.user.bc = pw.bc;
            if (this.user.isFainted()) {
               this.user.setHealth(1);
               this.user.useAttackOnly();
               this.user.setHealth(0);
            } else {
               this.user.useAttackOnly();
            }
         }

         pw.removeStatus((StatusBase)this);
      }

   }

   public int getRemainingTurns() {
      return this.turnsToGo;
   }

   public boolean isTeamStatus() {
      return true;
   }

   public boolean isWholeTeamStatus() {
      return false;
   }
}

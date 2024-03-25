package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Iterator;

public class LunarDance extends StatusBase {
   public LunarDance() {
      super(StatusType.LunarDance);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.getParticipant().hasMorePokemonReserve()) {
         user.addStatus(new LunarDance(), user);
         user.doBattleDamage(user, (float)user.getHealth(), DamageTypeEnum.SELF);
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public boolean isWholeTeamStatus() {
      return false;
   }

   public void applyEffectOnSwitch(PixelmonWrapper pw) {
      pw.bc.sendToAll("pixelmon.effect.lunardance", pw.getNickname());
      boolean didHeal = false;
      if (pw.getMaxHealth() > pw.getHealth()) {
         pw.healEntityBy(pw.getHealthDeficit());
         didHeal = true;
      }

      Iterator var3 = pw.getMoveset().iterator();

      while(var3.hasNext()) {
         Attack attack = (Attack)var3.next();
         if (attack != null && attack.pp < attack.getMaxPP()) {
            attack.pp = attack.getMaxPP();
            didHeal = true;
         }
      }

      for(int i = 0; i < pw.getStatusSize(); ++i) {
         StatusType currentStatus = pw.getStatus(i).type;
         if (currentStatus.isPrimaryStatus() || currentStatus == StatusType.LunarDance) {
            if (currentStatus != StatusType.LunarDance) {
               didHeal = true;
            }

            pw.removeStatus(i);
            --i;
         }
      }

      if (!didHeal) {
         pw.bc.sendToAll("pixelmon.effect.healfailed", pw.getNickname());
      }

   }
}

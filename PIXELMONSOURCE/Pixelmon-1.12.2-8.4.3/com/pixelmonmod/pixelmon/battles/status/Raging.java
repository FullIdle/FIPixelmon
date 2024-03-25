package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Raging extends StatusBase {
   public Raging() {
      super(StatusType.Raging);
   }

   public void onDamageReceived(PixelmonWrapper user, PixelmonWrapper pokemon, Attack a, int damage, DamageTypeEnum damagetype) {
      if (pokemon.isAlive() && user != pokemon && (damagetype == DamageTypeEnum.ATTACK || damagetype == DamageTypeEnum.ATTACKFIXED) && pokemon.getBattleStats().modifyStat(1, (StatsType)StatsType.Attack)) {
         user.bc.sendToAll("pixelmon.status.ragebuilding", pokemon.getNickname());
      }

   }

   public void onAttackUsed(PixelmonWrapper user, Attack attack) {
      user.removeStatus((StatusBase)this);
   }
}

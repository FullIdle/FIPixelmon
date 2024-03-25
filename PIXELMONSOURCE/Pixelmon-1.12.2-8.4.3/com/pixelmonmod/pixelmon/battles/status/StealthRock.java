package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class StealthRock extends EntryHazard {
   public StealthRock() {
      super(StatusType.StealthRock, 1);
   }

   public boolean isTeamStatus() {
      return true;
   }

   public boolean isUnharmed(PixelmonWrapper pw) {
      return super.isUnharmed(pw) || pw.getBattleAbility() instanceof MagicGuard;
   }

   public int getDamage(PixelmonWrapper pw) {
      float effectiveness = EnumType.getTotalEffectiveness(pw.type, EnumType.Rock, pw.bc.rules.hasClause("inverse"));
      float modifier = effectiveness * 12.5F;
      int damage = pw.getPercentMaxHealth(modifier);
      return damage;
   }

   protected String getFirstLayerMessage() {
      return "pixelmon.effect.floatingstones";
   }

   protected String getAffectedMessage() {
      return "pixelmon.status.hurtbystealthrock";
   }

   public int getAIWeight() {
      return 30;
   }

   public EntryHazard getNewInstance() {
      return new StealthRock();
   }
}

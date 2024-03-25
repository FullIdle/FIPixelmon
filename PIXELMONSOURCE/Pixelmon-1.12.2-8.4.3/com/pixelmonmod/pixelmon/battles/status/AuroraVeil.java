package com.pixelmonmod.pixelmon.battles.status;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.Set;

public class AuroraVeil extends Screen {
   transient PixelmonWrapper user;
   private static final Set directDamageMoves = Sets.newHashSet(new String[]{"Bide", "Counter", "Dragon Rage", "Endeavor", "Final Gambit", "Guardian of Alola", "Metal Burst", "Mirror Coat", "Nature's Madness", "Night Shade", "Psywave", "Seismic Toss", "Sonic Boom", "Super Fang"});

   public AuroraVeil() {
      this((PixelmonWrapper)null, 5);
   }

   public AuroraVeil(int turns) {
      this((PixelmonWrapper)null, turns);
   }

   public AuroraVeil(PixelmonWrapper user, int turns) {
      super(StatusType.AuroraVeil, (StatsType)null, turns, "pixelmon.effect.auroraveil.raised", "pixelmon.effect.auroraveil.already", "pixelmon.status.auroraveil.woreoff");
      this.user = null;
      this.user = user;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0 || user.bc.simulateMode) {
         if (!(user.bc.globalStatusController.getWeather() instanceof Hail)) {
            user.bc.sendToAll("pixelmon.effect.itfailed");
            user.attack.moveResult.result = AttackResult.failed;
         } else if (user.hasStatus(this.type)) {
            user.bc.sendToAll(this.langFail, user.getNickname());
            user.attack.moveResult.result = AttackResult.failed;
         } else if (!user.hasStatus(StatusType.Reflect)) {
            int turns = user.getUsableHeldItem().getHeldItemType() == EnumHeldItems.lightClay ? 8 : 5;
            user.addTeamStatus(((AuroraVeil)this.getNewInstance(turns)).withUser(user), user);
            user.bc.sendToAll(this.langStart, user.getNickname());
         }
      }

   }

   public AuroraVeil withUser(PixelmonWrapper user) {
      this.user = user;
      return this;
   }

   public boolean shouldReduce(Attack a) {
      return true;
   }

   public float getDamageMultiplier(PixelmonWrapper user, PixelmonWrapper target) {
      if (target == user && user.hasStatus(StatusType.Confusion)) {
         return 1.0F;
      } else if (user.attack.didCrit) {
         return 1.0F;
      } else if (directDamageMoves.contains(user.attack.getMove().getAttackName())) {
         return 1.0F;
      } else {
         return target.bc.rules.battleType == EnumBattleType.Single ? 0.5F : 0.6666667F;
      }
   }

   protected Screen getNewInstance(int effectTurns) {
      return new AuroraVeil((PixelmonWrapper)null, effectTurns);
   }
}

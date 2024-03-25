package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Overcoat;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SandForce;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SandRush;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SandVeil;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.Iterator;
import java.util.List;

public class Sandstorm extends Weather {
   public Sandstorm() {
      this(5);
   }

   public Sandstorm(int turnsToGo) {
      super(StatusType.Sandstorm, turnsToGo, EnumHeldItems.smoothRock, "pixelmon.effect.sandstorm", "pixelmon.status.sandstormrage", "pixelmon.status.sandstormsubside", false);
   }

   protected Weather getNewInstance(int turns) {
      return new Sandstorm(turns);
   }

   public void applyRepeatedEffect(BattleControllerBase bc) {
      Iterator var2 = bc.getDefaultTurnOrder().iterator();

      while(var2.hasNext()) {
         PixelmonWrapper p = (PixelmonWrapper)var2.next();
         if (!this.isImmune(p)) {
            p.bc.sendToAll("pixelmon.status.buffetedbysandstorm", p.getNickname());
            p.doBattleDamage(p, (float)p.getPercentMaxHealth(6.25F), DamageTypeEnum.WEATHER);
         }
      }

   }

   public boolean isImmune(PixelmonWrapper p) {
      AbilityBase ability = p.getBattleAbility();
      return p.hasType(EnumType.Ground, EnumType.Rock, EnumType.Steel) || p.isFainted() || ability instanceof MagicGuard || ability instanceof Overcoat || ability instanceof SandForce || ability instanceof SandRush || ability instanceof SandVeil || p.getUsableHeldItem().getHeldItemType() == EnumHeldItems.safetyGoggles;
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (user.hasType(EnumType.Rock)) {
         int var10001 = StatsType.SpecialDefence.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.5);
      }

      return stats;
   }

   protected int countBenefits(PixelmonWrapper user, PixelmonWrapper target) {
      int benefits = 0;
      if (!this.isImmune(target)) {
         --benefits;
      } else if (target.hasType(EnumType.Rock)) {
         ++benefits;
      }

      AbilityBase ability = target.getBattleAbility();
      if (ability instanceof SandForce || ability instanceof SandRush || ability instanceof SandVeil) {
         ++benefits;
      }

      List moveset = user.getBattleAI().getMoveset(target);
      if (Attack.hasAttack(moveset, "Weather Ball")) {
         ++benefits;
      }

      if (Attack.hasAttack(moveset, "SolarBeam")) {
         --benefits;
      }

      if (Attack.hasAttack(moveset, "Moonlight", "Morning Sun", "Synthesis")) {
         --benefits;
      }

      return benefits;
   }
}

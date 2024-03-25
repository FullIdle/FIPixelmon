package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.IceBody;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Overcoat;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SnowCloak;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.Iterator;
import java.util.List;

public class Hail extends Weather {
   public Hail() {
      this(5);
   }

   public Hail(int turnsToGo) {
      super(StatusType.Hail, turnsToGo, EnumHeldItems.icyRock, "pixelmon.effect.starthail", "pixelmon.status.heavyhail", "pixelmon.status.hailstopped", false);
   }

   protected Weather getNewInstance(int turns) {
      return new Hail(turns);
   }

   public void applyRepeatedEffect(BattleControllerBase bc) {
      Iterator var2 = bc.getDefaultTurnOrder().iterator();

      while(var2.hasNext()) {
         PixelmonWrapper p = (PixelmonWrapper)var2.next();
         if (!this.isImmune(p)) {
            p.bc.sendToAll("pixelmon.status.hurthail", p.getNickname());
            p.doBattleDamage(p, (float)p.getPercentMaxHealth(6.25F), DamageTypeEnum.WEATHER);
         }
      }

   }

   public boolean isImmune(PixelmonWrapper p) {
      AbilityBase ability = p.getBattleAbility();
      return p.hasType(EnumType.Ice) || p.isFainted() || ability instanceof MagicGuard || ability instanceof Overcoat || ability instanceof SnowCloak || ability instanceof IceBody || p.getUsableHeldItem().getHeldItemType() == EnumHeldItems.safetyGoggles;
   }

   protected int countBenefits(PixelmonWrapper user, PixelmonWrapper target) {
      int benefits = 0;
      if (!this.isImmune(target)) {
         --benefits;
      }

      AbilityBase ability = target.getBattleAbility();
      if (ability instanceof IceBody || ability instanceof SnowCloak) {
         ++benefits;
      }

      List moveset = user.getBattleAI().getMoveset(target);
      if (Attack.hasAttack(moveset, "Blizzard", "Weather Ball")) {
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

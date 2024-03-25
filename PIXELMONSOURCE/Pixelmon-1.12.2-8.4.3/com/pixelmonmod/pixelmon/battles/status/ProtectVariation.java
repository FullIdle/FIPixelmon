package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Feint;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.UnseenFist;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.forms.EnumAegislash;
import java.util.Iterator;

public abstract class ProtectVariation extends StatusBase {
   public ProtectVariation(StatusType type) {
      super(type);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.bc.simulateMode) {
         if (this instanceof KingsShield && user.getSpecies() == EnumSpecies.Aegislash && user.getForm() == EnumAegislash.BLADE.getForm()) {
            user.setForm(EnumAegislash.SHIELD);
            user.bc.modifyStats(user);
            user.bc.sendToAll("pixelmon.abilities.stancechange.shield", user.getNickname());
         } else if (this instanceof KingsShield && user.getSpecies() == EnumSpecies.Aegislash && user.getForm() == EnumAegislash.BLADE_ALTER.getForm()) {
            user.setForm(EnumAegislash.SHIELD_ALTER);
            user.bc.modifyStats(user);
            user.bc.sendToAll("pixelmon.abilities.stancechange.shield", user.getNickname());
         }

         float successChance = 1.0F / (float)Math.pow(3.0, (double)user.protectsInARow);
         boolean successful = true;
         if (this.canFail()) {
            successful = RandomHelper.getRandomChance(successChance);
         }

         successful = successful && !user.bc.isLastMover() && this.addStatus(user);
         if (successful) {
            if (user.protectsInARow < 6) {
               ++user.protectsInARow;
            }

            this.displayMessage(user);
         } else {
            user.protectsInARow = 0;
            user.bc.sendToAll("pixelmon.effect.effectfailed");
         }

      }
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (user.attack.getMove().getTargetingInfo().hitsAll && user.attack.getMove().getTargetingInfo().hitsSelf) {
         return false;
      } else {
         if (!pokemon.getTeamPokemon().contains(user)) {
            Iterator var3 = user.attack.getMove().effects.iterator();

            while(var3.hasNext()) {
               EffectBase e = (EffectBase)var3.next();
               if (e instanceof Vanish || e instanceof Feint) {
                  return false;
               }
            }
         }

         if (user.getBattleAbility().doesAttackUserIgnoreProtect(user, pokemon, user.attack)) {
            return false;
         } else if (user.isDynamax > 0) {
            if (this instanceof MaxGuard) {
               return true;
            } else {
               return user.attack.getAttackCategory() == AttackCategory.STATUS;
            }
         } else {
            return !user.usingZ || user.attack.getAttackCategory() == AttackCategory.STATUS;
         }
      }
   }

   public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a, DamageTypeEnum damageType) {
      if (user != target && (user.usingZ || user.isDynamax > 0) && a.getAttackCategory() != AttackCategory.STATUS && !a.isAttack("G-Max One Blow", "G-Max Rapid Flow") && !user.getBattleAbility().isAbility(UnseenFist.class)) {
         if (user.isDynamax > 0) {
            user.bc.sendToAll("pixelmon.effect.dynamaxprotect", target.getNickname());
         }

         return (int)Math.ceil((double)((float)damage / 4.0F));
      } else {
         return damage;
      }
   }

   protected abstract boolean addStatus(PixelmonWrapper var1);

   protected abstract void displayMessage(PixelmonWrapper var1);

   protected boolean canFail() {
      return true;
   }
}

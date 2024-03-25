package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Paralysis;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.forms.EnumCramorant;

public class GulpMissile extends AbilityBase {
   private static final transient float ONE_QUARTER = 25.0F;

   public void postProcessAttackUser(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a) {
      if (a.getAttackCategory() != AttackCategory.STATUS && pokemon.getFormEnum() == EnumCramorant.NORMAL && a.isAttack("Surf", "Dive") && !pokemon.hasStatus(StatusType.MultiTurn)) {
         if (pokemon.getHealthPercent() < 50.0F) {
            pokemon.setForm(EnumCramorant.GORGING);
            pokemon.bc.sendToAll("pixelmon.abilities.gulpmissile.gorging.eat", pokemon.getNickname());
         } else {
            pokemon.setForm(EnumCramorant.GULPING);
            pokemon.bc.sendToAll("pixelmon.abilities.gulpmissile.gulping.eat", pokemon.getNickname());
         }
      }

   }

   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user != target) {
         if (target.getFormEnum() != EnumCramorant.NORMAL) {
            if (!user.getBattleAbility().isAbility(MagicGuard.class)) {
               user.doBattleDamage(target, (float)user.getPercentMaxHealth(25.0F), DamageTypeEnum.ABILITY);
            }

            if (target.getFormEnum() == EnumCramorant.GULPING) {
               target.bc.sendToAll("pixelmon.abilities.gulpmissile.gulping.spit", target.getNickname(), user.getNickname());
               user.getBattleStats().modifyStat(-1, (StatsType)StatsType.Defence);
            } else {
               target.bc.sendToAll("pixelmon.abilities.gulpmissile.gorging.spit", target.getNickname(), user.getNickname());
               Paralysis.paralyze(target, user, (Attack)null, true);
            }

            this.revertForm(target);
         }

      }
   }

   public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
      this.revertForm(oldPokemon);
   }

   public void applyDynamaxEffect(PixelmonWrapper pokemon) {
      this.revertForm(pokemon);
   }

   private void revertForm(PixelmonWrapper pokemon) {
      pokemon.setForm(EnumCramorant.NORMAL);
   }
}

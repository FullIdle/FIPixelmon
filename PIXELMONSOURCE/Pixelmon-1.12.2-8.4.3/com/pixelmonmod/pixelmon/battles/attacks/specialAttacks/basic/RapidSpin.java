package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.EntryHazard;
import com.pixelmonmod.pixelmon.battles.status.PartialTrap;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.text.TextComponentTranslation;

public class RapidSpin extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.isFainted()) {
         PartialTrap partialTrap = (PartialTrap)user.getStatus(StatusType.PartialTrap);
         if (partialTrap != null) {
            user.bc.sendToAll("pixelmon.effect.freefrom", user.getNickname(), partialTrap.variant.getTranslatedName());
            user.removeStatus((StatusBase)partialTrap);
         }

         if (user.removeStatus(StatusType.Leech)) {
            user.bc.sendToAll("pixelmon.effect.freefrom", user.getNickname(), new TextComponentTranslation("attack.leech_seed.name", new Object[0]));
         }

         if (user.removeTeamStatus(StatusType.Spikes, StatusType.StealthRock, StatusType.ToxicSpikes, StatusType.StickyWeb)) {
            user.bc.sendToAll("pixelmon.effect.clearspikes", user.getNickname());
         }

      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = pw.getStatuses().iterator();

         while(var7.hasNext()) {
            StatusBase status = (StatusBase)var7.next();
            StatusType type = status.type;
            if (type == StatusType.PartialTrap) {
               userChoice.raiseWeight(10.0F);
            } else if (type == StatusType.Leech) {
               userChoice.raiseWeight(20.0F);
            } else if (status instanceof EntryHazard) {
               EntryHazard hazard = (EntryHazard)status;
               userChoice.raiseWeight((float)(hazard.getAIWeight() * hazard.getNumLayers() * (pw.getParticipant().countAblePokemon() - pw.bc.rules.battleType.numPokemon)));
            }
         }

      }
   }

   public void dealtDamage(PixelmonWrapper attacker, PixelmonWrapper defender, Attack attack, DamageTypeEnum damageType) {
      attacker.getBattleStats().modifyStat(1, (StatsType)StatsType.Speed);
   }
}

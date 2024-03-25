package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Guts;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MarvelScale;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.QuickFeet;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Synchronize;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;

public class Paralysis extends StatusPersist {
   public Paralysis() {
      super(StatusType.Paralysis);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.checkChance()) {
         paralyze(user, target, user.attack, true);
      }

   }

   public static boolean paralyze(PixelmonWrapper user, PixelmonWrapper target, Attack attack, boolean showMessage) {
      return (new Paralysis()).addStatus(user, target, attack, showMessage, "pixelmon.effect.alreadyparalyzed", "pixelmon.effect.isparalyzed");
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (RandomHelper.getRandomChance(25)) {
         user.bc.sendToAll("pixelmon.status.isparalyzed", user.getNickname());
         return false;
      } else {
         return true;
      }
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (!(user.getBattleAbility() instanceof QuickFeet)) {
         int var10001 = StatsType.Speed.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 0.5);
      }

      return stats;
   }

   public StatusPersist restoreFromNBT(NBTTagCompound nbt) {
      return new Paralysis();
   }

   public boolean isImmune(PixelmonWrapper pokemon) {
      return pokemon.hasType(EnumType.Electric);
   }

   public String getCureMessage() {
      return "pixelmon.status.paralysiscure";
   }

   public String getCureMessageItem() {
      return "pixelmon.status.paralysiscureitem";
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         float weight = this.getWeightWithChance(25.0F);
         if (userChoice.isMiddleTier()) {
            for(Iterator var8 = userChoice.targets.iterator(); var8.hasNext(); userChoice.raiseWeight(weight)) {
               PixelmonWrapper target = (PixelmonWrapper)var8.next();
               AbilityBase ability = target.getBattleAbility();
               if (ability instanceof Guts || ability instanceof MarvelScale || ability instanceof QuickFeet || ability instanceof Synchronize) {
                  return;
               }

               if (!MoveChoice.hasPriority(bestOpponentChoices) && MoveChoice.canOutspeed(bestOpponentChoices, pw, bestUserChoices)) {
                  userChoice.raiseWeight(weight * 3.0F);
               }
            }
         }

      }
   }
}

package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Corrosion;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Guts;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MarvelScale;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PoisonHeal;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.QuickFeet;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Synchronize;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ToxicBoost;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

public class Poison extends StatusPersist {
   private static final transient float AI_WEIGHT = 25.0F;

   public Poison() {
      this(StatusType.Poison);
   }

   public Poison(StatusType type) {
      super(type);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.checkChance()) {
         poison(user, target, user.attack, true);
      }

   }

   public static boolean poison(PixelmonWrapper user, PixelmonWrapper target, Attack attack, boolean showMessage) {
      if (canPoison(user, target, attack, showMessage)) {
         TextComponentTranslation message = null;
         if (showMessage) {
            message = ChatHandler.getMessage("pixelmon.effect.poisoned", target.getNickname());
         }

         boolean result = target.addStatus(new Poison(), user, message);
         if (!result && attack != null && attack.getAttackCategory() == AttackCategory.STATUS) {
            user.setAttackFailed();
         }

         return result;
      } else {
         return false;
      }
   }

   public static boolean canPoison(PixelmonWrapper user, PixelmonWrapper target, Attack attack, boolean showMessage) {
      boolean isStatusMove = attack != null && attack.getAttackCategory() == AttackCategory.STATUS;
      if (!target.hasStatus(StatusType.Poison) && !target.hasStatus(StatusType.PoisonBadly)) {
         if (target.hasType(EnumType.Poison, EnumType.Steel) && !(user.getBattleAbility() instanceof Corrosion)) {
            if (showMessage && isStatusMove) {
               user.bc.sendToAll("pixelmon.battletext.noeffect", target.getNickname());
               user.setAttackFailed();
            }

            return false;
         } else if (target.hasPrimaryStatus()) {
            if (showMessage && isStatusMove) {
               user.bc.sendToAll("pixelmon.effect.effectfailed");
               user.setAttackFailed();
            }

            return false;
         } else {
            return true;
         }
      } else {
         if (showMessage && isStatusMove) {
            user.bc.sendToAll("pixelmon.effect.alreadypoisoned", target.getNickname());
            user.setAttackFailed();
         }

         return false;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      AbilityBase ability = pw.getBattleAbility();
      if (!(ability instanceof MagicGuard)) {
         if (ability instanceof PoisonHeal) {
            if (pw.hasFullHealth() || pw.hasStatus(StatusType.HealBlock)) {
               return;
            }

            pw.bc.sendToAll("pixelmon.abilities.poisonheal", pw.getNickname());
            pw.healByPercent(12.5F);
         } else {
            pw.bc.sendToAll("pixelmon.status.hurtbypoison", pw.getNickname());
            pw.doBattleDamage(pw, this.getPoisonDamage(pw), DamageTypeEnum.STATUS);
         }

      }
   }

   protected float getPoisonDamage(PixelmonWrapper pw) {
      return (float)pw.getPercentMaxHealth(12.5F);
   }

   public StatusPersist restoreFromNBT(NBTTagCompound nbt) {
      return new Poison();
   }

   public boolean isImmune(PixelmonWrapper pokemon) {
      return pokemon.hasType(EnumType.Poison, EnumType.Steel);
   }

   public String getCureMessage() {
      return "pixelmon.status.poisoncure";
   }

   public String getCureMessageItem() {
      return "pixelmon.status.poisoncureitem";
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      boolean offensive = userChoice.isOffensiveMove();
      float weight = this.getWeightWithChance(25.0F);
      if (!offensive || userChoice.isMiddleTier()) {
         boolean hitsAlly = userChoice.hitsAlly();
         if (!offensive || !hitsAlly) {
            Iterator var10 = userChoice.targets.iterator();

            while(true) {
               while(var10.hasNext()) {
                  PixelmonWrapper target = (PixelmonWrapper)var10.next();
                  AbilityBase ability = target.getBattleAbility();
                  boolean beneficial = ability instanceof Guts || ability instanceof MarvelScale || ability instanceof PoisonHeal || ability instanceof QuickFeet || ability instanceof ToxicBoost || Attack.hasAttack(pw.getBattleAI().getMoveset(target), "Facade");
                  if (beneficial && hitsAlly) {
                     userChoice.raiseWeight(weight);
                  } else if (beneficial ^ hitsAlly) {
                     userChoice.raiseWeight(-weight);
                  } else if (!(ability instanceof MagicGuard) && !(ability instanceof Synchronize)) {
                     userChoice.raiseWeight(weight);
                  }
               }

               return;
            }
         }
      }
   }
}

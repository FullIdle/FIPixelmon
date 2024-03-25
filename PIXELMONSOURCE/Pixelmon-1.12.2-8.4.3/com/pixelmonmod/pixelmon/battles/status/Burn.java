package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.FlareBoost;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Guts;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Heatproof;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MarvelScale;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.QuickFeet;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Synchronize;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

public class Burn extends StatusPersist {
   private static final transient float AI_WEIGHT = 25.0F;

   public Burn() {
      super(StatusType.Burn);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.checkChance()) {
         burn(user, target, user.attack, true);
      }

   }

   public static boolean burn(PixelmonWrapper user, PixelmonWrapper target, Attack attack, boolean showMessage) {
      boolean isStatusMove = attack != null && attack.getAttackCategory() == AttackCategory.STATUS;
      if (target.hasType(EnumType.Fire)) {
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
         TextComponentTranslation message = null;
         if (showMessage) {
            message = ChatHandler.getMessage("pixelmon.effect.burnt", target.getNickname());
         }

         boolean result = target.addStatus(new Burn(), user, message);
         if (!result && isStatusMove) {
            user.setAttackFailed();
         }

         return result;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      AbilityBase userAbility = pw.getBattleAbility();
      if (!(userAbility instanceof MagicGuard)) {
         pw.bc.sendToAll("pixelmon.status.burnhurt", pw.getNickname());
         int fraction = userAbility instanceof Heatproof ? 32 : 16;
         pw.doBattleDamage(pw, (float)pw.getPercentMaxHealth(100.0F / (float)fraction), DamageTypeEnum.STATUS);
      }

   }

   public StatusPersist restoreFromNBT(NBTTagCompound nbt) {
      return new Burn();
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack attack) {
      return !user.getBattleAbility().isAbility(Guts.class) && !attack.isAttack("Facade") && attack.getAttackCategory() == AttackCategory.PHYSICAL ? new int[]{(int)Math.ceil((double)((float)power / 2.0F)), accuracy} : new int[]{power, accuracy};
   }

   public boolean isImmune(PixelmonWrapper pokemon) {
      return pokemon.hasType(EnumType.Fire);
   }

   public String getCureMessage() {
      return "pixelmon.status.burncure";
   }

   public String getCureMessageItem() {
      return "pixelmon.status.burncureitem";
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      boolean offensive = userChoice.isOffensiveMove();
      float weight = this.getWeightWithChance(25.0F);
      if (!offensive || userChoice.isMiddleTier()) {
         boolean hitsAlly = userChoice.hitsAlly();
         if (!offensive || !hitsAlly) {
            ArrayList opponents = pw.getOpponentPokemon();
            Iterator var11 = userChoice.targets.iterator();

            while(true) {
               PixelmonWrapper target;
               AbilityBase ability;
               do {
                  while(true) {
                     do {
                        if (!var11.hasNext()) {
                           return;
                        }

                        target = (PixelmonWrapper)var11.next();
                     } while(offensive && !burn(pw, target, userChoice.attack, false));

                     ability = target.getBattleAbility();
                     boolean beneficial = ability instanceof FlareBoost || ability instanceof Guts || ability instanceof MarvelScale || ability instanceof QuickFeet;
                     if (beneficial && hitsAlly) {
                        break;
                     }

                     if (beneficial ^ hitsAlly) {
                        userChoice.raiseWeight(-weight);
                     } else if (!(ability instanceof Synchronize)) {
                        pw.getBattleAI().weightStatusOpponentOptions(pw, userChoice, target, new Burn(), opponents, bestOpponentChoices);
                        if (!(ability instanceof MagicGuard)) {
                           userChoice.raiseWeight(weight);
                        }
                     }
                  }
               } while(!(ability instanceof Guts) && target.getMoveset().hasAttackCategory(AttackCategory.PHYSICAL));

               userChoice.raiseWeight(weight);
            }
         }
      }
   }
}

package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Iterator;

public class PartialTrap extends StatusBase {
   public transient AttackBase variant;
   private transient int turnsToGo;
   private transient PixelmonWrapper trapper;

   public PartialTrap() {
      super(StatusType.PartialTrap);
   }

   public PartialTrap(AttackBase variant, PixelmonWrapper trapper) {
      super(StatusType.PartialTrap);
      this.variant = variant;
      this.trapper = trapper;
      if (trapper.getUsableHeldItem().getHeldItemType() == EnumHeldItems.gripClaw) {
         this.turnsToGo = 7;
      } else {
         this.turnsToGo = RandomHelper.getRandomNumberBetween(4, 5);
      }

   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      AttackBase variant = user.attack.getMove();
      if (target.addStatus(new PartialTrap(variant, user), user)) {
         if (!variant.isAttack("Sand Tomb") && !variant.isAttack("G-Max Sandblast")) {
            if (variant.isAttack("Clamp")) {
               user.bc.sendToAll("pixelmon.effect.clamped", user.getNickname(), target.getNickname());
            } else if (variant.isAttack("Magma Storm")) {
               user.bc.sendToAll("pixelmon.effect.swirlingmagma", target.getNickname());
            } else if (!variant.isAttack("Fire Spin") && !variant.isAttack("G-Max Centiferno")) {
               if (variant.isAttack("Bind")) {
                  user.bc.sendToAll("pixelmon.effect.squeezed", target.getNickname(), user.getNickname());
               } else if (variant.isAttack("Wrap")) {
                  user.bc.sendToAll("pixelmon.effect.wrapped", target.getNickname(), user.getNickname());
               } else if (variant.isAttack("Whirlpool")) {
                  user.bc.sendToAll("pixelmon.effect.vortex", target.getNickname());
               } else if (variant.isAttack("Snap Trap")) {
                  user.bc.sendToAll("pixelmon.effect.snaptrap", target.getNickname());
               }
            } else {
               user.bc.sendToAll("pixelmon.effect.firevortex", target.getNickname());
            }
         } else {
            user.bc.sendToAll("pixelmon.effect.sandtomb", target.getNickname());
         }
      }

   }

   public boolean stopsSwitching() {
      return true;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (!pw.bc.getActiveUnfaintedPokemon().contains(this.trapper) && !this.variant.isAttack("G-Max Sandblast") && !this.variant.isAttack("G-Max Centiferno")) {
         pw.removeStatus((StatusBase)this);
      } else if (--this.turnsToGo >= 0) {
         if (!(pw.getBattleAbility() instanceof MagicGuard)) {
            int fraction = this.trapper.getUsableHeldItem().getHeldItemType() == EnumHeldItems.bindingBand ? 6 : 8;
            pw.doBattleDamage(pw, (float)pw.getPercentMaxHealth(100.0F / (float)fraction), DamageTypeEnum.STATUS);
            pw.bc.sendToAll("pixelmon.status.hurtby", pw.getNickname(), this.variant.getTranslatedName());
         }
      } else {
         pw.removeStatus((StatusBase)this);
         if (!this.variant.isAttack("Sand Tomb") && !this.variant.isAttack("G-Max Sandblast")) {
            if (this.variant.isAttack("Clamp")) {
               pw.bc.sendToAll("pixelmon.status.freefromclamp", pw.getNickname());
            } else if (this.variant.isAttack("Whirlpool")) {
               pw.bc.sendToAll("pixelmon.status.freefromwhirlpool", pw.getNickname());
            } else if (this.variant.isAttack("Wrap")) {
               pw.bc.sendToAll("pixelmon.status.freefromwrap", pw.getNickname());
            } else if (this.variant.isAttack("Bind")) {
               pw.bc.sendToAll("pixelmon.status.freefrombind", pw.getNickname());
            } else if (!this.variant.isAttack("Fire Spin") && !this.variant.isAttack("G-Max Centiferno")) {
               if (this.variant.isAttack("Snap Trap")) {
                  pw.bc.sendToAll("pixelmon.status.freefromsnaptrap", pw.getNickname());
               }
            } else {
               pw.bc.sendToAll("pixelmon.status.freefromfirespin", pw.getNickname());
            }
         } else {
            pw.bc.sendToAll("pixelmon.status.freefromsandtomb", pw.getNickname());
         }

      }
   }

   public int getRemainingTurns() {
      return this.turnsToGo;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         float weight;
         for(Iterator var7 = userChoice.targets.iterator(); var7.hasNext(); userChoice.raiseWeight(weight)) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            if (target.getBattleAbility() instanceof MagicGuard) {
               return;
            }

            weight = 12.5F;
            if (pw.getUsableHeldItem().getHeldItemType() == EnumHeldItems.bindingBand) {
               weight *= 2.0F;
            }
         }

      }
   }
}

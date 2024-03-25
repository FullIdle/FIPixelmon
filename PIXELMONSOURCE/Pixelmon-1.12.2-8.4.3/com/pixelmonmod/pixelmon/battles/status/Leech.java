package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.LiquidOoze;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.ArrayList;
import java.util.Iterator;

public class Leech extends StatusBase {
   transient PixelmonWrapper leechRecipient;
   transient int recipientPosition;

   public Leech() {
      super(StatusType.Leech);
   }

   public Leech(PixelmonWrapper leechRecipient) {
      super(StatusType.Leech);
      this.leechRecipient = leechRecipient;
      this.recipientPosition = leechRecipient.battlePosition;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(StatusType.Leech)) {
         user.bc.sendToAll("pixelmon.effect.alreadyseeded", target.getNickname());
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         if (!target.hasType(EnumType.Grass)) {
            if (target.addStatus(new Leech(user), user)) {
               user.bc.sendToAll("pixelmon.effect.seedplanted", user.getNickname());
            }
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            user.attack.moveResult.result = AttackResult.failed;
         }

      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (this.leechRecipient != null) {
         for(Iterator var2 = this.leechRecipient.getTeamPokemon().iterator(); var2.hasNext(); this.leechRecipient = null) {
            PixelmonWrapper recipient = (PixelmonWrapper)var2.next();
            if (recipient.battlePosition == this.recipientPosition) {
               this.leechRecipient = recipient;
               break;
            }
         }
      }

      AbilityBase victimAbility = pw.getBattleAbility();
      if (this.leechRecipient != null && !this.leechRecipient.isFainted() && !(victimAbility instanceof MagicGuard)) {
         pw.bc.sendToAll("pixelmon.status.drainhealth", this.leechRecipient.getNickname(), pw.getNickname());
         int dmg = pw.getPercentMaxHealth(12.5F);
         pw.doBattleDamage(this.leechRecipient, (float)dmg, DamageTypeEnum.STATUS);
         if (ItemHeld.canUseItem(this.leechRecipient) && this.leechRecipient.getUsableHeldItem().getHeldItemType() == EnumHeldItems.bigRoot) {
            dmg = (int)((double)dmg * 1.3);
         }

         if (victimAbility instanceof LiquidOoze) {
            this.leechRecipient.bc.sendToAll("pixelmon.abilities.liquidooze", this.leechRecipient.getNickname());
            this.leechRecipient.doBattleDamage(pw, (float)dmg, DamageTypeEnum.ABILITY);
         } else if (!this.leechRecipient.hasStatus(StatusType.HealBlock)) {
            this.leechRecipient.healEntityBy(dmg);
         }

      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            AbilityBase targetAbility = target.getBattleAbility();
            if (!(targetAbility instanceof LiquidOoze) && !(targetAbility instanceof MagicGuard)) {
               userChoice.raiseWeight(35.0F);
            }
         }

      }
   }
}

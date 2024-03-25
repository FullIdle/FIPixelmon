package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemBlackSludge extends ItemHeld {
   public ItemBlackSludge() {
      super(EnumHeldItems.blackSludge, "black_sludge");
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      int damage = pw.getPercentMaxHealth(6.25F);
      if (pw.hasType(EnumType.Poison)) {
         if (pw.hasFullHealth()) {
            return;
         }

         pw.bc.sendToAll("pixelmon.helditems.blacksludge", pw.getNickname());
         pw.healEntityBy(damage);
      } else if (!(pw.getBattleAbility() instanceof MagicGuard)) {
         pw.bc.sendToAll("pixelmon.helditems.blacksludgepsn", pw.getNickname());
         pw.doBattleDamage(pw, (float)(damage * 2), DamageTypeEnum.ITEM);
      }

   }
}

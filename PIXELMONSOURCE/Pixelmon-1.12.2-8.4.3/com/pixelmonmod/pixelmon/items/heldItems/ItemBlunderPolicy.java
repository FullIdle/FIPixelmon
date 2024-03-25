package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.OHKO;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.Iterator;

public class ItemBlunderPolicy extends ItemHeld {
   public ItemBlunderPolicy() {
      super(EnumHeldItems.blunderPolicy, "blunder_policy");
   }

   public void onMiss(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack) {
      if (!target.hasStatus(StatusType.SkyDropping, StatusType.SkyDropped, StatusType.Submerged, StatusType.Flying, StatusType.UnderGround, StatusType.Vanish)) {
         Iterator var4 = attack.getMove().effects.iterator();

         EffectBase effect;
         do {
            if (!var4.hasNext()) {
               attacker.bc.sendToAll("pixelmon.abilities.activated", attacker.getNickname(), attacker.getHeldItem().getLocalizedName());
               attacker.getBattleStats().modifyStat(2, (StatsType)StatsType.Speed);
               attacker.consumeItem();
               return;
            }

            effect = (EffectBase)var4.next();
         } while(!(effect instanceof OHKO));

      }
   }
}

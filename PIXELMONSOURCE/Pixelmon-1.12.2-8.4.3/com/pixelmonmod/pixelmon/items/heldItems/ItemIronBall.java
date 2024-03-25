package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Levitate;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.List;

public class ItemIronBall extends ItemHeld {
   public ItemIronBall() {
      super(EnumHeldItems.ironBall, "iron_ball");
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if ((newPokemon.removeStatuses(StatusType.MagnetRise, StatusType.Telekinesis) || newPokemon.hasType(EnumType.Flying) || newPokemon.getBattleAbility() instanceof Levitate) && !newPokemon.bc.globalStatusController.hasStatus(StatusType.Gravity) && !newPokemon.hasStatus(StatusType.SmackedDown)) {
         newPokemon.bc.sendToAll("pixelmon.helditems.ironball", newPokemon.getNickname());
      }

   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      int var10001 = StatsType.Speed.getStatIndex();
      stats[var10001] = (int)((double)stats[var10001] * 0.5);
      return stats;
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      return user.attack != null && user.attack.getType() == EnumType.Ground ? EnumType.ignoreType(target.type, EnumType.Flying) : target.type;
   }
}

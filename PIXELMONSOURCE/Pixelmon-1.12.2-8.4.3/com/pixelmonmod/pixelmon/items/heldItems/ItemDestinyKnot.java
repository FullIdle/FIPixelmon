package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Infatuated;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemDestinyKnot extends HeldItem {
   public ItemDestinyKnot() {
      super(EnumHeldItems.destinyKnot, "destiny_knot");
   }

   public void onStatusAdded(PixelmonWrapper user, PixelmonWrapper opponent, StatusBase status) {
      if (status instanceof Infatuated && !opponent.hasStatus(StatusType.Infatuated)) {
         opponent.addStatus(new Infatuated(user), user);
      }

   }
}

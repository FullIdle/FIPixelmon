package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Effectiveness;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.Iterator;
import java.util.List;

public class ItemRingTarget extends ItemHeld {
   public ItemRingTarget() {
      super(EnumHeldItems.ringTarget, "ring_target");
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      if (user != null && user.attack != null) {
         EnumType attackingType = user.attack.getType();
         Iterator var4 = target.type.iterator();

         while(var4.hasNext()) {
            EnumType targetType = (EnumType)var4.next();
            if (EnumType.getEffectiveness(attackingType, targetType) == Effectiveness.None.value) {
               return EnumType.ignoreType(target.type, targetType);
            }
         }
      }

      return target.type;
   }
}

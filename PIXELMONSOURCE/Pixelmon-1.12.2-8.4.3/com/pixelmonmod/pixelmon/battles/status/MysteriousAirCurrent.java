package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MysteriousAirCurrent extends Weather {
   public MysteriousAirCurrent() {
      super(StatusType.MysteriousAirCurrent, -1, (EnumHeldItems)null, (String)null, (String)null, (String)null, true);
   }

   protected Weather getNewInstance(int turns) {
      return new MysteriousAirCurrent();
   }

   protected int countBenefits(PixelmonWrapper user, PixelmonWrapper target) {
      return 1;
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.attack != null && Attack.dealsDamage(user.attack) && target.hasType(EnumType.Flying)) {
         if (user.bc.globalStatusController.getGlobalStatus(StatusType.Gravity) != null) {
            return user.bc.globalStatusController.getGlobalStatus(StatusType.Gravity).getEffectiveTypes(user, target);
         }

         float effectiveness = EnumType.getTotalEffectiveness(Arrays.asList(EnumType.Flying), user.attack.getType(), user.bc.rules.hasClause("inverse"));
         if (effectiveness >= 2.0F) {
            ArrayList types = new ArrayList(target.type);
            if (types.size() == 1) {
               return Arrays.asList(EnumType.Normal);
            }

            types.remove(EnumType.Flying);
            return types;
         }
      }

      return target.type;
   }
}

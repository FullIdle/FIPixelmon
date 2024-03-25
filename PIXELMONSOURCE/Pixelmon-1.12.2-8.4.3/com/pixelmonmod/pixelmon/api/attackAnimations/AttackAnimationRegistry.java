package com.pixelmonmod.pixelmon.api.attackAnimations;

import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationGrowthSpurt;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationLeapForward;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationRun;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationStationary;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationVerticalStomp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AttackAnimationRegistry {
   public static HashMap animations = new HashMap();

   public static String getKeyForAnimation(AttackAnimation animation) {
      Iterator var1 = animations.entrySet().iterator();

      Map.Entry entry;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         entry = (Map.Entry)var1.next();
      } while(entry.getValue() != animation.getClass());

      return (String)entry.getKey();
   }

   static {
      animations.put("leapForward", AttackAnimationLeapForward.class);
      animations.put("verticalStomp", AttackAnimationVerticalStomp.class);
      animations.put("stationary", AttackAnimationStationary.class);
      animations.put("growthSpurt", AttackAnimationGrowthSpurt.class);
      animations.put("run", AttackAnimationRun.class);
   }
}

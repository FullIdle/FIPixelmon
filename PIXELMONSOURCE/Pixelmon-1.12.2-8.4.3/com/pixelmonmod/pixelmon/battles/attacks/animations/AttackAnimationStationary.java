package com.pixelmonmod.pixelmon.battles.attacks.animations;

import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.HashMap;

public class AttackAnimationStationary extends AttackAnimation {
   public int durationTicks = 0;
   public HashMap effects = new HashMap();
   public boolean groundedStartPosition = false;
   public boolean groundedEndPosition = false;
   private transient HashMap remainingEffects = null;

   public void initialize(PixelmonWrapper user, PixelmonWrapper target, Attack attack) {
      super.initialize(user, target, attack);
      this.remainingEffects = new HashMap(this.effects);
   }

   public boolean tickAnimation(int tick) {
      if (this.remainingEffects.containsKey(tick)) {
         AttackAnimationData effect = (AttackAnimationData)this.remainingEffects.remove(tick);
         this.sendBattleEffect(effect, this.groundedStartPosition, this.groundedEndPosition);
      }

      return this.remainingEffects.isEmpty() && tick >= this.durationTicks;
   }

   public boolean usedOncePerTurn() {
      return true;
   }
}

package com.pixelmonmod.pixelmon.client.models.smd;

import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SmdAnimationSequence {
   AnimationType animationType;
   List sequence;
   int current = -1;

   public SmdAnimationSequence(AnimationType animType, List animations, ValveStudioModel model) {
      this.animationType = animType;
      this.sequence = new ArrayList();
      animations.forEach((animation) -> {
         this.sequence.add(new SmdAnimation(animation, model));
      });
   }

   public SmdAnimation next() {
      ++this.current;
      if (this.current >= this.sequence.size()) {
         this.current = 0;
      }

      return this.sequence.isEmpty() ? null : (SmdAnimation)this.sequence.get(this.current);
   }

   public SmdAnimation current() {
      if (this.current < 0) {
         this.current = 0;
      }

      return this.sequence.isEmpty() ? null : (SmdAnimation)this.sequence.get(this.current);
   }

   public void precalculate(SmdModel body) {
      List precalculated = new ArrayList();
      Iterator var3 = this.sequence.iterator();

      while(var3.hasNext()) {
         SmdAnimation animation = (SmdAnimation)var3.next();
         if (!precalculated.contains(animation)) {
            animation.precalculateAnimation(body);
            precalculated.add(animation);
         }
      }

   }

   public SmdAnimation checkForIncrement(IncrementingVariable variable) {
      SmdAnimation current = this.current();
      if (this.sequence.size() > 1 && variable.value + variable.increment >= (float)current.totalFrames) {
         variable.value = 0.0F;
         return this.next();
      } else {
         return current;
      }
   }

   public SmdAnimation checkForFinalFrame(int frame) {
      return frame > this.current().totalFrames ? this.next() : this.current();
   }
}

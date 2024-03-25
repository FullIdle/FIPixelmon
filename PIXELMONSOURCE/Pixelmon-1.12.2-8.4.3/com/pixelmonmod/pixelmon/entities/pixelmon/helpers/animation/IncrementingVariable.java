package com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation;

import java.util.Objects;

public class IncrementingVariable {
   public float value = 0.0F;
   public float increment;
   public float limit;

   public IncrementingVariable(float increment, float limit) {
      this.increment = increment;
      this.limit = limit;
   }

   public void tick() {
      this.value += this.increment;
      if (this.value >= this.limit) {
         this.value = 0.0F;
      } else if (this.value < 0.0F) {
         this.value = this.limit;
      }

   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof IncrementingVariable)) {
         return false;
      } else {
         IncrementingVariable variable = (IncrementingVariable)o;
         return Math.abs(variable.increment - this.increment) <= 1.0F && Math.abs(variable.limit - this.limit) <= 1.0F;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.increment, this.limit});
   }
}

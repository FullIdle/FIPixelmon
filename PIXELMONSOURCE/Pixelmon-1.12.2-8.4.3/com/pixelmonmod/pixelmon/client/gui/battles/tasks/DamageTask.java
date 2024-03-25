package com.pixelmonmod.pixelmon.client.gui.battles.tasks;

import java.util.UUID;

public class DamageTask extends HPTask {
   public DamageTask(float healthDifference, UUID uuid) {
      super(healthDifference, uuid);
   }

   protected void boundsCheck() {
      if (this.currentHealth + this.healthDifference < 0.0F) {
         this.healthDifference = -this.currentHealth;
      }

   }

   public boolean isDone() {
      return this.canceled || this.currentHealth <= this.originalHealth + this.healthDifference;
   }
}

package com.pixelmonmod.pixelmon.client.gui.battles.tasks;

import java.util.UUID;

public class HealTask extends HPTask {
   public HealTask(float healthDifference, UUID uuid) {
      super(healthDifference, uuid);
   }

   protected void boundsCheck() {
      if (this.currentHealth + this.healthDifference > (float)this.pokemon.maxHealth) {
         this.healthDifference = (float)this.pokemon.maxHealth - this.currentHealth;
      }

   }

   public boolean isDone() {
      return this.canceled || this.currentHealth >= this.originalHealth + this.healthDifference;
   }
}

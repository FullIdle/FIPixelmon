package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.ai.EntityAIBase;

public class AIIsInBattle extends EntityAIBase {
   EntityPixelmon pixelmon;

   public AIIsInBattle(EntityPixelmon entity7HasAI) {
      this.func_75248_a(1);
      this.pixelmon = entity7HasAI;
   }

   public boolean func_75250_a() {
      return this.pixelmon.battleController != null;
   }

   public boolean func_75253_b() {
      return this.pixelmon.battleController != null;
   }
}

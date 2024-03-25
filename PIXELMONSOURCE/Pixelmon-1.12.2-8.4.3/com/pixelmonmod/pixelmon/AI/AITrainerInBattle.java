package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.ai.EntityAIBase;

public class AITrainerInBattle extends EntityAIBase {
   NPCTrainer trainer;

   public AITrainerInBattle(NPCTrainer trainer) {
      this.func_75248_a(1);
      this.trainer = trainer;
   }

   public boolean func_75250_a() {
      return this.trainer.battleController != null;
   }

   public boolean func_75253_b() {
      return this.trainer.battleController != null;
   }
}

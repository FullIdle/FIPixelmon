package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import net.minecraft.util.ResourceLocation;

public class Fly {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("fly")).setName("pixelmon.moveskill.fly.name").describe("pixelmon.moveskill.fly.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/fly.png")).setDefaultCooldownTicks(20).setUsePP(true).setAnyMoves("Fly");
      return moveSkill.setBehaviourNoTarget(Teleport.teleportAction(moveSkill, false));
   }
}

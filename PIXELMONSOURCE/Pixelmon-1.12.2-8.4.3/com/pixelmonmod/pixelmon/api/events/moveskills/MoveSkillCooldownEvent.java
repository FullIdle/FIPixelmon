package com.pixelmonmod.pixelmon.api.events.moveskills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class MoveSkillCooldownEvent extends Event {
   public final EntityPixelmon pixelmon;
   public final MoveSkill moveSkill;
   public int cooldownTicks;

   public MoveSkillCooldownEvent(EntityPixelmon pixelmon, MoveSkill moveSkill, int cooldownTicks) {
      this.pixelmon = pixelmon;
      this.moveSkill = moveSkill;
      this.cooldownTicks = cooldownTicks;
   }
}

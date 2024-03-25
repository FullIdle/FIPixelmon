package com.pixelmonmod.pixelmon.api.events.moveskills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.function.Consumer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class UseMoveSkillEvent extends Event {
   public final EntityPixelmon pixelmon;
   public final MoveSkill moveSkill;
   public Object data;
   public Consumer handler = null;

   public UseMoveSkillEvent(EntityPixelmon pixelmon, MoveSkill moveSkill, Object data) {
      this.pixelmon = pixelmon;
      this.moveSkill = moveSkill;
      this.data = data;
   }

   public void onCooldown(Consumer handler) {
      this.handler = handler;
   }
}

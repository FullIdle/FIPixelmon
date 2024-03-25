package com.pixelmonmod.pixelmon.api.events.battles;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BattleMessageEvent extends Event {
   public final Entity target;
   public final TextComponentTranslation textComponent;

   public BattleMessageEvent(Entity target, TextComponentTranslation textComponent) {
      this.target = target;
      this.textComponent = textComponent;
   }
}

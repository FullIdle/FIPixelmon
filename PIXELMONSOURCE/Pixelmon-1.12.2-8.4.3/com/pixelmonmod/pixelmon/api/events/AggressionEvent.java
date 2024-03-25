package com.pixelmonmod.pixelmon.api.events;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class AggressionEvent extends Event {
   public final EntityLiving aggressor;
   public final EntityPlayerMP player;

   public AggressionEvent(EntityLiving aggressor, EntityPlayerMP player) {
      this.aggressor = aggressor;
      this.player = player;
   }
}

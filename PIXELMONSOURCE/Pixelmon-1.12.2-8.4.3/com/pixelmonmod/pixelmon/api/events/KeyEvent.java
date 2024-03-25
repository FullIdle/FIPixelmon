package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.comm.packetHandlers.EnumKeyPacketMode;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class KeyEvent extends Event {
   public final EntityPlayerMP player;
   public final EnumKeyPacketMode key;

   public KeyEvent(EntityPlayerMP player, EnumKeyPacketMode key) {
      this.player = player;
      this.key = key;
   }
}

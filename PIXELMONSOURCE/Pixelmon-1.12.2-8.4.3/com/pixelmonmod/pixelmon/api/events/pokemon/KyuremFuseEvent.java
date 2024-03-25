package com.pixelmonmod.pixelmon.api.events.pokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/** @deprecated */
@Cancelable
@Deprecated
public class KyuremFuseEvent extends Event {
   /** @deprecated */
   @Deprecated
   public final EntityPlayerMP player;
   /** @deprecated */
   @Deprecated
   public final EntityPixelmon pokemon;
   /** @deprecated */
   @Deprecated
   public final Pokemon fusing;

   /** @deprecated */
   @Deprecated
   public KyuremFuseEvent(EntityPlayerMP player, EntityPixelmon pokemon, Pokemon fusing) {
      this.player = player;
      this.pokemon = pokemon;
      this.fusing = fusing;
   }
}

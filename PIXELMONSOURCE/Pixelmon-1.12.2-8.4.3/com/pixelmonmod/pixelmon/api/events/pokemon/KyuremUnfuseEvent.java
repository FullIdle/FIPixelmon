package com.pixelmonmod.pixelmon.api.events.pokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/** @deprecated */
@Cancelable
@Deprecated
public class KyuremUnfuseEvent extends Event {
   /** @deprecated */
   @Deprecated
   public final EntityPlayerMP player;
   /** @deprecated */
   @Deprecated
   public final EntityPixelmon pokemon;
   /** @deprecated */
   @Nullable
   @Deprecated
   public Pokemon fused;

   /** @deprecated */
   @Deprecated
   public KyuremUnfuseEvent(EntityPlayerMP player, EntityPixelmon pokemon, @Nullable Pokemon fused) {
      this.player = player;
      this.pokemon = pokemon;
      this.fused = fused;
   }
}

package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pokeballs.EntityEmptyPokeball;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PokeballImpactEvent extends Event {
   public final EntityThrowable pokeball;
   public final RayTraceResult ballPosition;
   public final boolean isEmptyBall;

   public PokeballImpactEvent(EntityThrowable pokeball, RayTraceResult movingobjectposition) {
      this.isEmptyBall = pokeball instanceof EntityEmptyPokeball;
      this.pokeball = pokeball;
      this.ballPosition = movingobjectposition;
   }

   @Nullable
   public Entity getEntityHit() {
      return this.ballPosition.field_72308_g;
   }
}

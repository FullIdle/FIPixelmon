package com.pixelmonmod.pixelmon.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import java.util.function.Consumer;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.World;

public class DataSync {
   private DataParameter dataParameter;
   private Consumer fieldSetter;

   public DataSync(Consumer fieldSetter) {
      this.fieldSetter = fieldSetter;
   }

   public int getParameterId() {
      return this.dataParameter != null ? this.dataParameter.func_187155_a() : 0;
   }

   public DataSync registerDataParameter(DataParameter dataParameter) {
      this.dataParameter = dataParameter;
      return this;
   }

   public void setField(Object value) {
      this.fieldSetter.accept(value);
   }

   public void set(PokemonBase pokemon, Object value) {
      if (this.dataParameter != null && pokemon instanceof Pokemon) {
         Entity1Base base = this.getEntity((Pokemon)pokemon);
         if (base != null) {
            base.func_184212_Q().func_187227_b(this.dataParameter, value);
         } else {
            this.fieldSetter.accept(value);
         }
      } else {
         this.fieldSetter.accept(value);
      }

   }

   private Entity1Base getEntity(Pokemon pokemon) {
      if (pokemon.getEntityID() != -1) {
         World world = pokemon.getWorld();
         if (world != null) {
            Entity entity = world.func_73045_a(pokemon.getEntityID());
            if (entity instanceof Entity1Base) {
               return (Entity1Base)entity;
            }
         }
      }

      return null;
   }
}

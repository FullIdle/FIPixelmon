package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.battles.status.Burn;
import com.pixelmonmod.pixelmon.battles.status.Freeze;
import com.pixelmonmod.pixelmon.battles.status.NoStatus;
import com.pixelmonmod.pixelmon.battles.status.Paralysis;
import com.pixelmonmod.pixelmon.battles.status.Poison;
import com.pixelmonmod.pixelmon.battles.status.PoisonBadly;
import com.pixelmonmod.pixelmon.battles.status.Sleep;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class StatusSpec extends SpecValue implements ISpecType {
   public StatusSpec(String value) {
      super("status", value);
   }

   public List getKeys() {
      return Collections.singletonList("status");
   }

   public SpecValue parse(@Nullable String arg) {
      return new StatusSpec(arg);
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return this.parse(nbt.func_74779_i("status"));
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74778_a("status", value.value.toString());
   }

   public Class getSpecClass() {
      return StatusSpec.class;
   }

   public String toParameterForm(SpecValue value) {
      return value.key + ":" + value.value.toString();
   }

   public Class getValueClass() {
      return String.class;
   }

   public static StatusPersist getStatus(String name) {
      switch (name.toLowerCase()) {
         case "sleep":
            return new Sleep();
         case "burn":
            return new Burn();
         case "paralyzed":
         case "paralysis":
            return new Paralysis();
         case "frozen":
         case "freeze":
            return new Freeze();
         case "poison":
            return new Poison();
         case "poisonbadly":
         case "poison badly":
            return new PoisonBadly();
         default:
            return NoStatus.noStatus;
      }
   }

   public void apply(EntityPixelmon pixelmon) {
      this.apply(pixelmon.getPokemonData());
   }

   public void apply(Pokemon pokemon) {
      pokemon.setStatus(getStatus((String)this.value));
   }

   public boolean matches(EntityPixelmon pixelmon) {
      return this.matches(pixelmon.getPokemonData());
   }

   public boolean matches(Pokemon pokemon) {
      return getStatus((String)this.value).type == pokemon.getStatus().type;
   }

   public SpecValue clone() {
      return new StatusSpec((String)this.value);
   }
}

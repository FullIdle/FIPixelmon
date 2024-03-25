package com.pixelmonmod.pixelmon.api.pokemon;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;

public class SpecFlag extends SpecValue implements ISpecType {
   public transient ArrayList aliases = new ArrayList();

   public SpecFlag(String... aliases) {
      super(aliases[0], true);

      for(int i = 0; i < aliases.length; ++i) {
         this.aliases.add(aliases[i]);
      }

   }

   public SpecFlag(String key, boolean value) {
      super(key, value);
   }

   public List getKeys() {
      return this.aliases;
   }

   public SpecValue make(boolean value) {
      return new SpecFlag(this.key, value);
   }

   public SpecValue parse(String arg) {
      return arg != null && arg.equalsIgnoreCase("false") ? this.make(false) : this.make(true);
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return this.make(nbt.func_74767_n(this.key));
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74757_a(this.key, Boolean.parseBoolean(value.value.toString()));
   }

   public Class getSpecClass() {
      return this.getClass();
   }

   public String toParameterForm(SpecValue value) {
      return this.key + ":" + value.value.toString();
   }

   public Class getValueClass() {
      return Boolean.class;
   }

   public void apply(EntityPixelmon pixelmon) {
      this.apply(pixelmon.getPokemonData());
   }

   public void apply(Pokemon pokemon) {
      if (!pokemon.hasSpecFlag(this.key) && (Boolean)this.value) {
         pokemon.addSpecFlag(this.key);
      } else if (!(Boolean)this.value) {
         pokemon.removeSpecFlag(this.key);
      }

   }

   public boolean matches(EntityPixelmon pixelmon) {
      return this.matches(pixelmon.getPokemonData());
   }

   public boolean matches(Pokemon pokemon) {
      return pokemon.hasSpecFlag(this.key) == (Boolean)this.value;
   }

   public SpecValue clone() {
      return this.parse(((Boolean)this.value).toString());
   }

   public boolean sentToClient() {
      return true;
   }
}

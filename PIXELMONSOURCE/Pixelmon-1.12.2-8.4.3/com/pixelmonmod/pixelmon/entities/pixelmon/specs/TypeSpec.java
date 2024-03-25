package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class TypeSpec extends SpecValue implements ISpecType {
   private String value;
   private List keys;

   public TypeSpec(List keys, String value) {
      super((String)keys.get(0), value);
      this.keys = keys;
      this.value = value;
   }

   public List getKeys() {
      return this.keys;
   }

   public TypeSpec parse(@Nullable String s) {
      return new TypeSpec(this.keys, s);
   }

   public SpecValue readFromNBT(NBTTagCompound nbtTagCompound) {
      return this.parse(nbtTagCompound.func_74779_i(this.key));
   }

   public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue specValue) {
      nbtTagCompound.func_74778_a(this.key, this.value);
   }

   public Class getSpecClass() {
      return this.getClass();
   }

   public String toParameterForm(SpecValue specValue) {
      return this.key + ":" + this.value;
   }

   public Class getValueClass() {
      return String.class;
   }

   public void apply(EntityPixelmon entityPixelmon) {
      this.apply(entityPixelmon.getPokemonData());
   }

   public void apply(Pokemon pokemon) {
      if (this.value != null) {
         pokemon.setSpecies(this.randomPokeType(this.value));
      }
   }

   public boolean matches(EntityPixelmon entityPixelmon) {
      return entityPixelmon.getPokemonData().getBaseStats().getTypeList().contains(EnumType.parseType(this.value));
   }

   public boolean matches(Pokemon pokemon) {
      return pokemon.getBaseStats().getTypeList().contains(EnumType.parseType(this.value));
   }

   public SpecValue clone() {
      return new TypeSpec(this.keys, this.value);
   }

   private EnumSpecies randomPokeType(String value) {
      EnumSpecies s = EnumSpecies.randomPoke();
      boolean isValid = false;

      while(!isValid) {
         if (!s.getBaseStats().getTypeList().contains(EnumType.parseType(value))) {
            s = EnumSpecies.randomPoke();
         } else {
            isValid = true;
         }
      }

      return s;
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class IsLegendarySpec extends SpecValue implements ISpecType {
   public IsLegendarySpec(boolean value) {
      super("islegendary", value);
   }

   public List getKeys() {
      return Lists.newArrayList(new String[]{"legendary", "islegendary"});
   }

   public SpecValue parse(@Nullable String arg) {
      try {
         return new IsLegendarySpec(Boolean.parseBoolean(arg));
      } catch (Exception var3) {
         return null;
      }
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return new IsLegendarySpec(nbt.func_74767_n(this.key));
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74757_a(this.key, (Boolean)value.value);
   }

   public Class getSpecClass() {
      return IsLegendarySpec.class;
   }

   public String toParameterForm(SpecValue value) {
      return "islegendary:" + value.value.toString();
   }

   public Class getValueClass() {
      return Boolean.class;
   }

   public void apply(EntityPixelmon pixelmon) {
      this.apply(pixelmon.getPokemonData());
   }

   public void apply(Pokemon pokemon) {
      if ((Boolean)this.value) {
         pokemon.setSpecies((EnumSpecies)CollectionHelper.getRandomElement(EnumSpecies.legendaries), true);
      } else {
         pokemon.setSpecies(EnumSpecies.randomPoke(false), true);
      }

   }

   public boolean matches(EntityPixelmon pixelmon) {
      return this.matches(pixelmon.getPokemonData());
   }

   public boolean matches(Pokemon pokemon) {
      return pokemon.isLegendary() == (Boolean)this.value;
   }

   public SpecValue clone() {
      return new IsLegendarySpec((Boolean)this.value);
   }
}

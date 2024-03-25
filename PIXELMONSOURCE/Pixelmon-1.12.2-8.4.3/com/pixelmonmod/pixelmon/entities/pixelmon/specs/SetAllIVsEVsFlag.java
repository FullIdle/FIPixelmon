package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class SetAllIVsEVsFlag extends SpecValue implements ISpecType {
   public List keys;
   public int amount = 0;
   public boolean isIVs;

   public SetAllIVsEVsFlag(List keys, boolean isIVs, int amount) {
      super((String)keys.get(0), (Object)null);
      this.keys = keys;
      this.isIVs = isIVs;
      this.amount = amount;
   }

   public List getKeys() {
      return this.keys;
   }

   public SpecValue parse(@Nullable String arg) {
      return new SetAllIVsEVsFlag(this.keys, this.isIVs, this.amount);
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return new SetAllIVsEVsFlag(this.keys, this.isIVs, this.amount);
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74757_a(this.key, true);
   }

   public Class getSpecClass() {
      return SetAllIVsEVsFlag.class;
   }

   public String toParameterForm(SpecValue value) {
      return this.key;
   }

   public Class getValueClass() {
      return Void.class;
   }

   public void apply(EntityPixelmon pixelmon) {
      this.apply(pixelmon.getPokemonData());
   }

   public void apply(Pokemon pokemon) {
      if (this.isIVs) {
         pokemon.getIVs().CopyIVs(new IVStore(new int[]{this.amount, this.amount, this.amount, this.amount, this.amount, this.amount}));
      } else {
         pokemon.getEVs().fillFromArray(new int[]{this.amount, this.amount, this.amount, this.amount, this.amount, this.amount});
      }

   }

   public boolean matches(EntityPixelmon pixelmon) {
      return this.matches(pixelmon.getPokemonData());
   }

   public boolean matches(Pokemon pokemon) {
      int[] arr = new int[]{this.amount, this.amount, this.amount, this.amount, this.amount, this.amount};
      return this.isIVs ? pokemon.getIVs().getArray() == arr : pokemon.getEVs().getArray() == arr;
   }

   public SpecValue clone() {
      return new SetAllIVsEVsFlag(this.keys, this.isIVs, this.amount);
   }
}

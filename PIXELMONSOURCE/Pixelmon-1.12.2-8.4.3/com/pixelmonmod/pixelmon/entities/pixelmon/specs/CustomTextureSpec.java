package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;

public class CustomTextureSpec extends SpecValue implements ISpecType {
   public String value;
   public List keys;

   public CustomTextureSpec(List keys, String value) {
      super((String)keys.get(0), value);
      this.keys = keys;
      this.value = value;
   }

   public List getKeys() {
      return this.keys;
   }

   public Class getSpecClass() {
      return CustomTextureSpec.class;
   }

   public Class getValueClass() {
      return String.class;
   }

   public CustomTextureSpec parse(String arg) {
      return new CustomTextureSpec(this.keys, arg);
   }

   public String toParameterForm(SpecValue specValue) {
      return this.key + ":" + this.value;
   }

   public SpecValue clone() {
      return new CustomTextureSpec(this.keys, this.value);
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return this.parse(nbt.func_74779_i(this.key));
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74778_a(this.key, (String)value.value);
   }

   public boolean matches(EntityPixelmon pixelmon) {
      return this.matches(pixelmon.getPokemonData());
   }

   public boolean matches(Pokemon pokemon) {
      if (this.value.equalsIgnoreCase("*")) {
         return !pokemon.getCustomTexture().isEmpty();
      } else {
         return pokemon.getCustomTexture().equalsIgnoreCase(this.value);
      }
   }

   public void apply(EntityPixelmon pixelmon) {
      pixelmon.getPokemonData().setCustomTexture(this.value);
   }

   public void apply(Pokemon pokemon) {
      pokemon.setCustomTexture(this.value);
   }
}

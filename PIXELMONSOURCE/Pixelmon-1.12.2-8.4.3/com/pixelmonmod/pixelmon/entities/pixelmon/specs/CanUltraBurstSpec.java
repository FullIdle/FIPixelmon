package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class CanUltraBurstSpec extends SpecValue implements ISpecType {
   public CanUltraBurstSpec(boolean value) {
      super("canultraburst", value);
   }

   public List getKeys() {
      return Lists.newArrayList(new String[]{"canultraburst", "canultra"});
   }

   public SpecValue parse(@Nullable String arg) {
      try {
         return new CanUltraBurstSpec(arg == null || Boolean.parseBoolean(arg));
      } catch (Exception var3) {
         return new CanUltraBurstSpec(true);
      }
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return new CanUltraBurstSpec(nbt.func_74767_n(this.key));
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74757_a(this.key, (Boolean)value.value);
   }

   public Class getSpecClass() {
      return CanUltraBurstSpec.class;
   }

   public String toParameterForm(SpecValue value) {
      return this.key + ":" + value.value.toString();
   }

   public Class getValueClass() {
      return Boolean.class;
   }

   public void apply(EntityPixelmon pixelmon) {
   }

   public void apply(Pokemon pokemon) {
   }

   public boolean matches(EntityPixelmon pixelmon) {
      return this.matches(pixelmon.getPokemonData());
   }

   public boolean matches(Pokemon pokemon) {
      if (pokemon.getSpecies() != EnumSpecies.Necrozma) {
         return false;
      } else {
         return (pokemon.getFormEnum() == EnumNecrozma.DUSK || pokemon.getFormEnum() == EnumNecrozma.DAWN) && pokemon.getHeldItem().func_77973_b() == PixelmonItemsHeld.ultranecrozium_z;
      }
   }

   public SpecValue clone() {
      return new CanUltraBurstSpec((Boolean)this.value);
   }
}

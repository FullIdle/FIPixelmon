package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumPrimal;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class CanPrimalEvoSpec extends SpecValue implements ISpecType {
   public CanPrimalEvoSpec(boolean value) {
      super("canprimalevo", value);
   }

   public List getKeys() {
      return Lists.newArrayList(new String[]{"canprimalevo", "canprimal", "canprimalrevert", "canprimalevolve"});
   }

   public SpecValue parse(@Nullable String arg) {
      try {
         return new CanPrimalEvoSpec(arg == null || Boolean.parseBoolean(arg));
      } catch (Exception var3) {
         return new CanPrimalEvoSpec(true);
      }
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return new CanPrimalEvoSpec(nbt.func_74767_n(this.key));
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74757_a(this.key, (Boolean)value.value);
   }

   public Class getSpecClass() {
      return CanPrimalEvoSpec.class;
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
      if (pokemon.getSpecies() == EnumSpecies.Groudon) {
         return pokemon.getFormEnum() == EnumPrimal.PRIMAL || pokemon.getHeldItemAsItemHeld() == PixelmonItemsHeld.redOrb;
      } else if (pokemon.getSpecies() != EnumSpecies.Kyogre) {
         return false;
      } else {
         return pokemon.getFormEnum() == EnumPrimal.PRIMAL || pokemon.getHeldItemAsItemHeld() == PixelmonItemsHeld.blueOrb;
      }
   }

   public SpecValue clone() {
      return new CanPrimalEvoSpec((Boolean)this.value);
   }
}

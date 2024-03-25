package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumHeroDuo;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class CanCrownedSpec extends SpecValue implements ISpecType {
   public CanCrownedSpec(boolean value) {
      super("cancrowned", value);
   }

   public List getKeys() {
      return Lists.newArrayList(new String[]{"cancrowned"});
   }

   public SpecValue parse(@Nullable String arg) {
      try {
         return new CanCrownedSpec(arg == null || Boolean.parseBoolean(arg));
      } catch (Exception var3) {
         return new CanCrownedSpec(true);
      }
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return new CanCrownedSpec(nbt.func_74767_n(this.key));
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74757_a(this.key, (Boolean)value.value);
   }

   public Class getSpecClass() {
      return CanCrownedSpec.class;
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
      return pokemon.getFormEnum() == EnumHeroDuo.HERO && (pokemon.getSpecies() == EnumSpecies.Zacian && pokemon.getHeldItemAsItemHeld() == PixelmonItemsHeld.rustedSword || pokemon.getSpecies() == EnumSpecies.Zamazenta && pokemon.getHeldItemAsItemHeld() == PixelmonItemsHeld.rustedShield);
   }

   public SpecValue clone() {
      return new CanCrownedSpec((Boolean)this.value);
   }
}

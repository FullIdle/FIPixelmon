package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumGreninja;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMegaStone;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class CanMegaEvoSpec extends SpecValue implements ISpecType {
   public CanMegaEvoSpec(boolean value) {
      super("canmegaevo", value);
   }

   public List getKeys() {
      return Lists.newArrayList(new String[]{"canmegaevo", "canmega", "canmegaevolve"});
   }

   public SpecValue parse(@Nullable String arg) {
      try {
         return new CanMegaEvoSpec(arg == null || Boolean.parseBoolean(arg));
      } catch (Exception var3) {
         return new CanMegaEvoSpec(true);
      }
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return new CanMegaEvoSpec(nbt.func_74767_n(this.key));
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74757_a(this.key, (Boolean)value.value);
   }

   public Class getSpecClass() {
      return CanMegaEvoSpec.class;
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
      if (pokemon.getSpecies() == EnumSpecies.Rayquaza) {
         return pokemon.getMoveset().hasAttack("Dragon Ascent");
      } else if (pokemon.getSpecies() != EnumSpecies.Greninja) {
         if (pokemon.getHeldItemAsItemHeld() == NoItem.noItem) {
            return false;
         } else if (pokemon.getHeldItemAsItemHeld().getHeldItemType() == EnumHeldItems.megaStone) {
            return ((ItemMegaStone)pokemon.getHeldItemAsItemHeld()).pokemon == pokemon.getSpecies();
         } else {
            return false;
         }
      } else {
         return pokemon.getFormEnum() == EnumGreninja.BATTLE_BOND || pokemon.getFormEnum() == EnumGreninja.ZOMBIE_BATTLE_BOND || pokemon.getFormEnum() == EnumGreninja.ALTER_BATTLE_BOND;
      }
   }

   public SpecValue clone() {
      return new CanMegaEvoSpec((Boolean)this.value);
   }
}

package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.DataSync;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public enum EnumMissingNo implements IEnumForm {
   MissingNo(0, "MissingNo"),
   Regidrago(80, "Regidrago"),
   Regieleki(81, "Regieleki");

   private final byte form;
   private final String name;

   private EnumMissingNo(int form, String name) {
      this.form = (byte)form;
      this.name = name;
   }

   public static void migrate(PokemonBase pokemon) {
      DataSync dsSpecies;
      if (pokemon.getForm() == Regidrago.form) {
         dsSpecies = (DataSync)ReflectionHelper.getPrivateValue(PokemonBase.class, pokemon, new String[]{"dsSpecies"});
         dsSpecies.setField(EnumSpecies.Regidrago.getNationalPokedexInteger());
      } else if (pokemon.getForm() == Regieleki.form) {
         dsSpecies = (DataSync)ReflectionHelper.getPrivateValue(PokemonBase.class, pokemon, new String[]{"dsSpecies"});
         dsSpecies.setField(EnumSpecies.Regieleki.getNationalPokedexInteger());
      }

   }

   public String getFormSuffix() {
      return this == MissingNo ? "" : "-" + this.name.toLowerCase();
   }

   public byte getForm() {
      return this.form;
   }

   public boolean isDefaultForm() {
      return this == MissingNo;
   }

   public String getUnlocalizedName() {
      return "pixelmon.missingno.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

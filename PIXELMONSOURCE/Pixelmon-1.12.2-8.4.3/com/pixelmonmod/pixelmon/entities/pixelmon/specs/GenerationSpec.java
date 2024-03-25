package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class GenerationSpec extends SpecValue implements ISpecType {
   public static HashMap generationLists = null;

   static void initializeGenerationList() {
      generationLists = new HashMap();
      EnumSpecies[] var0 = EnumSpecies.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumSpecies species = var0[var2];
         int generation = species.getGeneration();
         if (!generationLists.containsKey(generation)) {
            generationLists.put(generation, Lists.newArrayList(new EnumSpecies[]{species}));
         } else {
            ((ArrayList)generationLists.get(generation)).add(species);
         }
      }

   }

   public GenerationSpec(Integer value) {
      super("generation", value);
      if (generationLists == null) {
         initializeGenerationList();
      }

   }

   public List getKeys() {
      return Lists.newArrayList(new String[]{"gen", "generation"});
   }

   public SpecValue parse(@Nullable String arg) {
      if (arg == null) {
         return null;
      } else {
         try {
            return new GenerationSpec(Integer.parseInt(arg));
         } catch (NumberFormatException var3) {
            return null;
         }
      }
   }

   public SpecValue readFromNBT(NBTTagCompound nbt) {
      return this.parse(nbt.func_74762_e(this.key) + "");
   }

   public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
      nbt.func_74778_a(this.key, value.value.toString());
   }

   public Class getSpecClass() {
      return GenerationSpec.class;
   }

   public String toParameterForm(SpecValue value) {
      return "generation:" + value.value.toString();
   }

   public Class getValueClass() {
      return Integer.class;
   }

   public void apply(EntityPixelmon pixelmon) {
      this.apply(pixelmon.getPokemonData());
   }

   public void apply(Pokemon pokemon) {
      EnumSpecies randomSpecies = (EnumSpecies)CollectionHelper.getRandomElement((List)generationLists.getOrDefault(this.value, new ArrayList()));
      if (randomSpecies != null) {
         pokemon.setSpecies(randomSpecies, true);
      }

   }

   public boolean matches(EntityPixelmon pixelmon) {
      return this.matches(pixelmon.getPokemonData());
   }

   public boolean matches(Pokemon pokemon) {
      return pokemon.getSpecies().getGeneration() == (Integer)this.value;
   }

   public SpecValue clone() {
      return new GenerationSpec((Integer)this.value);
   }
}

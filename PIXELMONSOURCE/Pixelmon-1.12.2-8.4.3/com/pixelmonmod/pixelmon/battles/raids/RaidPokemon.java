package com.pixelmonmod.pixelmon.battles.raids;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RaidPokemon {
   private final String name;
   private final EnumSpecies species;
   private IEnumForm form = null;
   private final Item item;
   private final int ability;
   private final AttackBase[] attacks;

   public RaidPokemon(String name, EnumSpecies species, Item item, int ability, List attacks) {
      this.name = name;
      this.species = species;
      this.item = item;
      this.ability = ability;
      this.attacks = (AttackBase[])attacks.toArray(new AttackBase[0]);
   }

   public RaidPokemon(String name, EnumSpecies species, Item item, int ability, AttackBase... attacks) {
      this.name = name;
      this.species = species;
      this.item = item;
      this.ability = ability;
      this.attacks = attacks;
   }

   public RaidPokemon setForm(IEnumForm form) {
      this.form = form;
      return this;
   }

   public String getName() {
      return this.name;
   }

   public Pokemon makePokemon(int level) {
      Pokemon pokemon = Pixelmon.pokemonFactory.create(this.species);
      if (this.form != null) {
         pokemon.setForm(this.form);
      }

      pokemon.setLevel(level);
      if (this.item != null) {
         pokemon.setHeldItem(new ItemStack(this.item));
      }

      pokemon.setAbilitySlot(this.ability);
      pokemon.getMoveset().clear();
      AttackBase[] var3 = this.attacks;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         AttackBase attack = var3[var5];
         pokemon.getMoveset().add(new Attack(attack));
      }

      return pokemon;
   }
}

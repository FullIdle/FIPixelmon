package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.IRarityTweak;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.text.translation.I18n;

public class ItemLure extends PixelmonItem implements IRarityTweak {
   public LureType type;
   public LureStrength strength;

   public ItemLure(LureType type, LureStrength strength) {
      super("lure_" + type.name().toLowerCase() + "_" + strength.name().toLowerCase());
      this.type = type;
      this.strength = strength;
      this.func_77637_a(CreativeTabs.field_78040_i);
      this.func_77656_e(128);
      this.func_77625_d(1);
      this.setNoRepair();
   }

   public float getMultiplier(AbstractSpawner spawner, SpawnInfo spawnInfo, float sum, float rarity) {
      if (!(spawnInfo instanceof SpawnInfoPokemon)) {
         return 1.0F;
      } else {
         SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;
         if (spawnInfoPokemon.getPokemonSpec() != null && spawnInfoPokemon.getPokemonSpec().name != null) {
            EnumSpecies species = spawnInfoPokemon.getSpecies();
            int form = spawnInfoPokemon.getPokemonSpec().form == null ? -1 : spawnInfoPokemon.getPokemonSpec().form;
            if (this.type.type != null && species.getBaseStats(species.getFormEnum(form)).getTypeList().contains(this.type.type)) {
               return this.strength.multiplier;
            } else {
               return this.type != ItemLure.LureType.SHINY || (spawnInfoPokemon.spawnSpecificShinyRate == null || spawnInfoPokemon.spawnSpecificShinyRate != 1.0F) && (spawnInfo.set.setSpecificShinyRate == null || spawnInfo.set.setSpecificShinyRate != 1.0F) ? 1.0F : this.strength.multiplier;
            }
         } else {
            return 1.0F;
         }
      }
   }

   public String getTooltipText() {
      String translatedType = I18n.func_74838_a("type." + this.type.name().toLowerCase());
      return this.type != ItemLure.LureType.HA && this.type != ItemLure.LureType.SHINY ? I18n.func_74837_a("lure." + this.strength.name().toLowerCase() + ".normal.tooltip", new Object[]{translatedType}) : I18n.func_74837_a("lure." + this.strength.name().toLowerCase() + ".special.tooltip", new Object[]{translatedType});
   }

   public static enum LureStrength {
      WEAK(1.5F),
      STRONG(3.0F);

      public float multiplier;

      private LureStrength(float multiplier) {
         this.multiplier = multiplier;
      }
   }

   public static enum LureType {
      NORMAL(EnumType.Normal),
      FIRE(EnumType.Fire),
      WATER(EnumType.Water),
      ELECTRIC(EnumType.Electric),
      GRASS(EnumType.Grass),
      ICE(EnumType.Ice),
      FIGHTING(EnumType.Fighting),
      POISON(EnumType.Poison),
      GROUND(EnumType.Ground),
      FLYING(EnumType.Flying),
      PSYCHIC(EnumType.Psychic),
      BUG(EnumType.Bug),
      ROCK(EnumType.Rock),
      GHOST(EnumType.Ghost),
      DRAGON(EnumType.Dragon),
      DARK(EnumType.Dark),
      STEEL(EnumType.Steel),
      FAIRY(EnumType.Fairy),
      SHINY,
      HA;

      public EnumType type;

      private LureType() {
         this.type = null;
      }

      private LureType(EnumType type) {
         this.type = type;
      }
   }
}

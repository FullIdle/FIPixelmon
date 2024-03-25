package com.pixelmonmod.pixelmon.enums.items;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.models.ModelHolder;
import com.pixelmonmod.pixelmon.client.render.tileEntities.SharedModels;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.List;
import net.minecraft.util.ResourceLocation;

public enum EnumFossils {
   NULL(-1, 0, (EnumSpecies)null, ""),
   HELIX(0, 1, EnumSpecies.Omanyte, "helix_fossil"),
   DOME(1, 1, EnumSpecies.Kabuto, "dome_fossil"),
   OLD_AMBER(2, 1, EnumSpecies.Aerodactyl, "old_amber"),
   ROOT(3, 3, EnumSpecies.Lileep, "root_fossil"),
   CLAW(4, 3, EnumSpecies.Anorith, "claw_fossil"),
   SKULL(5, 4, EnumSpecies.Cranidos, "skull_fossil"),
   ARMOR(6, 4, EnumSpecies.Shieldon, "armor_fossil"),
   COVER(7, 5, EnumSpecies.Tirtouga, "cover_fossil"),
   PLUME(8, 5, EnumSpecies.Archen, "plume_fossil"),
   JAW(9, 6, EnumSpecies.Tyrunt, "jaw_fossil"),
   SAIL(10, 6, EnumSpecies.Amaura, "sail_fossil"),
   BIRD(11, 8, (EnumSpecies)null, "bird_fossil", "gen8_individual"),
   DINO(12, 8, (EnumSpecies)null, "dino_fossil", "gen8_individual"),
   DRAKE(13, 8, (EnumSpecies)null, "drake_fossil", "gen8_individual"),
   FISH(14, 8, (EnumSpecies)null, "fish_fossil", "gen8_individual"),
   DZ(-2, 8, EnumSpecies.Dracozolt, "", "gen8_combined"),
   AZ(-3, 8, EnumSpecies.Arctozolt, "", "gen8_combined"),
   DV(-4, 8, EnumSpecies.Dracovish, "", "gen8_combined"),
   AV(-5, 8, EnumSpecies.Arctovish, "", "gen8_combined");

   private static final List FOSSIL_SPECIES;
   private int index;
   private int generation;
   private EnumSpecies pokemon;
   private String itemName;
   private ResourceLocation texture;

   private EnumFossils(int index, int generation, EnumSpecies pokemon, String itemName) {
      this.index = index;
      this.generation = generation;
      this.pokemon = pokemon;
      this.itemName = itemName;
      this.texture = new ResourceLocation("pixelmon", "textures/fossils/" + this.name().toLowerCase() + "_fossilmodel.png");
   }

   private EnumFossils(int index, int generation, EnumSpecies pokemon, String itemName, String texLoc) {
      this.index = index;
      this.generation = generation;
      this.pokemon = pokemon;
      this.itemName = itemName;
      this.texture = new ResourceLocation("pixelmon", "textures/fossils/" + texLoc + "_fossilmodel.png");
   }

   public static EnumFossils fromIndex(int curFos) {
      EnumFossils[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFossils f = var1[var3];
         if (f.index == curFos) {
            return f;
         }
      }

      return NULL;
   }

   public int getIndex() {
      return this.index;
   }

   public int getGeneration() {
      return this.generation;
   }

   public EnumSpecies getPokemon() {
      return this.pokemon;
   }

   public String getItemName() {
      return this.itemName;
   }

   public ResourceLocation getTexture() {
      return this.texture;
   }

   public ModelHolder getModel() {
      switch (this) {
         case HELIX:
            return SharedModels.helixFossil;
         case DOME:
            return SharedModels.domeFossil;
         case OLD_AMBER:
            return SharedModels.oldAmber;
         case ROOT:
            return SharedModels.rootFossil;
         case CLAW:
            return SharedModels.clawFossil;
         case SKULL:
            return SharedModels.skullFossil;
         case ARMOR:
            return SharedModels.armorFossil;
         case COVER:
            return SharedModels.coverFossil;
         case PLUME:
            return SharedModels.plumeFossil;
         case JAW:
            return SharedModels.jawFossil;
         case SAIL:
            return SharedModels.sailFossil;
         case BIRD:
            return SharedModels.birdFossil;
         case DINO:
            return SharedModels.dinoFossil;
         case DRAKE:
            return SharedModels.drakeFossil;
         case FISH:
            return SharedModels.fishFossil;
         case DZ:
            return SharedModels.dzFossil;
         case AZ:
            return SharedModels.azFossil;
         case DV:
            return SharedModels.dvFossil;
         case AV:
            return SharedModels.avFossil;
         default:
            return SharedModels.helixFossil;
      }
   }

   public static List getFossilSpecies() {
      return FOSSIL_SPECIES;
   }

   static {
      List species = Lists.newArrayList();
      EnumFossils[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFossils value = var1[var3];
         if (value.pokemon != null) {
            species.add(value.pokemon);
         }
      }

      FOSSIL_SPECIES = species;
   }
}

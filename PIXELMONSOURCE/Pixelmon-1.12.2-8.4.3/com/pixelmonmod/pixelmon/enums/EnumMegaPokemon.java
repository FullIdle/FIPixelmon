package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum EnumMegaPokemon {
   Abomasnow(EnumSpecies.Abomasnow, 1),
   Absol(EnumSpecies.Absol, 1),
   Aerodactyl(EnumSpecies.Aerodactyl, 1),
   Aggron(EnumSpecies.Aggron, 1),
   Alakazam(EnumSpecies.Alakazam, 1),
   Ampharos(EnumSpecies.Ampharos, 1),
   Altaria(EnumSpecies.Altaria, 1),
   Audino(EnumSpecies.Audino, 1),
   Banette(EnumSpecies.Banette, 1),
   Beedrill(EnumSpecies.Beedrill, 1),
   Blastoise(EnumSpecies.Blastoise, 1),
   Blaziken(EnumSpecies.Blaziken, 1),
   Camerupt(EnumSpecies.Camerupt, 1),
   Charizard(EnumSpecies.Charizard, 2),
   Diancie(EnumSpecies.Diancie, 1),
   Gallade(EnumSpecies.Gallade, 1),
   Garchomp(EnumSpecies.Garchomp, 1),
   Gardevoir(EnumSpecies.Gardevoir, 1),
   Gengar(EnumSpecies.Gengar, 1),
   Glalie(EnumSpecies.Glalie, 1),
   Gyarados(EnumSpecies.Gyarados, 1),
   Heracross(EnumSpecies.Heracross, 1),
   Houndoom(EnumSpecies.Houndoom, 1),
   Kangaskhan(EnumSpecies.Kangaskhan, 1),
   Latias(EnumSpecies.Latias, 1),
   Latios(EnumSpecies.Latios, 1),
   Lopunny(EnumSpecies.Lopunny, 1),
   Lucario(EnumSpecies.Lucario, 1),
   Manectric(EnumSpecies.Manectric, 1),
   Mawile(EnumSpecies.Mawile, 1),
   Medicham(EnumSpecies.Medicham, 1),
   Metagross(EnumSpecies.Metagross, 1),
   Mewtwo(EnumSpecies.Mewtwo, 2),
   Pidgeot(EnumSpecies.Pidgeot, 1),
   Pinsir(EnumSpecies.Pinsir, 1),
   Rayquaza(EnumSpecies.Rayquaza, 1),
   Sableye(EnumSpecies.Sableye, 1),
   Salamence(EnumSpecies.Salamence, 1),
   Sceptile(EnumSpecies.Sceptile, 1),
   Scizor(EnumSpecies.Scizor, 1),
   Sharpedo(EnumSpecies.Sharpedo, 1),
   Steelix(EnumSpecies.Steelix, 1),
   Swampert(EnumSpecies.Swampert, 1),
   Slowbro(EnumSpecies.Slowbro, 1),
   Tyranitar(EnumSpecies.Tyranitar, 1),
   Venusaur(EnumSpecies.Venusaur, 1);

   public EnumSpecies pokemon;
   public int numMegaForms;

   private EnumMegaPokemon(EnumSpecies pokemon, int numMegaForms) {
      this.pokemon = pokemon;
      this.numMegaForms = numMegaForms;
   }

   public static EnumMegaPokemon getMega(EnumSpecies pokemon) {
      EnumMegaPokemon[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumMegaPokemon e = var1[var3];
         if (e.pokemon == pokemon) {
            return e;
         }
      }

      return null;
   }

   public static int getNumberOfMegaForms(EnumSpecies pokemon) {
      EnumMegaPokemon mega;
      return (mega = getMega(pokemon)) != null ? mega.numMegaForms : 0;
   }

   public Item[] getMegaEvoItems() {
      switch (this) {
         case Abomasnow:
            return new Item[]{PixelmonItemsHeld.abomasite};
         case Absol:
            return new Item[]{PixelmonItemsHeld.absolite};
         case Aerodactyl:
            return new Item[]{PixelmonItemsHeld.aerodactylite};
         case Aggron:
            return new Item[]{PixelmonItemsHeld.aggronite};
         case Alakazam:
            return new Item[]{PixelmonItemsHeld.alakazite};
         case Altaria:
            return new Item[]{PixelmonItemsHeld.altarianite};
         case Ampharos:
            return new Item[]{PixelmonItemsHeld.ampharosite};
         case Audino:
            return new Item[]{PixelmonItemsHeld.audinite};
         case Banette:
            return new Item[]{PixelmonItemsHeld.banettite};
         case Beedrill:
            return new Item[]{PixelmonItemsHeld.beedrillite};
         case Blastoise:
            return new Item[]{PixelmonItemsHeld.blastoisinite};
         case Blaziken:
            return new Item[]{PixelmonItemsHeld.blazikenite};
         case Charizard:
            return new Item[]{PixelmonItemsHeld.charizardite_x, PixelmonItemsHeld.charizardite_y};
         case Camerupt:
            return new Item[]{PixelmonItemsHeld.cameruptite};
         case Diancie:
            return new Item[]{PixelmonItemsHeld.diancite};
         case Gallade:
            return new Item[]{PixelmonItemsHeld.galladite};
         case Garchomp:
            return new Item[]{PixelmonItemsHeld.garchompite};
         case Gardevoir:
            return new Item[]{PixelmonItemsHeld.gardevoirite};
         case Gengar:
            return new Item[]{PixelmonItemsHeld.gengarite};
         case Glalie:
            return new Item[]{PixelmonItemsHeld.glalitite};
         case Heracross:
            return new Item[]{PixelmonItemsHeld.heracronite};
         case Houndoom:
            return new Item[]{PixelmonItemsHeld.houndoominite};
         case Gyarados:
            return new Item[]{PixelmonItemsHeld.gyaradosite};
         case Kangaskhan:
            return new Item[]{PixelmonItemsHeld.kangaskhanite};
         case Latias:
            return new Item[]{PixelmonItemsHeld.latiasite};
         case Latios:
            return new Item[]{PixelmonItemsHeld.latiosite};
         case Lopunny:
            return new Item[]{PixelmonItemsHeld.lopunnite};
         case Lucario:
            return new Item[]{PixelmonItemsHeld.lucarionite};
         case Manectric:
            return new Item[]{PixelmonItemsHeld.manectite};
         case Mawile:
            return new Item[]{PixelmonItemsHeld.mawilite};
         case Medicham:
            return new Item[]{PixelmonItemsHeld.medichamite};
         case Metagross:
            return new Item[]{PixelmonItemsHeld.metagrossite};
         case Mewtwo:
            return new Item[]{PixelmonItemsHeld.mewtwonite_x, PixelmonItemsHeld.mewtwonite_y};
         case Pidgeot:
            return new Item[]{PixelmonItemsHeld.pidgeotite};
         case Pinsir:
            return new Item[]{PixelmonItemsHeld.pinsirite};
         case Sableye:
            return new Item[]{PixelmonItemsHeld.sablenite};
         case Salamence:
            return new Item[]{PixelmonItemsHeld.salamencite};
         case Sceptile:
            return new Item[]{PixelmonItemsHeld.sceptilite};
         case Scizor:
            return new Item[]{PixelmonItemsHeld.scizorite};
         case Sharpedo:
            return new Item[]{PixelmonItemsHeld.sharpedonite};
         case Steelix:
            return new Item[]{PixelmonItemsHeld.steelixite};
         case Swampert:
            return new Item[]{PixelmonItemsHeld.swampertite};
         case Slowbro:
            return new Item[]{PixelmonItemsHeld.slowbronite};
         case Tyranitar:
            return new Item[]{PixelmonItemsHeld.tyranitarite};
         case Venusaur:
            return new Item[]{PixelmonItemsHeld.venusaurite};
         default:
            return new Item[]{ItemStack.field_190927_a.func_77973_b()};
      }
   }
}

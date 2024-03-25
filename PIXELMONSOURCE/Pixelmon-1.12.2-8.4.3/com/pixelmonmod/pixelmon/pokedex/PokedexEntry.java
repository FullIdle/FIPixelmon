package com.pixelmonmod.pixelmon.pokedex;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class PokedexEntry {
   public final String name;
   public final int natPokedexNum;
   public final String heightM;
   public final String weightM;
   public final String heightC;
   public final String weightC;
   private EntityPixelmon renderTarget;

   public PokedexEntry(int i, String n, float w, float h) {
      this.natPokedexNum = i;
      this.name = n;
      this.heightM = h + " " + I18n.func_74838_a("gui.pokedex.meters");
      this.weightM = w + " " + I18n.func_74838_a("gui.pokedex.kilograms");
      float hc = h * 3.28084F;
      if (hc == 0.0F) {
         this.heightC = "??? ft.";
      } else {
         String s = hc + "";
         int i1 = s.indexOf(46);
         String feet = s.substring(0, i1);
         float inches = Float.parseFloat(0 + s.substring(i1)) * 12.0F;
         int in = Math.round(inches);
         this.heightC = feet + "'" + in + "\"";
      }

      float wc = w * 2.2046225F;
      this.weightC = (wc == 0.0F ? "???" : Math.round(wc)) + " " + I18n.func_74838_a("gui.pokedex.pounds");
   }

   public PokedexEntry(int i, String n) {
      this.natPokedexNum = i;
      this.name = n;
      this.weightC = "???";
      this.weightM = "???";
      this.heightC = "???";
      this.heightM = "???";
   }

   public String getPokedexDisplayNumber() {
      String s;
      for(s = "" + this.natPokedexNum; s.length() < 3; s = "0" + s) {
      }

      return s;
   }

   public EntityPixelmon getRenderTarget(World w) {
      if (this.renderTarget != null && w == this.renderTarget.field_70170_p) {
         return this.renderTarget;
      } else if (this.name != null && !this.name.equals("???") && EnumSpecies.hasPokemon(this.name)) {
         this.renderTarget = new EntityPixelmon(w);
         this.renderTarget.setPokemon(Pixelmon.pokemonFactory.create(EnumSpecies.getFromNameAnyCaseNoTranslate(this.name)));
         this.renderTarget.getLvl().setLevel(50);
         this.renderTarget.getPokemonData().setGrowth(EnumGrowth.Ordinary);
         return this.renderTarget;
      } else {
         return null;
      }
   }

   public String toString() {
      return this.getPokedexDisplayNumber() + " " + Entity1Base.getLocalizedName(this.name);
   }
}

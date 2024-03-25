package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumBurmy;
import com.pixelmonmod.pixelmon.enums.forms.EnumCastform;
import com.pixelmonmod.pixelmon.enums.forms.EnumDeoxys;
import com.pixelmonmod.pixelmon.enums.forms.EnumGastrodon;
import com.pixelmonmod.pixelmon.enums.forms.EnumShellos;
import com.pixelmonmod.pixelmon.enums.forms.EnumUnown;
import com.pixelmonmod.pixelmon.enums.forms.EnumWormadam;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.util.IEncodeable;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;

public class PokemonForm implements IEncodeable {
   public EnumSpecies pokemon;
   public int form;
   public Gender gender;
   public boolean shiny;

   public PokemonForm(EnumSpecies pokemon) {
      this.pokemon = pokemon;
      this.form = -1;
      this.gender = Gender.Male;
      this.shiny = false;
   }

   public PokemonForm(EnumSpecies pokemon, int form) {
      this(pokemon);
      this.form = form;
      this.gender = Gender.Male;
      this.shiny = false;
   }

   public PokemonForm(EnumSpecies pokemon, int form, Gender gender) {
      this(pokemon, form);
      this.gender = gender;
      this.shiny = false;
   }

   public PokemonForm(EnumSpecies pokemon, int form, Gender gender, boolean shiny) {
      this(pokemon, form, gender);
      this.shiny = shiny;
   }

   public PokemonForm(NBTTagCompound nbt) {
      this((EnumSpecies)EnumSpecies.getFromName(nbt.func_74779_i("Name")).orElse(EnumSpecies.Bulbasaur), nbt.func_74762_e("Variant"), Gender.getGender(nbt.func_74765_d("Gender")), nbt.func_74767_n("Shiny"));
   }

   public PokemonForm(ByteBuf buffer) {
      this.decodeInto(buffer);
   }

   public byte getForm() {
      return (byte)this.form;
   }

   public IEnumForm getEnumForm() {
      return this.pokemon.getFormEnum(this.form);
   }

   public PokemonForm copy() {
      return new PokemonForm(this.pokemon, this.form, this.gender, this.shiny);
   }

   public String getLocalizedName() {
      String baseName = this.pokemon.getLocalizedName();
      if (this.form == -1) {
         return baseName;
      } else if (this.form > 0 && this.pokemon.hasMega()) {
         if (this.pokemon == EnumSpecies.Charizard) {
            baseName = baseName + " " + (this.form == 2 ? "Y" : "X");
         }

         return I18n.func_135052_a("pixelmon.mega.name", new Object[]{baseName});
      } else {
         String formName = "";
         switch (this.pokemon) {
            case Burmy:
               formName = EnumBurmy.getFromIndex(this.form).name();
               break;
            case Castform:
               formName = EnumCastform.getFromIndex(this.form).name();
               break;
            case Deoxys:
               formName = EnumDeoxys.getFromIndex(this.form).name();
               break;
            case Gastrodon:
               formName = EnumGastrodon.getFromIndex(this.form).name();
               break;
            case Shellos:
               formName = EnumShellos.getFromIndex(this.form).name();
               break;
            case Unown:
               formName = EnumUnown.getFromIndex(this.form).name();
               break;
            case Wormadam:
               formName = EnumWormadam.getFromIndex(this.form).name();
         }

         String formTranslate = I18n.func_135052_a("pixelmon.form." + formName.toLowerCase() + ".name", new Object[0]);
         if (formTranslate.startsWith("pixelmon.")) {
            formTranslate = formName;
         }

         return String.format("%s-%s", baseName, formTranslate);
      }
   }

   public void writeToNBT(NBTTagCompound compound) {
      compound.func_74778_a("Name", this.pokemon.name);
      compound.func_74774_a("Variant", this.getForm());
      compound.func_74774_a("Gender", this.gender.getForm());
      compound.func_74757_a("Shiny", this.shiny);
   }

   public void encodeInto(ByteBuf buffer) {
      buffer.writeInt(this.pokemon.getNationalPokedexInteger());
      buffer.writeInt(this.form);
      buffer.writeByte(this.gender.ordinal());
      buffer.writeBoolean(this.shiny);
   }

   public void decodeInto(ByteBuf buffer) {
      this.pokemon = EnumSpecies.getFromDex(buffer.readInt());
      this.form = buffer.readInt();
      this.gender = Gender.getGender((short)buffer.readByte());
      this.shiny = buffer.readBoolean();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      result = 31 * result + this.form;
      result = 31 * result + (this.pokemon == null ? 0 : this.pokemon.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         PokemonForm other = (PokemonForm)obj;
         if (this.form != other.form) {
            return false;
         } else if (this.pokemon != other.pokemon) {
            return false;
         } else if (this.gender != other.gender) {
            return false;
         } else {
            return this.shiny == other.shiny;
         }
      }
   }

   public String toString() {
      return this.pokemon.name + (this.form > 0 ? this.form : "");
   }

   public static Optional getFromName(String name) {
      switch (name) {
         case "Farfetch'd":
            return Optional.of(new PokemonForm(EnumSpecies.Farfetchd));
         case "Ho-Oh":
            return Optional.of(new PokemonForm(EnumSpecies.Hooh));
         case "Mime Jr.":
            return Optional.of(new PokemonForm(EnumSpecies.MimeJr));
         case "Mr. Mime":
            return Optional.of(new PokemonForm(EnumSpecies.MrMime));
         case "Nidoran♀":
            return Optional.of(new PokemonForm(EnumSpecies.Nidoranfemale));
         case "Nidoran♂":
            return Optional.of(new PokemonForm(EnumSpecies.Nidoranmale));
         case "Porygon-Z":
            return Optional.of(new PokemonForm(EnumSpecies.PorygonZ));
         default:
            Optional pokemon = EnumSpecies.getFromName(name);
            if (pokemon.isPresent()) {
               return Optional.of(new PokemonForm((EnumSpecies)pokemon.get()));
            } else {
               String megaPrefix = "Mega ";
               if (name.startsWith(megaPrefix)) {
                  String nameEnd = name.substring(megaPrefix.length());
                  if (nameEnd.endsWith(" X")) {
                     return Optional.of(new PokemonForm(EnumSpecies.getFromNameAnyCase(nameEnd.replace(" X", "")), 1));
                  }

                  if (nameEnd.endsWith(" Y")) {
                     return Optional.of(new PokemonForm(EnumSpecies.getFromNameAnyCase(nameEnd.replace(" Y", "")), 2));
                  }

                  pokemon = EnumSpecies.getFromName(nameEnd);
                  if (pokemon.isPresent()) {
                     EnumSpecies enumSpecies = (EnumSpecies)pokemon.get();
                     if (enumSpecies.hasMega()) {
                        return Optional.of(new PokemonForm(enumSpecies, 1));
                     }
                  }
               } else {
                  String[] hyphenSplit = name.split("-");
                  if (hyphenSplit.length == 2) {
                     pokemon = EnumSpecies.getFromName(hyphenSplit[0]);
                     String formName = hyphenSplit[1];
                     if (pokemon.isPresent()) {
                        EnumSpecies species = (EnumSpecies)pokemon.get();
                        int formIndex = -1;
                        Iterator var7 = species.getPossibleForms(true).iterator();

                        while(var7.hasNext()) {
                           IEnumForm form = (IEnumForm)var7.next();
                           if (form.getFormSuffix().equalsIgnoreCase(formName)) {
                              formIndex = form.getForm();
                           }
                        }

                        return Optional.of(new PokemonForm(species, formIndex));
                     }
                  }
               }

               return Optional.empty();
            }
      }
   }

   public static PokemonForm[] convertEnumArray(EnumSpecies... pokemon) {
      PokemonForm[] pokemonForms = new PokemonForm[pokemon.length];

      for(int i = 0; i < pokemon.length; ++i) {
         pokemonForms[i] = new PokemonForm(pokemon[i]);
      }

      return pokemonForms;
   }

   public PokemonBase toBase() {
      return new PokemonBase(this.pokemon, this.form, this.gender);
   }
}

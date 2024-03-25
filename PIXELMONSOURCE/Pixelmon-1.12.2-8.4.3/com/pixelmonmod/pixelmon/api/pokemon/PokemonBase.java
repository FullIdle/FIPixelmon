package com.pixelmonmod.pixelmon.api.pokemon;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumMissingNo;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.util.DataSync;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;

public class PokemonBase implements ITranslatable {
   protected EnumSpecies species;
   protected int form;
   protected transient IEnumForm formEnum;
   protected Gender gender;
   protected Map dataSyncMap;
   protected final transient DataSync dsSpecies;
   protected final transient DataSync dsForm;
   protected final transient DataSync dsGender;

   public PokemonBase() {
      this.species = EnumSpecies.MissingNo;
      this.form = -2;
      this.formEnum = EnumNoForm.NoForm;
      this.gender = Gender.None;
      this.dataSyncMap = Maps.newHashMap();
      this.dsSpecies = new DataSync((v) -> {
         this.species = EnumSpecies.getFromDex(v);
         this.formEnum = this.species.getFormEnum(this.form);
      });
      this.dsForm = new DataSync((v) -> {
         this.form = v;
         this.formEnum = this.species.getFormEnum(this.form);
      });
      this.dsGender = new DataSync((v) -> {
         this.gender = Gender.getGender((short)v);
      });
   }

   public PokemonBase(EnumSpecies pokemon) {
      this.species = EnumSpecies.MissingNo;
      this.form = -2;
      this.formEnum = EnumNoForm.NoForm;
      this.gender = Gender.None;
      this.dataSyncMap = Maps.newHashMap();
      this.dsSpecies = new DataSync((v) -> {
         this.species = EnumSpecies.getFromDex(v);
         this.formEnum = this.species.getFormEnum(this.form);
      });
      this.dsForm = new DataSync((v) -> {
         this.form = v;
         this.formEnum = this.species.getFormEnum(this.form);
      });
      this.dsGender = new DataSync((v) -> {
         this.gender = Gender.getGender((short)v);
      });
      this.species = pokemon;
      this.form = -1;
      this.formEnum = this.species.getFormEnum(this.form);
      this.gender = Gender.Male;
   }

   public PokemonBase(EnumSpecies species, int form, Gender gender) {
      this.species = EnumSpecies.MissingNo;
      this.form = -2;
      this.formEnum = EnumNoForm.NoForm;
      this.gender = Gender.None;
      this.dataSyncMap = Maps.newHashMap();
      this.dsSpecies = new DataSync((v) -> {
         this.species = EnumSpecies.getFromDex(v);
         this.formEnum = this.species.getFormEnum(this.form);
      });
      this.dsForm = new DataSync((v) -> {
         this.form = v;
         this.formEnum = this.species.getFormEnum(this.form);
      });
      this.dsGender = new DataSync((v) -> {
         this.gender = Gender.getGender((short)v);
      });
      this.species = species;
      this.form = form;
      this.formEnum = species.getFormEnum(this.form);
      this.gender = gender;
   }

   public EnumSpecies getSpecies() {
      return this.species;
   }

   public void setSpecies(EnumSpecies species) {
      this.setSpecies(species, true);
   }

   public void setSpecies(EnumSpecies species, boolean overwriteExistingData) {
      this.dsSpecies.set(this, species.getNationalPokedexInteger());
   }

   public int getForm() {
      return this.form;
   }

   public void setForm(int form) {
      this.dsForm.set(this, (byte)form);
      if (this.getFormEnum() instanceof Gender && this.getGender().getForm() != form) {
         this.setGender(Gender.getGender((short)((byte)form)));
      }

   }

   public IEnumForm getFormEnum() {
      return (IEnumForm)(EnumSpecies.genderForm.contains(this.getSpecies()) ? this.getGender() : this.formEnum);
   }

   public void setForm(IEnumForm form) {
      if (form instanceof Gender && this.getGender() != form) {
         if (!this.species.getPossibleForms(false).contains(form)) {
            return;
         }

         this.dsGender.set(this, form.getForm());
      }

      this.dsForm.set(this, form.getForm());
   }

   public Gender getGender() {
      return this.gender;
   }

   public void setGender(Gender gender) {
      BaseStats bs = this.getBaseStats();
      List possibleForms = this.species.getPossibleForms(false);
      if (possibleForms.contains(Gender.None) || !bs.isGenderless() && gender != Gender.None) {
         if (possibleForms.contains(Gender.Male) || gender != Gender.Male || !bs.isFemaleOnly()) {
            if (possibleForms.contains(Gender.Female) || gender != Gender.Female || !bs.isMaleOnly()) {
               this.dsGender.set(this, gender.getForm());
               if (this.getFormEnum() instanceof Gender) {
                  this.setForm(gender);
               }

            }
         }
      }
   }

   public BaseStats getBaseStats() {
      return this.species.getBaseStats(this.getFormEnum());
   }

   public boolean isPokemon(EnumSpecies... species) {
      EnumSpecies[] var2 = species;
      int var3 = species.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumSpecies specie = var2[var4];
         if (this.species == specie) {
            return true;
         }
      }

      return false;
   }

   public void readFromNBT(NBTTagCompound nbt) {
      if (nbt.func_74764_b("ndex")) {
         this.dsSpecies.set(this, nbt.func_74762_e("ndex"));
      } else if (nbt.func_74764_b("Name")) {
         this.dsSpecies.set(this, ((EnumSpecies)EnumSpecies.getFromName(nbt.func_74779_i("Name")).orElse(EnumSpecies.Bulbasaur)).getNationalPokedexInteger());
      }

      if (nbt.func_74764_b("Variant")) {
         this.dsForm.set(this, nbt.func_74771_c("Variant"));
      }

      if (nbt.func_74764_b("Gender")) {
         this.dsGender.set(this, nbt.func_74771_c("Gender"));
      }

      if (this.species == EnumSpecies.MissingNo) {
         EnumMissingNo.migrate(this);
      }

   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      nbt.func_74778_a("Name", this.species.name);
      nbt.func_74777_a("ndex", (short)this.species.getNationalPokedexInteger());
      int form = this instanceof Pokemon && this.getFormEnum().isTemporary() ? this.getFormEnum().getDefaultFromTemporary((Pokemon)this).getForm() : this.getForm();
      nbt.func_74774_a("Variant", (byte)form);
      nbt.func_74774_a("Gender", (byte)this.getGender().ordinal());
      return nbt;
   }

   public PokemonBase copyBase() {
      return new PokemonBase(this.species, this.form, this.gender);
   }

   public void registerDataParameters(Map map) {
      this.dataSyncMap.clear();
      this.dataSyncMap.put(((DataParameter)map.get("pokemon")).func_187155_a(), this.dsSpecies.registerDataParameter((DataParameter)map.get("pokemon")));
      this.dataSyncMap.put(((DataParameter)map.get("form")).func_187155_a(), this.dsForm.registerDataParameter((DataParameter)map.get("form")));
      this.dataSyncMap.put(((DataParameter)map.get("gender")).func_187155_a(), this.dsGender.registerDataParameter((DataParameter)map.get("gender")));
   }

   public boolean dataManagerChange(DataParameter key, Object value) {
      Iterator var3 = this.dataSyncMap.values().iterator();

      DataSync dataSync;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         dataSync = (DataSync)var3.next();
      } while(dataSync.getParameterId() != key.func_187155_a());

      dataSync.setField(value);
      return true;
   }

   public String getUnlocalizedName() {
      return "pixelmon." + this.getBaseStats().getPokemonName().toLowerCase() + ".name";
   }
}

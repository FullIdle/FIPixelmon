package com.pixelmonmod.pixelmon.api.pokemon;

import com.pixelmonmod.pixelmon.client.gui.pokemoneditor.FormData;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumGreninja;
import com.pixelmonmod.pixelmon.enums.forms.ICosmeticForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class ImportExportForm {
   private static ImportExportForm instance;
   private Map speciesMap = new HashMap();
   private Map nameMap = new HashMap();

   static ImportExportForm getInstance() {
      if (instance == null) {
         instance = new ImportExportForm();
      }

      return instance;
   }

   private ImportExportForm() {
      this.addFormData(EnumSpecies.Unown, (short)0, "");
      this.addFormData(EnumSpecies.Unown, (short)26, "?");
      this.addFormData(EnumSpecies.Unown, (short)27, "!");

      for(short i = 1; i < 26; ++i) {
         this.addFormData(EnumSpecies.Unown, i, Character.toString((char)(65 + i)));
      }

      String[] burmyForms = new String[]{"Sandy", "Trash"};
      this.addSpeciesFormData(EnumSpecies.Burmy, burmyForms);
      this.addSpeciesFormData(EnumSpecies.Wormadam, burmyForms);
      this.addSpeciesFormData(EnumSpecies.Castform, "Snowy", "Rainy", "Sunny");
      this.addSpeciesFormData(EnumSpecies.Deoxys, "attack", "Defense", "speed");
   }

   private void addSpeciesFormData(EnumSpecies species, String... formNames) {
      this.addFormData(species, (short)0, "");

      for(short i = 0; i < formNames.length; ++i) {
         String formName = formNames[i];
         this.addFormData(species, (short)(i + 1), formName);
      }

   }

   private void addFormData(EnumSpecies species, short formIndex, String formName) {
      Object formMap;
      if (this.speciesMap.containsKey(species)) {
         formMap = (Map)this.speciesMap.get(species);
      } else {
         formMap = new HashMap();
         this.speciesMap.put(species, formMap);
      }

      String fullFormName = species.name;
      if (!formName.isEmpty()) {
         fullFormName = fullFormName + "-" + formName;
      }

      ((Map)formMap).put(formIndex, fullFormName);
      this.nameMap.put(fullFormName, new FormData(species, formIndex));
   }

   String getFormName(EnumSpecies species, short formIndex) {
      IEnumForm form = species.getFormEnum(formIndex);
      if (form.isDefaultForm() || form.getFormSuffix().isEmpty() || form instanceof Gender || form instanceof ICosmeticForm && ((ICosmeticForm)form).isCosmetic()) {
         return species.name;
      } else if (form != EnumGreninja.BATTLE_BOND && form != EnumGreninja.ZOMBIE_BATTLE_BOND && form != EnumGreninja.ALTER_BATTLE_BOND) {
         String s = form.getFormSuffix();
         return species.name + "-" + s.substring(1, 2).toUpperCase() + s.substring(2);
      } else {
         return species.name;
      }
   }

   Optional getFormData(String formName) {
      if (formName.contains("-")) {
         String pokemon = formName.substring(0, formName.indexOf("-"));
         String form = formName.substring(formName.indexOf("-"));
         EnumSpecies species = EnumSpecies.getFromNameAnyCase(pokemon);
         if (species != null) {
            Iterator var5 = species.getPossibleForms(true).iterator();

            IEnumForm enumForm;
            do {
               if (!var5.hasNext()) {
                  if (this.nameMap.containsKey(formName)) {
                     return Optional.of(this.nameMap.get(formName));
                  }

                  return Optional.empty();
               }

               enumForm = (IEnumForm)var5.next();
            } while(!enumForm.getFormSuffix().equalsIgnoreCase(form));

            return Optional.of(new FormData(species, (short)enumForm.getForm()));
         } else {
            return Optional.empty();
         }
      } else {
         return Optional.empty();
      }
   }
}

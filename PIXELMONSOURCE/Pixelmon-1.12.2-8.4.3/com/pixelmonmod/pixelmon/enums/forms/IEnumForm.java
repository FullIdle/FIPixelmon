package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.util.Collections;
import java.util.Set;

public interface IEnumForm extends ITranslatable {
   default String getSpriteSuffix(boolean shiny) {
      return this.getFormSuffix(shiny);
   }

   /** @deprecated */
   @Deprecated
   default String getSpriteSuffix() {
      return this.getFormSuffix();
   }

   default String getFormSuffix(boolean shiny) {
      return this.getFormSuffix();
   }

   String getFormSuffix();

   byte getForm();

   default boolean isTemporary() {
      return false;
   }

   default IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return EnumNoForm.NoForm;
   }

   default IEnumForm getDefaultFromForm(IEnumForm form) {
      return this.getDefaultFromTemporary((Pokemon)null);
   }

   default boolean isDefaultForm() {
      return false;
   }

   default boolean isRegionalForm() {
      String suffix = this.getFormSuffix();
      return suffix.equalsIgnoreCase("-alola") || suffix.equalsIgnoreCase("-galar");
   }

   default Set getFormAttributes() {
      return Collections.emptySet();
   }

   String getName();
}

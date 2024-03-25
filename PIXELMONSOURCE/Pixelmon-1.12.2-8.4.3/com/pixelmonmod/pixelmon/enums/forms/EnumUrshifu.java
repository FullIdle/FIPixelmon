package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Set;

public enum EnumUrshifu implements IEnumForm {
   SingleStrike(0, "singlestrike"),
   RapidStrike(1, "rapidstrike"),
   GigantamaxSS(2, "gmaxss"),
   GigantamaxRS(3, "gmaxrs");

   private final byte form;
   private final String suffix;

   private EnumUrshifu(int form, String suffix) {
      this.form = (byte)form;
      this.suffix = suffix;
   }

   public String getFormSuffix() {
      return "-" + this.suffix;
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return false;
   }

   public boolean isTemporary() {
      return this.form >= 2;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return pokemon.getForm() == 2 ? SingleStrike : RapidStrike;
   }

   public IEnumForm getDefaultFromForm(IEnumForm form) {
      return form != GigantamaxSS && form != SingleStrike ? RapidStrike : SingleStrike;
   }

   public String getUnlocalizedName() {
      return "pixelmon." + (this.isTemporary() ? "generic" : "urshifu") + ".form." + this.name().toLowerCase();
   }

   public Set getFormAttributes() {
      switch (this) {
         case GigantamaxSS:
         case GigantamaxRS:
            return Sets.immutableEnumSet(FormAttributes.GIGANTAMAX, new FormAttributes[0]);
         default:
            return IEnumForm.super.getFormAttributes();
      }
   }

   public EnumUrshifu getGigantamax() {
      switch (this) {
         case RapidStrike:
            return GigantamaxRS;
         case SingleStrike:
            return GigantamaxSS;
         default:
            return this;
      }
   }

   public EnumUrshifu getRegular() {
      switch (this) {
         case GigantamaxSS:
            return SingleStrike;
         case GigantamaxRS:
            return RapidStrike;
         default:
            return this;
      }
   }

   public String getName() {
      return this.name();
   }
}

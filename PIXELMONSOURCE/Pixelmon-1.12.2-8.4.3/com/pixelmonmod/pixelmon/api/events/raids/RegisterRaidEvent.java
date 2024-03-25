package com.pixelmonmod.pixelmon.api.events.raids;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class RegisterRaidEvent extends Event {
   protected final HashMap starTiers;

   public RegisterRaidEvent(HashMap starTiers) {
      this.starTiers = starTiers;
   }

   @Cancelable
   public static class AddDefault extends RegisterRaidEvent {
      private int stars;
      private Tuple raid;

      public AddDefault(HashMap starTiers, int stars, Tuple raid) {
         super(starTiers);
         this.stars = stars;
         this.raid = raid;
      }

      public int getStars() {
         return this.stars;
      }

      public Tuple getRaid() {
         return this.raid;
      }

      public void setRaid(int stars, EnumSpecies species) {
         this.setRaid(stars, species, (IEnumForm)null);
      }

      public void setRaid(int stars, EnumSpecies species, IEnumForm form) {
         if (stars >= 1 && stars <= 5) {
            this.stars = stars;
            this.raid = new Tuple(species, Optional.ofNullable(form));
         } else {
            Pixelmon.LOGGER.warn("RegisterRaidEvent.AddDefault: cannot set raid star level below 1 or above 5!");
            this.setCanceled(true);
         }

      }
   }

   public static class Register extends RegisterRaidEvent {
      private boolean defaults = true;

      public Register(HashMap starTiers) {
         super(starTiers);
      }

      public void enableDefaults() {
         this.defaults = true;
      }

      public void disableDefaults() {
         this.defaults = false;
      }

      public boolean shouldUseDefaults() {
         return this.defaults;
      }

      public void clearRaids() {
         Iterator var1 = this.starTiers.values().iterator();

         while(var1.hasNext()) {
            ArrayList inner = (ArrayList)var1.next();
            inner.clear();
         }

      }

      public void addRaid(int stars, EnumSpecies species) {
         this.addRaid(stars, species, (IEnumForm)null);
      }

      public void addRaid(int stars, EnumSpecies species, IEnumForm form) {
         if (stars >= 1 && stars <= 5) {
            ((ArrayList)this.starTiers.get(stars)).add(new Tuple(species, Optional.ofNullable(form)));
         } else {
            Pixelmon.LOGGER.warn("RegisterRaidEvent.Register: cannot set raid star level below 1 or above 5!");
         }

      }
   }
}

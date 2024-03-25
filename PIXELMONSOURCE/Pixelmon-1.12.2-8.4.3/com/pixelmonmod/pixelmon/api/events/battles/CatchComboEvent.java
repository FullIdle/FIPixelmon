package com.pixelmonmod.pixelmon.api.events.battles;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class CatchComboEvent extends Event {
   private final EntityPlayerMP player;

   protected CatchComboEvent(EntityPlayerMP player) {
      this.player = player;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public static class ComboExperienceBonus extends CatchComboEvent {
      private float exp;

      public ComboExperienceBonus(EntityPlayerMP player, float exp) {
         super(player);
         this.exp = exp;
      }

      public float getExperienceModifier() {
         return this.exp;
      }

      public void setExperienceModifier(float exp) {
         this.exp = exp;
      }
   }

   public static class ComboIncrement extends CatchComboEvent {
      private final EnumSpecies species;
      private final int combo;

      public ComboIncrement(EntityPlayerMP player, EnumSpecies species, int combo) {
         super(player);
         this.species = species;
         this.combo = combo;
      }

      public EnumSpecies getComboSpecies() {
         return this.species;
      }

      public int getCombo() {
         return this.combo;
      }
   }
}

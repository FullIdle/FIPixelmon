package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.Map;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MovesetSyncEvent extends Event {
   private final EnumSpecies enumSpecies;
   private final byte form;
   private final String texture;
   private Map moveset;

   public MovesetSyncEvent(EnumSpecies enumSpecies, byte form, String texture, Map baseMoveset) {
      this.enumSpecies = enumSpecies;
      this.form = form;
      this.texture = texture;
      this.moveset = baseMoveset;
   }

   public EnumSpecies getEnumSpecies() {
      return this.enumSpecies;
   }

   public byte getForm() {
      return this.form;
   }

   public String getTexture() {
      return this.texture;
   }

   public Map getMoveset() {
      return this.moveset;
   }

   public void setMoveset(Map moveset) {
      this.moveset = moveset;
   }
}

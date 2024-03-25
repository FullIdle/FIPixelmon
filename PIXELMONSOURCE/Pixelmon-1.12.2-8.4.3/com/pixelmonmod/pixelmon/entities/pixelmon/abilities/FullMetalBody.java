package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

public class FullMetalBody extends PreventStatDrop {
   public FullMetalBody() {
      super("pixelmon.abilities.fullmetalbody");
   }

   public boolean canBeIgnored() {
      return false;
   }
}

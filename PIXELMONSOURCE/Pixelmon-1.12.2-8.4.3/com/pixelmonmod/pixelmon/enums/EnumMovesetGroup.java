package com.pixelmonmod.pixelmon.enums;

public enum EnumMovesetGroup {
   LevelUp(-1),
   Egg(0),
   Kanto(1),
   Johto(2),
   Hoenn(3),
   Sinnoh(4),
   Unova(5),
   Kalos(6),
   Alola(7),
   Galar(8);

   private final int id;

   private EnumMovesetGroup(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }
}

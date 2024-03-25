package com.pixelmonmod.pixelmon.comm.packetHandlers;

public enum EnumKeyPacketMode {
   SendPokemon,
   ActionKeyEntity,
   ActionKeyBlock,
   ExternalMoveEntity,
   ExternalMoveBlock,
   ExternalMove;

   public static EnumKeyPacketMode getFromOrdinal(int ordinal) {
      return values()[ordinal];
   }

   public boolean isEntity() {
      return this == ActionKeyEntity || this == ExternalMoveEntity;
   }

   public boolean isAction() {
      return this == ActionKeyBlock || this == ActionKeyEntity;
   }
}

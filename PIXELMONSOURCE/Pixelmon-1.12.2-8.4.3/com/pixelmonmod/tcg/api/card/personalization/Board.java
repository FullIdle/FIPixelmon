package com.pixelmonmod.tcg.api.card.personalization;

import net.minecraft.util.ResourceLocation;

public class Board {
   private final String name;
   private final String ownerName;
   private final ResourceLocation file;

   public Board(String name, String ownerName, ResourceLocation file) {
      this.name = name;
      this.ownerName = ownerName;
      this.file = file;
   }

   public String getName() {
      return this.name;
   }

   public String getOwnerName() {
      return this.ownerName;
   }

   public ResourceLocation getFile() {
      return this.file;
   }
}

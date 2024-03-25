package com.pixelmonmod.tcg.api.card;

import java.awt.Color;

public enum CardRarity {
   COMMON(1, "common", "common", 16382457),
   UNCOMMON(2, "uncommon", "uncommon", 2666259),
   RARE(3, "rare", "rare", 1868269),
   HOLORARE(4, "holo_rare", "holoRare", 9575118),
   ULTRARARE(5, "ultra_rare", "ultraRare", 16346112),
   SECRETRARE(6, "secret_rare", "secretRare", Color.CYAN.getRGB());

   private final int id;
   private final String unlocalizedName;
   private final String fileName;
   private final int color;

   private CardRarity(int id, String unlocalizedName, String fileName, int color) {
      this.id = id;
      this.unlocalizedName = unlocalizedName;
      this.fileName = fileName;
      this.color = color;
   }

   public int getId() {
      return this.id;
   }

   public String getUnlocalizedName() {
      return this.unlocalizedName;
   }

   public String getFileName() {
      return this.fileName;
   }

   public int getColor() {
      return this.color;
   }
}

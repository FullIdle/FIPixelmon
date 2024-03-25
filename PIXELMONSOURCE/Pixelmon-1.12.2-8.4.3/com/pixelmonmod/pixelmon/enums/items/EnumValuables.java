package com.pixelmonmod.pixelmon.enums.items;

public enum EnumValuables {
   Nugget,
   Big_Nugget,
   Balm_Mushroom,
   Big_Mushroom,
   Big_Pearl,
   Comet_Shard,
   Pearl,
   Pearl_String,
   Pretty_Wing,
   Pretty_Feather,
   Rare_Bone,
   Relic_Band,
   Relic_Copper,
   Relic_Crown,
   Relic_Gold,
   Relic_Silver,
   Relic_Statue,
   Relic_Vase,
   Shoal_Salt,
   Shoal_Shell,
   Slowpoke_Tail,
   Star_Piece,
   Stardust,
   Strange_Souvenir,
   Tiny_Mushroom,
   Heart_Scale,
   Red_Shard2,
   Blue_Shard2,
   Green_Shard,
   Yellow_Shard,
   White_Shard,
   Black_Shard;

   public String getFileName() {
      return this.toString().toLowerCase();
   }
}

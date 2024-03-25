package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.enums.items.EnumValuables;
import com.pixelmonmod.pixelmon.items.ItemValuable;
import net.minecraft.item.Item;

public class PixelmonItemsValuables {
   public static Item nugget;
   public static Item bigNugget;
   public static Item balmMushroom;
   public static Item bigMushroom;
   public static Item bigPearl;
   public static Item cometShard;
   public static Item pearl;
   public static Item pearlString;
   public static Item prettyWing;
   public static Item prettyFeather;
   public static Item rareBone;
   public static Item relicCopper;
   public static Item relicGold;
   public static Item relicSilver;
   public static Item shoalSalt;
   public static Item shoalShell;
   public static Item slowpokeTail;
   public static Item starPiece;
   public static Item stardust;
   public static Item strangeSouvenir;
   public static Item tinyMushroom;
   public static Item heartScale;
   public static Item redShard2;
   public static Item blueShard2;
   public static Item greenShard;
   public static Item yellowShard;
   public static Item whiteShard;
   public static Item blackShard;

   public static void load() {
      nugget = new ItemValuable(EnumValuables.Nugget);
      bigNugget = new ItemValuable(EnumValuables.Big_Nugget);
      balmMushroom = new ItemValuable(EnumValuables.Balm_Mushroom);
      bigMushroom = new ItemValuable(EnumValuables.Big_Mushroom);
      bigPearl = new ItemValuable(EnumValuables.Big_Pearl);
      cometShard = new ItemValuable(EnumValuables.Comet_Shard);
      pearl = new ItemValuable(EnumValuables.Pearl);
      pearlString = new ItemValuable(EnumValuables.Pearl_String);
      prettyWing = new ItemValuable(EnumValuables.Pretty_Wing);
      prettyFeather = new ItemValuable(EnumValuables.Pretty_Feather);
      rareBone = new ItemValuable(EnumValuables.Rare_Bone);
      relicCopper = new ItemValuable(EnumValuables.Relic_Copper);
      relicGold = new ItemValuable(EnumValuables.Relic_Gold);
      relicSilver = new ItemValuable(EnumValuables.Relic_Silver);
      shoalSalt = new ItemValuable(EnumValuables.Shoal_Salt);
      shoalShell = new ItemValuable(EnumValuables.Shoal_Shell);
      slowpokeTail = new ItemValuable(EnumValuables.Slowpoke_Tail);
      starPiece = new ItemValuable(EnumValuables.Star_Piece);
      stardust = new ItemValuable(EnumValuables.Stardust);
      strangeSouvenir = new ItemValuable(EnumValuables.Strange_Souvenir);
      tinyMushroom = new ItemValuable(EnumValuables.Tiny_Mushroom);
      heartScale = new ItemValuable(EnumValuables.Heart_Scale);
      redShard2 = new ItemValuable(EnumValuables.Red_Shard2);
      blueShard2 = new ItemValuable(EnumValuables.Blue_Shard2);
      greenShard = new ItemValuable(EnumValuables.Green_Shard);
      yellowShard = new ItemValuable(EnumValuables.Yellow_Shard);
      whiteShard = new ItemValuable(EnumValuables.White_Shard);
      blackShard = new ItemValuable(EnumValuables.Black_Shard);
   }
}

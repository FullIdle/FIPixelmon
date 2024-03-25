package com.pixelmonmod.pixelmon.enums.items;

import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.Locale;

public enum EnumZCrystals {
   Buginium_Z(EnumType.Bug),
   Darkinium_Z(EnumType.Dark),
   Dragonium_Z(EnumType.Dragon),
   Electrium_Z(EnumType.Electric),
   Fairium_Z(EnumType.Fairy),
   Fightinium_Z(EnumType.Fighting),
   Firium_Z(EnumType.Fire),
   Flyinium_Z(EnumType.Flying),
   Ghostium_Z(EnumType.Ghost),
   Grassium_Z(EnumType.Grass),
   Groundium_Z(EnumType.Ground),
   Icium_Z(EnumType.Ice),
   Normalium_Z(EnumType.Normal),
   Poisonium_Z(EnumType.Poison),
   Psychium_Z(EnumType.Psychic),
   Rockium_Z(EnumType.Rock),
   Steelium_Z(EnumType.Steel),
   Waterium_Z(EnumType.Water),
   Aloraichium_Z,
   Decidium_Z,
   Eevium_Z,
   Incinium_Z,
   Kommonium_Z,
   Lunalium_Z,
   Lycanium_Z,
   Marshadium_Z,
   Mewnium_Z,
   Mimikium_Z,
   Pikanium_Z,
   Pikashunium_Z,
   Primarium_Z,
   Snorlium_Z,
   Solganium_Z,
   Tapunium_Z,
   Ultranecrozium_Z;

   public final EnumType affiliatedType;

   private EnumZCrystals() {
      this.affiliatedType = null;
   }

   private EnumZCrystals(EnumType affiliatedType) {
      this.affiliatedType = affiliatedType;
   }

   public String getFileName() {
      return this.toString().toLowerCase(Locale.US);
   }
}

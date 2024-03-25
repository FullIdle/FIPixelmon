package com.pixelmonmod.pixelmon.comm;

import java.util.Arrays;

public enum EnumUpdateType {
   HP,
   Nickname,
   Name,
   Form,
   Experience,
   Stats,
   IVs,
   EVs,
   Friendship,
   Moveset,
   Status,
   Ball,
   CanLevel,
   HeldItem,
   Texture,
   Egg,
   Target,
   Ability,
   Clones,
   Enchants,
   Smelts,
   Pokerus,
   InRanch,
   OriginalTrainer,
   Appearance,
   MoveSkills,
   SpecFlags,
   Wool_Growth,
   Minior_Core,
   Ribbons;

   public static final EnumUpdateType[] ALL = (EnumUpdateType[])Arrays.stream(values()).filter((type) -> {
      return type != Appearance;
   }).toArray((x$0) -> {
      return new EnumUpdateType[x$0];
   });
   public static EnumUpdateType[] CLIENT = new EnumUpdateType[]{HP, Nickname, Name, Form, Stats, EVs, Friendship, Status, Ball, Moveset, CanLevel, HeldItem, Egg, Texture, Ability, Pokerus, InRanch, OriginalTrainer, Clones, Enchants, MoveSkills, SpecFlags, Wool_Growth, Minior_Core, Ribbons};

   public static EnumUpdateType getType(int index) {
      EnumUpdateType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumUpdateType type = var1[var3];
         if (type.ordinal() == index) {
            return type;
         }
      }

      return null;
   }
}

package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;

public enum EnumEggGroup {
   Monster(0),
   Bug(1),
   Flying(2),
   Field(3),
   Fairy(4),
   Grass(5),
   Humanlike(6),
   Mineral(7),
   Amorphous(8),
   Ditto(9),
   Dragon(10),
   Water1(11),
   Water2(12),
   Water3(13),
   Undiscovered(14);

   public int index;
   private static final EnumEggGroup[] VALUES = values();

   private EnumEggGroup(int index) {
      this.index = index;
   }

   public static EnumEggGroup getEggGroupFromIndex(int index) {
      EnumEggGroup[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumEggGroup n = var1[var3];
         if (n.index == index) {
            return n;
         }
      }

      return null;
   }

   public static Integer getIndexFromEggGroup(EnumEggGroup group) {
      return group.index;
   }

   public static Integer getIndexFromEggGroupName(String groupName) {
      return hasEggGroup(groupName) ? getEggGroupFromString(groupName).index : -1;
   }

   public Integer getIndex() {
      return this.index;
   }

   public static EnumEggGroup getRandomEggGroup() {
      int rndm = RandomHelper.getRandomNumberBetween(0, 14);
      return getEggGroupFromIndex(rndm);
   }

   public static boolean hasEggGroup(String group) {
      EnumEggGroup[] var1 = VALUES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumEggGroup n = var1[var3];
         if (n.name().equalsIgnoreCase(group)) {
            return true;
         }
      }

      return false;
   }

   public static EnumEggGroup getEggGroupFromString(String group) {
      EnumEggGroup[] var1 = VALUES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumEggGroup n = var1[var3];
         if (n.name().equalsIgnoreCase(group)) {
            return n;
         }
      }

      return null;
   }

   public static EnumEggGroup[] getEggGroups(PokemonBase pokemon) {
      return pokemon.getBaseStats().getEggGroupsArray();
   }
}

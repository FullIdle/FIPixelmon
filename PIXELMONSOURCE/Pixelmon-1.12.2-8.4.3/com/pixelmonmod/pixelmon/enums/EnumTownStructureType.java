package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.RandomHelper;
import java.lang.reflect.Field;

public enum EnumTownStructureType {
   houseBasic(0, EnumTownType.basicTown, 2, false, "house"),
   pokecenterBasic(1, EnumTownType.basicTown, 5, false, "pokecenter"),
   pokemartBasic(2, EnumTownType.basicTown, 5, false, "pokemart"),
   tradingCenterBasic(3, EnumTownType.basicTown, 5, false, "tradingCenter"),
   fountainSnow(4, EnumTownType.snowTown, 1, true, "fountain"),
   snowmanSnow(5, EnumTownType.snowTown, 10, true, "snowman"),
   houseSnow(6, EnumTownType.snowTown, 2, false, "house"),
   pokecenterSnow(7, EnumTownType.snowTown, 5, false, "pokecenter"),
   pokemartSnow(8, EnumTownType.snowTown, 5, false, "pokemart"),
   tradingCenterSnow(9, EnumTownType.snowTown, 5, false, "tradingCenter"),
   healingCenterSnow(10, EnumTownType.snowTown, 7, false, "healingcenter");

   int structureId;
   EnumTownType townToGenIn;
   int rarity;
   boolean centerPiece;
   String schematicName;

   private EnumTownStructureType(int structureId, EnumTownType townToGenIn, int rarity, boolean centerPiece, String schematicName) {
      this.structureId = structureId;
      this.townToGenIn = townToGenIn;
      this.rarity = rarity;
      this.centerPiece = centerPiece;
      this.schematicName = schematicName;
   }

   public int getStructureId() {
      return this.structureId;
   }

   public EnumTownType getTownToGenIn() {
      return this.townToGenIn;
   }

   public int getRarity() {
      return RandomHelper.rand.nextInt(this.rarity);
   }

   public boolean isCenterPiece() {
      return this.centerPiece;
   }

   public String getschematicName() {
      return this.getTownToGenIn().folderPath + this.schematicName + ".schematic";
   }

   public static EnumTownStructureType getHouseFromTown(EnumTownType town) {
      try {
         Field[] var1 = EnumTownStructureType.class.getFields();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Field field = var1[var3];
            EnumTownStructureType structure = (EnumTownStructureType)field.get((Object)null);
            if (structure.townToGenIn == town) {
               return structure;
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return null;
   }

   public static EnumTownStructureType getCenterPieceForTown(EnumTownType town) {
      try {
         Field[] var1 = EnumTownStructureType.class.getFields();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Field field = var1[var3];
            EnumTownStructureType structure = (EnumTownStructureType)field.get((Object)null);
            if (structure.townToGenIn == town && structure.isCenterPiece()) {
               return structure;
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return null;
   }

   public static boolean hasTownStructureType(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}

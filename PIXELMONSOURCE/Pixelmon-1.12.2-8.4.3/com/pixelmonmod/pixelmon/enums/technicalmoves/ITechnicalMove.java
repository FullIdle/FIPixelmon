package com.pixelmonmod.pixelmon.enums.technicalmoves;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ITechnicalMove {
   String prefix();

   int getId();

   String getAttackName();

   default AttackBase getAttack() {
      return (AttackBase)AttackBase.getAttackBaseFromEnglishName(this.getAttackName()).orElse((Object)null);
   }

   default int getGeneration() {
      if (this instanceof Gen1TechnicalMachines) {
         return 1;
      } else if (this instanceof Gen2TechnicalMachines) {
         return 2;
      } else if (this instanceof Gen3TechnicalMachines) {
         return 3;
      } else if (this instanceof Gen4TechnicalMachines) {
         return 4;
      } else if (this instanceof Gen5TechnicalMachines) {
         return 5;
      } else if (this instanceof Gen6TechnicalMachines) {
         return 6;
      } else if (this instanceof Gen7TechnicalMachines) {
         return 7;
      } else {
         return !(this instanceof Gen8TechnicalMachines) && !(this instanceof Gen8TechnicalRecords) ? -1 : 8;
      }
   }

   static ITechnicalMove getMoveFor(String directory, int id) {
      switch (directory) {
         case "tr8":
         case "tr_gen8":
            return Gen8TechnicalRecords.getTr(id);
         case "tm8":
         case "tm_gen8":
            return Gen8TechnicalMachines.getTm(id);
         case "tm_gen7":
            return Gen7TechnicalMachines.getTm(id);
         case "tm_gen6":
            return Gen6TechnicalMachines.getTm(id);
         case "tm_gen5":
            return Gen5TechnicalMachines.getTm(id);
         case "tm_gen4":
            return Gen4TechnicalMachines.getTm(id);
         case "tm_gen3":
            return Gen3TechnicalMachines.getTm(id);
         case "tm_gen2":
            return Gen2TechnicalMachines.getTm(id);
         case "tm_gen1":
            return Gen1TechnicalMachines.getTm(id);
         default:
            return null;
      }
   }

   static ITechnicalMove getMoveFor(String directory, String move) {
      switch (directory) {
         case "tr8":
         case "tr_gen8":
            return Gen8TechnicalRecords.getTr(move);
         case "tm8":
         case "tm_gen8":
            return Gen8TechnicalMachines.getTm(move);
         case "tm_gen7":
            return Gen7TechnicalMachines.getTm(move);
         case "tm_gen6":
            return Gen6TechnicalMachines.getTm(move);
         case "tm_gen5":
            return Gen5TechnicalMachines.getTm(move);
         case "tm_gen4":
            return Gen4TechnicalMachines.getTm(move);
         case "tm_gen3":
            return Gen3TechnicalMachines.getTm(move);
         case "tm_gen2":
            return Gen2TechnicalMachines.getTm(move);
         case "tm_gen1":
            return Gen1TechnicalMachines.getTm(move);
         default:
            return null;
      }
   }

   static ITechnicalMove[] getAllFor(String name) {
      switch (name) {
         case "tr8":
         case "tr_gen8":
            return Gen8TechnicalRecords.values();
         case "tm8":
         case "tm_gen8":
            return Gen8TechnicalMachines.values();
         case "tm_gen7":
            return Gen7TechnicalMachines.values();
         case "tm_gen6":
            return Gen6TechnicalMachines.values();
         case "tm_gen5":
            return Gen5TechnicalMachines.values();
         case "tm_gen4":
            return Gen4TechnicalMachines.values();
         case "tm_gen3":
            return Gen3TechnicalMachines.values();
         case "tm_gen2":
            return Gen2TechnicalMachines.values();
         case "tm_gen1":
            return Gen1TechnicalMachines.values();
         default:
            return null;
      }
   }

   Map getTypeMap();

   static List getForType(ITechnicalMove group, EnumType type) {
      return (List)group.getTypeMap().getOrDefault(type, new ArrayList());
   }

   static Map getTypeMapFor(ITechnicalMove group) {
      return group.getTypeMap();
   }

   static void mapToTypes() {
      ITechnicalMove[][] values = new ITechnicalMove[][]{Gen1TechnicalMachines.values(), Gen2TechnicalMachines.values(), Gen3TechnicalMachines.values(), Gen4TechnicalMachines.values(), Gen5TechnicalMachines.values(), Gen6TechnicalMachines.values(), Gen7TechnicalMachines.values(), Gen8TechnicalMachines.values(), Gen8TechnicalRecords.values()};
      ITechnicalMove[][] var1 = values;
      int var2 = values.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ITechnicalMove[] group = var1[var3];
         Map map = getTypeMapFor(group[0]);
         EnumType[] var6 = EnumType.values();
         int var7 = var6.length;

         int var8;
         for(var8 = 0; var8 < var7; ++var8) {
            EnumType type = var6[var8];
            map.put(type, new ArrayList());
         }

         ITechnicalMove[] var10 = group;
         var7 = group.length;

         for(var8 = 0; var8 < var7; ++var8) {
            ITechnicalMove tm = var10[var8];
            ((List)map.get(tm.getAttack().getAttackType())).add(tm);
         }
      }

   }

   public static class Adapter implements JsonDeserializer, JsonSerializer {
      public JsonElement serialize(ITechnicalMove src, Type typeOfSrc, JsonSerializationContext context) {
         return new JsonPrimitive(src.getAttackName());
      }

      public ITechnicalMove deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         ITechnicalMove move;
         if (typeOfT == Gen8TechnicalMachines.class) {
            move = (ITechnicalMove)Gen8TechnicalMachines.nameMap.get(json.getAsString());
         } else if (typeOfT == Gen8TechnicalRecords.class) {
            move = (ITechnicalMove)Gen8TechnicalRecords.nameMap.get(json.getAsString());
         } else if (typeOfT == Gen7TechnicalMachines.class) {
            move = (ITechnicalMove)Gen7TechnicalMachines.nameMap.get(json.getAsString());
         } else if (typeOfT == Gen6TechnicalMachines.class) {
            move = (ITechnicalMove)Gen6TechnicalMachines.nameMap.get(json.getAsString());
         } else if (typeOfT == Gen5TechnicalMachines.class) {
            move = (ITechnicalMove)Gen5TechnicalMachines.nameMap.get(json.getAsString());
         } else if (typeOfT == Gen4TechnicalMachines.class) {
            move = (ITechnicalMove)Gen4TechnicalMachines.nameMap.get(json.getAsString());
         } else if (typeOfT == Gen3TechnicalMachines.class) {
            move = (ITechnicalMove)Gen3TechnicalMachines.nameMap.get(json.getAsString());
         } else if (typeOfT == Gen2TechnicalMachines.class) {
            move = (ITechnicalMove)Gen2TechnicalMachines.nameMap.get(json.getAsString());
         } else {
            if (typeOfT != Gen1TechnicalMachines.class) {
               throw new RuntimeException("Technical record subclass not programed for serialization");
            }

            move = (ITechnicalMove)Gen1TechnicalMachines.nameMap.get(json.getAsString());
         }

         if (move == null) {
            throw new RuntimeException("Invalid move name when deserializing: " + json.getAsString() + " from " + typeOfT.getTypeName().substring(typeOfT.getTypeName().lastIndexOf(".") + 1));
         } else {
            return move;
         }
      }
   }
}

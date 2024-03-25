package com.pixelmonmod.pixelmon.worldGeneration.structure.gyms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ComingSoon;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import java.util.Optional;

public class MovesetDefinition {
   public String[] move1;
   public String[] move2;
   public String[] move3;
   public String[] move4;
   public String[] heldItem;
   public EnumNature[] nature;
   public AbilityBase[] ability;
   public int evHP;
   public int evAtk;
   public int evDef;
   public int evSpAtk;
   public int evSpDef;
   public int evSpeed;
   public int ivHP = 31;
   public int ivAtk = 31;
   public int ivDef = 31;
   public int ivSpAtk = 31;
   public int ivSpDef = 31;
   public int ivSpeed = 31;
   public boolean ivsDefined;
   public boolean doubleOnly;
   public boolean lead;
   public int minLevel;
   public int form = -1;

   public static MovesetDefinition readMovesetDefinition(JsonObject obj) {
      MovesetDefinition def = new MovesetDefinition();
      def.move1 = getMoveArray(obj, "move1");
      def.move2 = getMoveArray(obj, "move2");
      def.move3 = getMoveArray(obj, "move3");
      def.move4 = getMoveArray(obj, "move4");
      JsonArray movesetArray;
      int i;
      String abilityName;
      if (obj.has("heldItem")) {
         movesetArray = obj.getAsJsonArray("heldItem");
         def.heldItem = new String[movesetArray.size()];

         for(i = 0; i < movesetArray.size(); ++i) {
            abilityName = movesetArray.get(i).getAsString();
            BaseShopItem item = ServerNPCRegistry.shopkeepers.getItem(abilityName);
            if (item != null) {
               def.heldItem[i] = abilityName;
            }
         }
      }

      if (obj.has("nature")) {
         movesetArray = obj.getAsJsonArray("nature");
         def.nature = new EnumNature[movesetArray.size()];

         for(i = 0; i < movesetArray.size(); ++i) {
            abilityName = movesetArray.get(i).getAsString();
            EnumNature currentNature = EnumNature.natureFromString(abilityName);
            if (currentNature == null) {
               handleInvalidError("Nature", abilityName);
               currentNature = EnumNature.Serious;
            }

            def.nature[i] = currentNature;
         }
      }

      if (obj.has("ability")) {
         movesetArray = obj.getAsJsonArray("ability");
         def.ability = new AbilityBase[movesetArray.size()];

         for(i = 0; i < movesetArray.size(); ++i) {
            abilityName = movesetArray.get(i).getAsString();
            Optional abilityBase = AbilityBase.getAbility(abilityName);
            if (abilityBase.isPresent()) {
               def.ability[i] = (AbilityBase)abilityBase.get();
            } else {
               handleInvalidError("Ability", abilityName);
               def.ability[i] = new ComingSoon(abilityName);
            }
         }
      }

      if (obj.has("minLevel")) {
         def.minLevel = obj.get("minLevel").getAsInt();
      }

      if (obj.has("evAtk")) {
         def.evAtk = obj.get("evAtk").getAsInt();
      }

      if (obj.has("evDef")) {
         def.evDef = obj.get("evDef").getAsInt();
      }

      if (obj.has("evSpAtk")) {
         def.evSpAtk = obj.get("evSpAtk").getAsInt();
      }

      if (obj.has("evSpDef")) {
         def.evSpDef = obj.get("evSpDef").getAsInt();
      }

      if (obj.has("evSpeed")) {
         def.evSpeed = obj.get("evSpeed").getAsInt();
      }

      if (obj.has("evHP")) {
         def.evHP = obj.get("evHP").getAsInt();
      }

      if (obj.has("ivAtk")) {
         def.ivAtk = obj.get("ivAtk").getAsInt();
         def.ivsDefined = true;
      }

      if (obj.has("ivDef")) {
         def.ivDef = obj.get("ivDef").getAsInt();
         def.ivsDefined = true;
      }

      if (obj.has("ivSpAtk")) {
         def.ivSpAtk = obj.get("ivSpAtk").getAsInt();
         def.ivsDefined = true;
      }

      if (obj.has("ivSpDef")) {
         def.ivSpDef = obj.get("ivSpDef").getAsInt();
         def.ivsDefined = true;
      }

      if (obj.has("ivSpeed")) {
         def.ivSpeed = obj.get("ivSpeed").getAsInt();
         def.ivsDefined = true;
      }

      if (obj.has("ivHP")) {
         def.ivHP = obj.get("ivHP").getAsInt();
         def.ivsDefined = true;
      }

      if (obj.has("doubleOnly")) {
         def.doubleOnly = obj.get("doubleOnly").getAsBoolean();
      }

      if (obj.has("lead")) {
         def.lead = obj.get("lead").getAsBoolean();
      }

      if (obj.has("form")) {
         def.form = obj.get("form").getAsInt();
      }

      return def;
   }

   private static String[] getMoveArray(JsonObject obj, String jsonName) {
      if (!obj.has(jsonName)) {
         return new String[0];
      } else {
         JsonArray movesetArray = obj.getAsJsonArray(jsonName);
         String[] moveArray = new String[movesetArray.size()];

         for(int i = 0; i < movesetArray.size(); ++i) {
            String moveName = movesetArray.get(i).getAsString();
            Optional baseAttack = AttackBase.getAttackBaseFromEnglishName(moveName);
            if (!baseAttack.isPresent() && !moveName.contains("Hidden Power")) {
               handleInvalidError("move", moveName);
               moveArray[i] = "Tackle";
            } else {
               moveArray[i] = moveName;
            }
         }

         return moveArray;
      }
   }

   private static void handleInvalidError(String type, String name) {
      Pixelmon.LOGGER.info("Invalid " + type + " in Gym NPC Trainer definition: " + name + ".");
   }
}

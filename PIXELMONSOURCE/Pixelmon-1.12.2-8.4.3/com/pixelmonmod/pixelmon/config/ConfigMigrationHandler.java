package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import info.pixelmon.repack.ninja.leaping.configurate.commented.CommentedConfigurationNode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class ConfigMigrationHandler {
   public static HashMap consumers = new HashMap();

   public static boolean handleMigration(CommentedConfigurationNode configNode) {
      String key = ((StringBuilder)Arrays.stream(configNode.getPath()).filter((o) -> {
         return o instanceof String;
      }).collect(StringBuilder::new, (sb, k) -> {
         if (sb.length() == 0) {
            sb.append(k);
         } else {
            sb.append(".").append(k);
         }

      }, StringBuilder::append)).toString();
      if (consumers.containsKey(key)) {
         Pixelmon.LOGGER.info("MigrationHandler: Migrating " + configNode.getKey());
         ((Consumer)consumers.get(key)).accept(configNode);
         Pixelmon.LOGGER.info("MigrationHandler: Migrated " + configNode.getKey());
         return true;
      } else {
         return false;
      }
   }

   static {
      consumers.put("General.useExternalJSONFiles", (cnf) -> {
         boolean value = cnf.getBoolean();
         Pixelmon.LOGGER.info("MigrationHandler: Applying old value " + value + " of " + cnf.getKey() + " to new nodes.");
         CommentedConfigurationNode mainNode = PixelmonConfig.getConfig();
         mainNode.getNode("General").getNode("useExternalJSONFilesDrops").setValue(value);
         mainNode.getNode("General").getNode("useExternalJSONFilesNPCs").setValue(value);
         mainNode.getNode("General").getNode("useExternalJSONFilesRules").setValue(value);
         mainNode.getNode("General").getNode("useExternalJSONFilesSpawning").setValue(value);
         mainNode.getNode("General").getNode("useExternalJSONFilesStructures").setValue(value);
         PixelmonConfig.useExternalJSONFilesDrops = value;
         PixelmonConfig.useExternalJSONFilesNPCs = value;
         PixelmonConfig.useExternalJSONFilesRules = value;
         PixelmonConfig.useExternalJSONFilesSpawning = value;
         PixelmonConfig.useExternalJSONFilesStructures = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.allowRiding", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Riding").getNode("allowRiding").setValue(value);
         PixelmonConfig.allowRiding = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.needHMToRide", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Riding").getNode("requireHM").setValue(value);
         PixelmonConfig.requireHM = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.ridingSpeedMultiplier", (cnf) -> {
         float value = cnf.getFloat();
         PixelmonConfig.getConfig().getNode("Riding").getNode("ridingSpeedMultiplier").setValue(value);
         PixelmonConfig.ridingSpeedMultiplier = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.enablePointToSteer", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Riding").getNode("enablePointToSteer").setValue(value);
         PixelmonConfig.enablePointToSteer = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.useExternalJSONFilesDrops", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("ExternalFiles").getNode("useExternalJSONFilesDrops").setValue(value);
         PixelmonConfig.useExternalJSONFilesDrops = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.useExternalJSONFilesMoves", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("ExternalFiles").getNode("useExternalJSONFilesMoves").setValue(value);
         PixelmonConfig.useExternalJSONFilesMoves = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.useExternalJSONFilesNPCs", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("ExternalFiles").getNode("useExternalJSONFilesNPCs").setValue(value);
         PixelmonConfig.useExternalJSONFilesNPCs = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.useExternalJSONFilesRules", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("ExternalFiles").getNode("useExternalJSONFilesRules").setValue(value);
         PixelmonConfig.useExternalJSONFilesRules = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.useExternalJSONFilesSpawning", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("ExternalFiles").getNode("useExternalJSONFilesSpawning").setValue(value);
         PixelmonConfig.useExternalJSONFilesSpawning = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.useExternalJSONFilesStats", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("ExternalFiles").getNode("useExternalJSONFilesStats").setValue(value);
         PixelmonConfig.useExternalJSONFilesStats = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.useExternalJSONFilesStructures", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("ExternalFiles").getNode("useExternalJSONFilesStructures").setValue(value);
         PixelmonConfig.useExternalJSONFilesStructures = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("Spawning.useBetterSpawnerConfig", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("ExternalFiles").getNode("useBetterSpawnerConfig").setValue(value);
         PixelmonConfig.useBetterSpawnerConfig = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("Spawning.spawnSetFolder", (cnf) -> {
         String value = cnf.getString();
         PixelmonConfig.getConfig().getNode("ExternalFiles").getNode("spawnSetFolder").setValue(value);
         PixelmonConfig.spawnSetFolder = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.ultraSpace", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("UltraSpace").getNode("ultraSpace").setValue(value);
         PixelmonConfig.ultraSpace = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.ultraSpaceDimId", (cnf) -> {
         int value = cnf.getInt();
         PixelmonConfig.getConfig().getNode("UltraSpace").getNode("ultraSpaceDimId").setValue(value);
         PixelmonConfig.ultraSpaceDimId = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.ultraSpaceShinyModifier", (cnf) -> {
         float value = cnf.getFloat();
         PixelmonConfig.getConfig().getNode("UltraSpace").getNode("ultraSpaceShinyModifier").setValue(value);
         PixelmonConfig.ultraSpaceShinyModifier = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.ultraSpaceBossModifier", (cnf) -> {
         float value = cnf.getFloat();
         PixelmonConfig.getConfig().getNode("UltraSpace").getNode("ultraSpaceBossModifier").setValue(value);
         PixelmonConfig.ultraSpaceBossModifier = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.ultraSpaceHiddenAbilityModifier", (cnf) -> {
         float value = cnf.getFloat();
         PixelmonConfig.getConfig().getNode("UltraSpace").getNode("ultraSpaceHiddenAbilityModifier").setValue(value);
         PixelmonConfig.ultraSpaceHiddenAbilityModifier = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.ultraSpaceColourblindMode", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("UltraSpace").getNode("ultraSpaceColourblindMode").setValue(value);
         PixelmonConfig.ultraSpaceColourblindMode = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.allowPayDayMoney", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("General").getNode("allowPayDay").setValue(value);
         PixelmonConfig.getConfig().getNode("General").getNode("allowAmuletCoin").setValue(value);
         PixelmonConfig.getConfig().getNode("General").getNode("allowHappyHour").setValue(value);
         PixelmonConfig.getConfig().getNode("General").getNode("payDayMultiplier").setValue(5);
         PixelmonConfig.allowPayDay = value;
         PixelmonConfig.allowAmuletCoin = value;
         PixelmonConfig.allowHappyHour = value;
         PixelmonConfig.payDayMultiplier = 5.0;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.reusableBirdShrines", (cnf) -> {
         boolean value = cnf.getBoolean();
         EnumEncounterMode mode = value ? EnumEncounterMode.Unlimited : EnumEncounterMode.Once;
         PixelmonConfig.getConfig().getNode("General").getNode("shrineEncounterMode").setValue(mode.ordinal());
         PixelmonConfig.shrineEncounterMode = mode;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.allowPvPExperience", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Battle").getNode("allowPvPExperience").setValue(value);
         PixelmonConfig.allowPVPExperience = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.allowTrainerExperience", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Battle").getNode("allowTrainerExperience").setValue(value);
         PixelmonConfig.allowTrainerExperience = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.returnHeldItems", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Battle").getNode("returnHeldItems").setValue(value);
         PixelmonConfig.returnHeldItems = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.forceEndBattleResult", (cnf) -> {
         int value = cnf.getInt();
         PixelmonConfig.getConfig().getNode("Battle").getNode("forceEndBattleResult").setValue(value);
         PixelmonConfig.forceEndBattleResult = EnumForceBattleResult.values()[value];
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.expModifier", (cnf) -> {
         float value = cnf.getFloat();
         PixelmonConfig.getConfig().getNode("Battle").getNode("expModifier").setValue(value);
         PixelmonConfig.expModifier = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.synchronizeChance", (cnf) -> {
         float value = cnf.getFloat();
         PixelmonConfig.getConfig().getNode("Battle").getNode("synchronizeChance").setValue(value);
         PixelmonConfig.synchronizeChance = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.allowHappyHour", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Battle").getNode("allowHappyHour").setValue(value);
         PixelmonConfig.allowHappyHour = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.allowPayDay", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Battle").getNode("allowPayDay").setValue(value);
         PixelmonConfig.allowPayDay = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.payDayMultiplier", (cnf) -> {
         double value = cnf.getDouble();
         PixelmonConfig.getConfig().getNode("Battle").getNode("payDayMultiplier").setValue(value);
         PixelmonConfig.payDayMultiplier = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.allowGMaxGoldRush", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Battle").getNode("allowGMaxGoldRush").setValue(value);
         PixelmonConfig.allowGMaxGoldRush = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.gMaxGoldRushMultiplier", (cnf) -> {
         double value = cnf.getDouble();
         PixelmonConfig.getConfig().getNode("Battle").getNode("gMaxGoldRushMultiplier").setValue(value);
         PixelmonConfig.gMaxGoldRushMultiplier = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.allowAmuletCoin", (cnf) -> {
         boolean value = cnf.getBoolean();
         PixelmonConfig.getConfig().getNode("Battle").getNode("allowAmuletCoin").setValue(value);
         PixelmonConfig.allowAmuletCoin = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
      consumers.put("General.pickupRate", (cnf) -> {
         int value = cnf.getInt();
         PixelmonConfig.getConfig().getNode("Battle").getNode("pickupRate").setValue(value);
         PixelmonConfig.pickupRate = value;
         cnf.getParent().removeChild(cnf.getKey());
      });
   }
}

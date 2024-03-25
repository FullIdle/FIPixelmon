package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.battles.rules.clauses.tiers.RulesRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.NPCRegistryData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.NPCRegistryShopkeepers;
import com.pixelmonmod.pixelmon.entities.npcs.registry.RaidSpawningRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextFormatting;

public class ReloadCommand extends PixelmonCommand {
   public String func_71517_b() {
      return "pokereload";
   }

   public String func_71518_a(ICommandSender commandSender) {
      return "/pokereload";
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      try {
         PixelmonConfig.reload();
      } catch (IOException var15) {
         var15.printStackTrace();
      }

      HashMap oldItems = NPCRegistryShopkeepers.shopItems;
      NPCRegistryShopkeepers.shopItems = new HashMap();

      try {
         ServerNPCRegistry.shopkeepers.registerShopItems();
      } catch (Exception var14) {
         this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.reload.error.shopitems", new Object[0]);
         NPCRegistryShopkeepers.shopItems = oldItems;
      }

      Set langCodes = ServerNPCRegistry.data.keySet();
      String[] langCodeArray = (String[])langCodes.toArray(new String[langCodes.size()]);
      String[] var6 = langCodeArray;
      int var7 = langCodeArray.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String langCode = var6[var8];
         NPCRegistryData oldData = (NPCRegistryData)ServerNPCRegistry.data.remove(langCode);

         try {
            ServerNPCRegistry.registerNPCS(langCode);
         } catch (Exception var13) {
            var13.printStackTrace();
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.reload.error.npcs", new Object[0]);
            ServerNPCRegistry.data.put(langCode, oldData);
         }
      }

      PixelmonSpawning.totalReload();
      RulesRegistry.registerRules();

      try {
         RaidSpawningRegistry.registerRaidSpawning();
      } catch (Exception var12) {
         var12.printStackTrace();
      }

      this.sendMessage(sender, "pixelmon.command.reload.done", new Object[0]);
   }
}

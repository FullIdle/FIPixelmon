package com.pixelmonmod.tcg.commands;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.Energy;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TCGEssenceReset extends SubcommandBase {
   String getHelp(ICommandSender sender) {
      return "/tcg essencereset <player>: resets all a players essence to 0\n";
   }

   String getPermissionNode() {
      return "tcg.essencereset";
   }

   int getPermissionLevel() {
      return 4;
   }

   protected boolean execute(ICommandSender sender, String[] args) throws CommandException {
      String[] var3 = args;
      int var4 = args.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String arg = var3[var5];
         TCG.logger.warn(arg);
      }

      TCG.logger.warn(args.length);
      if (args.length == 1) {
         sender.func_145747_a(new TextComponentString(this.getHelp(sender)));
         return false;
      } else if (args.length == 2) {
         EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_152612_a(args[1]);
         if (player == null) {
            sendMessage(sender, TextFormatting.RED, args[1] + " is not a valid player!");
         }

         Energy.resetEssence(player);
         sender.func_145747_a(new TextComponentString("You have reset " + args[1] + "'s essence!"));
         return true;
      } else {
         return false;
      }
   }
}

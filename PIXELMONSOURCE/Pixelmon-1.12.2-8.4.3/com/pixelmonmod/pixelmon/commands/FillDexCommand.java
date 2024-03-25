package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class FillDexCommand extends PixelmonCommand {
   public String func_71517_b() {
      return "filldex";
   }

   public String func_71518_a(ICommandSender icommandsender) {
      return "/filldex <me:player>";
   }

   public int func_82362_a() {
      return 2;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP target = null;
      if (args.length != 0 && !args[0].equalsIgnoreCase("me")) {
         target = getPlayer(args[0]);
         if (target == null) {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalidplayer", new Object[0]);
            return;
         }
      } else {
         if (!(sender instanceof EntityPlayerMP)) {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.general.invalidplayer", new Object[0]);
            return;
         }

         target = (EntityPlayerMP)sender;
      }

      PlayerPartyStorage storage = Pixelmon.storageManager.getParty(target);
      EnumSpecies[] var5 = EnumSpecies.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         EnumSpecies species = var5[var7];
         storage.pokedex.set(species.getNationalPokedexInteger(), EnumPokedexRegisterStatus.caught);
      }

      storage.pokedex.update();
   }
}

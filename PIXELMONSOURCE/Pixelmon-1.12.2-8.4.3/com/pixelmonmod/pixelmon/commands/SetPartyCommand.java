package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.comm.packetHandlers.chooseMoveset.ChooseMoveset;
import com.pixelmonmod.pixelmon.comm.packetHandlers.chooseMoveset.ChoosingMovesetData;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;

public class SetPartyCommand extends PixelmonCommand {
   public SetPartyCommand() {
      super("setparty", "/setparty <lvl>", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length != 1 && args.length != 2) {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      } else {
         int level = requireInt(args[0], 1, PixelmonConfig.maxLevel, "commands.generic.num.invalid", new Object[0]);
         EntityPlayerMP player = requireEntityPlayer(sender);
         if (BattleRegistry.getBattle(player) != null) {
            endCommand("pixelmon.command.general.inbattle", new Object[0]);
         }

         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         List team = party.getTeam();
         team.forEach((p) -> {
            p.setLevel(level);
         });
         if (args.length == 2 && args[1].equals("moves")) {
            ChoosingMovesetData data = new ChoosingMovesetData(player, team);
            data.next();
            if (!data.pokemonList.isEmpty()) {
               ChooseMoveset.choosingMoveset.put(player.func_110124_au(), data);
            }
         }
      }

   }
}

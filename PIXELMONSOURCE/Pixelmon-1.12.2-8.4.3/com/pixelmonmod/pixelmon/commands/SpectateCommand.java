package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.events.SpectateEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.Spectator;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.EndSpectate;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetAllBattlingPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetBattlingPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetPokemonBattleData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetPokemonTeamData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.StartBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.StartSpectate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class SpectateCommand extends PixelmonCommand {
   public String func_71517_b() {
      return "spectate";
   }

   public String func_71518_a(ICommandSender arg0) {
      return "/spectate <playerName>";
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player = requireEntityPlayer(sender);
      if (args.length == 0) {
         if (!BattleRegistry.removeSpectator(player)) {
            this.sendMessage(sender, TextFormatting.RED, this.func_71518_a(sender), new Object[0]);
         }

         Pixelmon.network.sendTo(new EndSpectate(), player);
      } else if (args.length == 1) {
         EntityPlayerMP target = requireEntityPlayer(args[0]);
         if (target == player) {
            endCommand("pixelmon.command.spectate.self", new Object[0]);
         }

         if (BattleRegistry.getBattle(player) != null) {
            endCommand("pixelmon.command.general.inbattle", new Object[0]);
         }

         BattleControllerBase base = (BattleControllerBase)require(BattleRegistry.getBattle(target), "pixelmon.command.spectate.nobattle", new Object[]{target.getDisplayNameString()});
         PlayerParticipant watchedPlayer = (PlayerParticipant)require(base.getPlayer(target.func_70005_c_()), "commands.generic.exception", new Object[0]);
         if (!Pixelmon.EVENT_BUS.post(new SpectateEvent.StartSpectate(player, base, target))) {
            Pixelmon.network.sendTo(new StartBattle(base.battleIndex, base.getBattleType(watchedPlayer), base.rules), player);
            Pixelmon.network.sendTo(new SetAllBattlingPokemon(PixelmonInGui.convertToGUI(Arrays.asList(watchedPlayer.allPokemon))), player);
            ArrayList teamList = watchedPlayer.getTeamPokemonList();
            Pixelmon.network.sendTo(new SetBattlingPokemon(teamList), player);
            Pixelmon.network.sendTo(new SetPokemonBattleData(PixelmonInGui.convertToGUI(teamList), false), player);
            Pixelmon.network.sendTo(new SetPokemonBattleData(watchedPlayer.getOpponentData(), true), player);
            if (base.getTeam(watchedPlayer).size() > 1) {
               Pixelmon.network.sendTo(new SetPokemonTeamData(watchedPlayer.getAllyData()), player);
            }

            Pixelmon.network.sendTo(new StartSpectate(watchedPlayer.player.func_110124_au(), base.rules.battleType), player);
            base.addSpectator(new Spectator(player, target.func_70005_c_()));
         }
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args, new String[0]);
   }
}

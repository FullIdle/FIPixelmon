package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.events.BreedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.UnbreedableFlag;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.BreedLogic;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class BreedCommand extends PixelmonCommand {
   public BreedCommand() {
      super("breed", "/breed <username> <party slot #1> <party slot #2>", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length != 1 && args.length != 3) {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      } else {
         EntityPlayerMP player = requireEntityPlayer(args[0]);
         PlayerPartyStorage storage = (PlayerPartyStorage)require(getPlayerStorage(player), "pixelmon.command.general.invalidplayer", new Object[0]);
         int slot1 = requireInt(args[1], 1, 6, "pixelmon.command.breed.invalidslot", new Object[]{args[1]});
         int slot2 = requireInt(args[2], 1, 6, "pixelmon.command.breed.invalidslot", new Object[]{args[2]});
         Pokemon pokemon1 = (Pokemon)require(storage.get(slot1 - 1), "pixelmon.command.breed.nullslot", new Object[]{slot1});
         Pokemon pokemon2 = (Pokemon)require(storage.get(slot2 - 1), "pixelmon.command.breed.nullslot", new Object[]{slot2});
         require(!pokemon1.isEgg(), "pixelmon.command.breed.egg", new Object[]{pokemon2.getDisplayName()});
         require(!pokemon2.isEgg(), "pixelmon.command.breed.egg", new Object[]{pokemon1.getDisplayName()});
         require(pokemon1 != pokemon2, "pixelmon.command.breed.same", new Object[]{pokemon1.getDisplayName()});
         require(BreedLogic.canBreed(pokemon1, pokemon2), "pixelmon.command.breed.notcompatible", new Object[]{pokemon1.getDisplayName(), pokemon2.getDisplayName()});
         if (UnbreedableFlag.UNBREEDABLE.matches(pokemon1)) {
            endCommand("pixelmon.ranch.cannotbreed", new Object[]{pokemon1.getDisplayName()});
         } else if (UnbreedableFlag.UNBREEDABLE.matches(pokemon2)) {
            endCommand("pixelmon.ranch.cannotbreed", new Object[]{pokemon2.getDisplayName()});
         }

         Pokemon egg = BreedLogic.makeEgg(pokemon1, pokemon2);
         BreedEvent.MakeEgg makeEvent = new BreedEvent.MakeEgg(player.func_110124_au(), (TileEntityRanchBlock)null, egg, pokemon1, pokemon2);
         if (Pixelmon.EVENT_BUS.post(makeEvent)) {
            return;
         }

         BreedEvent.CollectEgg eggEvent = new BreedEvent.CollectEgg(player.func_110124_au(), (TileEntityRanchBlock)null, makeEvent.getEgg());
         if (Pixelmon.EVENT_BUS.post(eggEvent)) {
            return;
         }

         this.sendMessage(sender, "pixelmon.command.breed.giveegg", new Object[]{pokemon1.getDisplayName(), pokemon2.getDisplayName()});
         storage.add(eggEvent.getEgg());
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      if (args.length == 1) {
         return tabCompleteUsernames(args);
      } else {
         return args.length != 2 && args.length != 3 ? tabComplete(args, new String[0]) : tabComplete(args, new String[]{"1", "2", "3", "4", "5", "6"});
      }
   }
}

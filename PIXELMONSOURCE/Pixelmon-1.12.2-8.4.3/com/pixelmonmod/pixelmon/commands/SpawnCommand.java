package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class SpawnCommand extends PixelmonCommand {
   public SpawnCommand() {
      super("pokespawn", "/pokespawn <pokemon> [<x> <y> <z>]", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length >= 1) {
         World world = sender instanceof EntityPlayerMP ? sender.func_130014_f_() : getServer().func_71218_a(0);
         EntityPixelmon pokemon = (EntityPixelmon)require(PokemonSpec.from(args).create((World)world), "pixelmon.command.general.notingame", new Object[]{args[0]});
         BlockPos pos;
         if (args.length >= 4) {
            try {
               pos = func_175757_a(sender, args, args.length - 3, false);
            } catch (NumberInvalidException var7) {
               pos = sender.func_180425_c();
            }
         } else {
            pos = sender.func_180425_c();
         }

         pokemon.func_70107_b((double)pos.func_177958_n(), (double)(pos.func_177956_o() + 1), (double)pos.func_177952_p());
         pokemon.canDespawn = false;
         pokemon.setSpawnLocation(pokemon.getDefaultSpawnLocation());
         ((World)world).func_72838_d(pokemon);
         this.sendMessage(sender, "pixelmon.command.spawn.spawned", new Object[]{pokemon.getSpecies().getTranslatedName()});
         func_152374_a(sender, this, 0, "pixelmon.command.spawn.spawnednotify", new Object[]{sender.func_70005_c_(), pokemon.getSpecies().getTranslatedName()});
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompletePokemon(args) : tabComplete(args, new String[0]);
   }
}

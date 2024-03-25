package com.pixelmonmod.pixelmon.commands;

import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class PokeGiveCommand extends PixelmonCommand {
   public PokeGiveCommand() {
      super("pokegive", "/pokegive [player] <pokemon>", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length >= 1) {
         this.resendWithMultipleTargets(sender, args, 0);
         GameProfile profile;
         if (EnumSpecies.hasPokemonAnyCase(args[0]) && sender.func_184102_h().func_184103_al().func_152612_a(args[0]) == null) {
            profile = requireEntityPlayer(sender).func_146103_bH();
         } else {
            profile = findProfile(args[0]);
         }

         if (profile == null) {
            endCommand("commands.generic.player.notFound", new Object[]{args[0]});
         }

         EntityPlayerMP player = getEntityPlayer(profile.getId());

         try {
            if (profile.getName().equalsIgnoreCase(args[0])) {
               args = (String[])Arrays.copyOfRange(args, 1, args.length);
            }

            PokemonSpec spec = PokemonSpec.from(args);
            spec.boss = null;
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(profile.getId());
            if (spec.name == null) {
               endCommand("pixelmon.command.general.invalid", new Object[0]);
            }

            Pokemon pokemon = spec.create();
            if (player != null) {
               Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent(player, ReceiveType.Command, pokemon));
            }

            if (player != null && BattleRegistry.getBattle(player) != null) {
               Pixelmon.storageManager.getPCForPlayer(profile.getId()).add(pokemon);
            } else {
               pps.add(pokemon);
            }

            if (!pokemon.isEgg() && !Pixelmon.EVENT_BUS.post(new PokedexEvent(pps.uuid, pokemon, EnumPokedexRegisterStatus.caught, "commandGiven"))) {
               pps.pokedex.set(pokemon, EnumPokedexRegisterStatus.caught);
               pps.pokedex.update();
            }

            this.sendMessage(sender, "pixelmon.command.give.givesuccess" + (pokemon.isEgg() ? "egg" : ""), new Object[]{profile.getName(), pokemon.getSpecies().getTranslatedName()});
            func_152374_a(sender, this, 0, "pixelmon.command.give.notifygive" + (pokemon.isEgg() ? "egg" : ""), new Object[]{sender.func_70005_c_(), profile.getName(), pokemon.getSpecies().getTranslatedName()});
         } catch (NullPointerException var8) {
            endCommand("pixelmon.command.general.notingame", new Object[]{args[0]});
         }
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      if (args.length == 1) {
         List list = tabCompleteUsernames(args);
         return !list.isEmpty() ? list : tabCompletePokemon(args);
      } else {
         return args.length == 2 ? tabCompletePokemon(args) : tabComplete(args, new String[0]);
      }
   }
}

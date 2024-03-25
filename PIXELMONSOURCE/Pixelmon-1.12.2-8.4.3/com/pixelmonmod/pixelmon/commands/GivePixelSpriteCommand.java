package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class GivePixelSpriteCommand extends PixelmonCommand {
   public GivePixelSpriteCommand() {
      super("givepixelsprite", "/givepixelsprite <player> <pokemon spec>", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      if (args.length >= 1) {
         this.resendWithMultipleTargets(sender, args, 0);
         EntityPlayerMP player = EnumSpecies.hasPokemonAnyCase(args[0]) ? requireEntityPlayer(sender) : requireEntityPlayer(args[0]);
         if (player.func_70005_c_().equalsIgnoreCase(args[0])) {
            args = (String[])Arrays.copyOfRange(args, 1, args.length);
         }

         PokemonSpec spec = PokemonSpec.from(args);
         if (spec.name == null) {
            endCommand("pixelmon.command.general.invalid", new Object[0]);
         }

         ItemStack stack = ItemPixelmonSprite.getPhoto(spec.create());
         player.field_71071_by.func_70441_a(stack);
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
      if (args.length == 1) {
         List list = tabCompleteUsernames(args);
         return !list.isEmpty() ? list : tabCompletePokemon(args);
      } else {
         return args.length == 2 ? tabCompletePokemon(args) : tabComplete(args, new String[0]);
      }
   }
}

package com.pixelmonmod.pixelmon.commands.client;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.dev.GuiDevScaleEditor;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class DevScaleCommand extends PixelmonCommand {
   public String func_71517_b() {
      return "devscale";
   }

   public String func_71518_a(ICommandSender icommandsender) {
      return "/devscale [spec]";
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayer player = (EntityPlayer)sender;
      PokemonSpec spec;
      if (args.length > 0) {
         spec = new PokemonSpec(args);
      } else {
         spec = new PokemonSpec(new String[]{EnumSpecies.Bulbasaur.getPokemonName()});
      }

      ClientProxy.scheduleNextTick(() -> {
         Minecraft.func_71410_x().func_147108_a(new GuiDevScaleEditor(spec));
         return 0;
      });
   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1 ? tabCompletePokemon(args) : tabComplete(args, new String[0]);
   }
}

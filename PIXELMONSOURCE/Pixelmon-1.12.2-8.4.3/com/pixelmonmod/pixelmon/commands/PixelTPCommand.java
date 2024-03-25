package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class PixelTPCommand extends PixelmonCommand {
   public PixelTPCommand() {
      super("pixeltp", "/pixeltp - teleports to a random Pixelmon", 4);
   }

   protected void execute(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player = requireEntityPlayer(sender);
      List pixelmonList = player.field_70170_p.func_175644_a(EntityPixelmon.class, (entity) -> {
         return entity != null && !entity.field_70128_L;
      });
      if (!pixelmonList.isEmpty()) {
         BlockPos pos = ((EntityPixelmon)RandomHelper.getRandomElementFromList(pixelmonList)).func_180425_c();
         player.func_70634_a((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p());
      }
   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
      return tabComplete(args, new String[0]);
   }
}

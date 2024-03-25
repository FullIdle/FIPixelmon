package com.pixelmonmod.pixelmon.commands;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class Save extends PixelmonCommand {
   public String func_71517_b() {
      return "pokesave";
   }

   public String func_71518_a(ICommandSender sender) {
      return "/pokesave <all|flush|player..>";
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
   }

   public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
      if (args.length == 1) {
         List list = Lists.newArrayList(new String[]{"all", "flush"});
         list.addAll(Lists.newArrayList(server.func_71213_z()));
         return list;
      } else {
         return tabCompleteUsernames(args);
      }
   }
}

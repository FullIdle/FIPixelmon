package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class FreezeCommand extends PixelmonCommand {
   public FreezeCommand() {
      super("freeze", "/freeze", 4);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      Pixelmon.freeze = !Pixelmon.freeze;
      if (Pixelmon.freeze) {
         this.sendMessage(sender, "pixelmon.command.freeze.frozen", new Object[0]);
         func_152374_a(sender, this, 0, "pixelmon.command.freeze.frozen", new Object[0]);
      } else {
         this.sendMessage(sender, "pixelmon.command.freeze.unfrozen", new Object[0]);
         func_152374_a(sender, this, 0, "pixelmon.command.freeze.unfrozen", new Object[0]);
      }

   }
}

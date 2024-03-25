package com.pixelmonmod.pixelmon.api.events.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ExecutePixelmonCommandEvent extends Event {
   public final ICommandSender sender;
   private PixelmonCommand command;
   private String[] args;

   public ExecutePixelmonCommandEvent(ICommandSender sender, PixelmonCommand command, String[] args) {
      this.sender = sender;
      this.command = command;
      this.args = args;
   }

   public PixelmonCommand getCommand() {
      return this.command;
   }

   public void setCommand(PixelmonCommand command) {
      if (command != null) {
         this.command = command;
      }

   }

   public String[] getArgs() {
      return this.args;
   }

   public void setArgs(String[] args) {
      if (args != null) {
         String[] var2 = args;
         int var3 = args.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String arg = var2[var4];
            if (arg == null) {
               return;
            }
         }

         this.args = args;
      }

   }
}

package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityWarpPlate;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;

public class WarpPlateCommand extends PixelmonCommand {
   public WarpPlateCommand() {
      super("warpplate", "/warpplate set x y z", 2);
   }

   public void execute(ICommandSender sender, String[] args) throws CommandException {
      EntityPlayerMP player = requireEntityPlayer(sender);
      if (args.length == 4 && args[0].equalsIgnoreCase("set")) {
         Block blockStandingOn = player.field_70170_p.func_180495_p(player.func_180425_c()).func_177230_c();
         if (blockStandingOn == PixelmonBlocks.warpPlate) {
            TileEntityWarpPlate warpPlate = (TileEntityWarpPlate)player.field_70170_p.func_175625_s(player.func_180425_c());
            if (warpPlate != null && warpPlate.calculatePosition(args[1], args[2], args[3]) != null) {
               warpPlate.setWarpPosition(args[1], args[2], args[3]);
               this.sendMessage(sender, "pixelmon.command.warpplate.set", new Object[]{warpPlate.getWarpPosition()});
            } else {
               this.sendMessage(sender, TextFormatting.RED, this.func_71518_a(player), new Object[0]);
            }
         } else {
            this.sendMessage(sender, TextFormatting.RED, "pixelmon.command.warpplate.notstanding", new Object[0]);
         }
      } else {
         sender.func_145747_a(format(TextFormatting.RED, "pixelmon.command.general.invalid", new Object[0]));
         endCommand(this.func_71518_a(sender), new Object[0]);
      }

   }
}

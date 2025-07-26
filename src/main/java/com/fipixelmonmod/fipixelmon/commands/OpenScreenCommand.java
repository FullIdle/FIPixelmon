package com.fipixelmonmod.fipixelmon.commands;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import lombok.val;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextFormatting;

public class OpenScreenCommand extends PixelmonCommand {
    public static final OpenScreenCommand INSTANCE = new OpenScreenCommand();

    private OpenScreenCommand() {
        super("openscreen", "/openscreen <player> <type> <int...(data)>", 2);
    }

    @Override
    protected void execute(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            sender.sendMessage(format(TextFormatting.RED, "pixelmon.command.general.invalid"));
            endCommand(this.getUsage(sender));
            return;
        }
        val entityPlayer = getEntityPlayer(args[0]);
        if (entityPlayer == null) {
            endCommand(this.getUsage(sender));
            return;
        }
        if (!EnumGuiScreen.hasGUI(args[1])) {
            endCommand(this.getUsage(sender));
            return;
        }
        val guiType = EnumGuiScreen.valueOf(args[1]);
        int[] data = new int[args.length - 2];
        if (args.length > 2)
            for (int i = 2; i < args.length; i++)
                try {
                    data[i-2] = Integer.parseInt(args[i]);
                } catch (NumberFormatException e) {
                    endCommand(this.getUsage(sender));
                    return;
                }
        OpenScreen.open(entityPlayer, guiType, data);
    }
}

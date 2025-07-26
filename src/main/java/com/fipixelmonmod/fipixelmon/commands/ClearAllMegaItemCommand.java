package com.fipixelmonmod.fipixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ClearAllMegaItemCommand extends PixelmonCommand {
    public static final ClearAllMegaItemCommand INSTANCE = new ClearAllMegaItemCommand();

    private ClearAllMegaItemCommand() {
        super("clearallmegaitem", "/clearallmegaitem [player]", 2);
    }

    public void execute(ICommandSender sender, String[] args) throws CommandException {
        this.resendWithMultipleTargets(sender, args, 0);
        EntityPlayerMP player = args.length == 1 ? requireEntityPlayer(args[0]) : requireEntityPlayer(sender);
        PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
        party.setMegaItemsUnlocked(EnumMegaItemsUnlocked.None);
        party.setMegaItem(EnumMegaItem.None, false);
        this.sendMessage(sender, "%s 已清除所有佩戴项。", player.getName());
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args);
    }
}

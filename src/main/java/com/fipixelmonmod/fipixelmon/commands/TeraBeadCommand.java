package com.fipixelmonmod.fipixelmon.commands;

import com.fipixelmonmod.fipixelmon.helper.PlayerPartyStorageHelper;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TeraBeadCommand extends PixelmonCommand {
    public static final TeraBeadCommand INSTANCE = new TeraBeadCommand();

    private TeraBeadCommand() {
        super("terabead", "/terabead [player]", 2);
    }

    public void execute(ICommandSender sender, String[] args) throws CommandException {
        this.resendWithMultipleTargets(sender, args, 0);
        EntityPlayerMP player = args.length == 1 ? requireEntityPlayer(args[0]) : requireEntityPlayer(sender);
        PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
        if (!party.getMegaItemsUnlocked().canMega()) {
            party.setMegaItem(EnumMegaItem.BraceletORAS, false);
            PlayerPartyStorageHelper.unlockTerastal(party);
            if (sender != player) {
                this.sendMessage(sender, "%s 被给予了一个太晶珠。", player.getName());
            }

            this.sendMessage(player, "您获得了一个太晶珠。");
            notifyCommandListener(sender, this, 0, "%s 给予了玩家 %s 一个太晶珠。", sender.getName(), player.getName());
        } else {
            this.sendMessage(sender, "%s 已经拥有一个太晶珠了。", player.getName());
        }

    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? tabCompleteUsernames(args) : tabComplete(args);
    }
}

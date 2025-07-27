package com.fipixelmonmod.fipixelmon.commands;

import com.fipixelmonmod.fipixelmon.helper.PokemonHelper;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.val;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LookTeraTypeCommand extends PixelmonCommand {
    public static final LookTeraTypeCommand INSTANCE = new LookTeraTypeCommand();

    public LookTeraTypeCommand() {
        super("lookteratype", "/lookteratype [player] [1-6]", 1);
    }

    @Override
    protected void execute(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            endCommand(this.getUsage(sender));
            return;
        }
        EntityPlayerMP player = requireEntityPlayer(args[0]);
        PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
        try {
            val i = Integer.parseInt(args[1]) - 1;
            if (i < 0 || i > 5) {
                endCommand(this.getUsage(sender));
                return;
            }
            val pokemon = party.get(i);
            if (pokemon == null) {
                endCommand(args[1] + "是空槽");
                return;
            }
            val teraType = PokemonHelper.getTeraType(pokemon);
            this.sendMessage(sender, "%s 的太晶属性为: %s", pokemon.getDisplayName(), teraType == null ? "NONE" : teraType.getLocalizedName());
        } catch (NumberFormatException e) {
            endCommand(this.getUsage(sender));
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length < 1) return tabComplete(args);
        if (args.length == 1) return tabCompleteUsernames(args);
        if (args.length == 2) return tabComplete(args, IntStream.range(1, 7).boxed().collect(Collectors.toList()));
        return Collections.emptyList();
    }
}

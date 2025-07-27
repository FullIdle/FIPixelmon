package com.fipixelmonmod.fipixelmon.commands;

import com.fipixelmonmod.fipixelmon.enums.EnumTeraType;
import com.fipixelmonmod.fipixelmon.helper.PlayerPartyStorageHelper;
import com.fipixelmonmod.fipixelmon.helper.PokemonHelper;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.val;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SetTeraTypeCommand extends PixelmonCommand {
    public static final SetTeraTypeCommand INSTANCE = new SetTeraTypeCommand();

    private SetTeraTypeCommand() {
        super("setteratype", "/setteratype [player] [1-6] [type]", 2);
    }

    public void execute(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 3) {
            endCommand(this.getUsage(sender));
            return;
        }
        EntityPlayerMP player = requireEntityPlayer(args[0]);
        PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
        try {
            val slot = Integer.parseInt(args[1]) - 1;
            if (slot < 0 || slot > 5) {
                endCommand(this.getUsage(sender));
                return;
            }
            val pokemon = party.get(slot);
            if (pokemon == null) {
                endCommand(args[1]+"是空槽");
                return;
            }
            val teraType = EnumTeraType.getEnumTeraTypeFormName(args[2]);
            if (teraType == null) {
                endCommand(args[2]+"不是合法的元素");
                return;
            }
            PokemonHelper.setTeraType(pokemon, teraType);
            this.sendMessage(sender, "%s 的太晶属性 %s 设置成功。", pokemon.getDisplayName(), teraType.getLocalizedName());
        } catch (NumberFormatException e) {
            endCommand(this.getUsage(sender));
            return;
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length < 1) return tabComplete(args);
        if (args.length == 1) return tabCompleteUsernames(args);
        if (args.length == 2) return tabComplete(args, IntStream.range(1, 7).boxed().collect(Collectors.toList()));
        return tabComplete(args, Arrays.asList(EnumTeraType.VALUE));
    }
}

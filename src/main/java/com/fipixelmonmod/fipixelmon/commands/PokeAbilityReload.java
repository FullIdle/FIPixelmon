package com.fipixelmonmod.fipixelmon.commands;

import com.fipixelmonmod.fipixelmon.data.AbilityConfig;
import com.fipixelmonmod.fipixelmon.data.ExtendClassConfig;
import com.fipixelmonmod.fipixelmon.helper.ScriptEngineHelper;
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

public class PokeAbilityReload extends PixelmonCommand {
    public static final PokeAbilityReload INSTANCE = new PokeAbilityReload();

    private PokeAbilityReload() {
        super("pokeabilityreload", "/pokeabilityreload", 2);
    }

    public void execute(ICommandSender sender, String[] args) {
        AbilityConfig.extraAbilities.values().forEach(ExtendClassConfig::load);
        this.sendMessage(sender, "FIPixelmon 特性函数重载完成。(成员变化需要重启!)");
    }
}

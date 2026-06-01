package com.fipixelmonmod.fipixelmon.commands;

import com.fipixelmonmod.fipixelmon.data.AbilityConfig;
import com.fipixelmonmod.fipixelmon.data.ExtendClassConfig;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import net.minecraft.command.ICommandSender;

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

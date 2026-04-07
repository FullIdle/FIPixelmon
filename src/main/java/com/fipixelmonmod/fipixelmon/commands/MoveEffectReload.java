package com.fipixelmonmod.fipixelmon.commands;

import com.fipixelmonmod.fipixelmon.data.AbilityConfig;
import com.fipixelmonmod.fipixelmon.data.ExtendClassConfig;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommand;
import net.minecraft.command.ICommandSender;

public class MoveEffectReload extends PixelmonCommand {
    public static final MoveEffectReload INSTANCE = new MoveEffectReload();

    private MoveEffectReload() {
        super("moveeffectreload", "/moveeffectreload", 2);
    }

    public void execute(ICommandSender sender, String[] args) {
        AbilityConfig.extraAbilities.values().forEach(ExtendClassConfig::load);
        this.sendMessage(sender, "FIPixelmon 技能效果函数重载完成。(成员变化需要重启!)");
    }
}

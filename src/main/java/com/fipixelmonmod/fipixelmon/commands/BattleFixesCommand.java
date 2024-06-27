package com.fipixelmonmod.fipixelmon.commands;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;

public class BattleFixesCommand extends CommandBase {
    public static final ArrayList<IMessage> cache = new ArrayList<>();

    @Override
    public String getName() {
        return "battlefixes";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return getName();
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) {
        if (ClientProxy.battleManager.isBattling()) {
            if (ClientProxy.battleManager.isSpectating
                    || cache.isEmpty()) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§7没有可修复的数据"));
                return;
            }

            if (Minecraft.getMinecraft().player != null) {
                ClientProxy.battleManager.selectedActions.clear();
                ClientProxy.battleManager.selectedActions.addAll(cache);
                ClientProxy.battleManager.finishSelection();
                iCommandSender.sendMessage(new TextComponentString("§7已尝试修复,效果自己查看."));
            }
        }
    }
}

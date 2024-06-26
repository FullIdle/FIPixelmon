package com.fipixelmonmod.fipixelmon.commands;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
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
        if (iCommandSender instanceof EntityPlayer) {
            if (ClientProxy.battleManager.isBattling()) {
                if (cache.isEmpty()){
                    return;
                }
                if (Minecraft.getMinecraft().player != null) {
                    ClientProxy.battleManager.selectedActions = cache;
                    ClientProxy.battleManager.finishSelection();
                }
            }
        }
    }
}

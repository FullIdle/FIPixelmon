package com.fipixelmonmod.fipixelmon.commands;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class BattleFixesCommand extends CommandBase {
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
        ClientBattleManager battleManager = ClientProxy.battleManager;
        //界面修复
        if (!battleManager.isBattling()) {
            iCommandSender.sendMessage(new TextComponentString("§7你不在对战状态!尝试界面修复!"));
            ClientProxy.battleManager.battleEnded = true;
            closeBattle();
            return;
        }
        //对战修复
        if (battleManager.getCurrentPokemon().health != 0){
            //界面重选
            battleManager.startPicking(battleManager.canSwitch, battleManager.canFlee, battleManager.pokemonToChoose);
        }else{
            //空血bug
            battleManager.mode = BattleMode.EnforcedSwitch;
            battleManager.oldMode = BattleMode.MainMenu;
        }
    }

    private static void closeBattle(){
        GuiPixelmonOverlay.isVisible = true;
        Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> {
            if (mc.currentScreen instanceof GuiBattle) {
                ((GuiBattle)mc.currentScreen).restoreSettingsAndClose();
            } else {
                GuiBattle.restoreSettingsAndCloseStatic(null);
            }
        });
    }
}

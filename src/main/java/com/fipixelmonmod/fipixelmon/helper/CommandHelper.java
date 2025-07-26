package com.fipixelmonmod.fipixelmon.helper;

import com.fipixelmonmod.fipixelmon.commands.ClearAllMegaItemCommand;
import com.fipixelmonmod.fipixelmon.commands.OpenScreenCommand;
import com.fipixelmonmod.fipixelmon.commands.TerastalBeadCommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandHelper {
    public static void register(FMLServerStartingEvent event){
        event.registerServerCommand(OpenScreenCommand.INSTANCE);
        event.registerServerCommand(ClearAllMegaItemCommand.INSTANCE);
        event.registerServerCommand(TerastalBeadCommand.INSTANCE);
    }
}

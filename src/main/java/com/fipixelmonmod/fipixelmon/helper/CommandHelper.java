package com.fipixelmonmod.fipixelmon.helper;

import com.fipixelmonmod.fipixelmon.commands.*;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandHelper {
    public static void register(FMLServerStartingEvent event){
        event.registerServerCommand(OpenScreenCommand.INSTANCE);
        event.registerServerCommand(ClearAllMegaItemCommand.INSTANCE);
        event.registerServerCommand(TeraBeadCommand.INSTANCE);
        event.registerServerCommand(SetTeraTypeCommand.INSTANCE);
        event.registerServerCommand(LookTeraTypeCommand.INSTANCE);
    }
}

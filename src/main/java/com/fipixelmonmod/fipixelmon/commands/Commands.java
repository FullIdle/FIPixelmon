package com.fipixelmonmod.fipixelmon.commands;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class Commands {
    public static void register(FMLServerStartingEvent event) {
        event.registerServerCommand(PokeAbilityReload.INSTANCE);
        event.registerServerCommand(MoveEffectReload.INSTANCE);
    }
}

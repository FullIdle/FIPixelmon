package com.fipixelmonmod.fipixelmon.common.api.events;

import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

@Getter
public class CloseScreenEvent extends Event {
    private final EntityPlayer player;
    private final EnumGuiScreen guiScreen;

    public CloseScreenEvent(EntityPlayer player, EnumGuiScreen guiScreen){
        this.player = player;
        this.guiScreen = guiScreen;
    }
}

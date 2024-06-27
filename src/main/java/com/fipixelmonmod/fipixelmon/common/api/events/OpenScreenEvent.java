package com.fipixelmonmod.fipixelmon.common.api.events;

import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

@Getter
public class OpenScreenEvent extends Event {
    private final EntityPlayer player;
    private final EnumGuiScreen gui;

    public OpenScreenEvent(EntityPlayer player, EnumGuiScreen gui){
        this.player = player;
        this.gui = gui;
    }
}

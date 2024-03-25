package com.pixelmonmod.pixelmon.tools;

import com.pixelmonmod.pixelmon.enums.EnumMovement;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IMovementHandler {
   void handleMovement(EntityPlayerMP var1, EnumMovement[] var2);
}

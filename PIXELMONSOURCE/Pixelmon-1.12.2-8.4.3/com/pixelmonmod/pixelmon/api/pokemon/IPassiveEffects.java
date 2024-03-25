package com.pixelmonmod.pixelmon.api.pokemon;

import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IPassiveEffects {
   void tick(EntityPlayerMP var1, PlayerPartyStorage var2);
}

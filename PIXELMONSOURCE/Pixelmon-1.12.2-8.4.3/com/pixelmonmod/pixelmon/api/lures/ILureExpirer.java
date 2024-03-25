package com.pixelmonmod.pixelmon.api.lures;

import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public interface ILureExpirer {
   void tick(EntityPlayerMP var1, PlayerPartyStorage var2, ItemStack var3);
}

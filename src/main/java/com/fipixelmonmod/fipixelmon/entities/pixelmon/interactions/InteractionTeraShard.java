package com.fipixelmonmod.fipixelmon.entities.pixelmon.interactions;

import com.fipixelmonmod.fipixelmon.helper.PokemonHelper;
import com.fipixelmonmod.fipixelmon.items.ItemTeraShard;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * 太晶碎块交互
 */
public class InteractionTeraShard implements IInteraction {
    public static final InteractionTeraShard INSTANCE = new InteractionTeraShard();

    @Override
    public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemTeraShard) || hand == EnumHand.OFF_HAND || itemStack.getCount() < 50) return false;
        val pokemonData = entityPixelmon.getPokemonData();
        if (!pokemonData.getOwnerPlayerUUID().equals(player.getUniqueID())) return false;
        val teraType = ((ItemTeraShard) itemStack.getItem()).getType();
        val old = PokemonHelper.getTeraType(pokemonData);
        if (old != null && old.equals(teraType)) {
            ChatHandler.sendChat(player, "%s 已经是太晶 %s 属性了", pokemonData.getDisplayName(), teraType.name());
            return true;
        }
        PokemonHelper.setTeraType(pokemonData,teraType);
        ChatHandler.sendChat(player, "%s 的属性已经变成了 %s", pokemonData.getDisplayName(), teraType.name());
        itemStack.shrink(50);
        return true;
    }
}

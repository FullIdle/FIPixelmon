package com.fipixelmonmod.fipixelmon.helper;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.listener.SendoutListener;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.WeakHashMap;

public class PixelHelper {
    public static void selectPixelmon(int index) {
        if (ClientStorageManager.party.countAll() == 0 || ClientStorageManager.party.countPokemon() == 0) return;
        index = Math.min(5, Math.max(0, index));
        val pokemon = ClientStorageManager.party.get(index);
        if (pokemon == null || pokemon.isEgg()) return;
        GuiPixelmonOverlay.selectedPixelmon = index;
    }

    public static int getSelectedPixelmon() {
        return GuiPixelmonOverlay.selectedPixelmon;
    }

    public static boolean isRelease(int slot) {
        val world = Minecraft.getMinecraft().world;
        if (world == null) return false;
        val poke = getPartyPoke(slot);
        if (poke == null) return false;
        return SendoutListener.isInWorld(poke.getUUID(), world);
    }

    public static ResourceLocation getSpriteRL(int slot) {
        return getSpriteRL(getPartyPoke(slot));
    }
    
    public static Pokemon getPartyPoke(int slot) {
        return getParty().get(slot);
    }

    public static Minecraft getMc() {
        return Minecraft.getMinecraft();
    }

    public static ResourceLocation getSpriteRL(Pokemon pokemon) {
        if (pokemon == null) return null;

        if (pokemon.isEgg()) {
            return GuiResources.getEggSprite(pokemon.getSpecies(), pokemon.getEggCycles());
        }

        if (pokemon.getFormEnum() == EnumSpecial.Online && pokemon.getOwnerPlayerUUID() != null) {
            PlayerExtraDataStore.get(pokemon.getOwnerPlayerUUID()).checkPokemon(pokemon);
        }

        ResourceLocation rl = GuiResources.getPokemonSprite(pokemon.getSpecies(), pokemon.getForm(), pokemon.getGender(), pokemon.getCustomTexture(), pokemon.isShiny());
        val weakSpriteExistenceCheck = (WeakHashMap<ResourceLocation, Boolean>) ReflectionHelper.getPrivateValue(GuiHelper.class, null, "weakSpriteExistenceCheck");
        if (!weakSpriteExistenceCheck.containsKey(rl)) {
            weakSpriteExistenceCheck.put(rl, Pixelmon.proxy.resourceLocationExists(rl));
        }

        boolean exists = weakSpriteExistenceCheck.get(rl) == Boolean.TRUE;
        if (!exists)
            rl = GuiResources.getPokemonSprite(pokemon.getSpecies(), ((IEnumForm) pokemon.getSpecies().getDefaultForms().get(0)).getForm(), pokemon.getGender(), "", pokemon.isShiny());
        return rl;
    }

    public static PlayerPartyStorage getParty() {
        return ClientStorageManager.party;
    }

    public static PCStorage getPC() {
        return ClientStorageManager.openPC;
    }

    public static EntityPixelmon getPixelEntity(int slot) {
        return getPixelEntity(getPartyPoke(slot));
    }

    public static EntityPixelmon getPixelEntity(Pokemon pokemon) {
        if (pokemon == null) return null;
        val ep = new EntityPixelmon(getMc().world);
        ep.setPokemon(pokemon);
        return ep;
    }
}

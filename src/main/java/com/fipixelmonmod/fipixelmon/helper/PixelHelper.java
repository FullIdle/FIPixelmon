package com.fipixelmonmod.fipixelmon.helper;

import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import lombok.val;

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
}

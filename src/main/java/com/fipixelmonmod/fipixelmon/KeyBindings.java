package com.fipixelmonmod.fipixelmon;

import com.fipixelmonmod.fipixelmon.helper.PixelHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.LinkedHashMap;
import java.util.Map;

public class KeyBindings {
    public static final Map<KeyBinding, Integer> PARTY_SELECT_KEY;

    public static void register() {
        for (KeyBinding value : PARTY_SELECT_KEY.keySet()) ClientRegistry.registerKeyBinding(value);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        for (Map.Entry<KeyBinding, Integer> entry : PARTY_SELECT_KEY.entrySet())
            if (entry.getKey().isPressed()) {
                PixelHelper.selectPixelmon(entry.getValue());
                return;
            }
    }

    static {
        PARTY_SELECT_KEY = new LinkedHashMap<>();
        for (int i = 0; i < 6; i++) {
            PARTY_SELECT_KEY.put(new KeyBinding(
                    "key.fipixelmon.party_select_" + i,
                    Keyboard.getKeyIndex(String.valueOf(i + 1)),
                    "key.categories.fipixelmon"
            ), i);
        }
    }
}

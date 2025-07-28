package com.fipixelmonmod.fipixelmon.helper;

import com.fipixelmonmod.fipixelmon.enums.EnumTeraType;
import com.fipixelmonmod.fipixelmon.items.ItemTeraShard;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;

import java.util.EnumMap;
import java.util.Objects;

public class ItemHelper {
    public static final EnumMap<EnumTeraType, Item> TERA_SHARD_MAP = new EnumMap<>(EnumTeraType.class);

    static {
        for (EnumTeraType type : EnumTeraType.VALUE) TERA_SHARD_MAP.put(type, new ItemTeraShard(type));
    }

    public static void registerTeraShardItems(RegistryEvent.Register<Item> event) {
        for (Item item : TERA_SHARD_MAP.values()) event.getRegistry().register(item);
    }

    public static void registerTeraShardRenderers() {
        for (Item item : TERA_SHARD_MAP.values())
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
    }
}

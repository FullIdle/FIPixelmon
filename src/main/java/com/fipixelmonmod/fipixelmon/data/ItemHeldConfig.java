package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import lombok.SneakyThrows;
import lombok.val;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemHeldConfig {
    //itemName -> Value
    public static final Map<String, ExtendClassConfig> extendClassConfigMap;
    public static final Map<String, ItemHeld> extraHeldItemsMap;
    public static EnumHeldItems ENUM_HELD_ITEMS;

    static {
        extendClassConfigMap = new HashMap<>();
        extraHeldItemsMap = new HashMap<>();

        val logger = FIPixelmon.logger;
        val files = FIPixelmon.heldItemsFolder.listFiles();
        val packageName = "com.pixelmonmod.pixelmon.items.heldItems.fipixelmon";
        if (files != null) for (File file : files) {
            val fileName = file.getName();
            if (!fileName.endsWith(".js")) continue;
            val config = new ExtendClassConfig(file, packageName, ItemHeld.class);
            val itemNameObj = Objects.requireNonNull(config.getGlobal("itemName"), "itemName cannot be null");
            val name = itemNameObj.toString();

            if (!name.equals(name.toLowerCase()))
                throw new IllegalArgumentException("ItemHeld NAME cannot contain uppercase: '" + itemNameObj + "'");

            extendClassConfigMap.put(name, config);
            logger.info("Loaded ItemHeld {}", itemNameObj);
        }
    }

    @SneakyThrows
    public static void init(EnumHeldItems element) {
        ENUM_HELD_ITEMS = element;

        for (Map.Entry<String, ExtendClassConfig> entry : extendClassConfigMap.entrySet()) {
            val name = entry.getKey();
            val clazz = ((Class<? extends ItemHeld>) entry.getValue().getRepresentedClass());
            val constructor = clazz.getConstructor(EnumHeldItems.class, String.class);
            val itemHeld = constructor.newInstance(ENUM_HELD_ITEMS, name);
            extraHeldItemsMap.put(name, itemHeld);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends IForgeRegistryEntry<T>> void registerAll(IForgeRegistry<T> registry) {
        for (ItemHeld value : extraHeldItemsMap.values()) {
            registry.register(((T) value));
        }
    }
}

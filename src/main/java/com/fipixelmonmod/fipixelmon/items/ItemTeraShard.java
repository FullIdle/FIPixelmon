package com.fipixelmonmod.fipixelmon.items;

import com.fipixelmonmod.fipixelmon.enums.EnumTeraType;
import com.pixelmonmod.pixelmon.items.PixelmonItem;
import lombok.Getter;

@Getter
public class ItemTeraShard extends PixelmonItem {
    private final EnumTeraType type;

    public ItemTeraShard(EnumTeraType type) {
        super(type.getName().toLowerCase() + "_tera_shard");
        this.type = type;
        maxStackSize = 50;
    }
}

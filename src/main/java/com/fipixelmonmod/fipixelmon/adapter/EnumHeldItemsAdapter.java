package com.fipixelmonmod.fipixelmon.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

import java.lang.reflect.Type;

public class EnumHeldItemsAdapter implements JsonDeserializer<EnumHeldItems> {
    public static final EnumHeldItemsAdapter INSTANCE = new EnumHeldItemsAdapter();

    @Override
    public EnumHeldItems deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return EnumHeldItems.valueOf(json.getAsString());
    }
}

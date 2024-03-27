package com.fipixelmonmod.fipixelmon.common.adapter;

import com.fipixelmonmod.fipixelmon.common.data.pokemon.EnumForm;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class EnumFormAdapter implements JsonDeserializer<EnumForm> {
    public static final EnumFormAdapter INSTANCE = new EnumFormAdapter();

    @Override
    public EnumForm deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return EnumForm.valueOf(jsonElement.getAsString());
    }
}

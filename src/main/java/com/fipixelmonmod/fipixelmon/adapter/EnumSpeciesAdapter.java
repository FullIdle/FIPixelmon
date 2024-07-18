package com.fipixelmonmod.fipixelmon.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.lang.reflect.Type;

public class EnumSpeciesAdapter implements JsonDeserializer<EnumSpecies> {
    public static final EnumSpeciesAdapter INSTANCE = new EnumSpeciesAdapter();

    @Override
    public EnumSpecies deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String name = jsonElement.getAsString();
        return EnumSpecies.getFromNameAnyCase(name);
    }
}

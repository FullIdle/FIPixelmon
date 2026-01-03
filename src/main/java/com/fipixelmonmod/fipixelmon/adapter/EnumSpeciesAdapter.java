package com.fipixelmonmod.fipixelmon.adapter;

import com.google.gson.*;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.val;

import java.lang.reflect.Type;

public class EnumSpeciesAdapter implements JsonDeserializer<EnumSpecies> {
    public static final EnumSpeciesAdapter INSTANCE = new EnumSpeciesAdapter();

    @Override
    public EnumSpecies deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String name = jsonElement.getAsString();
        System.out.println("name = " + name);
        try {
            return EnumSpecies.valueOf(name);
        } catch (IllegalArgumentException e) {
            val none = EnumSpecies.getFromNameAnyCase(name);
            if (none == null) throw new JsonParseException("Invalid EnumSpecies: " + name);
            return none;
        }
    }
}

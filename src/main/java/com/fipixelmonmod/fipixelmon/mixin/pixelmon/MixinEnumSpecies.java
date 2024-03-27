package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.fipixelmonmod.fipixelmon.common.data.pokemon.PokemonConfig;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.SneakyThrows;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


@Mixin(value = EnumSpecies.class,remap = false)
public class MixinEnumSpecies {
    @Mutable
    @Shadow @Final public static EnumSpecies[] LEGENDARY_ENUMS;
    @Shadow @Final private int nationalDex;

    @Shadow
    @Mutable
    @Final
    private static EnumSpecies[] $VALUES;

    @Shadow @Final private static EnumSpecies[] VALUES;

    @SneakyThrows
    @Inject(method = "<clinit>",
            at = @At(value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/EnumSpecies;VALUES:[Lcom/pixelmonmod/pixelmon/enums/EnumSpecies;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            remap = false)
    private static void registerEnumSpecies(CallbackInfo ci){
        File data = FIPixelmon.fiPixelmonFolder;
        File pokemonFolder = new File(data, "pokemon");
        File[] files = pokemonFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                PokemonConfig config = FIPixelmon.GSON.fromJson(new FileReader(file), PokemonConfig.class);
                config.inject();
            }
        }
    }

    @Inject(method = "<clinit>",
            at = @At("TAIL"),
            remap = false)
    private static void cliTail(CallbackInfo ci){
        ArrayList<EnumSpecies> list = Lists.newArrayList(LEGENDARY_ENUMS);
        list.addAll(Cache.extraLegendEnumSpecies);
        LEGENDARY_ENUMS = list.toArray(new EnumSpecies[0]);
    }
}
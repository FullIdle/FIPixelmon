package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
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
import java.util.Map;


@Mixin(value = EnumSpecies.class, remap = false)
public abstract class MixinEnumSpecies {
    @Mutable
    @Shadow
    @Final
    public static EnumSpecies[] LEGENDARY_ENUMS;
    @Shadow
    @Final
    private int nationalDex;

    @Shadow
    @Mutable
    @Final
    private static EnumSpecies[] $VALUES;

    @Shadow
    @Final
    private static EnumSpecies[] VALUES;

    @Shadow private static ListMultimap<EnumSpecies, IEnumForm> formList;

    @SneakyThrows
    @Inject(method = "<clinit>",
            at = @At(value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/EnumSpecies;VALUES:[Lcom/pixelmonmod/pixelmon/enums/EnumSpecies;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            remap = false)
    private static void registerEnumSpecies(CallbackInfo ci) {
        File[] files = FIPixelmon.pokemonFolder.listFiles();
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
    private static void cliTail(CallbackInfo ci) {
        ArrayList<EnumSpecies> list = Lists.newArrayList(LEGENDARY_ENUMS);
        for (Map.Entry<EnumSpecies, PokemonConfig> entry : PokemonConfig.extraPokemonConfig.entrySet()) {
            if (entry.getValue().isLegendary()) {
                list.add(entry.getValue().getSpecies());
            }
        }
        LEGENDARY_ENUMS = list.toArray(new EnumSpecies[0]);
    }

    @Inject(method = "<clinit>",
            at = @At("TAIL"),
            remap = false)
    private static void formsRegister(CallbackInfo ci) {
        formList = MultimapBuilder.enumKeys(EnumSpecies.class).arrayListValues(1).build(formList);
        for (Map.Entry<EnumSpecies, PokemonConfig> entry : PokemonConfig.extraPokemonConfig.entrySet()) {
            if (entry.getValue().isReplace()) {
                formList.removeAll(entry.getKey());
            }
            for (IEnumForm form : entry.getValue().getEnumForm()) {
                formList.put(entry.getKey(),form);
            }
        }
        formList = Multimaps.unmodifiableListMultimap(formList);
    }
}
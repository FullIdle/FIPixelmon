package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.pixelmonmod.pixelmon.enums.EnumPokemonModel;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.SneakyThrows;
import net.minecraftforge.common.util.EnumHelper;
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
                JsonObject json = FIPixelmon.GSON.fromJson(new FileReader(file), JsonObject.class);
                String name = json.get("name").getAsString();
                int dex = json.get("dex").getAsInt();
                boolean legendary = json.get("legendary").getAsBoolean();
                String model = json.get("model").getAsString();

                EnumSpecies es = EnumHelper.addEnum(EnumSpecies.class, name, new Class<?>[]{int.class, String.class}, dex, name);
                Cache.extraEnumSpecies.add(es);
                if (legendary) {
                    Cache.extraLegendEnumSpecies.add(es);
                }

                Cache.extraPokemonModels.put(es,EnumHelper.addEnum(EnumPokemonModel.class,name,new Class<?>[]{String.class},
                        "models"+((model.startsWith("/")?"":"/")+model)));

                FIPixelmon.logger.info("REGISTERED ENUM:{name:" + name + ",dex:" + dex + "}");
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
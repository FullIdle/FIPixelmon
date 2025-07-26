package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.data.PokemonConfig;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;


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
    private static ListMultimap<EnumSpecies, IEnumForm> formList;

    @SneakyThrows
    @Inject(method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/EnumSpecies;$VALUES:[Lcom/pixelmonmod/pixelmon/enums/EnumSpecies;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            remap = false)
    private static void registerEnumSpecies(CallbackInfo ci) {
        File[] files = FIPixelmon.pokemonFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    PokemonConfig config = FIPixelmon.GSON.fromJson(new FileReader(file), PokemonConfig.class);
                    config.inject();
                }
            }
        }
        File[] list = FIPixelmon.fiPixelmonFolder.listFiles();
        if (list != null) {
            for (File file : list) {
                if (!FileHelper.isZip(file)) continue;
                ZipFile zipFile = new ZipFile(file);
                for (String path : FileHelper.getZipFileList(zipFile, "pokemon")) {
                    if (path.endsWith(".json")) {
                        InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(zipFile.getEntry(path)));
                        PokemonConfig config = FIPixelmon.GSON.fromJson(reader, PokemonConfig.class);
                        config.setFromZip(file);
                        config.inject();
                        reader.close();
                    }
                }
                zipFile.close();
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
        EnumSpecies species;
        List<IEnumForm> forms;
        List<IEnumForm> temp;
        boolean isCovered;
        for (Map.Entry<EnumSpecies, PokemonConfig> entry : PokemonConfig.extraPokemonConfig.entrySet()) {
            species = entry.getKey();
            if (isCovered = (entry.getValue().isEdit() && entry.getValue().isEditReplace())) {
                formList.removeAll(species);
            }
            for (IEnumForm form : entry.getValue().getEnumForm()) {
                formList.put(species, form);
            }
            //检查是否该精灵是否拥有形态
            if (formList.containsKey(species)) {
                //拥有形态则获取处理后的所有形态并算出非临时形态的数量
                temp = Lists.newArrayList(forms = formList.get(species));
                temp.removeIf(IEnumForm::isTemporary);

                //如果是覆盖的情况下,且没有NoForm则增加一个默认形态
                if (isCovered && !temp.contains(EnumNoForm.NoForm))
                    forms.add(0, EnumNoForm.NoForm);

                //排除已FIP增加的形态(因为上面的覆盖判断需要带上FIP增加的形态进行判断)
                temp.removeIf(form -> form instanceof EnumForm);

                //全是临时形态的时候增加一个非临时形态的普通形态
                if (temp.isEmpty()) forms.add(0, EnumNoForm.NoForm);
                continue;
            }
            //没有形态则增加一个默认形态
            System.out.println(species.getNationalPokedexInteger()+"增加了默认形态");
            formList.put(species, EnumNoForm.NoForm);
        }
        formList = Multimaps.unmodifiableListMultimap(formList);
    }
}
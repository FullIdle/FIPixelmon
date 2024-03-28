package com.fipixelmonmod.fipixelmon.common.data.pokemon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.pixelmonmod.pixelmon.enums.EnumPokemonModel;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.util.EnumHelper;

@Getter
@Setter
public class PokemonConfig {
    private String name = null;
    private int dex = -1;
    private boolean legendary = false;
    private String model = null;
    private String flyingModel = null;
    private EnumForm.FormData[] forms = null;

    public void inject() {
        EnumSpecies es = EnumHelper.addEnum(EnumSpecies.class, name, new Class<?>[]{int.class, String.class}, dex, name);
        Cache.extraPokemonConfig.put(es, this);
        Cache.extraEnumSpecies.add(es);
        if (legendary) {
            Cache.extraLegendEnumSpecies.add(es);
        }
        //模型//形态模型
        if (model != null) {
            EnumHelper.addEnum(EnumPokemonModel.class, name, new Class<?>[]{String.class},
                    formatModelPath(model));
        }
        if (flyingModel != null) {
            EnumHelper.addEnum(EnumPokemonModel.class, name + "Flying", new Class<?>[]{String.class},
                    formatModelPath(flyingModel));
        }
        if (forms != null && forms.length >= 1) {
            for (EnumForm.FormData formData : this.forms) {
                EnumForm enumForm = EnumHelper.addEnum(EnumForm.class, formData.getFormName(),
                        new Class<?>[]{EnumForm.FormData.class}, formData);
                Cache.extraForm.put(es,enumForm);
                formData.setEnumForm(enumForm);
            }
        }

        FIPixelmon.logger.info("REGISTERED ENUM [name:" + name + ",dex:" + dex + "]");
    }

    private static String formatModelPath(String path) {
        return "models" + ((path.startsWith("/") ? "" : "/") + path);
    }

    public EnumPokemonModel getModel() {
        try {
            return EnumPokemonModel.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public EnumPokemonModel getFlyingModel() {
        try {
            return EnumPokemonModel.valueOf(name + "Flying");
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PokemonConfig {
    public static final Map<EnumSpecies, PokemonConfig> extraPokemonConfig = new HashMap<>();
    private String name = null;
    private int dex = -1;
    private boolean legendary = false;
    private String model = null;
    private String flyingModel = null;
    private EnumForm.FormData[] forms = new EnumForm.FormData[]{};
    private boolean editReplace = false;

    //geo
    private String geoModel = null;
    private String geoAnimation = null;

    //transient
    transient private EnumForm[] enumForm;
    transient private EnumSpecies species;
    transient private File fromZip = null;
    transient private boolean isEdit = false;

    //在EnumSpecies的$VAULT被初始化并被第一次条用之前进行注入
    public void inject() {
        String info;
        EnumSpecies fromDex = getFromDex(this.dex);
        //判断是否存在这个编号的精灵
        if (fromDex == null) {
            //进行注册
            this.species = EnumHelper.addEnum(EnumSpecies.class, this.name, new Class<?>[]{int.class, String.class}, this.dex, this.name);
            info = "REGISTERED ENUM [name:{},dex:{}]";
        } else {
            //进行修改
            this.species = fromDex;
            if (!species.name.equals(this.name))
                ReflectionHelper.setPrivateValue(EnumSpecies.class, this.species, this.name, "name", "name");
            this.isEdit = true;
            info = "EDIT ENUM [name:{},dex:{}]";
        }
        //形态
        ArrayList<EnumForm> EnumForms = new ArrayList<>();

        if (this.forms != null && this.forms.length >= 1) {
            for (EnumForm.FormData formData : this.forms) {
                EnumForm enumForm = EnumHelper.addEnum(EnumForm.class, formData.getFormName(),
                        new Class<?>[]{EnumForm.FormData.class}, formData);
                formData.setEnumForm(enumForm);
                EnumForms.add(enumForm);
            }
        }
        this.enumForm = EnumForms
                .toArray(new EnumForm[0]);
        FIPixelmon.logger.info(info, this.name, this.dex);
        extraPokemonConfig.put(this.species, this);
    }

    public boolean isFromZip() {
        return this.fromZip != null;
    }

    //旧的获取方法
    public static EnumSpecies getFromDex(int nationalDex) {
        EnumSpecies[] VALUES = EnumSpecies.values();

        if (nationalDex < 0) {
            return null;
        } else if (nationalDex < VALUES.length && VALUES[nationalDex].getNationalPokedexInteger() == nationalDex) {
            return VALUES[nationalDex];
        } else {
            for(int i = VALUES.length - 1; i >= 0; --i) {
                if (VALUES[i].getNationalPokedexInteger() == nationalDex) {
                    return VALUES[i];
                }

                if (VALUES[i].getNationalPokedexInteger() < nationalDex) {
                    break;
                }
            }

            return null;
        }
    }
}

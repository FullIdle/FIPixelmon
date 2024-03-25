package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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
    transient private boolean isReplace = false;
    transient private IEnumForm[] enumForm;
    transient private EnumSpecies species;

    public void inject() {
        String info;
        if (this.dex > 905) {
            this.species = EnumHelper.addEnum(EnumSpecies.class, this.name, new Class<?>[]{int.class, String.class}, this.dex, this.name);
            info = "REGISTERED ENUM [name:{},dex:{}]";
        }else{
            this.species = EnumSpecies.values()[this.dex];
            if (!species.name.equals(this.name)) {
                ReflectionHelper.setPrivateValue(EnumSpecies.class,this.species,this.name,"name","name");
                isReplace = true;
            }
            info = "EDIT ENUM [name:{},dex:{}]";
        }
        ArrayList<IEnumForm> iEnumForms = new ArrayList<>();
        if (this.model != null) iEnumForms.add(EnumNoForm.NoForm);

        if (this.forms != null && this.forms.length >= 1) {
            for (EnumForm.FormData formData : this.forms) {
                EnumForm enumForm = EnumHelper.addEnum(EnumForm.class, formData.getFormName(),
                        new Class<?>[]{EnumForm.FormData.class}, formData);
                formData.setEnumForm(enumForm);
                iEnumForms.add(enumForm);
            }
        }
        this.enumForm = iEnumForms.toArray(new IEnumForm[0]);
        FIPixelmon.logger.info(info, this.name, this.dex);
        extraPokemonConfig.put(this.species, this);
    }
}

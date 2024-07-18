package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.enums.EnumForm;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.util.EnumHelper;

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
    transient private IEnumForm[] enumForm;
    transient private EnumSpecies species;

    public void inject() {
        this.species = EnumHelper.addEnum(EnumSpecies.class, this.name, new Class<?>[]{int.class, String.class}, dex, name);
        extraPokemonConfig.put(this.species, this);

        ArrayList<IEnumForm> iEnumForms = new ArrayList<>();
        if (this.model != null) iEnumForms.add(EnumNoForm.NoForm);

        if (forms != null && forms.length >= 1) {
            for (EnumForm.FormData formData : this.forms) {
                EnumForm enumForm = EnumHelper.addEnum(EnumForm.class, formData.getFormName(),
                        new Class<?>[]{EnumForm.FormData.class}, formData);
                formData.setEnumForm(enumForm);
                iEnumForms.add(enumForm);
            }
        }
        this.enumForm = iEnumForms.toArray(new IEnumForm[0]);
        FIPixelmon.logger.info("REGISTERED ENUM [name:{},dex:{}]", name, dex);
    }
}

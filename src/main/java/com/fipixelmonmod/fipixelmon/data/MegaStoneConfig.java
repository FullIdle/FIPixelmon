package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMegaStone;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.util.EnumHelper;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MegaStoneConfig {
    public static final Map<EnumMegaPokemon, MegaStoneConfig> extraMegaStoneConfig = new HashMap<>();
    public static final Class<?>[] addEnumParamTypes = {EnumSpecies.class, int.class};

    private String name = null;
    private int numMegaForms = -1;
    private int pokemonDex = -1;

    transient public ItemMegaStone item;
    transient public EnumSpecies pokemon;
    transient public EnumMegaPokemon enumMegaPokemon;

    public void inject() {
        this.pokemon = EnumSpecies.getFromDex(this.pokemonDex);
        this.enumMegaPokemon = EnumHelper.addEnum(EnumMegaPokemon.class, this.name, addEnumParamTypes, this.pokemon, this.numMegaForms);
        this.item = new ItemMegaStone(this.name.toLowerCase(), this.numMegaForms, this.pokemon);
        extraMegaStoneConfig.put(this.enumMegaPokemon, this);
        FIPixelmon.logger.info("REGISTERED MEGA STONE [name:{},pokemon:{}]", this.name, this.pokemon.getLocalizedName());
    }
}

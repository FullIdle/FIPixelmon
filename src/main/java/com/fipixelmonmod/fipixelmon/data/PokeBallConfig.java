package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PokeBallConfig {
    public static final Map<EnumPokeballs, PokeBallConfig> extraPokeBallConfig = new HashMap<>();
    public static final Class<?>[] addEnumParamTypes = {int.class, double.class, String.class, int.class, int.class};


    private String name = null;
    private int index = -1;
    private double ballBonus = -1;
    private String filenamePrefix = null;
    private int quantityMade = -1;
    private int breakChance = -1;
    private String guiTex = null;

    transient public ResourceLocation guiTexResourceLocation;
    transient public Item item;

    public void inject() {
        EnumPokeballs enumPokeballs = EnumHelper.addEnum(EnumPokeballs.class, this.name, addEnumParamTypes,
                this.index, this.ballBonus, this.filenamePrefix, quantityMade, breakChance);
        extraPokeBallConfig.put(enumPokeballs, this);
        FIPixelmon.logger.info("REGISTERED BALL [name:{},index:{}]", this.name, this.index);
    }

    public void bindPokeballTexture(){
        Minecraft.getMinecraft().renderEngine.bindTexture(this.guiTexResourceLocation);
    }
}

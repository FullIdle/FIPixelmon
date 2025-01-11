package com.fipixelmonmod.fipixelmon.data;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumCustomModel;
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
    public static final Class<?>[] ballEnumTypes = {int.class, double.class, String.class, int.class, int.class};
    public static final Class<?>[] modelEnumTypes = {String.class};


    private String name = null;
    private int index = -1;
    private double ballBonus = -1;
    private String filenamePrefix = null;
    private int quantityMade = -1;
    private int breakChance = -1;
    private String guiTex = null;
    private String ballModelName = null;
    private String ballModelPath = null;

    transient public ResourceLocation guiTexResourceLocation;
    transient public Item item;
    transient public EnumPokeballs enumPokeballs;
    transient public EnumCustomModel customModel;

    public void inject() {
        this.enumPokeballs = EnumHelper.addEnum(EnumPokeballs.class, this.name, ballEnumTypes,
                this.index, this.ballBonus, this.filenamePrefix, quantityMade, breakChance);
        extraPokeBallConfig.put(this.enumPokeballs, this);
        FIPixelmon.logger.info("REGISTERED BALL [name:{},index:{}]", this.name, this.index);
        this.injectModel();
    }

    public void bindPokeballTexture(){
        Minecraft.getMinecraft().renderEngine.bindTexture(this.guiTexResourceLocation);
    }

    public void injectModel(){
        if (this.ballModelName == null || this.ballModelPath == null) return;
        this.customModel = EnumHelper.addEnum(EnumCustomModel.class, this.ballModelName, modelEnumTypes, this.ballModelPath);
        FIPixelmon.logger.info("REGISTERED BALL MODEL [name:{},path:{}]", this.ballModelName, this.ballModelPath);
    }

    public boolean hasCustomModel(){
        return this.customModel != null;
    }
}
